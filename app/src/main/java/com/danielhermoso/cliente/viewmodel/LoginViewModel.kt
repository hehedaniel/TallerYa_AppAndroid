package com.danielhermoso.cliente.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.danielhermoso.cliente.uistate.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel (

): ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onNombreUsuarioChange(
        nombreUsuario: String
    ){
        _uiState.value = _uiState.value.copy(nombreUsuario = nombreUsuario)
    }

    fun onPassUsuarioChange(
        passUsuario: String
    ){
        _uiState.value = _uiState.value.copy(passUsuario = passUsuario)
    }

}

class LoginViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel() as T
        }
        throw IllegalArgumentException("Error en el viewmodel")
    }
}
