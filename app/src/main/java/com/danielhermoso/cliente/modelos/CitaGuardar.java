package com.danielhermoso.cliente.modelos;
public class CitaGuardar {
    private String usuarioId;
    private String tallerId;
    private String servicioId;
    private String mecanicoId;
    private String fecha;
    private String horaInicio;
    private String descripcionProblema;

    public CitaGuardar() {
    }

    public CitaGuardar(String usuarioId, String tallerId, String servicioId, String mecanicoId, String fecha, String horaInicio,
                       String descripcionProblema) {
        this.usuarioId = usuarioId;
        this.tallerId = tallerId;
        this.servicioId = servicioId;
        this.mecanicoId = mecanicoId;

        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.descripcionProblema = descripcionProblema;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTallerId() {
        return tallerId;
    }

    public void setTallerId(String tallerId) {
        this.tallerId = tallerId;
    }

    public String getServicioId() {
        return servicioId;
    }

    public void setServicioId(String servicioId) {
        this.servicioId = servicioId;
    }

    public String getMecanicoId() {
        return mecanicoId;
    }

    public void setMecanicoId(String mecanicoId) {
        this.mecanicoId = mecanicoId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getDescripcionProblema() {
        return descripcionProblema;
    }

    public void setDescripcionProblema(String descripcionProblema) {
        this.descripcionProblema = descripcionProblema;
    }
}
