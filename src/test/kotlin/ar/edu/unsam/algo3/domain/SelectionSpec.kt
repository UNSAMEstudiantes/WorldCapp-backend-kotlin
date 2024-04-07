package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.domain.Confederacion
import ar.edu.unsam.algo3.domain.Seleccion
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class SelectionSpec : DescribeSpec({
    describe("Test de Seleccion") {

        val argentina = Seleccion("Argentina", Confederacion.CONMEBOL, 3, 10)

        it("seleccion argentina pertenece a CONMEBOL ") {
            //assert
            argentina.confederacion shouldBe Confederacion.CONMEBOL

        }
        it("seleccion argentina No pertenece a otra confederacion ") {

            //assert
            argentina.confederacion shouldNotBe Confederacion.UEFA

        }
        it("La copa es par ") {

            argentina.copasPar() shouldBe false
        }
        it("La copa es impar ") {

            argentina.copasPar() shouldNotBe true

        }
    }
})