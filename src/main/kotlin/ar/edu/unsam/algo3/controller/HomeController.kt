package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.HomeData
import ar.edu.unsam.algo3.services.JugadorService
import ar.edu.unsam.algo3.services.UsuarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@CrossOrigin("*")
class HomeController(
    @Autowired val usuarioService:UsuarioService,
    @Autowired val puntoDeVentaController:PuntoDeVentaController
) {


    @GetMapping("/home")
    fun getHomeInfo() :HomeData {
        val usuarios = usuarioService.cantidadUsuariosActivos()
        val ofrecidas = usuarioService.figuritasOfrecidas()
        val repetidas = usuarioService.figuritasRepetidas()
        val pdv =puntoDeVentaController.cantidadPuntosActivos()

        return HomeData(usuarios,repetidas,ofrecidas,pdv)
    }
}