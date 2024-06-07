package com.proyecto.cashcarp.pantallas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.cashcarp.R;
import com.proyecto.cashcarp.clases.TipoIngreso;

import yuku.ambilwarna.AmbilWarnaDialog;


public class CreateTypeScreen extends AppCompatActivity {

    Button colorPickerButton, createTypeButton;
    EditText typeNameText;
    LinearLayout ll;

    private int typecolor;

    private boolean colorSelected = false,isIngreso = false;

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


                guardarObjeto(name, typecolor,isIngreso);
            }
        });
    }

    private void guardarObjeto(String name, int typecolor, boolean isIngreso) {


        if (isIngreso){
            TipoIngreso ti = new TipoIngreso(name, String.valueOf(typecolor));

            db.collection("usuario").document(userId).collection("tipoIngreso").add(ti).onSu
        }
    }

    public void openColorPicker() {
        AmbilWarnaDialog awd = new AmbilWarnaDialog(this, ContextCompat.getColor(CreateTypeScreen.this, R.color.pastel_lavender), new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                typecolor = color;
                colorSelected = true;
            }
        });
        awd.show();
    }
}
