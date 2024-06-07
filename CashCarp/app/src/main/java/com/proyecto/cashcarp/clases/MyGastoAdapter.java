package com.proyecto.cashcarp.clases;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.cashcarp.R;
import com.proyecto.cashcarp.pantallas.EditGastoActivity;

import java.util.List;

public class MyGastoAdapter extends RecyclerView.Adapter<MyGastoViewHolder> {

    Context context;
    List<Gasto> gastoLista;

    public MyGastoAdapter(Context c, List<Gasto> gastoLista) {
        this.context = c;
        this.gastoLista = gastoLista;
    }

    @NonNull
    @Override
    public MyGastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyGastoViewHolder(LayoutInflater.from(context).inflate(R.layout.gasto_view, parent, false), new MyGastoViewHolder.OnGastoListener() {
            @Override
            public void onGastoClick(int position) {
                Gasto selectedGasto = gastoLista.get(position);
                Intent intent = new Intent(context, EditGastoActivity.class);
                intent.putExtra("descripcion", selectedGasto.getDescripcion());
                intent.putExtra("cantidad", selectedGasto.getCantidad());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull MyGastoViewHolder holder, int position) {
        holder.descValue.setText(gastoLista.get(position).getDescripcion());
        holder.cantValue.setText(String.valueOf(gastoLista.get(position).getCantidad()));
        holder.fechaValue.setText(gastoLista.get(position).getTs().toDate().toString());
    }

    @Override
    public int getItemCount() {
        return gastoLista.size();
    }
}
