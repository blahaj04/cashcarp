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

import java.util.List;

public class MyGastoAdapter extends RecyclerView.Adapter<MyGastoViewHolder> {

    Context context;
    List<Gasto> gastoLista;

    SharedPreferences sharedPreferences;
    String userId;
    FirebaseFirestore db;

    LinearLayout ll;

    public MyGastoAdapter(Context c, List<Gasto> gastoLista) {
        this.context = c;
        this.gastoLista = gastoLista;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public MyGastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyGastoViewHolder(LayoutInflater.from(context).inflate(R.layout.gasto_view, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyGastoViewHolder holder, int position) {
        sharedPreferences = context.getSharedPreferences("com.proyecto.cashcarp", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);


        Gasto currentGasto = gastoLista.get(position);

        holder.descValue.setText(currentGasto.getDescripcion());
        holder.cantValue.setText(String.valueOf(currentGasto.getCantidad()));
        holder.fechaValue.setText(currentGasto.getTs().toDate().toString());

        ll = holder.itemView.findViewById(R.id.card_gasto);

        int colorInt = getColorInt(currentGasto.getColor());
        ll.setBackgroundColor(colorInt);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Gasto currentGasto = gastoLista.get(adapterPosition);
                    Intent intent = new Intent(context, EditGastoActivity.class);
                    intent.putExtra("descripcionGasto", currentGasto.getDescripcion());
                    intent.putExtra("cantidadGasto", currentGasto.getCantidad());
                    intent.putExtra("tipoId", currentGasto.getTipoId());
                    intent.putExtra("id", currentGasto.getId());
                    context.startActivity(intent);
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {

                    String gastoId = gastoLista.get(adapterPosition).getId();

                    db.collection("usuario").document(userId).collection("tipoGasto").document(currentGasto.getTipoId()).collection("gastos").document(gastoId).delete().addOnSuccessListener(aVoid -> {

                        Toast.makeText(context, "Gasto borrado", Toast.LENGTH_SHORT).show();
                        gastoLista.remove(adapterPosition);
                        notifyItemRemoved(adapterPosition);
                        notifyItemRangeChanged(adapterPosition, gastoLista.size());
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
        return gastoLista.size();
    }
}
