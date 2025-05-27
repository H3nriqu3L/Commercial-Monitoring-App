package com.example.commercial_monitoring_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        // Sample data - replace with your actual data source
        List<Client> clientsList = new ArrayList<>();
        clientsList.add(new Client("Maria Silva", "maria.silva@email.com", "(11) 98765-4321", "123.456.789-01", "15/03/1985"));
        clientsList.add(new Client("João Santos", "joao.santos@gmail.com", "(21) 99876-5432", "987.654.321-09", "22/07/1990"));
        clientsList.add(new Client("Ana Costa", "ana.costa@hotmail.com", "(85) 91234-5678", "456.789.123-45", "08/12/1978"));

        // Add as many clients as you need

        clientsAdapter = new ClientsAdapter(clientsList);
        recyclerView.setAdapter(clientsAdapter);

        // Optional: Add item decoration for spacing between cards
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

}
