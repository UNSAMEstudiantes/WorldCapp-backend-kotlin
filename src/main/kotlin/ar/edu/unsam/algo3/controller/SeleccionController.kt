package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.DTO.*
import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.services.SeleccionService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin("*")
class SeleccionController(
    @Autowired val repositorioSelecciones: RepositorioSelecciones,
    @Autowired val seleccionesService: SeleccionService
) {

    @GetMapping("/selecciones")
    fun getSelecciones(@RequestParam(required = false) nombre: String?) = seleccionesService.getSelecciones(nombre)

    @GetMapping("/selecciones/{id}")
    @Operation(summary = "devuelve una seleccion por su id")
    fun getSeleccionById(@PathVariable id: Int) = repositorioSelecciones.getById(id).toDTO()

    @PutMapping("/editar-seleccion/{id}")
    @Operation(summary = "Actualiza una seleccion por su ID")
    fun actualizarSeleccion(@PathVariable id: Int, @RequestBody seleccionDTO: SeleccionDTO) {
        seleccionesService.actualizarSeleccion(id, seleccionDTO)
    }

    @PostMapping("/crear-seleccion")
    @Operation(summary ="Crea una seleccion")
    fun crearSeleccion(@RequestBody seleccionCreada: SeleccionDTO): SeleccionDTO {
        return seleccionesService.crearSeleccion(seleccionCreada)
    }

    @DeleteMapping("/eliminar-seleccion/{id}")
    @Operation(summary = "Elimina la seleccion si no esta asociada a ningun jugador")
    fun deleteSeleccion(@PathVariable id: Int){
        seleccionesService.eliminarSeleccion(id)
    }

}