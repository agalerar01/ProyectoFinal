package com.example.proyectofinal.Main.Model;

import java.util.Date;
import java.util.List;

public class Evento {

    private String id;
    private String nombre;
    private String ciudad;
    private String calle;
    private Long fechaInicio;
    private Long fechaFinal;
    private List<Comentario> lComentarios;
    private List<String> lApuntados;

    public Evento() {
    }

    public Evento(String nombre, String ciudad, String calle, Long fechaInicio, Long fechaFinal) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.calle = calle;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Long getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Long fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Long getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Long fechaFinal) {
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
