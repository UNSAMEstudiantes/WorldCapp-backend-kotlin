package ar.edu.unsam.algo3.DTO

import ar.edu.unsam.algo3.domain.*
import java.time.LocalDate

class JugadorDTO(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val nroDeCamiseta: Int,
    val seleccion: String,
    val altura: Double,
    val peso: Double,
    val posicion: String,
    val lider: Boolean,
    val cotizacion: Int,
    val fechaNacimiento: LocalDate
){}

fun Jugador.toDTO() =  JugadorDTO(
    id = this.id,
    nombre = this.nombre,
    apellido = this.apellido,
    nroDeCamiseta = this.nroDeCamiseta,
    seleccion = this.seleccion.pais,
    altura = this.altura,
    peso = this.peso,
    posicion = posicionJugador(this.posicion),
    lider = this.esLider(),
    cotizacion = this.cotizacion,
    fechaNacimiento = this.fechaDeNacimiento
)

private fun posicionJugador(posicion: Posicion): String {
    return when (posicion) {
        is Delantero -> "Delantero"
        is Arquero -> "Arquero"
        is MedioCampista -> "Mediocampista"
        is Defensor -> "Defensor"
        else -> ""
    }
}

