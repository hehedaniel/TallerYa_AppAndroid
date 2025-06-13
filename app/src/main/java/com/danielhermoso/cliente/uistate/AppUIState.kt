package com.danielhermoso.cliente.uistate

import com.danielhermoso.cliente.modelos.HorarioDisponible
import com.danielhermoso.cliente.modelos.Servicio
import com.danielhermoso.cliente.modelos.Taller

data class AppUIState(
    var talleresRecibidos: Boolean = false,
    var listaTalleres: List<Taller> = emptyList(),

    var tallerSeleccionado: Taller ?= null,

    var serviciosRecibidos: Boolean = false,
    var lsitaServicios: List<Servicio> = emptyList(),

    var servicioSeleccionado: Servicio ?= null,

    var horaDisponibleRecibida: Boolean = false,
    var listaHorariosDisponibles: List<HorarioDisponible> = emptyList(),


    //Para cuando la cita es aceptada
    var citaAceptada: Boolean = false,
    var citaRechazada: Boolean = false,

    var citaAceptadaCambiarVentana: Boolean = false
)
