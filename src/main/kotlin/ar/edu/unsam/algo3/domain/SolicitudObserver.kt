package ar.edu.unsam.algo3.domain

interface SolicitudObserver {
    fun recibioSolicitud(usuario: Usuario, figurita: Figurita){
        if (observerCondition(figurita,usuario)) aplicarObserver(figurita,usuario)
    }

    fun observerCondition(figurita: Figurita, usuario: Usuario): Boolean

    fun aplicarObserver(figurita: Figurita, usuario: Usuario)
}
class DistanciaSolicitudObserver : SolicitudObserver {
    override fun recibioSolicitud(usuario: Usuario, figurita: Figurita) {
        if(observerCondition(figurita,usuario))
            conversionDistancia(usuario)
    }

    fun conversionDistancia(usuario: Usuario){
        usuario.triplicarDistancia()
        usuario.removerSolicitudObserver(this)
    }

    fun validarCantidadDeFiguritas(usuario: Usuario) : Boolean = usuario.figuritasFaltantes.size<=5

    override fun observerCondition(figurita: Figurita, usuario: Usuario) : Boolean  =
        validarCantidadDeFiguritas(usuario)

    override fun aplicarObserver(figurita: Figurita, usuario: Usuario) {
        conversionDistancia(usuario)
    }

}

class NacionalistaSolicitudObserver : SolicitudObserver {

    val FiguritasSolicitadas = mutableListOf<Figurita>()

    override fun recibioSolicitud(usuario: Usuario, figurita: Figurita) {
        FiguritasSolicitadas.add(figurita)
        if (observerCondition(figurita , usuario))
            conversionNacionalista(usuario)
    }

    fun conversionNacionalista(usuario: Usuario) {
        usuario.tipoDeUsuario = UsuarioNacionalista(distintasSelecciones())
        FiguritasSolicitadas.clear()
    }

    fun vericarTresOMas(): Boolean = FiguritasSolicitadas.size>=3

    fun distintasSelecciones() = FiguritasSolicitadas.takeLast(3).map{ it.jugador.seleccion }.distinct().toMutableList()

    fun verificoQueSeanDeLaMismaSeleccion() = distintasSelecciones().size == 1

    fun verificarFiguritas(): Boolean = vericarTresOMas() && verificoQueSeanDeLaMismaSeleccion()

    override fun observerCondition(figurita: Figurita, usuario: Usuario): Boolean  = !usuario.llenoAlbum() && verificarFiguritas()
    override fun aplicarObserver(figurita: Figurita, usuario: Usuario) {
        TODO("Not yet implemented")
    }

}
class ReservadasSolicitudObserver : SolicitudObserver {

    val listaReservadas: MutableList<Figurita> = mutableListOf()

    fun puedeSerRevervada(figurita: Figurita, usuario: Usuario){
        usuario.repetidasValidation(figurita)
    }

    fun reservarFigurita(figurita: Figurita, usuario: Usuario) {
        puedeSerRevervada(figurita, usuario)
        listaReservadas.add(figurita)
        usuario.figuritasRepetidas.remove(figurita)
    }

    fun removerReservada(figurita: Figurita) {
        listaReservadas.remove(figurita)
    }

    fun reservadasARegalar(usuario: Usuario): List<Figurita> = listaReservadas.filter { usuario.tipoDeUsuario.regalaFigurita(it, usuario) }

    fun listaDeRepetidasVacia(usuario: Usuario) = !usuario.tieneFiguritasRepetidas()

    fun hayFiguritasARegalar(usuario: Usuario) = reservadasARegalar(usuario).isNotEmpty()

    fun figuSolicitadaValiosa(figuritaSolicitada: Figurita): Boolean =
        listaReservadas.any { it.valoracion() <= figuritaSolicitada.valoracion() }

    fun figuritaMenosValiosa(usuario: Usuario): Figurita =
        reservadasARegalar(usuario).minBy{it.valoracion()}

    override fun observerCondition(figurita: Figurita, usuario: Usuario): Boolean =
        figuSolicitadaValiosa(figurita) && listaDeRepetidasVacia(usuario) && hayFiguritasARegalar(usuario)

    override fun aplicarObserver(figurita: Figurita, usuario: Usuario) {
        usuario.registrarFiguritaRepetida(figuritaMenosValiosa(usuario))
        removerReservada(figuritaMenosValiosa(usuario))
    }
}

class DesprendidoSolicitudObserver(val cantidadFiguritasARegalar: Int) : SolicitudObserver {

    fun mayorCantidadDeFiguritas(usuario: Usuario) : Boolean = usuario.figuritasARegalar().size > cantidadFiguritasARegalar

    fun conversionDesprendido(usuario: Usuario) {
        usuario.tipoDeUsuario = UsuarioDesprendido()
    }
    override fun observerCondition(figurita: Figurita, usuario: Usuario): Boolean = usuario.llenoAlbum() && mayorCantidadDeFiguritas(usuario)
    override fun aplicarObserver(figurita: Figurita, usuario: Usuario) {
        conversionDesprendido(usuario)
    }

}

class FelicitacionesSolicitudObserver(val mailSender : MailSender) : SolicitudObserver {


    fun enviarMail(usuario: Usuario, figurita: Figurita){
        mailSender.enviarMail(crearMail(usuario, figurita))
    }

    fun crearMail(usuario: Usuario, figurita: Figurita) : Mail {
        return Mail(
                de = "info@worldcapp.com.ar",
                para = usuario.mail,
                asunto = "Felicitaciones, completaste el album!",
                contenido = "Con la siguiente figurita completaste el album: ${figurita.numero}, ${figurita.jugador.nombre} ${figurita.jugador.apellido}, ${figurita.jugador.seleccion}, ${figurita.valoracion()}"
        )
    }

    override fun observerCondition(figurita: Figurita, usuario: Usuario): Boolean = usuario.llenoAlbum()
    override fun aplicarObserver(figurita: Figurita, usuario: Usuario) {
        enviarMail(usuario, figurita)
    }
}
