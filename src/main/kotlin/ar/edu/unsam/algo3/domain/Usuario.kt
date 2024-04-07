package ar.edu.unsam.algo3.domain

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Usuario(
    var nombre: String,
    var apellido: String,
    val userName: String,
    var fechaDeNacimiento: LocalDate,
    var mail: String,
    var direccion: Direccion,
    var distanciaMaxima: Double,
    var tipoDeUsuario: TipoDeUsuario,
) : Entidad {

    override var id: Int = 0

    val figuritasFaltantes: MutableList<Figurita> = mutableListOf()

    val figuritasRepetidas: MutableList<Figurita> = mutableListOf()

    val seleccionesFavoritas: MutableCollection<Seleccion> = mutableListOf()

    val solicitudObserver: MutableList<SolicitudObserver> = mutableListOf()

    val puntosDeVenta: MutableCollection<PuntoDeVenta> = mutableListOf()


    fun edad(): Long = ChronoUnit.YEARS.between(fechaDeNacimiento, LocalDate.now())

    fun esCercano(usuario: Usuario): Boolean = direccion.distanciaEntre(usuario.direccion) <= distanciaMaxima

    fun esFaltante(figurita: Figurita): Boolean = figuritasFaltantes.contains(figurita)

    fun estaRepetida(figurita: Figurita): Boolean = figuritasRepetidas.contains(figurita)

    fun puedeEstaRepetida(figurita: Figurita): Boolean = !esFaltante(figurita)

    fun repetidasValidation(figurita: Figurita) {
        if (!puedeEstaRepetida(figurita)) throw RepetidasExeption("La figurita no puede ser registrada como repetida porque ya esta registrada como faltante")
    }

    fun puedeEstarFaltante(figurita: Figurita): Boolean = !estaRepetida(figurita) && !esFaltante(figurita)

    fun registrarFiguritaRepetida(figurita: Figurita) {
        if (puedeEstaRepetida(figurita)) figuritasRepetidas.add(figurita)
    }

    fun registrarFiguritaFaltante(figurita: Figurita) {
        if (puedeEstarFaltante(figurita)) figuritasFaltantes.add(figurita)
    }

    fun esValido() = nombre.isNotEmpty() && apellido.isNotEmpty() && userName.isNotEmpty() && mail.isNotEmpty()

    fun agregarSeleccionFavorita(seleccionFavorita: Seleccion) = seleccionesFavoritas.add(seleccionFavorita)

    fun llenoAlbum(): Boolean = figuritasFaltantes.isEmpty()

    fun figuritasARegalar(): List<Figurita> = figuritasRepetidas.filter { tipoDeUsuario.regalaFigurita(it, this) }

    override fun condicionDeBusqueda(value: String): Boolean =
        this.nombre.contains(value, true) || this.apellido.contains(value) || value == userName

    fun aRegalar(figurita: Figurita): Boolean = figuritasARegalar().contains(figurita)

    fun triplicarDistancia() {
        distanciaMaxima *= 3
    }

    fun comprobarSolicitud(figurita: Figurita, usuario: Usuario): Boolean {
        validarSolicitud(figurita, usuario)
        return esFaltante(figurita) && usuario.aRegalar(figurita) && esCercano(usuario)
    }

    fun validarFaltante(figurita: Figurita) {
        if (!esFaltante(figurita)) throw FaltanteException("El usuario ya tiene esa figurita")
    }

    fun validarRegalar(figurita: Figurita, usuario: Usuario) {
        if (!usuario.aRegalar(figurita)) throw RegalarException("El usuario no quiere regalar esa figurita")
    }

    fun validarCercano(usuario: Usuario) {
        if (!esCercano(usuario)) throw CercanoException("El usuario no es cercano")
    }

    fun validarSolicitud(figurita: Figurita, usuario: Usuario) {
        validarFaltante(figurita)
        validarCercano(usuario)
        validarRegalar(figurita, usuario)
    }

    fun solicitarFigurita(figurita: Figurita, usuario: Usuario) {
        validarSolicitud(figurita, usuario)
        figuritasFaltantes.remove(figurita)
        usuario.figuritasRepetidas.remove(figurita)
        realizarSolicitudObserver(this, figurita)

    }

    fun tieneFiguritasRepetidas() = figuritasRepetidas.isNotEmpty()

    fun agregarSolicitudObserver(solicitudObserver: SolicitudObserver) {
        this.solicitudObserver.add(solicitudObserver)
    }

    fun removerSolicitudObserver(solicitudObserver: SolicitudObserver) {
        this.solicitudObserver.remove(solicitudObserver)
    }

    fun realizarSolicitudObserver(usuario: Usuario, figurita: Figurita) {
        solicitudObserver.toList().forEach { it.recibioSolicitud(usuario, figurita) }
    }

    fun removerFiguritaRepetida(figurita: Figurita) {
        figuritasRepetidas.remove(figurita)
    }
    fun removerFiguritaFaltante(figurita: Figurita) {
        figuritasFaltantes.remove(figurita)
    }
    fun removerPuntoDeVenta(puntoDeVenta: PuntoDeVenta) {
        puntosDeVenta.remove(puntoDeVenta)
    }
}



