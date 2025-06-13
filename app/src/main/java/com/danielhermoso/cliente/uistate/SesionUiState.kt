package com.danielhermoso.cliente.uistate

import java.net.Socket

data class SesionUiState(

    val loginFallido: Boolean = false,

    var socket: Socket? = null,
    var usuarioNombre: String? = null,
    var usuarioId: String? = null,
    var address: String ?= null,
    var port: String ?= null,

    var addressConfigurada: Boolean = false,
    var portConfigurado: Boolean = false
)
