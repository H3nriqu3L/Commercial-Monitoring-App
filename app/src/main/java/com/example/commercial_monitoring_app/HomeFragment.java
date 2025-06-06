// HomeFragment.java
package com.example.commercial_monitoring_app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class HomeFragment extends Fragment {

    private InteractionAdapter adapter;
    private ArrayList<HashMap<String, String>> interactions = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        setupBarChart(view);
        setupPieChart(view);

        RecyclerView recyclerView = view.findViewById(R.id.interactions_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new InteractionAdapter(interactions);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void setupBarChart(View view) {
        BarChart generalBarChart = view.findViewById(R.id.generalBarChart);
        ArrayList<BarEntry> entriesGeneral = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            entriesGeneral.add(new BarEntry(i, (i + 1) * 10));
        }
        int barColor = ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark);
        BarDataSet generalDataSet = new BarDataSet(entriesGeneral, "Vendas Gerais");
        generalDataSet.setColor(barColor);
        generalDataSet.setValueTextSize(12f);
        BarData barData = new BarData(generalDataSet);
        generalBarChart.setData(barData);
        Description generalDesc = new Description();
        generalDesc.setText("Vendas Totais");
        generalBarChart.setDescription(generalDesc);
        generalBarChart.invalidate();
    }

    private void setupPieChart(View view) {
        PieChart statusPieChart = view.findViewById(R.id.statusPieChart);
        ArrayList<PieEntry> statusEntries = new ArrayList<>();
        statusEntries.add(new PieEntry(40f, "Potencial"));
        statusEntries.add(new PieEntry(35f, "Interessado"));
        statusEntries.add(new PieEntry(25f, "Inscrito Parcial"));
        PieDataSet pieDataSet = new PieDataSet(statusEntries, "Status dos Clientes");
        pieDataSet.setColors(new int[]{
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light
        }, requireContext());
        pieDataSet.setValueTextSize(14f);
        pieDataSet.setValueTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));
        PieData pieData = new PieData(pieDataSet);
        statusPieChart.setData(pieData);
        Description pieDesc = new Description();
        pieDesc.setText("Distribuição de Status");
        statusPieChart.setDescription(pieDesc);
        statusPieChart.setUsePercentValues(true);
        statusPieChart.setEntryLabelColor(ContextCompat.getColor(requireContext(), android.R.color.black));
        statusPieChart.invalidate();
    }


}
