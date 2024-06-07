package com.proyecto.cashcarp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.proyecto.cashcarp.R;

import com.proyecto.cashcarp.clases.Gasto;
import com.proyecto.cashcarp.clases.Ingreso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PrincipalFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private String userId;

    private Double budget = 0.0;
    private FirebaseFirestore db;

    private Double totalIncomes = 0.0;
    private Double totalSpends = 0.0;

    private List<Gasto> listaGastos = new ArrayList<Gasto>();

    private List<Ingreso> listaIngresos = new ArrayList<Ingreso>();

    TextView usernameTV, spendsTV, incomesTV, balanceTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        usernameTV = view.findViewById(R.id.userNameTextView);
        spendsTV = view.findViewById(R.id.spendsTextView);
        incomesTV = view.findViewById(R.id.incomesTextView);
        balanceTV = view.findViewById(R.id.budgetTextView);

        sharedPreferences = getContext().getSharedPreferences("com.proyecto.cashcarp", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);

        db = FirebaseFirestore.getInstance();

        cambiarColorHeader(view);

        setInfoInHeader();

        calculateBalance();

        setupSpendsChart(listaGastos, view);
        return view;
    }

    private void setInfoInHeader() {
        nameQuery();
        queryAndCalculateBalance();

    }

    private void queryAndCalculateBalance() {
        TaskCompletionSource<Void> incomesTask = new TaskCompletionSource<>();
        TaskCompletionSource<Void> spendsTask = new TaskCompletionSource<>();

        queryAllIncomes(incomesTask);
        queryAllSpends(spendsTask);

        Tasks.whenAll(incomesTask.getTask(), spendsTask.getTask()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                calculateBalance();
            } else {
                Toast.makeText(requireContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateBalance() {
        Double balance = budget + totalIncomes - totalSpends;
        balanceTV.setText(String.valueOf(balance));
    }

    private void queryAllIncomes(TaskCompletionSource<Void> taskCompletionSource) {

        db.collection("usuario").document(userId).collection("tipoIngreso").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int numDocuments = task.getResult().size();
                int[] documentsProcessed = {0}; // Variable final para llevar el registro de los documentos procesados
                for (DocumentSnapshot document : task.getResult()) {
                    String tipoId = document.getId();
                    String color = document.getString("color");
                    String nombreTipo = document.getString("nombreTipoIngreso");
                    db.collection("usuario").document(userId).collection("tipoIngreso").document(tipoId).collection("ingresos").get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (DocumentSnapshot ingresoDocument : task1.getResult()) {
                                String cantidad = String.valueOf(ingresoDocument.getDouble("cantidad"));
                                String descripcion = ingresoDocument.getString("descripcion");
                                Timestamp ts = ingresoDocument.getTimestamp("ts");
                                String id = ingresoDocument.getId();

                                Ingreso i = new Ingreso(descripcion, Double.parseDouble(cantidad), ts);
                                i.setColor(color);
                                i.setTipoId(tipoId);
                                i.setId(id);
                                i.setTipoNombre(nombreTipo);
                                listaIngresos.add(i);
                            }

                            documentsProcessed[0]++;
                            if (documentsProcessed[0] == numDocuments) {
                                totalIncomes = 0.0;
                                for (Ingreso i : listaIngresos) {
                                    totalIncomes += i.getCantidad();
                                }
                                incomesTV.setText(String.valueOf(totalIncomes));
                                taskCompletionSource.setResult(null);
                            }
                        } else {
                            taskCompletionSource.setException(task1.getException());
                            Toast.makeText(requireContext(), "Error al cargar Ingresos: " + task1.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                taskCompletionSource.setException(task.getException());
                Toast.makeText(requireContext(), "Error al cargar tipos de ingreso: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void queryAllSpends(TaskCompletionSource<Void> taskCompletionSource) {
        db.collection("usuario").document(userId).collection("tipoGasto").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int numDocuments = task.getResult().size();
                int[] documentsProcessed = {0}; // Variable final para llevar el registro de los documentos procesados
                for (DocumentSnapshot document : task.getResult()) {
                    String tipoId = document.getId();
                    String color = document.getString("color");
                    String tipoNombre = document.getString("nombreTipoGasto");
                    db.collection("usuario").document(userId).collection("tipoGasto").document(tipoId).collection("gastos").get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (DocumentSnapshot gastoDocument : task1.getResult()) {
                                String cantidad = String.valueOf(gastoDocument.getDouble("cantidad"));
                                String descripcion = gastoDocument.getString("descripcion");
                                Timestamp ts = gastoDocument.getTimestamp("ts");
                                String id = gastoDocument.getId();

                                Gasto g = new Gasto(descripcion, Double.parseDouble(cantidad), ts);
                                g.setTipoNombre(tipoNombre);
                                g.setColor(color);
                                g.setTipoId(tipoId);
                                listaGastos.add(g);
                            }

                            documentsProcessed[0]++;
                            if (documentsProcessed[0] == numDocuments) {
                                totalSpends = 0.0;
                                for (Gasto g : listaGastos) {
                                    totalSpends += g.getCantidad();
                                }
                                spendsTV.setText(String.valueOf(totalSpends));
                                taskCompletionSource.setResult(null);
                            }
                        } else {
                            taskCompletionSource.setException(task1.getException());
                            Toast.makeText(requireContext(), "Error al cargar gastos: " + task1.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                taskCompletionSource.setException(task.getException());
                Toast.makeText(requireContext(), "Error al cargar tipos de gasto: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void nameQuery() {
        db.collection("usuario").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String nickname = documentSnapshot.getString("nickname");
                        budget = documentSnapshot.getDouble("budget");
                        usernameTV.setText(nickname);
                        balanceTV.setText(String.valueOf(budget));
                    }
                });
            }
        });
    }

    private void setupSpendsChart(List<Gasto> spends, View view) {
        if (view != null) {
            BarChart barChart = view.findViewById(R.id.barChart);

            if (barChart != null) {
                ArrayList<BarEntry> entries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<>();
                ArrayList<Integer> colors = new ArrayList<>();

                int i = 0;
                for (Gasto gasto : spends) {
                    entries.add(new BarEntry(i, Float.parseFloat(String.valueOf(gasto.getCantidad()))));
                    labels.add(gasto.getTipoNombre());
                    int color = Color.parseColor(gasto.getColor());
                    colors.add(color);
                    i++;
                }

                BarDataSet dataSet = new BarDataSet(entries, "Gastos");
                dataSet.setColors(colors);

                BarData data = new BarData(dataSet);
                barChart.setData(data);
                barChart.setFitBars(true);
                barChart.invalidate();
            } else {

                Log.e("PrincipalFragment", "BarChart is null. Unable to set data.");
            }
        } else {
            Log.e("PrincipalFragment", "Fragment view is null.");
        }
    }

    private void cambiarColorHeader(View view) {
        int[] pastelColors = {R.color.pastel_purple, R.color.pastel_yellow, R.color.pastel_pink, R.color.pastel_violet, R.color.pastel_teal, R.color.pastel_peach, R.color.pastel_lavender, R.color.pastel_mint, R.color.pastel_lilac, R.color.pastel_coral, R.color.pastel_brown, R.color.pastel_grey, R.color.pastel_turquoise, R.color.pastel_magenta, R.color.pastel_cyan, R.color.pastel_banana};

        Random random = new Random();
        int selectedColor = pastelColors[random.nextInt(pastelColors.length)];

        LinearLayout linearLayout = view.findViewById(R.id.principal_header_layout);
        linearLayout.setBackgroundColor(getResources().getColor(selectedColor));
    }
}
