package ar.edu.unsam.algo3.DTO

import ar.edu.unsam.algo3.domain.Direccion
import ar.edu.unsam.algo3.domain.Usuario
import java.time.LocalDate

class UsuarioDTODetalles (
    var id: Number,
    var nombre: String,
    var apellido: String,
    val username: String,
){}

fun Usuario.toDTODetalles() = UsuarioDTODetalles(
    id = this.id,
    nombre = this.nombre,
    apellido = this.apellido,
    username = this.userName
)

