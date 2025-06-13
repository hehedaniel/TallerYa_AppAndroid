package com.danielhermoso.cliente.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.danielhermoso.cliente.modelos.HorarioDisponible
import com.danielhermoso.cliente.uistate.AppUIState
import com.danielhermoso.cliente.modelos.Servicio
import com.danielhermoso.cliente.modelos.Taller
import com.danielhermoso.cliente.protocolo.Mensaje
import com.danielhermoso.cliente.protocolo.Serializador
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.DataOutputStream

class AppViewModel(
    private val SesionViewModel: SesionViewModel
) : ViewModel() {
private val _uiState = MutableStateFlow(AppUIState())
    val uiState: StateFlow<AppUIState> = _uiState.asStateFlow()

    private var dis: DataInputStream? = null
    private var dos: DataOutputStream? = null

    var lectorJob: Job? = null

    fun escucharDelServidor_App() {
        if (lectorJob != null) return

        lectorJob = viewModelScope.launch(Dispatchers.IO) {
            dis = SesionViewModel.getDataInputStream()
            dos = SesionViewModel.getDataOutputStream()

            while (true){

                var mensajeRecibido: String = dis?.readUTF() ?: ""

                var msgRecibido: Mensaje = Serializador.fromString(mensajeRecibido)

                var comando: String = msgRecibido.accion.name

                var mapper = jacksonObjectMapper()

                when (comando) {
                    "LOGIN" -> {
                        if(msgRecibido.parametros.get(0).equals("ACEPTADO")){
                            SesionViewModel.iniciarSesion(msgRecibido.parametros.get(2), msgRecibido.parametros.get(1))
                            Log.d("talle", msgRecibido.parametros.get(2) + msgRecibido.parametros.get(1))
                        }else {
                            SesionViewModel.loginIncorrecto()
                            Log.d("talle", "fallo el login")
                        }
                    }
                    "LISTADO_TALLERES" -> {
                        if(msgRecibido.parametros.get(0).equals("SIN DATOS")){
                            Log.d("talle", "No se recibiieron talleres")
                        }else {
                            recibirTalleres(mapper.readValue(msgRecibido.parametros.get(0), object : TypeReference<List<Taller>>() {}))
                        }
                    }
                    "OBTENER_SERVICIOS_TALLER" -> {
                        if(msgRecibido.parametros.get(0).equals("SIN DATOS")){
                            Log.d("talle", "No se recibiieron talleres")
                        }else {
                            recibiriServiciosTaller(mapper.readValue(msgRecibido.parametros.get(0), object : TypeReference<List<Servicio>>() {}))
                        }
                    }
                    "OBTENER_HORAS_DISPONIBLES" -> {
                        if(msgRecibido.parametros.get(0).equals("SIN DATOS")){
                            Log.d("talle", "No se recibiieron horas")
                        }else{
                            recibirHorariosDisponiblesCita(mapper.readValue(msgRecibido.parametros.get(0), object : TypeReference<List<HorarioDisponible>>() {}))
                        }
                    }
                    "GUARDAR_CITA" -> {
                        if(msgRecibido.parametros.get(0).equals("DENEGADO")){
                            Log.d("talle", "No se guardo la cita")
                        }else {
                            cambiarBoolCitaAceptada(true)
                        }
                    }
                    else -> {
                        Log.d("talle", "El comando recibido no esta")
                    }
                }
            }

        }
        lectorJob?.start()
    }

    fun recibirTalleres(
        listaTalleresRecibidos: List<Taller>
    ){
        _uiState.value = _uiState.value.copy(
            talleresRecibidos = true, listaTalleres = listaTalleresRecibidos
        )
    }

    fun vaciarListaTalleres(listaTalleresRecibidos: List<Taller>){
        _uiState.value = _uiState.value.copy(
            talleresRecibidos = false, listaTalleres = emptyList()
        )
    }

    fun recibiriServiciosTaller(
        listaServiciosTaller: List<Servicio>
    ){
        _uiState.value = _uiState.value.copy(
            serviciosRecibidos = true, lsitaServicios = listaServiciosTaller
        )
    }

    fun recibirHorariosDisponiblesCita(
        listaHorarioDisponible: List<HorarioDisponible>
    ){
        _uiState.value = _uiState.value.copy(
            horaDisponibleRecibida = true, listaHorariosDisponibles = listaHorarioDisponible
        )
    }

    fun guardarTallerSeleccionado(
        tallerSeleccionao: Taller
    ){
        _uiState.value = _uiState.value.copy(tallerSeleccionado = tallerSeleccionao)
    }

    fun vaciarTallerSeleccionado(){
        _uiState.value = _uiState.value.copy(tallerSeleccionado = null)
    }

    fun guardarServicioSeleccionado(servicio: Servicio) {
        _uiState.value = _uiState.value.copy(servicioSeleccionado = servicio)
    }

    fun vaciarServicioSeleccionado() {
        _uiState.value = _uiState.value.copy(servicioSeleccionado = null)
    }

    fun cambiarBoolCitaAceptada(estasdo: Boolean){
        _uiState.value = _uiState.value.copy(citaAceptada = estasdo)
    }

    fun cambiarBoolCitaRechazada(estasdo: Boolean){
        _uiState.value = _uiState.value.copy(citaRechazada = estasdo)
    }

    fun cambiarBoolCitaAceptadaCambiarVentana(estasdo: Boolean){
        _uiState.value = _uiState.value.copy(citaAceptadaCambiarVentana = estasdo)
    }

    fun enviarMnsajeAlServidor(msgEnviar: Mensaje) {
        Log.d("talle", Serializador.toString(msgEnviar))
        dos?.writeUTF(Serializador.toString(msgEnviar))
        dos?.flush()
    }
}

class AppViewModelFactory(
    private val SesionViewModel: SesionViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(SesionViewModel) as T
        }
        throw IllegalArgumentException("Error en el viewmodel")
    }
}