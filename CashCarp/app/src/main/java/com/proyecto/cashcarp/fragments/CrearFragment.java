package com.proyecto.cashcarp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.cashcarp.R;
import com.proyecto.cashcarp.clases.TipoGasto;
import com.proyecto.cashcarp.clases.TipoIngreso;

import java.util.ArrayList;
import java.util.List;

public class CrearFragment extends Fragment {

    private SwitchCompat switchGastoIngreso;
    private Spinner spinnerTipo;
    private Button botonCrearTipo;

    private FirebaseFirestore db;

    private SharedPreferences sharedPreferences;
    private String userId;

    private List<TipoGasto> tipoGastos = new ArrayList<>();
    private List<TipoIngreso> tipoIngresos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear, container, false);

        sharedPreferences = getContext().getSharedPreferences("com.proyecto.cashcarp", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);

        switchGastoIngreso = view.findViewById(R.id.switchGastoIngreso);
        spinnerTipo = view.findViewById(R.id.spinnerTipo);
        botonCrearTipo = view.findViewById(R.id.botonCrearTipo);

        db = FirebaseFirestore.getInstance();


        switchGastoIngreso.setOnCheckedChangeListener((buttonView, isChecked) -> {

            mostrarTipos(isChecked);
        });

        botonCrearTipo.setOnClickListener(v -> {
            // Aquí puedes manejar el clic del botón para crear un nuevo tipo
            Toast.makeText(getContext(), "Botón crear tipo clickeado", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void cargarTipos() {


        db.collection("usuario").document(userId)
                .collection("tipoGasto")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String tipoId = document.getId();
                            String colorTipo = document.getString("color");
                            String nombreTipo = document.getString("nombreTipoGasto");
                            TipoGasto tg = new TipoGasto(colorTipo, nombreTipo);
                            tg.setId(tipoId);
                            tipoGastos.add(tg);
                        }
                    } else {
                        Toast.makeText(getContext(), "Error al cargar tipos: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
        db.collection("usuario").document(userId)
                .collection("tipoIngreso")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String tipoId = document.getId();
                            String colorTipo = document.getString("color");
                            String nombreTipo = document.getString("nombreTipoGasto");
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
        List<String> listaNombres = obtenerNombres(isIngreso); // Obtener la lista de tipos según sea un ingreso o un gasto


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tiposList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerTipo.setAdapter(adapter);
    }


    private List<String> obtenerNombres(boolean isIngreso) {
        List<String> listaNombres = new ArrayList<String>();
        if (isIngreso) {
            for (TipoIngreso tipo : tipoIngresos) {
                listaNombres.add(tipo.getNombreTipoIngreso());
            }
        } else {
            for (TipoGasto tipo : tipoGastos) {
                listaNombres.add(tipo.getNombreTipoGasto());
            }

        }
        return listaNombres;
    }
}
