package ar.edu.unsam.algo3.domain

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*


open class Jugador(
    var nombre: String,
    var apellido: String,
    var fechaDeNacimiento: LocalDate,
    var nroDeCamiseta: Int,
    var seleccion: Seleccion,
    var anioDeDebut: LocalDate,
    var altura: Double,
    var peso: Double,
    var posicion: Posicion,
    var lider: Boolean,
    var cotizacion: Int,
    var pais: String = ""
) : Entidad {
    override var id: Int = 0

    fun deducirPais(seleccion: Seleccion) = seleccion.pais.substring(0, 3).uppercase(Locale.getDefault()).also { pais = it }

    open fun valoracion(): Double = posicion.valoracionJugador(this)

    fun aniosJugados(): Double = ChronoUnit.YEARS.between(anioDeDebut, LocalDate.now()).toDouble()

    fun esCampeon(): Boolean = seleccion.copasDelMundo > 0

    fun camisetaPar(): Boolean = nroDeCamiseta % 2 == 0

    fun copasSeleccion(): Int = seleccion.copasDelMundo

    fun edad(): Long = ChronoUnit.YEARS.between(fechaDeNacimiento, LocalDate.now())

    fun esLeyenda(): Boolean = esVeteranoDeSeleccion() && (buenaCotizacion() || camisetaImportante()) && esLider()

    fun esPromesa(): Boolean = esNuevoEnSeleccion() && esJoven() && !buenaCotizacion()

    fun esVeteranoDeSeleccion(): Boolean = antiguedadSeleccion() > 10

    fun esNuevoEnSeleccion(): Boolean = antiguedadSeleccion() < 2

    fun esLider(): Boolean = lider

    fun camisetaImportante(): Boolean = nroDeCamiseta in 5..10

    fun buenaCotizacion(): Boolean = cotizacion > 20

    fun antiguedadSeleccion(): Int = LocalDate.now().year - anioDeDebut.year

    fun esJoven(): Boolean = edad() <= 22

    fun validarCamiseta(): Boolean = nroDeCamiseta in 1..99

    fun esPositivo(numero: Double): Boolean = numero > 0.0

    fun esValido() = nombre.isNotEmpty() && apellido.isNotEmpty() && validarCamiseta() && esPositivo(altura) && esPositivo(peso)

    override fun condicionDeBusqueda(value: String): Boolean =
        this.nombre.contains(value, true) || this.apellido.contains(value, true)
}
