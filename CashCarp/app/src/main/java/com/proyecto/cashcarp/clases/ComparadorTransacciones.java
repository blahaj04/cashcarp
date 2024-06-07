package com.proyecto.cashcarp.clases;

import java.util.Comparator;

public class ComparadorTransacciones implements Comparator<Object> {
    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof Gasto && o2 instanceof Ingreso) {
            return ((Gasto) o1).getTs().compareTo(((Ingreso) o2).getTs());
        } else if (o1 instanceof Ingreso && o2 instanceof Gasto) {
            return ((Ingreso) o1).getTs().compareTo(((Gasto) o2).getTs());
        } else if (o1 instanceof Gasto) {
            return ((Gasto) o1).getTs().compareTo(((Gasto) o2).getTs());
        } else {
            return ((Ingreso) o1).getTs().compareTo(((Ingreso) o2).getTs());
        }
    }
}
