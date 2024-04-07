package ar.edu.unsam.algo3.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.instanceOf
import io.mockk.mockk
import io.mockk.verify
import org.uqbar.geodds.Point
import java.time.LocalDate

class SolicitudObserverSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    describe("test de SolicitudesObservers") {
        describe("test de reservadasSolicitudObserver") {
            val usuarioAPedir = Usuario(
                "juan",
                "lopez",
                "jl213",
                LocalDate.now(),
                "juan@mail.com",
                Direccion("Buenos Aires", "San Martin", "calle", 2345, Point(122, 200)),
                12.0,
                UsuarioDesprendido()
            )

            val seleccionArgentina = Seleccion("Argentina", Confederacion.OFC, 3, 3)
            val messi = Jugador(
                "Lionel",
                "Messi",
                LocalDate.of(2000, 11, 12),
                10,
                seleccionArgentina,
                LocalDate.of(2000, 11, 12),
                160.0,
                60.0,
                Delantero,
                true,
                200
            )
            val jugMenorAMessi = Jugador(
                "Lionel",
                "Messi",
                LocalDate.of(2000, 11, 12),
                10,
                seleccionArgentina,
                LocalDate.of(2000, 11, 12),
                160.0,
                60.0,
                Delantero,
                false,
                10
            )


            val figuritaConValorAlto = Figurita(10, Impresion.ALTA, true, messi)
            val figuritaConValorBajo = Figurita(22, Impresion.BAJA, false, jugMenorAMessi)
            val figuritaDeMessi = Figurita(10, Impresion.ALTA, true, messi)
            usuarioAPedir.registrarFiguritaRepetida(figuritaConValorAlto)


            describe(" usuario con lista de repetidas vacias") {
                val usuarioSinRepetidas = Usuario(
                    "juan",
                    "lopez",
                    "jl213",
                    LocalDate.now(),
                    "juan@mail.com",
                    Direccion("Buenos Aires", "San Martin", "calle", 2345, Point(122, 200)),
                    12.0,
                    UsuarioDesprendido()
                )
                val reservadasSolicitudObserver = ReservadasSolicitudObserver()

                usuarioSinRepetidas.registrarFiguritaRepetida(figuritaConValorBajo)

                usuarioSinRepetidas.agregarSolicitudObserver(reservadasSolicitudObserver)

                usuarioSinRepetidas.registrarFiguritaFaltante(figuritaConValorAlto)

                reservadasSolicitudObserver.reservarFigurita(figuritaConValorBajo, usuarioSinRepetidas)

                it("el usuario reserva una figurita por lo que deja de estar repetida") {
                    usuarioSinRepetidas.figuritasRepetidas.isEmpty() shouldBe true
                }
                it("el usuario intercambia una figurita de mayor valor a alguna de su lista de reservadas") {

                    usuarioSinRepetidas.solicitarFigurita(figuritaConValorAlto, usuarioAPedir)

                    usuarioSinRepetidas.figuritasRepetidas.contains(figuritaConValorBajo) shouldBe true
                }
                it("se intenta reservar una figurita que no puede estar repetida, pero no se puede") {

                    usuarioSinRepetidas.registrarFiguritaFaltante(figuritaDeMessi)

                    shouldThrow<RepetidasExeption> {
                        reservadasSolicitudObserver.reservarFigurita(figuritaDeMessi, usuarioSinRepetidas)
                    }

                }
                it("si ninguna de las figuritas reservadas tienen un valor menor o igual a la figurita solicitada no sucede nada"){

                    usuarioSinRepetidas.figuritasRepetidas.clear()
                    usuarioSinRepetidas.figuritasFaltantes.clear()
                    reservadasSolicitudObserver.listaReservadas.clear()

                    usuarioSinRepetidas.registrarFiguritaFaltante(figuritaConValorBajo)

                    usuarioAPedir.registrarFiguritaRepetida(figuritaConValorBajo)

                    usuarioSinRepetidas.registrarFiguritaRepetida(figuritaConValorAlto)

                    reservadasSolicitudObserver.reservarFigurita(figuritaConValorAlto, usuarioSinRepetidas)

                    usuarioSinRepetidas.solicitarFigurita(figuritaConValorBajo, usuarioAPedir)

                    usuarioSinRepetidas.figuritasRepetidas.isEmpty() shouldBe true
                }
                it("si ninguna de las figuritas es regalable no se registra ninguna repetida"){

                    val usuarioPar= UsuarioPar()
                    usuarioSinRepetidas.apply { tipoDeUsuario=usuarioPar }

                    usuarioSinRepetidas.solicitarFigurita(figuritaConValorAlto, usuarioAPedir)

                    usuarioSinRepetidas.figuritasRepetidas.isEmpty() shouldBe true

                }



            }
            describe("usuario con lista de repetidas no vacia") {
                val usuarioConRepetidas = Usuario(
                    "juan",
                    "lopez",
                    "jl213",
                    LocalDate.now(),
                    "juan@mail.com",
                    Direccion("Buenos Aires", "San Martin", "calle", 2345, Point(122, 200)),
                    12.0,
                    UsuarioDesprendido()
                )
                val reservadasSolicitudObserver = ReservadasSolicitudObserver()

                usuarioConRepetidas.registrarFiguritaRepetida(figuritaConValorBajo)
                usuarioConRepetidas.agregarSolicitudObserver(reservadasSolicitudObserver)

                usuarioConRepetidas.registrarFiguritaFaltante(figuritaConValorAlto)

                reservadasSolicitudObserver.reservarFigurita(figuritaConValorBajo, usuarioConRepetidas)

                it("el usuario intercambia una figurita de mayor valor a alguna de su lista de reservadas pero al no tener la lista vacia, ocurre nada") {


                    usuarioConRepetidas.registrarFiguritaRepetida(figuritaDeMessi)

                    usuarioConRepetidas.solicitarFigurita(figuritaConValorAlto, usuarioAPedir)

                    usuarioConRepetidas.figuritasRepetidas.contains(figuritaConValorBajo) shouldBe false
                }
            }
        }

        describe("Tests de DesprendidoSolicitudObserver") {
            val ubiGeografica = Point(122, 200)
            val direccion1 = Direccion("Buenos Aires", "San Martin", "calle", 2345, ubiGeografica)
            val seleccionArgentina = Seleccion("Argentina", Confederacion.UEFA, 3, 2)
            val seleccionBrasil = Seleccion("Brasil", Confederacion.CONMEBOL, 5, 10)
            val seleccionFrancia = Seleccion("Francia", Confederacion.UEFA, 2, 3)
            val seleccionUruguay = Seleccion("Uruguay", Confederacion.CONMEBOL, 2, 3)
            val fechaNacimientoDefault = LocalDate.now().minusYears(20)
            val fechaNacimientoMessi = LocalDate.now().minusYears(35)
            val fechaNacimientoMbappe = LocalDate.now().minusYears(19)
            val fechaNacimientoMasche = LocalDate.now().minusYears(35)
            val messi = Jugador(
                "Lionel",
                "Messi",
                fechaNacimientoMessi,
                10,
                seleccionArgentina,
                LocalDate.of(2000, 12, 20),
                160.0,
                60.0,
                Delantero,
                true,
                200
            )
            val kiki = Jugador(
                "Kylian",
                "Mbappe",
                fechaNacimientoMbappe,
                10,
                seleccionFrancia,
                LocalDate.of(2000, 12, 20),
                160.0,
                60.0,
                Delantero,
                false,
                120
            )
            val ney = Jugador(
                nombre = "Neymar",
                apellido = "da Silva Santos Júnior",
                fechaDeNacimiento = LocalDate.of(1992, 1, 5),
                nroDeCamiseta = 10,
                seleccion = seleccionBrasil,
                anioDeDebut = LocalDate.of(2012, 12, 20),
                altura = 173.0,
                peso = 70.0,
                Delantero,
                lider = false,
                cotizacion = 222
            )
            val masche = Jugador(
                "Lionel",
                "Mascherano",
                fechaNacimientoMasche,
                5,
                seleccionArgentina,
                LocalDate.of(2003, 12, 20),
                160.0,
                60.0,
                MedioCampista,
                true,
                200
            )

            val figuritaMasche = Figurita(5, Impresion.MEDIA, true, masche)
            val figuritaMessi = Figurita(10, Impresion.BAJA, true, messi)
            val figuritaMbappe = Figurita(8, Impresion.BAJA, true, kiki)
            val figuritaNeymar = Figurita(11, Impresion.BAJA, true, ney)

            val usuarioAPedir = Usuario(
                "juan",
                "lopez",
                "jl213",
                LocalDate.now(),
                "juan@mail.com",
                Direccion("Buenos Aires", "San Martin", "calle", 2345, Point(122, 200)),
                12.0,
                UsuarioDesprendido()
            )

            usuarioAPedir.registrarFiguritaRepetida(figuritaMbappe)
            usuarioAPedir.registrarFiguritaRepetida(figuritaNeymar)
            usuarioAPedir.registrarFiguritaRepetida(figuritaMessi)

            it("Si el usuario con el intercambio, no lleno el album pero tiene mas de un determinado numero de figuritas a regalar, no se convierte en UsuarioDesprendido") {
                val usuarioAlbumIncompleto = Usuario(
                    "juan",
                    "lopez",
                    "jl213",
                    fechaNacimientoDefault,
                    "juan@mail.com",
                    direccion1,
                    12.0,
                    tipoDeUsuario = UsuarioNacionalista(mutableListOf(seleccionUruguay))
                )

                usuarioAlbumIncompleto.agregarSolicitudObserver(DesprendidoSolicitudObserver(cantidadFiguritasARegalar = 1))
                usuarioAlbumIncompleto.registrarFiguritaFaltante(figuritaMbappe)
                usuarioAlbumIncompleto.registrarFiguritaFaltante(figuritaMessi)
                usuarioAlbumIncompleto.registrarFiguritaRepetida(figuritaNeymar)
                usuarioAlbumIncompleto.registrarFiguritaRepetida(figuritaMasche)

                usuarioAlbumIncompleto.solicitarFigurita(figuritaMbappe, usuarioAPedir)

                usuarioAlbumIncompleto.tipoDeUsuario shouldNotBe UsuarioDesprendido()
            }

            it("Si el usuario con el intercambio, lleno el album pero no tiene mas de un determinado numero de figuritas a regalar, no se convierte en UsuarioDesprendido") {
                val usuarioSinFigusRepetidas = Usuario(
                    "juan",
                    "lopez",
                    "jl213",
                    fechaNacimientoDefault,
                    "juan@mail.com",
                    direccion1,
                    12.0,
                    tipoDeUsuario = UsuarioNacionalista(mutableListOf(seleccionUruguay))
                )

                usuarioSinFigusRepetidas.agregarSolicitudObserver(DesprendidoSolicitudObserver(cantidadFiguritasARegalar = 2))
                usuarioSinFigusRepetidas.registrarFiguritaFaltante(figuritaNeymar)

                usuarioSinFigusRepetidas.solicitarFigurita(figuritaNeymar, usuarioAPedir)

                usuarioSinFigusRepetidas.tipoDeUsuario shouldNotBe UsuarioDesprendido()
            }

            it("Si el usuario con el intercambio, lleno el album y tiene mas de un determinado numero de figuritas a regalar, se convierte en UsuarioDesprendido") {
                val usuarioCompleto = Usuario(
                    "juan",
                    "lopez",
                    "jl213",
                    fechaNacimientoDefault,
                    "juan@mail.com",
                    direccion1,
                    12.0,
                    tipoDeUsuario = UsuarioNacionalista(mutableListOf(seleccionUruguay))
                )

                usuarioCompleto.agregarSolicitudObserver(DesprendidoSolicitudObserver(cantidadFiguritasARegalar = 1))
                usuarioCompleto.registrarFiguritaFaltante(figuritaMessi)
                usuarioCompleto.registrarFiguritaRepetida(figuritaMasche)
                usuarioCompleto.registrarFiguritaRepetida(figuritaNeymar)


                usuarioCompleto.solicitarFigurita(figuritaMessi, usuarioAPedir)

                usuarioCompleto.tipoDeUsuario shouldBe instanceOf<UsuarioDesprendido>()
            }

        }

        describe("Tests de FelicitacionesSolicitudObserver") {
            val ubiGeografica = Point(122, 200)
            val direccion1 = Direccion("Buenos Aires", "San Martin", "calle", 2345, ubiGeografica)
            val seleccionArgentina = Seleccion("Argentina", Confederacion.UEFA, 3, 2)
            val fechaNacimientoMessi = LocalDate.now().minusYears(35)
            val fechaNacimientoMasche = LocalDate.now().minusYears(35)
            val messi = Jugador(
                "Lionel",
                "Messi",
                fechaNacimientoMessi,
                10,
                seleccionArgentina,
                LocalDate.of(2000, 12, 20),
                160.0,
                60.0,
                Delantero,
                true,
                200
            )
            val figuritaMessi = Figurita(10, Impresion.BAJA, true, messi)

            val usuarioAPedir = Usuario(
                "juan",
                "lopez",
                "jl213",
                LocalDate.now(),
                "juan@mail.com",
                Direccion("Buenos Aires", "San Martin", "calle", 2345, Point(122, 200)),
                12.0,
                UsuarioDesprendido()
            )

            val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)

            usuarioAPedir.registrarFiguritaRepetida(figuritaMessi)

            it("Si el usuario llena el album con la figurita obtenida, recibe un mail de felicitaciones") {
                val usuarioCompleto = Usuario(
                    "juan",
                    "lopez",
                    "jl213",
                    fechaNacimientoMasche,
                    "juan@mail.com",
                    direccion1,
                    12.0,
                    tipoDeUsuario = UsuarioDesprendido()
                )

                usuarioCompleto.agregarSolicitudObserver(FelicitacionesSolicitudObserver(mailSender = mockedMailSender))
                usuarioCompleto.registrarFiguritaFaltante(figuritaMessi)
                usuarioCompleto.solicitarFigurita(figuritaMessi, usuarioAPedir)

                verify(exactly = 1) {
                    mockedMailSender.enviarMail(
                        Mail(
                            de = "info@worldcapp.com.ar",
                            para = usuarioCompleto.mail,
                            asunto = "Felicitaciones, completaste el album!",
                            contenido = "Con la siguiente figurita completaste el album: ${figuritaMessi.numero}, ${figuritaMessi.jugador.nombre} ${figuritaMessi.jugador.apellido}, ${figuritaMessi.jugador.seleccion}, ${figuritaMessi.valoracion()}"
                        )
                    )
                }

            }

        }

        describe("Test de distanciaSolicitudObserver") {
            val ubiGeografica = Point(122, 200)
            val direccion1 = Direccion("Buenos Aires", "San Martin", "calle", 2345, ubiGeografica)
            val seleccionArgentina = Seleccion("Argentina", Confederacion.UEFA, 3, 15)
            val fechaNacimientoMessi = LocalDate.now().minusYears(35)
            val seleccionBrasil = Seleccion("Brasil", Confederacion.CONMEBOL, 5, 10)
            val messi = Jugador(
                "Lionel",
                "Messi",
                fechaNacimientoMessi,
                10,
                seleccionArgentina,
                LocalDate.of(2000, 12, 20),
                160.0,
                60.0,
                Delantero,
                true,
                200
            )

            val ney = Jugador(
                "Neymar",
                "da Silva Santos Júnior",
                LocalDate.of(1992, 1, 5),
                10,
                seleccionBrasil,
                LocalDate.of(2012, 12, 20),
                173.0,
                70.0,
                Delantero,
                false,
                222
            )

            val dibu = Jugador(
                "Emiliano",
                "Martinez",
                LocalDate.of(1992, 9, 2),
                23,
                seleccionArgentina,
                LocalDate.of(2011, 4, 27),
                1.94,
                84.0,
                Arquero,
                false,
                10000000
            )
            val enzoFernandez = Jugador(
                "Enzo",
                "Fernandez",
                LocalDate.of(1996, 2, 14),
                24,
                seleccionArgentina,
                LocalDate.of(2016, 3, 24),
                1.78,
                70.0,
                MedioCampista,
                true,
                150000000
            )

            val viniciusJr = Jugador(
                "Vinicius",
                "Jr",
                LocalDate.of(2000, 7, 12),
                20,
                seleccionBrasil,
                LocalDate.of(2017, 5, 13),
                1.76,
                73.0,
                Delantero,
                false,
                6000000
            )

            val rodrygo = Jugador(
                "Rodrygo",
                "",
                LocalDate.of(2001, 1, 9),
                27,
                seleccionBrasil,
                LocalDate.of(2017, 11, 10),
                1.74,
                63.0,
                Delantero,
                false,
                70000000,
            )

            val richarlison = Jugador(
                "Richarlison",
                "",
                LocalDate.of(1997, 5, 10),
                7,
                seleccionBrasil,
                LocalDate.of(2015, 7, 19),
                1.84,
                71.0,
                Delantero,
                false,
                80000000,
            )
            val figuritaMessi = Figurita(10, Impresion.BAJA, true, messi)
            val figuritaNeymar = Figurita(11, Impresion.BAJA, true, ney)
            val figuritaDibu = Figurita(12, Impresion.BAJA, true, dibu)
            val figuritaEnzo = Figurita(13, Impresion.BAJA, true, enzoFernandez)
            val figuritaVini = Figurita(14, Impresion.BAJA, true, viniciusJr)
            val figuritaRodry = Figurita(15, Impresion.BAJA, true, rodrygo)
            val figuritaRichar = Figurita(15, Impresion.BAJA, true, richarlison)

            val usuarioAcambiar = Usuario(
                "carlos",
                "boka",
                "jl213",
                fechaNacimientoMessi,
                "juan@mail.com",
                direccion1,
                12.0,
                UsuarioDesprendido()
            )

            val usuarioAPedir = Usuario(
                "juan",
                "lopez",
                "jl213",
                LocalDate.now(),
                "juan@mail.com",
                direccion1,
                12.0,
                UsuarioDesprendido()
            )

            usuarioAcambiar.agregarSolicitudObserver(DistanciaSolicitudObserver())

            it("usuario a cambiar triplica su distancia ya que le faltan 5 figuritas o menos") {
                usuarioAcambiar.registrarFiguritaFaltante(figuritaNeymar)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaMessi)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaDibu)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaEnzo)
                usuarioAPedir.registrarFiguritaRepetida(figuritaMessi)
                usuarioAcambiar.solicitarFigurita(figuritaMessi, usuarioAPedir)
                usuarioAcambiar.distanciaMaxima shouldBe 36.0
            }

            it("al pedir dos figuritas no se triplica dos veces ya que se elimina la accion del usuario") {
                usuarioAcambiar.registrarFiguritaFaltante(figuritaNeymar)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaMessi)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaDibu)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaEnzo)
                usuarioAPedir.registrarFiguritaRepetida(figuritaMessi)
                usuarioAPedir.registrarFiguritaRepetida(figuritaDibu)
                usuarioAcambiar.solicitarFigurita(figuritaMessi, usuarioAPedir)
                usuarioAcambiar.solicitarFigurita(figuritaDibu, usuarioAPedir)
                usuarioAcambiar.distanciaMaxima shouldBe 36.0
            }

            it("al tener mas de 5 figuritas no se triplica la distancia") {
                usuarioAcambiar.registrarFiguritaFaltante(figuritaNeymar)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaMessi)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaDibu)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaEnzo)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaVini)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaRodry)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaRichar)

                usuarioAPedir.registrarFiguritaRepetida(figuritaMessi)

                usuarioAcambiar.solicitarFigurita(figuritaMessi, usuarioAPedir)

                usuarioAcambiar.distanciaMaxima shouldBe 12.0
            }

            it("al volver a habilitarse se vuelve a triplicar la distancia") {
                usuarioAcambiar.registrarFiguritaFaltante(figuritaNeymar)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaMessi)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaDibu)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaEnzo)
                usuarioAPedir.registrarFiguritaRepetida(figuritaMessi)
                usuarioAPedir.registrarFiguritaRepetida(figuritaDibu)
                usuarioAcambiar.solicitarFigurita(figuritaMessi, usuarioAPedir)
                usuarioAcambiar.agregarSolicitudObserver(DistanciaSolicitudObserver())
                usuarioAcambiar.solicitarFigurita(figuritaDibu, usuarioAPedir)
                usuarioAcambiar.distanciaMaxima shouldBe 108.0
            }
        }

        describe("test de NacionalistaSolicitudObserver ") {
            val ubiGeografica = Point(122, 200)
            val direccion1 = Direccion("Buenos Aires", "San Martin", "calle", 2345, ubiGeografica)
            val seleccionArgentina = Seleccion("Argentina", Confederacion.UEFA, 3, 15)
            val fechaNacimientoMessi = LocalDate.now().minusYears(35)
            val seleccionBrasil = Seleccion("Brasil", Confederacion.CONMEBOL, 5, 10)
            val messi = Jugador(
                "Lionel",
                "Messi",
                fechaNacimientoMessi,
                10,
                seleccionArgentina,
                LocalDate.of(2000, 12, 20),
                160.0,
                60.0,
                Delantero,
                true,
                200
            )

            val ney = Jugador(
                "Neymar",
                "da Silva Santos Júnior",
                LocalDate.of(1992, 1, 5),
                10,
                seleccionBrasil,
                LocalDate.of(2012, 12, 20),
                173.0,
                70.0,
                Delantero,
                false,
                222
            )

            val dibu = Jugador(
                "Emiliano",
                "Martinez",
                LocalDate.of(1992, 9, 2),
                23,
                seleccionArgentina,
                LocalDate.of(2011, 4, 27),
                1.94,
                84.0,
                Arquero,
                false,
                10000000
            )
            val enzoFernandez = Jugador(
                "Enzo",
                "Fernandez",
                LocalDate.of(1996, 2, 14),
                24,
                seleccionArgentina,
                LocalDate.of(2016, 3, 24),
                1.78,
                70.0,
                MedioCampista,
                true,
                150000000
            )

            val figuritaMessi = Figurita(10, Impresion.BAJA, true, messi)
            val figuritaNeymar = Figurita(11, Impresion.BAJA, true, ney)
            val figuritaDibu = Figurita(12, Impresion.BAJA, true, dibu)
            val figuritaEnzo = Figurita(13, Impresion.BAJA, true, enzoFernandez)

            val usuarioAcambiar = Usuario(
                "carlos",
                "boka",
                "jl213",
                fechaNacimientoMessi,
                "juan@mail.com",
                direccion1,
                12.0,
                UsuarioDesprendido()
            )

            val usuarioAPedir = Usuario(
                "juan",
                "lopez",
                "jl213",
                LocalDate.now(),
                "juan@mail.com",
                direccion1,
                12.0,
                UsuarioDesprendido()
            )

            usuarioAcambiar.agregarSolicitudObserver(NacionalistaSolicitudObserver())

            it(
                " usuarioACambiar le pide 3 figurias de la seleccion argentina a usuarioAPedir por que " +
                        "lo cambia a ser nacionalista de la seleccion argentina"
            )
            {

                usuarioAcambiar.registrarFiguritaFaltante(figuritaNeymar)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaMessi)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaDibu)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaEnzo)
                usuarioAPedir.registrarFiguritaRepetida(figuritaMessi)
                usuarioAPedir.registrarFiguritaRepetida(figuritaDibu)
                usuarioAPedir.registrarFiguritaRepetida(figuritaEnzo)
                usuarioAcambiar.solicitarFigurita(figuritaMessi, usuarioAPedir)
                usuarioAcambiar.solicitarFigurita(figuritaDibu, usuarioAPedir)
                usuarioAcambiar.solicitarFigurita(figuritaEnzo, usuarioAPedir)

                usuarioAcambiar.tipoDeUsuario shouldBe instanceOf<UsuarioNacionalista>()

            }

            it(
                " usuarioACambiar le pide 2 figuritas de la seleccion argentina a usuarioAPedir por lo cual " +
                        "no cambia"
            )
            {

                usuarioAcambiar.registrarFiguritaFaltante(figuritaNeymar)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaMessi)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaDibu)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaEnzo)
                usuarioAPedir.registrarFiguritaRepetida(figuritaMessi)
                usuarioAPedir.registrarFiguritaRepetida(figuritaDibu)
                usuarioAcambiar.solicitarFigurita(figuritaMessi, usuarioAPedir)
                usuarioAcambiar.solicitarFigurita(figuritaDibu, usuarioAPedir)

                usuarioAcambiar.tipoDeUsuario shouldBe instanceOf<UsuarioDesprendido>()
            }

            it(
                " usuarioACambiar le pide 2 figuritas de la seleccion argentina  y una de brasil a usuarioAPedir por lo cual " +
                        "no cambia"
            ) {
                usuarioAcambiar.registrarFiguritaFaltante(figuritaNeymar)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaMessi)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaDibu)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaEnzo)
                usuarioAPedir.registrarFiguritaRepetida(figuritaMessi)
                usuarioAPedir.registrarFiguritaRepetida(figuritaDibu)
                usuarioAPedir.registrarFiguritaRepetida(figuritaNeymar)
                usuarioAcambiar.solicitarFigurita(figuritaMessi, usuarioAPedir)
                usuarioAcambiar.solicitarFigurita(figuritaDibu, usuarioAPedir)
                usuarioAcambiar.solicitarFigurita(figuritaNeymar, usuarioAPedir)

                usuarioAcambiar.tipoDeUsuario shouldBe instanceOf<UsuarioDesprendido>()
            }

            it(
                " usuarioACambiar le pide 3 figuritas de la seleccion argentina a usuarioAPedir pero completo el " +
                        " album por lo cual no cambia"
            ) {
                usuarioAcambiar.registrarFiguritaFaltante(figuritaMessi)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaDibu)
                usuarioAcambiar.registrarFiguritaFaltante(figuritaEnzo)
                usuarioAPedir.registrarFiguritaRepetida(figuritaMessi)
                usuarioAPedir.registrarFiguritaRepetida(figuritaDibu)
                usuarioAPedir.registrarFiguritaRepetida(figuritaEnzo)
                usuarioAcambiar.solicitarFigurita(figuritaMessi, usuarioAPedir)
                usuarioAcambiar.solicitarFigurita(figuritaDibu, usuarioAPedir)
                usuarioAcambiar.solicitarFigurita(figuritaEnzo, usuarioAPedir)

                usuarioAcambiar.tipoDeUsuario shouldBe instanceOf<UsuarioDesprendido>()

            }


        }

    }

})
