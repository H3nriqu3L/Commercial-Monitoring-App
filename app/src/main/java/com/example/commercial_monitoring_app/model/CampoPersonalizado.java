package com.example.commercial_monitoring_app.model;

import com.google.gson.annotations.SerializedName;

public class CampoPersonalizado {

    @SerializedName("id")
    private String id;

    @SerializedName("nome")
    private String nome;

    @SerializedName("coluna")
    private String coluna;


    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getColuna() {
        return coluna;
    }
}
