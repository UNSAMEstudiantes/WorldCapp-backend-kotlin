package ar.edu.unsam.algo3.DTO

import ar.edu.unsam.algo3.domain.*
import java.time.LocalDate

data class UsuarioDTO(
    var nombre: String,
    var apellido: String,
    val userName: String,
    val fechaDeNacimiento: LocalDate,
    val mail: String,
    val direccion: Direccion,
    var distanciaMaxima: Double,
    var edad: Long,
    var tipo:String,
    var fotoPerfil:String,
) {
}

fun Usuario.toDTO() = UsuarioDTO(
    userName = this.userName,
    fechaDeNacimiento = this.fechaDeNacimiento,
    mail = this.mail,
    direccion = this.direccion,
    distanciaMaxima = this.distanciaMaxima,
    edad = this.edad(),
    nombre = this.nombre,
    apellido = this.apellido,
    tipo = getTipoDeUsuario(this.tipoDeUsuario),
    fotoPerfil ="usuario"+this.id + ".jpg",
)
private fun getTipoDeUsuario(tipoDeUsuario: TipoDeUsuario): String {
    return when (tipoDeUsuario) {
        is UsuarioPar -> "Par"
        is UsuarioNacionalista -> "Nacionalista"
        is UsuarioConservador -> "Conservador"
        is UsuarioFanatico -> "Fanatico"
        is UsuarioDesprendido -> "Desprendido"
        is UsuarioApostador -> "Apostador"
        is UsuarioInteresado -> "Interesado"
        is UsuarioCambiante -> "Cambiante"
        else -> ""
    }
}
