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

import com.bumptech.glide.Glide;
import com.example.commercial_monitoring_app.R;
import com.example.commercial_monitoring_app.model.Oportunidade;

import de.hdodenhof.circleimageview.CircleImageView;

public class OportunidadeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_OPORTUNIDADE_ID = "extra_oportunidade_id";
    public static final String EXTRA_OPORTUNIDADE_NAME = "extra_oportunidade_name";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oportunidade_detail);

        Button toggleButton = findViewById(R.id.toggleAccordionButton);
        LinearLayout accordionContent = findViewById(R.id.accordionContent);

        // Opportunity data
        String oportunidadeName = getIntent().getStringExtra(EXTRA_OPORTUNIDADE_NAME);
        //title.setText(oportunidadeName != null ? oportunidadeName : "Detalhes da Oportunidade");

        // Client data
        String clientNome = getIntent().getStringExtra("client_nome");
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
            contactInfo.append("Telefone: ").append(clientTelefone).append("\n");
            clientProfilePhone.setText(clientTelefone);
        }
        if (clientEmail != null){
            contactInfo.append("Email: ").append(clientEmail);
            clientProfileMail.setText(clientEmail);
        }
        clientContactView.setText(contactInfo.toString());

        StringBuilder docInfo = new StringBuilder();
        if (clientCpf != null) docInfo.append("CPF: ").append(clientCpf).append("\n");
        if (clientNascimento != null) docInfo.append("Nascimento: ").append(clientNascimento);
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
            extraDetail3.setText(oportunidadeContactado != null ? "Contatado?: " + oportunidadeContactado : "Sem informações de contato");

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
}