package com.example.proyectofinal.Main.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal.Main.Model.Evento;
import com.example.proyectofinal.Main.ViewModel.EventoRepository;
import com.example.proyectofinal.databinding.FragmentCrearEventoBinding;

import java.util.Calendar;
import java.util.Date;

public class CrearEventoFragment extends Fragment {

    FragmentCrearEventoBinding binding;
    EventoRepository rep = new EventoRepository();
    Date fechaIni, fechaFin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCrearEventoBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        requireContext(),
                        (view1, selectedYear, selectedMonth, selectedDay) -> {
                            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                            binding.fechaInicio.setText(selectedDate);
                            fechaIni = new Date(selectedDate);
                        },
                        year, month, day);

                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

                datePickerDialog.show();
            }
        });

        binding.fechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        requireContext(),
                        (view1, selectedYear, selectedMonth, selectedDay) -> {
                            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                            binding.fechaFinal.setText(selectedDate);
                            fechaFin = new Date(selectedDate);
                        },
                        year, month, day);

                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

                datePickerDialog.show();
            }
        });

        binding.botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Evento e = new Evento();

                e.setNombre(binding.nombre.getText().toString());
                e.setCiudad(binding.ciudad.getText().toString());
                e.setCalle(binding.calle.getText().toString());
                e.setDescripcion(binding.descripcion.getText().toString());
                e.setFechaInicio(fechaIni.getTime());
                e.setFechaFinal(fechaFin.getTime());

                rep.anadirEvento(e);
            }
        });
    }
}