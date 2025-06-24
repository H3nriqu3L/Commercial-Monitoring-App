package com.example.commercial_monitoring_app.model;

import com.google.gson.annotations.SerializedName;

public class OportunidadeDadosResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("dados")
    private Oportunidade dados;

    public boolean isSuccess() {
        return success;
    }

    public Oportunidade getDados() {
        return dados;
    }
}
