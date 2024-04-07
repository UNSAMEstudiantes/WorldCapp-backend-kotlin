
package ar.edu.unsam.algo3.controller
import ar.edu.unsam.algo3.DTO.JugadorDTO
import ar.edu.unsam.algo3.DTO.PuntoDeVentaDTO
import ar.edu.unsam.algo3.DTO.toDTO
import ar.edu.unsam.algo3.domain.PuntoDeVenta
import ar.edu.unsam.algo3.domain.Repositorio
import ar.edu.unsam.algo3.domain.RepositorioPuntoDeVenta
import ar.edu.unsam.algo3.domain.Usuario
import ar.edu.unsam.algo3.services.JugadorService
import ar.edu.unsam.algo3.services.PuntoDeVentaService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin("*")
@RequestMapping("/puntosdeventa")
class PuntoDeVentaController(
    @Autowired val repoPuntosDeVenta: RepositorioPuntoDeVenta<PuntoDeVenta>,
    @Autowired val repoUsuario: Repositorio<Usuario>,
    @Autowired val puntoDeVentaService: PuntoDeVentaService

) {

    @GetMapping("/{idUsuario}/all")
    @Operation(summary = "Devuelve toodos los puntos de venta")
    fun getALLPuntosDeVenta(@PathVariable idUsuario: Int): List<PuntoDeVentaDTO> {
        val usuarioABuscar = repoUsuario.getById(idUsuario)
        return repoPuntosDeVenta.convertiraDTO(usuarioABuscar)
    }

    @GetMapping("/{idUsuario}/{idPuntoDeVenta}")
    @Operation(summary = "devuelve un punto de venta por su id")

    fun getPuntoDeVentaByID(@PathVariable idUsuario: Int ,@PathVariable idPuntoDeVenta: Int): PuntoDeVentaDTO {
        val usuarioABuscar = repoUsuario.getById(idUsuario)
        return repoPuntosDeVenta.getById(idPuntoDeVenta).toDTO(usuarioABuscar)
    }

    @GetMapping("/{idUsuario}/Ordenado")
    @Operation(summary = "devuelve un punto de venta ordenado por lo pedido")
    fun ordenarPuntosDeVenta(
        @PathVariable idUsuario: Int,
        @RequestParam("tipoOrden") tipoOrden : String,
        @RequestParam("nombreABuscar") nombreABuscar : String
    ): List<PuntoDeVentaDTO> {
        val usuarioABuscar = repoUsuario.getById(idUsuario)
        return repoPuntosDeVenta.ordenarPuntos(tipoOrden,usuarioABuscar,nombreABuscar)
    }

    @GetMapping("/total-activos")
    @Operation(summary="trae el numero de todos los puntos de ventas activos")
    fun cantidadPuntosActivos() = repoPuntosDeVenta.getCantidad()

    @DeleteMapping("/{idUsuario}/{idPuntoDeVenta}")
    @Operation(summary = "Elimina un punto de venta por su id")
    fun deletePuntoDeVenta(
        @PathVariable idUsuario: Int,
        @PathVariable idPuntoDeVenta: Int
    ) {
        val usuario = repoUsuario.getById(idUsuario)
        val puntoDeVenta = repoPuntosDeVenta.getById(idPuntoDeVenta)
        usuario.removerPuntoDeVenta(puntoDeVenta)
        repoPuntosDeVenta.delete(puntoDeVenta)
    }

    @PostMapping("/{idUsuario}/crear")
    fun crearPuntoDeVenta(
        @RequestBody puntoDeVentaNuevo: PuntoDeVentaDTO,
        @PathVariable idUsuario: Int): PuntoDeVentaDTO {
        return puntoDeVentaService.crearPuntoDeVenta(puntoDeVentaNuevo, idUsuario)
    }

    @PutMapping("/editar-punto/{id}")
    @Operation(summary = "Actualiza un punto de venta por su ID")
    fun actualizarPuntoDeVenta(@PathVariable id: Int, @RequestBody puntoDeVentaDTO: PuntoDeVentaDTO) {
        puntoDeVentaService.actualizarPuntoDeVenta(id, puntoDeVentaDTO)
    }
}