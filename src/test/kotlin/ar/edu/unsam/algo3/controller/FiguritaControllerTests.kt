package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.*
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
@DisplayName("Dado un controller de figuritas")
class FiguritaControllerTests (@Autowired val mockMvc: MockMvc) {

    @Autowired
    val repoUsuarios: RepositorioUsuarios = RepositorioUsuarios()

    @Autowired
    val repoFiguritas: RepositorioFiguritas = RepositorioFiguritas()

    @Test
    fun `Si no se reciben filtros, se obtendran todas las figuritas disponibles`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/busqueda-figuritas"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(14))
    }

    @Test
    fun `Pueden filtrarse por nombre del jugador`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/busqueda-figuritas?nombre=Lionel"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].nombre").value("Lionel Messi"))
    }

    @Test
    fun `Pueden filtrarse por apellido del jugador`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/busqueda-figuritas?nombre=Messi"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].nombre").value("Lionel Messi"))
    }

    @Test
    fun `Pueden filtrarse por la seleccion a la cual pertenecen`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/busqueda-figuritas?nombre=Argentina"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(4))
    }

    @Test
    fun `Pueden filtrarse por el numero de la figurita`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/busqueda-figuritas?nombre=21"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].nombre").value("Emiliano Martinez"))
    }

    @Test
    fun `Pueden filtrarse por valoracion`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/busqueda-figuritas?desdeValor=186&hastaValor=435"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(12))
    }

    @Test
    fun `Pueden filtrarse dependiendo de si la figurita es onFire`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/busqueda-figuritas?onFire=true"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(5))
    }

    @Test
    fun `Pueden filtrarse dependiendo de si el jugador es promesa`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/busqueda-figuritas?esPromesa=true"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
    }

    @Test
    fun `Pueden aplicarse varios filtros a la vez`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/busqueda-figuritas?desdeValor=350&hastaValor=450&onFire=true"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].nombre").value("Lionel Messi"))
    }

    @Test
    fun `Puede obtenerse una figurita a traves del ID`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/busqueda-figuritas/1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Lionel Messi"))
    }

    @Test
    fun `En caso de que no existe una figurita con ese ID, tira error`() {
        val errorMessage = mockMvc.perform(MockMvcRequestBuilders.get("/busqueda-figuritas/28"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andReturn().resolvedException?.message

        assertEquals(errorMessage, "No se encontró el elemento con ID 28")
    }

    @Test
    fun `Si una figurita faltante de un usuario, esta dentro de las cedidas por los usuarios, se muestra`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/main-busqueda-figuritas/1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
    }

    @Test
    fun `Si una figurita faltante de un usuario, no esta dentro de las cedidas por los usuarios, no se muestra`() {
        val usuario = repoUsuarios.getById(1)
        val figuritaNoCedida = repoFiguritas.getById(5)
        usuario.registrarFiguritaFaltante(figuritaNoCedida)
        val figuritaFaltanteNoCedida = usuario.figuritasFaltantes[2]
        mockMvc.perform(MockMvcRequestBuilders.get("/main-busqueda-figuritas/1?nombre=${figuritaFaltanteNoCedida.jugador.nombre}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0))
    }

    @Test
    fun `Al actualizarse las lista de figuritas repetidas de algun usuario, en caso de cumplir con el criterio para ceder, se actualiza la lista de cedidas`() {
        val usuario = repoUsuarios.getById(1)
        val usuarioQueCede = repoUsuarios.getById(2)
        mockMvc.perform(MockMvcRequestBuilders.get("/main-busqueda-figuritas/1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))

        val nuevaFiguritaCedida = repoFiguritas.getById(5)
        usuario.registrarFiguritaFaltante(nuevaFiguritaCedida)
        usuarioQueCede.registrarFiguritaRepetida(repoFiguritas.getById(5))
        val cantidadDeFiguritasCedidasDisponibles = usuario.figuritasFaltantes.size


        mockMvc.perform(MockMvcRequestBuilders.get("/main-busqueda-figuritas/1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(cantidadDeFiguritasCedidasDisponibles))
    }

    @Test
    fun `no se puede eliminar a messi porque perternece a una lista repetida o faltante`() {

        val errorMessage = mockMvc.perform(MockMvcRequestBuilders.delete("/busqueda-figuritas/borrar/1"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn().resolvedException?.message

        assertEquals(errorMessage, "La figurita esta como faltante o repetida para algun usuario")
    }
}
//    @Test
//    fun `se puede eliminar a otamendi del la lista porque no pertenece a ninguna lista repetida o faltante de un usuario`(){
//            mockMvc.perform(MockMvcRequestBuilders.delete("/busqueda-figuritas/borrar/14"))
//            mockMvc.perform(MockMvcRequestBuilders.get("/busqueda-figuritas"))
//                .andExpect(MockMvcResultMatchers.status().isOk)
//                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(13))
//        }
//    }
//
//
//
