package com.mrh.layouts

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mrh.layouts.ui.theme.LayoutsTheme

class MainActivity : ComponentActivity() {

    val listaEquipos = listOf<Equipo>(
        Equipo(
            id = 0,
            escudo = R.raw.rm,
            nombre = "Real Madrid C.F.",
            liga = "Liga EA Sports"
        ),
        Equipo(
            id = 1,
            escudo = R.raw.miami,
            nombre = "Inter Miami",
            liga = "MLS"
        ),
        Equipo(
            id = 2,
            escudo = R.raw.psg,
            nombre = "Paris Saint Germain",
            liga = "Ligue 1"
        )
    )

    val listaPaises = listOf<Pais>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            LayoutsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Cartas FIFA")
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                //Utilizar color del fondo de pantalla en aplicacion,
                                //en este caso, el fondo del TopAppBar
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                                //Forma de poner un color específico
                                //containerColor = Color.Magenta
                                , titleContentColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Home,
                                        contentDescription = "Inicio"
                                    )
                                },
                                onClick = {
                                    navController.navigate(route = "home")
                                },
                                selected = true,
                                label = {
                                    Text("Inicio")
                                }
                            )
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = "Equipos"
                                    )
                                },
                                onClick = {
                                    navController.navigate(route = "equipos")
                                },
                                selected = false,
                                label = {
                                    Text("Equipos")
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavigationHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @Composable
    fun NavigationHost(
        navController: NavHostController,
        modifier: Modifier = Modifier
    ) {
        NavHost(
            navController = navController,
            modifier = modifier,
            startDestination = "home"
        ) {
            composable(route = "home") {
                Greeting()
            }
            composable(route = "equipos") {
                ListaEquiposView(navController)
            }
            composable("jugadoresEquipo/{id}") { direccion ->
                val idEquipo = direccion.arguments!!.getString("id").toString().toInt()
                Greeting(id = idEquipo)
            }
            composable("jugadoresPais/{id}") { direccion ->
                val idPais = direccion.arguments!!.getString("id").toString().toInt()
                Greeting(pais = listaPaises[idPais].nombre)
            }
        }
    }

    @SuppressLint("ResourceType")
    @Composable
    fun Greeting(id: Int? = null, pais: String? = null) {
        var textoFiltrar by remember { mutableStateOf("") }
        val listaJugadores = listOf(
            Player(
                imagenJugador = R.raw.messi,
                nombre = "MESSI",
                bandera = R.raw.argentina,
                equipo = listaEquipos[1],
                posicion = "ED"
            ),
            Player(
                imagenJugador = R.raw.mbappe,
                nombre = "MBAPPÉ",
                bandera = R.raw.france,
                equipo = listaEquipos[0],
                posicion = "ED"
            ),
            Player(
                imagenJugador = R.raw.dembele,
                nombre = "DEMBELÉ",
                bandera = R.raw.france,
                equipo = listaEquipos[2],
                posicion = "ED"
            )
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = textoFiltrar,
                onValueChange = { textoUsuario ->
                    textoFiltrar = textoUsuario
                }
            )
            LazyVerticalGrid(
                columns = GridCells.FixedSize(200.dp)
            ) {
                items(filtrarJugadores(listaJugadores, textoFiltrar, id)) { jugador ->
                    PlayerCard(
                        imagenJugador = jugador.imagenJugador,
                        nombre = jugador.nombre,
                        bandera = jugador.bandera,
                        escudoEquipo = jugador.equipo.escudo,
                        posicion = jugador.posicion
                    )
                }
            }
        }
    }


    @SuppressLint("ResourceType")
    @Composable
    fun ListaEquiposView(navController: NavHostController) {
        var textoFiltrar by remember { mutableStateOf("") }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                //Poder hacer scroll en la columna
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = textoFiltrar,
                onValueChange = { textoUsuario ->
                    textoFiltrar = textoUsuario
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )
            for (equipo in listaEquipos.filter { equipo ->
                equipo.nombre.uppercase().contains(textoFiltrar.uppercase())
            }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    onClick = {
                        navController.navigate("jugadoresEquipo/"+equipo.id)
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Image(
                            painter = painterResource(equipo.escudo),
                            contentDescription = null,
                            modifier = Modifier
                                .size(90.dp)
                                .padding(start = 10.dp)
                        )
                        Column {
                            Text(text = equipo.nombre)
                            Text(text = equipo.liga)
                        }
                    }
                }
            }

        }

    }


    @SuppressLint("ResourceType")
    @Composable
    fun PlayerCard(
        imagenJugador: Int,
        nombre: String,
        bandera: Int,
        escudoEquipo: Int,
        posicion: String
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(R.raw.card),
                contentDescription = ""
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("91", color = Color.Black, fontSize = 18.sp)
                        Text(posicion, color = Color.Black, fontSize = 12.sp)
                        Image(
                            painter = painterResource(id = bandera),
                            modifier = Modifier.size(24.dp),
                            contentDescription = null
                        )
                        Image(
                            painter = painterResource(id = escudoEquipo),
                            modifier = Modifier.size(24.dp),
                            contentDescription = null
                        )
                    }
                    Image(
                        painter = painterResource(id = imagenJugador),
                        modifier = Modifier.size(120.dp),
                        contentDescription = null
                    )
                }

                Text(text = nombre, fontSize = 20.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column {
                        Text(text = "91 PAC", fontSize = 12.sp)
                        Text(text = "91 PAC", fontSize = 12.sp)
                        Text(text = "91 PAC", fontSize = 12.sp)
                    }
                    Column {
                        Text(text = "91 PAC", fontSize = 12.sp)
                        Text(text = "91 PAC", fontSize = 12.sp)
                        Text(text = "91 PAC", fontSize = 12.sp)
                    }
                }
            }
        }
    }

    /**
     * Funcion para filtrar una lista de jugadores por nombre
     */
    fun filtrarJugadores(
        listaJugadores: List<Player>,
        textoFiltrar: String,
        idEquipo: Int?
    ): List<Player> {
        // Lista de elementos modificable, a diferencia de una List que es fija, solo puede SOBRECARGARSE
        val listaFiltrada: ArrayList<Player> = ArrayList()
        val listaEquipoFiltrada: ArrayList<Player> = ArrayList()
        //Filtrar 1º por equipo y luego por texto
        if(idEquipo!= null){
            for (jugador in listaJugadores) {
                if (jugador.equipo.id == idEquipo) {
                    listaEquipoFiltrada.add(jugador)
                }
            }
            for (jugador in listaEquipoFiltrada) {
                if (jugador.nombre.uppercase().contains(textoFiltrar.uppercase())) {
                    listaFiltrada.add(jugador)
                }
            }
            return listaFiltrada
        }
        // Solo filtra por texto escrito
        else {
            for (jugador in listaJugadores) {
                if (jugador.nombre.uppercase().contains(textoFiltrar.uppercase())) {
                    listaFiltrada.add(jugador)
                }
            }
            return listaFiltrada
        }
    }

}