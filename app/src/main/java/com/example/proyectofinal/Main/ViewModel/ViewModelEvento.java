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
import com.example.proyectofinal.Main.Model.Usuario;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewModelEvento extends AndroidViewModel {

    public MutableLiveData<Evento> eventoElegido = new MutableLiveData<>();
    EventoRepository rep = new EventoRepository();
    UsuarioRepository repUsu = new UsuarioRepository();
    public boolean idiomaNoCambiado= true;

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

        if (imageFileUri != null) {
            try {
                File imageFile = ImageUtils.getFileFromUri(getApplication().getApplicationContext(), imageFileUri);

                return Transformations.switchMap(rep.uploadImage(imageFile), fileUrl -> {
                    if (fileUrl != null) {
                        evento.aniadirFoto(fileUrl);
                        return rep.anadirEvento(evento);
                    } else {
                        resultadoLiveData.postValue(false);
                        return resultadoLiveData;
                    }
                });
            } catch (IOException e) {
                resultadoLiveData.postValue(false);
                return resultadoLiveData;
            }
        } else {
            return rep.anadirEvento(evento);
        }
    }

    public LiveData<Boolean> anadirUsu(Usuario usu){
        MutableLiveData<Boolean> bolean = new MutableLiveData<>();

        repUsu.anadirUsu(usu);

        bolean.postValue(true);
        return bolean;
    }

    public LiveData<Usuario> devolverUsuPorCorreo(String correo){

        return repUsu.devolverUsuPorCorreo(correo);
    }

    public LiveData<String> uploadImageUsu(Uri uri){
        File fileUri = null;
        try {
            fileUri = ImageUtils.getFileFromUri(getApplication().getApplicationContext(), uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return rep.uploadImageUsu(fileUri);
    }

    public void actualizarUsuario(Usuario usu){
        repUsu.actualizarUsu(usu);
    }

    public void actualizarEvento(Evento evento){
        rep.actualizarEvento(evento);
    }

    public LiveData<List<Evento>> recuperarEventosPorFechaYParticipacion(long timestamp,long timestamp2, String email){
        return rep.recuperarEventosPorFechaYParticipacion(timestamp, timestamp2, email);
    }
}
