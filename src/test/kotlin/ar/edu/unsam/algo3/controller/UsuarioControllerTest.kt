//package ar.edu.unsam.algo3.controller
//
//import org.junit.jupiter.api.DisplayName
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@DisplayName("Dado un controller de Usuarios")
//class UsuarioControllerTest(@Autowired val mockMvc: MockMvc) {
//
//
//
//    val jsonConTodosLosUsuarios = """
//                [
//                    {
//                        "nombre": "juan",
//                        "apellido": "lopez",
//                        "userName": "admin",
//                        "fechaDeNacimiento": "1992-01-05",
//                        "mail": "juan@mail.com",
//                        "direccion": {
//                            "provincia": "Buenos Aires",
//                            "localidad": "San Martin",
//                            "calle": "calle",
//                            "altura": 2345,
//                            "ubiGeografica": {
//                                "x": -34.57813776963215,
//                                "y": -58.52762521767519
//                            }
//                        },
//                        "distanciaMaxima": 7.0,
//                        "tipoDeUsuario": {
//                            "tipo": "Par"
//                        },
//                        "id": 1,
//                        "figuritasFaltantes": [],
//                        "figuritasRepetidas": [],
//                        "seleccionesFavoritas": [],
//                        "solicitudObserver": []
//                    },
//                    {
//                        "nombre": "roberto",
//                        "apellido": "Carlos",
//                        "userName": "miusername",
//                        "fechaDeNacimiento": "1992-03-05",
//                        "mail": "example@mail.com",
//                        "direccion": {
//                            "provincia": "Buenos Aires",
//                            "localidad": "San Martin",
//                            "calle": "calle",
//                            "altura": 2345,
//                            "ubiGeografica": {
//                                "x": 1220.0,
//                                "y": 2200.0
//                            }
//                        },
//                        "distanciaMaxima": 3.0,
//
//                        "tipoDeUsuario": {
//                            "tipo": "Par"
//                        }
//
//                    },
//                    {
//                        "nombre": "luis",
//                        "apellido": "sanchez",
//                        "userName": "ls213",
//                        "fechaDeNacimiento": "1982-05-05",
//                        "mail": "luis@mail.com",
//                        "direccion": {
//                            "provincia": "Buenos Aires",
//                            "localidad": "San Martin",
//                            "calle": "calle",
//                            "altura": 2345,
//                            "ubiGeografica": {
//                                "x": 4122.0,
//                                "y": 2020.0
//                            }
//                        },
//                        "distanciaMaxima": 12.0,
//                        "tipoDeUsuario": {
//                            "tipo": "Par"
//                        },
//                        "id": 3,
//                        "figuritasFaltantes": [],
//                        "figuritasRepetidas": [],
//                        "seleccionesFavoritas": [],
//                        "solicitudObserver": []
//                    }
//                ]
//            """.trimIndent()
//
//    val jsonUsuarioConId1= """
//                {
//                    "nombre": "juan",
//                    "apellido": "lopez",
//                    "userName": "admin",
//                    "fechaDeNacimiento": "1992-01-05",
//                    "mail": "juan@mail.com",
//                    "direccion": {
//                        "provincia": "Buenos Aires",
//                        "localidad": "San Martin",
//                        "calle": "calle",
//                        "altura": 2345,
//                        "ubiGeografica": {
//                            "x": -34.57813776963215,
//                            "y": -58.52762521767519
//                        }
//                    },
//                    "distanciaMaxima": 7.0,
//                    "tipo": "Par"
//                }
//            """.trimIndent()
//    @Test
//    fun `llamo al endpoint de getAllUsuarios y me responde correcctamente`(){
//        mockMvc
//            .perform(MockMvcRequestBuilders.get("/usuarios?value="))
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.content().json(jsonConTodosLosUsuarios))
//    }
//    @Test
//    fun `llamo al endpoint que me devuelve un usuario por ID y me lo trae`(){
//        mockMvc
//            .perform(MockMvcRequestBuilders.get("/usuarios/1"))
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.content().json(jsonUsuarioConId1))
//    }
//}