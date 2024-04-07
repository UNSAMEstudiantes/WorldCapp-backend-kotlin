package ar.edu.unsam.algo3.domain

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

interface ServiceSelecciones {
    fun getSelecciones(): String
}

class Actualizador(val servicio: ServiceSelecciones, val repo: Repositorio<Seleccion>) {

    private fun getListaDeSelecciones(): List<Seleccion>  {
        val gson = Gson()
        val listType = object : TypeToken<List<Seleccion>>() {}.type
        return gson.fromJson(servicio.getSelecciones(), listType)
     }

    fun actualizar() = getListaDeSelecciones().forEach { it ->
        if (!it.esNueva()) repo.update(it) else repo.create(it)
    }
}


