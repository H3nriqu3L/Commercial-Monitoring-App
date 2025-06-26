// HomeFragment.java
package com.example.commercial_monitoring_app;

import static com.example.commercial_monitoring_app.MyApp.getOportunidadeList;
import static com.example.commercial_monitoring_app.MyApp.getUserSession;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.example.commercial_monitoring_app.model.Client;
import com.example.commercial_monitoring_app.model.Oportunidade;
import com.example.commercial_monitoring_app.model.OportunidadeDadosResponse;
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

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView oportunidadesGanhas;
    private TextView oportunidadesPerdidas;
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

        if (!MyApp.getClientList().isEmpty()) {
            Log.d("HomeFragment", "Client list already available. Calling loadData() immediately.");
            loadData();
        } else {
            Log.d("HomeFragment", "Client list NOT ready. Waiting via callback.");
            MyApp.setOnClientsReadyCallback(() -> {
                Log.d("HomeFragment", "onClientsReady callback triggered!");
                loadData();
            });
        }


    }

    public void onClientsReady() {
        Log.d("HomeFragment", "onClientsReady() chamado - iniciando loadData()");
        loadData();
    }


    private void initViews(View view) {
        oportunidadesGanhas = view.findViewById(R.id.oportunidadesGanhas);
        oportunidadesAbertas = view.findViewById(R.id.oportunidadesAbertas);
        oportunidadesPerdidas = view.findViewById(R.id.oportunidadesPerdidas);
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
        //loadOportunidadesGanhas();
        loadOportunidadesGanhasEPerdidas();
        loadOportunidadesAbertas();
    }

    private void loadOportunidadesGanhasEPerdidas() {
        if (MyApp.getCountOportunidadesGanhas()!=-1 || MyApp.getCountOportunidadesPerdidas()!=-1){
            if (MyApp.getCountOportunidadesGanhas()!=-1) {
                oportunidadesGanhas.setText(String.valueOf(MyApp.getCountOportunidadesGanhas()));
            }
            if (MyApp.getCountOportunidadesPerdidas()!=-1) {
                oportunidadesPerdidas.setText(String.valueOf(MyApp.getCountOportunidadesPerdidas()));
            }

            return;
        }

        try {
            List<Client> clients = MyApp.getClientList();
            int totalClients = clients.size();

            if (totalClients == 0) {
                return;
            }

            List<Oportunidade> todasOportunidades = MyApp.getTodasOportunidadesList();

            final int[] ganhoCount = {0};
            final int [] lostCount = {0};
            final int[] totalGanhoCount = {0};
            final int [] totalLostCount = {0};

            final int[] responsesReceived = {0};
            UserSession session = UserSession.getInstance(MyApp.getAppContext());

            String responsael_ID = String.valueOf(session.getUserID());


            for (Client client : clients) {
                int clientId = client.getId();

                Call<ResponseBody> call = MyApp.getApiService().getNavegacaoInternaUsuario(clientId);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                String rawJson = response.body().string();
                                Log.d("NAV_DEBUG", "Client ID " + clientId + ": " + rawJson);

                                JSONObject root = new JSONObject(rawJson);
                                JSONObject dadosPessoaWrapper = root.optJSONObject("dadosPessoa");

                                if (dadosPessoaWrapper != null && dadosPessoaWrapper.optBoolean("success")) {
                                    JSONObject dadosPessoa = dadosPessoaWrapper.optJSONObject("dados");
                                    if (dadosPessoa != null) {
                                        JSONArray processos = dadosPessoa.optJSONArray("processos");
                                        if (processos != null) {
                                            for (int i = 0; i < processos.length(); i++) {
                                                JSONObject processo = processos.getJSONObject(i);
                                                JSONArray oportunidades = processo.optJSONArray("oportunidades");

                                                if (oportunidades != null) {
                                                    for (int j = 0; j < oportunidades.length(); j++) {
                                                        JSONObject oportunidade = oportunidades.getJSONObject(j);
                                                        String status = oportunidade.optString("status");
                                                        String responsavel = oportunidade.optString("responsavel");
                                                        if("2".equals(status)) {
                                                            totalGanhoCount[0]++;
                                                            if (responsael_ID.equals(responsavel)) {
                                                                ganhoCount[0]++;
                                                                Log.d("STATUS_MATCH", "Ganho encontrado para clientId " + clientId + " consultoraresponsavel: " + responsavel + "eu: " + responsael_ID);
                                                            }
                                                        }
                                                        if("3".equals(status)) {
                                                            totalLostCount[0]++;
                                                            if (responsael_ID.equals(responsavel)) {
                                                                lostCount[0]++;
                                                                Log.d("STATUS_MATCH", "Lost encontrado para clientId " + clientId + " consultoraresponsavel: " + responsavel + "eu: " + responsael_ID);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            } catch (Exception e) {
                                Log.e("PARSE_ERROR", "Erro ao parsear JSON para clientId " + clientId, e);
                            }
                        } else {
                            Log.e("API_ERROR", "Erro HTTP para clientId " + clientId + ": código " + response.code());
                        }

                        responsesReceived[0]++;
                        if (responsesReceived[0] == totalClients) {
                            oportunidadesGanhas.setText(String.valueOf(ganhoCount[0]));
                            MyApp.setCountOportunidadesGanhas(ganhoCount[0]);

                            oportunidadesPerdidas.setText(String.valueOf(lostCount[0]));
                            MyApp.setCountOportunidadesPerdidas(lostCount[0]);

                            MyApp.setCountTotalOportunidadesGanhas(totalGanhoCount[0]);
                            MyApp.setCountTotalOportunidadesPerdidas(totalLostCount[0]);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("NAV_FAILURE", "Erro de rede para clientId " + clientId, t);
                        responsesReceived[0]++;
                        if (responsesReceived[0] == totalClients) {
                            oportunidadesGanhas.setText(String.valueOf(ganhoCount[0]));
                            MyApp.setCountOportunidadesGanhas(ganhoCount[0]);
                            oportunidadesPerdidas.setText(String.valueOf(lostCount[0]));
                            MyApp.setCountOportunidadesPerdidas(lostCount[0]);

                            MyApp.setCountTotalOportunidadesGanhas(totalGanhoCount[0]);
                            MyApp.setCountTotalOportunidadesPerdidas(totalLostCount[0]);
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.e("LOAD_ERROR", "Erro inesperado", e);
            throw new RuntimeException(e);
        }
    }

    private void loadOportunidadesAbertas() {
        String TAG = "LoadOportunidades";

        Log.d(TAG, "Starting loadOportunidadesAbertas method");

        List<Oportunidade> oportunidadeList = MyApp.getOportunidadeList();
        Log.i(TAG, "Retrieved opportunity list with size: " + (oportunidadeList != null ? oportunidadeList.size() : "null"));

        UserSession session = UserSession.getInstance(getContext());
        String currentUserName = session.getUserName();
        Log.i(TAG, "Current user: " + currentUserName);

        int count = 0;

        if (oportunidadeList != null) {
            for(int i = 0; i < oportunidadeList.size(); i++){
                Oportunidade oportunidade = oportunidadeList.get(i);
                String responsavelNome = oportunidade.getResponsavelNome();

                Log.v(TAG, "Checking opportunity " + i + " - Responsible: " + responsavelNome);

                if(responsavelNome != null && responsavelNome.equals(currentUserName)){
                    count++;
                    Log.d(TAG, "Match found! Count is now: " + count);
                }
            }
        } else {
            Log.w(TAG, "Opportunity list is null");
        }

        Log.i(TAG, "Final count of opportunities for user '" + currentUserName + "': " + count);
        oportunidadesAbertas.setText(String.valueOf(count));

        Log.d(TAG, "loadOportunidadesAbertas method completed");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApp.setOnClientsReadyCallback(null);
    }
}