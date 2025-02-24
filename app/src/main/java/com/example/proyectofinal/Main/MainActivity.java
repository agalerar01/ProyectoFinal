package com.example.proyectofinal.Main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.proyectofinal.Login.ActivityLogin;
import com.example.proyectofinal.R;
import com.example.proyectofinal.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityMainBinding.inflate(getLayoutInflater())).getRoot());

        setSupportActionBar(binding.toolbar);

        configurarClienteGoogleSignIn();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nuevaNoticiaFragment, R.id.busquedaFragment, R.id.misEventosFragment
        ).setOpenableLayout(binding.drawerLayout).build();

        if(mAuth.getCurrentUser() == null){
            redirectToLogin();
        }

        NavigationView navigationView = findViewById(R.id.nav_view);

        NavController navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment)).getNavController();

        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);

        navigationView.getHeaderView(0).findViewById(R.id.cerrarSesion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        ImageView i= navigationView.getHeaderView(0).findViewById(R.id.fotoPerfilDrawer);

        Glide.with(this)
                .load(R.drawable.ic_launcher_background)
                .circleCrop()
                .into(i);

        getSupportActionBar().setTitle("Proximo Evento");
    }

    private void logoutUser() {
        boolean loginGoogle = isGoogleLogin();
        mAuth.signOut();
        Toast.makeText(MainActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

        if (loginGoogle) { // Si ha iniciado sesión con Google, cerramos sesión en el cliente de Google
            googleSignInClient.signOut().addOnCompleteListener(this, task -> {
                Toast.makeText(MainActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                redirectToLogin();
            });
        } else {
            Toast.makeText(MainActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            redirectToLogin();
        }

    }

    private void redirectToLogin() {
        Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void configurarClienteGoogleSignIn() {
        // Configurar Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Usa tu Web client ID
                .requestEmail()
                .build();

        // Inicializar Google Sign-In a partir de la configuración previa
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    // Determina si el usuario ha iniciado sesión con Google
    private boolean isGoogleLogin() {
        FirebaseUser user = mAuth.getCurrentUser();
        for (UserInfo profile : user.getProviderData()) {
            if (profile.getProviderId().equals("google.com")) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAuth.getCurrentUser() == null){
            redirectToLogin();
        }
    }
}