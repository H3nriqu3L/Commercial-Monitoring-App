package com.example.commercial_monitoring_app;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gráfico de barras - Vendas Gerais
        BarChart generalBarChart = findViewById(R.id.generalBarChart);

        ArrayList<BarEntry> entriesGeneral = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            entriesGeneral.add(new BarEntry(i, (i + 1) * 10));
        }

        int barColor = getResources().getColor(android.R.color.holo_blue_dark, null);
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
        PieChart statusPieChart = findViewById(R.id.statusPieChart);

        ArrayList<PieEntry> statusEntries = new ArrayList<>();
        statusEntries.add(new PieEntry(40f, "Potencial"));
        statusEntries.add(new PieEntry(35f, "Interessado"));
        statusEntries.add(new PieEntry(25f, "Inscrito Parcial"));

        PieDataSet pieDataSet = new PieDataSet(statusEntries, "Status dos Clientes");
        pieDataSet.setColors(new int[]{
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light
        }, this);
        pieDataSet.setValueTextSize(14f);
        pieDataSet.setValueTextColor(getResources().getColor(android.R.color.black, null));

        PieData pieData = new PieData(pieDataSet);
        statusPieChart.setData(pieData);

        Description pieDesc = new Description();
        pieDesc.setText("Distribuição de Status");
        statusPieChart.setDescription(pieDesc);
        statusPieChart.setUsePercentValues(true);
        statusPieChart.setEntryLabelColor(getResources().getColor(android.R.color.black, null));

        statusPieChart.invalidate(); // Atualizar gráfico de pizza

        // Sample data for item_interaction without using classes
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

        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.interactions_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        InteractionAdapter adapter = new InteractionAdapter(interactions);
        recyclerView.setAdapter(adapter);
    }
}
