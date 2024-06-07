package com.proyecto.cashcarp.clases;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.cashcarp.R;

public class MyGastoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView descLabel, descValue, cantLabel, cantValue, fechaLabel, fechaValue;
    OnGastoListener onGastoListener;
    public MyGastoViewHolder(@NonNull View itemView, OnGastoListener onGastoListener) {
        super(itemView);

        descLabel = itemView.findViewById(R.id.descriptionLabel);
        descValue = itemView.findViewById(R.id.textViewDescripcion);
        cantLabel = itemView.findViewById(R.id.cantidad_label);
        cantValue = itemView.findViewById(R.id.textViewCantidad);
        fechaLabel = itemView.findViewById(R.id.fecha_label);
        fechaValue = itemView.findViewById(R.id.textViewFecha);
    }
    @Override
    public void onClick(View v) {
        onGastoListener.onGastoClick(getAdapterPosition());
    }

    public interface OnGastoListener {
        void onGastoClick(int position);
    }
}
