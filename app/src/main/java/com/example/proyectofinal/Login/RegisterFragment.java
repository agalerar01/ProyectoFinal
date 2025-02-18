package com.example.proyectofinal.Login;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyectofinal.Main.MainActivity;
import com.example.proyectofinal.R;
import com.example.proyectofinal.databinding.FragmentRegisterBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;
    NavController navController;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(requireView());

        configurarClienteGoogleSignIn();
        inicializarLauncherGoogleSignIn();

        binding.googleSignInButton.setOnClickListener(view1 -> signInWithGoogle());

        binding.volverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });

        binding.registroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.correo.getText().toString().equalsIgnoreCase("")) {
                    binding.correo.setError("El campo no puede estar vacio");
                    return;
                }
                String email = binding.correo.getText().toString();

                if (binding.contrasena.getText().toString().equalsIgnoreCase("")) {
                    binding.contrasena.setError("El campo no puede estar vacio");
                    return;
                }
                String contrasena = binding.contrasena.getText().toString();

                if (binding.repetirContrasena.getText().toString().equalsIgnoreCase("")) {
                    binding.repetirContrasena.setError("El campo no puede estar vacio");
                    return;
                }
                String reContra = binding.repetirContrasena.getText().toString();

                if (!contrasena.equals(reContra)) {
                    binding.repetirContrasena.setError("La contraseña no coinciden");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, contrasena)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser usuario = mAuth.getCurrentUser();
                                    Toast.makeText(getContext(), "Inicio de sesión exitoso: " + usuario.getEmail(), Toast.LENGTH_SHORT).show();
                                    iniciarMainActivity();
                                } else {
                                    Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    public void iniciarMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void configurarClienteGoogleSignIn() {
        // Configurar Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Usa tu Web client ID
                .requestEmail()
                .build();

        // Inicializar Google Sign-In a partir de la configuración previa
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }

    private void inicializarLauncherGoogleSignIn() {
        // Inicializar el ActivityResultLauncher para manejar la respuesta de Google Sign-In
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        gestionarResultadoSignIn(task);
                    } else {
                        Toast.makeText(requireActivity(), "Error en el inicio de sesión con Google", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void gestionarResultadoSignIn(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Toast.makeText(requireActivity(), "Error al iniciar sesión con Google", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(requireActivity(), "Inicio de sesión con Google exitoso", Toast.LENGTH_SHORT).show();
                        iniciarMainActivity();
                    } else {
                        Toast.makeText(requireActivity(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}