package com.proyecto.cashcarp.clases;

public class TipoGasto {

    private String color;

    private String nombreTipoGasto;

    private String id;


    public TipoGasto() {

    }

    public TipoGasto(String color, String nombreTipoGasto) {
        this.color = color;
        this.nombreTipoGasto = nombreTipoGasto;
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

    public String getNombreTipoGasto() {
        return nombreTipoGasto;
    }

    public void setNombreTipoGasto(String nombreTipoGasto) {
        this.nombreTipoGasto = nombreTipoGasto;
    }
}
