package com.proyecto.cashcarp.clases;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Ingreso {

    private String descripcion;
    private double cantidad;
    private Timestamp ts;

    private String id;

    private String tipoNombre;



    private String tipoId;
    private String color;

    public Ingreso() {
    }

    public Ingreso(String descripcion, double cantidad, Timestamp ts) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.ts = ts;
    }

    public String getTipoNombre() {
        return tipoNombre;
    }

    public void setTipoNombre(String tipoNombre) {
        this.tipoNombre = tipoNombre;
    }
    public Timestamp getTs() {
        return ts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipoId() {
        return tipoId;
    }

    public void setTipoId(String tipoId) {
        this.tipoId = tipoId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public static void ordenarIngresosPorFecha(ArrayList<Ingreso> ingresos) {
        Collections.sort(ingresos, new Comparator<Ingreso>() {
            @Override
            public int compare(Ingreso i1, Ingreso i2) {

                return i2.getTs().compareTo(i1.getTs());
            }
        });
    }

    @Override
    public String toString() {
        return "Ingreso{" +
                "descripcion='" + descripcion + '\'' +
                ", cantidad=" + cantidad +
                ", ts=" + ts +
                ", id='" + id + '\'' +
                ", tipoId='" + tipoId + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
