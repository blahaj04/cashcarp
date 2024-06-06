package com.proyecto.cashcarp.pantallas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.proyecto.cashcarp.R;

public class IniciarSesionScreen extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private static String userid;
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iniciar_sesion_screen);

        sharedPreferences = getSharedPreferences("com.proyecto.cashcarp", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("userId", null);

        db = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextNumberPassword);

        Button signInButton = findViewById(R.id.buttonCreateAccount);
        Button signUpButton = findViewById(R.id.buttonAlreadyHaveAccount);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSignUpScreen = new Intent(IniciarSesionScreen.this, CreateAccountActivity.class);
                startActivity(goToSignUpScreen);
            }
        });
    }

    private void iniciarSesion() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Correo electrónico no válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Contraseña no puede estar vacía.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("usuario")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean userFound = false;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userFound = true;
                                // Usuario encontrado, guardar ID en SharedPreferences
                                String userId = document.getId();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userId", userId);
                                editor.apply();

                                // Navegar a la siguiente pantalla
                                Intent goToMainScreen = new Intent(IniciarSesionScreen.this, MainScreen.class);
                                startActivity(goToMainScreen);
                                finish();
                            }
                            if (!userFound) {
                                // Usuario no encontrado
                                Toast.makeText(IniciarSesionScreen.this, "Correo electrónico o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(IniciarSesionScreen.this, "Error al buscar el usuario.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

