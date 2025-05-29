package com.example.commercial_monitoring_app;

import android.app.Application;
import android.content.Context;

import com.example.commercial_monitoring_app.model.Client;

import java.util.List;

public class MyApp extends Application {
    private static Context context;
    private static List<Client> clientList;

    public void onCreate(){
        super.onCreate();
        MyApp.context = getApplicationContext();
    }

    public static Context getAppContext(){
        return MyApp.context;
    }

    public static void setClientList(List<Client> list) {
        clientList = list;
    }

    public static List<Client> getClientList() {
        return clientList;
    }
}
