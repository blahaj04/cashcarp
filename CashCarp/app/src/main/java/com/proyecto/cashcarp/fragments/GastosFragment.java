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
import com.proyecto.cashcarp.clases.MyGastoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GastosFragment extends Fragment {

    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private String userId;
    private MyGastoAdapter adapter;
    private List<Gasto> listaGastos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gastos, container, false);

        cambiarColorHeader(view);
        sharedPreferences = requireContext().getSharedPreferences("com.proyecto.cashcarp", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewGastos);
        listaGastos = new ArrayList<>();
        adapter = new MyGastoAdapter(requireContext(), listaGastos);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        obtenerTodosLosGastos();

        return view;
    }

    private void obtenerTodosLosGastos() {
        db.collection("usuario").document(userId).collection("tipoGasto").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String tipoId = document.getId();
                    String color = document.getString("color");
                    db.collection("usuario").document(userId).collection("tipoGasto").document(tipoId).collection("gastos").get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (DocumentSnapshot gastoDocument : task1.getResult()) {
                                String cantidad = String.valueOf(gastoDocument.getDouble("cantidad"));
                                String descripcion = gastoDocument.getString("descripcion");
                                Timestamp ts = gastoDocument.getTimestamp("ts");
                                String id = gastoDocument.getId();

                                Gasto g = new Gasto(descripcion, Double.parseDouble(cantidad), ts);

                                g.setColor(color);
                                g.setTipoId(tipoId);
                                g.setId(id);
                                listaGastos.add(g);
                            }
                            Gasto.ordenarGastosPorFecha((ArrayList<Gasto>) listaGastos);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(requireContext(), "Error al cargar gastos: " + task1.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(requireContext(), "Error al cargar tipos de gasto: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void cambiarColorHeader(View view) {
        int[] pastelColors = {R.color.pastel_purple, R.color.pastel_yellow, R.color.pastel_pink, R.color.pastel_violet, R.color.pastel_teal, R.color.pastel_peach, R.color.pastel_lavender, R.color.pastel_mint, R.color.pastel_lilac, R.color.pastel_coral, R.color.pastel_brown, R.color.pastel_grey, R.color.pastel_turquoise, R.color.pastel_magenta, R.color.pastel_cyan, R.color.pastel_banana};

        Random random = new Random();
        int selectedColor = pastelColors[random.nextInt(pastelColors.length)];


        LinearLayout linearLayout = view.findViewById(R.id.gastos_header_layout);
        linearLayout.setBackgroundColor(getResources().getColor(selectedColor));
    }
}
