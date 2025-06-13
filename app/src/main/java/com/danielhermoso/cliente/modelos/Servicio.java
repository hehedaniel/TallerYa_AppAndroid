package com.danielhermoso.cliente.modelos;

public class Servicio {
    private String taller_servicio_id, taller_id, servicio_id, nombre_servicio, precio, tiempo_estimado_taller, descripcion_taller, disponible, descripcion_servicio, tiempo_estimado_servicio, especialidad_id;

    public Servicio(){}

    public Servicio(String taller_servicio_id, String taller_id, String servicio_id, String nombre_servicio, String precio, String tiempo_estimado_taller, String descripcion_taller, String disponible, String descripcion_servicio, String tiempo_estimado_servicio, String especialidad_id) {
        this.taller_servicio_id = taller_servicio_id;
        this.taller_id = taller_id;
        this.servicio_id = servicio_id;
        this.nombre_servicio = nombre_servicio;
        this.precio = precio;
        this.tiempo_estimado_taller = tiempo_estimado_taller;
        this.descripcion_taller = descripcion_taller;
        this.disponible = disponible;
        this.descripcion_servicio = descripcion_servicio;
        this.tiempo_estimado_servicio = tiempo_estimado_servicio;
        this.especialidad_id = especialidad_id;
    }

    public String getTallerServicioId() {
        return taller_servicio_id;
    }

    public void setTallerServicioId(String taller_servicio_id) {
        this.taller_servicio_id = taller_servicio_id;
    }

    public String getTallerId() {
        return taller_id;
    }

    public void setTallerId(String taller_id) {
        this.taller_id = taller_id;
    }

    public String getServicioId() {
        return servicio_id;
    }

    public void setServicioId(String servicio_id) {
        this.servicio_id = servicio_id;
    }

    public String getNombreServicio() {
        return nombre_servicio;
    }

    public void setNombreServicio(String nombre_servicio) {
        this.nombre_servicio = nombre_servicio;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getTiempoEstimadoTaller() {
        return tiempo_estimado_taller;
    }

    public void setTiempoEstimadoTaller(String tiempo_estimado_taller) {
        this.tiempo_estimado_taller = tiempo_estimado_taller;
    }

    public String getDescripcionTaller() {
        return descripcion_taller;
    }

    public void setDescripcionTaller(String descripcion_taller) {
        this.descripcion_taller = descripcion_taller;
    }

    public String getDisponible() {
        return disponible;
    }

    public void setDisponible(String disponible) {
        this.disponible = disponible;
    }

    public String getDescripcionServicio() {
        return descripcion_servicio;
    }

    public void setDescripcionServicio(String descripcion_servicio) {
        this.descripcion_servicio = descripcion_servicio;
    }

    public String getTiempoEstimadoServicio() {
        return tiempo_estimado_servicio;
    }

    public void setTiempoEstimadoServicio(String tiempo_estimado_servicio) {
        this.tiempo_estimado_servicio = tiempo_estimado_servicio;
    }

    public String getEspecialidadId() {
        return especialidad_id;
    }

    public void setEspecialidadId(String especialidad_id) {
        this.especialidad_id = especialidad_id;
    }
}

