package com.example.commercial_monitoring_app;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;



public class AddClientActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

    }

    public void navigateBack(View view) {
        finish(); // This will close the current activity and return to the previous one
    }


}