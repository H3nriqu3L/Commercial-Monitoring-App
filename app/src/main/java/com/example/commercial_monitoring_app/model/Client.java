package com.example.commercial_monitoring_app.model;

public class Client {
    private String name;
    private String email;
    private String phoneNumber;
    private String cpf;

    private String birthDate;

    public Client(String name, String email, String phoneNumber, String cpf, String birthDate) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.cpf = cpf;
        this.birthDate = birthDate;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCpf() {
        return cpf;
    }

    public String getBirthDate() {
        return birthDate;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

}
