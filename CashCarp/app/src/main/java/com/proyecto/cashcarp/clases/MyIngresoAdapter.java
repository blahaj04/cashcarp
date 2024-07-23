package com.proyecto.cashcarp.clases;

import static androidx.core.content.ContentProviderCompat.requireContext;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.cashcarp.R;
import com.proyecto.cashcarp.pantallas.EditGastoActivity;
import com.proyecto.cashcarp.pantallas.EditIngresoActivity;

import java.util.List;

public class MyIngresoAdapter extends RecyclerView.Adapter<MyIngresoViewHolder> {

    Context context;
    List<Ingreso> ingresoLista;

    SharedPreferences sharedPreferences;
    String userId;
    FirebaseFirestore db;

    LinearLayout ll;


    public MyIngresoAdapter(Context c, List<Ingreso> ingresoLista){
        this.context = c;
        this.ingresoLista = ingresoLista;
        this.db = FirebaseFirestore.getInstance();}

    @NonNull
    @Override
    public MyIngresoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyIngresoViewHolder(LayoutInflater.from(context).inflate(R.layout.ingreso_view, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyIngresoViewHolder holder, int position) {
        sharedPreferences = context.getSharedPreferences("com.proyecto.cashcarp", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);


        Ingreso currentIngreso = ingresoLista.get(position);

        holder.descValue.setText(currentIngreso.getDescripcion());
        holder.cantValue.setText(String.valueOf(currentIngreso.getCantidad()));
        holder.fechaValue.setText(currentIngreso.getTs().toDate().toString());

        ll = holder.itemView.findViewById(R.id.card_ingreso);

        int colorInt = getColorInt(currentIngreso.getColor());
        ll.setBackgroundColor(colorInt);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Ingreso currentIngreso = ingresoLista.get(adapterPosition);
                    Intent intent = new Intent(context, EditIngresoActivity.class);
                    intent.putExtra("descripcionIngreso", currentIngreso.getDescripcion());
                    intent.putExtra("cantidadIngreso", currentIngreso.getCantidad());
                    intent.putExtra("tipoId", currentIngreso.getTipoId());
                    intent.putExtra("id", currentIngreso.getId());
                    context.startActivity(intent);
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {

                    String ingresoId = ingresoLista.get(adapterPosition).getId();

                    db.collection("usuario").document(userId).collection("tipoIngreso").document(currentIngreso.getTipoId()).collection("gastos").document(ingresoId).delete().addOnSuccessListener(aVoid -> {

                        Toast.makeText(context, "Ingreso borrado", Toast.LENGTH_SHORT).show();

                        ingresoLista.remove(adapterPosition);
                        notifyItemRemoved(adapterPosition);
                        notifyItemRangeChanged(adapterPosition, ingresoLista.size());
                    }).addOnFailureListener(e -> {

                        Toast.makeText(context, "Error al borrar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

    }


    private int getColorInt(String hexColor) {
        try {
            return Color.parseColor(hexColor);
        } catch (IllegalArgumentException e) {
            // Si hay un error al convertir el color, devuelve un color predeterminado
            return Color.WHITE; // Puedes elegir cualquier otro color predeterminado aquí
        }
    }

    @Override
    public int getItemCount() {
        return ingresoLista.size();
    }
}
