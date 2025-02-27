package com.example.proyectofinal.Main.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal.Main.Controladores.SharedPreferencesHelper;
import com.example.proyectofinal.databinding.FragmentPrivacidadBinding;

public class PrivacidadFragment extends Fragment {

    FragmentPrivacidadBinding binding;
    SharedPreferencesHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPrivacidadBinding.inflate(getLayoutInflater(), container, false);
        helper = new SharedPreferencesHelper(getContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(helper.devolverMostrarCorreoEvento()){
            binding.switchCorreoEvento.setChecked(true);
        }else{
            binding.switchCorreoEvento.setChecked(false);
        }

        if(helper.devolverMostrarCorreoComentarios()){
            binding.switchCorreoComentarios.setChecked(true);
        }else{
            binding.switchCorreoComentarios.setChecked(false);
        }

        if(helper.devolverMostrarComentarios()){
            binding.switchActivarComentarios.setChecked(true);
        }else{
            binding.switchActivarComentarios.setChecked(false);
        }

        binding.guardarCambiosPrivacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.guardarMostrarCorreoEvento(binding.switchCorreoEvento.isChecked());
                helper.guardarMostrarCorreoComentarios(binding.switchCorreoComentarios.isChecked());
                helper.guardarMostrarComentarios(binding.switchActivarComentarios.isChecked());
            }
        });
    }
}