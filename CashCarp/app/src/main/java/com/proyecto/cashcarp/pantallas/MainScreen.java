package com.proyecto.cashcarp.pantallas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.proyecto.cashcarp.R;
import com.proyecto.cashcarp.fragments.AjustesFragment;
import com.proyecto.cashcarp.fragments.CrearFragment;
import com.proyecto.cashcarp.fragments.GastosFragment;
import com.proyecto.cashcarp.fragments.IngresoFragment;
import com.proyecto.cashcarp.fragments.PrincipalFragment;

import java.util.Random;

public class MainScreen extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private static final int HOME_BUTTON_ID = R.id.home_button_navbar;
    private static final int SPENDS_BUTTON_ID = R.id.spends_button_navbar;
    private static final int ADD_BUTTON_ID = R.id.add_button_navbar;
    private static final int INCOMES_BUTTON_ID = R.id.incomes_button_navbar;
    private static final int SETTINGS_BUTTON_ID = R.id.settings_button_navbar;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_COLOR = "navbar_color";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        cambiarColorNavBar();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case HOME_BUTTON_ID:
                        selectedFragment = new PrincipalFragment();
                        break;
                    case SPENDS_BUTTON_ID:
                        selectedFragment = new GastosFragment();
                        break;
                    case ADD_BUTTON_ID:
                        selectedFragment = new CrearFragment();
                        break;
                    case INCOMES_BUTTON_ID:
                        selectedFragment = new IngresoFragment();
                        break;
                    case SETTINGS_BUTTON_ID:
                        selectedFragment = new AjustesFragment();
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid menu item selected");
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
                return true;
            }
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(HOME_BUTTON_ID); // Load PrincipalFragment by default
        }
    }

    private void cambiarColorNavBar() {
        int[] pastelColors = {
                R.color.pastel_red,
                R.color.pastel_green,
                R.color.pastel_blue,
                R.color.pastel_purple,
                R.color.pastel_yellow,
                R.color.pastel_orange,
                R.color.pastel_pink,
                R.color.pastel_violet,
                R.color.pastel_teal,
                R.color.pastel_peach,
                R.color.pastel_lavender,
                R.color.pastel_mint,
                R.color.pastel_lilac,
                R.color.pastel_coral,
                R.color.pastel_brown,
                R.color.pastel_grey,
                R.color.pastel_turquoise,
                R.color.pastel_magenta,
                R.color.pastel_cyan,
                R.color.pastel_banana
        };

        Random random = new Random();
        int selectedColor = pastelColors[random.nextInt(pastelColors.length)];


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackgroundColor(getResources().getColor(selectedColor));



    }
}
