package com.example.proyectofinal.Main.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal.databinding.FragmentNuevaEventoBinding;

public class NuevaEventoFragment extends Fragment {

    FragmentNuevaEventoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNuevaEventoBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }
}