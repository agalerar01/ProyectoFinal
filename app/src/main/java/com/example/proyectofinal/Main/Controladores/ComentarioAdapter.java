package com.example.proyectofinal.Main.Controladores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectofinal.Main.Model.Comentario;
import com.example.proyectofinal.Main.ViewModel.ViewModelEvento;
import com.example.proyectofinal.databinding.ViewholdercomentarioBinding;
import com.example.proyectofinal.databinding.ViewholderfotoBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ComentarioAdapter {
    private Adapter la;
    private ViewModelEvento viewModel;
    private SharedPreferencesHelper helper;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Activity activity;

    public Adapter recuperarAdapter(LayoutInflater lt, NavController navController, int id, View view, ViewModelEvento viewModel, Activity activity){
        this.la = new Adapter(lt, navController, id, view);
        this.viewModel = viewModel;
        helper = new SharedPreferencesHelper(lt.getContext());
        this.activity = activity;

        return la;
    }

    public void establecerListaComentarios(List<Comentario> lComentarios){
        la.establecerListaComentario(lComentarios);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged(){
        la.notifyDataSetChanged();
    }

    class ComentarioViewHolder extends RecyclerView.ViewHolder {
        private final ViewholdercomentarioBinding binding;

        public ComentarioViewHolder(ViewholdercomentarioBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class Adapter extends RecyclerView.Adapter<ComentarioViewHolder>{
        NavController navController;
        private final LayoutInflater lt;
        private final int id;
        private final View view;
        List<Comentario> lComentarios;

        public Adapter(LayoutInflater lt, NavController navController, int id, View view){
            this.lt = lt;
            this.navController = navController;
            this.id = id;
            this.view = view;
        }

        @NonNull
        @Override
        public ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ComentarioViewHolder(ViewholdercomentarioBinding.inflate(lt, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ComentarioViewHolder holder, int position) {
            Comentario comentario = lComentarios.get(position);

            if(helper.devolverMostrarCorreoComentarios()&&comentario.getCorreo() != null){
                holder.binding.nombreusuycorreo.setText("Usuario: "+comentario.getUsuario()+" Correo: "+comentario.getCorreo());
            }else{
                holder.binding.nombreusuycorreo.setText("Usuario: "+comentario.getUsuario());
            }

            holder.binding.textocomentario.setText(comentario.getComentario());
        }

        @Override
        public int getItemCount() {
            return lComentarios != null ? lComentarios.size() : 0;
        }


        public void establecerListaComentario(List<Comentario> lComentarios){
            this.lComentarios = lComentarios;
        }

        public Comentario obtenerComentario(int posicion){
            return lComentarios.get(posicion);
        }
    }
}
