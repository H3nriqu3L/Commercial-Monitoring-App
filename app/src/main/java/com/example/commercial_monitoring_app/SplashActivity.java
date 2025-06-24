package com.example.commercial_monitoring_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.commercial_monitoring_app.database.DatabaseHelper;
import com.example.commercial_monitoring_app.model.Agendamento;
import com.example.commercial_monitoring_app.model.Oportunidade;
import com.example.commercial_monitoring_app.network.ApiService;
import com.example.commercial_monitoring_app.network.ResponseWrapper;
import com.example.commercial_monitoring_app.network.RetrofitClient;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private ApiService apiService;
    private CountDownLatch apiLatch;
    private static final int TOTAL_APIS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        apiService = RetrofitClient.getApiService(ApiService.class, "https://crmufvgrupo3.apprubeus.com.br/");

        new Thread(() -> {
            loadData();

            runOnUiThread(() -> {
                UserSession session = UserSession.getInstance(SplashActivity.this);

                if (session.isLoggedIn()) {
                    String email = session.getUserEmail();

                    Log.d("SplashActivity", "Usuário logado. Verificando responsável e cliente...");

                    AuthHelper.verifyResponsavelAndClient(SplashActivity.this, email, apiService);

                } else {
                    Log.d("SplashActivity", "Usuário não logado. Indo para LoginActivity...");
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }).start();
    }

    private void loadData() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance();
        apiLatch = new CountDownLatch(TOTAL_APIS);

        Log.d("SplashActivity", "Iniciando carregamento de dados das APIs...");

        MyApp.fetchOportunidadesFromApi(new Callback<ResponseWrapper<Oportunidade>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<Oportunidade>> call, Response<ResponseWrapper<Oportunidade>> response) {
                Log.d("SplashActivity", "Oportunidades carregadas");
                apiLatch.countDown();
            }

            @Override
            public void onFailure(Call<ResponseWrapper<Oportunidade>> call, Throwable t) {
                Log.e("SplashActivity", "Erro ao carregar oportunidades: " + t.getMessage());
                apiLatch.countDown();
            }
        }, apiService);



        MyApp.fetchAgendamentoFromApi(new Callback<ResponseWrapper<Agendamento>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<Agendamento>> call, Response<ResponseWrapper<Agendamento>> response) {
                Log.d("SplashActivity", "Agendamentos carregados");
                apiLatch.countDown();
            }

            @Override
            public void onFailure(Call<ResponseWrapper<Agendamento>> call, Throwable t) {
                Log.e("SplashActivity", "Erro ao carregar agendamentos: " + t.getMessage());
                apiLatch.countDown();
            }
        }, apiService);

        MyApp.fetchClientesFromApi(apiService, () -> {
            runOnUiThread(MyApp::notifyClientsReady);
            MyApp.fetchTodasOportunidadesFromApi(() -> {
                List<Oportunidade> todas = MyApp.getTodasOportunidadesList();
                Log.d("RESULTADO_FINAL", "Total de oportunidades carregadas: " + todas.size());
            });
        });

        MyApp.fetchPersonalDataFromApi(apiService);

        try {
            boolean finished = apiLatch.await(15, TimeUnit.SECONDS); // Timeout de 15 segundos
            if (finished) {
                Log.d("SplashActivity", "APIs principais terminaram. Prosseguindo...");
            } else {
                Log.w("SplashActivity", "Timeout nas APIs principais. Prosseguindo mesmo assim...");
            }
        } catch (InterruptedException e) {
            Log.e("SplashActivity", "Interrompido enquanto aguardava APIs", e);
        }
    }
}
