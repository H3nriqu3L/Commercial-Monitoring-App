package com.example.commercial_monitoring_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    private ApiService apiService = RetrofitClient.getApiService(ApiService.class, "https://crmufvgrupo3.apprubeus.com.br/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(view -> attemptLogin());
    }

    private void attemptLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthHelper.verifyResponsavelAndClient(LoginActivity.this, email, apiService);
    }

    private void loadInitialData(Runnable onComplete) {
        apiService.listarOportunidades().enqueue(new Callback<ResponseWrapper<Oportunidade>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<Oportunidade>> call, Response<ResponseWrapper<Oportunidade>> response) {
                Log.d("LOGIN", "Oportunidades carregadas");
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
                Log.d("LOGIN", "Dados pessoais carregados");
                onComplete.run();
            }

            @Override
            public void onFailure(Call<PersonalDataResponse> call, Throwable t) {
                onComplete.run();
            }
        });
    }
}
