package ar.edu.unsam.algo3.bootstrap

import ar.edu.unsam.algo3.domain.*
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import org.uqbar.geodds.Point
import java.time.LocalDate

@Service
class Bootstrap(
    val repositorioUsuario : RepositorioUsuarios,
    val repositorioPuntosDeventa :  RepositorioPuntoDeVenta<PuntoDeVenta>,
    val repositorioFiguritas : RepositorioFiguritas,
    val repositorioJugadores : RepositorioJugadores,
    val repositorioSelecciones : RepositorioSelecciones

) : InitializingBean {

    fun crearSelecciones() = repositorioSelecciones.apply{
        create(Seleccion("Argentina", Confederacion.CONMEBOL, 3, 15))
        create(Seleccion("Alemania", Confederacion.UEFA, 4, 4))
        create(Seleccion("Inglaterra", Confederacion.UEFA, 1, 0))
        create(Seleccion("Francia", Confederacion.UEFA, 2, 2))
        create(Seleccion("Portugal", Confederacion.UEFA, 0, 1))
        create(Seleccion("Polonia", Confederacion.UEFA, 0, 0))
        create(Seleccion("España", Confederacion.UEFA, 1, 2))
        create(Seleccion("Marruecos", Confederacion.AFC, 0, 0))
        create(Seleccion("Mexico", Confederacion.CONCACAF, 0, 0))
    }
    fun crearJugadores() = repositorioJugadores.apply{
        create(Jugador(
            "Nicolas",
            "Otamendi",
            LocalDate.of(1988, 2, 12),
            19,
            repositorioSelecciones.getById(1),
            LocalDate.of(2008, 11, 19),
            183.0,
            81.0,
            Defensor,
            true,
            5000000
        ))
        create(Jugador(
            "Lionel",
            "Messi",
            LocalDate.of(1987, 6, 24),
            10,
            repositorioSelecciones.getById(1),
            LocalDate.of(2005, 8, 17),
            170.0,
            72.0,
            Delantero,
            true,
            1000000000
        ))
        create(Jugador(
            "Jamal",
            "Musiala",
            LocalDate.of(2003, 2, 26),
            14,
            repositorioSelecciones.getById(2),
            LocalDate.of(2022, 12, 20),
            184.0,
            72.0,
            Delantero,
            false,
            17
        ))
        create(Jugador(
            "Jude",
            "Bellingham",
            LocalDate.of(2003, 6, 29),
            22,
            repositorioSelecciones.getById(3),
            LocalDate.of(2022, 12, 20),
            186.0,
            75.0,
            MedioCampista,
            false,
            18
        ))
        create(Jugador(
            "Enzo",
            "Fernandez",
            LocalDate.of(2001, 1, 17),
            5,
            repositorioSelecciones.getById(1),
            LocalDate.of(2022, 12, 20),
            160.0,
            60.0,
            Delantero,
            false,
            18
        ))
        create(Jugador(
            "Kylian",
            "Mbappe",
            LocalDate.of(1998, 12, 20),
            10,
            repositorioSelecciones.getById(4),
            LocalDate.of(2017, 3, 25),
            178.0,
            75.0,
            Delantero,
            true,
            90000000
        ))
        create(Jugador(
            "Rafael",
            "Leao",
            LocalDate.of(1999, 6, 16),
            10,
            repositorioSelecciones.getById(5),
            LocalDate.of(2021, 10, 9),
            188.0,
            81.0,
            Delantero,
            false,
            15
        ))
        create(Jugador(
            "Robert",
            "Lewandoski",
            LocalDate.of(1988, 8, 21),
            9,
            repositorioSelecciones.getById(6),
            LocalDate.of(2008, 9, 10),
            185.0,
            81.0,
            Delantero,
            false,
            30000000
        ))
        create(Jugador(
            "Sergio",
            "Ramos",
            LocalDate.of(1986, 3, 30),
            4,
            repositorioSelecciones.getById(7),
            LocalDate.of(2005, 3, 26),
            184.0,
            82.0,
            Defensor,
            true,
            5000000
        ))
        create(Jugador(
            "Cristiano",
            "Ronaldo",
            LocalDate.of(1985, 2, 5),
            7,
            repositorioSelecciones.getById(5),
            LocalDate.of(2002, 9, 19),
            187.0,
            83.0,
            Delantero,
            true,
            80000000
        ))
        create(Jugador(
            "Harry",
            "Kane",
            LocalDate.of(1993, 7, 28),
            9,
            repositorioSelecciones.getById(3),
            LocalDate.of(2010, 10, 8),
            188.0,
            86.0,
            Delantero,
            false,
            5000000
        ))
        create(Jugador(
            "Achraf",
            "Hakimi",
            LocalDate.of(1998, 11, 4),
            2,
            repositorioSelecciones.getById(8),
            LocalDate.of(2016, 10, 11),
            181.0,
            73.0,
            Defensor,
            false,
            3000000
        ))
        create(Jugador(
            "Emiliano",
            "Martinez",
            LocalDate.of(1992, 9, 2),
            23,
            repositorioSelecciones.getById(1),
            LocalDate.of(2011, 4, 27),
            1.94,
            84.0,
            Arquero,
            false,
            10000000
        ))
        create(Jugador(
            "Guillermo",
            "Ochoa",
            LocalDate.of(1985, 7, 13),
            1,
            repositorioSelecciones.getById(9),
            LocalDate.of(2005, 2, 16),
            1.70,
            78.0,
            Arquero,
            true,
            30
        ))
    }
    fun crearUsuarios() = repositorioUsuario.apply {
        create(Usuario(
            "juan",
            "lopez",
            "admin",
            LocalDate.of(1992, 1, 5),
            "juan@mail.com",
            Direccion("Buenos Aires", "San Martin", "calle", 2345, Point(-34.57813776963215, -58.52762521767519)),
            7.0,
            UsuarioDesprendido(),
        ).apply{
            this.registrarFiguritaRepetida(repositorioFiguritas.getById(1))
            this.registrarFiguritaRepetida(repositorioFiguritas.getById(2))
            this.registrarFiguritaRepetida(repositorioFiguritas.getById(3))
            this.registrarFiguritaRepetida(repositorioFiguritas.getById(8))
            this.registrarFiguritaFaltante(repositorioFiguritas.getById(6))
            this.registrarFiguritaFaltante(repositorioFiguritas.getById(9))
        })
        create(Usuario(
            "roberto",
            "Carlos",
            "finger",
            LocalDate.of(1992, 3, 5),
            "example@mail.com",
            Direccion("Buenos Aires", "San Martin", "calle", 2345, Point(1220, 2200)),
            3.0,
            UsuarioDesprendido(),
        ).apply{
            this.registrarFiguritaRepetida(repositorioFiguritas.getById(6))
            this.registrarFiguritaRepetida(repositorioFiguritas.getById(7))
            this.registrarFiguritaFaltante(repositorioFiguritas.getById(1))
            this.registrarFiguritaFaltante(repositorioFiguritas.getById(8))
        })
        create(Usuario(
            "luis",
            "sanchez",
            "huell",
            LocalDate.of(1982, 5, 5),
            "luis@mail.com",
            Direccion("Buenos Aires", "San Martin", "calle", 2345, Point(4122, 2020)),
            12.0,
            UsuarioDesprendido(),
        ).apply{
            this.registrarFiguritaRepetida(repositorioFiguritas.getById(9))
            this.registrarFiguritaRepetida(repositorioFiguritas.getById(10))
            this.registrarFiguritaRepetida(repositorioFiguritas.getById(8))
            this.registrarFiguritaFaltante(repositorioFiguritas.getById(1))
            this.registrarFiguritaFaltante(repositorioFiguritas.getById(6))
            this.registrarFiguritaFaltante(repositorioFiguritas.getById(7))
        })
    }
    fun crearPuntosDeVenta() = repositorioPuntosDeventa.apply {
        Kiosko(
            "Xeneize",
            Direccion("Buenos Aires", "San Martin", "Triunvirato", 1451, Point(-34.57107295792488, -58.53777599103668)),
            77,
            false
        ).apply {
            agregarPedido(Pedido(10, LocalDate.now().plusDays(5), false))
            agregarPedido(Pedido(3, LocalDate.now().plusDays(7), false))
            agregarPedido(Pedido(8, LocalDate.now().plusDays(4), false))
            agregarPedido(Pedido(5, LocalDate.now().plusDays(6), false))
            agregarPedido(Pedido(7, LocalDate.now().plusDays(10), false))
            agregarPedido(Pedido(2, LocalDate.now().plusDays(2), false))
            create(this)
        }

        Libreria(
            "Pepe",
            Direccion("Buenos Aires", "Chascomus", "Saenz Peña", 2345, Point(-35.58278277518326, -58.00807533670503)),
            100
        ).apply {
            agregarPedido(Pedido(5, LocalDate.now().plusDays(3), false))
            agregarPedido(Pedido(15, LocalDate.now().plusDays(7), false))
            agregarPedido(Pedido(9, LocalDate.now().plusDays(5), false))
             create(this)
        }

        Supermercado(
            "Dia",
            Direccion("Buenos Aires", "Castelar", "Jose Hernandez", 8989, Point(-34.63482727236517, -58.62935700007068)),
            50,
            DescuentoCompraGrande
        ).apply {
            agregarPedido(Pedido(10, LocalDate.now().plusDays(5), false))
            agregarPedido(Pedido(3, LocalDate.now().plusDays(7), false))
            agregarPedido(Pedido(8, LocalDate.now().plusDays(8), false))
            agregarPedido(Pedido(5, LocalDate.now().plusDays(11), false))
            agregarPedido(Pedido(7, LocalDate.now().plusDays(3), false))
            agregarPedido(Pedido(2, LocalDate.now().plusDays(4), false))
            agregarPedido(Pedido(5, LocalDate.now().plusDays(6), false))
            agregarPedido(Pedido(7, LocalDate.now().plusDays(5), false))
            agregarPedido(Pedido(2, LocalDate.now().plusDays(7), false))
            create(this)
        }

        Libreria(
            "TurboToy",
            Direccion("Buenos Aires", "San Martin", "Israel", 3551, Point(-34.58551055588788, -58.5247897379015)),
            150
        ).apply{
            agregarPedido(Pedido(10, LocalDate.now().plusDays(2), false))
            agregarPedido(Pedido(3, LocalDate.now().plusDays(6), false))
            agregarPedido(Pedido(8, LocalDate.now().plusDays(5), false))
            agregarPedido(Pedido(5, LocalDate.now().plusDays(7), false))
            agregarPedido(Pedido(7, LocalDate.now().plusDays(10), false))
            create(this)
        }

        Kiosko(
            "Bingo",
            Direccion("Buenos Aires", "San Martin", "Mitre", 4065, Point(-34.57879511403234, -58.54072752138931)),
            200,
            false
        ).apply{
            agregarPedido(Pedido(7, LocalDate.now().plusDays(5), false))
            create(this)
        }


        Supermercado(
            "Balduzzi",
            Direccion("Buenos Aires", "Capilla Del Señor", "Padre Fahy", 7765, Point(-34.28909841567301, -59.102304124252726)),
            400,
            DescuentoCompraGrande
        ).apply{
            agregarPedido(Pedido(5, LocalDate.now().plusDays(8), false))
            agregarPedido(Pedido(7, LocalDate.now().plusDays(4), false))
            create(this)
        }
    }

    fun crearFiguritas()= repositorioFiguritas.apply{
        create(Figurita(10, Impresion.BAJA, true, repositorioJugadores.getById(2)))
        create(Figurita(2, Impresion.MEDIA, false, repositorioJugadores.getById(3)))
        create(Figurita(4, Impresion.MEDIA, false, repositorioJugadores.getById(4)))
        create(Figurita(5, Impresion.MEDIA, false, repositorioJugadores.getById(5)))
        create(Figurita(3, Impresion.BAJA, true, repositorioJugadores.getById(6)))
        create(Figurita(3, Impresion.BAJA, true, repositorioJugadores.getById(7)))
        create(Figurita(28, Impresion.MEDIA, false, repositorioJugadores.getById(8)))
        create(Figurita(30, Impresion.MEDIA, false, repositorioJugadores.getById(9)))
        create(Figurita(7, Impresion.ALTA, false, repositorioJugadores.getById(10)))
        create(Figurita(6, Impresion.MEDIA, false, repositorioJugadores.getById(11)))
        create(Figurita(8, Impresion.MEDIA, false, repositorioJugadores.getById(12)))
        create(Figurita(21, Impresion.BAJA, true, repositorioJugadores.getById(13)))
        create(Figurita(12, Impresion.ALTA, false, repositorioJugadores.getById(14)))
        create(Figurita(15, Impresion.BAJA, true, repositorioJugadores.getById(1)))
    }
    override fun afterPropertiesSet() {
        crearSelecciones()
        crearJugadores()
        crearFiguritas()
        crearPuntosDeVenta()
        crearUsuarios()
    }

}