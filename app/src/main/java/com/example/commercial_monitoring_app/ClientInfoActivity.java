package com.example.commercial_monitoring_app;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.commercial_monitoring_app.model.Client;

public class ClientInfoActivity  extends AppCompatActivity {
    Client myClient;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_client_info);

        initializeViews();
        loadClientData();
    }

   private void initializeViews(){

   }

   private void loadClientData(){


   }




}
