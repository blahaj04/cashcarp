package com.proyecto.cashcarp.clases;

import com.google.firebase.Timestamp;

public class Ingreso {

    private String descripcion;

    private double cantidad;

    private Timestamp ts;

    public Ingreso() {
    }

    public Ingreso(String descripcion, double cantidad, Timestamp ts) {
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
}
