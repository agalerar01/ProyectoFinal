package com.example.proyectofinal.Main.ViewModel;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.proyectofinal.Main.Constantes;
import com.example.proyectofinal.Main.Controladores.ConexionSupabase;
import com.example.proyectofinal.Main.Controladores.SupabaseHelper;
import com.example.proyectofinal.Main.Model.Apuntado;
import com.example.proyectofinal.Main.Model.Evento;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.io.File;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventoRepository {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private SupabaseHelper helperSupabase = ConexionSupabase.getClient().create(SupabaseHelper.class);

    public LiveData<Boolean> anadirEvento(Evento evento){
        MutableLiveData<Boolean> bolean = new MutableLiveData<>();
        String id = db.collection("Eventos").document().getId();

        evento.setId(id);

        db.collection("Eventos").document(id)
                .set(evento);
        bolean.postValue(true);
        return bolean;
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

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);


        db.collection("Eventos")
                .orderBy("fechaInicio", Query.Direction.ASCENDING)
                .whereGreaterThan("fechaInicio", calendar.getTime().getTime())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if(!querySnapshot.isEmpty()) {
                        eventosLiveData.setValue(querySnapshot.getDocuments().get(0).toObject(Evento.class));
                    }else{
                        eventosLiveData.setValue(null);
                    }
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

    public LiveData<List<Evento>> recuperarEventosPorFechaYParticipacion(long timestamp,long timestamp2, String email){
        MutableLiveData<List<Evento>> eventosLiveData = new MutableLiveData<>();
        List<Evento> lEventos = new ArrayList<>();

        db.collection("Eventos")
                .orderBy("fechaInicio", Query.Direction.DESCENDING)
                .whereGreaterThan("fechaInicio", timestamp)
                .whereLessThan("fechaInicio", timestamp2)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        for(DocumentSnapshot doc : querySnapshot){
                            lEventos.add(doc.toObject(Evento.class));
                        }
                        eventosLiveData.setValue(lEventos);
                    } else {
                        eventosLiveData.setValue(null);
                    }
                });

        return eventosLiveData;
    }

    public LiveData<List<Evento>> recuperarEventosPorNombre(String nom){
        MutableLiveData<List<Evento>> eventosLiveData = new MutableLiveData<>();
        List<Evento> lEventos = new ArrayList<>();

        db.collection("Eventos")
                .orderBy("nombre")
                .startAt(nom)
                .endAt(nom + "\uf8ff")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for(DocumentSnapshot doc : querySnapshot){
                        lEventos.add(doc.toObject(Evento.class));
                    }

                    eventosLiveData.setValue(lEventos);
                });

        return eventosLiveData;
    }

    public void actualizarEvento(Evento evento){

        db.collection("Eventos")
                .document(evento.getId())
                .set(evento, SetOptions.merge());
    }

    public LiveData<List<Evento>> recuperarEventosPorCiudad(String nom){
        MutableLiveData<List<Evento>> eventosLiveData = new MutableLiveData<>();
        List<Evento> lEventos = new ArrayList<>();

        db.collection("Eventos")
                .orderBy("ciudad")
                .startAt(nom)
                .endAt(nom + "\uf8ff")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for(DocumentSnapshot doc : querySnapshot){
                        lEventos.add(doc.toObject(Evento.class));
                    }

                    eventosLiveData.setValue(lEventos);
                });

        return eventosLiveData;
    }

    public LiveData<String> uploadImage(File imageFile) {
        MutableLiveData<String> liveDataUrl = new MutableLiveData<>();

        // Crear el cuerpo de la petición para enviar a Supabase (en él se envía el fichero)
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);

        // Llamada a la API de Supabase
        // Param 1: Tu API KEY (Autenticación)
        // Param 2: nombre de tu bucket
        // Param 3: nombre con el que se creará el fichero en Supabase
        // Param 4: cuerpo de la petición (imagen)
        Call<Void> call = helperSupabase.uploadImage("Bearer " + Constantes.API_KEY, "foto_perfil", imageFile.getName(), body);

        // Enviamos la petición en segundo plano
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Si todo va bien, recuperamos la URL pública de la imagen en Supabase
                    // Esta URL será la que guardemos en nuestra base de datos
                    String fileUrl = response.raw().request().url().toString();
                    // Almacenamos el valor en el LiveData
                    liveDataUrl.postValue(fileUrl);
                } else {
                    // Si no se ha podido completar la petición, devolvemos null
                    liveDataUrl.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Si no se ha podido completar la petición, devolvemos null
                liveDataUrl.postValue(null);
            }
        });

        // Devolvemos el LiveData con la URL de nuestra imagen
        return liveDataUrl;
    }

    public LiveData<String> uploadImageUsu(File imageFile) {
        MutableLiveData<String> liveDataUrl = new MutableLiveData<>();

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);

        Call<Void> call = helperSupabase.uploadImage("Bearer " + Constantes.API_KEY, "foto_perfil", imageFile.getName(), body);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    String fileUrl = response.raw().request().url().toString();
                    Log.d("DEBUG", "Imagen subida con éxito: " + fileUrl);
                    liveDataUrl.postValue(fileUrl);
                } else {
                    Log.e("ERROR", "Error en la respuesta de Supabase: " + response.code());
                    liveDataUrl.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ERROR", "Fallo en la subida de imagen", t);
                liveDataUrl.postValue(null);
            }
        });

        return liveDataUrl;
    }

    public void eliminarEvento(Evento evento) {
        db.collection("Eventos").document(evento.getId())
                .delete();
    }

    public LiveData<List<Evento>> recuperarEventosPorApuntado(String nombre) {
        MutableLiveData<List<Evento>> eventosLiveData = new MutableLiveData<>();
        List<Evento> lEventos = new ArrayList<>();

        db.collection("Eventos")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for(DocumentSnapshot doc : querySnapshot){
                       List<Apuntado> lApuntados = doc.toObject(Evento.class).getlApuntados();

                       if(!lApuntados.isEmpty()){
                           for (Apuntado a : lApuntados){
                               if(a.getNombre().equals(nombre)){
                                   lEventos.add(doc.toObject(Evento.class));
                               }
                           }
                       }
                    }

                    eventosLiveData.setValue(lEventos);
                });

        return eventosLiveData;
    }
}
