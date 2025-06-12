package com.example.commercial_monitoring_app.adapter;

import static com.example.commercial_monitoring_app.MyApp.getPersonalDataList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commercial_monitoring_app.MyApp;
import com.example.commercial_monitoring_app.OportunidadeDetailActivity;
import com.example.commercial_monitoring_app.R;
import com.example.commercial_monitoring_app.model.Client;
import com.example.commercial_monitoring_app.model.Oportunidade;
import com.example.commercial_monitoring_app.model.PersonalData;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OportunidadesAdapter extends RecyclerView.Adapter<OportunidadesAdapter.OportunidadeViewHolder> {

    private List<Oportunidade> oportunidades;
    private OnDeleteClickListener deleteListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position, Oportunidade oportunidade);
    }

    public OportunidadesAdapter(List<Oportunidade> oportunidades, OnDeleteClickListener deleteListener) {
        this.oportunidades = oportunidades;
        this.deleteListener = deleteListener;
    }

    public OportunidadesAdapter(List<Oportunidade> oportunidades) {
        this.oportunidades = oportunidades;
        this.deleteListener = null;
    }

    public static class OportunidadeViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView subtitle;
        TextView etapaStatus;
        // ImageView deleteIcon; // Commented out - delete icon removed from layout

        public OportunidadeViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.oportunidadeTitle);
            subtitle = itemView.findViewById(R.id.oportunidadeSubtitle);
            etapaStatus = itemView.findViewById(R.id.oportunidadeEtapaStatus);
            // deleteIcon = itemView.findViewById(R.id.deleteOportunidadeIcon); // Commented out - ID doesn't exist
        }
    }

    @Override
    public OportunidadeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_oportunidade, parent, false);
        return new OportunidadeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OportunidadeViewHolder holder, int position) {
        Oportunidade oportunidade = oportunidades.get(position);

        Client cliente = MyApp.getClientList().stream().filter(c -> String.valueOf(c.getId()).equals(oportunidade.getPessoa())).findFirst().orElse(null);
        String responsavel = "Sem responsável";





        holder.title.setText(oportunidade.getPessoaNome());
        holder.subtitle.setText("Agendamento: " + oportunidade.getAgendamentoNome() + " | Responsável: " + responsavel);
        holder.etapaStatus.setText("Etapa: " + oportunidade.getEtapaNome() );



        holder.itemView.setOnClickListener(v -> {
            final String id = String.valueOf(cliente.getId());
            List<PersonalData> personalDataList = MyApp.getPersonalDataList();
            Log.d("ADAPTER_DEBUG", "ID procurado: " + id);
            Log.d("ADAPTER_DEBUG", "Lista size: " + (personalDataList != null ? personalDataList.size() : "null"));
            String imagem = null;
            if (id != null && personalDataList != null) {
                Optional<String> imagemResult = personalDataList.stream()
                        .filter(Objects::nonNull)
                        .filter(personalData -> id.equals(personalData.getId()))
                        .map(PersonalData::getImagem)
                        .filter(img -> img != null && !img.isEmpty())
                        .findFirst();

                if (imagemResult.isPresent()) {
                    imagem = imagemResult.get();
                }
            }

            Log.d("ADAPTER_DEBUG", "Imagem url: " + (imagem));

            Context context = v.getContext();
            Intent intent = new Intent(context, OportunidadeDetailActivity.class);
            intent.putExtra(OportunidadeDetailActivity.EXTRA_OPORTUNIDADE_NAME, oportunidade.getPessoaNome());
            intent.putExtra("oportunidade_status", oportunidade.getStatusNome());
            intent.putExtra("oportunidade_etapa", oportunidade.getEtapaNome());
            intent.putExtra("oportunidade_origem", oportunidade.getOrigemNome());
            intent.putExtra("oportunidade_momento", oportunidade.getMomento());
            intent.putExtra("oportunidade_razao", oportunidade.getRazaoOportunidadeNome());
            if (cliente != null) {
                intent.putExtra("client_nome", cliente.getName());
                intent.putExtra("client_email", cliente.getEmail());
                intent.putExtra("client_telefone", cliente.getTelefone());
                intent.putExtra("client_cpf", cliente.getCpf());
                intent.putExtra("client_nascimento", cliente.getDataNascimento());
                intent.putExtra("client_endereco", cliente.getEndereco());
                intent.putExtra("client_bairro", cliente.getBairro());
                intent.putExtra("client_cep", cliente.getCep());
                intent.putExtra("client_image", imagem);
            }
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return oportunidades.size();
    }

    public void updateData(List<Oportunidade> newList) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new OportunidadeDiffCallback(this.oportunidades, newList));
        this.oportunidades.clear();
        this.oportunidades.addAll(newList);
        result.dispatchUpdatesTo(this);
    }

    static class OportunidadeDiffCallback extends DiffUtil.Callback {
        private final List<Oportunidade> oldList;
        private final List<Oportunidade> newList;

        OportunidadeDiffCallback(List<Oportunidade> oldList, List<Oportunidade> newList) {
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