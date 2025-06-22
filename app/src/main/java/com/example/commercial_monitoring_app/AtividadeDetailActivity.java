package com.example.commercial_monitoring_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

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
            tipoAtividade = intent.getStringExtra("tipo_atividade");
            atividade = intent.getStringExtra("atividade");
            vencimento = formatarData(intent.getStringExtra("vencimento"));
            responsavel = intent.getStringExtra("responsavel");
            clienteNome = intent.getStringExtra("cliente_nome");
            clienteNumero = formatarTelefone(intent.getStringExtra("cliente_numero"));
            clienteEmail = intent.getStringExtra("cliente_email");
            descricao = intent.getStringExtra("descricao");


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
                // Formato não reconhecido
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

    private void performAtividadeConclude(){
        //atividadeId é o ID da atividade/agendamento que precisa ser marcada como concluida na API
        // TODO: MUDANCA NA API AQUI:


        setResult(RESULT_OK); //Avisa que a atividade foi concluida para a tela Home atualizar com as mudancas
        finish(); // volta pra tela anterior
    }

}