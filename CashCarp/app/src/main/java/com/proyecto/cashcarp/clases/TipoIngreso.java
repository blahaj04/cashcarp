package com.proyecto.cashcarp.clases;

public class TipoIngreso {
    private String color;

    private String nombreTipoIngreso;

    private String id;


    public TipoIngreso() {

    }

    public TipoIngreso(String color, String nombre) {
        this.color = color;
        this.nombreTipoIngreso = nombre;
    }

    public String getNombreTipoIngreso() {
        return nombreTipoIngreso;
    }

    public void setNombreTipoIngreso(String nombreTipoIngreso) {
        this.nombreTipoIngreso = nombreTipoIngreso;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
