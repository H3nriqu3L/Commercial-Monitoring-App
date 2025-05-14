package com.example.commercial_monitoring_app;

import android.content.Intent;
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

import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import android.widget.ImageView;
import android.util.DisplayMetrics;

public class MainActivity extends AppCompatActivity {
    private TextView txtSelectedMonth;
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


    public void onMenuClick(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_menu, null);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        int width = (int) (screenWidth * 0.75); // 75% da largura da tela
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

        popupWindow.showAtLocation(view, Gravity.START, 0, 0); // como um drawer da esquerda

        // Botão de fechar
        ImageView closeButton = popupView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> popupWindow.dismiss());

        // Exemplo de clique
        LinearLayout item1 = popupView.findViewById(R.id.menu_item1);
        item1.setOnClickListener(v -> {
            popupWindow.dismiss();
            Intent it = new Intent(this, PerfilActivity.class);
            startActivity(it);
        });

        LinearLayout item2 = popupView.findViewById(R.id.menu_item2);
        item2.setOnClickListener(v -> {
            Toast.makeText(this, "Item 2 clicado", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        LinearLayout item3 = popupView.findViewById(R.id.menu_item3);
        item3.setOnClickListener(v -> {
            Toast.makeText(this, "Item 3 clicado", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        LinearLayout item4 = popupView.findViewById(R.id.menu_item4);
        item4.setOnClickListener(v -> {
            Toast.makeText(this, "Item 4 clicado", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        LinearLayout item5 = popupView.findViewById(R.id.menu_item5);
        item5.setOnClickListener(v -> {
            Toast.makeText(this, "Item 5 clicado", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
    }
}
