package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.DTO.FiguritaDTO
import ar.edu.unsam.algo3.DTO.PuntoDeVentaDTO
import ar.edu.unsam.algo3.DTO.toDTO
import ar.edu.unsam.algo3.DTO.toDTODetalles
import org.springframework.stereotype.Component
import kotlin.reflect.typeOf

@Component
class Repositorio<T : Entidad> {
    val elementos: MutableList<T> = mutableListOf<T>()
    var nextId = 1
    lateinit var figuritasFiltradas : List<FiguritaDTO>

    fun create(elemento: T)  {
        validarElementoNuevo(elemento)
        asignarId(elemento)
        elementos.add(elemento)
      }

    private fun validarElementoNuevo(elemento: T) {
            if (!elemento.esNueva()) {
                throw ElementoYaExisteException("No se puede crear el elemento con ID ${elemento.id}" +
                        ". Este ID ya existe en el repositorio")
        }
    }
    private fun asignarId(elemento: T) {
        elemento.id = nextId++
    }
    fun getCantidad():Int{
        return elementos.size
    }

    fun delete(elemento: T) {
        val elementoExistente = getById(elemento.id)
            elementos.remove(elementoExistente)
     }

    fun update(elemento: T) {
        val elementoViejo = getById(elemento.id)
        reemplazarElemento(elementoViejo, elemento)
    }

    private fun reemplazarElemento(elementoViejo: T, nuevo: T) {
        val index = elementos.indexOf(elementoViejo)
        if (index != -1) {
            elementos[index] = nuevo
        }
    }
    fun getById(id: Int): T {
        return elementos.find { elemento -> elemento.id == id }
                ?: throw ElementoInexistenteException("No se encontró el elemento con ID $id")
    }

    fun search(value: String): List<T> {
        return elementos.filter { it.condicionDeBusqueda(value) }
     }

    fun searchFigurita(value: String) : List<FiguritaDTO>{
        return figuritasFiltradas.filter { it.condicionDeBusqueda(value)}
    }

    fun filtrarFiguritasValor(desdeValor: Int, hastaValor: Int){
        figuritasFiltradas = figuritasFiltradas.filter { figurita -> figurita.valoReal.toInt() in desdeValor..hastaValor }
    }

    fun filtrarFiguritasOnFire(){
        figuritasFiltradas = figuritasFiltradas.filter { figurita -> figurita.onFire }
    }

    fun filtrarFiguritasPromesas(){
        figuritasFiltradas = figuritasFiltradas.filter { figurita -> figurita.esPromesa }
    }

    fun filtrarFiguritas(nombre: String?, desdeValor: Int?, hastaValor: Int?, onFire: String?, esPromesa: String?) : List<FiguritaDTO>{
        establecerFiguritas()

        if(nombre != null){
            figuritasFiltradas = searchFigurita(nombre)
        }
        if(desdeValor != null && hastaValor != null){
            filtrarFiguritasValor(desdeValor, hastaValor)
        }
        if(onFire.toBoolean()) {
            filtrarFiguritasOnFire()
        }
        if(esPromesa.toBoolean()) {
            filtrarFiguritasPromesas()
        }

        val tempFiguritasFiltradas = figuritasFiltradas
        actualizarFiguritas()

        return tempFiguritasFiltradas
    }

    fun establecerFiguritas(){
        if(!this::figuritasFiltradas.isInitialized){
            actualizarFiguritas()
        }
        if(!sonFiguritasCedidas()){
            actualizarFiguritas()
        }
    }

    fun sonFiguritasCedidas() : Boolean = this.figuritasFiltradas.get(0).cedidaPor != null

    fun actualizarFiguritas(){
        val figuritas = elementos as List<Figurita>
        if(figuritas.get(0) is Figurita){
            figuritasFiltradas = figuritas.map{figurita -> figurita.toDTO()}
        }
    }


}
@Component
class RepositorioUsuarios : Repositorio<Usuario>(){

    fun buscarIdPorUsername(username: String): Int?{
        return elementos.find { it.userName == username }?.id ?: throw ElementoInexistenteException("Usuario o contraseña inexistente.")
    }

    fun generarFiguritasCedidas(usuarios: List<Usuario>): List<FiguritaDTO>{
        return usuarios.map{usuario_ -> usuario_.figuritasARegalar().map{ figurita -> figurita.toDTO(usuario_.toDTODetalles())}}.flatten()
    }
    fun generarFiguritasCedidasPara(idUsuario: Int): List<FiguritaDTO>{
        val usuario = this.getById(idUsuario)
        val numerosFiguritasFaltantes = numeroFiguritasFaltantes(usuario)
        val otrosUsuarios = this.elementos.filter{ usuario_ -> usuario_.id != idUsuario}
        val figuritasCedidas_ = generarFiguritasCedidas(otrosUsuarios)
        val figuritasCedidas = figuritasCedidas_.filter{figurita -> numerosFiguritasFaltantes.contains(figurita.numeroFigurita)}

        figuritasFiltradas = figuritasCedidas

        return figuritasCedidas
    }
    fun cantidadoFiguritasRepetidas():Int = elementos.sumOf { it -> it.figuritasRepetidas.size }

    fun cantidadFiguritasOfrecidas():Int = elementos.sumOf { it -> it.figuritasARegalar().size}

    fun numeroFiguritasFaltantes(usuario: Usuario): List<Number> = usuario.figuritasFaltantes.map{figurita -> figurita.numero}

    fun figuritaRepetidaOFaltanteEnAlgunUsuario(figurita: Figurita): Boolean{
        return elementos.any{it.estaRepetida(figurita) || it.esFaltante(figurita)}
    }


}

@Component
class RepositorioPuntoDeVenta<T: PuntoDeVenta> : Repositorio<T>() {

    lateinit var puntosDeVentaFiltrados : List<PuntoDeVentaDTO>

    fun search(value: String, usuario: Usuario): List<PuntoDeVentaDTO> {
        return puntosDeVentaFiltrados.filter { it.nombre.contains(value, true) }
    }

    fun convertiraDTO(usuario: Usuario): List<PuntoDeVentaDTO> = elementos.map { it.toDTO(usuario) }

    fun masSobres(): List<PuntoDeVentaDTO>{
        return puntosDeVentaFiltrados.sortedByDescending{ it.stockSobres }
    }

    fun masBaratos(): List<PuntoDeVentaDTO> {
        return puntosDeVentaFiltrados.sortedBy { it.precioPorSobre }
    }

    fun menorDistancia(): List<PuntoDeVentaDTO>  {
        return puntosDeVentaFiltrados.sortedBy { it.distancia }
    }

    fun masCercanos(usuario: Usuario): List<PuntoDeVentaDTO>  {
        return puntosDeVentaFiltrados.filter { it.distancia < usuario.distanciaMaxima }
    }

    fun ordenarPuntos(tipoOrden :String , usuario: Usuario, nombreABuscar : String) : List<PuntoDeVentaDTO>{
        puntosDeVentaFiltrados = convertiraDTO(usuario)
        puntosDeVentaFiltrados = when(tipoOrden){
            "massobres" -> masSobres()
            "masbaratos" -> masBaratos()
            "menordistancia" -> menorDistancia()
            "mascercanos" -> masCercanos(usuario)
            else -> {convertiraDTO(usuario)}
        }
        if (nombreABuscar.isNotEmpty()) {
            puntosDeVentaFiltrados = search(nombreABuscar, usuario)
        }
        return puntosDeVentaFiltrados
    }

}
@Component
class RepositorioFiguritas : Repositorio<Figurita>(){
    fun contieneAlgunaAlJugador(jugador: Jugador): Boolean{
        return elementos.any { figurita -> figurita.jugador == jugador }
    }
}

@Component
class RepositorioJugadores : Repositorio<Jugador>(){
    fun contieneAlgunoALaSeleccion(seleccion: Seleccion): Boolean{
        return elementos.any { jugador -> jugador.seleccion == seleccion }
    }
}

@Component
class RepositorioSelecciones: Repositorio<Seleccion>(){

}