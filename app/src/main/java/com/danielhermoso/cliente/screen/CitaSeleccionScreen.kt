package com.danielhermoso.cliente.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.danielhermoso.cliente.R
import com.danielhermoso.cliente.modelos.CitaGuardar
import com.danielhermoso.cliente.protocolo.Mensaje
import com.danielhermoso.cliente.viewmodel.AppViewModel
import com.danielhermoso.cliente.viewmodel.SesionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitaSeleccionScreen(
    appViewModel: AppViewModel,
    sesionViewModel: SesionViewModel,
    onGuardarCitaClick: (CitaGuardar) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val appUIState by appViewModel.uiState.collectAsState()
    val sesionViewModel by sesionViewModel.uiState.collectAsState()

    var fechaSeleccionada by remember { mutableStateOf("") }
    var hoarSeleccionada by remember { mutableStateOf("") }

    var expandirFechas by remember { mutableStateOf(false) }
    var expandirHoras by remember { mutableStateOf(false) }

    var mecanicoTrabajo by remember { mutableStateOf("") }

    var motivoCita by remember { mutableStateOf("") }

    val fechasCita = mutableListOf<String>()

//Para que me de como opcion los dias de amñana a dos semanas
    for (i in 1..15) {
        val date = LocalDate.now().plusDays(i.toLong())
        fechasCita.add(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
    }

    LaunchedEffect(fechaSeleccionada) {
        if (fechaSeleccionada.isNotEmpty()) {
            val msg = Mensaje(
                Mensaje.acciones.OBTENER_HORAS_DISPONIBLES,
                listOf(
                    appUIState.tallerSeleccionado?.tallerId.toString(),
                    appUIState.servicioSeleccionado?.servicioId.toString(),
                    appUIState.servicioSeleccionado?.tiempoEstimadoTaller,
                    appUIState.tallerSeleccionado?.horarioApertura,
                    appUIState.tallerSeleccionado?.horarioCierre,
                    appUIState.servicioSeleccionado?.especialidadId,
                    fechaSeleccionada,
                    motivoCita
                )
            )
            
            appViewModel.viewModelScope.launch(Dispatchers.IO) {
                appViewModel.enviarMnsajeAlServidor(msgEnviar = msg)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = appUIState.servicioSeleccionado?.nombreServicio ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.CitaSelScreen_precio) + appUIState.servicioSeleccionado?.precio,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = stringResource(R.string.CitaSelScreen_descripcion) + appUIState.servicioSeleccionado?.descripcionTaller,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = stringResource(R.string.tiempo_estimado_minutos) + appUIState.servicioSeleccionado?.tiempoEstimadoTaller,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Text(
            text = stringResource(R.string.CitaSelScreen_selDia),
            style = MaterialTheme.typography.titleMedium
        )
        ExposedDropdownMenuBox(
            expanded = expandirFechas,
            onExpandedChange = { expandirFechas = !expandirFechas }
        ) {
            TextField(
                value = fechaSeleccionada,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirFechas) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandirFechas,
                onDismissRequest = { expandirFechas = false }
            ) {
                fechasCita.forEach { date ->
                    DropdownMenuItem(
                        text = { Text(date) },
                        onClick = {
                            fechaSeleccionada = date
                            expandirFechas = false
                        }
                    )
                }
            }
        }

        Text(
            text = stringResource(R.string.CitaSelScreen_selHora),
            style = MaterialTheme.typography.titleMedium
        )
        ExposedDropdownMenuBox(
            expanded = expandirHoras,
            onExpandedChange = { if (fechaSeleccionada.isNotEmpty()) expandirHoras = !expandirHoras },
        ) {
            TextField(
                value = hoarSeleccionada,
                onValueChange = {},
                readOnly = true,
                enabled = fechaSeleccionada.isNotEmpty(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirHoras) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandirHoras,
                onDismissRequest = { expandirHoras = false }
            ) {
                appUIState.listaHorariosDisponibles
                    .forEach { time ->
                    DropdownMenuItem(
                        text = { Text(time.horaInicioCita.toString()) },
                        onClick = {
                            hoarSeleccionada = time.horaInicioCita.toString()
                            mecanicoTrabajo = time.mecanicoId.toString()
                            expandirHoras = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Motivo de la cita
        OutlinedTextField(
            value = motivoCita,
            onValueChange = { motivoCita = it },
            label = { Text("Motivo de la cita") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        Button(
            onClick = {
                onGuardarCitaClick(CitaGuardar(
                    sesionViewModel.usuarioId,
                    appUIState.tallerSeleccionado?.tallerId.toString(),
                    appUIState.servicioSeleccionado?.servicioId,
                    mecanicoTrabajo,
                    fechaSeleccionada,
                    hoarSeleccionada,
                    motivoCita
                ))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.confirmarSeleccion))
        }
    }

    if (appUIState.citaAceptada) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(onClick = {
                    appViewModel.cambiarBoolCitaAceptadaCambiarVentana(true)
                }) {
                    Text(stringResource(R.string.cita_aceptada))
                }
            },
            text = {
                Text(stringResource(R.string.cita_aceptada_mensaje))
            }
        )
    } else if (appUIState.citaRechazada) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(onClick = {

                }) {
                    Text(stringResource(R.string.cita_rechazada))
                }
            },
            text = {
                Text(stringResource(R.string.cita_rechazada_mensaje))
            }
        )
    }

    LaunchedEffect(appUIState.citaAceptadaCambiarVentana) {
        if(appUIState.citaAceptadaCambiarVentana){
            appViewModel.cambiarBoolCitaAceptada(false)
            appViewModel.cambiarBoolCitaAceptadaCambiarVentana(false)
            navController.navigate(AppScreens.Home.screenName)
        }
    }

}

