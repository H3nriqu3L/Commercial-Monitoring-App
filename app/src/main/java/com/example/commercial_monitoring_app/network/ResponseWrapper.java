package com.example.commercial_monitoring_app.network;

import java.util.List;

public class ResponseWrapper<T> {
    public Dados<T> dados;

    public static class Dados<T> {
        public List<T> dados;
    }
}
