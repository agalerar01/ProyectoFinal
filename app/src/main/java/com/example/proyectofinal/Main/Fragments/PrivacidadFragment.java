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

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyectofinal.Main.Controladores.SharedPreferencesHelper;
import com.example.proyectofinal.Main.ImageUtils;
import com.example.proyectofinal.Main.Model.Usuario;
import com.example.proyectofinal.Main.ViewModel.ViewModelEvento;
import com.example.proyectofinal.R;
import com.example.proyectofinal.databinding.FragmentPrivacidadBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.io.IOException;

public class PrivacidadFragment extends Fragment {

    FragmentPrivacidadBinding binding;
    SharedPreferencesHelper helper;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;
    private byte[] fotoBlob;
    private ViewModelEvento viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPrivacidadBinding.inflate(getLayoutInflater(), container, false);
        helper = new SharedPreferencesHelper(getContext());

        viewModel = new ViewModelProvider(requireActivity()).get(ViewModelEvento.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(helper.devolverMostrarCorreoEvento()){
            binding.switchCorreoEvento.setChecked(true);
        }else{
            binding.switchCorreoEvento.setChecked(false);
        }

        if(helper.devolverMostrarCorreoComentarios()){
            binding.switchCorreoComentarios.setChecked(true);
        }else{
            binding.switchCorreoComentarios.setChecked(false);
        }

        if(helper.devolverMostrarComentarios()){
            binding.switchActivarComentarios.setChecked(true);
        }else{
            binding.switchActivarComentarios.setChecked(false);
        }

        if(isGoogleLogin()){
            Glide.with(this)
                    .load(mAuth.getCurrentUser().getPhotoUrl())
                    .override(240,240)
                    .circleCrop()
                    .into(binding.fotoPerfilDrawer);
            binding.nomusu.setText(mAuth.getCurrentUser().getDisplayName());
        }else{
            Glide.with(this)
                    .load(R.drawable.no_foto)
                    .override(240,240)
                    .circleCrop()
                    .into(binding.fotoPerfilDrawer);
            viewModel.devolverUsuPorCorreo(mAuth.getCurrentUser().getEmail()).observe(getViewLifecycleOwner(), new Observer<Usuario>() {
                @Override
                public void onChanged(Usuario usuario) {
                    if (usuario == null) return;

                    binding.nomusu.setText(usuario.getNombre());
                }
            });
        }

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            selectedImageUri = result.getData().getData();
                            fotoBlob = ImageUtils.uriToBlob(requireContext().getContentResolver(), selectedImageUri);

                            viewModel.devolverUsuPorCorreo(mAuth.getCurrentUser().getEmail()).observe(getViewLifecycleOwner(), new Observer<Usuario>() {
                                @Override
                                public void onChanged(Usuario usuario) {
                                    if (usuario == null) return;

                                    viewModel.uploadImageUsu(selectedImageUri).observe(getViewLifecycleOwner(), new Observer<String>() {
                                        @Override
                                        public void onChanged(String fileUrl) {
                                            if (fileUrl != null) {
                                                usuario.setFotoPerfil(fileUrl);

                                                Glide.with(requireActivity())
                                                        .load(usuario.getFotoPerfil())
                                                        .override(240, 240)
                                                        .circleCrop()
                                                        .into(binding.fotoPerfilDrawer);

                                                viewModel.actualizarUsuario(usuario);
                                            }
                                        }
                                    });
                                }
                            });
                            binding.fotoPerfilDrawer.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );

        binding.cambiarFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        binding.cambiarnomusu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.devolverUsuPorCorreo(mAuth.getCurrentUser().getEmail()).observe(getViewLifecycleOwner(), new Observer<Usuario>() {
                    @Override
                    public void onChanged(Usuario usuario) {
                        if (usuario == null) return;

                        if(binding.nombre.getText().toString().equalsIgnoreCase("")){
                            binding.nombre.setError("");
                            return;
                        }else{
                            binding.nomusu.setText(binding.nombre.getText());

                            usuario.setNombre(binding.nombre.getText().toString());

                            viewModel.actualizarUsuario(usuario);
                        }
                    }
                });
            }
        });

        binding.guardarCambiosPrivacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.guardarMostrarCorreoEvento(binding.switchCorreoEvento.isChecked());
                helper.guardarMostrarCorreoComentarios(binding.switchCorreoComentarios.isChecked());
                helper.guardarMostrarComentarios(binding.switchActivarComentarios.isChecked());
                Toast.makeText(getActivity(), R.string.restartapp, Toast.LENGTH_LONG).show();
            }
        });
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