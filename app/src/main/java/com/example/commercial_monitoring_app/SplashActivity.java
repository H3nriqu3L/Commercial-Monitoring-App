package com.example.commercial_monitoring_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.commercial_monitoring_app.database.DatabaseHelper;
import com.example.commercial_monitoring_app.model.Client;
import com.example.commercial_monitoring_app.model.Oportunidade;
import com.example.commercial_monitoring_app.network.ApiService;
import com.example.commercial_monitoring_app.network.ResponseWrapper;
import com.example.commercial_monitoring_app.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private ApiService apiService;

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

        // Criar as 3 threads para as requisições
        Thread t1 = new Thread(() -> MyApp.fetchOportunidadesFromApi(null, apiService));
        Thread t2 = new Thread(() -> MyApp.fetchClientesFromApi(apiService));
        Thread t3 = new Thread(() -> MyApp.fetchPersonalDataFromApi(apiService));

        // Iniciar as 3 threads
        t1.start();
        t2.start();
        t3.start();

        try {
            // Aguardar todas terminarem
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
