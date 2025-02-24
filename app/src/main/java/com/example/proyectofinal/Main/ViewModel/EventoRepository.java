package com.example.proyectofinal.Main.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.proyectofinal.Main.Model.Evento;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EventoRepository {

    public void anadirEvento(Evento evento){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = db.collection("Eventos").document().getId();

        evento.setId(id);

        db.collection("Eventos").document(id)
                .set(evento);
    }

    public LiveData<List<Evento>> recuperarEventos(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        MutableLiveData<List<Evento>> eventosLiveData = new MutableLiveData<>();
        List<Evento> lEventos = new ArrayList<>();

        db.collection("Eventos").get()
                .addOnSuccessListener(querySnapshot -> {
                    for(DocumentSnapshot doc : querySnapshot){
                        lEventos.add(doc.toObject(Evento.class));
                    }

                    eventosLiveData.setValue(lEventos);
                 });

        return eventosLiveData;
    }
}
