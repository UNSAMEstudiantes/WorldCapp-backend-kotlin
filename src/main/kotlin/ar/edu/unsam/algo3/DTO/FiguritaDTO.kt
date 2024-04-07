package ar.edu.unsam.algo3.DTO

import ar.edu.unsam.algo3.domain.*
import java.time.LocalDate

class FiguritaDTO (
    val id: Int,
    val numeroFigurita: Int,
    val nombre: String,
    val onFire: Boolean,
    val valoracion: Number,
    val peso: Number,
    val altura: Number,
    val nroCamiseta: Number,
    val seleccion: String,
    val nacimiento: LocalDate,
    val cotizacion: Number,
    val imagen: String,
    var cedidaPor: UsuarioDTODetalles?,
    val posicion: String,
    val nivelImpresion: String,
    val valoReal: Number,
    val esPromesa: Boolean,
    val copasDelMundo: Number,
    val anioDeDebut: LocalDate,
    val confederacion: Confederacion,
    val copasConfederacion: Number,
    val lider: Boolean,
    val jugadorId : Int
){
    fun condicionDeBusqueda(value:String):Boolean =
        this.nombre.contains(value, true) ||  this.seleccion.contains(value, true) ||  this.numeroFigurita.toString().equals(value, true)
}

fun Figurita.toDTO(usuario: UsuarioDTODetalles? = null) = FiguritaDTO(
    id = this.id,
    numeroFigurita = this.numero,
    nombre = this.jugador.nombre + " " + this.jugador.apellido,
    onFire = this.estaOnFire,
    valoracion = this.jugador.valoracion(),
    peso = this.jugador.peso,
    altura = this.jugador.altura,
    nroCamiseta = this.jugador.nroDeCamiseta,
    seleccion = this.jugador.seleccion.pais,
    nacimiento = this.jugador.fechaDeNacimiento,
    cotizacion = cotizacionPromesa(this.jugador.cotizacion),
    imagen = this.jugador.apellido + ".jpg",
    cedidaPor = usuario,
    posicion = posicionJugador(this.jugador.posicion),
    nivelImpresion = nivelImpresion(this.nivelDeImpresion),
    valoReal = this.valoracion(),
    esPromesa = this.jugador.esPromesa(),
    copasDelMundo = this.jugador.seleccion.copasDelMundo ,
    anioDeDebut = this.jugador.anioDeDebut,
    confederacion =  this.jugador.seleccion.confederacion,
    copasConfederacion = this.jugador.seleccion.copasConfederacion,
    lider = this.jugador.lider,
    jugadorId = this.jugador.id
)

private fun cotizacionPromesa(cotizacion: Int): Int{
    if(cotizacion <= 20){
        return cotizacion * 1000000
    }else{
        return cotizacion
    }
}
private fun posicionJugador(posicion: Posicion): String {
    return when (posicion) {
        is Delantero -> "Delantero"
        is Arquero -> "Arquero"
        is MedioCampista -> "Mediocampista"
        is Defensor -> "Defensor"
        else -> ""
    }
}

private fun nivelImpresion(nivelImpresion: Impresion): String {
    return when (nivelImpresion) {
        Impresion.BAJA -> "baja"
        Impresion.MEDIA -> "media"
        Impresion.ALTA -> "alta"
        else -> ""
    }
}






