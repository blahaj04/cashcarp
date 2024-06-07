package com.proyecto.cashcarp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.cashcarp.R;

import java.util.Random;

public class AjustesFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    private String userId;
    private SharedPreferences.Editor editor;
    private SwitchCompat switchEnableTutorial;
    private EditText editTextBudget;

    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ajustes, container, false);

        cambiarColorHeader(view);
        db = FirebaseFirestore.getInstance();
        sharedPreferences = requireContext().getSharedPreferences("com.proyecto.cashcarp", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);

        editor = sharedPreferences.edit();

        switchEnableTutorial = view.findViewById(R.id.switchEnableTutorial);
        editTextBudget = view.findViewById(R.id.editTextBudget);
        Button saveBudgetButton = view.findViewById(R.id.btnActualizarBudget);

        // Inicializar SwitchCompat con el valor actual de las SharedPreferences
        boolean tutorialHecho = sharedPreferences.getBoolean("tutorialHecho", false);
        switchEnableTutorial.setChecked(!tutorialHecho);

        // Configurar listener para el switch
        switchEnableTutorial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("tutorialHecho", !isChecked);
            editor.apply();
        });

        // Configurar listener para el botón de actualizar presupuesto
        saveBudgetButton.setOnClickListener(v -> {
            String newBudgetStr = editTextBudget.getText().toString();
            if (!newBudgetStr.isEmpty()) {
                try {
                    db.collection("usuario").document(userId).update("budget", Double.parseDouble(newBudgetStr));
                    Toast.makeText(requireContext(), "Presupuesto actualizado", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Ingrese un número válido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "El campo de presupuesto está vacío", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private void cambiarColorHeader(View view) {
        int[] pastelColors = {R.color.pastel_purple, R.color.pastel_yellow, R.color.pastel_pink, R.color.pastel_violet, R.color.pastel_teal, R.color.pastel_peach, R.color.pastel_lavender, R.color.pastel_mint, R.color.pastel_lilac, R.color.pastel_coral, R.color.pastel_brown, R.color.pastel_grey, R.color.pastel_turquoise, R.color.pastel_magenta, R.color.pastel_cyan, R.color.pastel_banana};

        Random random = new Random();
        int selectedColor = pastelColors[random.nextInt(pastelColors.length)];


        LinearLayout linearLayout = view.findViewById(R.id.ajustes_header_layout);
        linearLayout.setBackgroundColor(getResources().getColor(selectedColor));
    }
}
