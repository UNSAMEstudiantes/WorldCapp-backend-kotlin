package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.DTO.*
import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.services.JugadorService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin("*")
class JugadorController(
    @Autowired val repoJugadores: RepositorioJugadores,
    @Autowired val jugadoresService: JugadorService
) {

    @GetMapping("/jugadores")
    fun getJugadores(@RequestParam(required = false) nombre: String?) = jugadoresService.getJugadores(nombre)

    @GetMapping("/jugador/{id}")
    @Operation(summary = "devuelve un jugador por su id")
    fun getJugadorById(@PathVariable id: Int) = repoJugadores.getById(id).toDTO()

    @PutMapping("/editar-jugador/{id}")
    @Operation(summary = "Actualiza un jugador por su ID")
    fun actualizarJugador(@PathVariable id: Int, @RequestBody jugadorDTO: JugadorDTO) {
        jugadoresService.actualizarJugador(id, jugadorDTO)
    }

    @PostMapping("/crear-jugador")
    @Operation(summary ="Crea un jugador")
    fun crearJugador(@RequestBody jugadorCreado: JugadorDTO): JugadorDTO {
        return jugadoresService.crearJugador(jugadorCreado)
    }

    @DeleteMapping("/jugadores/borrar-jugador/{id}")
    @Operation(summary = "Elimina al jugador si no se encuentra en ninguna figurita")
    fun deleteJugador(@PathVariable id: Int){
        jugadoresService.eliminarJugador(id)
    }

}