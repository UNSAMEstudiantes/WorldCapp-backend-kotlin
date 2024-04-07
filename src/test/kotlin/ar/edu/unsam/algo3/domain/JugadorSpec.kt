package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.domain.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class PosicionSpec : DescribeSpec({
    describe("test de valor de jugadores") {
        val argentina = Seleccion("Argentina", Confederacion.CONMEBOL, 3, 15)
        val mexico = Seleccion("México", Confederacion.CONCACAF, 0, 0)
        val brasil = Seleccion("Brasil", Confederacion.CONMEBOL, 5, 9)
        val javierHernandez = Jugador(
            "Javier",
            "Hernandez",
            LocalDate.of(1988, 6, 1),
            14,
            mexico,
            LocalDate.of(2006, 9, 9),
            1.75,
            70.0,
            Delantero,
            false,
            20
        )
        val messi = Jugador(
            "Lionel",
            "Messi",
            LocalDate.of(1987, 6, 24),
            10,
            argentina,
            LocalDate.of(2005, 8, 17),
            1.70,
            72.5,
            Delantero,
            true,
            100000000
        )
        val dibu = Jugador(
            "Emiliano",
            "Martinez",
            LocalDate.of(1992, 9, 2),
            23,
            argentina,
            LocalDate.of(2011, 4, 27),
            1.94,
            84.0,
            Arquero,
            false,
            10000000
        )
        val ochoa = Jugador(
            "Guillermo",
            "Ochoa",
            LocalDate.of(1985, 7, 13),
            1,
            mexico,
            LocalDate.of(2005, 2, 16),
            1.70,
            78.0,
            Arquero,
            true,
            30
        )
        val enzoFernandez = Jugador(
            "Enzo",
            "Fernandez",
            LocalDate.of(1996, 2, 14),
            24,
            argentina,
            LocalDate.of(2016, 3, 24),
            1.78,
            70.0,
            MedioCampista,
            true,
            150000000
        )
        val enriqueGonzalez = Jugador(
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
        val otamendi = Jugador(
            "Nicolás",
            "Otamendi",
            LocalDate.of(1988, 2, 12),
            19,
            argentina,
            LocalDate.of(2008, 11, 19),
            1.83,
            81.0,
            Defensor,
            true,
            5000000
        )
        val hectorMoreno = Jugador(
            "Héctor",
            "Moreno",
            LocalDate.of(1988, 1, 17),
            15,
            mexico,
            LocalDate.of(2007, 9, 9),
            1.84,
            78.0,
            Defensor,
            false,
            80
        )
        val lozano = Jugador(
            "",
            "Lozano",
            LocalDate.of(1995, 7, 30),
            150,
            mexico,
            LocalDate.of(2013, 9, 11),
            1.75,
            -70.0,
            Delantero,
            false,
            40
        )

        val PolivalenteMedioDelantero = Polivalente()
        PolivalenteMedioDelantero.posiciones.addAll(listOf(Delantero, MedioCampista))

        val alexisMacAllister = Jugador(
            "Alexis",
            "Mac Allister",
            LocalDate.of(1998, 12, 24),
            14,
            argentina,
            LocalDate.of(2018, 1, 29),
            1.76,
            70.0,
            PolivalenteMedioDelantero,
            false,
            20000000,
        )

        val neymar = Jugador(
            "neymar",
            "jr",
            LocalDate.of(1993, 1, 1),
            10,
            brasil,
            LocalDate.of(2006, 1, 1),
            1.75,
            70.0,
            PolivalenteMedioDelantero,
            true,
            10,
        )



        it("emiliano martinez es un arquero mayor a 1.80 por lo que vale 194") {
            dibu.valoracion() shouldBe 194
        }

        it("Guillermo Ochoa es un arquero menor a 1.80 por lo que vale 100") {
            ochoa.valoracion() shouldBe 100
        }

        it("Otamendi es un defensor argentino, es lider , debuto en 2008 y tiene 14 años en la seleccion por lo tanto vale 190") {
            otamendi.valoracion() shouldBe 190
        }

        it("Hector moreno es un defensor mexicano que no es lider, por lo que vale 50") {
            hectorMoreno.valoracion() shouldBe 50
        }

        it("enzo fernandez es un mediocampista ligero que pesa 70 kg por lo que vale 220") {
            enzoFernandez.valoracion() shouldBe 220
        }

        it("enrique gonzales es un mediocampista pesado que pesa 78 kg por lo que vale 150") {
            enriqueGonzalez.valoracion() shouldBe 150
        }

        it("messi es un delantero campeon del mundo y tiene la 10 por lo tanto vale 300") {
            messi.valoracion() shouldBe 300
        }

        it("Javier hernandez es un delantero mexicano que no tiene copas del mundo por lo que vale 200") {
            javierHernandez.valoracion() shouldBe 200
        }

        it("messi es un jugador valido") {
            messi.esValido() shouldBe true
        }

        it("lozano es un jugador invalido porque tiene numero de camiseta 150 , peso negativo y no tiene nombre") {
            lozano.esValido() shouldBe false
            lozano.validarCamiseta() shouldBe false
        }

        it("el pais de messi es ARG") {
            messi.deducirPais(argentina)
            messi.pais shouldBe "ARG"
        }
        it("Alexis Mac Allister es un jugador polivalente argentino en las posiciones de mediocampista y delantero que no es leyenda ni promesa") {
            alexisMacAllister.valoracion() shouldBe 175
        }

        it("Neymar es un jugador polivalente brasileño en las posiciones de mediocampista y delantero que es leyenda , pesa 70kg , 30 años de edad y tiene la 10"){

            neymar.valoracion() shouldBe 405
        }

    }

})