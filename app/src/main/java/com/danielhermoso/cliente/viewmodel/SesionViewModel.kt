package com.danielhermoso.cliente.viewmodel

import androidx.lifecycle.ViewModel
import com.danielhermoso.cliente.uistate.SesionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class SesionViewModel(

): ViewModel() {
    private val _uiState = MutableStateFlow(SesionUiState())
    val uiState: StateFlow<SesionUiState> = _uiState.asStateFlow()

    private var dis: DataInputStream? = null
    private var dos: DataOutputStream? = null

    fun getDataInputStream(): DataInputStream? {
        return dis ?: run {
            dis = DataInputStream(_uiState.value.socket?.inputStream)
            dis
        }
    }

    fun getDataOutputStream(): DataOutputStream? {
        return dos ?: run {
            dos = DataOutputStream(_uiState.value.socket?.outputStream)
            dos
        }
    }

    fun iniciarSesion(usuario: String, idUsuario: String){
        _uiState.value = _uiState.value.copy(usuarioNombre = usuario, usuarioId = idUsuario)
    }

    fun conectar(address: String, port: Int) {
        if(_uiState.value.socket == null || _uiState.value.socket!!.isClosed){
            val socket = Socket(address, port)
            _uiState.value = _uiState.value.copy(
                socket = socket,
            )
        }
    }

    fun guardarAddressUsar(addressInput: String){
        _uiState.value = _uiState.value.copy(address = addressInput)
    }

    fun guardarPortsUsar(portInput: String){
        _uiState.value = _uiState.value.copy(port = portInput)
    }

    fun addressGuardad(valor: Boolean){
        _uiState.value = _uiState.value.copy(addressConfigurada = valor)
    }

    fun portGuardado(valor: Boolean){
        _uiState.value = _uiState.value.copy(portConfigurado = valor)
    }

    fun loginIncorrecto(){
        _uiState.value = _uiState.value.copy(loginFallido = true)
    }

}
