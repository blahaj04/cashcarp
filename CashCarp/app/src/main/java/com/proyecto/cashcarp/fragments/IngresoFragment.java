package com.proyecto.cashcarp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.cashcarp.R;
import com.proyecto.cashcarp.clases.Gasto;
import com.proyecto.cashcarp.clases.Ingreso;
import com.proyecto.cashcarp.clases.MyGastoAdapter;
import com.proyecto.cashcarp.clases.MyIngresoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IngresoFragment extends Fragment {

    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private String userId;
    private MyIngresoAdapter adapter;
    private List<Ingreso> listaIngresos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingreso, container, false);
        cambiarColorHeader(view);
        sharedPreferences = requireContext().getSharedPreferences("com.proyecto.cashcarp", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewIngresos);
        listaIngresos = new ArrayList<>();
        adapter = new MyIngresoAdapter(requireContext(), listaIngresos);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        obtenerTodosLosGastos();

        return view;
    }

    private void obtenerTodosLosGastos() {
        db.collection("usuario").document(userId).collection("tipoIngreso").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String tipoId = document.getId();
                    String color = document.getString("color");
                    db.collection("usuario").document(userId).collection("tipoIngreso").document(tipoId).collection("ingresos").get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (DocumentSnapshot gastoDocument : task1.getResult()) {
                                String cantidad = String.valueOf(gastoDocument.getDouble("cantidad"));
                                String descripcion = gastoDocument.getString("descripcion");
                                Timestamp ts = gastoDocument.getTimestamp("ts");
                                String id = gastoDocument.getId();

                                Ingreso i = new Ingreso(descripcion, Double.parseDouble(cantidad), ts);

                                i.setColor(color);
                                i.setTipoId(tipoId);
                                i.setId(id);
                                listaIngresos.add(i);
                            }
                            Ingreso.ordenarIngresosPorFecha((ArrayList<Ingreso>) listaIngresos);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(requireContext(), "Error al cargar Ingresos: " + task1.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(requireContext(), "Error al cargar tipos de ingreso: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cambiarColorHeader(View view) {
        int[] pastelColors = {R.color.pastel_purple, R.color.pastel_yellow, R.color.pastel_pink, R.color.pastel_violet, R.color.pastel_teal, R.color.pastel_peach, R.color.pastel_lavender, R.color.pastel_mint, R.color.pastel_lilac, R.color.pastel_coral, R.color.pastel_brown, R.color.pastel_grey, R.color.pastel_turquoise, R.color.pastel_magenta, R.color.pastel_cyan, R.color.pastel_banana};

        Random random = new Random();
        int selectedColor = pastelColors[random.nextInt(pastelColors.length)];


        LinearLayout linearLayout = view.findViewById(R.id.ingresos_type_header_layout);
        linearLayout.setBackgroundColor(getResources().getColor(selectedColor));
    }
}
