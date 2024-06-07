package com.proyecto.cashcarp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.cashcarp.R;
import com.proyecto.cashcarp.clases.Gasto;
import com.proyecto.cashcarp.clases.MyGastoAdapter;
import com.proyecto.cashcarp.clases.TipoGasto;

import java.util.ArrayList;
import java.util.List;


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

        sharedPreferences = getContext().getSharedPreferences("com.proyecto.cashcarp", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewGastos);
        listaGastos = new ArrayList<>();
        adapter = new MyGastoAdapter(getActivity().getApplicationContext(), listaGastos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        obtenerTodosLosGastos();

        return view;
    }

    private void obtenerTodosLosGastos() {
        db.collection("usuario").document(userId).collection("tipoGasto").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String tipoId = document.getId();
                    db.collection("usuario").document(userId).collection("tipoGasto").document(tipoId).collection("gastos").get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (DocumentSnapshot gastoDocument : task1.getResult()) {
                                String cantidad = String.valueOf(gastoDocument.getDouble("cantidad"));
                                String descripcion = gastoDocument.getString("descripcion");
                                Timestamp ts = gastoDocument.getTimestamp("ts");

                                Gasto g = new Gasto(descripcion, Double.parseDouble(cantidad), ts);
                                listaGastos.add(g);
                            }
                            Gasto.ordenarGastosPorFecha((ArrayList<Gasto>) listaGastos);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Error al cargar gastos: " + task1.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(getContext(), "Error al cargar tipos de gasto: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}