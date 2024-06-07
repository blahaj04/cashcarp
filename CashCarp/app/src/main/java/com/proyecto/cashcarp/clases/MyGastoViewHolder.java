package com.proyecto.cashcarp.clases;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.cashcarp.R;

public class MyGastoViewHolder extends RecyclerView.ViewHolder {

    TextView descLabel, descValue, cantLabel, cantValue, fechaLabel, fechaValue;
    Button editButton, deleteButton;

    public MyGastoViewHolder(@NonNull View itemView) {
        super(itemView);

        descLabel = itemView.findViewById(R.id.descriptionLabel);
        descValue = itemView.findViewById(R.id.textViewDescripcion);
        cantLabel = itemView.findViewById(R.id.cantidad_label);
        cantValue = itemView.findViewById(R.id.textViewCantidad);
        fechaLabel = itemView.findViewById(R.id.fecha_label);
        fechaValue = itemView.findViewById(R.id.textViewFecha);
        editButton = itemView.findViewById(R.id.editButton);
        deleteButton = itemView.findViewById(R.id.deleteButton);
    }
}
