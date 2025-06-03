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

        apiService = RetrofitClient.getApiService(ApiService.class, "https://crmufvgrupo3.apprubeus.com.br/");

        setupBarChart(view);
        setupPieChart(view);

        RecyclerView recyclerView = view.findViewById(R.id.interactions_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new InteractionAdapter(interactions);
        recyclerView.setAdapter(adapter);

        //fetchPessoasFromApi();
        fetchOportunidadesFromApi(); // você pode usar esse método alternadamente

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
        Call<ResponseWrapper<Pessoa>> call = apiService.listarPessoas();

        call.enqueue(new Callback<ResponseWrapper<Pessoa>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<Pessoa>> call, Response<ResponseWrapper<Pessoa>> response) {
                if (response.isSuccessful()) {
                    ResponseWrapper<Pessoa> pessoas = response.body();
                    System.out.println(pessoas.dados);
                    if (pessoas != null) {
                        interactions.clear();
                        for (Pessoa pessoa : pessoas.dados.dados) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("customer", pessoa.getNome());
                            map.put("type", pessoa.getTelefone() != null ? pessoa.getTelefone() : "Sem telefone");
                            map.put("date", "Data não disponível"); // Ajuste conforme o modelo
                            interactions.add(map);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        showError("Nenhum dado recebido da API");
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Erro desconhecido";
                        Log.e("API_RESPONSE", "Erro: " + errorBody);
                        showError("Erro na API: " + response.code());
                    } catch (IOException e) {
                        showError("Erro ao processar resposta");
                        Log.e("API_RESPONSE", "Exception: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<Pessoa>> call, Throwable t) {
                showError("Erro de rede: " + t.getMessage());
                Log.e("API_FAILURE", "Erro: ", t);
            }


        });
    }

    private void fetchOportunidadesFromApi() {
        Call<ResponseWrapper<Oportunidade>> call = apiService.listarOportunidades();

        call.enqueue(new Callback<ResponseWrapper<Oportunidade>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<Oportunidade>> call, Response<ResponseWrapper<Oportunidade>> response) {
                if (response.isSuccessful()) {
                    ResponseWrapper<Oportunidade> oportunidades = response.body();
                    System.out.println(oportunidades.dados);
                    if (oportunidades != null) {
                        interactions.clear();
                        for (Oportunidade oportunidade : oportunidades.dados.dados) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("customer", oportunidade.getPessoaNome());
                            map.put("type", oportunidade.getEtapaNome() != null ? oportunidade.getEtapaNome() : "Sem razao");
                            map.put("date", oportunidade.getUltimaAlteracaoEtapa()); // Ajuste conforme o modelo
                            interactions.add(map);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        showError("Nenhum dado recebido da API");
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Erro desconhecido";
                        Log.e("API_RESPONSE", "Erro: " + errorBody);
                        showError("Erro na API: " + response.code());
                    } catch (IOException e) {
                        showError("Erro ao processar resposta");
                        Log.e("API_RESPONSE", "Exception: ", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<Oportunidade>> call, Throwable t) {
                showError("Erro de rede: " + t.getMessage());
                Log.e("API_FAILURE", "Erro: ", t);
            }


        });


    }

    private void showError(String message) {
        if (isAdded()) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
