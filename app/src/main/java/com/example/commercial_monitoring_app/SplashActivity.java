package com.example.commercial_monitoring_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.commercial_monitoring_app.database.DatabaseHelper;
import com.example.commercial_monitoring_app.model.Agendamento;
import com.example.commercial_monitoring_app.model.Client;
import com.example.commercial_monitoring_app.model.Oportunidade;
import com.example.commercial_monitoring_app.network.ApiService;
import com.example.commercial_monitoring_app.network.ResponseWrapper;
import com.example.commercial_monitoring_app.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private ApiService apiService;
    private CountDownLatch apiLatch;
    private static final int TOTAL_APIS = 2; // Apenas agendamentos e oportunidades por enquanto

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread(()->{
            loadData();

            runOnUiThread(()->{
                UserSession session = UserSession.getInstance(SplashActivity.this);

                Intent intent;
                if (session.isLoggedIn()) {
                    // Usuário já está logado, vai direto para MainActivity
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    // Usuário não está logado, vai para LoginActivity
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }

                startActivity(intent);
                finish();
            });
        }).start();
    }

    private void loadData() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance();
        apiService = RetrofitClient.getApiService(ApiService.class, "https://crmufvgrupo3.apprubeus.com.br/");

        // ountDownLatch para esperar as 2 APIs principais (agendamentos e oportunidades)
        apiLatch = new CountDownLatch(2);

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

        // Chama as outras APIs sem aguardar
        MyApp.fetchClientesFromApi(apiService);
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
