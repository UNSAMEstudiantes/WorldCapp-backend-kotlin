package ar.edu.unsam.algo3.domain

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
import org.junit.jupiter.api.assertThrows
import org.uqbar.geodds.Point
import java.time.LocalDate


class UsuarioSpec : DescribeSpec({


    isolationMode = IsolationMode.InstancePerTest

    describe("Test de Usuarios") {

        val ubiGeografica = Point(122, 200)
        val direccion1 = Direccion("Buenos Aires", "San Martin", "calle", 2345, ubiGeografica)
        val seleccionArgentina = Seleccion("Argentina", Confederacion.UEFA, 3, 2)
        val seleccionBrasil = Seleccion("Brasil", Confederacion.CONMEBOL, 5, 10)
        val seleccionFrancia = Seleccion("Francia", Confederacion.UEFA, 2, 3)
        val fechaNacimientoDefault = LocalDate.now().minusYears(20)
        val fechaNacimientoMessi = LocalDate.now().minusYears(35)
        val fechaNacimientoMbappe = LocalDate.now().minusYears(19)
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
        val mbappe = Jugador(
            "killean",
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
        val figuritaMessi = Figurita(10, Impresion.BAJA, true, messi)
        val figuritaMbappe = Figurita(8, Impresion.BAJA, true, mbappe)

        describe("Testeando edades de usuarios") {

            val tipoDeUsuario1 = UsuarioPar()

            it("un usuario nacido en el proximo hace 23 años tiene 22 años") {
                //arrang
                val fechaDeNacimiento1MesDespues = LocalDate.now().plusMonths(1).minusYears(23)
                val usuarioQueCumple23ElProximoMes = Usuario(
                    "juan",
                    "lopez",
                    "jl213",
                    fechaDeNacimiento1MesDespues,
                    "juan@mail.com",
                    direccion1,
                    12.0,
                    tipoDeUsuario1
                )
                //assert
                usuarioQueCumple23ElProximoMes.edad() shouldBe 22
            }
            it("un usuario nacido en el mes pasado hace 23 años tiene 23 años") {
                //arrang
                val fechaDeNacimiento1MesAantes = LocalDate.now().minusMonths(1).minusYears(23)
                val usuarioQueCumplio23ElMesPasado = Usuario(
                    "juan",
                    "lopez",
                    "jl213",
                    fechaDeNacimiento1MesAantes,
                    "juan@mail.com",
                    direccion1,
                    12.0,
                    tipoDeUsuario1
                )
                //assert
                usuarioQueCumplio23ElMesPasado.edad() shouldBe 23
            }
            it("un usuario nacido el dia de ayer  hace 23 años tiene 23 años") {
                //arrang
                val fechaDeNacimiento1DiaAntes = LocalDate.now().minusDays(1).minusYears(23)
                val usuarioQueCumplio23ayer = Usuario(
                    "juan",
                    "lopez",
                    "jl213",
                    fechaDeNacimiento1DiaAntes,
                    "juan@mail.com",
                    direccion1,
                    12.0,
                    tipoDeUsuario1
                )
                //assert
                usuarioQueCumplio23ayer.edad() shouldBe 23
            }
            it("un usuario nacido el dia de mañana  hace 23 años tiene 22 años") {
                //arrang
                val fechaDeNacimiento1DiaDespues = LocalDate.now().plusDays(1).minusYears(23)
                val usuarioQueCumple23Maniana = Usuario(
                    "juan",
                    "lopez",
                    "jl213",
                    fechaDeNacimiento1DiaDespues,
                    "juan@mail.com",
                    direccion1,
                    12.0,
                    tipoDeUsuario1
                )
                //assert
                usuarioQueCumple23Maniana.edad() shouldBe 22
            }
        }
        describe("Testeando tipos de usuarios") {
            val usuario1 = Usuario(
                "juan",
                "lopez",
                "jl213",
                fechaNacimientoDefault,
                "juan@mail.com",
                direccion1,
                12.0,
                tipoDeUsuario = UsuarioDesprendido()
            )
            it("El usuario puede cambiar de tipo cuando desee") {
                val juan = Usuario(
                    "juan",
                    "lopez",
                    "jl213",
                    fechaNacimientoDefault,
                    "juan@mail.com",
                    direccion1,
                    12.0,
                    UsuarioNacionalista(mutableListOf(seleccionArgentina))
                )


                juan.tipoDeUsuario shouldBe instanceOf<UsuarioNacionalista>()

                juan.tipoDeUsuario = UsuarioDesprendido()

                juan.tipoDeUsuario shouldBe instanceOf<UsuarioDesprendido>()
            }

            describe("Tests de tipo UsuarioPar") {

                val fakeMessi = Jugador(
                    "Lionel",
                    "Messi",
                    fechaNacimientoMessi,
                    11,
                    seleccionArgentina,
                    LocalDate.of(2000, 12, 20),
                    160.0,
                    60.0,
                    Delantero,
                    true,
                    100
                )
                val figuritaPar = Figurita(8, Impresion.ALTA, true, mbappe)
                val figuritaImpar = Figurita(9, Impresion.ALTA, false, fakeMessi)
                it("Si la figurita es par, no la regala") {
                    UsuarioPar().regalaFigurita(figuritaPar, usuario1) shouldBe false
                }

                it("Si el jugador tiene camiseta par, no la regala") {
                    UsuarioPar().regalaFigurita(figuritaPar, usuario1) shouldBe false
                }

                it("Si la seleccion del jugador tiene copas par, no la regala") {
                    UsuarioPar().regalaFigurita(figuritaPar, usuario1) shouldBe false
                }

                it("Si la figurita no es par, el jugador no tiene camiseta par y la seleccion no tiene copas par, la regala") {
                    UsuarioPar().regalaFigurita(figuritaImpar, usuario1) shouldBe true
                }
            }

            describe("Tests de tipo UsuarioNacionalista") {
                val tipoUsuario = UsuarioNacionalista(mutableListOf(seleccionArgentina))
                usuario1.tipoDeUsuario = tipoUsuario

                it("El UsuarioNacionalista tiene selecciones favoritas") {
                    tipoUsuario.seleccionesFavoritas.size shouldBe 1
                }

                it("El usuario puede agregar selecciones a sus selecciones favoritas") {
                    tipoUsuario.agregarSeleccionFavorita(seleccionBrasil)
                    tipoUsuario.seleccionesFavoritas.contains(seleccionBrasil) shouldBe true
                }

                it("Si la seleccion del jugador pertenece a las favoritas del usuario, no regala figurita") {
                    tipoUsuario.regalaFigurita(figuritaMessi, usuario1) shouldBe false
                }

                it("Si la seleccion del jugador no pertenece a las favoritas del usuario, regala la figurita") {
                    tipoUsuario.regalaFigurita(figuritaMbappe, usuario1) shouldBe true
                }
            }

            describe("Tests de tipo UsuarioConservador") {
                val figuritaMessiv2 = Figurita(10, Impresion.ALTA, true, messi)

                it("Si el album esta lleno y el nivel de impresion es alto, regala figurita") {
                    UsuarioConservador().regalaFigurita(figuritaMessiv2, usuario1) shouldBe true
                }

                it("Si el album esta lleno y el nivel de impresion no es alto, no regala figurita") {
                    UsuarioConservador().regalaFigurita(figuritaMbappe, usuario1) shouldBe false
                }
                it("si el album no esta lleno y el nivel de imporesion es alto") {
                    UsuarioConservador().regalaFigurita(figuritaMbappe, usuario1) shouldBe false
                }
                it("Si el album no esta lleno y el nivel de impresion es alto, no regala figurita") {
                    usuario1.registrarFiguritaFaltante(figuritaMessiv2)
                    UsuarioConservador().regalaFigurita(figuritaMessiv2, usuario1) shouldBe false
                }

            }

            describe("Tests de tipo UsuarioFanatico") {
                val fechaNacimientoMasche = LocalDate.now().minusYears(35)
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
                val figuritaMascheLeyenda = Figurita(5, Impresion.MEDIA, true, masche)
                val tipoUsuario = UsuarioFanatico(messi)
                usuario1.tipoDeUsuario = tipoUsuario
                it("Si la figurita es la de su jugador preferido, no la regala") {
                    tipoUsuario.regalaFigurita(figuritaMessi, usuario1) shouldBe false
                }

                it("Si la figurita es de un jugador leyenda, no la regala") {
                    tipoUsuario.regalaFigurita(figuritaMascheLeyenda, usuario1) shouldBe false
                }

                it("Si la figurita no es la de su jugador preferido y tampoco uno leyenda, regala figurita") {
                    tipoUsuario.regalaFigurita(figuritaMbappe, usuario1) shouldBe true
                }

            }

            describe("Tests de tipo UsuarioDesprendido") {
                it("Regala cualquier figurita") {
                    UsuarioDesprendido().regalaFigurita(figuritaMessi, usuario1) shouldBe true
                    UsuarioDesprendido().regalaFigurita(figuritaMbappe, usuario1) shouldBe true
                }
            }

            describe("Tests de tipo UsuarioApostador") {
                //val enzo = Jugador(5, seleccionArgentina, 2022, 18, false, fechaNacimientoDefault)
                val enzo = Jugador(
                    "Enzo",
                    "Fernandez",
                    LocalDate.of(2003, 12, 20),
                    5,
                    seleccionArgentina,
                    LocalDate.of(2022, 12, 20),
                    160.0,
                    60.0,
                    Delantero,
                    false,
                    18
                )
                val figuritaEnzoPromesa = Figurita(5, Impresion.MEDIA, false, enzo)
                val figuritaMessiNotOnFire = Figurita(10, Impresion.BAJA, false, messi)
                it("Si la figurita esta on fire, no la regala") {
                    UsuarioApostador().regalaFigurita(figuritaMessi, usuario1) shouldBe false
                }

                it("Si el jugador es promesa, no la regala") {
                    UsuarioApostador().regalaFigurita(figuritaEnzoPromesa, usuario1) shouldBe false
                }

                it("Si la figurita no esta on fire y el jugador no es promesa, la regala") {
                    UsuarioApostador().regalaFigurita(figuritaMessiNotOnFire, usuario1) shouldBe true
                }
            }

            describe("Tests de tipo UsuarioCambiante") {
                usuario1.tipoDeUsuario = UsuarioCambiante()
                val fechaNacimientoMayor = LocalDate.now().minusYears(30)
                val usuarioMayor = Usuario(
                    "juan",
                    "lopez",
                    "jl213",
                    fechaNacimientoMayor,
                    "juan@mail.com",
                    direccion1,
                    12.0,
                    tipoDeUsuario = UsuarioDesprendido()
                )
                val figuritaFakeMessi = Figurita(5, Impresion.ALTA, false, messi)
                val figuritaFakeMessiBaja = Figurita(5, Impresion.BAJA, false, messi)



                it("Si la edad del usuario es menor o igual a 25, se comporta como desprendidos, regala cualquier figurita") {
                    UsuarioCambiante().regalaFigurita(figuritaMessi, usuario1) shouldBe true
                    UsuarioCambiante().regalaFigurita(figuritaMbappe, usuario1) shouldBe true
                }

                it("Si la edad del usuario es mayor a 25, pasa a comportarse como conservador, regala figuritas de alto nivel de impresion si el album esta lleno") {
                    UsuarioCambiante().regalaFigurita(figuritaFakeMessi, usuarioMayor) shouldBe true
                }

                it("Si la edad del usuario es mayor a 25, pasa a comportarse como conservador, si la figurita no es de nivel de impresion alto y el album esta lleno, no regala") {
                    UsuarioCambiante().regalaFigurita(figuritaFakeMessiBaja, usuarioMayor) shouldBe false
                }


            }

            describe("Tests de tipo UsuarioInteresado") {
                val dibu = Jugador(
                    nombre = "Emiliano",
                    apellido = "Martinez",
                    fechaDeNacimiento = fechaNacimientoDefault,
                    nroDeCamiseta = 23,
                    seleccion = seleccionArgentina,
                    anioDeDebut = LocalDate.of(2021, 5, 15),
                    altura = 1.95,
                    peso = 90.0,
                    Arquero,
                    lider = true,
                    cotizacion = 80
                )
                val fideo = Jugador(
                    nombre = "Angel",
                    apellido = "Di Maria",
                    fechaDeNacimiento = fechaNacimientoDefault,
                    nroDeCamiseta = 11,
                    seleccion = seleccionArgentina,
                    anioDeDebut = LocalDate.of(2008, 9, 6),
                    altura = 1.80,
                    peso = 75.0,
                    Delantero,
                    lider = true,
                    cotizacion = 100
                )

                val griezmann = Jugador(
                    nombre = "Antoine",
                    apellido = "Griezmann",
                    fechaDeNacimiento = fechaNacimientoDefault,
                    nroDeCamiseta = 1,
                    seleccion = seleccionFrancia,
                    anioDeDebut = LocalDate.of(2008, 9, 6),
                    altura = 1.80,
                    peso = 75.0,
                    Delantero,
                    lider = true,
                    cotizacion = 100
                )
                val figuritaMessiTop = figuritaMessi

                val figuritaMbappeTop = figuritaMbappe

                val figuritaNeymarTop = Figurita(
                    numero = 8,
                    nivelDeImpresion = Impresion.BAJA,
                    estaOnFire = true,
                    jugador = ney
                )

                val figuritaDibuNoTop = Figurita(
                    numero = 24,
                    nivelDeImpresion = Impresion.BAJA,
                    estaOnFire = true,
                    jugador = dibu
                )

                val figuritaFideoTop = Figurita(
                    numero = 18,
                    nivelDeImpresion = Impresion.BAJA,
                    estaOnFire = true,
                    jugador = fideo
                )

                val figuritaGriezmannTop = Figurita(
                    numero = 8,
                    nivelDeImpresion = Impresion.BAJA,
                    estaOnFire = true,
                    jugador = griezmann
                )

                val tipoUsuario = UsuarioInteresado()
                val usuarioInteresado = Usuario(
                    "juan",
                    "lopez",
                    "jl213",
                    fechaNacimientoDefault,
                    "juan@mail.com",
                    direccion1,
                    12.0,
                    tipoDeUsuario = tipoUsuario
                )

                usuarioInteresado.registrarFiguritaRepetida(figuritaDibuNoTop)
                usuarioInteresado.registrarFiguritaRepetida(figuritaMessiTop)
                usuarioInteresado.registrarFiguritaRepetida(figuritaMbappeTop)
                usuarioInteresado.registrarFiguritaRepetida(figuritaFideoTop)
                usuarioInteresado.registrarFiguritaRepetida(figuritaGriezmannTop)
                usuarioInteresado.registrarFiguritaRepetida(figuritaNeymarTop)

                it("El UsuarioInteresado tiene un top 5 de jugadores repetidos") {
                    tipoUsuario.topFive(usuarioInteresado).size shouldBe 5
                }

                it("Si la figurita no se encuentra en el top, la regala") {
                    tipoUsuario.regalaFigurita(figuritaDibuNoTop, usuarioInteresado) shouldBe true
                }

                it("Si la figurita se encuentra en el top, no la regala") {
                    tipoUsuario.regalaFigurita(figuritaMessiTop, usuarioInteresado) shouldBe false
                }
            }

        }

        describe("El Usuario crea la lista de figuritas a regalar en base a las repetidas") {
            val usuarioRandom = Usuario(
                "juan",
                "lopez",
                "jl213",
                fechaNacimientoDefault,
                "juan@mail.com",
                direccion1,
                12.0,
                tipoDeUsuario = UsuarioFanatico(messi)
            )

            val figuritaNeymar = Figurita(
                numero = 8,
                nivelDeImpresion = Impresion.BAJA,
                estaOnFire = true,
                jugador = ney
            )

            usuarioRandom.registrarFiguritaRepetida(figuritaMbappe)
            usuarioRandom.registrarFiguritaRepetida(figuritaMessi)
            usuarioRandom.registrarFiguritaRepetida(figuritaNeymar)


            it("Las figuritas a regalar son una lista") {
                usuarioRandom.figuritasARegalar() shouldBe instanceOf<ArrayList<Figurita>>()
            }

            it("La lista de figuritas a regalar, se forma a partir de las repetidas") {
                usuarioRandom.figuritasARegalar().contains(figuritaMbappe) shouldBe true
                usuarioRandom.figuritasARegalar().contains(figuritaNeymar) shouldBe true

                usuarioRandom.figuritasRepetidas.contains(figuritaMbappe) shouldBe true
                usuarioRandom.figuritasRepetidas.contains(figuritaNeymar) shouldBe true
            }
        }

        describe("test sobre si el usuario completo o no el album") {
            val usuario1 = Usuario(
                "juan",
                "lopez",
                "jl213",
                fechaNacimientoDefault,
                "juan@mail.com",
                direccion1,
                12.0,
                tipoDeUsuario = UsuarioDesprendido()
            )
            it("le faltan figuritas por ende el album no esta completo") {

                usuario1.registrarFiguritaFaltante(figuritaMessi)

                usuario1.llenoAlbum() shouldBe false
            }
            it("no le faltan figuritas por ende el album esta completo") {

                usuario1.figuritasFaltantes.clear()


                usuario1.llenoAlbum() shouldBe true
            }
        }
    }



    describe("Test de listas de figuritas faltantes y repetidas") {
        val seleccionArgentina = Seleccion("Argentina", Confederacion.UEFA, 3, 2)
        val ubiGeografica = Point(122, 200)
        val jugador = Jugador(
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
        val direccion1 = Direccion("Buenos Aires", "San Martin", "calle", 2345, ubiGeografica)
        val tipoDeUsuario1 = UsuarioPar()
        val figuritaNumero12 = Figurita(12, Impresion.BAJA, true, jugador)
        val figuritaNumero13 = Figurita(13, Impresion.MEDIA, false, jugador)
        val figuritaNumero16 = Figurita(16, Impresion.ALTA, false, jugador)
        val usuarioSinFiguritasRepetidasNiRegistradas = Usuario(
            "juan",
            "lopez",
            "jl213",
            LocalDate.of(2000, 10, 10),
            "juan@mail.com",
            direccion1,
            12.0,
            tipoDeUsuario1
        )

        it("usuario sin figuritas repetidas no tiene figuritas repetidas ") {
            usuarioSinFiguritasRepetidasNiRegistradas.figuritasRepetidas.clear()
            usuarioSinFiguritasRepetidasNiRegistradas.estaRepetida(figuritaNumero12) shouldBe false

        }
        it("usuario sin figuritas faltantes no tiene figuritas faltantes ") {
            usuarioSinFiguritasRepetidasNiRegistradas.figuritasFaltantes.clear()
            usuarioSinFiguritasRepetidasNiRegistradas.esFaltante(figuritaNumero12) shouldBe false

        }
        it("usuario registra la misma figurita repetida 3 veces") {
            usuarioSinFiguritasRepetidasNiRegistradas.figuritasRepetidas.clear()
            usuarioSinFiguritasRepetidasNiRegistradas.registrarFiguritaRepetida(figuritaNumero12)
            usuarioSinFiguritasRepetidasNiRegistradas.registrarFiguritaRepetida(figuritaNumero12)
            usuarioSinFiguritasRepetidasNiRegistradas.registrarFiguritaRepetida(figuritaNumero12)

            usuarioSinFiguritasRepetidasNiRegistradas.figuritasRepetidas.size shouldBe 3

        }
        it("usuario no puede registrar mas de 1 vez una figurita faltante") {
            usuarioSinFiguritasRepetidasNiRegistradas.figuritasRepetidas.clear()
            usuarioSinFiguritasRepetidasNiRegistradas.registrarFiguritaFaltante(figuritaNumero13)
            usuarioSinFiguritasRepetidasNiRegistradas.registrarFiguritaFaltante(figuritaNumero13)
            usuarioSinFiguritasRepetidasNiRegistradas.registrarFiguritaFaltante(figuritaNumero13)

            usuarioSinFiguritasRepetidasNiRegistradas.figuritasFaltantes.size shouldBe 1

        }
        it("usuario registra como faltante la figurita n13") {
            usuarioSinFiguritasRepetidasNiRegistradas.figuritasFaltantes.clear()
            usuarioSinFiguritasRepetidasNiRegistradas.registrarFiguritaFaltante(figuritaNumero13)

            usuarioSinFiguritasRepetidasNiRegistradas.esFaltante(figuritaNumero13) shouldBe true
        }
        it("usuario registra como repetida la figurita n13") {
            usuarioSinFiguritasRepetidasNiRegistradas.figuritasRepetidas.clear()
            usuarioSinFiguritasRepetidasNiRegistradas.registrarFiguritaRepetida(figuritaNumero16)

            usuarioSinFiguritasRepetidasNiRegistradas.estaRepetida(figuritaNumero16) shouldBe true
        }
        it("usuario no puede registrar una figurita repetida como faltante") {
            usuarioSinFiguritasRepetidasNiRegistradas.figuritasRepetidas.clear()
            usuarioSinFiguritasRepetidasNiRegistradas.figuritasFaltantes.clear()
            usuarioSinFiguritasRepetidasNiRegistradas.registrarFiguritaRepetida(figuritaNumero16)
            usuarioSinFiguritasRepetidasNiRegistradas.registrarFiguritaFaltante(figuritaNumero16)
            usuarioSinFiguritasRepetidasNiRegistradas.esFaltante(figuritaNumero16) shouldBe false
        }
        it("usuario no puede registrar una figurita faltante como repetida") {
            usuarioSinFiguritasRepetidasNiRegistradas.figuritasRepetidas.clear()
            usuarioSinFiguritasRepetidasNiRegistradas.figuritasFaltantes.clear()
            usuarioSinFiguritasRepetidasNiRegistradas.registrarFiguritaFaltante(figuritaNumero16)
            usuarioSinFiguritasRepetidasNiRegistradas.registrarFiguritaRepetida(figuritaNumero16)
            usuarioSinFiguritasRepetidasNiRegistradas.estaRepetida(figuritaNumero16) shouldBe false
        }


    }
    describe("Test de validacion de usuarios") {
        val ubiGeografica = Point(122, 200)
        val seleccionArgentina = Seleccion("Argentina", Confederacion.UEFA, 3, 2)
        val jugador = Jugador(
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
        val direccion1 = Direccion("Buenos Aires", "San Martin", "calle", 2345, ubiGeografica)
        val tipoDeUsuario1 = UsuarioPar()
        it("usuario con todos sus campos String llenos es valido") {
            val usuarioConNombreEnBlanco = Usuario(
                "juan",
                "lopez",
                "jl213",
                LocalDate.of(2000, 10, 10),
                "juan@mail.com",
                direccion1,
                12.0,
                tipoDeUsuario1
            )

            usuarioConNombreEnBlanco.esValido() shouldBe true
        }

        it("usuario con nombre en blanco es invalido") {
            val usuarioConNombreEnBlanco = Usuario(
                "",
                "lopez",
                "jl213",
                LocalDate.of(2000, 10, 10),
                "juan@mail.com",
                direccion1,
                12.0,
                tipoDeUsuario1
            )

            usuarioConNombreEnBlanco.esValido() shouldBe false
        }
        it("usuario con mail en blanco es invalido") {
            val usuarioConNombreEnBlanco = Usuario(
                "", "lopez", "jl213", LocalDate.of(2000, 10, 10), "", direccion1, 12.0, tipoDeUsuario1
            )

            usuarioConNombreEnBlanco.esValido() shouldBe false
        }
        it("usuario con username en blanco es invalido") {
            val usuarioConNombreEnBlanco = Usuario(
                "juan",
                "lopez",
                "",
                LocalDate.of(2000, 10, 10),
                "juan@mail.com",
                direccion1,
                12.0,
                tipoDeUsuario1
            )

            usuarioConNombreEnBlanco.esValido() shouldBe false
        }
        it("usuario con apellido en blanco es invalido") {
            val usuarioConNombreEnBlanco = Usuario(
                "juan",
                "",
                "jl213",
                LocalDate.of(2000, 10, 10),
                "juan@mail.com",
                direccion1,
                12.0,
                tipoDeUsuario1
            )

            usuarioConNombreEnBlanco.esValido() shouldBe false
        }

    }
    describe("Test de distancia que un usuario considera cercana") {
        describe("un usuario al que su cantidad de km maxima que concidera cercano son 5km") {
            val seleccionArgentina = Seleccion("Argentina", Confederacion.UEFA, 3, 2)
            val ubiGeograficaEnUNSANCyT = Point(-34.57864449843753, -58.52688543784881)
            val jugador = Jugador(
                "Lionel",
                "Messi",
                LocalDate.of(2000, 11, 12),
                9,
                seleccionArgentina,
                LocalDate.of(2000, 11, 12),
                160.0,
                60.0,
                Delantero,
                true,
                200
            )
            val figuritaMessi = Figurita(11, Impresion.ALTA, true, jugador)
            val direccionUNSAMCyT =
                Direccion("Buenos Aires", "San Martin", "av. 25 de mayo", 1169, ubiGeograficaEnUNSANCyT)
            val tipoDeUsuario1 = UsuarioPar()
            val usuarioQueViveEnLaUNSAMCyT = Usuario(
                "juan",
                "lopez",
                "jl213",
                LocalDate.of(2000, 10, 10),
                "juan@mail.com",
                direccionUNSAMCyT,
                5.0,
                tipoDeUsuario1
            )

            val ubiGeograficaA5km = Point(-34.58288676038703, -58.58122657445346)
            val direccionA5km =
                Direccion("Buenos Aires", "San Martin", "av. 25 de mayo", 1169, ubiGeograficaA5km)

            val usuarioQueViveAMenosDe5KM = Usuario(
                "juan",
                "lopez",
                "jl213",
                LocalDate.of(2000, 10, 10),
                "juan@mail.com",
                direccionA5km,
                5.0,
                tipoDeUsuario1

            )

            val usuarioQueNoTieneAMessi = Usuario(
                "carlos",
                "boka",
                "boka",
                LocalDate.of(2000, 10, 10),
                "boka@mail.com",
                direccionA5km,
                5.0,
                tipoDeUsuario1

            )

            it("PizzeriaLofi esta a 3.33km ") {

                val ubiPizzeriaLofi = Point(-34.58074945290149, -58.563153784488854)
                val mercadoSanMartin =
                    Direccion("Buenos Aires", "San Martin", "Billinghurst ", 5621, ubiPizzeriaLofi)

                usuarioQueViveEnLaUNSAMCyT.direccion.ubiGeografica.distance(mercadoSanMartin.ubiGeografica)
                    .coerceIn(3.32, 3.34)
            }

            it("con una direccion que esta a 4.9km ") {
                val ubiGeograficaAMenosDe5km = Point(-34.58269628233307, -58.58017963512021)
                val direccionAMenosDe5km =
                    Direccion("Buenos Aires", "San Martin", "av. 25 de mayo", 1169, ubiGeograficaAMenosDe5km)

                val usuarioQueViveA5KM = Usuario(
                    "juan",
                    "lopez",
                    "jl213",
                    LocalDate.of(2000, 10, 10),
                    "juan@mail.com",
                    direccionAMenosDe5km,
                    5.0,
                    tipoDeUsuario1
                )

                usuarioQueViveEnLaUNSAMCyT.esCercano(usuarioQueViveA5KM) shouldBe true
            }
            it("con una direccion que esta a 5km ") {

                val ubiGeograficaA5km = Point(-34.58288676038703, -58.58122657445346)
                val direccionA5km =
                    Direccion("Buenos Aires", "San Martin", "av. 25 de mayo", 1169, ubiGeograficaA5km)

                val usuarioQueViveAMenosDe5KM = Usuario(
                    "juan",
                    "lopez",
                    "jl213",
                    LocalDate.of(2000, 10, 10),
                    "juan@mail.com",
                    direccionA5km,
                    5.0,
                    tipoDeUsuario1
                )

                usuarioQueViveEnLaUNSAMCyT.esCercano(usuarioQueViveAMenosDe5KM) shouldBe true

            }
            it("con una direccion que esta a 5.1km ") {
                val ubiGeograficaAMeasDe5km = Point(-34.582926996282346, -58.58133131082526)
                val direccionAMasDe5km =
                    Direccion("Buenos Aires", "San Martin", "av. 25 de mayo", 1169, ubiGeograficaAMeasDe5km)

                val usuarioQueViveAMasDe5KM = Usuario(
                    "juan",
                    "lopez",
                    "jl213",
                    LocalDate.of(2000, 10, 10),
                    "juan@mail.com",
                    direccionAMasDe5km,
                    5.0,
                    tipoDeUsuario1
                )

                usuarioQueViveEnLaUNSAMCyT.esCercano(usuarioQueViveAMasDe5KM) shouldBe false
            }
            describe("usuarioQueViveEnLaUNSAMCyT le pide la figurita de messi a usuarioQueViveAMenosDe5KM") {
                usuarioQueViveEnLaUNSAMCyT.figuritasFaltantes.add(figuritaMessi)
                usuarioQueViveAMenosDe5KM.figuritasRepetidas.add(figuritaMessi)
                usuarioQueViveEnLaUNSAMCyT.comprobarSolicitud(figuritaMessi, usuarioQueViveAMenosDe5KM) shouldBe true
                usuarioQueViveEnLaUNSAMCyT.solicitarFigurita(figuritaMessi, usuarioQueViveAMenosDe5KM)
                usuarioQueViveEnLaUNSAMCyT.esFaltante(figuritaMessi) shouldBe false
            }

            describe("usuarioQueViveEnLaUNSAMCyT le pide la figurita de messi a usuarioQueNoTieneAMessi") {
                usuarioQueViveEnLaUNSAMCyT.figuritasFaltantes.add(figuritaMessi)
                assertThrows<Exception>{usuarioQueViveEnLaUNSAMCyT.solicitarFigurita(figuritaMessi,usuarioQueNoTieneAMessi)}
            }
        }

    }
})
