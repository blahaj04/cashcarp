package com.proyecto.cashcarp.clases;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import com.google.firebase.Timestamp;

public class Gasto {
    private String descripcion;
    private double cantidad;
    private Timestamp ts;

    private String id;

    private String tipoNombre;



    private String tipoId;
    private String color;


    public Gasto(String descripcion, double cantidad, Timestamp ts) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.ts = ts;
    }


    public Gasto() {

    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    public String getTipoNombre() {
        return tipoNombre;
    }

    public void setTipoNombre(String tipoNombre) {
        this.tipoNombre = tipoNombre;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getCantidad() {
        return cantidad;
    }

    public Timestamp getTs() {
        return ts;
    }

    public void setTipoId(String tipoId) {
        this.tipoId = tipoId;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTipoId() {
        return tipoId;
    }

    public String getColor() {
        return color;
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
