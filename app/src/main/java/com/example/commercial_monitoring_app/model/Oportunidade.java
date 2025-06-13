package com.example.commercial_monitoring_app.model;

public class Oportunidade {
    private String id;
    private String status;
    private String origem;
    private String etapa;
    private String pessoa;
    private String pessoaNome;
    private String momento;
    private String ultimaAlteracaoEtapa;
    private String razaoOportunidade;
    private String etapaNome;
    private String statusNome;
    private String origemNome;
    private String razaoOportunidadeNome;
    private String agendamentoNome;
    private String dias;

    private String responsavelNome;

    private String cursoNome;
    private CamposPersonalizados camposPersonalizados;

    // Getters
    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String  getResponsavelNome(){return responsavelNome;}

    public String getOrigem() {
        return origem;
    }

    public String getEtapa() {
        return etapa;
    }

    public String getPessoa() {
        return pessoa;
    }

    public String getPessoaNome() {
        return pessoaNome;
    }

    public String getMomento() {
        return momento;
    }

    public String getCursoNome(){return cursoNome;}

    public String getUltimaAlteracaoEtapa() {
        return ultimaAlteracaoEtapa;
    }

    public String getRazaoOportunidade() {
        return razaoOportunidade;
    }

    public String getEtapaNome() {
        return etapaNome;
    }

    public String getStatusNome() {
        return statusNome;
    }

    public String getOrigemNome() {
        return origemNome;
    }

    public String getRazaoOportunidadeNome() {
        return razaoOportunidadeNome;
    }

    public String getAgendamentoNome() {
        return agendamentoNome;
    }

    public String getDias() {
        return dias;
    }

    public CamposPersonalizados getCamposPersonalizados() {
        return camposPersonalizados;
    }

    public static class CamposPersonalizados {
        private String campopersonalizado_5_compl_proc;
        private String campopersonalizado_1_compl_proc;
        private String campopersonalizado_2_compl_proc;
        private String campopersonalizado_3_compl_proc;
        private String campopersonalizado_4_compl_proc;

        // Getters
        public String getCampopersonalizado_5_compl_proc() {
            return campopersonalizado_5_compl_proc;
        }

        public String getCampopersonalizado_1_compl_proc() {
            return campopersonalizado_1_compl_proc;
        }

        public String getCampopersonalizado_2_compl_proc() {
            return campopersonalizado_2_compl_proc;
        }

        public String getCampopersonalizado_3_compl_proc() {
            return campopersonalizado_3_compl_proc;
        }

        public String getCampopersonalizado_4_compl_proc() {
            return campopersonalizado_4_compl_proc;
        }
    }
}
