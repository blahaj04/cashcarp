package com.proyecto.cashcarp.pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.cashcarp.R;
import com.proyecto.cashcarp.clases.TipoGasto;
import com.proyecto.cashcarp.clases.TipoIngreso;
import com.proyecto.cashcarp.clases.Usuario;

public class CreateAccountActivity extends AppCompatActivity {


    // Tipos de gasto
    private static final TipoGasto tipoGasto1 = new TipoGasto("#FF6961", "Comida");
    private static final TipoGasto tipoGasto2 = new TipoGasto("#77DD77", "Transporte");

    // Tipos de ingreso
    private static final TipoIngreso tipoIngreso1 = new TipoIngreso("#AEC6CF", "Paga semanal");
    private static final TipoIngreso tipoIngreso2 = new TipoIngreso("#FFB347", "Regalo");
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nicknameEditText;
    private EditText budgetEditText;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_screen);

        db = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextNumberPassword);
        nicknameEditText = findViewById(R.id.editTextText);
        budgetEditText = findViewById(R.id.editTextNumber);

        Button createAccountButton = findViewById(R.id.buttonCreateAccount);
        Button alreadyWithAccount = findViewById(R.id.buttonAlreadyHaveAccount);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCampos();
            }
        });

        alreadyWithAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shared preffs tutorial done true

                Intent goToLoginScreen = new Intent(CreateAccountActivity.this, IniciarSesionScreen.class);
                startActivity(goToLoginScreen);
                finish();
            }
        });
    }

    private void guardarCampos() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String nickname = nicknameEditText.getText().toString().trim();
        String budget = budgetEditText.getText().toString().trim();


        StringBuilder errorMessage = new StringBuilder();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage.append("Correo electrónico no válido.\n");
        }

        if (TextUtils.isEmpty(password)) {
            errorMessage.append("Contraseña no puede estar vacía.\n");
        }

        if (TextUtils.isEmpty(nickname)) {
            errorMessage.append("Apodo no puede estar vacío.\n");
        }

        if (TextUtils.isEmpty(budget) || !isNumeric(budget)) {
            errorMessage.append("Presupuesto no válido.\n");
        }

        if (errorMessage.length() > 0) {
            Toast.makeText(this, errorMessage.toString().trim(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Datos guardados:\nEmail: " + email + "\nContraseña: " + password + "\nApodo: " + nickname + "\nPresupuesto: " + budget, Toast.LENGTH_LONG).show();

            Usuario user = new Usuario(email, password, nickname, Double.parseDouble(budget));
            db.collection("usuario")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference userDocumentReference) {
                            Toast.makeText(CreateAccountActivity.this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();

                            insertarDatosIniciales(userDocumentReference);

                            Intent goToIniciarSesionScreen = new Intent(CreateAccountActivity.this, IniciarSesionScreen.class);
                            startActivity(goToIniciarSesionScreen);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateAccountActivity.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    private void insertarDatosIniciales(DocumentReference userDocRef) {

        userDocRef.collection("tipoGasto").add(tipoGasto1);
        userDocRef.collection("tipoGasto").add(tipoGasto2);
        userDocRef.collection("tipoIngreso").add(tipoIngreso1);
        userDocRef.collection("tipoIngreso").add(tipoIngreso2);

    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
