package com.proyecto.cashcarp.clases;

public class TipoGasto {

    private String color;

    private String nombreTipoGasto;

    public TipoGasto(){

    }

    public TipoGasto(String color, String nombreTipoGasto) {
        this.color = color;
        this.nombreTipoGasto = nombreTipoGasto;
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
