package com.proyecto.cashcarp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

    public static final int MAXIMO_ITEMS_SCROLL = 10;
    private LinearLayout transactionsContainer;
    private TextView noTransactionsMessage;
    private SharedPreferences sharedPreferences;
    private String userId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        transactionsContainer = view.findViewById(R.id.transactions_container);
        noTransactionsMessage = view.findViewById(R.id.no_transactions_message);


        sharedPreferences = getContext().getSharedPreferences("com.proyecto.cashcarp", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);

        Log.d("UserID", "User ID: " + userId);

        cambiarColorHeader(view);


        return view;
    }


    private void cambiarColorHeader(View view) {
        int[] pastelColors = {R.color.pastel_purple, R.color.pastel_yellow, R.color.pastel_pink, R.color.pastel_violet, R.color.pastel_teal, R.color.pastel_peach, R.color.pastel_lavender, R.color.pastel_mint, R.color.pastel_lilac, R.color.pastel_coral, R.color.pastel_brown, R.color.pastel_grey, R.color.pastel_turquoise, R.color.pastel_magenta, R.color.pastel_cyan, R.color.pastel_banana};

        Random random = new Random();
        int selectedColor = pastelColors[random.nextInt(pastelColors.length)];


        LinearLayout linearLayout = view.findViewById(R.id.principal_header_layout);
        linearLayout.setBackgroundColor(getResources().getColor(selectedColor));
    }


    private void addTransactionView(Object transaction) {
        View transactionView = LayoutInflater.from(getContext()).inflate(R.layout.item_gasto_ingreso_scroll, transactionsContainer, false);

        TextView descriptionTextView = transactionView.findViewById(R.id.transaction_description);
        TextView amountTextView = transactionView.findViewById(R.id.transaction_amount);

        if (transaction instanceof Gasto) {
            Gasto gasto = (Gasto) transaction;
            descriptionTextView.setText(gasto.getDescripcion());
            amountTextView.setText(String.format("-$%.2f", gasto.getCantidad()));
            amountTextView.setTextColor(getResources().getColor(R.color.pastel_red));
        } else if (transaction instanceof Ingreso) {
            Ingreso ingreso = (Ingreso) transaction;
            descriptionTextView.setText(ingreso.getDescripcion());
            amountTextView.setText(String.format("+$%.2f", ingreso.getCantidad()));
            amountTextView.setTextColor(getResources().getColor(R.color.pastel_green));
        }

        transactionsContainer.addView(transactionView);
    }


}