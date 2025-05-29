package com.example.commercial_monitoring_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commercial_monitoring_app.adapter.ClientsAdapter;
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

        clientsAdapter = new ClientsAdapter(clientsList);
        recyclerView.setAdapter(clientsAdapter);


        recyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }


    public void refreshClientsList() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                List<Client> updatedList = MyApp.getClientList();
                Log.d("ClientsFragment", "Refreshing list with " + updatedList.size() + " clients");

                // new adapter
                clientsAdapter = new ClientsAdapter(updatedList);
                recyclerView.setAdapter(clientsAdapter);

                Log.d("ClientsFragment", "Adapter recreated and set");
            });
        }
    }




}
