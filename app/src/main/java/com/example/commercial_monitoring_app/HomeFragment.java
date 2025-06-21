// HomeFragment.java
package com.example.commercial_monitoring_app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commercial_monitoring_app.adapter.AgendamentoAdapter;
import com.example.commercial_monitoring_app.adapter.OportunidadesAdapter;
import com.example.commercial_monitoring_app.model.Agendamento;
import com.example.commercial_monitoring_app.model.Oportunidade;
import com.example.commercial_monitoring_app.model.Pessoa;
import com.example.commercial_monitoring_app.network.ApiService;
import com.example.commercial_monitoring_app.network.ResponseWrapper;
import com.example.commercial_monitoring_app.network.RetrofitClient;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView oportunidadesGanhas;
    private TextView oportunidadesAbertas;
    private RecyclerView recyclerView;
    private AgendamentoAdapter agendamentoAdapter;
    private List<Agendamento> agendamentoList;
    private List<Agendamento> filteredAgendamento;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla o layout do fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa as views
        initViews(view);

        // Configura o RecyclerView
        setupRecyclerView();

        // Carrega os dados
        loadData();
    }

    private void initViews(View view) {
        oportunidadesGanhas = view.findViewById(R.id.oportunidadesGanhas);
        oportunidadesAbertas = view.findViewById(R.id.oportunidadesAbertas);
        recyclerView = view.findViewById(R.id.atividadesRecyclerView);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa a lista de agendamentos
        agendamentoList = MyApp.getAgendamentoList();
        Log.d("HomeFragment", "Tamanho da lista original: " + agendamentoList.size());

        if (agendamentoList == null) {
            agendamentoList = new ArrayList<>();
            Log.w("HomeFragment", "AgendamentoList is null or empty");
        }


        filteredAgendamento = new ArrayList<>(agendamentoList);
        agendamentoAdapter = new AgendamentoAdapter(filteredAgendamento);

        recyclerView.setAdapter(agendamentoAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void loadData() {
        loadOportunidadesGanhas();
        loadOportunidadesAbertas();
    }

    private void loadOportunidadesGanhas() {
        // Procura quantidade oportunidades ganhas na API e seta valor
        oportunidadesGanhas.setText("15");
    }

    private void loadOportunidadesAbertas() {
        // Procura quantidade oportunidades abertas na API e seta valor
        oportunidadesAbertas.setText("13");
    }
}