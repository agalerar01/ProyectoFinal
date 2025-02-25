package com.example.proyectofinal.Main.ViewModel;

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

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void anadirEvento(Evento evento){
        String id = db.collection("Eventos").document().getId();

        evento.setId(id);

        db.collection("Eventos").document(id)
                .set(evento);
    }

    public LiveData<List<Evento>> recuperarEventos(){
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
        MutableLiveData<Evento> eventosLiveData = new MutableLiveData<>();

        db.collection("Eventos")
                .orderBy("fechaInicio", Query.Direction.ASCENDING)
                .whereGreaterThan("fechaInicio",new Date().getTime())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    eventosLiveData.setValue(querySnapshot.getDocuments().get(0).toObject(Evento.class));
                });

        return eventosLiveData;
    }

    public LiveData<List<Evento>> recuperarEventosPorFecha(long timestamp){
        MutableLiveData<List<Evento>> eventosLiveData = new MutableLiveData<>();
        List<Evento> lEventos = new ArrayList<>();

        db.collection("Eventos")
                .whereGreaterThan("fechaInicio", timestamp)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for(DocumentSnapshot doc : querySnapshot){
                        lEventos.add(doc.toObject(Evento.class));
                    }

                    eventosLiveData.setValue(lEventos);
                });

        return eventosLiveData;
    }

    public LiveData<List<Evento>> recuperarEventosPorNombre(String nom){
        MutableLiveData<List<Evento>> eventosLiveData = new MutableLiveData<>();
        List<Evento> lEventos = new ArrayList<>();

        db.collection("Eventos")
                .orderBy("nombre")
                .startAt(nom)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for(DocumentSnapshot doc : querySnapshot){
                        lEventos.add(doc.toObject(Evento.class));
                    }

                    eventosLiveData.setValue(lEventos);
                });

        return eventosLiveData;
    }

    public LiveData<List<Evento>> recuperarEventosPorCiudad(String nom){
        MutableLiveData<List<Evento>> eventosLiveData = new MutableLiveData<>();
        List<Evento> lEventos = new ArrayList<>();

        db.collection("Eventos")
                .whereEqualTo("ciudad", nom)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for(DocumentSnapshot doc : querySnapshot){
                        lEventos.add(doc.toObject(Evento.class));
                    }

                    eventosLiveData.setValue(lEventos);
                });

        return eventosLiveData;
    }
}
