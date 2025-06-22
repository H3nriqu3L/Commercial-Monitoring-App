package com.example.commercial_monitoring_app;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        updateSteps(Arrays.asList("Prospecção", "Qualificação", "Proposta", "Fechamento"));
        updateValues(Arrays.asList(10f, 7f, 5f, 3f), Arrays.asList(5f, 10f, 8f, 12f));

        setupCharts();

        return view;
    }

    private void setupCharts() {
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setGranularity(1f);
    }

    public void updateSteps(List<String> steps) {
        this.steps = new ArrayList<>(steps);

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(this.steps));
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);

        barChart.invalidate();
    }

    public void updateValues(List<Float> barValues, List<Float> lineValues) {
        List<BarEntry> barEntries = new ArrayList<>();
        List<Entry> lineEntries = new ArrayList<>();

        for (int i = 0; i < barValues.size(); i++) {
            barEntries.add(new BarEntry(i, barValues.get(i)));
        }

        for (int i = 0; i < lineValues.size(); i++) {
            lineEntries.add(new Entry(i, lineValues.get(i)));
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

        barDataSet.setValueTextSize(12f);
        barDataSet.setValueTextColor(getResources().getColor(android.R.color.black, null));

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.6f);
        barChart.setData(barData);
        barChart.invalidate();
    }
}
