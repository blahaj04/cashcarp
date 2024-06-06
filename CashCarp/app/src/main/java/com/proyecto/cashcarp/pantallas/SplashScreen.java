package com.proyecto.cashcarp.pantallas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.proyecto.cashcarp.R;

public class SplashScreen extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private boolean tutorialHecho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        sharedPreferences = getSharedPreferences("com.proyecto.cashcarp", Context.MODE_PRIVATE);
        tutorialHecho = sharedPreferences.getBoolean("tutorialHecho", false);

        /*
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("tutorialHecho", false);
        editor.apply();
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if(tutorialHecho){
                    i = new Intent(SplashScreen.this, IniciarSesionScreen.class);
                }else{
                    i = new Intent(SplashScreen.this, TutorialScreen.class);
                }
                startActivity(i);
                finish();

            }
        }, 3000);
    }
}
