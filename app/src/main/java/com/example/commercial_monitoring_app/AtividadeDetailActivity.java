package com.example.commercial_monitoring_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.commercial_monitoring_app.network.ApiService;
import com.example.commercial_monitoring_app.network.RetrofitClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class AtividadeDetailActivity extends AppCompatActivity {

    private TextView tvAtividadeId;
    private TextView tvTipoAtividade;
    private TextView tvAtividade;
    private TextView tvVencimento;
    private TextView tvResponsavel;
    private TextView tvClienteNome;
    private TextView tvClienteNumero;
    private TextView tvClienteEmail;
    private TextView tvDescricao;
    String atividadeId;
    String oportunidadeId;
    String pessoaId;
    String curso;
    String cursoNome;
    String razaoOportunidade;
    String razaoOportunidadeNome;

    String tipoAtividade;
    String atividade;
    String vencimento;
    String responsavel;
    String clienteNome;
    String clienteNumero;
    String clienteEmail;
    String descricao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade_detail);

        initializeViews();
        loadDataFromIntent();
    }

    private void initializeViews() {
        tvTipoAtividade = findViewById(R.id.tv_tipo_atividade);
        tvAtividade = findViewById(R.id.tv_atividade);
        tvVencimento = findViewById(R.id.tv_vencimento);
        tvResponsavel = findViewById(R.id.tv_responsavel);
        tvClienteNome = findViewById(R.id.tv_cliente_nome);
        tvClienteNumero = findViewById(R.id.tv_cliente_numero);
        tvClienteEmail = findViewById(R.id.tv_cliente_email);
        tvDescricao = findViewById(R.id.tv_descricao);
    }

    private void loadDataFromIntent() {
        Intent intent = getIntent();

        if (intent != null) {

            atividadeId = intent.getStringExtra("atividade_id");
            oportunidadeId = intent.getStringExtra("oportunidade");
            pessoaId = intent.getStringExtra("pessoa");
            tipoAtividade = intent.getStringExtra("tipo_atividade");
            atividade = intent.getStringExtra("atividade");
            vencimento = formatarData(intent.getStringExtra("vencimento"));
            responsavel = intent.getStringExtra("responsavel");
            clienteNome = intent.getStringExtra("cliente_nome");
            clienteNumero = formatarTelefone(intent.getStringExtra("cliente_numero"));
            clienteEmail = intent.getStringExtra("cliente_email");
            descricao = intent.getStringExtra("descricao");

            curso = intent.getStringExtra("curso");;
            cursoNome = intent.getStringExtra("cursoNome");;
            razaoOportunidade = intent.getStringExtra("razaoOportunidade");;
            razaoOportunidadeNome = intent.getStringExtra("razaoOportunidadeNome");;


            tvTipoAtividade.setText(tipoAtividade != null ? tipoAtividade : "-");
            tvAtividade.setText(atividade != null ? atividade : "-");
            tvVencimento.setText(vencimento != null ? vencimento : "-");
            tvResponsavel.setText(responsavel != null ? responsavel : "-");
            tvClienteNome.setText(clienteNome != null ? clienteNome : "-");
            tvClienteNumero.setText(clienteNumero != null ? clienteNumero : "-");
            tvClienteEmail.setText(clienteEmail != null ? clienteEmail : "-");
            tvDescricao.setText(descricao != null ? descricao : "Nenhuma descrição disponível");
        }
    }

    public void navigateBack(View view) {
        finish();
    }

    private String formatarData(String dataOriginal) {
        try {

            SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

            return formatoSaida.format(formatoEntrada.parse(dataOriginal));
        } catch (ParseException e) {
            e.printStackTrace();

            return dataOriginal;
        }
    }

    private String formatarTelefone(String telefoneOriginal) {
        try {
            String telefone = telefoneOriginal.replaceAll("[^+0-9]", "");

            // Verifica se o telefone tem o formato esperado (+55...)
            if (!telefone.startsWith("+55") || telefone.length() < 12) {
                return telefoneOriginal;
            }

            String codigoPais = telefone.substring(0, 3); // +55
            String codigoArea = telefone.substring(3, 5);  // 11
            String numero = telefone.substring(5);         // resto do número

            // Formata baseado no tamanho do número
            if (numero.length() == 8) {
                return String.format("%s (%s) %s-%s",
                        codigoPais,
                        codigoArea,
                        numero.substring(0, 4),
                        numero.substring(4));
            } else if (numero.length() == 9) {
                return String.format("%s (%s) %s-%s",
                        codigoPais,
                        codigoArea,
                        numero.substring(0, 5),
                        numero.substring(5));
            } else {
                return telefoneOriginal;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return telefoneOriginal;
        }
    }

    public void concluirAtividade(View view){
        new AlertDialog.Builder(AtividadeDetailActivity.this)
                .setTitle("Concluir Atividade")
                .setMessage("Deseja realmente confirmar que a atividade " + atividade + " foi concluída?")
                .setPositiveButton("Confirmar", (dialog, which) -> performAtividadeConclude())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private String formatarParaBackend(String dataFormatada) {
        try {
            SimpleDateFormat entrada = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            SimpleDateFormat backend = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return backend.format(entrada.parse(dataFormatada));
        } catch (ParseException e) {
            e.printStackTrace();
            return dataFormatada;
        }
    }

    private String extrairHora(String dataFormatada) {
        try {
            SimpleDateFormat entrada = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            SimpleDateFormat hora = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return hora.format(entrada.parse(dataFormatada));
        } catch (ParseException e) {
            e.printStackTrace();
            return "08:00";
        }
    }

    private String gerarDataVisao(String dataFormatada) {
        try {
            SimpleDateFormat entrada = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            SimpleDateFormat gmt = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT-0300 (Hora padrão de Brasília)'", Locale.ENGLISH);
            return gmt.format(entrada.parse(dataFormatada));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }



    private void performAtividadeConclude() {
        ApiService apiService = RetrofitClient.getApiService(ApiService.class, "https://crmufvgrupo3.apprubeus.com.br/");
        UserSession session = MyApp.getUserSession();

        if(!responsavel.equals(session.getUserName())){
            Toast.makeText(AtividadeDetailActivity.this, "Essa tarefa não é sua; é impossível concluí-la.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Map<String, String> payload = new HashMap<>();
        payload.put("tipoSalvar", "3");
        payload.put("id", atividadeId);
        payload.put("tipoAtividadeNome", tipoAtividade);
        payload.put("tipoAtividade", "4");
        payload.put("atividade", atividade);
        payload.put("contato", atividade);
        payload.put("vencimento", formatarParaBackend(vencimento));
        payload.put("hora", extrairHora(vencimento));
        payload.put("responsavel", responsavel);
        payload.put("pessoaNome", clienteNome);
        payload.put("pessoa", pessoaId);
        payload.put("pessoas[]", pessoaId);
        payload.put("descricao", descricao != null ? descricao : "");
        payload.put("concluido", "1");
        payload.put("duracao", "900"); // 15 minutos
        payload.put("duracaoVisao", "00:15:00");
        payload.put("tipo", "3");
        payload.put("formaContato", "1");
        payload.put("tipoVinculo", "pessoa");
        payload.put("responsavelUnico", String.valueOf(session.getUserID()));
        payload.put("agendamentoUnico", "true");
        payload.put("indiceFilaRequisicao", "1");

        payload.put("mostrarNotificacao", "");
        payload.put("tempoNotificacao", "");
        payload.put("tipoTempo", "");
        payload.put("alterarResumo", "1");
        payload.put("bloquearRazao", "");

        if (curso != null) {
            payload.put("curso", curso);
        }

        if(oportunidadeId != null){
            payload.put("oportunidades[]", oportunidadeId);
            payload.put("oportunidade[0][id]", oportunidadeId);
        }

        payload.put("oportunidade[0][curso]", curso != null ? curso : "");
        payload.put("oportunidade[0][cursoNome]", cursoNome != null ? cursoNome : "");
        payload.put("oportunidade[0][razaoOportunidade]", razaoOportunidade != null ? razaoOportunidade : "1");
        payload.put("oportunidade[0][razaoOportunidadeNome]", razaoOportunidadeNome != null ? razaoOportunidadeNome : "Não contactado");

        payload.put("dataVisao", gerarDataVisao(vencimento));

        Log.d("AddClientActivity", "payload: " + payload.toString());

        Call<okhttp3.ResponseBody> call = apiService.concluirAtividadeCadastro(payload);
        call.enqueue(new retrofit2.Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<okhttp3.ResponseBody> call, retrofit2.Response<okhttp3.ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseBody = response.body() != null ? response.body().string() : "Resposta vazia";
                        Log.d("API_RESPONSE", responseBody);


                        setResult(RESULT_OK);
                        finish();


                    } else {
                        showError("Erro ao concluir atividade. Código: " + response.code());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Erro ao processar resposta: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showError("Erro de rede: " + t.getMessage());
            }
        });
    }

    private void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Erro")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}