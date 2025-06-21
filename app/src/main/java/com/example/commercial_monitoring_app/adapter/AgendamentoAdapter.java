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

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

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

    public AgendamentoAdapter(List<Agendamento> agendamentoList) {
        this.agendamentoList= agendamentoList;
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
        holder.atividadeData.setText(formatarData(agendamento.getMomento()));
        holder.atividadeResponsavel.setText(agendamento.getResponsavelNome());



        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "tarefa clicada", Toast.LENGTH_SHORT).show();
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

