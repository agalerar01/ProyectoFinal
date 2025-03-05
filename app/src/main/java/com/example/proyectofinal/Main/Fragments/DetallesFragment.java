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
import android.widget.Toast;

import com.example.proyectofinal.Main.Controladores.FotoAdapter;
import com.example.proyectofinal.Main.Controladores.SharedPreferencesHelper;
import com.example.proyectofinal.Main.Model.Apuntado;
import com.example.proyectofinal.Main.Model.Evento;
import com.example.proyectofinal.Main.ViewModel.EventoRepository;
import com.example.proyectofinal.Main.ViewModel.ViewModelEvento;
import com.example.proyectofinal.R;
import com.example.proyectofinal.databinding.FragmentDetallesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetallesFragment extends Fragment {

    FragmentDetallesBinding binding;
    Evento eventoElegido;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EventoRepository rep = new EventoRepository();
    SharedPreferencesHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDetallesBinding.inflate(getLayoutInflater(), container, false);

        helper = new SharedPreferencesHelper(getLayoutInflater().getContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelEvento viewModelEvento = new ViewModelProvider(requireActivity()).get(ViewModelEvento.class);
        NavController navController = Navigation.findNavController(requireView());

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() ->  {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            getActivity().runOnUiThread(() -> {
                binding.progressBar2.setVisibility(View.GONE);
                binding.nombre.setVisibility(View.VISIBLE);
                binding.ciudad.setVisibility(View.VISIBLE);
                binding.calle.setVisibility(View.VISIBLE);
                binding.descripcion.setVisibility(View.VISIBLE);
                binding.fechaLimite.setVisibility(View.VISIBLE);
                binding.recyclerFotos.setVisibility(View.VISIBLE);
                binding.apuntarse.setVisibility(View.VISIBLE);
            });
        });

        viewModelEvento.eventoElegido.observe(getViewLifecycleOwner(), new Observer<Evento>() {
            @Override
            public void onChanged(Evento evento) {
                eventoElegido = evento;
                binding.nombre.setText(evento.getNombre());
                binding.ciudad.setText(evento.getCiudad());
                binding.calle.setText(evento.getCalle());
                binding.descripcion.setText(evento.getDescripcion());
                binding.fechaLimite.setText("["+formatearFecha(evento.getFechaInicio())+"] / ["+formatearFecha(evento.getFechaFinal())+"]");

                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                binding.recyclerFotos.setLayoutManager(gridLayoutManager);

                FotoAdapter ad = new FotoAdapter();
                binding.recyclerFotos.setAdapter(ad.recuperarAdapter(getLayoutInflater(),navController, R.id.action_misEventosFragment_to_detallesFragment, view,viewModelEvento, requireActivity()));

                ad.establecerListaFotos(evento.getlUrls());
                ad.notifyDataSetChanged();
            }
        });

        binding.apuntarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Apuntado apuntado = new Apuntado();

                if(helper.devolverMostrarComentarios()) {
                    apuntado.setCorreo(mAuth.getCurrentUser().getEmail());
                }

                if(isGoogleLogin()){
                    apuntado.setNombre(mAuth.getCurrentUser().getDisplayName());
                }else{

                }

                eventoElegido.aniadirParticipante(apuntado);

                rep.actualizarEvento(eventoElegido);

                Toast.makeText(requireContext(),"Te has añadido al evento. Esperamos verte alli", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatearFecha(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    private boolean isGoogleLogin() {
        FirebaseUser user = mAuth.getCurrentUser();
        for (UserInfo profile : user.getProviderData()) {
            if (profile.getProviderId().equals("google.com")) {
                return true;
            }
        }
        return false;
    }
}