package com.example.commercial_monitoring_app.model;

public class Client {
    private String title;
    private String description;

    public Client(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
