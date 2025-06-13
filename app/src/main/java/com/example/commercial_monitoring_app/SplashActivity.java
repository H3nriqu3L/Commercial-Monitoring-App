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
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }).start();

    }

    private void loadData() {
        // data loading logic here
        // - TODO  Load local datasets
        // - TODO Initialize components
        // - TODO Make initial API calls

        DatabaseHelper dbHelper = DatabaseHelper.getInstance();

        Cursor cursor = dbHelper.search("Client", null, "", "name ASC");
        List<Client> clients = new ArrayList<>();

        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"));
            String cpf = cursor.getString(cursor.getColumnIndexOrThrow("cpf"));
            String birthDate = cursor.getString(cursor.getColumnIndexOrThrow("birthDate"));

            Client client = new Client(name, email, phoneNumber, cpf, birthDate);
            clients.add(client);
        }

        cursor.close();

        // Make the clientList Global
        MyApp.setClientList(clients);

        apiService = RetrofitClient.getApiService(ApiService.class, "https://crmufvgrupo3.apprubeus.com.br/");
        MyApp.fetchOportunidadesFromApi(null, apiService);
        MyApp.fetchClientesFromApi(apiService);
        MyApp.fetchPersonalDataFromApi(apiService);

        // Simulated loading time
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
