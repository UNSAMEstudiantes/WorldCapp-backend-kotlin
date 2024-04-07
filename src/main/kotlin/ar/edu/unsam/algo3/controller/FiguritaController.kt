package ar.edu.unsam.algo3.controller
import ar.edu.unsam.algo3.DTO.FiguritaDetailsDTO
import ar.edu.unsam.algo3.DTO.toDTODetalles
import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.services.FiguritaService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ar.edu.unsam.algo3.DTO.*

@RestController
@CrossOrigin("*")
class FiguritaController(
    @Autowired val repoUsuarios: Repositorio<Usuario>,
    @Autowired val figuritaService: FiguritaService
) {

    @GetMapping("/busqueda-figuritas")
    @Operation(summary = "Devuelve las figuritas en busqueda-figuritas, puede recibir filtros")
    fun getFiguritas(
        @RequestParam(required = false) nombre: String?,
        @RequestParam(required = false) desdeValor: Int?,
        @RequestParam(required = false) hastaValor: Int?,
        @RequestParam(required = false) onFire: String?,
        @RequestParam(required = false) esPromesa: String?

    ) = figuritaService.filtrarFiguritas(nombre, desdeValor, hastaValor, onFire, esPromesa)

    @GetMapping("/busqueda-figuritas/{id}")
    @Operation(summary = "Devuelve la figurita con el id correspondiente")
    fun getFiguritaDetailsById(@PathVariable id: Int): FiguritaDetailsDTO {
        val usuario = repoUsuarios.getById(id).toDTODetalles()
        return figuritaService.encontrarFiguritaDetailsPorId(id, usuario)
     }

    @GetMapping("/main-busqueda-figuritas/{id}")
    @Operation(summary = "Devuelve las figuritas, puede recibir filtros")
    fun getFiguritasMain(
        @PathVariable id: Int,
        @RequestParam(required = false) nombre: String?,
        @RequestParam(required = false) desdeValor: Int?,
        @RequestParam(required = false) hastaValor: Int?,
        @RequestParam(required = false) onFire: String?,
        @RequestParam(required = false) esPromesa: String?

    ) = figuritaService.filtrarFiguritasCedidas(nombre, desdeValor, hastaValor, onFire, esPromesa, id)

    @DeleteMapping("/busqueda-figuritas/borrar/{id}")
    @Operation(summary = "elimina la figurita si no esta repetida o faltante en algun usuario")
    fun deleteFigurita(@PathVariable id: Int){
        val figuritaAEliminar = figuritaService.getByID(id)
        figuritaService.eliminarFigurita(figuritaAEliminar)
    }

    @PutMapping("/busqueda-figuritas/actualizar/{id}")
    @Operation(summary = "actualiza los datos de una figurita")
    fun actualizarFigurita(@PathVariable id: Int , @RequestBody figuritaActualizarDTO: FiguritaActualizarDTO){
        val figuritaParaActualizar = figuritaService.getByID(id)
        figuritaService.actualizarFigurita(figuritaParaActualizar , figuritaActualizarDTO)
    }

    @PostMapping("/crear-figurita")
    @Operation(summary ="Crea una figurita")
    fun crearFigurita(@RequestBody figuritaNueva: FiguritaActualizarDTO) {
        figuritaService.crearJugador(figuritaNueva)
    }
}