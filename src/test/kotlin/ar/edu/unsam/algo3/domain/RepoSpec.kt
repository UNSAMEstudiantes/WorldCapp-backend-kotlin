package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.domain.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.assertThrows
import org.uqbar.geodds.Point
import java.time.LocalDate

class RepoTest : DescribeSpec({

    val repoSeleccion = Repositorio<Seleccion>()
    val repoFigurita = Repositorio<Figurita>()
    val repoJugador = Repositorio<Jugador>()
    val repoUsuario = Repositorio<Usuario>()
    val repoPuntoDeVenta = Repositorio<PuntoDeVenta>()

    val tipoDeUsuario1 = UsuarioPar()
    val argentina = Seleccion("Argentina", Confederacion.CONMEBOL, 3, 15)
    val mexico = Seleccion("México", Confederacion.CONCACAF, 0, 0)
    val venezuela = Seleccion("Venezuela", Confederacion.CONMEBOL, 0, 1)
    val ubiGeografica = Point(122, 200)
    val direccion1 = Direccion("Buenos Aires", "San Martin", "calle", 2345, ubiGeografica)
    val fechaDeNacimiento1MesDespues = LocalDate.now().plusMonths(1).minusYears(23)
    val usuarioQueCumple23ElProximoMes = Usuario(
        "juan", "lopez", "jl213", fechaDeNacimiento1MesDespues, "juan@mail.com",
        direccion1, 12.0, tipoDeUsuario1
    )
    val puntoDeVenta = Kiosko(
        nombre = "random",
        direccion = direccion1,
        stockSobres = 50,
        tieneEmpleados = false
    )
    val delanteroCampeonDelMundoCami10 = Jugador(
        "Lionel", "Messi", LocalDate.of(1987, 6, 24),
        10, argentina, LocalDate.of(2005, 8, 17), 1.70, 72.5, Delantero, true, 100000000
    )
    val figuritaMessi = Figurita(10, Impresion.BAJA, true, delanteroCampeonDelMundoCami10)



    describe("Repositorio Seleccion") {

        describe("create") {

            it("En un respositorio vacio, agrego dos selecciones") {
                repoSeleccion.create(argentina)
                repoSeleccion.create(mexico)
                repoSeleccion.getById(1) shouldBe argentina
                repoSeleccion.getById(2) shouldBe mexico
            }

            it("verifico  la cantidad de elementos en la repo selección") {
                repoSeleccion.elementos.size shouldBe 2
            }
            it("Intento crear en elemento con ID existente en el repo y recibo expeción") {
                assertThrows<ElementoYaExisteException> { repoSeleccion.create(mexico) }
            }

        }
        describe("update") {
            it("quiero actualizar la cantidad de copas del mundo") {
                argentina.copasDelMundo = 4
                repoSeleccion.update(argentina)
                argentina.copasDelMundo shouldBe 4
            }
        }
        describe("delete") {
            it("elimino una seleccion de la lista y verifico que ya no este") {
                repoSeleccion.delete(mexico)
                repoSeleccion.search("México") shouldNotBe mutableListOf<Seleccion>(mexico)
                repoSeleccion.elementos.size shouldBe 1
            }
            it("Intento actualizar un elemento que no está en la lista y recibo expeción") {
                assertThrows<ElementoInexistenteException> { repoSeleccion.update(venezuela) }
            }
        }

        describe("getById") {
            it("debería devolver el usuario con id 1") {
                val usuarioEncontrado = repoSeleccion.getById(1)
                usuarioEncontrado.id shouldBe 1
            }
        }
    }

    describe("busquedas") {


        it("busqueda de figurita por nombre") {
            repoFigurita.create(figuritaMessi)
            repoJugador.create(delanteroCampeonDelMundoCami10)
            repoFigurita.search("LIOnel") shouldBe mutableListOf<Figurita>(figuritaMessi)
        }

        it("busqueda de figurita por apellido") {
            repoFigurita.search("mEsSI") shouldBe mutableListOf<Figurita>(figuritaMessi)

        }
        it("busqueda de figurita que coincida exactamente con pais de la seleccion") {
            repoFigurita.search("Argentina") shouldBe mutableListOf<Figurita>(figuritaMessi)

        }
        it("busqueda de figurita por numero") {
            repoFigurita.search("10") shouldBe mutableListOf<Figurita>(figuritaMessi)

        }

        it("busqueda de un jugador por nombre") {
            repoJugador.search("Lionel") shouldBe mutableListOf<Jugador>(delanteroCampeonDelMundoCami10)

        }
        it("busqueda de un jugador por apellido") {
            repoJugador.search("mESSI") shouldBe mutableListOf<Jugador>(delanteroCampeonDelMundoCami10)

        }
        it("busqueda de una seleccion que coincida exactamente con el pais") {
            repoSeleccion.search("Argentina") shouldBe mutableListOf<Seleccion>(argentina)

        }
        it("busqueda de una seleccion que no coincida exactamente con el pais") {
            repoSeleccion.search("argentina") shouldNotBe mutableListOf<Seleccion>(argentina)

        }
        it("busqueda de un usuario que coincida parcialmente con el nombre") {
            repoUsuario.create(usuarioQueCumple23ElProximoMes)
            repoUsuario.search("JUAN") shouldBe mutableListOf<Usuario>(usuarioQueCumple23ElProximoMes)

        }
        it("busqueda de un usuario que coincida parcialmente con el apellido") {
            repoUsuario.search("lopez") shouldBe mutableListOf<Usuario>(usuarioQueCumple23ElProximoMes)

        }
        it("busqueda de un usuario que coincida exactamente con el username") {
            repoUsuario.search("jl213") shouldBe mutableListOf<Usuario>(usuarioQueCumple23ElProximoMes)

        }
        it("busqueda de un usuario que no coincida exactamente con el username") {
            repoUsuario.search("JL213") shouldNotBe mutableListOf<Usuario>(usuarioQueCumple23ElProximoMes)

        }
        it("busqueda de un punto de venta que no coincida exactamente con el nombre comercial") {
            repoPuntoDeVenta.create(puntoDeVenta)
            repoPuntoDeVenta.search("ramdon") shouldNotBe mutableListOf<PuntoDeVenta>(puntoDeVenta)


        }
    }
})




