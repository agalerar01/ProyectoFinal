package com.example.proyectofinal.Main.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
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
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal.Main.ImageUtils;
import com.example.proyectofinal.Main.Model.Evento;
import com.example.proyectofinal.Main.ViewModel.EventoRepository;
import com.example.proyectofinal.Main.ViewModel.ViewModelEvento;
import com.example.proyectofinal.databinding.FragmentCrearEventoBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.transform.Result;

public class CrearEventoFragment extends Fragment {

    private FragmentCrearEventoBinding binding;
    private ViewModelEvento viewModel;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Date fechaIni, fechaFin;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;
    private byte[] fotoBlob;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCrearEventoBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModelEvento.class);
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

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) { //  Se ejecuta automáticamente cuando el usuario selecciona una imagen o cancela.
                        // Si se ha seleccionado una imagen, la recuperamos del parámetro del método
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            selectedImageUri = result.getData().getData();  // URI de la imagen seleccionada
                            fotoBlob = ImageUtils.uriToBlob(requireContext().getContentResolver(), selectedImageUri); // Conseguimos el BLOB a partir de la URI

                            binding.imagenEvento.setImageBitmap(ImageUtils.blobToBitmap(fotoBlob));  // Muestra la imagen en el ImageView
                            binding.imagenEvento.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );

        binding.imagen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
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

                e.setlApuntados(new ArrayList<>());

                e.setlComentarios(new ArrayList<>());

                e.setlUrls(new ArrayList<>());

                viewModel.aniadirEvento(e, selectedImageUri);

                binding.nombre.setText("");
                binding.ciudad.setText("");
                binding.calle.setText("");
                binding.descripcion.setText("");
                binding.fechaInicio.setText("");
                binding.fechaFinal.setText("");
                binding.imagenEvento.setVisibility(View.GONE);
            }
        });
    }
}