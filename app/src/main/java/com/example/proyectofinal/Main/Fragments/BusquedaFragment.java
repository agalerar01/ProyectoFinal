package com.example.proyectofinal.Main.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal.R;
import com.example.proyectofinal.databinding.FragmentBusquedaBinding;

public class BusquedaFragment extends Fragment {

    FragmentBusquedaBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBusquedaBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.nombre.isChecked()){
                    binding.fecha.setChecked(false);
                    binding.ciudad.setChecked(false);
                }
            }
        });

        binding.fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.fecha.isChecked()){
                    binding.nombre.setChecked(false);
                    binding.ciudad.setChecked(false);
                }
            }
        });

        binding.ciudad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.ciudad.isChecked()){
                    binding.fecha.setChecked(false);
                    binding.nombre.setChecked(false);
                }
            }
        });

        binding.busqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //hacer codigo
            }
        });
    }
}