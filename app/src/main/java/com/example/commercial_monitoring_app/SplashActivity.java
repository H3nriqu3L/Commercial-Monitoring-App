package com.example.commercial_monitoring_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.appcompat.app.AppCompatActivity;

import com.example.commercial_monitoring_app.database.DatabaseHelper;
import com.example.commercial_monitoring_app.model.Client;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

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


        // Simulated loading time
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
