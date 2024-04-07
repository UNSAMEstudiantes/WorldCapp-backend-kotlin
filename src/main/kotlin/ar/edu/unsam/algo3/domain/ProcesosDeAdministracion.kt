package ar.edu.unsam.algo3.domain

abstract class Proceso {
    abstract val proceso: String

    open fun ejecutar(mailSender: MailSender) {
        ejecutarProceso()
        enviarCorreo(mailSender)
    }


    fun enviarCorreo(mailSender: MailSender) {
        val mail = Mail(
            contenido = "¡Se realizó el proceso: $proceso exitosamente!",
            asunto = "Proceso completado",
            de = "notificaciones@enviar.com",
            para = "admin@worldcapp.com.ar")
        mailSender.enviarMail(mail)
    }
    abstract fun ejecutarProceso()
}
class BorrarUsuariosInactivos(private val repositorioUsuarios: Repositorio<Usuario>) : Proceso() {
    override val proceso: String
        get() = "Borrar Usuarios Inactivos"
    override fun ejecutarProceso() {
        val usuariosInactivos = repositorioUsuarios.elementos.filter { usuario ->
            usuario.llenoAlbum() && usuario.figuritasRepetidas.isEmpty() }
            usuariosInactivos.forEach { usuario -> repositorioUsuarios.delete(usuario) }
    }
}

class CreacionActualizacionSelecciones(private val actualizador: Actualizador) : Proceso() {
    override val proceso: String
        get() = "Creacion/Actualizacion de selecciones"
    override fun ejecutarProceso() {
        actualizador.actualizar()
    }
}

class BorrarPuntosVentasInactivos(private val repositorioPuntosVenta: Repositorio<PuntoDeVenta>) : Proceso() {
    override val proceso: String
        get() = "Borrar puntos de venta Inactivos"

    override fun ejecutarProceso() {
        val puntosVentasInactivos = repositorioPuntosVenta.elementos.filter { puntoVenta ->
            !puntoVenta.disponibilidad() && puntoVenta.pedidosPendientes.isEmpty() && !puntoVenta.tienePedidosARecibirEn90Dias()
        }
        puntosVentasInactivos.forEach { puntoVenta ->
            repositorioPuntosVenta.delete(puntoVenta)
        }

    }
}
class CambiarOnFireLoteFiguritas(private val repositorioFiguritas: Repositorio<Figurita>, private  val  numerosFiguritasAOnFire: List<Int>) : Proceso() {

        override val proceso: String
        get() = "Cambio de lote de figuritas a OnFire"
         override fun ejecutarProceso() {
             val figuritasAOnFire = repositorioFiguritas.elementos.filter { figurita ->
            numerosFiguritasAOnFire.contains(figurita.numero)}

            figuritasAOnFire.forEach { figurita -> figurita.estaOnFire = true
            repositorioFiguritas.update(figurita)}
    }
}
class ActualizarStockSobresPuntosVenta(private val repositorioPuntosVenta: Repositorio<PuntoDeVenta>) : Proceso() {
    override val proceso: String
        get() = "Actualizar Stock de sobres en punto de venta"
    override fun ejecutarProceso() {
        repositorioPuntosVenta.elementos.forEach { puntoVenta -> puntoVenta.actualizarStockSobres()
            repositorioPuntosVenta.update(puntoVenta) }
    }
}