package ar.edu.unsam.algo3.services

import ar.edu.unsam.algo3.DTO.*
import ar.edu.unsam.algo3.domain.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FiguritaService(@Autowired val repoFiguritas: RepositorioFiguritas, @Autowired val repoUsuarios: RepositorioUsuarios , @Autowired val repoJugadores : RepositorioJugadores) {

    fun filtrarFiguritas(
        nombre: String?,
        desdeValor: Int?,
        hastaValor: Int?,
        onFire: String?,
        esPromesa: String?
    ): List<FiguritaDTO> {
        return repoFiguritas.filtrarFiguritas(nombre, desdeValor, hastaValor, onFire, esPromesa)
    }

    fun filtrarFiguritasCedidas(
        nombre: String?,
        desdeValor: Int?,
        hastaValor: Int?,
        onFire: String?,
        esPromesa: String?,
        idUsuario: Int
    ): List<FiguritaDTO> {
        generarFiguritasCedidas(idUsuario)
        return repoUsuarios.filtrarFiguritas(nombre, desdeValor, hastaValor, onFire, esPromesa)

    }

    fun generarFiguritasCedidas(idUsuario: Int) {
        repoUsuarios.generarFiguritasCedidasPara(idUsuario)
    }

    fun encontrarFiguritaDetailsPorId(id: Int, usuario: UsuarioDTODetalles): FiguritaDetailsDTO {
        return repoFiguritas.getById(id).toDTODetails(usuario)
    }

    fun eliminarFigurita(figurita: Figurita) {
        if (repoUsuarios.figuritaRepetidaOFaltanteEnAlgunUsuario(figurita)) {
            throw FiguritaException("La figurita esta como faltante o repetida para algun usuario")
        } else {
            repoFiguritas.delete(figurita)
        }
    }

    fun getByID(id: Int): Figurita = repoFiguritas.getById(id)

    fun actualizarFigurita(figuritaParaActualizar: Figurita, figuritaActualizada: FiguritaActualizarDTO) {
        val nivelDeImpresionActualizado = obtenerNivelDeImpresion(figuritaActualizada.nivelDeImpresion)
        val jugadorActualizado = repoJugadores.getById(figuritaActualizada.nuevoJugador)
        figuritaParaActualizar.apply {
            numero = figuritaActualizada.numero
            nivelDeImpresion = nivelDeImpresionActualizado
            estaOnFire = figuritaActualizada.onFire
            jugador.nombre = jugadorActualizado.nombre
            jugador.apellido = jugadorActualizado.apellido
            jugador.fechaDeNacimiento = jugadorActualizado.fechaDeNacimiento
            jugador.nroDeCamiseta = jugadorActualizado.nroDeCamiseta
            jugador.peso = jugadorActualizado.peso
            jugador.altura = jugadorActualizado.altura
            jugador.cotizacion = jugadorActualizado.cotizacion
            jugador.seleccion = jugadorActualizado.seleccion
            jugador.lider = jugadorActualizado.lider
            jugador.posicion = jugadorActualizado.posicion
            jugador.id = jugadorActualizado.id
        }
        repoFiguritas.update(figuritaParaActualizar)
    }

    fun crearJugador(figuritaNueva: FiguritaActualizarDTO) {
        val jugadorNuevo = repoJugadores.getById(figuritaNueva.nuevoJugador)
        val nivelDeImpresionNuevo = obtenerNivelDeImpresion(figuritaNueva.nivelDeImpresion)
        val nuevaFigurita = Figurita(
                numero = figuritaNueva.numero,
                nivelDeImpresion = nivelDeImpresionNuevo,
                estaOnFire = figuritaNueva.onFire,
                jugador = jugadorNuevo
        )
        repoFiguritas.create(nuevaFigurita)
    }

    fun obtenerNivelDeImpresion(nivelDeImpresion: String): Impresion {
        nivelDeImpresion.lowercase()
        val impresionMap: Map<String, Impresion> = mapOf(
            "baja" to Impresion.BAJA,
            "media" to Impresion.MEDIA,
            "alta" to Impresion.ALTA,
        )
        return impresionMap[nivelDeImpresion]
            ?: throw IllegalArgumentException("Impresion no valida: $nivelDeImpresion")

    }
}