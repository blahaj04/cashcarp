package com.proyecto.cashcarp.pantallas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.proyecto.cashcarp.R;

import java.util.Random;


public class MainScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        cambiarColorNavBar();
    }

    private void cambiarColorNavBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

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
        int randomColor = pastelColors[random.nextInt(pastelColors.length)];


        bottomNavigationView.setBackgroundColor(getResources().getColor(randomColor));
    }
}
