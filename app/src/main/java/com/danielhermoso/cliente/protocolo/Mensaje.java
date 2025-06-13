package com.danielhermoso.cliente.protocolo;


import java.io.*;
import java.util.List;

/**
 *
 * @author Daniel Martín Hermoso Hermoso
 */
public class Mensaje implements Serializable {

    public enum acciones {
        LOGIN,
        REGISTRO,
        OBTENER_CITAS_SEMANALES_TALLER,
        OBTENER_DATOS_VEHICULO,
        CAMBIAR_ESTADO_CITA,
        LISTADO_TALLERES,
        OBTENER_SERVICIOS_TALLER,
        OBTENER_HORAS_DISPONIBLES,
        GUARDAR_CITA
    }

    private acciones accion;
    private List<String> parametros;

    public Mensaje() {}

    public Mensaje(acciones accion) {
        this.accion = accion;
    }

    public Mensaje(acciones accion, List<String> parametros) {
        this.accion = accion;
        this.parametros = parametros;
    }

    public acciones getAccion() {
        return accion;
    }

    public List<String> getParametros() {
        return parametros;
    }

    public void setAccion(acciones accion) {
        this.accion = accion;
    }

    public void setParametros(List<String> parametros) {
        this.parametros = parametros;
    }



}

