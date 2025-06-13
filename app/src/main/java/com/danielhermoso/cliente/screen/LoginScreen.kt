package com.danielhermoso.cliente.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.danielhermoso.cliente.R
import com.danielhermoso.cliente.viewmodel.LoginViewModel
import com.danielhermoso.cliente.viewmodel.SesionViewModel

@Composable
fun LoginScreen(
    LoginViewModel: LoginViewModel,
    sesionViewModel: SesionViewModel,
    onLoginClick: (String, String) -> Unit
) {
    val loginUiState by LoginViewModel.uiState.collectAsState()
    val sessionUiState by sesionViewModel.uiState.collectAsState()

    var passVisible by remember { mutableStateOf(false) }

    var logoUsar = if (isSystemInDarkTheme()) R.drawable.nobgwhite
                    else R.drawable.nobgblack

    val txtIniciarSesion = if (sessionUiState.loginFallido) R.string.LoginScreen_iniciarSesionFallido
                            else R.string.LoginScreen_iniciarSesion

    val image = if (passVisible) Icons.Default.Visibility
                else Icons.Default.VisibilityOff

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = logoUsar),
            contentDescription = stringResource(R.string.LoginScreen_logo_description),
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(txtIniciarSesion),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = loginUiState.nombreUsuario,
            onValueChange = { LoginViewModel.onNombreUsuarioChange(it) },
            label = { Text(stringResource(R.string.LoginScreen_usuario)) },
            isError = sessionUiState.loginFallido,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = loginUiState.passUsuario,
            onValueChange = { LoginViewModel.onPassUsuarioChange(it) },
            isError = sessionUiState.loginFallido,
            label = { Text(stringResource(R.string.LoginScren_contrasena)) },
            visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passVisible = !passVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onLoginClick(loginUiState.nombreUsuario, loginUiState.passUsuario) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.LoginScreen_iniciarSesion))
        }
    }
}
