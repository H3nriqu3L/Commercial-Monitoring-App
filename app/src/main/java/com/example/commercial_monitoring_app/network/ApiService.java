package com.example.commercial_monitoring_app.network;

import com.example.commercial_monitoring_app.model.Oportunidade;
import com.example.commercial_monitoring_app.model.Pessoa;

import java.sql.Wrapper;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Cookie: token=Q9VRHME2FL; PHPSESSID=l78cv2qc2u2qva1eoddh4d1004"
    })
    @POST("api/Pessoa/listarPessoas")
    Call<ResponseWrapper<Pessoa>> listarPessoas();

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Cookie: token=Q9VRHME2FL; PHPSESSID=l78cv2qc2u2qva1eoddh4d1004"
    })
    @POST("api/Oportunidade/listarOportunidades")
    Call<ResponseWrapper<Oportunidade>> listarOportunidades();
}
