package com.example.proyectofinal.Main.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.proyectofinal.Main.Controladores.ConexionSupabase;
import com.example.proyectofinal.Main.Controladores.SupabaseHelper;
import com.example.proyectofinal.Main.Model.Evento;
import com.example.proyectofinal.Main.Model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class UsuarioRepository {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private SupabaseHelper helperSupabase = ConexionSupabase.getClient().create(SupabaseHelper.class);

    public LiveData<Boolean> anadirUsu(Usuario usu){
        MutableLiveData<Boolean> bolean = new MutableLiveData<>();
        String id = db.collection("Usuarios").document().getId();

        usu.setId(id);

        db.collection("Usuarios").document(id)
                .set(usu);
        bolean.postValue(true);
        return bolean;
    }

    public LiveData<Usuario> devolverUsuPorCorreo(String correo){
        MutableLiveData<Usuario> usu = new MutableLiveData<>();

        db.collection("Usuarios").whereEqualTo("correo", correo)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if(querySnapshot.isEmpty()) {
                        usu.postValue(null);
                    }else{
                        usu.postValue(querySnapshot.getDocuments().get(0).toObject(Usuario.class));
                    }
                });

        return usu;
    }

    public void actualizarUsu(Usuario usu){

        db.collection("Usuarios")
                .document(usu.getId())
                .set(usu, SetOptions.merge());
    }
}
