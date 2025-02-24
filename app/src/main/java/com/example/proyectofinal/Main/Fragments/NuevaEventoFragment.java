package com.example.proyectofinal.Main.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal.Main.Model.Evento;
import com.example.proyectofinal.Main.ViewModel.ViewModelEvento;
import com.example.proyectofinal.databinding.FragmentNuevaEventoBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NuevaEventoFragment extends Fragment {

    FragmentNuevaEventoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNuevaEventoBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelEvento viewModel = new ViewModelProvider(requireActivity()).get(ViewModelEvento.class);

        viewModel.recuperarEventosProximo().observe(getViewLifecycleOwner(), new Observer<Evento>() {
            @Override
            public void onChanged(Evento evento) {
                binding.nombreEvento.setText(evento.getNombre());
                binding.city.setText(evento.getCiudad());
                binding.calle.setText(evento.getCalle());
                binding.descripcion.setText(evento.getDescripcion());
                binding.fechas.setText("["+formatearFecha(evento.getFechaInicio())+"] / ["+formatearFecha(evento.getFechaFinal())+"]");
            }
        });
    }

    private String formatearFecha(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}