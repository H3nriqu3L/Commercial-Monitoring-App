package com.example.commercial_monitoring_app;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.commercial_monitoring_app.R;
import com.example.commercial_monitoring_app.model.Oportunidade;

public class OportunidadeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_OPORTUNIDADE_ID = "extra_oportunidade_id";
    public static final String EXTRA_OPORTUNIDADE_NAME = "extra_oportunidade_name";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oportunidade_detail);

        // Setup toolbar with back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TextView title = findViewById(R.id.detailTitle);
        TextView status = findViewById(R.id.detailStatus);
        TextView etapa = findViewById(R.id.detailEtapa);
        TextView origem = findViewById(R.id.detailOrigem);
        TextView momento = findViewById(R.id.detailMomento);
        TextView razao = findViewById(R.id.detailRazao);
        Button toggleButton = findViewById(R.id.toggleAccordionButton);
        LinearLayout accordionContent = findViewById(R.id.accordionContent);

        // Opportunity data
        String oportunidadeName = getIntent().getStringExtra(EXTRA_OPORTUNIDADE_NAME);
        title.setText(oportunidadeName != null ? oportunidadeName : "Detalhes da Oportunidade");

        // Client data
        String clientNome = getIntent().getStringExtra("client_nome");
        String clientEmail = getIntent().getStringExtra("client_email");
        String clientTelefone = getIntent().getStringExtra("client_telefone");
        String clientCpf = getIntent().getStringExtra("client_cpf");
        String clientNascimento = getIntent().getStringExtra("client_nascimento");
        String clientEndereco = getIntent().getStringExtra("client_endereco");
        String clientBairro = getIntent().getStringExtra("client_bairro");
        String clientCep = getIntent().getStringExtra("client_cep");

        // Set client info in the accordion
        TextView clientNameView = findViewById(R.id.clientName);
        TextView clientContactView = findViewById(R.id.clientContact);
        TextView clientAddressView = findViewById(R.id.clientAddress);
        TextView clientDocView = findViewById(R.id.clientDocuments);

        if (clientNome != null) {
            clientNameView.setText(clientNome);
        }

        StringBuilder contactInfo = new StringBuilder();
        if (clientTelefone != null) contactInfo.append("Telefone: ").append(clientTelefone).append("\n");
        if (clientEmail != null) contactInfo.append("Email: ").append(clientEmail);
        clientContactView.setText(contactInfo.toString());

        StringBuilder addressInfo = new StringBuilder();
        if (clientEndereco != null) addressInfo.append("Endereço: ").append(clientEndereco).append("\n");
        if (clientBairro != null) addressInfo.append("Bairro: ").append(clientBairro).append("\n");
        if (clientCep != null) addressInfo.append("CEP: ").append(clientCep);
        clientAddressView.setText(addressInfo.toString());

        StringBuilder docInfo = new StringBuilder();
        if (clientCpf != null) docInfo.append("CPF: ").append(clientCpf).append("\n");
        if (clientNascimento != null) docInfo.append("Nascimento: ").append(clientNascimento);
        clientDocView.setText(docInfo.toString());

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}