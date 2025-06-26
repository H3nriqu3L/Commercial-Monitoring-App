package com.example.commercial_monitoring_app;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.commercial_monitoring_app.R;
import com.example.commercial_monitoring_app.model.Client;
import com.example.commercial_monitoring_app.model.NavigationResponse;
import com.example.commercial_monitoring_app.model.Oportunidade;
import com.example.commercial_monitoring_app.network.ApiService;
import com.example.commercial_monitoring_app.network.ResponseWrapper;
import com.example.commercial_monitoring_app.network.RetrofitClient;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OportunidadeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_OPORTUNIDADE_ID = "extra_oportunidade_id";
    public static final String EXTRA_OPORTUNIDADE_NAME = "extra_oportunidade_name";
    public static final String EXTRA_STATUS = "client_status";

    private String clientId;
    private TextView statusText;
    private LinearLayout defaultStatusActions;
    private LinearLayout nonDefaultStatusActions;
    private int oportunidadeId;
    private int currentStatus = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oportunidade_detail);

        Button toggleButton = findViewById(R.id.toggleAccordionButton);
        LinearLayout accordionContent = findViewById(R.id.accordionContent);

        statusText = findViewById(R.id.status_text);
        defaultStatusActions = findViewById(R.id.default_status_actions);
        nonDefaultStatusActions = findViewById(R.id.non_default_status_actions);

        String oportunidadeName = getIntent().getStringExtra(EXTRA_OPORTUNIDADE_NAME);
        oportunidadeId = Integer.parseInt(getIntent().getStringExtra(EXTRA_OPORTUNIDADE_ID));
        currentStatus = Integer.parseInt(getIntent().getStringExtra(EXTRA_STATUS));

        String clientNome = getIntent().getStringExtra("client_nome");
        clientId = getIntent().getStringExtra("client_id");

        String clientEmail = getIntent().getStringExtra("client_email");
        String clientTelefone = getIntent().getStringExtra("client_telefone");
        String clientCpf = getIntent().getStringExtra("client_cpf");
        String clientNascimento = getIntent().getStringExtra("client_nascimento");

        //Oportunidade data
        String oportunidadeResponsavel = getIntent().getStringExtra("oportunidade_responsavel");
        String oportunidadeCurso = getIntent().getStringExtra("oportunidade_curso");
        String oportunidadeContactado = getIntent().getStringExtra("oportunidade_contactado");
        String oportunidadeEtapa = getIntent().getStringExtra("oportunidade_etapa");

        //TextViews
        TextView clientNameView = findViewById(R.id.clientName);
        TextView clientContactView = findViewById(R.id.clientContact);
        TextView clientDocView = findViewById(R.id.clientDocuments);
        TextView clientProfileName = findViewById(R.id.profile_name);
        TextView clientProfileMail = findViewById(R.id.profile_email);
        TextView clientProfilePhone = findViewById(R.id.profile_phone);
        CircleImageView clientProfileImage = findViewById(R.id.profile_image);

        TextView extraDetail1 = findViewById(R.id.extraDetail1);
        TextView extraDetail2 = findViewById(R.id.extraDetail2);
        TextView extraDetail3 = findViewById(R.id.extraDetail3);
        TextView extraDetail4 = findViewById(R.id.extraDetail4);



        String imagemUrl = getIntent().getStringExtra("client_image");
        if (imagemUrl != null && !imagemUrl.isEmpty()) {

            Glide.with(this)
                    .load(imagemUrl)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(clientProfileImage);
        }

        if (clientNome != null) {
            clientNameView.setText(clientNome);
            clientProfileName.setText(clientNome);
        }

        StringBuilder contactInfo = new StringBuilder();
        if (clientTelefone != null) {
            contactInfo.append("Telefone: ").append(formatarTelefone(clientTelefone)).append("\n");
            clientProfilePhone.setText(formatarTelefone(clientTelefone));
        }
        if (clientEmail != null){
            contactInfo.append("Email: ").append(clientEmail);
            clientProfileMail.setText(clientEmail);
        }
        clientContactView.setText(contactInfo.toString());

        StringBuilder docInfo = new StringBuilder();
        if (clientCpf != null) docInfo.append("CPF: ").append(clientCpf).append("\n");
        if (clientNascimento != null) docInfo.append("Nascimento: ").append(formatarDataNascimento(clientNascimento));
        clientDocView.setText(docInfo.toString());


        // Lógica para mostrar detalhes extras
        if (oportunidadeEtapa != null &&
                !oportunidadeEtapa.equals("Em Alerta") &&
                !oportunidadeEtapa.equals("Evadido") &&
                !oportunidadeEtapa.equals("Alunos Regulares")) {

            // Define extraDetail1 com oportunidadeEtapa
            extraDetail1.setText(oportunidadeEtapa != null ? "Etapa: " + oportunidadeEtapa : "Etapa: Sem Etapa Definida");

            // Define extraDetail2 com oportunidadeCurso
            extraDetail2.setText(oportunidadeCurso != null ? "Curso: " + oportunidadeCurso : "Curso: Curso não informado");

            // Define extraDetail3 com oportunidadeContactado
            extraDetail3.setText(oportunidadeContactado != null ? "Contactado? " + oportunidadeContactado : "Sem informações de contato");

            extraDetail4.setText(oportunidadeResponsavel != null ? "Responsável: " + oportunidadeResponsavel : "Sem responsável");

        } else {
            extraDetail1.setText(oportunidadeEtapa != null ? "Etapa: " + oportunidadeEtapa : "Etapa: Sem Etapa Definida");

            extraDetail2.setText(oportunidadeContactado != null ? "Contatado?: " + oportunidadeContactado : "Sem informações de contato");

            extraDetail3.setText(oportunidadeResponsavel != null ? "Responsável: " + oportunidadeResponsavel : "Sem responsável");
            extraDetail4.setText("");
        }

        // Toggle button functionality
        toggleButton.setOnClickListener(v -> {
            if (accordionContent.getVisibility() == View.GONE) {
                accordionContent.setVisibility(View.VISIBLE);
                toggleButton.setText("Ocultar detalhes");
            } else {
                accordionContent.setVisibility(View.GONE);
                toggleButton.setText("Ver mais detalhes");
            }
        });

        updateStatusUI();
    }

    private void updateStatus(int newStatus) {
        currentStatus = newStatus;
        updateStatusUI();
    }

    private void updateStatusUI() {
        switch (currentStatus) {
            case 1: // Active
                statusText.setText("Status: Ativo");
                defaultStatusActions.setVisibility(View.VISIBLE);
                nonDefaultStatusActions.setVisibility(View.GONE);
                break;
            case 2: // Won
                statusText.setText("Status: Ganho");
                defaultStatusActions.setVisibility(View.GONE);
                nonDefaultStatusActions.setVisibility(View.VISIBLE);
                break;
            case 3: // Lost
                statusText.setText("Status: Perdido");
                defaultStatusActions.setVisibility(View.GONE);
                nonDefaultStatusActions.setVisibility(View.VISIBLE);
                break;
            default:
                statusText.setText("Status: Desconhecido");
                defaultStatusActions.setVisibility(View.VISIBLE);
                nonDefaultStatusActions.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void navigateBack(View view) {
        finish();
    }


    public void callClient(View view) {
        String clientTelefone = getIntent().getStringExtra("client_telefone");

        if (clientTelefone != null && !clientTelefone.trim().isEmpty()) {
            String phoneNumber = clientTelefone.replaceAll("[^0-9+]", "");

            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));

            try {
                startActivity(callIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Não foi possível encontrar um aplicativo para fazer ligações", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Número de telefone não disponível", Toast.LENGTH_SHORT).show();
        }
    }

    public void emailClient(View view) {
        String clientEmail = getIntent().getStringExtra("client_email");
        String clientNome = getIntent().getStringExtra("client_nome");
        String oportunidadeName = getIntent().getStringExtra(EXTRA_OPORTUNIDADE_NAME);

        if (clientEmail != null && !clientEmail.trim().isEmpty()) {

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + clientEmail));

            String subject = "Contato - ";
            if (oportunidadeName != null && !oportunidadeName.trim().isEmpty()) {
                subject += oportunidadeName;
            } else {
                subject += "Oportunidade";
            }
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

            String emailBody = "";
            if (clientNome != null && !clientNome.trim().isEmpty()) {
                emailBody = "Olá " + clientNome + ",\n\n";
            } else {
                emailBody = "Olá,\n\n";
            }
            emailBody += "Entrando em contato referente à sua oportunidade.\n\n";
            emailBody += "Atenciosamente,\n[Seu nome]";

            emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

            try {
                startActivity(Intent.createChooser(emailIntent, "Enviar email"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Não foi possível encontrar um aplicativo de email", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Email do cliente não disponível", Toast.LENGTH_SHORT).show();
        }
    }

    public void assignClient(View view){
        String clientNome = getIntent().getStringExtra("client_nome");

        new AlertDialog.Builder(OportunidadeDetailActivity.this)
                .setTitle("Tornar-se Responsável")
                .setMessage("Deseja realmente se tornar responsável da oportunidade de " + clientNome + "?")
                .setPositiveButton("Confirmar", (dialog, which) -> performClientAssignment())
                .setNegativeButton("Cancelar", null)
                .show();

    }

    public void setClientAsWon(View view){
        String clientNome = getIntent().getStringExtra("client_nome");

        new AlertDialog.Builder(OportunidadeDetailActivity.this)
                .setTitle("Oportunidade Ganha")
                .setMessage("Deseja realmente confirmar que a oportunidade de " + clientNome + " foi ganha?")
                .setPositiveButton("Confirmar", (dialog, which) -> performClientWon())
                .setNegativeButton("Cancelar", null)
                .show();

    }
    public void setClientAsLost(View view){
        String clientNome = getIntent().getStringExtra("client_nome");

        new AlertDialog.Builder(OportunidadeDetailActivity.this)
                .setTitle("Oportunidade Perdida")
                .setMessage("Deseja realmente confirmar que a oportunidade de " + clientNome + " foi perdida?")
                .setPositiveButton("Confirmar", (dialog, which) -> performClientLost())
                .setNegativeButton("Cancelar", null)
                .show();

    }

    private void performClientAssignment() {
        try {
            UserSession session = UserSession.getInstance(OportunidadeDetailActivity.this);
            int userIdResponsavel = session.getUserID();
            Log.e("API_ASSIGN", "UserId: " + userIdResponsavel + "Usermail: " + session.getUserEmail());

            if (oportunidadeId == -1 || userIdResponsavel == -1 || clientId == null) {
                Toast.makeText(this, "Dados inválidos para alteração de responsável", Toast.LENGTH_SHORT).show();
                return;
            }

            String responsavelNome = MyApp.getCurrentresponsavelUser().getName();
            ApiService apiService = RetrofitClient.getApiService(ApiService.class, "https://crmufvgrupo3.apprubeus.com.br/");

            Call<Void> call = apiService.alterarResponsavel(
                    oportunidadeId,
                    userIdResponsavel,
                    responsavelNome,
                    1
            );

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Agora chama o atualizarDadosOportunidade
                        Call<Void> updateCall = apiService.atualizarDadosOportunidade(
                                oportunidadeId,
                                Integer.parseInt(clientId),
                                1
                        );

                        updateCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(OportunidadeDetailActivity.this,
                                            "Responsável alterado e dados atualizados com sucesso",
                                            Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finishWithClientRefresh();
                                } else {
                                    handleUpdateError(response);
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(OportunidadeDetailActivity.this,
                                        "Responsável alterado mas erro ao atualizar dados: " + t.getMessage(),
                                        Toast.LENGTH_LONG).show();
                                Log.e("API_ERROR", "Update data failure", t);
                            }
                        });

                    } else {
                        try {
                            String errorMsg = "Erro ao alterar responsável";
                            if (response.errorBody() != null) {
                                errorMsg += ": " + response.errorBody().string();
                            }
                            Toast.makeText(OportunidadeDetailActivity.this,
                                    errorMsg,
                                    Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Toast.makeText(OportunidadeDetailActivity.this,
                                    "Erro ao processar resposta do servidor",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(OportunidadeDetailActivity.this,
                            "Falha na conexão: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR", "Network failure", t);
                }
            });

        } catch (Exception e) {
            Toast.makeText(this,
                    "Erro inesperado: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.e("API_ERROR", "Unexpected error", e);
        }
    }


    private void finishWithClientRefresh() {
        ApiService apiService = RetrofitClient.getApiService(ApiService.class, "https://crmufvgrupo3.apprubeus.com.br/");
        Call<ResponseWrapper<Client>> call = apiService.listarPessoas();

        call.enqueue(new Callback<ResponseWrapper<Client>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<Client>> call, Response<ResponseWrapper<Client>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().dados != null) {
                    MyApp.setClientList(response.body().dados.dados);
                }
                finish();
            }

            @Override
            public void onFailure(Call<ResponseWrapper<Client>> call, Throwable t) {
                Toast.makeText(OportunidadeDetailActivity.this,
                        "Operação concluída, mas erro ao atualizar lista: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    private void performClientWon() {
        try {
            if (oportunidadeId == -1 || clientId == null) {
                Toast.makeText(this, "Dados inválidos", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitClient.getApiService(ApiService.class, "https://crmufvgrupo3.apprubeus.com.br/");

            // First call - update status
            Call<Void> statusCall = apiService.alterarStatusOportunidade(
                    oportunidadeId,  // opportunity ID
                    1,              // origem
                    2,              // status (2 = won)
                    2,              // statusCliente
                    1               // indiceFilaRequisicao
            );

            statusCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // After successful status update, call the second API
                        Call<Void> updateCall = apiService.atualizarDadosOportunidade(
                                oportunidadeId,
                                Integer.parseInt(clientId),
                                1
                        );

                        updateCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(OportunidadeDetailActivity.this,
                                            "Oportunidade ganha e dados atualizados com sucesso",
                                            Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finishWithClientRefresh();
                                } else {
                                    handleUpdateError(response);
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(OportunidadeDetailActivity.this,
                                        "Erro ao atualizar dados: " + t.getMessage(),
                                        Toast.LENGTH_LONG).show();
                                Log.e("API_ERROR", "Update data failure", t);
                            }
                        });
                    } else {
                        handleStatusError(response);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(OportunidadeDetailActivity.this,
                            "Falha na conexão: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR", "Network failure", t);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this,
                    "Erro inesperado: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.e("API_ERROR", "Unexpected error", e);
        }
    }

    private void performClientLost() {
        try {
            if (oportunidadeId == -1 || clientId == null) {
                Toast.makeText(this, "Dados inválidos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create an AlertDialog to get the loss reason from user
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Motivo da Perda");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("Confirmar", (dialog, which) -> {
                String motivoPerda = input.getText().toString().trim();
                if (motivoPerda.isEmpty()) {
                    Toast.makeText(this, "Por favor, informe o motivo da perda", Toast.LENGTH_SHORT).show();
                    return;
                }
                setResult(RESULT_OK);
                executeLostOpportunityCall(motivoPerda);
            });
            builder.setNegativeButton("Cancelar", null);
            builder.show();

        } catch (Exception e) {
            Toast.makeText(this,
                    "Erro inesperado: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.e("API_ERROR", "Unexpected error", e);
        }
    }

    private void executeLostOpportunityCall(String motivoPerda) {
        ApiService apiService = RetrofitClient.getApiService(ApiService.class, "https://crmufvgrupo3.apprubeus.com.br/");

        // First call - update status to lost
        Call<Void> statusCall = apiService.alterarStatusOportunidadePerdida(
                oportunidadeId,      // id
                1,                  // origem
                1,                  // statusCliente
                1,                  // processo
                0,                  // possuiConcorrente (0 = não)
                motivoPerda,        // observacaoPerda (user input)
                3,                  // status (3 = lost)
                1,                  // objecao (1 = sim)
                "objecao padrao",   // objecaoNome
                1                   // indiceFilaRequisicao
        );

        statusCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // After successful status update, call the second API
                    Call<Void> updateCall = apiService.atualizarDadosOportunidade(
                            oportunidadeId,
                            Integer.parseInt(clientId),
                            1
                    );

                    updateCall.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(OportunidadeDetailActivity.this,
                                        "Oportunidade marcada como perdida e dados atualizados",
                                        Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finishWithClientRefresh();
                            } else {
                                handleUpdateError(response);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(OportunidadeDetailActivity.this,
                                    "Status atualizado mas erro ao atualizar dados: " + t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Log.e("API_ERROR", "Update data failure", t);
                        }
                    });
                } else {
                    handleStatusError(response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(OportunidadeDetailActivity.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e("API_ERROR", "Network failure", t);
            }
        });
    }

    public void returnToOriginalStatus(View view) {
        String clientNome = getIntent().getStringExtra("client_nome");

        new AlertDialog.Builder(OportunidadeDetailActivity.this)
                .setTitle("Retornar Status Original")
                .setMessage("Deseja realmente retornar o status da oportunidade de " + clientNome + " para o original?")
                .setPositiveButton("Confirmar", (dialog, which) -> performReturnToOriginalStatus())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void performReturnToOriginalStatus() {
        try {
            if (oportunidadeId == -1 || clientId == null) {
                Toast.makeText(this, "Dados inválidos", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitClient.getApiService(ApiService.class, "https://crmufvgrupo3.apprubeus.com.br/");

            // First call - update status to original (1)
            Call<Void> statusCall = apiService.alterarStatusOportunidade(
                    oportunidadeId,  // opportunity ID
                    1,              // origem
                    1,              // status (1 = original/active)
                    1,              // statusCliente
                    1               // indiceFilaRequisicao
            );

            statusCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // After successful status update, call the second API
                        Call<Void> updateCall = apiService.atualizarDadosOportunidade(
                                oportunidadeId,
                                Integer.parseInt(clientId),
                                1
                        );

                        updateCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(OportunidadeDetailActivity.this,
                                            "Status retornado ao original e dados atualizados com sucesso",
                                            Toast.LENGTH_SHORT).show();
                                    finishWithClientRefresh();
                                } else {
                                    handleUpdateError(response);
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(OportunidadeDetailActivity.this,
                                        "Erro ao atualizar dados: " + t.getMessage(),
                                        Toast.LENGTH_LONG).show();
                                Log.e("API_ERROR", "Update data failure", t);
                            }
                        });
                    } else {
                        handleStatusError(response);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(OportunidadeDetailActivity.this,
                            "Falha na conexão: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR", "Network failure", t);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this,
                    "Erro inesperado: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.e("API_ERROR", "Unexpected error", e);
        }
    }

    private void handleStatusError(Response<Void> response) {
        try {
            String errorMsg = "Erro ao atualizar status";
            if (response.errorBody() != null) {
                errorMsg += ": " + response.errorBody().string();
            }
            Toast.makeText(OportunidadeDetailActivity.this,
                    errorMsg,
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(OportunidadeDetailActivity.this,
                    "Erro ao processar resposta de status",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void handleUpdateError(Response<Void> response) {
        try {
            String errorMsg = "Status atualizado mas erro ao atualizar dados";
            if (response.errorBody() != null) {
                errorMsg += ": " + response.errorBody().string();
            }
            Toast.makeText(OportunidadeDetailActivity.this,
                    errorMsg,
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(OportunidadeDetailActivity.this,
                    "Erro ao processar resposta de atualização",
                    Toast.LENGTH_LONG).show();
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

    private String formatarDataNascimento(String dataNascimentoOriginal) {
        try {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            return formatoSaida.format(formatoEntrada.parse(dataNascimentoOriginal));
        } catch (ParseException e) {
            e.printStackTrace();
            return dataNascimentoOriginal;
        }
    }




}