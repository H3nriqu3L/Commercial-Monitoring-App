package com.example.commercial_monitoring_app.model;

public class Agendamento {
    private String id;
    private String vencimento;
    private String tipo;
    private String contato;
    private String pessoa;
    private String pessoaCodigo;
    private String pessoaNome;
    private String pessoaCpf;
    private String status;
    private String statusNome;
    private String processo;
    private String processoNome;
    private String descricao;
    private String tipoNome;
    private String duracao;
    private String formaContato;
    private String email;
    private String telefone;
    private String responsavel;
    private String responsavelNome;
    private String oportunidade;
    private String curso;
    private String contextoSubItem;
    private String cursoNome;
    private String momentoConcluido;
    private String momento;
    private String processoSeletivo;
    private String processoSeletivoNome;
    private String contextoProcesso;
    private String unidade;
    private String unidadeNome;
    private String razaoOportunidade;
    private String razaoOportunidadeNome;
    private String statusOportunidade;
    private String statusOportunidadeNome;
    private String localOferta;
    private String localOfertaNome;
    private String contextoLocal;
    private String etapa;
    private String etapaNome;
    private String objecao;
    private String objecaoNome;
    private String contatoVinculado;
    private String contatoVinculadoNome;

    // Construtor padrão
    public Agendamento() {
    }

    // Construtor com parâmetros principais
    public Agendamento(String id, String vencimento, String tipo, String contato,
                       String pessoa, String pessoaNome, String pessoaCpf,
                       String status, String descricao, String responsavel) {
        this.id = id;
        this.vencimento = vencimento;
        this.tipo = tipo;
        this.contato = contato;
        this.pessoa = pessoa;
        this.pessoaNome = pessoaNome;
        this.pessoaCpf = pessoaCpf;
        this.status = status;
        this.descricao = descricao;
        this.responsavel = responsavel;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getPessoa() {
        return pessoa;
    }

    public void setPessoa(String pessoa) {
        this.pessoa = pessoa;
    }

    public String getPessoaCodigo() {
        return pessoaCodigo;
    }

    public void setPessoaCodigo(String pessoaCodigo) {
        this.pessoaCodigo = pessoaCodigo;
    }

    public String getPessoaNome() {
        return pessoaNome;
    }

    public void setPessoaNome(String pessoaNome) {
        this.pessoaNome = pessoaNome;
    }

    public String getPessoaCpf() {
        return pessoaCpf;
    }

    public void setPessoaCpf(String pessoaCpf) {
        this.pessoaCpf = pessoaCpf;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusNome() {
        return statusNome;
    }

    public void setStatusNome(String statusNome) {
        this.statusNome = statusNome;
    }

    public String getProcesso() {
        return processo;
    }

    public void setProcesso(String processo) {
        this.processo = processo;
    }

    public String getProcessoNome() {
        return processoNome;
    }

    public void setProcessoNome(String processoNome) {
        this.processoNome = processoNome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoNome() {
        return tipoNome;
    }

    public void setTipoNome(String tipoNome) {
        this.tipoNome = tipoNome;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getFormaContato() {
        return formaContato;
    }

    public void setFormaContato(String formaContato) {
        this.formaContato = formaContato;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getResponsavelNome() {
        return responsavelNome;
    }

    public void setResponsavelNome(String responsavelNome) {
        this.responsavelNome = responsavelNome;
    }

    public String getOportunidade() {
        return oportunidade;
    }

    public void setOportunidade(String oportunidade) {
        this.oportunidade = oportunidade;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getContextoSubItem() {
        return contextoSubItem;
    }

    public void setContextoSubItem(String contextoSubItem) {
        this.contextoSubItem = contextoSubItem;
    }

    public String getCursoNome() {
        return cursoNome;
    }

    public void setCursoNome(String cursoNome) {
        this.cursoNome = cursoNome;
    }

    public String getMomentoConcluido() {
        return momentoConcluido;
    }

    public void setMomentoConcluido(String momentoConcluido) {
        this.momentoConcluido = momentoConcluido;
    }

    public String getMomento() {
        return momento;
    }

    public void setMomento(String momento) {
        this.momento = momento;
    }

    public String getProcessoSeletivo() {
        return processoSeletivo;
    }

    public void setProcessoSeletivo(String processoSeletivo) {
        this.processoSeletivo = processoSeletivo;
    }

    public String getProcessoSeletivoNome() {
        return processoSeletivoNome;
    }

    public void setProcessoSeletivoNome(String processoSeletivoNome) {
        this.processoSeletivoNome = processoSeletivoNome;
    }

    public String getContextoProcesso() {
        return contextoProcesso;
    }

    public void setContextoProcesso(String contextoProcesso) {
        this.contextoProcesso = contextoProcesso;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getUnidadeNome() {
        return unidadeNome;
    }

    public void setUnidadeNome(String unidadeNome) {
        this.unidadeNome = unidadeNome;
    }

    public String getRazaoOportunidade() {
        return razaoOportunidade;
    }

    public void setRazaoOportunidade(String razaoOportunidade) {
        this.razaoOportunidade = razaoOportunidade;
    }

    public String getRazaoOportunidadeNome() {
        return razaoOportunidadeNome;
    }

    public void setRazaoOportunidadeNome(String razaoOportunidadeNome) {
        this.razaoOportunidadeNome = razaoOportunidadeNome;
    }

    public String getStatusOportunidade() {
        return statusOportunidade;
    }

    public void setStatusOportunidade(String statusOportunidade) {
        this.statusOportunidade = statusOportunidade;
    }

    public String getStatusOportunidadeNome() {
        return statusOportunidadeNome;
    }

    public void setStatusOportunidadeNome(String statusOportunidadeNome) {
        this.statusOportunidadeNome = statusOportunidadeNome;
    }

    public String getLocalOferta() {
        return localOferta;
    }

    public void setLocalOferta(String localOferta) {
        this.localOferta = localOferta;
    }

    public String getLocalOfertaNome() {
        return localOfertaNome;
    }

    public void setLocalOfertaNome(String localOfertaNome) {
        this.localOfertaNome = localOfertaNome;
    }

    public String getContextoLocal() {
        return contextoLocal;
    }

    public void setContextoLocal(String contextoLocal) {
        this.contextoLocal = contextoLocal;
    }

    public String getEtapa() {
        return etapa;
    }

    public void setEtapa(String etapa) {
        this.etapa = etapa;
    }

    public String getEtapaNome() {
        return etapaNome;
    }

    public void setEtapaNome(String etapaNome) {
        this.etapaNome = etapaNome;
    }

    public String getObjecao() {
        return objecao;
    }

    public void setObjecao(String objecao) {
        this.objecao = objecao;
    }

    public String getObjecaoNome() {
        return objecaoNome;
    }

    public void setObjecaoNome(String objecaoNome) {
        this.objecaoNome = objecaoNome;
    }

    public String getContatoVinculado() {
        return contatoVinculado;
    }

    public void setContatoVinculado(String contatoVinculado) {
        this.contatoVinculado = contatoVinculado;
    }

    public String getContatoVinculadoNome() {
        return contatoVinculadoNome;
    }

    public void setContatoVinculadoNome(String contatoVinculadoNome) {
        this.contatoVinculadoNome = contatoVinculadoNome;
    }
}