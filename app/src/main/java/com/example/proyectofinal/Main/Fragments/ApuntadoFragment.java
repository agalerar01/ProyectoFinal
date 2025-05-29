package com.example.proyectofinal.Main.Fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyectofinal.Main.Controladores.ApuntadoAdapter;
import com.example.proyectofinal.Main.Controladores.EventoAdapter;
import com.example.proyectofinal.Main.Model.Apuntado;
import com.example.proyectofinal.Main.Model.Evento;
import com.example.proyectofinal.Main.Model.Usuario;
import com.example.proyectofinal.Main.ViewModel.ViewModelEvento;
import com.example.proyectofinal.R;
import com.example.proyectofinal.databinding.FragmentApuntadoBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ApuntadoFragment extends Fragment {

    FragmentApuntadoBinding binding;
    ViewModelEvento viewModel;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentApuntadoBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(requireView());

        ViewModelEvento viewModel = new ViewModelProvider(requireActivity()).get( ViewModelEvento.class);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        binding.recyclerApuntado.setLayoutManager(gridLayoutManager);

        ApuntadoAdapter ad = new ApuntadoAdapter();
        binding.recyclerApuntado.setAdapter(ad.recuperarAdapter(getLayoutInflater(),navController,R.id.action_apuntadoFragment_to_detallesFragment, this.getView(),viewModel));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                Evento evento = ad.recuperarPorPosicio(pos);

                viewModel.devolverUsuPorCorreo(auth.getCurrentUser().getEmail()).observe(getViewLifecycleOwner(), new Observer<Usuario>() {
                    @Override
                    public void onChanged(Usuario usuario) {

                        for(Apuntado a : evento.getlApuntados()){
                            if(a.getNombre().equals(usuario.getNombre())){
                                evento.getlApuntados().remove(a);
                                viewModel.actualizarEvento(evento);
                            }
                        }

                        viewModel.recuperarEventosApuntado(usuario.getNombre()).observe(getViewLifecycleOwner(), new Observer<List<Evento>>() {
                            @Override
                            public void onChanged(List<Evento> eventos) {
                                ad.establecerListaApuntados(eventos);
                                ad.notifyDataSetChanged();
                            }
                        });
                    }
                });


            }
        });

        itemTouchHelper.attachToRecyclerView(binding.recyclerApuntado);

        viewModel.devolverUsuPorCorreo(auth.getCurrentUser().getEmail()).observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                viewModel.recuperarEventosApuntado(usuario.getNombre()).observe(getViewLifecycleOwner(), new Observer<List<Evento>>() {
                    @Override
                    public void onChanged(List<Evento> eventos) {
                        if(eventos != null && !eventos.isEmpty()) {
                            ad.establecerListaApuntados(eventos);
                            ad.notifyDataSetChanged();
                        }else{
                            binding.recyclerApuntado.setVisibility(GONE);
                            binding.textoNoApuntado.setVisibility(VISIBLE);
                        }
                    }
                });
            }
        });
    }
}