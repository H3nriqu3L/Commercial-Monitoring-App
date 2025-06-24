package com.example.commercial_monitoring_app.network;

import com.example.commercial_monitoring_app.model.Agendamento;
import com.example.commercial_monitoring_app.model.Client;
import com.example.commercial_monitoring_app.model.NavigationResponse;
import com.example.commercial_monitoring_app.model.Oportunidade;
import com.example.commercial_monitoring_app.model.OportunidadeDadosResponse;
import com.example.commercial_monitoring_app.model.PersonalData;
import com.example.commercial_monitoring_app.model.Responsavel;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Cookie: token=Q9VRHME2FL; PHPSESSID=l78cv2qc2u2qva1eoddh4d1004"
    })
    @POST("api/Agendamento/listarAgendamentos")
    Call<ResponseWrapper<Agendamento>> listarAgendamentos();

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


    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Cookie: token=Q9VRHME2FL; PHPSESSID=l78cv2qc2u2qva1eoddh4d1004"
    })
    @FormUrlEncoded
    @POST("api/Contato/listarOportunidades")
    Call<ResponseWrapper<Oportunidade>> listarOportunidadesContato(
            @Field("origem") String origem,
            @Field("id") String id
    );

    @FormUrlEncoded
    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Cookie: token=Q9VRHME2FL; PHPSESSID=l78cv2qc2u2qva1eoddh4d1004"
    })
    @POST("api/Oportunidade/alterarResponsavel")
    Call<Void> alterarResponsavel(
            @Field("id") int id,
            @Field("responsavel") int responsavel,
            @Field("responsavelNome") String responsavelNome,
            @Field("indiceFilaRequisicao") int indiceFilaRequisicao
    );

    @FormUrlEncoded
    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Cookie: token=Q9VRHME2FL; PHPSESSID=l78cv2qc2u2qva1eoddh4d1004"
    })
    @POST("api/Oportunidade/listarResponsavelAlteracao")
    Call<ResponseWrapper2<Responsavel>> listarResponsavelAlteracao(
            @Field("id") int oportunidadeId,
            @Field("indiceFilaRequisicao") int indiceFilaRequisicao);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Cookie: token=Q9VRHME2FL; PHPSESSID=l78cv2qc2u2qva1eoddh4d1004"
    })
    @GET("api/Navegacao/Tela/6/2/")
    Call<NavigationResponse> getNavigationData();

    @FormUrlEncoded
    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Cookie: token=Q9VRHME2FL; PHPSESSID=l78cv2qc2u2qva1eoddh4d1004"
    })
    @POST("api/Oportunidade/alterarStatus")
    Call<Void> alterarStatusOportunidade(
            @Field("id") int id,
            @Field("origem") int origem,
            @Field("status") int status,
            @Field("statusCliente") int statusCliente,
            @Field("indiceFilaRequisicao") int indiceFilaRequisicao
    );

    @FormUrlEncoded
    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Cookie: token=Q9VRHME2FL; PHPSESSID=l78cv2qc2u2qva1eoddh4d1004"
    })
    @POST("api/Pessoa/dadosOportunidadeAtualizada")
    Call<Void> atualizarDadosOportunidade(
            @Field("oportunidade") int oportunidadeId,
            @Field("pessoa") int pessoaId,
            @Field("indiceFilaRequisicao") int indiceFilaRequisicao
    );

    @FormUrlEncoded
    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Cookie: token=Q9VRHME2FL; PHPSESSID=l78cv2qc2u2qva1eoddh4d1004"
    })
    @POST("api/Oportunidade/alterarStatus")
    Call<Void> alterarStatusOportunidadePerdida(
            @Field("id") int id,
            @Field("origem") int origem,
            @Field("statusCliente") int statusCliente,
            @Field("processo") int processo,
            @Field("possuiConcorrente") int possuiConcorrente,
            @Field("observacaoPerda") String observacaoPerda,
            @Field("status") int status,
            @Field("objecao") int objecao,
            @Field("objecaoNome") String objecaoNome,
            @Field("indiceFilaRequisicao") int indiceFilaRequisicao
    );

    @FormUrlEncoded
    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Cookie: token=Q9VRHME2FL; PHPSESSID=l78cv2qc2u2qva1eoddh4d1004"
    })
    @POST("api/Oportunidade/dados")
    Call<okhttp3.ResponseBody> getOportunidadeDadosRaw(
            @Field("id") int id,
            @Field("apenasOpcoesHabilitados") int apenasOpcoesHabilitados,
            @Field("indiceFilaRequisicao") int indiceFilaRequisicao
    );

    @FormUrlEncoded
    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Cookie: token=Q9VRHME2FL; PHPSESSID=l78cv2qc2u2qva1eoddh4d1004"
    })
    @POST("api/Pessoa/listarAtividades")
    Call<okhttp3.ResponseBody> listarAtividadesRaw(
            @Field("pessoa") int pessoaId,
            @Field("oportunidades[]") int oportunidadeId,
            @Field("filtrarOportunidades") boolean filtrar,
            @Field("indiceFilaRequisicao") int indiceFila
    );

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "Cookie: token=Q9VRHME2FL; PHPSESSID=vihgqnrm3vv4klvdftb81pfm9p"
    })
    @GET("api/Navegacao/Tela/5/2/")
    Call<okhttp3.ResponseBody> getNavegacaoInternaUsuario(
            @Query("id") int id
    );






}
