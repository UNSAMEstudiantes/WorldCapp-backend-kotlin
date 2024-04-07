package ar.edu.unsam.algo3.domain

import java.time.DayOfWeek
import java.time.LocalDate

interface DescuentoSupermercado {

    fun condicionDescuento(cantidadSobres: Int): Boolean
    fun factorDescuento(): Double
    fun descuentoPorAplicar(cantidadSobres: Int): Double =
        if (condicionDescuento(cantidadSobres)) factorDescuento() else 0.0
}

object DescuentoJueves : DescuentoSupermercado {
    override fun condicionDescuento(cantidadSobres: Int): Boolean = LocalDate.now().dayOfWeek == DayOfWeek.THURSDAY

    override fun factorDescuento(): Double = 0.10

}

object DescuentoPrimerosDias : DescuentoSupermercado {
    override fun condicionDescuento(cantidadSobres: Int): Boolean = LocalDate.now().dayOfMonth <= 10

    override fun factorDescuento(): Double = 0.05

}

object DescuentoCompraGrande : DescuentoSupermercado {
    override fun condicionDescuento(cantidadSobres: Int): Boolean = cantidadSobres > 200

    override fun factorDescuento(): Double = 0.45

}

object SinDescuento : DescuentoSupermercado {
    override fun condicionDescuento(cantidadSobres: Int): Boolean = false

    override fun factorDescuento(): Double = 0.0

}

class DescuentoCombinado(val descuentos: MutableList<DescuentoSupermercado>) : DescuentoSupermercado {
    override fun condicionDescuento(cantidadSobres: Int): Boolean =
        descuentos.any { it.condicionDescuento(cantidadSobres) }

    override fun factorDescuento(): Double = descuentos.sumOf { it.factorDescuento() }
    override fun descuentoPorAplicar(cantidadSobres: Int): Double = descuentos.sumOf {it.descuentoPorAplicar(cantidadSobres)}


}
