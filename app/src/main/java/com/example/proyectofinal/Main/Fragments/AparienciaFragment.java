package com.example.proyectofinal.Main.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.proyectofinal.Main.Controladores.SharedPreferencesHelper;
import com.example.proyectofinal.Main.MainActivity;
import com.example.proyectofinal.R;
import com.example.proyectofinal.databinding.FragmentAparienciaBinding;

public class AparienciaFragment extends Fragment {

    FragmentAparienciaBinding binding;
    SharedPreferencesHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAparienciaBinding.inflate(getLayoutInflater(), container, false);
        helper = new SharedPreferencesHelper(getContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.languagues));

        binding.spinnerIdioma.setAdapter(adapter);

        binding.spinnerIdioma.setSelection(helper.devolverNumIdioma());

        if(helper.devolverTemaOscuro()){
            binding.switchModoOscuro.setChecked(true);
        }else{
            binding.switchModoOscuro.setChecked(false);
        }

        if(helper.devolverRecordatorio()){
            binding.switchRecordador.setChecked(true);
            binding.diasRecordatorio.setEnabled(true);
            binding.diasRecordatorio.setText(String.valueOf(helper.devolverDiasRecordatorio()));
        }else{
            binding.switchRecordador.setChecked(false);
            binding.diasRecordatorio.setEnabled(false);
            binding.diasRecordatorio.setText("3");
        }

        binding.switchRecordador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.switchRecordador.isChecked()){
                    binding.diasRecordatorio.setEnabled(false);
                }else{
                    binding.diasRecordatorio.setEnabled(true);
                }
            }
        });

        binding.botonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.switchModoOscuro.setChecked(false);
                binding.switchRecordador.setChecked(false);
                binding.diasRecordatorio.setEnabled(false);
                binding.diasRecordatorio.setText("3");

                helper.guardarTemaOscuro(false);
                helper.guardarRecordatorio(false);
                helper.guardarDiasRecordatorio(3);
            }
        });

        binding.guardarCambiosApariencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.guardarTemaOscuro(binding.switchModoOscuro.isChecked());
                helper.guardarRecordatorio(binding.switchRecordador.isChecked());
                helper.guardarDiasRecordatorio(Integer.parseInt(String.valueOf(binding.diasRecordatorio.getText())));
                helper.guardarIdioma(binding.spinnerIdioma.getSelectedItem().toString());
                helper.guardarNumIdioma(binding.spinnerIdioma.getSelectedItemPosition());
                helper.guardarIdiomaCambiado(true);

                Toast.makeText(getActivity(), R.string.restartapp, Toast.LENGTH_LONG).show();
            }
        });
    }
}