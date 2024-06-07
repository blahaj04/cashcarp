package com.proyecto.cashcarp.pantallas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.cashcarp.R;
import com.proyecto.cashcarp.clases.Gasto;
import com.proyecto.cashcarp.clases.MyGastoViewHolder;

public class EditGastoActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String userId;
    private Button saveButton;
    private FirebaseFirestore db;
    private String descripcion, tipoId, id;
    private double cantidad;
    private Timestamp ts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gasto);

        sharedPreferences = getApplicationContext().getSharedPreferences("com.proyecto.cashcarp", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        descripcion = intent.getStringExtra("descripcionGasto");
        tipoId = intent.getStringExtra("tipoId");
        id = intent.getStringExtra("id");

        cantidad = intent.getDoubleExtra("cantidadGasto", 0.0);

        EditText descripcionEditText = findViewById(R.id.descripcionEditText);
        EditText cantidadEditText = findViewById(R.id.cantidadEditText);
        descripcionEditText.setText(descripcion);
        cantidadEditText.setText(String.valueOf(cantidad));

        // Inicializar el botón saveButton
        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            String newDescripcion = descripcionEditText.getText().toString();
            double newCantidad = Double.parseDouble(cantidadEditText.getText().toString());

            db.collection("usuario").document(userId).collection("tipoGasto").document(tipoId).collection("gastos").document(id).update("descripcion", newDescripcion, "cantidad", newCantidad).addOnSuccessListener(aVoid -> {
                Toast.makeText(EditGastoActivity.this, "Gasto actualizado", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EditGastoActivity.this, MainScreen.class);
                startActivity(i);
                finish();
            }).addOnFailureListener(e -> Toast.makeText(EditGastoActivity.this, "Error al actualizar gasto", Toast.LENGTH_SHORT).show());
        });
    }


}
