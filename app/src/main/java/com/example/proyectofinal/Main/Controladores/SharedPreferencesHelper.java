package com.example.proyectofinal.Main.Controladores;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "prefs";
    private static final String PREFS_TEMA_OSCURO = "temaOscuro";
    private static final String PREFS_RECORDATORIO = "recordatorio";
    private static final String PREFS_DIAS_RECORDATORIO = "diasRecordatorio";
    private static final String PREFS_MOSTRAR_CORREO_EN_EVENTO = "mostrarCorreoEnEvento";
    private static final String PREFS_MOSTRAR_CORREO_EN_COMENTARIOS = "mostrarCorreoEnComentarios";
    private static final String PREFS_MOSTRAR_COMENTARIOS = "mostrarComentarios";
    private static final String PREFS_PRIMERA_VEZ = "primeraVez";
    private static final String PREFS_NOM_USU = "nomUsu";
    private static final String PREFS_IDIOMA = "idioma";

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void guardarTemaOscuro(boolean b){
        sharedPreferences.edit().putBoolean(PREFS_TEMA_OSCURO, b).apply();
    }

    public boolean devolverTemaOscuro(){
       return sharedPreferences.getBoolean(PREFS_TEMA_OSCURO, false);
    }

    public void guardarRecordatorio(boolean b){
        sharedPreferences.edit().putBoolean(PREFS_RECORDATORIO, b).apply();
    }

    public boolean devolverRecordatorio(){
        return sharedPreferences.getBoolean(PREFS_RECORDATORIO, false);
    }

    public void guardarDiasRecordatorio(int b){
        sharedPreferences.edit().putInt(PREFS_DIAS_RECORDATORIO, b).apply();
    }

    public int devolverDiasRecordatorio(){
        return sharedPreferences.getInt(PREFS_DIAS_RECORDATORIO, 3);
    }

    public void guardarMostrarCorreoEvento(boolean b){
        sharedPreferences.edit().putBoolean(PREFS_MOSTRAR_CORREO_EN_EVENTO, b).apply();
    }

    public boolean devolverMostrarCorreoEvento(){
        return sharedPreferences.getBoolean(PREFS_MOSTRAR_CORREO_EN_EVENTO, false);
    }

    public void guardarMostrarCorreoComentarios(boolean b){
        sharedPreferences.edit().putBoolean(PREFS_MOSTRAR_CORREO_EN_COMENTARIOS, b).apply();
    }

    public boolean devolverMostrarCorreoComentarios(){
        return sharedPreferences.getBoolean(PREFS_MOSTRAR_CORREO_EN_COMENTARIOS, false);
    }

    public void guardarMostrarComentarios(boolean b){
        sharedPreferences.edit().putBoolean(PREFS_MOSTRAR_COMENTARIOS, b).apply();
    }

    public boolean devolverMostrarComentarios(){
        return sharedPreferences.getBoolean(PREFS_MOSTRAR_COMENTARIOS, true);
    }

    public void guardarPrimeraVez(boolean b){
        sharedPreferences.edit().putBoolean(PREFS_PRIMERA_VEZ, b).apply();
    }

    public boolean devolverPrimeraVez(){
        return sharedPreferences.getBoolean(PREFS_PRIMERA_VEZ, true);
    }

    public void guardarNomUsu(String nom){
        sharedPreferences.edit().putString(PREFS_NOM_USU, nom).apply();
    }

    public String devolverNomUsu(){
        return sharedPreferences.getString(PREFS_NOM_USU, "Invitado");
    }

    public void guardarIdioma(String nom){
        sharedPreferences.edit().putString(PREFS_IDIOMA, nom).apply();
    }

    public String devolverIdioma(){
        return sharedPreferences.getString(PREFS_IDIOMA, "English");
    }
}
