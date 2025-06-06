package com.example.commercial_monitoring_app.model;

public class Client {
    private String id;
    private String pessoaprincipal_id;
    private String codigo;
    private String cidade_id;
    private String endereco;
    private String cep;
    private String numero;
    private String bairro;
    private String sexo_id;
    private String cor_id;
    private String nome;
    private String nomeSocial;
    private String telefone;
    private String email;
    private String origem;
    private String grauinstrucao_id;
    private String profissao;
    private String escolaorigem;
    private String anoformacao;
    private String outrasdeficiencias;
    private String aluno;
    private String exaluno;
    private String canhoto;
    private String origemNome;
    private String criadoEm;
    private String cpf;
    private String dataNascimento;
    private String idade;
    private String desinscreveu;
    private String usuario_id;

    public Client(String name, String email, String phone, String cpf, String birthDate) {
        this.nome = name;
        this.email = email;
        this.telefone = phone;
        this.cpf = cpf;
        this.dataNascimento = birthDate;
    }
    // Getters and Setters
    public int getId() { return Integer.parseInt(id) ; }
    public void setId(String id) { this.id = id; }

    public String getPessoaprincipal_id() { return pessoaprincipal_id; }
    public void setPessoaprincipal_id(String pessoaprincipal_id) { this.pessoaprincipal_id = pessoaprincipal_id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getCidade_id() { return cidade_id; }
    public void setCidade_id(String cidade_id) { this.cidade_id = cidade_id; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getSexo_id() { return sexo_id; }
    public void setSexo_id(String sexo_id) { this.sexo_id = sexo_id; }

    public String getCor_id() { return cor_id; }
    public void setCor_id(String cor_id) { this.cor_id = cor_id; }

    public String getName() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getNomeSocial() { return nomeSocial; }
    public void setNomeSocial(String nomeSocial) { this.nomeSocial = nomeSocial; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }

    public String getGrauinstrucao_id() { return grauinstrucao_id; }
    public void setGrauinstrucao_id(String grauinstrucao_id) { this.grauinstrucao_id = grauinstrucao_id; }

    public String getProfissao() { return profissao; }
    public void setProfissao(String profissao) { this.profissao = profissao; }

    public String getEscolaorigem() { return escolaorigem; }
    public void setEscolaorigem(String escolaorigem) { this.escolaorigem = escolaorigem; }

    public String getAnoformacao() { return anoformacao; }
    public void setAnoformacao(String anoformacao) { this.anoformacao = anoformacao; }

    public String getOutrasdeficiencias() { return outrasdeficiencias; }
    public void setOutrasdeficiencias(String outrasdeficiencias) { this.outrasdeficiencias = outrasdeficiencias; }

    public String getAluno() { return aluno; }
    public void setAluno(String aluno) { this.aluno = aluno; }

    public String getExaluno() { return exaluno; }
    public void setExaluno(String exaluno) { this.exaluno = exaluno; }

    public String getCanhoto() { return canhoto; }
    public void setCanhoto(String canhoto) { this.canhoto = canhoto; }

    public String getOrigemNome() { return origemNome; }
    public void setOrigemNome(String origemNome) { this.origemNome = origemNome; }

    public String getCriadoEm() { return criadoEm; }
    public void setCriadoEm(String criadoEm) { this.criadoEm = criadoEm; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getIdade() { return idade; }
    public void setIdade(String idade) { this.idade = idade; }

    public String getDesinscreveu() { return desinscreveu; }
    public void setDesinscreveu(String desinscreveu) { this.desinscreveu = desinscreveu; }

    public String getUsuario_id() { return usuario_id; }
    public void setUsuario_id(String usuario_id) { this.usuario_id = usuario_id; }
}
