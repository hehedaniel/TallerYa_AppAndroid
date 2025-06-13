package com.danielhermoso.cliente

import java.io.*
import java.net.Socket

class Conexion (val serverAddr: String, val serverPort: Int) {

    private lateinit var socket: Socket
    private lateinit var out: BufferedWriter
    private lateinit var inBR: BufferedReader

    init {
        conectar()
    }

    private fun conectar() {
        socket = Socket(serverAddr, serverPort)
        out = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
        inBR = BufferedReader(InputStreamReader(socket.getInputStream()))
    }
}