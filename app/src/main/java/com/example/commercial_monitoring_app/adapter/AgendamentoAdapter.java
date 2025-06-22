package com.example.commercial_monitoring_app.adapter;

import static com.example.commercial_monitoring_app.OportunidadeDetailActivity.EXTRA_STATUS;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commercial_monitoring_app.AtividadeDetailActivity;
import com.example.commercial_monitoring_app.MyApp;
import com.example.commercial_monitoring_app.OportunidadeDetailActivity;
import com.example.commercial_monitoring_app.R;
import com.example.commercial_monitoring_app.model.Agendamento;
import com.example.commercial_monitoring_app.model.Client;
import com.example.commercial_monitoring_app.model.Oportunidade;
import com.example.commercial_monitoring_app.model.PersonalData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class AgendamentoAdapter extends RecyclerView.Adapter<AgendamentoAdapter.AgendamentoViewHolder> {

    private List<Agendamento> agendamentoList;
    private ActivityResultLauncher<Intent> activityLauncher;

    public AgendamentoAdapter(List<Agendamento> agendamentoList) {
        this.agendamentoList= agendamentoList;
    }

    public AgendamentoAdapter(List<Agendamento> agendamentoList, ActivityResultLauncher<Intent> launcher) {
        this.agendamentoList = agendamentoList;
        this.activityLauncher = launcher;
    }

    public static class AgendamentoViewHolder extends RecyclerView.ViewHolder {
        TextView atividadeTitle;
        TextView atividadeCliente;
        TextView atividadeData;
        TextView atividadeResponsavel;

        public AgendamentoViewHolder(View itemView) {
            super(itemView);
            atividadeTitle = itemView.findViewById(R.id.atividadeTitle);
            atividadeCliente = itemView.findViewById(R.id.atividadeCliente);
            atividadeData= itemView.findViewById(R.id.atividadeData);
            atividadeResponsavel = itemView.findViewById(R.id.atividadeResponsavel);
        }
    }

    @Override
    public AgendamentoAdapter.AgendamentoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_atividade, parent, false);
        return new AgendamentoAdapter.AgendamentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AgendamentoAdapter.AgendamentoViewHolder holder, int position) {
        Agendamento agendamento = agendamentoList.get(position);


        holder.atividadeTitle.setText(agendamento.getContato());
        holder.atividadeCliente.setText(agendamento.getPessoaNome());
        holder.atividadeData.setText(formatarData(agendamento.getVencimento()));
        holder.atividadeResponsavel.setText(agendamento.getResponsavelNome());



        holder.itemView.setOnClickListener(v -> {

            Context context = v.getContext();
            Intent intent = new Intent(context, AtividadeDetailActivity.class);
            intent.putExtra("atividade", agendamento.getContato());
            intent.putExtra("cliente_nome", agendamento.getPessoaNome());
            intent.putExtra("vencimento", agendamento.getVencimento());
            intent.putExtra("responsavel", agendamento.getResponsavelNome());
            intent.putExtra("descricao", agendamento.getDescricao());
            intent.putExtra("tipo_atividade", agendamento.getTipoNome());
            intent.putExtra("cliente_numero", agendamento.getTelefone());
            intent.putExtra("cliente_email", agendamento.getEmail());
            intent.putExtra("atividade_id", agendamento.getId());


            activityLauncher.launch(intent);
        });

    }

    @Override
    public int getItemCount() {
        return agendamentoList.size();
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

    public void updateData(List<Agendamento> newList) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new AgendamentoAdapter.AgendamentoDiffCallback(this.agendamentoList, newList));
        this.agendamentoList.clear();
        this.agendamentoList.addAll(newList);
        result.dispatchUpdatesTo(this);
    }


    static class AgendamentoDiffCallback extends DiffUtil.Callback {
        private final List<Agendamento> oldList;
        private final List<Agendamento> newList;

        AgendamentoDiffCallback(List<Agendamento> oldList, List<Agendamento> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId().equals(newList.get(newItemPosition).getId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }
}

