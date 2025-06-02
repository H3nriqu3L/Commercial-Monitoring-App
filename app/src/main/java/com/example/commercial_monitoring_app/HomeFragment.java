package com.example.commercial_monitoring_app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;

import com.example.commercial_monitoring_app.model.Pessoa;
import com.example.commercial_monitoring_app.network.ApiService;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private InteractionAdapter adapter;
    private ArrayList<HashMap<String, String>> interactions = new ArrayList<>();
    private ApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        apiService = RetrofitClient.getApiService();


        // Configura gráficos (igual ao seu código original)
        setupBarChart(view);
        setupPieChart(view);

        // Setup RecyclerView com adapter vazio por enquanto
        RecyclerView recyclerView = view.findViewById(R.id.interactions_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new InteractionAdapter(interactions);
        recyclerView.setAdapter(adapter);

        // Faz a requisição API para carregar Pessoas e atualizar RecyclerView
        fetchPessoasFromApi();

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

    private void fetchPessoasFromApi() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Pessoa>> call = apiService.listarPessoas();

        call.enqueue(new Callback<List<Pessoa>>() {
            @Override
            public void onResponse(Call<List<Pessoa>> call, Response<List<Pessoa>> response) {
                if (response.isSuccessful()) {
                    List<Pessoa> pessoas = response.body();
                    if (pessoas != null) {
                        interactions.clear();
                        for (Pessoa pessoa : pessoas) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("customer", pessoa.getNome());
                            map.put("type", pessoa.getTelefone() != null ? pessoa.getTelefone() : "Sem telefone");
                            map.put("date", "Data não disponível"); // ou algum valor real, se tiver

                            // Add other fields as needed
                            interactions.add(map);
                        }

                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.e("API_RESPONSE", "Response body is null");
                        showError("No data received");
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Unknown error";
                        Log.e("API_RESPONSE", "Unsuccessful response: " + response.code() +
                                ", Error: " + errorBody);
                        showError("Error: " + response.code());
                    } catch (IOException e) {
                        Log.e("API_RESPONSE", "Error parsing error body", e);
                        showError("Error parsing response");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Pessoa>> call, Throwable t) {
                Log.e("API_FAILURE", "API call failed", t);
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private void showError(String message) {
        if (isAdded() && getActivity() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
