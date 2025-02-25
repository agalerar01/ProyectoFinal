package com.example.proyectofinal.Main.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal.Main.EventoAdapter;
import com.example.proyectofinal.Main.Model.Evento;
import com.example.proyectofinal.Main.ViewModel.ViewModelEvento;
import com.example.proyectofinal.R;
import com.example.proyectofinal.databinding.FragmentBusquedaBinding;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BusquedaFragment extends Fragment {

    FragmentBusquedaBinding binding;
    Date fechaBusqueda;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBusquedaBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(requireView());

        ViewModelEvento viewModel = new ViewModelProvider(requireActivity()).get(ViewModelEvento.class);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        binding.recyclerBusqueda.setLayoutManager(gridLayoutManager);

        EventoAdapter ad = new EventoAdapter();
        binding.recyclerBusqueda.setAdapter(ad.recuperarAdapter(getLayoutInflater(),navController,R.id.action_busquedaFragment_to_detallesFragment, this.getView(),viewModel));

        binding.nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.nombre.isChecked()){
                    binding.fecha.setChecked(false);
                    binding.ciudad.setChecked(false);
                    binding.busqueda.setFocusable(true);
                }
            }
        });

        binding.fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.fecha.isChecked()){
                    binding.nombre.setChecked(false);
                    binding.ciudad.setChecked(false);
                    binding.busqueda.setFocusable(false);
                }
            }
        });

        binding.ciudad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.ciudad.isChecked()){
                    binding.fecha.setChecked(false);
                    binding.nombre.setChecked(false);
                    binding.busqueda.setFocusable(true);
                }
            }
        });

        binding.busqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.fecha.isChecked()) {
                    final Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            requireContext(),
                            (view1, selectedYear, selectedMonth, selectedDay) -> {
                                String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                                binding.busqueda.setText(selectedDate);
                                calendar.set(Calendar.YEAR, selectedYear);
                                calendar.set(Calendar.MONTH, selectedMonth);
                                calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                                fechaBusqueda = calendar.getTime();
                            },
                            year, month, day);

                    datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

                    datePickerDialog.show();
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
                if(binding.nombre.isChecked()){

                } else if (binding.fecha.isChecked()) {
                    viewModel.recuperarEventosPorFecha(fechaBusqueda.getTime()).observe(getViewLifecycleOwner(), new Observer<List<Evento>>() {
                        @Override
                        public void onChanged(List<Evento> eventos) {
                            ad.establecerListaEventos(eventos);
                            ad.notifyDataSetChanged();
                        }
                    });
                }else if (binding.ciudad.isChecked()){

                }
            }
        });
    }
}