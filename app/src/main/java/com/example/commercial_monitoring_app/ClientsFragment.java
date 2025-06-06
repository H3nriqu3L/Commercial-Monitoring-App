package com.example.commercial_monitoring_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commercial_monitoring_app.adapter.ClientsAdapter;
import com.example.commercial_monitoring_app.database.DatabaseHelper;
import com.example.commercial_monitoring_app.model.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientsFragment extends Fragment {
    private ClientsAdapter clientsAdapter;
    private RecyclerView recyclerView;


    public ClientsFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_clients, container, false);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.clientsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Client> clientsList = MyApp.getClientList();
        if (clientsList == null) {
            clientsList = new ArrayList<>();
            Log.w("ClientsFragment", "Client list null or empty");
        }

        clientsAdapter = new ClientsAdapter(clientsList, new ClientsAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position, Client client) {
                showDeleteConfirmation(position, client);
            }
        });
        recyclerView.setAdapter(clientsAdapter);


        recyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    // Delete confirmation dialog
    private void showDeleteConfirmation(int position, Client client) {
        if (getContext() == null) return;

        new AlertDialog.Builder(getContext())
                .setTitle("Excluir cliente")
                .setMessage("Deseja realmente excluir " + client.getName() + "?\nEssa ação removerá o cliente do banco de dados.")
                .setPositiveButton("Excluir", (dialog, which) -> {
                    deleteClient(position, client);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Handle actual deletion
    private void deleteClient(int position, Client client) {
        try {
           // MyApp.excluirOportunidadeEAtualizarLista(client.getId());
            if (getContext() != null) {
                Toast.makeText(getContext(), "Cliente deletado com sucesso!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("ClientsFragment", "Error deleting client", e);
            if (getContext() != null) {
                Toast.makeText(getContext(), "Erro ao deletar cliente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Database deletion method
    private void deleteFromDatabase(Client client) {
        try {
            DatabaseHelper dbHelper = DatabaseHelper.getInstance();

            // Delete from Client table where email matches
            String whereClause = "email = '" + client.getEmail() + "'";
            int deletedRows = dbHelper.delete("Client", whereClause);

            if (deletedRows > 0) {
                Log.d("ClientsFragment", "Successfully deleted client from database: " + client.getName() + " (Email: " + client.getEmail() + ")");
            } else {
                Log.w("ClientsFragment", "No client found with email: " + client.getEmail());
            }

        } catch (Exception e) {
            Log.e("ClientsFragment", "Error deleting client from database", e);
            throw e;
        }
    }




    public void refreshClientsList() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                List<Client> updatedList = MyApp.getClientList();
                Log.d("ClientsFragment", "Refreshing list with " + updatedList.size() + " clients");

                // new adapter
                clientsAdapter = new ClientsAdapter(updatedList, new ClientsAdapter.OnDeleteClickListener() {
                    @Override
                    public void onDeleteClick(int position, Client client) {
                        showDeleteConfirmation(position, client);
                    }
                });
                recyclerView.setAdapter(clientsAdapter);

                Log.d("ClientsFragment", "Adapter recreated and set");
            });
        }
    }




}
