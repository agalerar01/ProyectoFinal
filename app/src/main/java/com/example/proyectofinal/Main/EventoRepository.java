package com.example.proyectofinal.Main;

import android.util.Log;
import android.widget.Toast;

import com.example.proyectofinal.Main.Model.Evento;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventoRepository {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void anadirEvento(Evento evento){
        String id = db.collection("Eventos").document().getId();

        db.collection("Eventos").document(id)
                .set(evento)
                .addOnSuccessListener(command -> Log.d("Va?", "Creado"))
                .addOnFailureListener(command -> Log.d("Va?", "No va"));

    }
}
