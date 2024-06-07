package com.proyecto.cashcarp.pantallas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.cashcarp.R;
import com.proyecto.cashcarp.clases.TipoGasto;
import com.proyecto.cashcarp.clases.TipoIngreso;

import java.util.Random;

import yuku.ambilwarna.AmbilWarnaDialog;


public class CreateTypeScreen extends AppCompatActivity {

    Button colorPickerButton, createTypeButton;
    EditText typeNameText;
    LinearLayout ll;

    private String typecolor;

    private boolean colorSelected = false, isIngreso = false;

    private SwitchCompat switchGastoIngreso;

    private SharedPreferences sharedPreferences;
    private String userId;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_type_screen);

        sharedPreferences = getSharedPreferences("com.proyecto.cashcarp", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);

        switchGastoIngreso = findViewById(R.id.switchTipo);
        typeNameText = findViewById(R.id.editTextTypeName);
        colorPickerButton = findViewById(R.id.colorPickerButton);
        createTypeButton = findViewById(R.id.botonGuardarTipo);
        ll = findViewById(R.id.create_type_layout);

cambiarColorHeader();
        db = FirebaseFirestore.getInstance();
        switchGastoIngreso.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isIngreso = isChecked;
        });
        colorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });
        createTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = typeNameText.getText().toString().trim();


                if (name.isEmpty() || !colorSelected) {
                    Toast.makeText(CreateTypeScreen.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }


                guardarObjeto(name, typecolor, isIngreso);
            }
        });
    }

    private void guardarObjeto(String name, String typecolor, boolean isIngreso) {


        if (isIngreso) {
            TipoIngreso ti = new TipoIngreso(typecolor, name );

            db.collection("usuario").document(userId).collection("tipoIngreso").add(ti).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {

                    Toast.makeText(CreateTypeScreen.this, "Tipo de ingreso guardado con exito ", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CreateTypeScreen.this, MainScreen.class);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateTypeScreen.this, "Error al guardar el tipo de ingreso: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            TipoGasto tg = new TipoGasto(typecolor, name);

            db.collection("usuario").document(userId).collection("tipoGasto").add(tg).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {

                    Toast.makeText(CreateTypeScreen.this, "Tipo de gasto guardado con exito ", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CreateTypeScreen.this, MainScreen.class);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateTypeScreen.this, "Error al guardar el tipo de gasto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void openColorPicker() {
        AmbilWarnaDialog awd = new AmbilWarnaDialog(this, ContextCompat.getColor(CreateTypeScreen.this, R.color.pastel_lavender), new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                typecolor = convertirColorAHexadecimal(color);
                colorSelected = true;
            }
        });
        awd.show();
    }
    public String convertirColorAHexadecimal(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }
    private void cambiarColorHeader() {
        int[] pastelColors = {R.color.pastel_purple, R.color.pastel_yellow, R.color.pastel_pink, R.color.pastel_violet, R.color.pastel_teal, R.color.pastel_peach, R.color.pastel_lavender, R.color.pastel_mint, R.color.pastel_lilac, R.color.pastel_coral, R.color.pastel_brown, R.color.pastel_grey, R.color.pastel_turquoise, R.color.pastel_magenta, R.color.pastel_cyan, R.color.pastel_banana};

        Random random = new Random();
        int selectedColor = pastelColors[random.nextInt(pastelColors.length)];


        LinearLayout linearLayout = findViewById(R.id.crear_type_header_layout);
        linearLayout.setBackgroundColor(getResources().getColor(selectedColor));
    }
}
