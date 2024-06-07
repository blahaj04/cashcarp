package com.proyecto.cashcarp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.cashcarp.R;
import com.proyecto.cashcarp.clases.Gasto;
import com.proyecto.cashcarp.clases.Ingreso;
import com.proyecto.cashcarp.clases.TipoGasto;
import com.proyecto.cashcarp.clases.TipoIngreso;
import com.proyecto.cashcarp.pantallas.CreateTypeScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrearFragment extends Fragment {

    private EditText editTextDescripcion, editTextCantidad;
    private SwitchCompat switchGastoIngreso;
    private Spinner spinnerTipo;
    private Button botonCrearTipo, botonGuardar;

    private FirebaseFirestore db;

    private SharedPreferences sharedPreferences;
    private String userId;

    private boolean isIngreso = false;

    private List<TipoGasto> tipoGastos = new ArrayList<>();
    private List<TipoIngreso> tipoIngresos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear, container, false);

        sharedPreferences = getContext().getSharedPreferences("com.proyecto.cashcarp", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);

        editTextDescripcion = view.findViewById(R.id.editTextDescripcion);
        editTextCantidad = view.findViewById(R.id.editTextCantidad);
        switchGastoIngreso = view.findViewById(R.id.switchGastoIngreso);
        spinnerTipo = view.findViewById(R.id.spinnerTipo);
        botonCrearTipo = view.findViewById(R.id.botonCrearTipo);
        botonGuardar = view.findViewById(R.id.botonGuardarCrear);

        db = FirebaseFirestore.getInstance();

        cambiarColorHeader(view);
        cargarTipos();

        mostrarTipos(false);

        switchGastoIngreso.setOnCheckedChangeListener((buttonView, isChecked) -> {

            isIngreso = isChecked;
            mostrarTipos(isChecked);
        });

        botonCrearTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), CreateTypeScreen.class);
                startActivity(i);

            }
        });

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descripcion = editTextDescripcion.getText().toString().trim();
                String cantidad = editTextCantidad.getText().toString().trim();
                String tipoSeleccionado = spinnerTipo.getSelectedItem().toString();

                if (descripcion.isEmpty() || cantidad.isEmpty()) {

                    Toast.makeText(getContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }


                try {
                    double cantidadValue = Double.parseDouble(cantidad);
                    if (cantidadValue <= 0) {
                        Toast.makeText(getContext(), "La cantidad debe ser mayor que cero", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {

                    Toast.makeText(getContext(), "Formato de cantidad inválido", Toast.LENGTH_SHORT).show();
                    return;
                }

                String tipoId = obtenerId(tipoSeleccionado, isIngreso);
                guardarObjeto(descripcion, cantidad, tipoId);
            }
        });


        return view;
    }

    private String obtenerId(String tipoSeleccionado, boolean isIngreso) {
        if (isIngreso) {
            for (TipoIngreso ti : tipoIngresos) {
                if (ti.getNombreTipoIngreso().equals(tipoSeleccionado)) {
                    return ti.getId();
                }
            }
        } else {
            for (TipoGasto tg : tipoGastos
            ) {
                if (tg.getNombreTipoGasto().equals(tipoSeleccionado)) {
                    return tg.getId();
                }
            }
        }
        return null;
    }

    private void guardarObjeto(String descripcion, String cantidad, String idTipo) {

        if (isIngreso) {
            Ingreso i = new Ingreso(descripcion, Double.parseDouble(cantidad), Timestamp.now());

            db.collection("usuario").document(userId).collection("tipoIngreso").document(idTipo).collection("ingresos").add(i).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getContext(), "Ingreso guardado correctamente", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error al guardar el ingreso: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Gasto g = new Gasto(descripcion, Double.parseDouble(cantidad), Timestamp.now());

            db.collection("usuario").document(userId).collection("tipoGasto").document(idTipo).collection("gastos").add(g).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getContext(), "Gasto guardado correctamente", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error al guardar el gasto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void cargarTipos() {
        db.collection("usuario").document(userId).collection("tipoGasto").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String tipoId = document.getId();
                    String colorTipo = document.getString("color");
                    String nombreTipo = document.getString("nombreTipoGasto");
                    TipoGasto tg = new TipoGasto(colorTipo, nombreTipo);
                    tg.setId(tipoId);
                    tipoGastos.add(tg);
                }

                mostrarTipos(false);
            } else {
                Toast.makeText(getContext(), "Error al cargar tipos: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
        db.collection("usuario").document(userId).collection("tipoIngreso").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String tipoId = document.getId();
                    String colorTipo = document.getString("color");
                    String nombreTipo = document.getString("nombreTipoIngreso");
                    TipoIngreso ti = new TipoIngreso(colorTipo, nombreTipo);
                    ti.setId(tipoId);
                    tipoIngresos.add(ti);
                }
            } else {
                Toast.makeText(getContext(), "Error al cargar tipos: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarTipos(boolean isIngreso) {
        List<String> listaNombres = obtenerNombres(isIngreso);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listaNombres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerTipo.setAdapter(adapter);
    }


    private List<String> obtenerNombres(boolean isIngreso) {
        List<String> listaNombres = new ArrayList<>();

        if (isIngreso) {
            for (TipoIngreso tipo : tipoIngresos) {
                if (tipo != null && tipo.getNombreTipoIngreso() != null) {
                    listaNombres.add(tipo.getNombreTipoIngreso());
                }
            }
        } else {
            for (TipoGasto tipo : tipoGastos) {
                if (tipo != null && tipo.getNombreTipoGasto() != null) {
                    listaNombres.add(tipo.getNombreTipoGasto());
                }
            }
        }

        return listaNombres;
    }

    private void cambiarColorHeader(View view) {
        int[] pastelColors = {R.color.pastel_purple, R.color.pastel_yellow, R.color.pastel_pink, R.color.pastel_violet, R.color.pastel_teal, R.color.pastel_peach, R.color.pastel_lavender, R.color.pastel_mint, R.color.pastel_lilac, R.color.pastel_coral, R.color.pastel_brown, R.color.pastel_grey, R.color.pastel_turquoise, R.color.pastel_magenta, R.color.pastel_cyan, R.color.pastel_banana};

        Random random = new Random();
        int selectedColor = pastelColors[random.nextInt(pastelColors.length)];


        LinearLayout linearLayout = view.findViewById(R.id.crear_header_layout);
        linearLayout.setBackgroundColor(getResources().getColor(selectedColor));
    }
}
