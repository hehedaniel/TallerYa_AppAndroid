package com.danielhermoso.cliente.modelos

data class Taller(
    var tallerId: Int ?= null,
    var nombre: String,
    var direccion: String,
    var telefono: String,
    var horarioApertura: String,
    var horarioCierre: String
)
