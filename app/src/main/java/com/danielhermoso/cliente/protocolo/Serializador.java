package com.danielhermoso.cliente.protocolo;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Daniel Martín Hermoso Hermoso
 */
public class Serializador {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toString(Mensaje mensaje) throws IOException {
        return mapper.writeValueAsString(mensaje);
    }

    public static Mensaje fromString(String mensajeStr) throws IOException {
        return mapper.readValue(mensajeStr, Mensaje.class);
    }

}
