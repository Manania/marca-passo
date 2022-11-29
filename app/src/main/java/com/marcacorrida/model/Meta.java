package com.marcacorrida.model;

import java.util.Date;

public class Meta {
    private int numPassos;
    private Date data;

    public Meta(int numPassos, Date data) {
        this.numPassos = numPassos;
        this.data = data;
    }

    public int getNumPassos() {
        return numPassos;
    }

    public void setNumPassos(int numPassos) {
        this.numPassos = numPassos;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
