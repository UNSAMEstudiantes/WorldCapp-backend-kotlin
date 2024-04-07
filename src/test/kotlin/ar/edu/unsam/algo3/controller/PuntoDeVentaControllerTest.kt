package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.*
import com.jayway.jsonpath.JsonPath
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Dado un controller de puntos de venta")

class PuntoDeVentaControllerTest(@Autowired val mockMvc: MockMvc) {

    @Test
    fun `Se pueden recibir todos los puntos de venta de un usuario`() {
        val expectedCantidadPuntosVenta = 6
        val userId = 1

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/puntosdeventa/${userId}/all"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val jsonResponse = result.response.contentAsString
        val actualCantidadPuntosVenta = JsonPath.read<Int>(jsonResponse, "$.length()")

        assertEquals(expectedCantidadPuntosVenta, actualCantidadPuntosVenta)
    }

    @Test
    fun `Se pueden recibir un punto de venta por su id`() {
        val expectedNombre = "TurboToy"
        val puntoVentaId = 4

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/puntosdeventa/1/${puntoVentaId}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val jsonResponse = result.response.contentAsString
        val actualNombre = JsonPath.read<String>(jsonResponse, "$.nombre")

        assertEquals(expectedNombre, actualNombre)
    }

    @Test
    fun `Se puede escribir un nombre de un punto de venta`() {
        val expectedNombre = "Xeneize"
        val nombreABuscar = "xen"

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/puntosdeventa/1/Ordenado?tipoOrden=&nombreABuscar=${nombreABuscar}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val jsonResponse = result.response.contentAsString
        val actualNombre = JsonPath.read<String>(jsonResponse, "$.[0].nombre")

        assertEquals(expectedNombre, actualNombre)
    }

    @Test
    fun `Se pueden ordenar por cantidad de sobres`() {
        val expectedSobresStock = listOf(400, 200, 150, 100, 77, 50)

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/puntosdeventa/1/Ordenado?tipoOrden=massobres&nombreABuscar="))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val jsonResponse = result.response.contentAsString
        val actualSobresStock = JsonPath.read<List<Int>>(jsonResponse, "$[*].stockSobres")

        assertEquals(expectedSobresStock, actualSobresStock)
    }

    @Test
    fun `Se pueden ordenar por baratos`() {
        val expectedNombres = listOf("Dia", "Xeneize", "TurboToy", "Bingo", "Balduzzi", "Pepe")

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/puntosdeventa/1/Ordenado?tipoOrden=masbaratos&nombreABuscar="))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val jsonResponse = result.response.contentAsString
        val actualNombres = JsonPath.read<List<String>>(jsonResponse, "$[*].nombre")

        assertEquals(expectedNombres, actualNombres)
    }

    @Test
    fun `Se pueden ordenar por menor distancia`(){
        val expectedNombres = listOf("TurboToy", "Bingo", "Xeneize", "Dia", "Balduzzi", "Pepe")

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/puntosdeventa/1/Ordenado?tipoOrden=menordistancia&nombreABuscar="))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val jsonResponse = result.response.contentAsString
        val actualNombres = JsonPath.read<List<String>>(jsonResponse, "$[*].nombre")

        assertEquals(expectedNombres, actualNombres)
    }

    @Test
    fun `Se muestran solo los mas cercanos`() {
        val expectedNombres = listOf("Xeneize", "TurboToy", "Bingo")
        val expectedLength = 3

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/puntosdeventa/1/Ordenado?tipoOrden=mascercanos&nombreABuscar="))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val jsonResponse = result.response.contentAsString
        val actualNombres = JsonPath.read<List<String>>(jsonResponse, "$[*].nombre")
        val actualLength = JsonPath.read<Int>(jsonResponse, "$.length()")

        assertEquals(expectedNombres, actualNombres)
        assertEquals(expectedLength, actualLength)
    }

    @Test
    fun `Se ordenar y buscar a la vez`() {
        val expectedNombres = listOf("TurboToy", "Bingo", "Balduzzi")
        val expectedLength = 3

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/puntosdeventa/1/Ordenado?tipoOrden=menordistancia&nombreABuscar=b"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val jsonResponse = result.response.contentAsString
        val actualNombres = JsonPath.read<List<String>>(jsonResponse, "$[*].nombre")
        val actualLength = JsonPath.read<Int>(jsonResponse, "$.length()")

        assertEquals(expectedNombres, actualNombres)
        assertEquals(expectedLength, actualLength)
    }

}


