package com.danielhermoso.cliente.modelos;

public class HorarioDisponible {
    private int mecanicoId;
    private String horaInicioCita;
    private String horaFinCita;

    public HorarioDisponible() {
    }

    public HorarioDisponible(int mecanicoId, String horaInicioCita, String horaFinCita) {
        this.mecanicoId = mecanicoId;
        this.horaInicioCita = horaInicioCita;
        this.horaFinCita = horaFinCita;
    }

    public int getMecanicoId() {
        return mecanicoId;
    }

    public void setMecanicoId(int mecanicoId) {
        this.mecanicoId = mecanicoId;
    }

    public String getHoraInicioCita() {
        return horaInicioCita;
    }

    public void setHoraInicioCita(String horaInicioCita) {
        this.horaInicioCita = horaInicioCita;
    }

    public String getHoraFinCita() {
        return horaFinCita;
    }

    public void setHoraFinCita(String horaFinCita) {
        this.horaFinCita = horaFinCita;
    }
}
