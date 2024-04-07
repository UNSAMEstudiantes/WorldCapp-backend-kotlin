package ar.edu.unsam.algo3.domain

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.uqbar.geodds.Point
import java.time.DayOfWeek
import java.time.LocalDate

class PuntosDeVentaSpec : DescribeSpec({
    afterSpec {
        unmockkStatic(LocalDate::class)
    }
    isolationMode = IsolationMode.InstancePerTest
    describe("Tests de PuntosDeVenta") {
        val ubiGeografica = Point(-34.574306, -58.610908)
        val direccion1 = Direccion("Buenos Aires", "San Martin", "calle", 2345, ubiGeografica)
        val puntoDeVenta = Kiosko(
            nombre = "random",
            direccion = direccion1,
            stockSobres = 50,
            tieneEmpleados = false
        )
        val pedido = Pedido(cantidadSobres = 10, fechaEntrega = LocalDate.of(2023, 5, 2), false)
        val cantidadSobres = 3
        val ubicacionCercana = Point(-35.574306, -58.610908)
        val direccionCercana = Direccion("Buenos Aires", "San Martin", "calle", 2345, ubiGeografica)
        describe("Tests de atributos importantes de PuntosDeVenta") {

            it("El PuntoDeVenta tiene una lista de pedidos pendientes, a la cual podemos agregarle pedidos") {
                puntoDeVenta.agregarPedido(pedido)
                puntoDeVenta.pedidosPendientes.size shouldBe 1
            }

            describe("Tests de disponibilidad de sobres") {
                it("Si hay sobres, habra disponibilidad") {
                    puntoDeVenta.disponibilidad() shouldBe true
                }

                it("Si no hay sobres, no habra disponibilidad") {
                    puntoDeVenta.stockSobres = 0
                    puntoDeVenta.disponibilidad() shouldBe false
                }
            }
        }

        describe("Tests del importeACobrar") {
            describe("Tests del coste minimo") {
                it("Parte del costo minimo, es un valor fijo que depende de la cantidad de sobres") {
                    puntoDeVenta.costoSobres(10) shouldBe 1700.00
                }

                it("Si la distancia del envio supera no supera los 10km, se adicionan $1000") {
                    val ubicacionCercana = Point(-35.574306, -58.610908)
                    val direccionCercana = Direccion("Buenos Aires", "San Martin", "calle", 2345, ubiGeografica)
                    puntoDeVenta.costoDistancia(direccionCercana) shouldBe 1000.00
                }

                it("Si la distancia del envio supera los 10km, se adicionan 1000 + 100 por km de exceso") {
                    val ubicacionA12km = Point(-34.56376644765669, -58.47114814178834)
                    val direccionA12km = Direccion("Buenos Aires", "San Martin", "calle", 2345, ubicacionA12km)

                    puntoDeVenta.costoDistancia(direccionA12km) shouldBe 1200.00
                }
            }

            describe("Tests del multiplicador a aplicar dependiendo del PuntoDeVenta") {
                describe("Tests de Kiosko") {
                    val kioskoConEmpleados = Kiosko(
                        nombre = "random",
                        direccion = direccion1,
                        stockSobres = 50,
                        tieneEmpleados = true
                    )

                    val kioskoAtendidoPorPropietario = puntoDeVenta

                    val kioskoVacio = Kiosko(
                        nombre = "random",
                        direccion = direccion1,
                        stockSobres = 50,
                        tieneEmpleados = false
                    )


                    it("Si el kiosko es atendido por el dueño, se le suma al pedido un 10% del total") {
                        kioskoAtendidoPorPropietario.multiplicadorEspecial(cantidadSobres) shouldBe 1.10
                    }

                    it("Si el kiosko tiene empleados, se le suma al pedido un 25% del total") {
                        kioskoConEmpleados.multiplicadorEspecial(cantidadSobres) shouldBe 1.25
                    }

                    it("Si el kiosko no es atendido por el dueño y no tiene empleados, no se le suma nada") {
                        kioskoVacio.multiplicadorEspecial(cantidadSobres) shouldBe 1.1
                    }
                }

                describe("Tests de Libreria") {
                    val libreria = Libreria(
                        nombre = "random",
                        direccion = direccion1,
                        stockSobres = 50,
                    )
                    val pedidoDentroDeLos10Dias =
                        Pedido(cantidadSobres = 10, fechaEntrega = LocalDate.now().plusDays(1), false)
                    val pedidoFueraDeLos10Dias = Pedido(cantidadSobres = 10, fechaEntrega = LocalDate.of(3023, 8, 30), false)


                    it("Si existe al menos un pedido a fabrica que ingresa dentro de los 10 dias, se suma un 5% del total ") {
                        libreria.agregarPedido(pedidoDentroDeLos10Dias)
                        libreria.diferenciaDeDias(pedidoDentroDeLos10Dias.fechaEntrega) shouldBe 1
                        libreria.multiplicadorEspecial(cantidadSobres) shouldBe 1.05
                    }

                    it("Si no existe un pedido a fabrica que ingrese dentro de los 10 dias, se suma un 10% del total") {
                        libreria.agregarPedido(pedidoFueraDeLos10Dias)
                        libreria.multiplicadorEspecial(cantidadSobres) shouldBe 1.10
                    }
                }
                describe("Test de Supermercado") {
                    mockkStatic(LocalDate::class)

                    it("supermercadoSinDescuentos no aplica ningun descuento") {
                        val supermercadoSinDescuentos = Supermercado(
                            nombre = "random",
                            direccion = direccion1,
                            stockSobres = 50,
                            tipoDescuento = SinDescuento
                        )
                        supermercadoSinDescuentos.multiplicadorEspecial(1) shouldBe 1.0

                    }
                    describe("Test de descuento por primeros dias del mes") {
                        val supermercadoConDescuentoPorDia = Supermercado(
                            nombre = "random",
                            direccion = direccion1,
                            stockSobres = 50,
                            tipoDescuento=DescuentoPrimerosDias
                        )

                        it("descuento por dia el dia 20 no hace ningun descuento") {

                            stubLocalDateNroDiaDelMes(20)
                            supermercadoConDescuentoPorDia.multiplicadorEspecial(1) shouldBe 1.0
                        }
                        it("descuento por dia el dia 5 hace 5% de descuento") {

                            stubLocalDateNroDiaDelMes(5)
                            supermercadoConDescuentoPorDia.multiplicadorEspecial(1) shouldBe 0.95

                        }
                        it("descuento por dia el dia 10 hace 5% de descuento") {
                            stubLocalDateNroDiaDelMes(5)
                            supermercadoConDescuentoPorDia.multiplicadorEspecial(1) shouldBe 0.95

                        }
                    }
                    describe("Test de descuento por dia jueves") {
                        val supermercadoConDescuentoPorJueves = Supermercado(
                            nombre = "random",
                            direccion = direccion1,
                            stockSobres = 50,
                            tipoDescuento= DescuentoJueves
                        )


                        it("si es jueves se aplica descuento") {

                            stubLocalDateDiaDeLaSemana(DayOfWeek.THURSDAY)
                            supermercadoConDescuentoPorJueves.multiplicadorEspecial(1) shouldBe 0.9
                        }
                        it("si no es jueves no se aplica descuento") {
                            stubLocalDateDiaDeLaSemana(DayOfWeek.SUNDAY)
                            supermercadoConDescuentoPorJueves.multiplicadorEspecial(1) shouldBe 1.0
                        }
                    }
                    describe("Test de descuentos combinados") {



                        val todosLosDescuentos = DescuentoCombinado(mutableListOf(
                            DescuentoPrimerosDias,
                            DescuentoJueves,
                            DescuentoCompraGrande
                        ))
                        val supermercadoConDescuentoCombinado = Supermercado(
                            nombre = "random",
                            direccion = direccion1,
                            stockSobres = 50,
                            tipoDescuento=todosLosDescuentos
                        )
                        stubLocalDateNroDiaDelMes(5)
                        stubLocalDateDiaDeLaSemana(DayOfWeek.THURSDAY)
                        it("tiene todos los descuentos = 55% pero no puede terner mas de 50%") {
                            supermercadoConDescuentoCombinado.multiplicadorEspecial(210) shouldBe 0.05
                        }

                        it("tiene los descuentos de primeros dias y del jueves = 15%") {
                            supermercadoConDescuentoCombinado.multiplicadorEspecial(1) shouldBe 0.85
                        }

                        it("alguien compra 10 sobres en un supermercado con descuentos combinados un jueves y dia 2") {
                            supermercadoConDescuentoCombinado.importeACobrar(5, direccionCercana) shouldBe 1572.5
                        }
                    }
                }

            }

        }
    }
})

fun stubLocalDateNroDiaDelMes(nroDia: Int) {
    every { LocalDate.now().dayOfMonth } returns nroDia
}

fun stubLocalDateDiaDeLaSemana(dia: DayOfWeek) {
    every { LocalDate.now().dayOfWeek } returns dia
}

