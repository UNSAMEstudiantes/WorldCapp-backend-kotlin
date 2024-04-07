package ar.edu.unsam.algo3.domain

interface Posicion {

    fun valoracionJugador(jugador: Jugador): Double

    fun valorBase(): Double
}

object Arquero : Posicion {

    override fun valorBase(): Double = 100.0

    override fun valoracionJugador(jugador: Jugador): Double =
        if (jugador.altura >= 1.80) 100 * jugador.altura else valorBase()
}

object Defensor : Posicion {

    override fun valorBase(): Double = 50.0

    override fun valoracionJugador(jugador: Jugador): Double =
         if (jugador.esLider()) 50 + 10 * jugador.aniosJugados() else valorBase()
}

object MedioCampista : Posicion {

    override fun valorBase(): Double = 150.0

    override fun valoracionJugador(jugador: Jugador): Double =
         if (jugador.peso in 65.0..70.0) 150 + jugador.peso else valorBase()

}

object Delantero : Posicion {

    override fun valorBase(): Double = 200.0

    override fun valoracionJugador(jugador: Jugador): Double =
         if (jugador.esCampeon()) 200 + jugador.nroDeCamiseta.toDouble() * 10 else valorBase()

}

class Polivalente : Posicion {

    val posiciones: MutableList<Posicion> = mutableListOf()

    override fun valoracionJugador(jugador: Jugador): Double =
        if (jugador.esLeyenda() || jugador.esPromesa()) valoracionLeyendaOPromesa(jugador) else valorBase()

    fun valoracionPosiciones(jugador: Jugador): Double = posiciones.sumOf { it.valoracionJugador(jugador) }

    override fun valorBase(): Double = posiciones.sumOf { it.valorBase() } / posiciones.size

    fun valoracionLeyendaOPromesa(jugador: Jugador): Double =
        valorBase() + (valoracionPosiciones(jugador) / posiciones.size) - jugador.edad()
}
