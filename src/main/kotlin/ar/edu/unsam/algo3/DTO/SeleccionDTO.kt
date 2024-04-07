package ar.edu.unsam.algo3.DTO

import ar.edu.unsam.algo3.domain.*

data class SeleccionDTO (
    val id: Int,
    val pais: String,
    val copasDelMundo: Int,
    val confederacion: String
){}

fun Seleccion.toDTO() = SeleccionDTO(
    id = this.id,
    pais = this.pais,
    copasDelMundo = this.copasDelMundo,
    confederacion = confederacionNombre(this.confederacion)
)

private fun confederacionNombre(confederacion: Confederacion): String {
    return when (confederacion) {
        Confederacion.CONMEBOL -> "CONMEBOL"
        Confederacion.CONCACAF -> "CONCACAF"
        Confederacion.CAF -> "CAF"
        Confederacion.AFC -> "AFC"
        Confederacion.UEFA -> "UEFA"
        Confederacion.OFC -> "OFC"
        else -> ""
    }
}
