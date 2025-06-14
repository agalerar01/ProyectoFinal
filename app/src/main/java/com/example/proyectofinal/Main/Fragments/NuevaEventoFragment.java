package com.example.proyectofinal.Main.Fragments;

import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal.Main.Controladores.ComentarioAdapter;
import com.example.proyectofinal.Main.Controladores.EventoAdapter;
import com.example.proyectofinal.Main.Controladores.FotoAdapter;
import com.example.proyectofinal.Main.Controladores.SharedPreferencesHelper;
import com.example.proyectofinal.Main.Model.Evento;
import com.example.proyectofinal.Main.ViewModel.ViewModelEvento;
import com.example.proyectofinal.R;
import com.example.proyectofinal.databinding.FragmentNuevaEventoBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NuevaEventoFragment extends Fragment {

    FragmentNuevaEventoBinding binding;
    SharedPreferencesHelper helper;
    boolean noMostrado = true;
    ViewModelEvento viewModel;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNuevaEventoBinding.inflate(getLayoutInflater(), container, false);

        helper = new SharedPreferencesHelper(requireContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ViewModelEvento.class);
        NavController navController = Navigation.findNavController(requireView());

        viewModel.recuperarEventosProximo().observe(getViewLifecycleOwner(), new Observer<Evento>() {
            @Override
            public void onChanged(Evento evento) {
                if(evento != null) {
                    binding.nombreEvento.setText(evento.getNombre());
                    binding.city.setText(evento.getCiudad());
                    binding.calle.setText(evento.getCalle());
                    binding.descripcion.setText(evento.getDescripcion());
                    binding.fechas.setText("[" + formatearFecha(evento.getFechaInicio()) + "] / [" + formatearFecha(evento.getFechaFinal()) + "]");

                    if (evento.getlUrls() != null) {
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                        binding.recyclerProximoEvento.setLayoutManager(gridLayoutManager);

                        FotoAdapter ad = new FotoAdapter();
                        binding.recyclerProximoEvento.setAdapter(ad.recuperarAdapter(getLayoutInflater(), navController, R.id.action_misEventosFragment_to_detallesFragment, view, viewModel, requireActivity()));

                        ad.establecerListaFotos(evento.getlUrls());
                        ad.notifyDataSetChanged();

                        binding.recyclerProximoEvento.setVisibility(View.VISIBLE);
                    }

                    if(helper.devolverMostrarComentarios()){
                        binding.recyclerComentario.setVisibility(View.VISIBLE);

                        if(evento.getlComentarios() != null){
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
                            binding.recyclerComentario.setLayoutManager(gridLayoutManager);

                            ComentarioAdapter ad = new ComentarioAdapter();
                            binding.recyclerComentario.setAdapter(ad.recuperarAdapter(getLayoutInflater(), navController, R.id.action_misEventosFragment_to_detallesFragment, view, viewModel, requireActivity()));

                            ad.establecerListaComentarios(evento.getlComentarios());
                            ad.notifyDataSetChanged();
                        }
                    }

                    binding.progressBar.setVisibility(View.GONE);
                    binding.textobienvenida.setVisibility(View.GONE);
                    binding.nombreEvento.setVisibility(View.VISIBLE);
                    binding.city.setVisibility(View.VISIBLE);
                    binding.calle.setVisibility(View.VISIBLE);
                    binding.descripcion.setVisibility(View.VISIBLE);
                    binding.fechas.setVisibility(View.VISIBLE);
                }else{
                    binding.progressBar.setVisibility(View.GONE);
                    binding.textobienvenida.setVisibility(View.VISIBLE);
                    binding.nombreEvento.setVisibility(View.GONE);
                    binding.city.setVisibility(View.GONE);
                    binding.calle.setVisibility(View.GONE);
                    binding.descripcion.setVisibility(View.GONE);
                    binding.fechas.setVisibility(View.GONE);
                    binding.recyclerProximoEvento.setVisibility(View.GONE);
                }
                mostrarPopUp();
            }
        });
    }

    private String formatearFecha(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    private void mostrarPopUp(){

        if (noMostrado) {
            if(helper.devolverRecordatorio()) {
                Calendar calendar = Calendar.getInstance();

                calendar.add(Calendar.DAY_OF_MONTH, -1);
                Date fechaMenosUnDia = calendar.getTime();

                int diasASumar = helper.devolverDiasRecordatorio();

                calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, diasASumar+1);
                Date fechaMasXDias = calendar.getTime();

                viewModel.recuperarEventosPorFechaYParticipacion(fechaMenosUnDia.getTime(),fechaMasXDias.getTime(), mAuth.getCurrentUser().getEmail())
                        .observe(this, new Observer<List<Evento>>() {
                            @Override
                            public void onChanged(List<Evento> eventos) {
                                Evento evento = null;
                                if(eventos != null) {
                                    for(Evento e : eventos){
                                        for(int i =0; i < e.getlApuntados().size(); i++){
                                                evento = e;
                                        }
                                    }
                                    if (evento != null) {
                                        new AlertDialog.Builder(requireContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog)
                                                .setTitle("Aviso de evento proximo")
                                                .setMessage("Estabas apuntado al evento " + evento.getNombre() + " sera en la cidad " + evento.getCiudad()
                                                        + " en la calle " + evento.getCalle() + " esperamos verte alli :D")
                                                .setPositiveButton("Muchas Gracias", (dialog, which) -> {
                                                })
                                                .show();
                                    }
                                }
                            }
                        });
            }
            noMostrado = false;
        }
    }

}