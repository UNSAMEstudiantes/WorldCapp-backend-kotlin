package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.domain.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class FiguritaSpec : DescribeSpec({
    describe("Test de Figurita") {

        val seleccionArgentina = Seleccion("Argentina", Confederacion.OFC, 3, 3)
        val fechaNacimiento = LocalDate.now().minusYears(23)
        val jugador = Jugador(
            "Lionel" ,
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
        describe("tests de valor base de figuritas") {
            it("valor base una figurita on fire par y con nivel de impresion alto ") {
                //arrange

                val figuritaOnfireParNivelAlto = Figurita(12, Impresion.ALTA, true, jugador)

                //assert
                figuritaOnfireParNivelAlto.valorBase() shouldBe 112.20

            }

            it("valor base de una figurita que no esta en on fire y con nivel de impresion baja") {
                val figuritaNoOnFireImparNivelBajo = Figurita(13, Impresion.BAJA, false, jugador)

                figuritaNoOnFireImparNivelBajo.valorBase() shouldBe 100.0
            }


            it("valor base una figurita on fire, par y con nivel de impresion medio ") {
                //arrange

                val figuritaOnfireParNivelMedio = Figurita(12, Impresion.MEDIA, true, jugador)

                //assert
                figuritaOnfireParNivelMedio.valorBase() shouldBe 112.2

            }

            it("Figurita impar, está OnFire Nivel de Impresión Alta") {
                //arrange

                val figuritaOnfireImparNivelAlto = Figurita(7, Impresion.ALTA, true, jugador)

                //assert
                figuritaOnfireImparNivelAlto.valorBase() shouldBe 102

            }
            it("valor base de una figurita par que no esta on fire nivel bajo") {
                val figuritaNoOnFireParNivelBajo = Figurita(8, Impresion.BAJA, false, jugador)

                figuritaNoOnFireParNivelBajo.valorBase() shouldBe 100.0
            }
            it("valor base de una figurita par que no esta on fire nivel alto") {
                val figuritaNoOnFireParNivelAlto = Figurita(8, Impresion.ALTA, false, jugador)

                figuritaNoOnFireParNivelAlto.valorBase() shouldBe 85.0
            }
            it("valor base de una figurita par que no esta on fire nivel medio") {
                val figuritaNoOnFireParNivelMedio = Figurita(8, Impresion.MEDIA, false, jugador)

                figuritaNoOnFireParNivelMedio.valorBase() shouldBe 85.0
            }
        }

        describe("tests de valor final de las figuritas") {
            val argentina = Seleccion("Argentina", Confederacion.CONMEBOL, 3, 15)
            val mexico = Seleccion("México", Confederacion.CONCACAF, 0, 0)
            val delanteroCampeonDelMundoCami10 = Jugador(
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

            it("un delantero campeon del mundo y tiene la 10 por lo tanto vale 300 mas el valor base de una figurita on fire par y con nivel de impresion bajo asi que su valoracion deberia ser 432.0") {
                figuritaMessi.valoracion() shouldBe 432.0
            }

            it("un mediocampista pesado por lo que vale 150 ademas de ser una figurita impar, de nivel alto y no estar on fire por eso la valovarion de su figurita debe ser ") {
                figuritaEnrrique.valoracion() shouldBe 235.0
            }
        }

        describe("validacion de figuritas "){
            it("figurita con numero negativo es invalida"){
             val figuritaConNumeroNegativo= Figurita(-1,Impresion.ALTA,true,jugador)
                figuritaConNumeroNegativo.esValida() shouldBe false
            }
            it("figurita con numero positivo es invalida y sus campos llenos es valida"){
                val figuritaConNumeroPosiivo= Figurita(1,Impresion.ALTA,true,jugador)
                figuritaConNumeroPosiivo.esValida() shouldBe true

            }
        }
    }
})