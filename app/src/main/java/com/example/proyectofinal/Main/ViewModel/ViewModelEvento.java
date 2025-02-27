package com.example.proyectofinal.Main.ViewModel;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.proyectofinal.Main.ImageUtils;
import com.example.proyectofinal.Main.Model.Evento;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ViewModelEvento extends AndroidViewModel {

    public MutableLiveData<Evento> eventoElegido = new MutableLiveData<>();
    EventoRepository rep = new EventoRepository();

    public ViewModelEvento(@NonNull Application application){
        super(application);
    }

    public LiveData<List<Evento>> recuperarEventos(){
        return rep.recuperarEventos();
    }

    public LiveData<Evento> recuperarEventosProximo(){
        return rep.recuperarEventosProximo();
    }

    public LiveData<List<Evento>> recuperarEventosPorFecha(long timestamp){
        return rep.recuperarEventosPorFecha(timestamp);
    }

    public LiveData<List<Evento>> recuperarEventosPorNombre(String nom){
        return rep.recuperarEventosPorNombre(nom);
    }

    public LiveData<List<Evento>> recuperarEventosPorCiudad(String nom){
        return rep.recuperarEventosPorCiudad(nom);
    }

    public LiveData<Boolean> aniadirEvento(Evento evento, Uri imageFileUri) {
        MutableLiveData<Boolean> resultadoLiveData = new MutableLiveData<>();

        try {
            // Convertimos el Uri en un File para enviarlo a Supabase
            // Más abajo tienes el código de esa clase estática
            File imageFile = ImageUtils.getFileFromUri(getApplication().getApplicationContext(), imageFileUri);

            // Usamos switchMap para esperar a que la imagen se suba antes de guardar el anuncio
            return Transformations.switchMap(rep.uploadImage(imageFile), fileUrl -> {
                if (fileUrl != null) {
                    // Creamos el anuncio solo si tenemos la URL de la imagen
                    evento.aniadirFoto(fileUrl);
                    return rep.anadirEvento(evento);
                } else {
                    // Si la subida falla, devolvemos false
                    resultadoLiveData.postValue(false);
                    return resultadoLiveData;
                }
            });

        } catch (IOException e) {
            // Capturamos posibles errores en la conversión del archivo
            resultadoLiveData.postValue(false);
        }

        return resultadoLiveData;
    }
}
