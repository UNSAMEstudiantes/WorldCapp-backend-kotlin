package ar.edu.unsam.algo3.domain

class Figurita(var numero: Int, var nivelDeImpresion: Impresion, var estaOnFire: Boolean, var jugador: Jugador):
    Entidad {

     override var id: Int = 0
     val valorPiso: Double = 100.0

    fun nivelDeImpresion() = nivelDeImpresion

    fun esPar(): Boolean = numero % 2 == 0

    fun calculoSiEstaOnFire(): Double = if(estaOnFire && esPar()) 1.20 * 1.10 else if (estaOnFire) 1.20 else 1.0

    fun calculoNivel(): Double = if (nivelDeImpresion === Impresion.MEDIA || nivelDeImpresion === Impresion.ALTA) 0.85 else 1.0

    fun valorBase(): Double = valorPiso * calculoNivel() * calculoSiEstaOnFire()

    fun valoracion(): Double = jugador.valoracion() + valorBase()

    fun esValida(): Boolean = numero >= 0

    override fun condicionDeBusqueda(value:String):Boolean =
         jugador.nombre.contains(value, true) || jugador.apellido.contains(value, true)
                ||  jugador.seleccion.pais.contains(value, true) ||  this.numero.toString().equals(value, true)


   

}


