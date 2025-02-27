package com.example.proyectofinal.Main.Controladores;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConexionSupabase {

    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://clngyfzigczadjoyetiv.supabase.co";

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
