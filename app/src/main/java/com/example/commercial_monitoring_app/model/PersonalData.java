package com.example.commercial_monitoring_app.model;

import java.util.List;

public class PersonalData {
    public String id;
    public String nome;
    public String cpf;
    public String imagem;
    public String nomeSocial;
    public String codigo;
    public String endereco;
    public String cep;
    public String numero;
    public String bairro;
    public String cidade;
    public String cidadeNome;

    // Nested objects
    public Telefones telefones;
    public Emails emails;

    public static class Telefones {
        public Telefone principal;
        public List<Telefone> secundarios;
    }

    public static class Telefone {
        public String id;
        public String telefone;
    }

    public static class Emails {
        public Email principal;
        public List<Email> secundarios;
    }

    public static class Email {
        public String id;
        public String email;
    }
}