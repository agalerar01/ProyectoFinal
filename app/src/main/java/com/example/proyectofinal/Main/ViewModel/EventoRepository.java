package com.example.proyectofinal.Main.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.proyectofinal.Main.Model.Evento;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventoRepository {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

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

        db.collection("Eventos")
                .whereEqualTo("creador", mAuth.getCurrentUser().getEmail())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for(DocumentSnapshot doc : querySnapshot){
                        lEventos.add(doc.toObject(Evento.class));
                    }

                    eventosLiveData.setValue(lEventos);
                 });

        return eventosLiveData;
    }

    public LiveData<Evento> recuperarEventosProximo(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        MutableLiveData<Evento> eventosLiveData = new MutableLiveData<>();
        Calendar calendar = Calendar.getInstance();


        db.collection("Eventos")
                .orderBy("fechaInicio", Query.Direction.ASCENDING)
                .whereGreaterThan("fechaInicio",new Date().getTime())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    eventosLiveData.setValue(querySnapshot.getDocuments().get(0).toObject(Evento.class));
                });

        return eventosLiveData;
    }

    private String formatearFecha(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}
