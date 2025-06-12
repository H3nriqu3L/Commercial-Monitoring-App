package com.example.commercial_monitoring_app.model;

import java.util.List;

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

    public Telefones telefones;
    public Emails emails;

    // Getters e Setters para PersonalData
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getNomeSocial() {
        return nomeSocial;
    }

    public void setNomeSocial(String nomeSocial) {
        this.nomeSocial = nomeSocial;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCidadeNome() {
        return cidadeNome;
    }

    public void setCidadeNome(String cidadeNome) {
        this.cidadeNome = cidadeNome;
    }

    public Telefones getTelefones() {
        return telefones;
    }

    public void setTelefones(Telefones telefones) {
        this.telefones = telefones;
    }

    public Emails getEmails() {
        return emails;
    }

    public void setEmails(Emails emails) {
        this.emails = emails;
    }

    public static class Telefones {
        public Telefone principal;
        public List<Telefone> secundarios;

        public Telefone getPrincipal() {
            return principal;
        }

        public void setPrincipal(Telefone principal) {
            this.principal = principal;
        }

        public List<Telefone> getSecundarios() {
            return secundarios;
        }

        public void setSecundarios(List<Telefone> secundarios) {
            this.secundarios = secundarios;
        }
    }

    public static class Telefone {
        public String id;
        public String telefone;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTelefone() {
            return telefone;
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
        }
    }

    public static class Emails {
        public Email principal;
        public List<Email> secundarios;

        public Email getPrincipal() {
            return principal;
        }

        public void setPrincipal(Email principal) {
            this.principal = principal;
        }

        public List<Email> getSecundarios() {
            return secundarios;
        }

        public void setSecundarios(List<Email> secundarios) {
            this.secundarios = secundarios;
        }
    }

    public static class Email {
        public String id;
        public String email;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}