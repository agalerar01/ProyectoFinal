package com.example.proyectofinal.Main.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal.R;
import com.example.proyectofinal.databinding.FragmentAjustesBinding;

public class AjustesFragment extends Fragment {

    FragmentAjustesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAjustesBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }
}