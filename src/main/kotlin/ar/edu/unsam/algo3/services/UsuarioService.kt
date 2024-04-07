package ar.edu.unsam.algo3.services

import ar.edu.unsam.algo3.DTO.*
import ar.edu.unsam.algo3.domain.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UsuarioService(
    @Autowired val repoUsuarios: RepositorioUsuarios,
    @Autowired val repoFiguritas: RepositorioFiguritas
) {


    @Transactional
    fun actualizarUsuario(id: Int, usuarioActualizado: UsuarioDTO): Usuario {

        val usuarioExistente = repoUsuarios.getById(id)
        val tipoDeUsuarioNuevo = obtenerTipoDeUsuarioPorNombre(usuarioActualizado.tipo)
        usuarioExistente.apply {
            nombre = usuarioActualizado.nombre
            apellido = usuarioActualizado.apellido
            fechaDeNacimiento = usuarioActualizado.fechaDeNacimiento
            direccion = usuarioActualizado.direccion
            mail = usuarioActualizado.mail
            tipoDeUsuario = tipoDeUsuarioNuevo
            distanciaMaxima = usuarioActualizado.distanciaMaxima
        }

        repoUsuarios.update(usuarioExistente)

        return usuarioExistente
    }

    fun obtenerTipoDeUsuarioPorNombre(nombre: String): TipoDeUsuario {
        val tipoDeUsuarioMap: Map<String, TipoDeUsuario> = mapOf(
            "Par" to UsuarioPar(),
            "Nacionalista" to UsuarioNacionalista(),
            "Desprendido" to UsuarioDesprendido(),
            "Apostador" to UsuarioApostador(),
            "Interesado" to UsuarioInteresado(),
            "Cambiante" to UsuarioCambiante(),
            "Conservador" to UsuarioConservador(),
            "Fanatico" to UsuarioCambiante(),
        )
        return tipoDeUsuarioMap[nombre] ?: throw IllegalArgumentException("Tipo de usuario no válido: $nombre")
    }

    fun getFiguritasFaltantes(id: Int): List<FiguritaDTO> =
        convertirAFiguritasDTO(repoUsuarios.getById(id).figuritasFaltantes)

    fun getFiguritasRepetidas(id: Int): List<FiguritaDTO> =
        convertirAFiguritasDTO(repoUsuarios.getById(id).figuritasRepetidas)

    fun agregarFiguritaFaltante(idUsuario: Int, idFigurita: Int) =
        repoUsuarios.getById(idUsuario).registrarFiguritaFaltante(repoFiguritas.getById(idFigurita))

    fun agregarFiguritaRepetida(idUsuario: Int, idFigurita: Int) =
        repoUsuarios.getById(idUsuario).registrarFiguritaRepetida(repoFiguritas.getById(idFigurita))

    fun validarUsername(username: String) =
        repoUsuarios.buscarIdPorUsername(username)

    fun convertirAFiguritasDTO(figuritas: MutableList<Figurita>): List<FiguritaDTO> =
        figuritas.map { figurita: Figurita -> figurita.toDTO() }

    fun removerFiguritaRepetida(idUsuario: Int, idFigurita: Int) {
        repoUsuarios.getById(idUsuario).removerFiguritaRepetida(repoFiguritas.getById(idFigurita))
    }

    fun removerFiguritaFaltante(idUsuario: Int, idFigurita: Int) {
        repoUsuarios.getById(idUsuario).removerFiguritaFaltante(repoFiguritas.getById(idFigurita))
    }

    fun solicitarFigurita(idUsuarioSolicitante: Int, idFigurita: Int, idUsuarioCedente: Int) {
        val usuarioSolicitante = repoUsuarios.getById(idUsuarioSolicitante)
        val figurita = repoFiguritas.getById(idFigurita)
        val usuarioCedente = repoUsuarios.getById(idUsuarioCedente)
        usuarioSolicitante.solicitarFigurita(figurita, usuarioCedente)

    }

    fun cantidadUsuariosActivos(): Int =
        repoUsuarios.getCantidad()

    fun figuritasOfrecidas(): Int =
        repoUsuarios.cantidadFiguritasOfrecidas()

    fun figuritasRepetidas(): Int =
        repoUsuarios.cantidadoFiguritasRepetidas()

}


