package ar.edu.unsam.algo3.services

import ar.edu.unsam.algo3.DTO.*
import ar.edu.unsam.algo3.domain.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class SeleccionService(
    @Autowired val repositorioSelecciones: RepositorioSelecciones,
    @Autowired val repositorioJugadores: RepositorioJugadores
) {

    fun seleccionesToDTO(selecciones: List<Seleccion>): List<SeleccionDTO> = selecciones.map { seleccion -> seleccion.toDTO() }

    fun getSelecciones(nombre: String?): List<SeleccionDTO> {
        return if(nombre != null){
            seleccionesToDTO(repositorioSelecciones.search(nombre))
        }else{
            seleccionesToDTO(repositorioSelecciones.search(""))
        }
    }

    fun crearSeleccion(seleccionCreada: SeleccionDTO): SeleccionDTO {
        val seleccionNueva = Seleccion(
            pais = seleccionCreada.pais,
            copasDelMundo = seleccionCreada.copasDelMundo,
            confederacion = obtenerConfederacion(seleccionCreada.confederacion),
            copasConfederacion = 10
        )

        repositorioSelecciones.create(seleccionNueva)

        return seleccionNueva.toDTO()
    }

    fun actualizarSeleccion(id: Int, seleccionActualizada: SeleccionDTO): Seleccion {
        val seleccionExistente = repositorioSelecciones.getById(id)
        seleccionExistente.apply {
            pais = seleccionActualizada.pais
            confederacion = obtenerConfederacion(seleccionActualizada.confederacion)
            copasDelMundo = seleccionActualizada.copasDelMundo
        }

        repositorioSelecciones.update(seleccionExistente)

        return seleccionExistente
    }

    fun eliminarSeleccion(id: Int){
        val seleccionABorrar = repositorioSelecciones.getById(id)
        if(repositorioJugadores.contieneAlgunoALaSeleccion(seleccionABorrar)){
            throw SeleccionException("La seleccion no puede borrarse por que se encuentra asociada a un jugador")
        }else{
            repositorioSelecciones.delete(seleccionABorrar)
        }
    }

    fun obtenerConfederacion(nombreConfederacion: String): Confederacion {
        val confederacionMap: Map<String, Confederacion> = mapOf(
            "CONMEBOL" to Confederacion.CONMEBOL,
            "UEFA" to Confederacion.UEFA,
            "AFC" to Confederacion.AFC,
            "CAF" to Confederacion.CAF,
            "CONCACAF" to Confederacion.CONCACAF,
            "OFC" to Confederacion.OFC
        )
        return confederacionMap[nombreConfederacion] ?: throw IllegalArgumentException("Confederacion no valida: $nombreConfederacion")
    }
}