// HomeFragment.java
package com.example.commercial_monitoring_app;

import static com.example.commercial_monitoring_app.MyApp.getOportunidadeList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView oportunidadesGanhas;
    private TextView oportunidadesAbertas;
    private RecyclerView recyclerView;
    private AgendamentoAdapter agendamentoAdapter;
    private List<Agendamento> agendamentoList;
    private List<Agendamento> filteredAgendamento;
    private ActivityResultLauncher<Intent> homeDetailLauncher;
    private String currentFilter = "all";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla o layout do fragment

        homeDetailLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // Fazer refresh quando retornar com RESULT_OK
                            Log.d("HomeFragment", "RESULT_OK recebido, fazendo refresh");
                            refreshAtividades();
                        }
                    }
                }
        );
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
        ImageView filterIcon = view.findViewById(R.id.filterIconHome);

        filterIcon.setOnClickListener(v -> showFilterMenu(v));
    }

    private void showFilterMenu(View view) {
        PopupMenu popup = new PopupMenu(getContext(), view);


        popup.getMenuInflater().inflate(R.menu.filter_menu_home, popup.getMenu());


        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();


            if (itemId == R.id.filter_all) {
                applyFilter("all");
                return true;
            } else if (itemId == R.id.filter_mine) {
                applyFilter("mine");
                return true;
            }

            return false;
        });

        popup.show();
    }

    private void applyFilter(String filter) {
        currentFilter = filter;
        List<Agendamento> newFilteredList = new ArrayList<>();
        UserSession session = UserSession.getInstance(getContext());


        for (Agendamento agendamento : filteredAgendamento) {

            switch (currentFilter) {
                case "all":
                    newFilteredList = agendamentoList;
                    break;
                case "mine":
                    if (agendamento.getResponsavel() != null && agendamento.getResponsavelNome().equals(session.getUserName())) {
                        newFilteredList.add(agendamento);
                    }
                    break;
            }

        }
        filteredAgendamento = newFilteredList;
        agendamentoAdapter.updateData(filteredAgendamento);

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
        agendamentoAdapter = new AgendamentoAdapter(filteredAgendamento, homeDetailLauncher);

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
        String aux = String.valueOf(MyApp.getOportunidadeList().size());
        oportunidadesAbertas.setText(aux);
    }

    public void refreshAtividades() {
        Log.d("HomeFragment", "refreshAtividades() CHAMADO - fazendo nova requisição da API");

        ApiService apiService = RetrofitClient.getApiService(ApiService.class, "https://crmufvgrupo3.apprubeus.com.br/");

        Callback<ResponseWrapper<Agendamento>> callback = new Callback<ResponseWrapper<Agendamento>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<Agendamento>> call, Response<ResponseWrapper<Agendamento>> response) {
                Log.d("HomeFragment", "API callback executado - resposta recebida");

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Pegar a lista atualizada que foi setada pelo fetchAgendamento
                        agendamentoList = MyApp.getAgendamentoList();
                        Log.d("HomeFragment", "Atualizando UI com " + agendamentoList.size() + " agendamentos");

                        Log.d("HomeFragment", "Adapter atualizado com dados da API");
                    });
                } else {
                    Log.e("HomeFragment", "Activity é null no callback da API");
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<Agendamento>> call, Throwable t) {
                Log.e("HomeFragment", "Erro ao carregar agendamentos: " + t.getMessage());

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(),
                                "Erro ao atualizar lista: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
                }
            }
        };

        MyApp.fetchAgendamentoFromApi(callback, apiService);
    }
}