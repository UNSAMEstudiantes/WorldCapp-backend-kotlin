package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.DTO.UsuarioDTO
import ar.edu.unsam.algo3.DTO.toDTO
import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.services.UsuarioService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin("*")
class UsuarioController(
    @Autowired val repoUsuarios: Repositorio<Usuario>,
    @Autowired val usuarioService: UsuarioService,
) {

    @GetMapping("/usuarios")
    @Operation(summary = "Devuelve la lsita de todos los usuarios")
    fun getAllUsuarios(@RequestParam("value") usuarioABuscar: String) =
        repoUsuarios.search(usuarioABuscar)

    @GetMapping("/usuarios/{id}")
    @Operation(summary = "devuelve un usuario por su id")
    fun getUsuarioById(@PathVariable id: Int) = repoUsuarios.getById(id).toDTO()

    @GetMapping("/usuarios/{id}/faltantes")
    @Operation(summary = "devuelve las figuritas faltantes del usuario")
    fun getFiguritasFaltantes(@PathVariable id :Int)= usuarioService.getFiguritasFaltantes(id)
    @GetMapping("/usuarios/{id}/repetidas")
    @Operation(summary = "devuelve las figuritas repetidas del usuario")
    fun getFiguritasRepetidas(@PathVariable id :Int)= usuarioService.getFiguritasRepetidas(id)

    @PutMapping("/usuarios/{id}")
    @Operation(summary = "Actualiza un usuario por su ID")
    fun actualizarUsuario(@PathVariable id: Int, @RequestBody usuarioDTO: UsuarioDTO) {
        usuarioService.actualizarUsuario(id, usuarioDTO)
//        return ResponseEntity.ok("Datos actualizados correctamente")

    }
    @PutMapping("/usuarios/{idUsaurio}/agregar-repetidas")
    @Operation(summary = "agrega una figurita a la lista de repetidas de un usuario")
    fun agregarFiguritaRepetida(@PathVariable idUsaurio: Int, @RequestBody idFigurita: Int) {
        usuarioService.agregarFiguritaRepetida(idUsaurio, idFigurita)
//        return ResponseEntity.ok("figurita repetida registrada correctamente")

    }
    @PutMapping("/usuarios/{idUsaurio}/agregar-faltantes")
    @Operation(summary = "agrega una figurita a la lista de faltantes de un usuario")
    fun agregarFiguritaFaltante(@PathVariable idUsaurio: Int, @RequestBody idFigurita: Int) {
        usuarioService.agregarFiguritaFaltante(idUsaurio, idFigurita)
//        return ResponseEntity.ok("figurita faltante registrada correctamente")

    }

    @PutMapping("/usuarios/{idUsaurio}/borrar-repetidas")
    @Operation(summary = "borra una figurita a la lista de repetidas de un usuario")
    fun removerFiguritaRepetida(@PathVariable idUsaurio: Int, @RequestBody idFigurita: Int) {
        usuarioService.removerFiguritaRepetida(idUsaurio, idFigurita)
//        return ResponseEntity.ok("figurita repetida registrada correctamente")
    }
    @PutMapping("/usuarios/{idUsaurio}/borrar-faltantes")
    @Operation(summary = "borra una figurita a la lista de faltantes de un usuario")
    fun removerFiguritaFaltante(@PathVariable idUsaurio: Int, @RequestBody idFigurita: Int) {
        usuarioService.removerFiguritaFaltante(idUsaurio, idFigurita)
    }

    @PostMapping("/login")
    @Operation(summary ="devuelve true si el userName se encuentra en la lista de usuarios")
    fun validarUsuario(@RequestBody request: Map<String, String>): Int? {
         val userName = request["userName"]
        if(userName != null){
            return usuarioService.validarUsername(userName)
        }else throw ElementoInexistenteException("Usuario o contraseña inexistente.")

    }
    @PostMapping("/usuarios/{id}/solicitar-figurita")
    @Operation(summary = "Solicita una figurita para el usuario")
    fun solicitarFigurita(
        @PathVariable("id") id: Int,  // Asegúrate de que este sea el ID del usuario que solicita la figurita
        @RequestParam("idFigurita") idFigurita: Int,
        @RequestParam("idUsuarioCedente") idUsuarioCedente: Int
    ): ResponseEntity<String> {
            usuarioService.solicitarFigurita(id, idFigurita, idUsuarioCedente)
            return ResponseEntity.ok("Figurita solicitada correctamente")

    }
    @GetMapping("/usuarios/total-activos")
    @Operation(summary="trae el numero de todos los usuarios activos")
    fun cantidadUsuariosActivos() = usuarioService.cantidadUsuariosActivos()

    @GetMapping("/usuarios/total-repetidas")
    @Operation(summary="trae el numero de todas las figuritas repetidas")
    fun cantidadFigusRepetidas(): Int = usuarioService.figuritasRepetidas()

    @GetMapping("/usuarios/total-Ofrecidas")
    @Operation(summary="trae el numero de todas las figuritas Ofrecidas")
    fun cantidadFigusOfrecidas(): Int = usuarioService.figuritasOfrecidas()
}

