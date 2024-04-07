package ar.edu.unsam.algo3.domain

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import org.uqbar.geodds.Point
import java.time.LocalDate

class ProcesosSpec : DescribeSpec({

    val repoFigurita = Repositorio<Figurita>()
    val repoUsuario = Repositorio<Usuario>()
    val repoPuntoDeVenta = Repositorio<PuntoDeVenta>()

    val ubiGeografica = Point(122, 200)
    val direccion1 = Direccion("Buenos Aires", "San Martin", "calle", 2345, ubiGeografica)
    val tipoDeUsuario1 = UsuarioPar()
    val usuario1 = Usuario(
        "pedro",
        "perez",
        "pedroperez",
        LocalDate.of(1990, 1, 1),
        "pedroperez@example.com",
        direccion1,
        10.0,
        tipoDeUsuario1
    )

    val usuario2 = Usuario(
        "Jane",
        "Smith",
        "janesmith",
        LocalDate.of(1995, 5, 10),
        "jane.smith@example.com",
        direccion1,
        10.0,
        tipoDeUsuario1
    )

    val puntoDeVentaActivo = Kiosko(
        nombre = "random",
        direccion = direccion1,
        stockSobres = 50,
        tieneEmpleados = false
    )


    val puntoDeVentaInactivo = Libreria(
        nombre = "Librería Inactiva",
        direccion = direccion1,
        stockSobres = 0,
    )
    val argentina = Seleccion("Argentina", Confederacion.CONMEBOL, 3, 15)
    val mexico = Seleccion("México", Confederacion.CONCACAF, 0, 0)


    val delanteroCampeonDelMundoCami10 = Jugador(
        "Lionel", "Messi", LocalDate.of(1987, 6, 24),
        10, argentina, LocalDate.of(2005, 8, 17), 1.70, 72.5, Delantero, true, 100000000
    )

    val medioCampistaPesado = Jugador(
        "Enrique",
        "Gonzalez",
        LocalDate.of(1992, 7, 13),
        8,
        mexico,
        LocalDate.of(2015, 11, 11),
        1.74,
        70.5,
        MedioCampista,
        false,
        120
    )
    val figuritaMessi = Figurita(10, Impresion.BAJA, true, delanteroCampeonDelMundoCami10)
    val figuritaEnrrique = Figurita(7, Impresion.ALTA, false, medioCampistaPesado)

    describe("Procesos de administracion") {

        describe("Borrar usuarios inactivos") {
            val proceso = BorrarUsuariosInactivos(repoUsuario)
            val mailSenderMock = mockk<MailSender>(relaxed = true)

            repoUsuario.create(usuario1)
            repoUsuario.create(usuario2)

            it("Debe borrar el usuario si completaron el álbum y no tienen figuritas repetidas.") {
                usuario1.llenoAlbum()
                usuario1.figuritasRepetidas.isEmpty()
                proceso.ejecutar(mailSenderMock)
                repoUsuario.elementos.shouldNotContain(usuario1)

                verify(exactly = 1) {
                    mailSenderMock.enviarMail(
                        Mail(
                            contenido = "¡Se realizó el proceso: Borrar Usuarios Inactivos exitosamente!",
                            asunto = "Proceso completado",
                            de = "notificaciones@enviar.com",
                            para = "admin@worldcapp.com.ar"
                        )
                    )
                }
            }
        }

        describe("Creacion/Actualizacion de selecciones") {
            val actualizadorMock = mockk<Actualizador>(relaxed = true)
            val mailSenderMock = mockk<MailSender>(relaxed = true)
            val proceso = CreacionActualizacionSelecciones(actualizadorMock)

            it("debe llamar al método 'actualizar' del actualizador") {
                proceso.ejecutar(mailSenderMock)
                verify(exactly = 1) { actualizadorMock.actualizar() }

                verify(exactly = 1) {
                    mailSenderMock.enviarMail(
                        Mail(
                            contenido = "¡Se realizó el proceso: Creacion/Actualizacion de selecciones exitosamente!",
                            asunto = "Proceso completado",
                            de = "notificaciones@enviar.com",
                            para = "admin@worldcapp.com.ar"
                        )
                    )
                }
            }
        }

        describe("Borrar puntos de venta inactivos") {
            val proceso = BorrarPuntosVentasInactivos(repoPuntoDeVenta)
            val mailSenderMock = mockk<MailSender>(relaxed = true)

            repoPuntoDeVenta.create(puntoDeVentaInactivo)


            it("debe eliminar los puntos de venta inactivos y enviar un correo") {
                proceso.ejecutar(mailSenderMock)
                repoPuntoDeVenta.elementos.shouldNotContain(puntoDeVentaInactivo)

                verify(exactly = 1) {
                    mailSenderMock.enviarMail(
                        Mail(
                            contenido = "¡Se realizó el proceso: Borrar puntos de venta Inactivos exitosamente!",
                            asunto = "Proceso completado",
                            de = "notificaciones@enviar.com",
                            para = "admin@worldcapp.com.ar"
                        )
                    )
                }
            }
        }

        describe("Cambiar a OnFire lote de figuritas") {
            val numerosFiguritasAOnFire = listOf(10, 7)
            repoFigurita.create(figuritaMessi)
            repoFigurita.create(figuritaEnrrique)
            val mailSenderMock = mockk<MailSender>(relaxed = true)


            val proceso = CambiarOnFireLoteFiguritas(repoFigurita, numerosFiguritasAOnFire)

            it("Se deben cambiar las figuritas a Onfire") {
                proceso.ejecutar(mailSenderMock)
                val figuritasOnFire = repoFigurita.elementos.filter { it.estaOnFire }
                figuritasOnFire.map { it.numero }.shouldContainExactly(numerosFiguritasAOnFire)

                verify(exactly = 1) {
                    mailSenderMock.enviarMail(match<Mail> { mail ->
                        mail.contenido == "¡Se realizó el proceso: Cambio de lote de figuritas a OnFire exitosamente!" &&
                                mail.asunto == "Proceso completado" &&
                                mail.de == "notificaciones@enviar.com" &&
                                mail.para == "admin@worldcapp.com.ar"
                    })
                }


            }
        }

        describe("Actualizar Stock de sobres de los Puntos de Ventas") {
            repoPuntoDeVenta.create(puntoDeVentaActivo)
            val mailSenderMock = mockk<MailSender>(relaxed = true)
            val proceso = ActualizarStockSobresPuntosVenta(repoPuntoDeVenta)

            it("Deberia actualizar el stock de los puntos de venta") {
                val pedido1 = Pedido(50, LocalDate.of(2023, 6, 1), true)
                val pedido2 = Pedido(30, LocalDate.of(2023, 6, 5), true)

                puntoDeVentaActivo.agregarPedido(pedido1)
                puntoDeVentaActivo.agregarPedido(pedido2)
                puntoDeVentaActivo.actualizarStockSobres()
                proceso.ejecutar(mailSenderMock)

                puntoDeVentaActivo.stockSobres shouldBe 130


                verify(exactly = 1) {
                    mailSenderMock.enviarMail(match<Mail> { mail ->
                        mail.contenido == "¡Se realizó el proceso: Actualizar Stock de sobres en punto de venta exitosamente!" &&
                                mail.asunto == "Proceso completado" &&
                                mail.de == "notificaciones@enviar.com" &&
                                mail.para == "admin@worldcapp.com.ar"
                    }
                    )
                }
            }
        }
    }
})