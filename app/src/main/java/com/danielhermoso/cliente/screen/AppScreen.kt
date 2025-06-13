package com.danielhermoso.cliente.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.danielhermoso.cliente.R
import com.danielhermoso.cliente.encriptador.SecureUtils
import com.danielhermoso.cliente.protocolo.Mensaje
import com.danielhermoso.cliente.viewmodel.AppViewModel
import com.danielhermoso.cliente.viewmodel.AppViewModelFactory
import com.danielhermoso.cliente.viewmodel.LoginViewModel
import com.danielhermoso.cliente.viewmodel.LoginViewModelFactory
import com.danielhermoso.cliente.viewmodel.SesionViewModel
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Arrays
import java.util.Properties


enum class AppScreens(val screenName: String){
    Login(screenName = "login"),
    Home(screenName = "home"),
    InfoTaller(screenName = "infoTaller"),
    CitaSeleccion(screenName = "citaSeleccion")
}

@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMain(
    navController: NavHostController = rememberNavController(),
    sesionViewModel: SesionViewModel = viewModel(),
    modifier: Modifier = Modifier
){

    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory()
    )

    val AppViewModel: AppViewModel = viewModel(
        factory = AppViewModelFactory(sesionViewModel)
    )

    val sesionUiState by sesionViewModel.uiState.collectAsState()

    val appUIState by AppViewModel.uiState.collectAsState()

    val context = LocalContext.current
    var preguntarConfig by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val properties = Properties()
        val configFile = File(context.filesDir, "taller.properties")

        if (configFile.exists()) {
            properties.load(InputStreamReader(FileInputStream(configFile)))
            val storedAddress = properties.getProperty("ADDRESS")
            val storedPort = properties.getProperty("PORT")

            if (storedAddress != null && storedPort != null) { //Todo ok
                launch(Dispatchers.IO) {
                    sesionViewModel.conectar(storedAddress, storedPort.toInt())
                    AppViewModel.escucharDelServidor_App()
                }
            } else { // Falta lago
                preguntarConfig = true
            }
        } else { //No hay archivo
            configFile.createNewFile()
            preguntarConfig = true
        }
    }

    if (preguntarConfig) {
        PreguntarConfiguracionServidor(
            sesionViewModel = sesionViewModel,
            appViewModel = AppViewModel,
            context = context,
            configCancelada = { (context as? Activity)?.finishAffinity() }, //Se cierra si se cancela
            configHecha = { //Quitar el input
                preguntarConfig = false
            }
        )
    }

    LaunchedEffect(sesionUiState.usuarioId) {
        if(sesionUiState.usuarioId != null){
            //Esto es q ha hecho login
            navController.navigate(AppScreens.Home.screenName)
        }
    }

    val appTitulo = stringResource(R.string.appTitulo)
    var topAppTitulo by remember {mutableStateOf(appTitulo)}
    val onBackHandler = {
        topAppTitulo = appTitulo
        navController.navigateUp()
    }

    Scaffold(
        topBar = {
            TallerAppTopBar(
                title = topAppTitulo,
                canNavigateBack = navController.previousBackStackEntry != null,
                onBackClick = { onBackHandler() }
            )
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = AppScreens.Login.screenName,
            modifier = modifier.padding(paddingValues)
        ){
            composable(route = AppScreens.Login.screenName){
                LoginScreen(
                    loginViewModel,
                    sesionViewModel,
                    onLoginClick = { usuario, contrasena ->

                        val passHasheada: String = SecureUtils.hashearPass(contrasena)

                        val msg = Mensaje(
                            Mensaje.acciones.LOGIN,
                            Arrays.asList(usuario, passHasheada, "1")
                        )
                        AppViewModel.viewModelScope.launch(Dispatchers.IO) {
                            AppViewModel.enviarMnsajeAlServidor(msgEnviar = msg)
                        }

                        topAppTitulo = context.getString(R.string.topBarPantallaInicio)
                    }
                )
            }

            composable(route = AppScreens.Home.screenName) {
                HomeScreen(
                    appViewModel = AppViewModel,
                    onClick = { taller ->
                        AppViewModel.guardarTallerSeleccionado(taller)
                        navController.navigate(AppScreens.InfoTaller.screenName)
                        topAppTitulo = context.getString(R.string.topBarPantallaTaller)
                    }
                )
            }

            composable(route = AppScreens.InfoTaller.screenName){
                InfoTaller(
                    appViewModel = AppViewModel,
                    onServicioClick = { servicio ->
                        AppViewModel.guardarServicioSeleccionado(servicio)
                        navController.navigate(AppScreens.CitaSeleccion.screenName)
                        topAppTitulo = context.getString(R.string.topBarCitaSeleccion)
                    }
                )
            }

            composable(route = AppScreens.CitaSeleccion.screenName){
                CitaSeleccionScreen(
                    appViewModel = AppViewModel,
                    sesionViewModel = sesionViewModel,
                    navController = navController,
                    onGuardarCitaClick = { CitaGuardar ->

                        var mapper = jacksonObjectMapper()

                        val msg = Mensaje(
                            Mensaje.acciones.GUARDAR_CITA,
                            listOf(mapper.writeValueAsString(CitaGuardar))
                        )

                        AppViewModel.viewModelScope.launch(Dispatchers.IO) {
                            AppViewModel.enviarMnsajeAlServidor(msgEnviar = msg)
                        }

                    }
                )
            }
        }
    }
}


@Composable
fun PreguntarConfiguracionServidor(
    sesionViewModel: SesionViewModel,
    appViewModel: AppViewModel,
    context: Context,
    configCancelada: () -> Unit,
    configHecha: () -> Unit
) {
    var address by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = configCancelada,
        title = { Text(stringResource(R.string.configuracion_de_conexion)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(stringResource(R.string.textoIpPuerto))
                TextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(stringResource(R.string.dirIp)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                TextField(
                    value = port,
                    onValueChange = { port = it },
                    label = { Text(stringResource(R.string.puerto)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (address.isNotEmpty() && port.isNotEmpty() && port.toIntOrNull() != null) {
                        val properties = Properties()
                        val archivoConfiguracionConexion = File(context.filesDir, "taller.properties")

                        try {
                            properties.setProperty("ADDRESS", address)
                            properties.setProperty("PORT", port)
                            properties.store(archivoConfiguracionConexion.outputStream(), null)

                            sesionViewModel.guardarAddressUsar(address)
                            sesionViewModel.guardarPortsUsar(port)

                            sesionViewModel.viewModelScope.launch(Dispatchers.IO) {
                                sesionViewModel.conectar(address, port.toInt())
                                appViewModel.escucharDelServidor_App()
                            }
                            
                            configHecha()
                            
                        } catch (e: Exception) {
                            Log.e("Configuración", "Error al guardar o conectar: ${e.message}")
                        }
                    }
                }
            ) {
                Text(stringResource(R.string.confirmar))
            }
        },
        dismissButton = {
            TextButton(onClick = configCancelada) {
                Text(stringResource(R.string.cancelar))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TallerAppTopBar(
    title: String,
    canNavigateBack: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (canNavigateBack) {
        TopAppBar(
            title = {
                Box(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {Text(title)}
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(
                            R.string.back
                        )
                    )
                }
            },
            modifier = modifier
        )
    } else {
        TopAppBar(
            title = {
                Box(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {Text(title)}
            },
            modifier = modifier
        )
    }
}