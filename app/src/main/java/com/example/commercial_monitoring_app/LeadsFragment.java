package com.example.commercial_monitoring_app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.commercial_monitoring_app.model.Oportunidade;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LeadsFragment extends Fragment {

    private HorizontalBarChart barChart;
    private ArrayList<String> steps = new ArrayList<>();

    public LeadsFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_leads, container, false);

        barChart = view.findViewById(R.id.barChart);

        List<Oportunidade> oportunidadeList = MyApp.getOportunidadeList();
        Map<String, Integer> etapaCountMap = new HashMap<>();

        for (Oportunidade oportunidade : oportunidadeList) {
            String etapa = oportunidade.getEtapaNome();
            if (etapa == null) continue;

            if (etapaCountMap.containsKey(etapa)) {
                etapaCountMap.put(etapa, etapaCountMap.get(etapa) + 1);
            } else {
                etapaCountMap.put(etapa, 1);
            }
        }
        List<String> etapasList = new ArrayList<>(etapaCountMap.keySet());
        List<Integer> valores = new ArrayList<>();

        for (String etapa : etapasList) {
            valores.add(etapaCountMap.get(etapa));
        }

        updateSteps(etapasList);
        updateValues(valores);

        setupCharts();

        return view;
    }

    private void setupCharts() {
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getDescription().setEnabled(false);
    }

    public void updateSteps(List<String> steps) {
        this.steps = new ArrayList<>(steps);

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(this.steps));

        barChart.invalidate();
    }

    public void updateValues(List<Integer> barValues) {
        List<BarEntry> barEntries = new ArrayList<>();

        for (int i = 0; i < barValues.size(); i++) {
            barEntries.add(new BarEntry(i, barValues.get(i)));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Quantidade de Oportunidades");

        List<Integer> colors = new ArrayList<>();
        int[] palette = new int[] {
                R.color.purple_700,
                R.color.purple_200,
                R.color.teal_700,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light
        };
        for (int i = 0; i < barEntries.size(); i++) {
            int colorRes = palette[i % palette.length];
            colors.add(getResources().getColor(colorRes, null));
        }
        barDataSet.setColors(colors);

        barDataSet.setDrawValues(true);
        barDataSet.setValueTextSize(12f);
        barDataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.6f);

        barData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                return String.format("%d", barEntry.getY());
            }
        });

        barChart.setData(barData);
        barChart.invalidate();
    }
}
