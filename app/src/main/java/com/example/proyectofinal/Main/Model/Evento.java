package com.example.proyectofinal.Main.Model;

import java.util.Date;
import java.util.List;

public class Evento {

    private String id;
    private String nombre;
    private String ciudad;
    private String calle;
    private String descripcion;
    private Long fechaInicio;
    private Long fechaFinal;
    private String creador;
    private List<Comentario> lComentarios;
    private List<String> lApuntados;
    private List<String> lUrls;

    public Evento() {
    }

    public Evento(String id, String nombre, String ciudad, String calle, String descripcion, Long fechaInicio, Long fechaFinal, String creador, List<Comentario> lComentarios, List<String> lApuntados, List<String> lUrls) {
        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.calle = calle;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.creador = creador;
        this.lComentarios = lComentarios;
        this.lApuntados = lApuntados;
        this.lUrls = lUrls;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public List<String> getlUrls() {
        return lUrls;
    }

    public void setlUrls(List<String> lUrls) {
        this.lUrls = lUrls;
    }

    public void aniadirFoto(String Url) {
        lUrls.add(Url);
    }
}
