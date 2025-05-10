package com.example.commercial_monitoring_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class InteractionAdapter extends RecyclerView.Adapter<InteractionAdapter.InteractionViewHolder> {

    private ArrayList<HashMap<String, String>> interactions;

    public InteractionAdapter(ArrayList<HashMap<String, String>> interactions) {
        this.interactions = interactions;
    }

    @Override
    public InteractionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interaction, parent, false);
        return new InteractionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InteractionViewHolder holder, int position) {
        HashMap<String, String> interaction = interactions.get(position);
        holder.date.setText(interaction.get("date"));
        holder.type.setText(interaction.get("type"));
        holder.customer.setText(interaction.get("customer"));
    }

    @Override
    public int getItemCount() {
        return interactions.size();
    }

    public static class InteractionViewHolder extends RecyclerView.ViewHolder {

        TextView date, type, customer;

        public InteractionViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.interaction_date);
            type = itemView.findViewById(R.id.interaction_type);
            customer = itemView.findViewById(R.id.customer_name);
        }
    }
}
