package com.example.commercial_monitoring_app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

import java.util.ArrayList;
import java.util.List;

public class OportunidadesFragment extends Fragment {

    private OportunidadesAdapter oportunidadesAdapter;
    private RecyclerView recyclerView;
    List<Oportunidade> oportunidadesList;
    private List<Oportunidade> filteredOportunidades;
    private String currentFilter = "all";

    public OportunidadesFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oportunidades, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.oportunidadesRecyclerView); // use correct ID
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ImageView filterIcon = view.findViewById(R.id.filterIcon);
        filterIcon.setOnClickListener(v -> showFilterMenu(v));

        // Get original data (keep this as your source of truth)
        oportunidadesList = MyApp.getOportunidadeList();
        if (oportunidadesList == null) {
            oportunidadesList = new ArrayList<>();
            Log.w("OportunidadesFragment", "Oportunidade list is null or empty");
        }

        // Create initial filtered list (copy of original)
        filteredOportunidades = new ArrayList<>(oportunidadesList);

        // Setup adapter
        oportunidadesAdapter = new OportunidadesAdapter(filteredOportunidades);


        recyclerView.setAdapter(oportunidadesAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void showFilterMenu(View view) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.getMenuInflater().inflate(R.menu.filter_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.filter_all) {

                applyFilter("all");
                return true;
            } else if (itemId == R.id.filter_alerta) {

                applyFilter("alerta");
                return true;
            } else if (itemId == R.id.filter_evadidos) {

                applyFilter("evadidos");
                return true;
            } else if (itemId == R.id.filter_regulares  ) {

                applyFilter("regulares");
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void applyFilter(String filterType) {
        List<Oportunidade> newFilteredList = new ArrayList<>();

        switch (filterType) {
            case "all":
                newFilteredList.addAll(oportunidadesList); // original list
                break;

            case "alerta":
                for (Oportunidade oportunidade : oportunidadesList) {
                    if (oportunidade.getEtapaNome() != null && oportunidade.getEtapaNome().equals("Em Alerta")) {
                        newFilteredList.add(oportunidade);
                    }
                }
                break;

            case "evadidos":
                for (Oportunidade oportunidade : oportunidadesList) {
                    if (oportunidade.getEtapaNome() != null && oportunidade.getEtapaNome().equals("Evadidos")) {
                        newFilteredList.add(oportunidade);
                    }
                }
                break;

            case "regulares":
                for (Oportunidade oportunidade : oportunidadesList) {
                    if (oportunidade.getEtapaNome() != null && oportunidade.getEtapaNome().equals("Alunos Regulares")) {
                        newFilteredList.add(oportunidade);
                    }
                }
                break;
        }

        oportunidadesAdapter.updateData(newFilteredList);

        //Toast.makeText(getContext(), filterType + " - " + newFilteredList.size() + " items", Toast.LENGTH_SHORT).show();
    }

    private void showDeleteConfirmation(int position, Oportunidade oportunidade) {
        if (getContext() == null) return;

        new AlertDialog.Builder(getContext())
                .setTitle("Excluir oportunidade")
                .setMessage("Deseja realmente excluir a oportunidade de " + oportunidade.getPessoaNome() + "?")
                .setPositiveButton("Excluir", (dialog, which) -> deleteOportunidade(position, oportunidade))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deleteOportunidade(int position, Oportunidade oportunidade) {
        try {
            // Remove imediatamente da lista local
            List<Oportunidade> currentList = MyApp.getOportunidadeList();
            currentList.remove(position);
            oportunidadesAdapter.notifyItemRemoved(position);

            MyApp.excluirOportunidadeEAtualizarLista(Integer.parseInt(oportunidade.getId()),
                    new MyApp.OnOportunidadeDeletedListener() {
                        @Override
                        public void onOportunidadeDeleted() {
                            refreshOportunidadesList();
                        }
                    });
        } catch (Exception e) {
            Log.e("OportunidadesFragment", "Erro ao deletar oportunidade", e);
            Toast.makeText(getContext(), "Erro ao deletar oportunidade", Toast.LENGTH_SHORT).show();
            // Reverte a UI em caso de erro
            oportunidadesAdapter.notifyItemChanged(position);
        }
    }


    public void refreshOportunidadesList() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                List<Oportunidade> updatedList = MyApp.getOportunidadeList();
                Log.d("OportunidadesFragment", "Atualizando lista com " + updatedList.size() + " oportunidades");

                oportunidadesAdapter = new OportunidadesAdapter(updatedList); // Changed to single parameter
                recyclerView.setAdapter(oportunidadesAdapter);

                Log.d("OportunidadesFragment", "Adapter atualizado");
            });
        }
    }

    public void refreshOportunidades() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                List<Oportunidade> updatedList = MyApp.getOportunidadeList();
                Log.d("OportunidadesFragment", "Atualizando lista com " + updatedList.size() + " oportunidades");

                if (oportunidadesAdapter != null) {
                    oportunidadesAdapter.updateData(updatedList);
                }

                Log.d("OportunidadesFragment", "Adapter atualizado");
            });
        }
    }
}
