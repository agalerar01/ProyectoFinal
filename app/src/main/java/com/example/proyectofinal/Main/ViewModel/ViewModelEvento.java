package com.example.proyectofinal.Main.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.proyectofinal.Main.Model.Evento;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ViewModelEvento extends AndroidViewModel {

    public static MutableLiveData<Evento> eventoElegido = new MutableLiveData<>();
    EventoRepository rep = new EventoRepository();

    public ViewModelEvento(@NonNull Application application){
        super(application);
    }

    public LiveData<List<Evento>> recuperarEventos(){
        return rep.recuperarEventos();
    }
}
