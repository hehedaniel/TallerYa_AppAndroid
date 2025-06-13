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
import com.danielhermoso.cliente.modelos.Taller
import com.danielhermoso.cliente.protocolo.Mensaje
import com.danielhermoso.cliente.viewmodel.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    appViewModel: AppViewModel,
    onClick: (Taller) -> Unit,
    modifier: Modifier = Modifier
) {
    val appUIState by appViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        val msg = Mensaje(
            Mensaje.acciones.LISTADO_TALLERES,
        )

        appViewModel.viewModelScope.launch(Dispatchers.IO) {
            appViewModel.enviarMnsajeAlServidor(msgEnviar = msg)
        }
    }


    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            appUIState.listaTalleres
        ) { taller ->
            TallerItem(
                taller,
                {onClick(taller)}
            )
        }

        if(!appUIState.talleresRecibidos){
            item{
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun TallerItem(
    taller: Taller,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
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
                text = stringResource(R.string.HomeScreen_direccion) + taller.direccion,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = stringResource(R.string.HomeScreen_telefono) + taller.telefono,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = stringResource(
                    R.string.HomeScreen_horario,
                    taller.horarioApertura,
                    taller.horarioCierre
                ),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
