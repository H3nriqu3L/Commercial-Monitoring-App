package com.example.commercial_monitoring_app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class LeadsActivity extends AppCompatActivity {

    BarChart barChart;
    LineChart lineChart;
    Spinner spinnerProcessos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leads);

        barChart = findViewById(R.id.barChart);
        lineChart = findViewById(R.id.lineChart);
        spinnerProcessos = findViewById(R.id.spinner_processos);

        setupSpinner();
    }

    private void setupSpinner() {
        List<String> processos = new ArrayList<>();
        processos.add("Captação Graduação");
        processos.add("Acompanhamento do Estudante");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, processos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProcessos.setAdapter(adapter);

        spinnerProcessos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    setupGraficosCaptacao();
                } else {
                    setupGraficosAcompanhamento();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void navigateBack(View view) {
        finish();
    }

    private void setupGraficosCaptacao() {
        String[] etapas = {"Interessado", "Inscrito", "Visita", "Matrícula"};
        int[] valores = {4, 25, 20, 25};
        int[] tempos = {10, 5, 2, 7};
        atualizarGraficos(etapas, valores, tempos);
    }

    private void setupGraficosAcompanhamento() {
        String[] etapas = {"Regulares", "Em alerta", "Evadidos", "Egressos"};
        int[] valores = {25, 10, 5, 7};
        int[] tempos = {3, 6, 13, 11};
        atualizarGraficos(etapas, valores, tempos);
    }

    private void atualizarGraficos(String[] etapas, int[] valoresBar, int[] valoresLine) {
        List<BarEntry> barEntries = new ArrayList<>();
        List<Entry> lineEntries = new ArrayList<>();

        for (int i = 0; i < etapas.length; i++) {
            barEntries.add(new BarEntry(i, valoresBar[i]));
            lineEntries.add(new Entry(i, valoresLine[i]));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Oportunidades");
        barDataSet.setColors(new int[]{R.color.teal_200, R.color.teal_700, R.color.purple_200, R.color.purple_500}, this);
        barDataSet.setValueTextSize(14f);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        XAxis xBar = barChart.getXAxis();
        xBar.setValueFormatter(new IndexAxisValueFormatter(etapas));
        barChart.invalidate();

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Tempo Médio (dias)");
        lineDataSet.setColor(getColor(R.color.purple_500));
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setValueTextSize(12f);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        XAxis xLine = lineChart.getXAxis();
        xLine.setValueFormatter(new IndexAxisValueFormatter(etapas));
        lineChart.invalidate();
    }
}

