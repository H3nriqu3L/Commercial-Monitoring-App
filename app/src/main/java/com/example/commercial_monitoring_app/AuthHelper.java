package com.example.commercial_monitoring_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.commercial_monitoring_app.model.NavigationResponse;
import com.example.commercial_monitoring_app.model.Responsavel;
import com.example.commercial_monitoring_app.network.ApiService;
import com.example.commercial_monitoring_app.network.ResponseWrapper2;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthHelper {

    public static void verifyResponsavelAndClient(Context context, String email, ApiService apiService) {
        Call<ResponseWrapper2<Responsavel>> responsavelCall = apiService.listarResponsavelAlteracao(34, 1);
        responsavelCall.enqueue(new Callback<ResponseWrapper2<Responsavel>>() {
            @Override
            public void onResponse(Call<ResponseWrapper2<Responsavel>> call, Response<ResponseWrapper2<Responsavel>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().dados != null) {
                    fetchNavigationDataAndCompare(context, email, response.body().dados, apiService);
                } else {
                    Toast.makeText(context, "Erro ao buscar responsáveis", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper2<Responsavel>> call, Throwable t) {
                Log.e("AuthHelper", "Erro na API", t);
                Toast.makeText(context, "Erro de rede: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void fetchNavigationDataAndCompare(Context context, String email, List<Responsavel> responsaveis, ApiService apiService) {
        apiService.getNavigationData().enqueue(new Callback<NavigationResponse>() {
            @Override
            public void onResponse(Call<NavigationResponse> call, Response<NavigationResponse> response) {
                if (response.isSuccessful() && response.body() != null &&
                        response.body().getUserDataWrapper() != null &&
                        response.body().getUserDataWrapper().getUsers() != null) {

                    List<NavigationResponse.User> users = response.body().getUserDataWrapper().getUsers();
                    verifyCredentials(context, email, responsaveis, users);
                } else {
                    Toast.makeText(context, "Erro ao buscar navegação", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NavigationResponse> call, Throwable t) {
                Toast.makeText(context, "Erro de rede: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void verifyCredentials(Context context, String email, List<Responsavel> responsaveis, List<NavigationResponse.User> users) {
        for (Responsavel responsavel : responsaveis) {
            for (NavigationResponse.User user : users) {
                if (user.getEmail().equalsIgnoreCase(email)) {
                    handleSuccessfulLogin(context, responsavel, users, user.getEmail(), user.getName(), user.getId());
                    return;
                }
            }
        }
        Toast.makeText(context, "Credenciais inválidas ou usuário não autorizado", Toast.LENGTH_SHORT).show();
    }

    private static void handleSuccessfulLogin(Context context, Responsavel responsavel, List<NavigationResponse.User> users, String email, String nome, String id) {
        UserSession session = UserSession.getInstance(context);
        session.saveUserSession(Integer.parseInt(id), email, nome);
        Log.e("LOGIN_DATA", "Dados Login"+ email + nome + id);
        MyApp.setNavigationUsersList(users);

        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);

        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }
}
