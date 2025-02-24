package com.example.proyectofinal.Main.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal.Main.EventoRepository;
import com.example.proyectofinal.Main.Model.Evento;
import com.example.proyectofinal.R;
import com.example.proyectofinal.databinding.FragmentCrearEventoBinding;

public class CrearEventoFragment extends Fragment {

    FragmentCrearEventoBinding binding;
    EventoRepository rep = new EventoRepository();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCrearEventoBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
}