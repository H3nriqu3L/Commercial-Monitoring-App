package com.example.commercial_monitoring_app.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.commercial_monitoring_app.R;
import com.example.commercial_monitoring_app.model.Client;

import java.util.List;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ClientViewHolder> {

    private List<Client> clients;
    private OnDeleteClickListener deleteListener;
    public interface OnDeleteClickListener{
        void onDeleteClick(int position, Client client);
    }

    public ClientsAdapter(List<Client> clients, OnDeleteClickListener deleteListener){
        this.clients = clients;
        this.deleteListener = deleteListener;
    }

    public static class ClientViewHolder extends RecyclerView.ViewHolder {
        TextView cardTitle;
        TextView cardDescription;
        ImageView deleteIcon;

        public ClientViewHolder(View itemView) {
            super(itemView);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardDescription = itemView.findViewById(R.id.cardDescription);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);

        }
    }

    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClientViewHolder holder, int position) {
        Client client = clients.get(position);
        holder.cardTitle.setText(client.getName());
        holder.cardDescription.setText(client.getEmail());

        // Existing card click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle card click
            }
        });

        // New delete icon click
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteListener != null) {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        deleteListener.onDeleteClick(currentPosition, client);
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return clients.size();
    }

    public void updateClientsList(List<Client> newClientsList) {
        Log.d("ClientsAdapter", "=== UPDATE CLIENTS LIST ===");
        Log.d("ClientsAdapter", "Current list size: " + this.clients.size());
        Log.d("ClientsAdapter", "New list size: " + newClientsList.size());

        // Log alguns nomes para ver se os dados estão corretos
        for (int i = 0; i < Math.min(3, newClientsList.size()); i++) {
            Log.d("ClientsAdapter", "Client " + i + ": " + newClientsList.get(i).getName());
        }

        this.clients.clear();
        this.clients.addAll(newClientsList);

        Log.d("ClientsAdapter", "After update, adapter size: " + this.clients.size());
        Log.d("ClientsAdapter", "Calling notifyDataSetChanged()");

        notifyDataSetChanged();

        Log.d("ClientsAdapter", "=== END UPDATE ===");
    }



}
