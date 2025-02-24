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
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;

public class CrearEventoFragment extends Fragment {

    FragmentCrearEventoBinding binding;
    EventoRepository rep = new EventoRepository();
    FirebaseAuth auth = FirebaseAuth.getInstance();
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
                            calendar.set(Calendar.YEAR, selectedYear);
                            calendar.set(Calendar.MONTH, selectedMonth);
                            calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                            fechaIni = calendar.getTime();
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
                            calendar.set(Calendar.YEAR, selectedYear);
                            calendar.set(Calendar.MONTH, selectedMonth);
                            calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                            fechaFin = calendar.getTime();
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

                if(!binding.nombre.getText().toString().isEmpty()) {
                    e.setNombre(binding.nombre.getText().toString());
                }else{
                    binding.nombre.setError("El campo no puede estar vacio");
                    return;
                }

                if(!binding.ciudad.getText().toString().isEmpty()) {
                    e.setCiudad(binding.ciudad.getText().toString());
                }else{
                    binding.ciudad.setError("El campo no puede estar vacio");
                    return;
                }

                if (!binding.calle.getText().toString().isEmpty()) {
                    e.setCalle(binding.calle.getText().toString());
                }else{
                    binding.calle.setError("El campo no puede estar vacio");
                    return;
                }

                if (!binding.descripcion.getText().toString().isEmpty()) {
                    e.setDescripcion(binding.descripcion.getText().toString());
                }else{
                    binding.descripcion.setError("El campo no puede estar vacio");
                    return;
                }


                if (fechaIni != null) {
                    e.setFechaInicio(fechaIni.getTime());
                }else{
                    binding.fechaInicio.setError("El campo no puede estar vacio");
                    return;
                }

                if (fechaFin != null) {
                    e.setFechaFinal(fechaFin.getTime());
                }else{
                    binding.fechaFinal.setError("El campo no puede estar vacio");
                    return;
                }

                e.setCreador(auth.getCurrentUser().getEmail());

                rep.anadirEvento(e);

                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}