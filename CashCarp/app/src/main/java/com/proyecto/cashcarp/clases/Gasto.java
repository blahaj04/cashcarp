package com.proyecto.cashcarp.clases;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Gasto {

    private String descripcion;

    private double cantidad;


    private Timestamp ts;

    public Gasto() {
    }

    public Gasto(String descripcion, double cantidad, Timestamp ts) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.ts = ts;
    }

    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
    public static void ordenarGastosPorFecha(ArrayList<Gasto> gastos) {
        Collections.sort(gastos, new Comparator<Gasto>() {
            @Override
            public int compare(Gasto g1, Gasto g2) {

                return g2.getTs().compareTo(g1.getTs());
            }
        });
    }

    @Override
    public String toString() {
        return "Gasto{" +
                "descripcion='" + descripcion + '\'' +
                ", cantidad=" + cantidad +
                ", ts=" + ts +
                '}';
    }
}
