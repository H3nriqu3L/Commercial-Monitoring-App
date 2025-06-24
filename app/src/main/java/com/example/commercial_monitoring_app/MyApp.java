package com.example.commercial_monitoring_app;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.commercial_monitoring_app.model.Agendamento;
import com.example.commercial_monitoring_app.model.Client;
import com.example.commercial_monitoring_app.model.NavigationResponse;
import com.example.commercial_monitoring_app.model.Oportunidade;
import com.example.commercial_monitoring_app.model.PersonalData;
import com.example.commercial_monitoring_app.model.Responsavel;
import com.example.commercial_monitoring_app.network.ApiService;
import com.example.commercial_monitoring_app.network.PersonalDataResponse;
import com.example.commercial_monitoring_app.network.ResponseWrapper;
import com.example.commercial_monitoring_app.network.ResponseWrapper2;
import com.example.commercial_monitoring_app.network.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApp extends Application {
    private static Runnable onClientsReadyCallback;
    private static Runnable onTodasOportunidadesReadyCallback;


    public static void setOnClientsReadyCallback(Runnable callback) {
        onClientsReadyCallback = callback;
    }

    public static void notifyClientsReady() {
        if (onClientsReadyCallback != null) {
            onClientsReadyCallback.run();
        }
    }

    public static void setOnTodasOportunidadesReadyCallback(Runnable callback) {
        onTodasOportunidadesReadyCallback = callback;

        // if already loaded, trigger immediately
        if (todasOportunidadesList != null && !todasOportunidadesList.isEmpty()) {
            callback.run();
        }
    }

    public static void notifyTodasOportunidadesReady() {
        if (onTodasOportunidadesReadyCallback != null) {
            onTodasOportunidadesReadyCallback.run();
        }
    }

    private static Context context;
    private static List<Client> clientList = new ArrayList<>();
    private static List<Oportunidade> oportunidadeList = new ArrayList<>();

    private static List<Oportunidade> todasOportunidadesList = new ArrayList<>();

    private static List<Agendamento> agendamentoList = new ArrayList<>();
    private static List<PersonalData> personalDataList = new ArrayList<>();

    private static List<Responsavel> responsaveisList = new ArrayList<>();
    private static List<NavigationResponse.User> navigationUsersList = new ArrayList<>();


    private static ApiService apiService;

    private static UserSession userSession;

    public static ApiService getApiService() {
        if (apiService == null) {
            apiService = RetrofitClient.getApiService(ApiService.class, "https://crmufvgrupo3.apprubeus.com.br/");
        }
        return apiService;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        userSession =  UserSession.getInstance(context);
        apiService = RetrofitClient.getApiService(ApiService.class, "https://crmufvgrupo3.apprubeus.com.br/");

    }

    public static UserSession getUserSession() {
        if (userSession == null) {
            userSession = UserSession.getInstance(getAppContext());
        }
        return userSession;
    }

    public static Context getAppContext() {
        return MyApp.context;
    }

    public static void setClientList(List<Client> list) {
        clientList = list != null ? list : new ArrayList<>();
    }

    public static List<Client> getClientList() {
        return clientList != null ? clientList : new ArrayList<>();
    }

    public static List<Oportunidade> getOportunidadeList() {
        return oportunidadeList != null ? oportunidadeList : new ArrayList<>();
    }

    public static List<Oportunidade> getTodasOportunidadesList() {
        return todasOportunidadesList != null ? todasOportunidadesList : new ArrayList<>();
    }

    public static void setTodasOportunidadesList(List<Oportunidade> list) {
        todasOportunidadesList = list != null ? list : new ArrayList<>();
    }

    public static List<Agendamento> getAgendamentoList() {
        return agendamentoList != null ? agendamentoList : new ArrayList<>();
    }


    public static List<PersonalData> getPersonalDataList() {
        return personalDataList != null ? personalDataList : new ArrayList<>();
    }

    public static void setOportunidadesList(List<Oportunidade> newList) {
        oportunidadeList = newList != null ? newList : new ArrayList<>();
    }

    public static void setPersonalDataList(List<PersonalData> list) {
        personalDataList = list != null ? list : new ArrayList<>();
    }

    public static void fetchOportunidadesFromApi(Callback<ResponseWrapper<Oportunidade>> callback, ApiService apiService) {
        Call<ResponseWrapper<Oportunidade>> call = apiService.listarOportunidades();

        call.enqueue(new Callback<ResponseWrapper<Oportunidade>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<Oportunidade>> call, Response<ResponseWrapper<Oportunidade>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().dados != null) {
                    oportunidadeList = response.body().dados.dados != null ? response.body().dados.dados : new ArrayList<>();
                    Log.d("API", "Oportunidades carregadas: " + oportunidadeList.size());
                } else {
                    logAndShowError("Erro na API: " + response.code(), response);
                }
                if (callback != null) {
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<Oportunidade>> call, Throwable t) {
                Log.e("API_FAILURE", "Erro: ", t);
                showToast("Erro de rede: " + t.getMessage());
                if (callback != null) {
                    callback.onFailure(call, t);
                }
            }
        });
    }

    public static void fetchTodasOportunidadesFromApi(Runnable onComplete) {
        try {
            List<Client> clients = getClientList();
            int totalClients = clients.size();

            if (totalClients == 0) {
                setTodasOportunidadesList(new ArrayList<>());
                if (onComplete != null) onComplete.run();
                return;
            }

            List<Oportunidade> allOportunidades = new ArrayList<>();
            final int[] responsesReceived = {0};

            for (Client client : clients) {
                int clientId = client.getId();

                Call<ResponseBody> call = getApiService().getNavegacaoInternaUsuario(clientId);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                String rawJson = response.body().string();
                                JSONObject root = new JSONObject(rawJson);
                                JSONObject dadosPessoaWrapper = root.optJSONObject("dadosPessoa");

                                if (dadosPessoaWrapper != null && dadosPessoaWrapper.optBoolean("success")) {
                                    JSONObject dadosPessoa = dadosPessoaWrapper.optJSONObject("dados");
                                    if (dadosPessoa != null) {
                                        JSONArray processos = dadosPessoa.optJSONArray("processos");
                                        if (processos != null) {
                                            for (int i = 0; i < processos.length(); i++) {
                                                JSONObject processo = processos.getJSONObject(i);
                                                JSONArray oportunidades = processo.optJSONArray("oportunidades");

                                                if (oportunidades != null) {
                                                    for (int j = 0; j < oportunidades.length(); j++) {
                                                        JSONObject oportunidadeJson = oportunidades.getJSONObject(j);

                                                        Oportunidade oportunidade = new Oportunidade();
                                                        try {
                                                            Field statusField = Oportunidade.class.getDeclaredField("status");
                                                            statusField.setAccessible(true);
                                                            statusField.set(oportunidade, oportunidadeJson.optString("status"));

                                                            Field respField = Oportunidade.class.getDeclaredField("responsavelNome");
                                                            respField.setAccessible(true);
                                                            respField.set(oportunidade, oportunidadeJson.optString("responsavel"));

                                                            Field idField = Oportunidade.class.getDeclaredField("id");
                                                            idField.setAccessible(true);
                                                            idField.set(oportunidade, oportunidadeJson.optString("id"));
                                                        } catch (Exception reflectionError) {
                                                            Log.e("REFLECTION_ERROR", "Erro setando campos em Oportunidade", reflectionError);
                                                        }

                                                        allOportunidades.add(oportunidade);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            } catch (Exception e) {
                                Log.e("PARSE_ERROR", "Erro ao parsear JSON para clientId " + clientId, e);
                            }
                        } else {
                            Log.e("API_ERROR", "Erro HTTP para clientId " + clientId + ": código " + response.code());
                        }

                        responsesReceived[0]++;
                        if (responsesReceived[0] == totalClients) {
                            setTodasOportunidadesList(allOportunidades);
                            Log.d("TODAS_OPORTS", "Total carregadas: " + allOportunidades.size());
                            if (onComplete != null) onComplete.run();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("NAV_FAILURE", "Erro de rede para clientId " + clientId, t);
                        responsesReceived[0]++;
                        if (responsesReceived[0] == totalClients) {
                            setTodasOportunidadesList(allOportunidades);
                            if (onComplete != null) onComplete.run();
                        }
                    }
                });
            }

        } catch (Exception e) {
            Log.e("FETCH_ALL_OPORTS_ERROR", "Erro inesperado ao buscar oportunidades", e);
            if (onComplete != null) onComplete.run();
        }
    }


    public static void fetchAgendamentoFromApi(Callback<ResponseWrapper<Agendamento>> callback, ApiService apiService) {
        Call<ResponseWrapper<Agendamento>> call = apiService.listarAgendamentos();

        call.enqueue(new Callback<ResponseWrapper<Agendamento>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<Agendamento>> call, Response<ResponseWrapper<Agendamento>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().dados != null) {
                    agendamentoList = response.body().dados.dados != null ? response.body().dados.dados : new ArrayList<>();
                    Log.d("API", "Agendamentos carregados: " + agendamentoList.size());
                } else {
                    logAndShowError("Erro na API: " + response.code(), response);
                }
                if (callback != null) {
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<Agendamento>> call, Throwable t) {
                Log.e("API_FAILURE", "Erro: ", t);
                showToast("Erro de rede: " + t.getMessage());
                if (callback != null) {
                    callback.onFailure(call, t);
                }
            }
        });
    }

    public static List<NavigationResponse.User> getNavigationUsersList() {
        return navigationUsersList != null ? navigationUsersList : new ArrayList<>();
    }

    public static void setNavigationUsersList(List<NavigationResponse.User> list) {
        navigationUsersList = list != null ? list : new ArrayList<>();
    }

    public static NavigationResponse.User getCurrentresponsavelUser(){
        int userIdResponsavel = UserSession.getLoggedUserId();

        List<NavigationResponse.User> users = MyApp.getNavigationUsersList();
        for (NavigationResponse.User user : users) {
            if (String.valueOf(userIdResponsavel).equals(user.getId())) {
                return user;
            }
        }
        return null;
    }

    public static void fetchNavigationData(ApiService apiService, Callback<NavigationResponse> callback) {
        Call<NavigationResponse> call = apiService.getNavigationData();

        call.enqueue(new Callback<NavigationResponse>() {
            @Override
            public void onResponse(Call<NavigationResponse> call, Response<NavigationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NavigationResponse.UserDataWrapper wrapper = response.body().getUserDataWrapper();
                    if (wrapper != null && wrapper.getUsers() != null) {
                        setNavigationUsersList(wrapper.getUsers());
                        Log.d("API", "Navigation users loaded: " + wrapper.getUsers().size());
                    }
                } else {
                    logAndShowError("Navigation API error: " + response.code(), response);
                }

                if (callback != null) {
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<NavigationResponse> call, Throwable t) {
                Log.e("API_FAILURE", "Navigation data error: ", t);
                showToast("Navigation data error: " + t.getMessage());
                if (callback != null) {
                    callback.onFailure(call, t);
                }
            }
        });
    }

    public static List<Responsavel> getResponsaveisList() {
        return responsaveisList != null ? responsaveisList : new ArrayList<>();
    }

    public static void fetchResponsaveisFromApi(int oportunidadeId, int indiceFilaRequisicao,
                                                Callback<ResponseWrapper2<Responsavel>> callback, ApiService apiService) {

        Call<ResponseWrapper2<Responsavel>> call = apiService.listarResponsavelAlteracao(
                34,
                1);

        call.enqueue(new Callback<ResponseWrapper2<Responsavel>>() {
            @Override
            public void onResponse(Call<ResponseWrapper2<Responsavel>> call,
                                   Response<ResponseWrapper2<Responsavel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    responsaveisList = response.body().dados != null ?
                            response.body().dados : new ArrayList<>();
                    Log.d("API", "Responsáveis carregados: " + responsaveisList.size());

                    if (callback != null) {
                        callback.onResponse(call, response);
                    }
                } else {
                    logAndShowError("Erro na API (responsáveis): " + response.code(), response);
                    if (callback != null) {
                        callback.onResponse(call, response);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper2<Responsavel>> call, Throwable t) {
                Log.e("API_FAILURE", "Erro ao buscar responsáveis: ", t);
                showToast("Erro de rede ao buscar responsáveis: " + t.getMessage());
                if (callback != null) {
                    callback.onFailure(call, t);
                }
            }
        });
    }

    public interface OnOportunidadeDeletedListener {
        void onOportunidadeDeleted();
    }

    public static void excluirOportunidadeEAtualizarLista(int oportunidadeId, OnOportunidadeDeletedListener listener) {
//        Call<Void> call = apiService.excluirOportunidade(oportunidadeId);
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    showToast("Oportunidade excluída com sucesso");
//                    // Fetch updated list and then notify
//                    fetchOportunidadesFromApi(new Callback<ResponseWrapper<Oportunidade>>() {
//                        @Override
//                        public void onResponse(Call<ResponseWrapper<Oportunidade>> call, Response<ResponseWrapper<Oportunidade>> response) {
//                            if (listener != null) {
//                                listener.onOportunidadeDeleted();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseWrapper<Oportunidade>> call, Throwable t) {
//                            if (listener != null) {
//                                listener.onOportunidadeDeleted();
//                            }
//                        }
//                    });
//                } else {
//                    logAndShowError("Erro ao excluir: " + response.code(), response);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.e("API_FAILURE", "Erro ao excluir oportunidade: ", t);
//                showToast("Erro de rede ao excluir: " + t.getMessage());
//            }
//        });
    }

    public static void fetchClientesFromApi(ApiService apiService, Runnable onComplete) {
        Call<ResponseWrapper<Client>> call = apiService.listarPessoas();

        call.enqueue(new Callback<ResponseWrapper<Client>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<Client>> call, Response<ResponseWrapper<Client>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().dados != null) {
                    clientList = response.body().dados.dados != null ? response.body().dados.dados : new ArrayList<>();
                    Log.d("API", "Clientes carregados: " + clientList.size());
                } else {
                    logAndShowError("Erro na API (clientes): " + response.code(), response);
                }
                if (onComplete != null) onComplete.run();
            }

            @Override
            public void onFailure(Call<ResponseWrapper<Client>> call, Throwable t) {
                Log.e("API_FAILURE", "Erro ao buscar clientes: ", t);
                showToast("Erro de rede ao buscar clientes: " + t.getMessage());
                if (onComplete != null) onComplete.run();
            }
        });
    }


    public static void fetchPersonalDataFromApi(ApiService apiService) {
        Call<PersonalDataResponse> call = apiService.searchPersonalData();

        call.enqueue(new Callback<PersonalDataResponse>() {
            @Override
            public void onResponse(Call<PersonalDataResponse> call, Response<PersonalDataResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().dados != null) {
                    personalDataList = response.body().dados;  // <- Remove the second .dados
                    Log.d("API", "Personal Data carregados: " + personalDataList.size());
                } else {
                    logAndShowError("Erro na API (personal data): " + response.code(), response);
                }
            }

            @Override
            public void onFailure(Call<PersonalDataResponse> call, Throwable t) {  // <- New type
                Log.e("API_FAILURE", "Erro ao buscar personalData: ", t);
                showToast("Erro de rede ao buscar personal data: " + t.getMessage());
            }
        });
    }

    private static void logAndShowError(String message, Response<?> response) {
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Erro desconhecido";
            Log.e("API_RESPONSE", "Erro: " + errorBody);
        } catch (IOException e) {
            Log.e("API_RESPONSE", "Exception ao ler erro: ", e);
        }
        showToast(message);
    }

    private static void showToast(String message) {
        if (getAppContext() != null) {
            Toast.makeText(getAppContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}