package com.example.proyectofinal.Main;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.proyectofinal.Login.ActivityLogin;
import com.example.proyectofinal.Main.Controladores.SharedPreferencesHelper;
import com.example.proyectofinal.R;
import com.example.proyectofinal.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private GoogleSignInClient googleSignInClient;
    SharedPreferencesHelper helper;
    boolean cambioIdioma = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityMainBinding.inflate(getLayoutInflater())).getRoot());
        helper = new SharedPreferencesHelper(getLayoutInflater().getContext());

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
                .load(R.drawable.no_foto)
                .override(240,240)
                .circleCrop()
                .into(i);

        cambiarTema(navigationView);

        imagenGoogle(navigationView, i);

        TextView e = navigationView.getHeaderView(0).findViewById(R.id.nombreUsu);

        cambiarIdioma();

        getSupportActionBar().setTitle("Proximo Evento");
    }

    private void cambiarIdioma() {
        if(cambioIdioma) {
            switch (helper.devolverIdioma()) {
                case "English":
                    changeLanguage("en");
                    break;
                case "Spanish":
                    changeLanguage("es");
                    break;
                case "French":
                    changeLanguage("fr");
                    break;
            }
        }
        cambioIdioma = false;
    }

    private void cambiarTema(NavigationView navigationView){
        if(helper.devolverTemaOscuro()){
            navigationView.getHeaderView(0).setBackgroundColor(getResources().getColor(R.color.azul_crosscut));
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            getDelegate().applyDayNight();
        }else{
            navigationView.getHeaderView(0).setBackgroundColor(getResources().getColor(R.color.azul_pastel));
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            getDelegate().applyDayNight();
        }
    }

    private void imagenGoogle(NavigationView navigationView, ImageView i){
        if (mAuth.getCurrentUser() != null) {
            if(isGoogleLogin()){
                Glide.with(this)
                        .load(mAuth.getCurrentUser().getPhotoUrl())
                        .override(240,240)
                        .circleCrop()
                        .into(i);
            }

            TextView e = navigationView.getHeaderView(0).findViewById(R.id.correo);

            e.setText(mAuth.getCurrentUser().getEmail());
        }
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

    private void changeLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        recreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAuth.getCurrentUser() == null){
            redirectToLogin();
        }
    }
}