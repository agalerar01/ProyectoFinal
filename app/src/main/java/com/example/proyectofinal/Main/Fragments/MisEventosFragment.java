package com.example.proyectofinal.Main.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal.Main.Controladores.EventoAdapter;
import com.example.proyectofinal.Main.Model.Evento;
import com.example.proyectofinal.Main.ViewModel.ViewModelEvento;
import com.example.proyectofinal.R;
import com.example.proyectofinal.databinding.FragmentMisEventosBinding;

import java.util.List;

public class MisEventosFragment extends Fragment {

    FragmentMisEventosBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMisEventosBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(requireView());

        ViewModelEvento viewModel = new ViewModelProvider(requireActivity()).get( ViewModelEvento.class);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        binding.recyclerLista.setLayoutManager(gridLayoutManager);

        EventoAdapter ad = new EventoAdapter();
        binding.recyclerLista.setAdapter(ad.recuperarAdapter(getLayoutInflater(),navController,R.id.action_misEventosFragment_to_detallesFragment, this.getView(),viewModel));

        viewModel.recuperarEventos().observe(getViewLifecycleOwner(), new Observer<List<Evento>>() {
            @Override
            public void onChanged(List<Evento> eventos) {
                ad.establecerListaEventos(eventos);
                ad.notifyDataSetChanged();
            }
        });

        binding.floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_misEventosFragment_to_crearEventoFragment);
            }
        });
    }
}