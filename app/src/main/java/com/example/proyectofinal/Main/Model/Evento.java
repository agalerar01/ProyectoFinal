package com.example.proyectofinal.Main.Model;

import java.util.Date;
import java.util.List;

public class Evento {

    private String nombre;
    private String ciudad;
    private String calle;
    private Date fechaInicio;
    private Date fechaFinal;
    private List<Comentario> lComentarios;
    private List<String> lApuntados;

    public Evento() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public List<Comentario> getlComentarios() {
        return lComentarios;
    }

    public void setlComentarios(List<Comentario> lComentarios) {
        this.lComentarios = lComentarios;
    }

    public List<String> getlApuntados() {
        return lApuntados;
    }

    public void setlApuntados(List<String> lApuntados) {
        this.lApuntados = lApuntados;
    }
}
