package com.example.commercial_monitoring_app.network;

import com.example.commercial_monitoring_app.model.Client;
import com.example.commercial_monitoring_app.model.Oportunidade;
import com.example.commercial_monitoring_app.model.PersonalData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Cookie: token=Q9VRHME2FL; PHPSESSID=l78cv2qc2u2qva1eoddh4d1004"
    })
    @POST("api/Pessoa/listarPessoas")
    Call<ResponseWrapper<Client>> listarPessoas();

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Cookie: token=Q9VRHME2FL; PHPSESSID=l78cv2qc2u2qva1eoddh4d1004"
    })
    @POST("api/Oportunidade/listarOportunidades")
    Call<ResponseWrapper<Oportunidade>> listarOportunidades();

    @FormUrlEncoded
    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Cookie: token=Q9VRHME2FL; PHPSESSID=l78cv2qc2u2qva1eoddh4d1004"
    })
    @POST("api/Oportunidade/excluirOportunidade")
    Call<Void> excluirOportunidade(
            @Field("id") int oportunidadeId
    );

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Cookie: token=Q9VRHME2FL; PHPSESSID=l78cv2qc2u2qva1eoddh4d1004"
    })
    @POST("api/Contato/dadosPessoas")
    Call<PersonalDataResponse> searchPersonalData();
}
