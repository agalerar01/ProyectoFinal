package com.example.proyectofinal.Main.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyectofinal.Main.Controladores.ComentarioAdapter;
import com.example.proyectofinal.Main.Controladores.FotoAdapter;
import com.example.proyectofinal.Main.Controladores.SharedPreferencesHelper;
import com.example.proyectofinal.Main.ImageUtils;
import com.example.proyectofinal.Main.Model.Apuntado;
import com.example.proyectofinal.Main.Model.Comentario;
import com.example.proyectofinal.Main.Model.Evento;
import com.example.proyectofinal.Main.Model.Usuario;
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
    SharedPreferencesHelper helper;
    boolean mostrar = true;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;

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

        viewModelEvento.eventoElegido.observe(getViewLifecycleOwner(), new Observer<Evento>() {
            @Override
            public void onChanged(Evento evento) {
                eventoElegido = evento;
                binding.nombre.setText(evento.getNombre());
                binding.ciudad.setText(evento.getCiudad());
                binding.calle.setText(evento.getCalle());
                binding.descripcion.setText(evento.getDescripcion());
                binding.fechaLimite.setText("["+formatearFecha(evento.getFechaInicio())+"] / ["+formatearFecha(evento.getFechaFinal())+"]");

                if(evento.getlUrls() != null) {
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                    binding.recyclerFotos.setLayoutManager(gridLayoutManager);

                    FotoAdapter ad = new FotoAdapter();
                    binding.recyclerFotos.setAdapter(ad.recuperarAdapter(getLayoutInflater(), navController, R.id.action_misEventosFragment_to_detallesFragment, view, viewModelEvento, requireActivity()));

                    ad.establecerListaFotos(evento.getlUrls());
                    ad.notifyDataSetChanged();

                    binding.recyclerFotos.setVisibility(View.VISIBLE);
                }

                binding.progressBar2.setVisibility(View.GONE);
                binding.nombre.setVisibility(View.VISIBLE);
                binding.ciudad.setVisibility(View.VISIBLE);
                binding.calle.setVisibility(View.VISIBLE);
                binding.descripcion.setVisibility(View.VISIBLE);
                binding.fechaLimite.setVisibility(View.VISIBLE);
                binding.apuntarse.setVisibility(View.VISIBLE);

                if(helper.devolverMostrarComentarios()){
                    binding.botoncomentario.setVisibility(View.VISIBLE);
                    binding.recyclerComentario.setVisibility(View.VISIBLE);

                    if(evento.getlComentarios() != null){
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
                        binding.recyclerComentario.setLayoutManager(gridLayoutManager);

                        ComentarioAdapter ad = new ComentarioAdapter();
                        binding.recyclerComentario.setAdapter(ad.recuperarAdapter(getLayoutInflater(), navController, R.id.action_misEventosFragment_to_detallesFragment, view, viewModelEvento, requireActivity()));

                        ad.establecerListaComentarios(evento.getlComentarios());
                        ad.notifyDataSetChanged();
                    }
                }

                viewModelEvento.devolverUsuPorCorreo(mAuth.getCurrentUser().getEmail()).observe(getViewLifecycleOwner(), new Observer<Usuario>() {
                    @Override
                    public void onChanged(Usuario usuario) {
                        if(usuario.getCorreo().equals(evento.getCreador())){
                            binding.aniadirFoto.setVisibility(View.VISIBLE);
                            binding.apuntarse.setVisibility(View.GONE);
                            binding.genteapuntada.setVisibility(View.VISIBLE);
                            binding.genteapuntada.setText(binding.genteapuntada.getText()+" "+evento.getlApuntados().size());
                        }
                    }
                });
            }
        });

        binding.apuntarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Apuntado apuntado = new Apuntado();

                if(helper.devolverMostrarCorreoEvento()) {
                    apuntado.setCorreo(mAuth.getCurrentUser().getEmail());
                }

                if(isGoogleLogin()){
                    apuntado.setNombre(mAuth.getCurrentUser().getDisplayName());

                    eventoElegido.aniadirParticipante(apuntado);

                    viewModelEvento.actualizarEvento(eventoElegido);
                }else{
                    viewModelEvento.devolverUsuPorCorreo(mAuth.getCurrentUser().getEmail()).observe(getViewLifecycleOwner(), new Observer<Usuario>() {
                        @Override
                        public void onChanged(Usuario usuario) {
                            apuntado.setNombre(usuario.getNombre());

                            eventoElegido.aniadirParticipante(apuntado);

                            viewModelEvento.actualizarEvento(eventoElegido);
                        }
                    });
                }



                Toast.makeText(requireContext(),"Te has añadido al evento. Esperamos verte alli", Toast.LENGTH_SHORT).show();
            }
        });

        binding.botoncomentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mostrar) {
                    binding.textLayoutComentario.setVisibility(View.VISIBLE);
                    binding.guardarcomentario.setVisibility(View.VISIBLE);
                    mostrar = false;
                }else{
                    binding.textLayoutComentario.setVisibility(View.GONE);
                    binding.guardarcomentario.setVisibility(View.GONE);
                    mostrar = true;
                }
            }
        });

        binding.guardarcomentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.comentario.getText().toString().equalsIgnoreCase("")){
                    binding.comentario.setError("El comentario no puede estar vacio");
                }else{
                    viewModelEvento.devolverUsuPorCorreo(mAuth.getCurrentUser().getEmail()).observe(getViewLifecycleOwner(), new Observer<Usuario>() {
                        @Override
                        public void onChanged(Usuario usuario) {
                            Comentario comentario = new Comentario();

                            comentario.setUsuario(usuario.getNombre());
                            if (helper.devolverMostrarCorreoComentarios()) {
                                comentario.setCorreo(usuario.getCorreo());
                            }
                            comentario.setComentario(binding.comentario.getText().toString());

                            eventoElegido.aniadirComentario(comentario);

                            viewModelEvento.actualizarEvento(eventoElegido);

                            binding.comentario.setText("");
                        }
                    });
                }
            }
        });

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) { //  Se ejecuta automáticamente cuando el usuario selecciona una imagen o cancela.
                        // Si se ha seleccionado una imagen, la recuperamos del parámetro del método
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            selectedImageUri = result.getData().getData();  // URI de la imagen seleccionada
                            viewModelEvento.uploadImageUsu(selectedImageUri).observe(getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    eventoElegido.aniadirFoto(s);

                                    viewModelEvento.actualizarEvento(eventoElegido);
                                }
                            });
                        }
                    }
                }
        );

        binding.aniadirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imagePickerLauncher.launch(intent);
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