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

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void anadirEvento(Evento evento){
        String id = db.collection("Eventos").document().getId();

        db.collection("Eventos").document(id)
                .set(evento)
                .addOnSuccessListener(command -> Log.d("Va?", "Creado"))
                .addOnFailureListener(command -> Log.d("Va?", "No va"));
    }

    public LiveData<List<Evento>> recuperarEventos(){
        MutableLiveData<List<Evento>> eventosLiveData = new MutableLiveData<>();
        List<Evento> lEventos = new ArrayList<>();

        db.collection("Eventos").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(DocumentSnapshot doc : queryDocumentSnapshots){
                lEventos.add(doc.toObject(Evento.class));
            }
        });

        eventosLiveData.setValue(lEventos);

        return eventosLiveData;
    }
}
