package com.example.proyectofinal.Main.Controladores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectofinal.Main.ViewModel.ViewModelEvento;
import com.example.proyectofinal.databinding.ViewholderBinding;
import com.example.proyectofinal.databinding.ViewholderfotoBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


public class FotoAdapter {
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

    public void establecerListaFotos(List<String> Urls){
        la.establecerListaFotos(Urls);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged(){
        la.notifyDataSetChanged();
    }

    class FotoViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderfotoBinding binding;

        public FotoViewHolder(ViewholderfotoBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class Adapter extends RecyclerView.Adapter<FotoViewHolder>{
        NavController navController;
        private final LayoutInflater lt;
        private final int id;
        private final View view;
        List<String> Urls;

        public Adapter(LayoutInflater lt, NavController navController, int id, View view){
            this.lt = lt;
            this.navController = navController;
            this.id = id;
            this.view = view;
        }

        @NonNull
        @Override
        public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FotoViewHolder(ViewholderfotoBinding.inflate(lt, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull FotoViewHolder holder, int position) {
            String url = Urls.get(position);

            Glide.with(activity)
                    .load(url)
                    .override(300,300)
                    .into(holder.binding.fotoHolder);
        }

        @Override
        public int getItemCount() {
            return Urls != null ? Urls.size() : 0;
        }


        public void establecerListaFotos(List<String> Urls){
            this.Urls = Urls;
        }

        public String obtenerFoto(int posicion){
            return Urls.get(posicion);
        }
    }
}
