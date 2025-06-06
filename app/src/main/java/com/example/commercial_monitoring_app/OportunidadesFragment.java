package com.example.commercial_monitoring_app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public OportunidadesFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oportunidades, container, false); // make sure this layout exists
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.oportunidadesRecyclerView); // use correct ID
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Oportunidade> oportunidadesList = MyApp.getOportunidadeList(); // replace with your actual getter
        if (oportunidadesList == null) {
            oportunidadesList = new ArrayList<>();
            Log.w("OportunidadesFragment", "Oportunidade list is null or empty");
        }

        oportunidadesAdapter = new OportunidadesAdapter(oportunidadesList, (position, oportunidade) -> {
            showDeleteConfirmation(position, oportunidade);
        });

        recyclerView.setAdapter(oportunidadesAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
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
        }
    }

    private void deleteFromDatabase(Oportunidade oportunidade) {
        try {
            DatabaseHelper dbHelper = DatabaseHelper.getInstance();

            String whereClause = "id = " + oportunidade.getId(); // use appropriate condition
            int deletedRows = dbHelper.delete("Oportunidade", whereClause);

            if (deletedRows > 0) {
                Log.d("OportunidadesFragment", "Oportunidade removida do DB: ID " + oportunidade.getId());
            } else {
                Log.w("OportunidadesFragment", "Nenhuma oportunidade encontrada com ID: " + oportunidade.getId());
            }

        } catch (Exception e) {
            Log.e("OportunidadesFragment", "Erro ao deletar do DB", e);
            throw e;
        }
    }

    public void refreshOportunidadesList() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                List<Oportunidade> updatedList = MyApp.getOportunidadeList();
                Log.d("OportunidadesFragment", "Atualizando lista com " + updatedList.size() + " oportunidades");

                oportunidadesAdapter = new OportunidadesAdapter(updatedList, (position, oportunidade) -> {
                    showDeleteConfirmation(position, oportunidade);
                });
                recyclerView.setAdapter(oportunidadesAdapter);

                Log.d("OportunidadesFragment", "Adapter atualizado");
            });
        }
    }
}
