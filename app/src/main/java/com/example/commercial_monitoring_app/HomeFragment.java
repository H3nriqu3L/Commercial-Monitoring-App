package com.example.commercial_monitoring_app;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla o layout do fragmento
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Gráfico de barras - Vendas Gerais
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

        generalBarChart.invalidate(); // Atualizar gráfico de barras

        // Gráfico de pizza - Status de Clientes
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

        statusPieChart.invalidate(); // Atualizar gráfico de pizza

        // Sample data para o RecyclerView
        ArrayList<HashMap<String, String>> interactions = new ArrayList<>();

        HashMap<String, String> interaction1 = new HashMap<>();
        interaction1.put("date", "2025-05-09");
        interaction1.put("type", "Call");
        interaction1.put("customer", "Customer A");
        interactions.add(interaction1);

        HashMap<String, String> interaction2 = new HashMap<>();
        interaction2.put("date", "2025-05-08");
        interaction2.put("type", "Email");
        interaction2.put("customer", "Customer B");
        interactions.add(interaction2);

        HashMap<String, String> interaction3 = new HashMap<>();
        interaction3.put("date", "2025-05-07");
        interaction3.put("type", "Meeting");
        interaction3.put("customer", "Customer C");
        interactions.add(interaction3);

        // Configurar o RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.interactions_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        InteractionAdapter adapter = new InteractionAdapter(interactions);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
