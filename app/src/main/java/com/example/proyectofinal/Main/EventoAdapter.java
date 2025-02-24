package com.example.proyectofinal.Main;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal.Main.Model.Evento;
import com.example.proyectofinal.Main.ViewModel.ViewModelEvento;
import com.example.proyectofinal.databinding.ViewholderBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventoAdapter {
    public Adapter la;
    ViewModelEvento viewModel;
    SharedPreferencesHelper helper;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public Adapter recuperarAdapter(LayoutInflater lt, NavController navController, int id, View view, ViewModelEvento viewModel){
        this.la = new Adapter(lt, navController, id, view);
        this.viewModel = viewModel;
        helper = new SharedPreferencesHelper(lt.getContext());

        return la;
    }

    public void establecerListaEventos(List<Evento> lEventos){
        la.establecerLista(lEventos);
        notifyDataSetChanged();
    }

    public Evento obtenerTarea(int posicion){
        return la.obtenerTarea(posicion);
    }

    public void notifyDataSetChanged(){
        la.notifyDataSetChanged();
    }

    class EventoViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderBinding binding;

        public EventoViewHolder(ViewholderBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class Adapter extends RecyclerView.Adapter<EventoViewHolder>{
        NavController navController;
        private final LayoutInflater lt;
        private final int id;
        private final View view;
        List<Evento> lEventos;

        public Adapter(LayoutInflater lt, NavController navController, int id, View view){
            this.lt = lt;
            this.navController = navController;
            this.id = id;
            this.view = view;
        }

        @NonNull
        @Override
        public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new EventoViewHolder(ViewholderBinding.inflate(lt, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
            Evento evento = lEventos.get(position);

            holder.binding.viewNombre.setText(evento.getNombre());
            holder.binding.fechaViewHolder.setText(formatearFecha(evento.getFechaInicio()));

            holder.itemView.setOnClickListener(v -> navegarPantallaDetalle(evento));
        }

        @Override
        public int getItemCount() {
            return lEventos != null ? lEventos.size() : 0;
        }

        public void navegarPantallaDetalle(Evento evento){
            if(evento != null){
                viewModel.eventoElegido.postValue(evento);
            }
            navController.navigate(id);
        }


        public void establecerLista(List<Evento> lEventos){
            this.lEventos = lEventos;
        }

        public Evento obtenerTarea(int posicion){
            return lEventos.get(posicion);
        }

        private String formatearFecha(long timestamp) {
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            return sdf.format(date);
        }
    }
}

