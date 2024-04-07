package ar.edu.unsam.algo3.domain

interface Entidad {
    var id: Int
    fun condicionDeBusqueda(value: String): Boolean
    fun esNueva(): Boolean = id == 0

   }
