package ar.edu.unsam.algo3.DTO

import ar.edu.unsam.algo3.domain.*
import java.lang.IllegalArgumentException

data class PuntoDeVentaDTO(
    val id: Int,
    val tipo: String,
    val nombre: String,
    val direccion: String,
    val stockSobres : Int,
    val puntoX: Double,
    val puntoY: Double,
    val pedidosPendientes: Int,
    val precioPorSobre: Double,
    val distancia : Double,
    val tipoDescuento : String,
    val provincia : String,
    val localidad : String,
    val altura : Int

)

fun PuntoDeVenta.toDTO(usuario: Usuario) = PuntoDeVentaDTO(
    id = this.id,
    tipo = this::class.toString().replace("class ar.edu.unsam.algo3.domain.",""),
    nombre = nombre,
    direccion = direccion.calle + " " + direccion.altura.toString() + ", " + direccion.localidad,
    stockSobres = stockSobres,
    puntoX = direccion.ubiGeografica.x,
    puntoY = direccion.ubiGeografica.y,
    pedidosPendientes = this.pedidosPendientes.size,
    precioPorSobre = importeACobrar(1,usuario.direccion),
    distancia = distanciaConUsuario(usuario.direccion),
    tipoDescuento = if (this is Supermercado) tipoDescuentoSupermercado(this.tipoDescuento) else "",
    provincia = direccion.provincia,
    localidad = direccion.localidad,
    altura = direccion.altura

)

private fun tipoDescuentoSupermercado(tipoDescuento: DescuentoSupermercado): String {
    return when (tipoDescuento) {
        is DescuentoJueves -> "DescuentoJueves"
        is DescuentoPrimerosDias -> "DescuentoPrimerosDias"
        is DescuentoCompraGrande -> "DescuentoCompraGrande"
        is SinDescuento -> "SinDescuento"
        else -> ""
    }

}