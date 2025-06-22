package com.example.commercial_monitoring_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commercial_monitoring_app.adapter.OportunidadesAdapter;
import com.example.commercial_monitoring_app.database.DatabaseHelper;
import com.example.commercial_monitoring_app.model.Oportunidade;
import com.example.commercial_monitoring_app.network.ApiService;
import com.example.commercial_monitoring_app.network.ResponseWrapper;
import com.example.commercial_monitoring_app.network.RetrofitClient;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OportunidadesFragment extends Fragment {

    private OportunidadesAdapter oportunidadesAdapter;
    private RecyclerView recyclerView;
    List<Oportunidade> oportunidadesList;
    private List<Oportunidade> filteredOportunidades;
    private String currentFilter = "all";
    private String currentSearchQuery = "";
    private SearchView searchView;
    private TabLayout tabLayout;
    private String currentTab = "captacao"; // "captacao" ou "acompanhamento"

    private ActivityResultLauncher<Intent> oportunidadeDetailLauncher;

    public OportunidadesFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        oportunidadeDetailLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // Fazer refresh quando retornar com RESULT_OK
                            Log.d("OportunidadesFragment", "RESULT_OK recebido, fazendo refresh");
                            refreshOportunidades();
                        }
                    }
                }
        );
        return inflater.inflate(R.layout.fragment_oportunidades, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.oportunidadesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ImageView filterIcon = view.findViewById(R.id.filterIcon);
        filterIcon.setOnClickListener(v -> showFilterMenu(v));

        // Setup TabLayout
        tabLayout = view.findViewById(R.id.tabLayout);
        setupTabLayout();

        oportunidadesList = MyApp.getOportunidadeList();
        if (oportunidadesList == null) {
            oportunidadesList = new ArrayList<>();
            Log.w("OportunidadesFragment", "Oportunidade list is null or empty");
        }

        // Create initial filtered list (copy of original)
        filteredOportunidades = new ArrayList<>(oportunidadesList);

        //oportunidadesAdapter = new OportunidadesAdapter(filteredOportunidades);
        oportunidadesAdapter = new OportunidadesAdapter(filteredOportunidades, oportunidadeDetailLauncher);

        recyclerView.setAdapter(oportunidadesAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        //Search Setup
        searchView = view.findViewById(R.id.searchView);
        if (searchView != null) {
            setupSearchView();
        } else {
            Log.e("OportunidadesFragment", "SearchView not found in layout");
        }

        // Apply initial filter
        applyFiltersAndSearch();
    }

    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0: // Captação
                        currentTab = "captacao";
                        break;
                    case 1: // Acompanhamento
                        currentTab = "acompanhamento";
                        break;
                }
                // Reset filter when changing tabs
                currentFilter = "all";
                applyFiltersAndSearch();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Não precisa fazer nada
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Não precisa fazer nada
            }
        });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentSearchQuery = query;
                applyFiltersAndSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchQuery = newText;
                applyFiltersAndSearch();
                return true;
            }
        });

        // Optional: Handle search view close
        searchView.setOnCloseListener(() -> {
            currentSearchQuery = "";
            applyFiltersAndSearch();
            return false;
        });
    }

    private void showFilterMenu(View view) {
        PopupMenu popup = new PopupMenu(getContext(), view);

        // Infla o menu correto baseado na tab atual
        if (currentTab.equals("acompanhamento")) {
            popup.getMenuInflater().inflate(R.menu.filter_menu, popup.getMenu());
        } else {
            popup.getMenuInflater().inflate(R.menu.filter_menu_captacao, popup.getMenu());
        }

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.filter_all) {
                applyFilter("all");
                return true;
            }

            // Filtros para Acompanhamento
            if (currentTab.equals("acompanhamento")) {
                if (itemId == R.id.filter_alerta) {
                    applyFilter("alerta");
                    return true;
                } else if (itemId == R.id.filter_evadidos) {
                    applyFilter("evadidos");
                    return true;
                } else if (itemId == R.id.filter_regulares) {
                    applyFilter("regulares");
                    return true;
                }
            }
            // Filtros para Captação
            else {
                if (itemId == R.id.filter_potencial) {
                    applyFilter("potencial");
                    return true;
                } else if (itemId == R.id.filter_interessado) {
                    applyFilter("interessado");
                    return true;
                } else if (itemId == R.id.filter_inscrito_parcial) {
                    applyFilter("inscrito_parcial");
                    return true;
                } else if (itemId == R.id.filter_inscrito) {
                    applyFilter("inscrito");
                    return true;
                } else if (itemId == R.id.filter_confirmado) {
                    applyFilter("confirmado");
                    return true;
                } else if (itemId == R.id.filter_convocado) {
                    applyFilter("convocado");
                    return true;
                } else if (itemId == R.id.filter_matriculado) {
                    applyFilter("matriculado");
                    return true;
                }
            }

            return false;
        });

        popup.show();
    }

    private void applyFiltersAndSearch() {
        List<Oportunidade> newFilteredList = new ArrayList<>();

        // Primeiro aplica o filtro da tab
        for (Oportunidade oportunidade : oportunidadesList) {
            if (shouldShowInCurrentTab(oportunidade)) {
                newFilteredList.add(oportunidade);
            }
        }

        // Depois aplica o filtro específico
        if (!currentFilter.equals("all")) {
            List<Oportunidade> specificFilteredList = new ArrayList<>();

            for (Oportunidade oportunidade : newFilteredList) {
                if (currentTab.equals("acompanhamento")) {
                    // Filtros para Acompanhamento
                    switch (currentFilter) {
                        case "alerta":
                            if (oportunidade.getEtapaNome() != null && oportunidade.getEtapaNome().equals("Em Alerta")) {
                                specificFilteredList.add(oportunidade);
                            }
                            break;
                        case "evadidos":
                            if (oportunidade.getEtapaNome() != null && oportunidade.getEtapaNome().equals("Evadidos")) {
                                specificFilteredList.add(oportunidade);
                            }
                            break;
                        case "regulares":
                            if (oportunidade.getEtapaNome() != null && oportunidade.getEtapaNome().equals("Alunos Regulares")) {
                                specificFilteredList.add(oportunidade);
                            }
                            break;
                    }
                } else {
                    // Filtros para Captação
                    switch (currentFilter) {
                        case "potencial":
                            if (oportunidade.getEtapaNome() != null && oportunidade.getEtapaNome().equals("Potencial")) {
                                specificFilteredList.add(oportunidade);
                            }
                            break;
                        case "interessado":
                            if (oportunidade.getEtapaNome() != null && oportunidade.getEtapaNome().equals("Interessado")) {
                                specificFilteredList.add(oportunidade);
                            }
                            break;
                        case "inscrito_parcial":
                            if (oportunidade.getEtapaNome() != null && oportunidade.getEtapaNome().equals("Inscrito parcial")) {
                                specificFilteredList.add(oportunidade);
                            }
                            break;
                        case "inscrito":
                            if (oportunidade.getEtapaNome() != null && oportunidade.getEtapaNome().equals("Inscrito")) {
                                specificFilteredList.add(oportunidade);
                            }
                            break;
                        case "confirmado":
                            if (oportunidade.getEtapaNome() != null && oportunidade.getEtapaNome().equals("Confirmado")) {
                                specificFilteredList.add(oportunidade);
                            }
                            break;
                        case "convocado":
                            if (oportunidade.getEtapaNome() != null && oportunidade.getEtapaNome().equals("Convocado")) {
                                specificFilteredList.add(oportunidade);
                            }
                            break;
                        case "matriculado":
                            if (oportunidade.getEtapaNome() != null && oportunidade.getEtapaNome().equals("Matriculado")) {
                                specificFilteredList.add(oportunidade);
                            }
                            break;
                    }
                }
            }
            newFilteredList = specificFilteredList;

        }

        // Por último aplica o filtro de busca
        if (!currentSearchQuery.isEmpty()) {
            List<Oportunidade> searchFilteredList = new ArrayList<>();
            String searchLower = currentSearchQuery.toLowerCase().trim();

            for (Oportunidade oportunidade : newFilteredList) {
                if (oportunidade.getPessoaNome() != null &&
                        oportunidade.getPessoaNome().toLowerCase().contains(searchLower)) {
                    searchFilteredList.add(oportunidade);
                }
            }
            newFilteredList = searchFilteredList;
        }


        if (currentTab.equals("captacao")) {
            newFilteredList = sortCaptacaoList(newFilteredList);
        } else if (currentTab.equals("acompanhamento")) {
            newFilteredList = sortAcompanhamentoList(newFilteredList);
        }


        oportunidadesAdapter.updateData(newFilteredList);
    }

    private boolean shouldShowInCurrentTab(Oportunidade oportunidade) {
        String etapaNome = oportunidade.getEtapaNome();

        if (currentTab.equals("acompanhamento")) {
            // Tab Acompanhamento: mostrar apenas "Evadidos", "Em Alerta" ou "Alunos Regulares"
            return etapaNome != null &&
                    (etapaNome.equals("Evadidos") ||
                            etapaNome.equals("Em Alerta") ||
                            etapaNome.equals("Alunos Regulares"));
        } else {
            // Tab Captação: mostrar qualquer coisa que NÃO seja "Evadidos", "Em Alerta" ou "Alunos Regulares"
            return etapaNome == null ||
                    (!etapaNome.equals("Evadidos") &&
                            !etapaNome.equals("Em Alerta") &&
                            !etapaNome.equals("Alunos Regulares"));
        }
    }

    private void applyFilter(String filterType) {
        currentFilter = filterType;
        applyFiltersAndSearch();
    }


    private List<Oportunidade> sortCaptacaoList(List<Oportunidade> oportunidades) {
        // Definindo a ordem de prioridade das etapas
        Map<String, Integer> etapaOrder = new HashMap<>();
        etapaOrder.put("Potencial", 1);
        etapaOrder.put("Interessado", 2);
        etapaOrder.put("Inscrito parcial", 3);
        etapaOrder.put("Inscrito", 4);
        etapaOrder.put("Confirmado", 5);
        etapaOrder.put("Convocado", 6);
        etapaOrder.put("Matriculado", 7);

        // Criando uma nova lista para não modificar a original
        List<Oportunidade> sortedList = new ArrayList<>(oportunidades);


        Collections.sort(sortedList, new Comparator<Oportunidade>() {
            @Override
            public int compare(Oportunidade o1, Oportunidade o2) {
                String etapa1 = o1.getEtapaNome();
                String etapa2 = o2.getEtapaNome();


                if (etapa1 == null && etapa2 == null) return 0;
                if (etapa1 == null) return 1;
                if (etapa2 == null) return -1;


                Integer order1 = etapaOrder.get(etapa1);
                Integer order2 = etapaOrder.get(etapa2);


                if (order1 == null && order2 == null) return 0;
                if (order1 == null) return 1;
                if (order2 == null) return -1;


                return Integer.compare(order1, order2);
            }
        });

        return sortedList;
    }

    private List<Oportunidade> sortAcompanhamentoList(List<Oportunidade> oportunidades) {
        // Definindo a ordem de prioridade das etapas para acompanhamento
        Map<String, Integer> etapaOrder = new HashMap<>();
        etapaOrder.put("Em Alerta", 1);
        etapaOrder.put("Evadidos", 2);
        etapaOrder.put("Alunos Regulares", 3);

        List<Oportunidade> sortedList = new ArrayList<>(oportunidades);

        // Ordenando a lista baseada na ordem das etapas
        Collections.sort(sortedList, new Comparator<Oportunidade>() {
            @Override
            public int compare(Oportunidade o1, Oportunidade o2) {
                String etapa1 = o1.getEtapaNome();
                String etapa2 = o2.getEtapaNome();

                if (etapa1 == null && etapa2 == null) return 0;
                if (etapa1 == null) return 1;
                if (etapa2 == null) return -1;


                Integer order1 = etapaOrder.get(etapa1);
                Integer order2 = etapaOrder.get(etapa2);


                if (order1 == null && order2 == null) return 0;
                if (order1 == null) return 1;
                if (order2 == null) return -1;


                return Integer.compare(order1, order2);
            }
        });

        return sortedList;
    }


    public void refreshOportunidades() {
        Log.d("OportunidadesFragment", "refreshOportunidades() CHAMADO - fazendo nova requisição da API");

        ApiService apiService = RetrofitClient.getApiService(ApiService.class, "https://crmufvgrupo3.apprubeus.com.br/");

        Callback<ResponseWrapper<Oportunidade>> callback = new Callback<ResponseWrapper<Oportunidade>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<Oportunidade>> call, Response<ResponseWrapper<Oportunidade>> response) {
                Log.d("OportunidadesFragment", "API callback executado - resposta recebida");

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Pegar a lista atualizada que foi setada pelo fetchOportunidadesFromApi
                        oportunidadesList = MyApp.getOportunidadeList();
                        Log.d("OportunidadesFragment", "Atualizando UI com " + oportunidadesList.size() + " oportunidades");

                        // Reaplicar filtros e atualizar adapter
                        applyFiltersAndSearch();

                        Log.d("OportunidadesFragment", "Adapter atualizado com dados da API");
                    });
                } else {
                    Log.e("OportunidadesFragment", "Activity é null no callback da API");
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<Oportunidade>> call, Throwable t) {
                Log.e("OportunidadesFragment", "Erro ao carregar oportunidades: " + t.getMessage());

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(),
                                "Erro ao atualizar lista: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
                }
            }
        };

        MyApp.fetchOportunidadesFromApi(callback, apiService);
    }
}