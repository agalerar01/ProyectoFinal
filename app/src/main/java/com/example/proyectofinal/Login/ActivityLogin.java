package com.example.proyectofinal.Login;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.proyectofinal.Main.Controladores.SharedPreferencesHelper;
import com.example.proyectofinal.Main.MainActivity;
import com.example.proyectofinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActivityLogin extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    SharedPreferencesHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NavController navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView)).getNavController();

        ExecutorService executor = Executors.newSingleThreadExecutor();

        mAuth = FirebaseAuth.getInstance();

        helper = new SharedPreferencesHelper(getLayoutInflater().getContext());

        FirebaseUser user = mAuth.getCurrentUser();

        cambiarIdioma();

        executor.execute(() -> {
            try {
                Thread.sleep(5000);
                helper.guardarIdiomaCambiado(true);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void cambiarIdioma() {
        if(helper.devolverIdiomaCambiado()) {
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
        helper.guardarIdiomaCambiado(false);
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
}