package com.danielhermoso.cliente.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.danielhermoso.cliente.R
import com.danielhermoso.cliente.modelos.Servicio
import com.danielhermoso.cliente.modelos.Taller
import com.danielhermoso.cliente.protocolo.Mensaje
import com.danielhermoso.cliente.viewmodel.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoTaller(
    appViewModel: AppViewModel,
    onServicioClick: (Servicio) -> Unit,
    modifier: Modifier = Modifier
) {
    val appUIState by appViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        val msg = Mensaje(
            Mensaje.acciones.OBTENER_SERVICIOS_TALLER,
            listOf(appUIState.tallerSeleccionado?.tallerId.toString())
        )

        appViewModel.viewModelScope.launch(Dispatchers.IO) {
            appViewModel.enviarMnsajeAlServidor(msgEnviar = msg)
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            appUIState.tallerSeleccionado?.let { taller ->
                TallerInfoCard(taller = taller)
            }
        }

        item {
            Text(
                text = stringResource(R.string.InfoTallerScreen_servDisponibles),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        items(appUIState.lsitaServicios) { servicio ->
            ServicioCard(
                servicio = servicio,
                onClick = onServicioClick
            )
        }
    }
}

@Composable
fun TallerInfoCard(
    taller: Taller,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = taller.nombre,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.InfoTallerScreen_direccion, taller.direccion),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = stringResource(R.string.InfoTallerScreen_telefono, taller.telefono),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = stringResource(
                    R.string.InfoTallerScreen_horario,
                    taller.horarioApertura,
                    taller.horarioCierre
                ),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicioCard(
    servicio: Servicio,
    onClick: (Servicio) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onClick(servicio) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = servicio.nombreServicio,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.InfoTallerScreen_precio, servicio.precio),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = stringResource(R.string.InfoTallerScreen_descripcion, servicio.descripcionServicio),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = stringResource(
                    R.string.InfoTallerScreen_tiempoEstimado,
                    servicio.tiempoEstimadoTaller
                ),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}