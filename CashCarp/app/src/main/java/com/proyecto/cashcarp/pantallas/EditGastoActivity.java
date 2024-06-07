package com.proyecto.cashcarp.pantallas;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.cashcarp.R;

public class EditGastoActivity extends AppCompatActivity {

    private EditText descripcionEditText, cantidadEditText;
    private Button saveButton;
    private FirebaseFirestore db;
    private String descripcion;
    private double cantidad;
    private Timestamp ts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gasto);

        db = FirebaseFirestore.getInstance();

        descripcionEditText = findViewById(R.id.descripcionEditText);
        cantidadEditText = findViewById(R.id.cantidadEditText);
        saveButton = findViewById(R.id.saveButton);

        descripcion = getIntent().getStringExtra("descripcion");
        cantidad = getIntent().getDoubleExtra("cantidad", 0);


        descripcionEditText.setText(descripcion);
        cantidadEditText.setText(String.valueOf(cantidad));

        saveButton.setOnClickListener(v -> {
            String newDescripcion = descripcionEditText.getText().toString();
            double newCantidad = Double.parseDouble(cantidadEditText.getText().toString());

            // Actualizar los datos en Firebase
            // Asume que tienes una referencia de documento a actualizar
            db.collection("usuario")
                    .document("userId") // Debes obtener el userId correcto
                    .collection("tipoGasto")
                    .document("tipoId") // Debes obtener el tipoId correcto
                    .collection("gastos")
                    .document("gastoId") // Debes obtener el gastoId correcto
                    .update("descripcion", newDescripcion, "cantidad", newCantidad)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(EditGastoActivity.this, "Gasto actualizado", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(EditGastoActivity.this, "Error al actualizar gasto", Toast.LENGTH_SHORT).show());
        });
    }
}
