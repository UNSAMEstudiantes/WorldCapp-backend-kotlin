package ar.edu.unsam.algo3.domain

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk



class ActualizadorSpec : DescribeSpec({
    describe("test Actualizador") {

        val json = """
[
    {
        "id": 1,
        "pais": "Argentina",
        "confederacion": "CONMEBOL",
        "copasDelMundo": 3,
        "copasConfederacion": 15
    },
    {
        "id": 2,
        "pais": "Brasil",
        "confederacion": "CONMEBOL",
        "copasDelMundo": 5,
        "copasConfederacion": 9
    },
      {
        "pais": "Alemania",
        "confederacion": "UEFA",
        "copasDelMundo": 4,
        "copasConfederacion": 3
    },
  
    {
        "id": 3,
        "pais": "Mexico",
        "confederacion": "CONCACAF",
        "copasDelMundo": 0,
        "copasConfederacion": 1
    }
]
"""
        val repo = Repositorio<Seleccion>()
        val servicio = mockk<ServiceSelecciones>()

        val mockActu = Actualizador(servicio, repo)
        every { servicio.getSelecciones() } answers { json }


        val brasil = Seleccion("Brasil", Confederacion.CONMEBOL, 5, 9)
        val argentina = Seleccion("Argentina", Confederacion.CONMEBOL, 2, 10)
        val mexico = Seleccion("Mexico", Confederacion.CONCACAF, 0, 1)

        repo.create(argentina)
        repo.create(brasil)
        repo.create(mexico)
        it("update de la seleccion argentina de 2 copas del mundo a 3") {
            mockActu.actualizar()

            repo.search("Argentina")[0].copasDelMundo shouldBe 3
         }
        it("alemania al no tener ID ya que no estaba inicialmente en el repo se agrega al mismo con una id") {
            mockActu.actualizar()
            repo.search("Alemania").isNotEmpty()
        }
        it("alemania se le asigna el siguiente ID disponible en este caso el 4") {
            mockActu.actualizar()
            repo.search("Alemania")[0].id shouldBe 4
        }
    }

})

