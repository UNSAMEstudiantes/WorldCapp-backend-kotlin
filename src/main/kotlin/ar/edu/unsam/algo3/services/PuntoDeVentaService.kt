package ar.edu.unsam.algo3.services
import ar.edu.unsam.algo3.DTO.PuntoDeVentaDTO
import ar.edu.unsam.algo3.DTO.toDTO
import ar.edu.unsam.algo3.domain.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.uqbar.geodds.Point
import java.lang.IllegalArgumentException

@Service
class PuntoDeVentaService(
    @Autowired val repoPuntosDeVenta: RepositorioPuntoDeVenta<PuntoDeVenta>,
    @Autowired val repoUsuarios: RepositorioUsuarios
) {
    fun crearPuntoDeVenta(puntoDeVentaNuevo: PuntoDeVentaDTO, idUsuario: Int): PuntoDeVentaDTO {
        val usuario = repoUsuarios.getById(idUsuario)
        val direccionNueva = Direccion(
            puntoDeVentaNuevo.provincia,
            puntoDeVentaNuevo.localidad,
            puntoDeVentaNuevo.direccion,
            puntoDeVentaNuevo.altura,
            ubiGeografica = Point(puntoDeVentaNuevo.puntoX, puntoDeVentaNuevo.puntoY)
        )

        val nuevoPuntoDeVenta = when (puntoDeVentaNuevo.tipo) {
            "Kiosko" -> Kiosko(
                puntoDeVentaNuevo.nombre,
                direccionNueva,
                puntoDeVentaNuevo.stockSobres,
                tieneEmpleados = false
            )

            "Libreria" -> Libreria(
                puntoDeVentaNuevo.nombre,
                direccionNueva,
                puntoDeVentaNuevo.stockSobres
            )


            "Supermercado" -> {
                val tipoDescuento = obtenerTipoDescuento(puntoDeVentaNuevo.tipoDescuento)
                Supermercado(
                    puntoDeVentaNuevo.nombre,
                    direccionNueva,
                    puntoDeVentaNuevo.stockSobres,
                    tipoDescuento
                )
            }

            else -> throw IllegalArgumentException("Tipo de punto de venta no permitido")
        }

        repoPuntosDeVenta.create(nuevoPuntoDeVenta)
        return nuevoPuntoDeVenta.toDTO(usuario)
    }


    fun actualizarPuntoDeVenta(id: Int, puntoDeVentaActualizado: PuntoDeVentaDTO): PuntoDeVenta {
        val puntoDeVentaExistente = repoPuntosDeVenta.getById(id)

        val direccionActualizada = Direccion(
            puntoDeVentaActualizado.provincia,
            puntoDeVentaActualizado.localidad,
            puntoDeVentaActualizado.direccion,
            puntoDeVentaActualizado.altura,
            ubiGeografica = Point(puntoDeVentaActualizado.puntoX, puntoDeVentaActualizado.puntoY)
        )

        when (puntoDeVentaExistente) {
            is Kiosko -> {
                puntoDeVentaExistente.apply {
                    nombre = puntoDeVentaActualizado.nombre
                    direccion = direccionActualizada
                    stockSobres = puntoDeVentaActualizado.stockSobres
                    tieneEmpleados = false

                }
            }
            is Libreria -> {
                puntoDeVentaExistente.apply {
                    nombre = puntoDeVentaActualizado.nombre
                    direccion = direccionActualizada
                    stockSobres = puntoDeVentaActualizado.stockSobres
                }
            }
            is Supermercado -> {
                puntoDeVentaExistente.apply {
                    nombre = puntoDeVentaActualizado.nombre
                    direccion = direccionActualizada
                    stockSobres = puntoDeVentaActualizado.stockSobres
                    tipoDescuento = obtenerTipoDescuento(puntoDeVentaActualizado.tipoDescuento)
                    }
                }
            else -> throw IllegalArgumentException("Tipo de punto de venta no permitido")
        }
        repoPuntosDeVenta.update(puntoDeVentaExistente)
        return puntoDeVentaExistente
    }

    private fun obtenerTipoDescuento(tipoDescuento: String): DescuentoSupermercado {
        val TipoDescuentoMap: Map<String, DescuentoSupermercado> = mapOf(
            "DescuentoJueves" to DescuentoJueves,
            "DescuentoPrimerosDias" to DescuentoPrimerosDias,
            "DescuentoCompraGrande" to DescuentoCompraGrande,
            "SinDescuento" to SinDescuento
        )
        return TipoDescuentoMap[tipoDescuento]
            ?: throw IllegalArgumentException("Tipo de descuento no valido: $tipoDescuento")
    }
}