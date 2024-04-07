package ar.edu.unsam.algo3.DTO

import ar.edu.unsam.algo3.domain.Impresion

class FiguritaActualizarDTO(
    val numero: Int,
    val nuevoJugador: Int,
    val onFire: Boolean,
    val nivelDeImpresion: String,
    val imagen: String
    ) {
}