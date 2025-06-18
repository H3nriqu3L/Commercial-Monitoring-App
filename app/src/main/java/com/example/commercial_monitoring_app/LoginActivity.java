package com.example.commercial_monitoring_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.commercial_monitoring_app.model.Client;
import com.example.commercial_monitoring_app.model.NavigationResponse;
import com.example.commercial_monitoring_app.model.Oportunidade;
import com.example.commercial_monitoring_app.model.Responsavel;
import com.example.commercial_monitoring_app.network.ApiService;
import com.example.commercial_monitoring_app.network.PersonalDataResponse;
import com.example.commercial_monitoring_app.network.ResponseWrapper;
import com.example.commercial_monitoring_app.network.ResponseWrapper2;
import com.example.commercial_monitoring_app.network.RetrofitClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button loginButton;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        apiService = RetrofitClient.getApiService(ApiService.class, "https://crmufvgrupo3.apprubeus.com.br/");

        loginButton.setOnClickListener(view -> attemptLogin());
    }

    private void attemptLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        verifyResponsavelAndClient(email);
    }

    private void verifyResponsavelAndClient(String email) {
        Call<ResponseWrapper2<Responsavel>> responsavelCall = apiService.listarResponsavelAlteracao(34, 1);
        responsavelCall.enqueue(new Callback<ResponseWrapper2<Responsavel>>() {
            @Override
            public void onResponse(Call<ResponseWrapper2<Responsavel>> call, Response<ResponseWrapper2<Responsavel>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().dados != null) {
                    fetchNavigationDataAndCompare(email, response.body().dados);
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper2<Responsavel>> call, Throwable t) {
                handleNetworkError(t);
            }
        });
    }
    private void fetchNavigationDataAndCompare(String email, List<Responsavel> responsaveis) {
        Call<NavigationResponse> navigationCall = apiService.getNavigationData();
        navigationCall.enqueue(new Callback<NavigationResponse>() {
            @Override
            public void onResponse(Call<NavigationResponse> call, Response<NavigationResponse> response) {
                if (response.isSuccessful() && response.body() != null &&
                        response.body().getUserDataWrapper() != null &&
                        response.body().getUserDataWrapper().getUsers() != null) {

                    List<NavigationResponse.User> users = response.body().getUserDataWrapper().getUsers();
                    verifyCredentials(email, responsaveis, users);
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<NavigationResponse> call, Throwable t) {
                handleNetworkError(t);
            }
        });
    }


    private void verifyCredentials(String email, List<Responsavel> responsaveis, List<NavigationResponse.User> users) {
        for (Responsavel responsavel : responsaveis) {
            for (NavigationResponse.User user : users) {
                if (user.getEmail().equalsIgnoreCase(email)) {
                    handleSuccessfulLogin(responsavel, users, user.getEmail(), user.getName());  // Pass users list here
                    return;
                }
            }
        }
        Toast.makeText(this, "Credenciais inválidas ou usuário não autorizado", Toast.LENGTH_SHORT).show();
    }

    private void handleSuccessfulLogin(Responsavel responsavel, List<NavigationResponse.User> users, String email, String nome) {
        UserSession session = UserSession.getInstance(LoginActivity.this);
        session.saveUserSession(responsavel.id, email, nome);

        MyApp.setNavigationUsersList(users);

        loadInitialData(() -> {
            Toast.makeText(LoginActivity.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });
    }

    private void loadInitialData(Runnable onComplete) {
        // Load opportunities
        apiService.listarOportunidades().enqueue(new Callback<ResponseWrapper<Oportunidade>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<Oportunidade>> call, Response<ResponseWrapper<Oportunidade>> response) {
                if (response.isSuccessful()) {
                    Log.d("LOGIN", "Oportunidades carregadas");
                }
                loadPersonalData(onComplete);
            }

            @Override
            public void onFailure(Call<ResponseWrapper<Oportunidade>> call, Throwable t) {
                loadPersonalData(onComplete);
            }
        });
    }

    private void loadPersonalData(Runnable onComplete) {
        apiService.searchPersonalData().enqueue(new Callback<PersonalDataResponse>() {
            @Override
            public void onResponse(Call<PersonalDataResponse> call, Response<PersonalDataResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("LOGIN", "Dados pessoais carregados");
                }
                onComplete.run();
            }

            @Override
            public void onFailure(Call<PersonalDataResponse> call, Throwable t) {
                onComplete.run();
            }
        });
    }

    private void handleApiError(Response<?> response) {
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
            Log.e("API_ERROR", "Error: " + errorBody);
            Toast.makeText(this, "Erro na API: " + response.code(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("API_ERROR", "Error reading error body", e);
            Toast.makeText(this, "Erro ao processar resposta", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleNetworkError(Throwable t) {
        Log.e("NETWORK_ERROR", "Error: ", t);
        Toast.makeText(this, "Erro de rede: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}