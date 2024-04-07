package ar.edu.unsam.algo3.domain

import org.uqbar.geodds.Point

class Direccion(
    val provincia: String, val localidad: String, val calle: String, val altura: Int, val ubiGeografica: Point

) {
    fun distanciaEntre(direccion: Direccion) =
        ubiGeografica.distance(direccion.ubiGeografica)
}