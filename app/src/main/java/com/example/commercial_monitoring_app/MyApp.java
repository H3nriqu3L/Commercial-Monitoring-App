package com.example.commercial_monitoring_app;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.commercial_monitoring_app.model.Client;
import com.example.commercial_monitoring_app.model.NavigationResponse;
import com.example.commercial_monitoring_app.model.Oportunidade;
import com.example.commercial_monitoring_app.model.PersonalData;
import com.example.commercial_monitoring_app.model.Responsavel;
import com.example.commercial_monitoring_app.network.ApiService;
import com.example.commercial_monitoring_app.network.PersonalDataResponse;
import com.example.commercial_monitoring_app.network.ResponseWrapper;
import com.example.commercial_monitoring_app.network.ResponseWrapper2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApp extends Application {
    private static Context context;
    private static List<Client> clientList = new ArrayList<>();
    private static List<Oportunidade> oportunidadeList = new ArrayList<>();
    private static List<PersonalData> personalDataList = new ArrayList<>();

    private static List<Responsavel> responsaveisList = new ArrayList<>();
    private static List<NavigationResponse.User> navigationUsersList = new ArrayList<>();


    private static ApiService apiService;

    private static UserSession userSession;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        userSession =  UserSession.getInstance(context);
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

    public static List<PersonalData> getPersonalDataList() {
        return personalDataList != null ? personalDataList : new ArrayList<>();
    }

    public void setOportunidadesList(List<Oportunidade> newList) {
        this.oportunidadeList = newList != null ? newList : new ArrayList<>();
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

    public static void fetchClientesFromApi( ApiService apiService) {
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
            }

            @Override
            public void onFailure(Call<ResponseWrapper<Client>> call, Throwable t) {
                Log.e("API_FAILURE", "Erro ao buscar clientes: ", t);
                showToast("Erro de rede ao buscar clientes: " + t.getMessage());
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