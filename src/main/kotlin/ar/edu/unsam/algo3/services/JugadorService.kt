package ar.edu.unsam.algo3.services

import ar.edu.unsam.algo3.DTO.*
import ar.edu.unsam.algo3.domain.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class JugadorService(
    @Autowired val repoJugadores: RepositorioJugadores,
    @Autowired val repositorioSelecciones: RepositorioSelecciones,
    @Autowired val repositorioFiguritas: RepositorioFiguritas
) {
    fun jugadoresToDTO (jugadores: List<Jugador>): List<JugadorDTO> = jugadores.map { jugador -> jugador.toDTO() }
    fun getJugadores(nombre: String?): List<JugadorDTO> {
        return if(nombre != null){
            jugadoresToDTO(repoJugadores.search(nombre))
        }else{
            jugadoresToDTO(repoJugadores.search(""))
        }
    }

    fun crearJugador(jugadorCreado: JugadorDTO): JugadorDTO{
        val jugadorNuevo = Jugador(
            nombre = jugadorCreado.nombre,
            apellido = jugadorCreado.apellido,
            altura = jugadorCreado.altura,
            peso = jugadorCreado.peso,
            anioDeDebut = LocalDate.of(2000, 5, 5),
            cotizacion = jugadorCreado.cotizacion,
            fechaDeNacimiento = jugadorCreado.fechaNacimiento,
            lider = jugadorCreado.lider,
            nroDeCamiseta = jugadorCreado.nroDeCamiseta,
            posicion = obtenerPosicion(jugadorCreado.posicion),
            seleccion = obtenerSeleccion(jugadorCreado.seleccion),
        )

        repoJugadores.create(jugadorNuevo)

        return jugadorNuevo.toDTO()
    }

    fun eliminarJugador(id: Int){
        val jugadorABorrar = repoJugadores.getById(id)
        if(repositorioFiguritas.contieneAlgunaAlJugador(jugadorABorrar)){
            throw JugadorException("El jugador no puede borrarse por que se encuentra en una figurita")
        }else{
            repoJugadores.delete(jugadorABorrar)
        }
    }

    fun actualizarJugador(id: Int, jugadorActualizado: JugadorDTO): Jugador {
        val jugadorExistente = repoJugadores.getById(id)
        val posicionJugador = obtenerPosicion(jugadorActualizado.posicion)
        val seleccionJugador = obtenerSeleccion(jugadorActualizado.seleccion)
        jugadorExistente.apply {
            nombre = jugadorActualizado.nombre
            apellido = jugadorActualizado.apellido
            fechaDeNacimiento = jugadorActualizado.fechaNacimiento
            nroDeCamiseta = jugadorActualizado.nroDeCamiseta
            peso = jugadorActualizado.peso
            altura = jugadorActualizado.altura
            cotizacion = jugadorActualizado.cotizacion
            seleccion = seleccionJugador
            lider = jugadorActualizado.lider
            posicion = posicionJugador
        }

        repoJugadores.update(jugadorExistente)

        return jugadorExistente
    }

    fun obtenerSeleccion(nombreSeleccion: String): Seleccion{
        return repositorioSelecciones.search(nombreSeleccion).get(0)
    }
    fun obtenerPosicion(posicion: String): Posicion {
        val PosicionMap: Map<String, Posicion> = mapOf(
            "Delantero" to Delantero,
            "Mediocampista" to MedioCampista,
            "Defensor" to Defensor,
            "Arquero" to Arquero
        )

        return PosicionMap[posicion] ?: throw IllegalArgumentException("Posicion no valida: $posicion")
    }
}