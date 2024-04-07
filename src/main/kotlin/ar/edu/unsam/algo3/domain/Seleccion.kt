package ar.edu.unsam.algo3.domain

class Seleccion(
    var pais: String, var confederacion: Confederacion,
    var copasDelMundo: Int, var copasConfederacion: Int
) : Entidad {

    override var id: Int = 0

    fun copasPar(): Boolean = copasDelMundo % 2 === 0

    override fun condicionDeBusqueda(value: String): Boolean = this.pais.contains(value, true)
}