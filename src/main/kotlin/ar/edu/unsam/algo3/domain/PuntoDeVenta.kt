package ar.edu.unsam.algo3.domain

import java.time.LocalDate
import java.time.temporal.ChronoUnit

abstract class PuntoDeVenta(var nombre: String, var direccion: Direccion, var stockSobres: Int) : Entidad {
    override var id: Int = 0

    val pedidosPendientes: MutableList<Pedido> = mutableListOf()

    fun agregarPedido(pedido: Pedido) = pedidosPendientes.add(pedido)

    fun disponibilidad() = stockSobres > 0

    fun importeACobrar(cantidadSobres: Int, ubicacionUsuario: Direccion): Double =
        costoMinimo(cantidadSobres, ubicacionUsuario) * multiplicadorEspecial(cantidadSobres)

    abstract fun multiplicadorEspecial(cantidadSobres: Int): Double

    fun costoMinimo(cantidadSobres: Int, ubicacionUsuario: Direccion) = costoSobres(cantidadSobres) + costoDistancia(ubicacionUsuario)

    fun costoDistancia(ubicacionUsuario: Direccion): Double {
        val distancia = distanciaConUsuario(ubicacionUsuario)
        return 1000 + (100 * excesoDeKms(distancia))
    }

    fun excesoDeKms(distancia: Double): Double = if (distancia < 10) 0.00 else (distancia.toInt() - 10).toDouble()

    fun distanciaConUsuario(ubicacionUsuario: Direccion): Double = ubicacionUsuario.distanciaEntre(direccion)

    fun costoSobres(cantidad: Int) = cantidad * 170.00

    override fun condicionDeBusqueda(value: String): Boolean = nombre.contains(value , true)

     fun actualizarStockSobres() {
        val pedidosRecibidos = pedidosPendientes.filter { pedido ->
            pedido.recibido }
        val sobresRecibidos = pedidosRecibidos.sumBy { pedido -> pedido.cantidadSobres }

        stockSobres += sobresRecibidos

        pedidosPendientes.removeAll(pedidosRecibidos)
    }
    fun tienePedidosARecibirEn90Dias(): Boolean {
        val fechaLimite = LocalDate.now().plusDays(90)
        return pedidosPendientes.any { pedido -> pedido.fechaEntrega.isBefore(fechaLimite)
        }
    }



}

class Kiosko(
    nombre: String,
    direccion: Direccion,
    stockSobres: Int,
    var tieneEmpleados: Boolean
) : PuntoDeVenta(nombre, direccion, stockSobres) {
    override fun multiplicadorEspecial(cantidadSobres: Int): Double = 1.00 + esPropietario()

    fun esPropietario(): Double = if (tieneEmpleados) 0.25 else 0.10



}

class Libreria(nombre: String, direccion: Direccion, stockSobres: Int) : PuntoDeVenta(nombre, direccion, stockSobres) {

    override fun multiplicadorEspecial(cantidadSobres: Int): Double = if (existenPedidosPendientes()) 1.05 else 1.10

    fun existenPedidosPendientes(): Boolean =
        pedidosPendientes.any { pedido -> diferenciaDeDias(pedido.fechaEntrega) <= 10 }

    fun diferenciaDeDias(fechaEntrega: LocalDate): Long = ChronoUnit.DAYS.between(LocalDate.now(), fechaEntrega)
}

class Supermercado(nombre: String, direccion: Direccion, stockSobres: Int, var tipoDescuento : DescuentoSupermercado) :
    PuntoDeVenta(nombre, direccion, stockSobres) {

    override fun multiplicadorEspecial(cantidadSobres: Int): Double =
         maxOf(1 - tipoDescuento.descuentoPorAplicar(cantidadSobres) , 0.50)

}

data class Pedido(val cantidadSobres: Int, val fechaEntrega: LocalDate, val recibido: Boolean)

