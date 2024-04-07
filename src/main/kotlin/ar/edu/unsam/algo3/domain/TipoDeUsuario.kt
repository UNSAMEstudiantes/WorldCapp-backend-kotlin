package ar.edu.unsam.algo3.domain

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.springframework.stereotype.Component

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes(
    JsonSubTypes.Type(value = UsuarioPar::class, name = "Par"),
    JsonSubTypes.Type(value = UsuarioNacionalista::class, name = "Nacionalista"),
    JsonSubTypes.Type(value = UsuarioConservador::class, name = "Conservador"),
    JsonSubTypes.Type(value = UsuarioFanatico::class, name = "Fanatico"),
    JsonSubTypes.Type(value = UsuarioDesprendido::class, name = "Desprendido"),
    JsonSubTypes.Type(value = UsuarioApostador::class, name = "Apostador"),
    JsonSubTypes.Type(value = UsuarioInteresado::class, name = "Interesado"),
    JsonSubTypes.Type(value = UsuarioCambiante::class, name = "Cambiante"),
    )
interface TipoDeUsuario {
    fun regalaFigurita(figurita: Figurita, usuario: Usuario): Boolean
}

class UsuarioPar : TipoDeUsuario {
    override fun regalaFigurita(figurita: Figurita, usuario: Usuario): Boolean =
    !(figurita.esPar() || figurita.jugador.camisetaPar() || figurita.jugador.copasSeleccion() % 2 == 0)

}

class UsuarioNacionalista(val seleccionesFavoritas:MutableList<Seleccion> = mutableListOf()) : TipoDeUsuario {
    override fun regalaFigurita(figurita: Figurita, usuario: Usuario): Boolean =
        !(seleccionesFavoritas.contains(figurita.jugador.seleccion))

    fun agregarSeleccionFavorita(seleccion: Seleccion) {
        seleccionesFavoritas.add(seleccion)
    }
}

class UsuarioConservador : TipoDeUsuario {
    override fun regalaFigurita(figurita: Figurita, usuario: Usuario): Boolean =
        figurita.nivelDeImpresion() == Impresion.ALTA && usuario.llenoAlbum()
}

class UsuarioFanatico(val jugadorFavorito: Jugador ) : TipoDeUsuario {
    override fun regalaFigurita(figurita: Figurita, usuario: Usuario): Boolean =
        !(figurita.jugador == jugadorFavorito || figurita.jugador.esLeyenda())
}

class UsuarioDesprendido : TipoDeUsuario {
    override fun regalaFigurita(figurita: Figurita, usuario: Usuario): Boolean = true
}

class UsuarioApostador : TipoDeUsuario {
    override fun regalaFigurita(figurita: Figurita, usuario: Usuario): Boolean =
        !(figurita.estaOnFire || figurita.jugador.esPromesa())
}

class UsuarioInteresado : TipoDeUsuario {
    override fun regalaFigurita(figurita: Figurita, usuario: Usuario): Boolean = !topFive(usuario).contains(figurita)

    fun topFive(usuario: Usuario): List<Figurita> =
        repetidasSinRepetir(usuario.figuritasRepetidas).sortedByDescending { it.valoracion() }.slice(0..4)

    fun repetidasSinRepetir(figuritas: MutableList<Figurita>): MutableList<Figurita> =
        figuritas.toSet().toMutableList()
}

class UsuarioCambiante : TipoDeUsuario {

    override fun regalaFigurita(figurita: Figurita, usuario: Usuario): Boolean = if (usuario.edad() <= 25)
        UsuarioDesprendido().regalaFigurita(figurita, usuario) else
        UsuarioConservador().regalaFigurita(figurita, usuario)
}
