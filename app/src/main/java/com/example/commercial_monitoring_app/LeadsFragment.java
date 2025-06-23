package com.example.commercial_monitoring_app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.commercial_monitoring_app.model.Agendamento;
import com.example.commercial_monitoring_app.model.Oportunidade;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import android.graphics.Color;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeadsFragment extends Fragment {

    private PieChart pieChart;
    private LinearLayout chartContainer, atividadesContainer, oportunidadesContainer;
    private Button btnToggleChart, btnToggleAtividades, btnToggleOportunidades, btnCaptacao, btnAcompanhamento;
    private View indicatorCaptacao, indicatorAcompanhamento;
    private ArrayList<String> steps = new ArrayList<>();
    private boolean isChartVisible = true;
    private boolean isAtividadesVisible = true;
    private boolean isOportunidadesVisible = true;
    private boolean isCaptacaoMode = true;
    private LinearLayout percentageContainer;

    //TextViews do Status Atividade
    private TextView tvTarefasTotal, tvTarefasPendentes, tvTarefasVencidas;

    //TextView do Status Oportunidades
    private TextView tvCaptacaoTotal, tvCaptacaoGanhas, tvCaptacaoPerdidas, tvCaptacaoEmAberto;
    private TextView tvAcompanhamentoTotal, tvAcompanhamentoGanhas, tvAcompanhamentoPerdidas, tvAcompanhamentoEmAberto;


    private final List<String> etapasAcompanhamento = Arrays.asList("Em Alerta", "Evadidos", "Alunos Regulares");


    // Mapeamento de cores para etapas de captação
    private final Map<String, Integer> coresCaptacao = new HashMap<String, Integer>() {{
        put("Potencial", R.color.light_blue);
        put("Interessado", R.color.medium_blue);
        put("Inscrito parcial", R.color.blue_teal);
        put("Inscrito", R.color.teal_green);
        put("Confirmado", R.color.medium_green);
        put("Convocado", R.color.sea_green);
        put("Matriculado", R.color.dark_green);
    }};

    // Mapeamento de cores para etapas de acompanhamento
    private final Map<String, Integer> coresAcompanhamento = new HashMap<String, Integer>() {{
        put("Em Alerta", android.R.color.holo_red_dark);
        put("Evadidos", android.R.color.holo_orange_light);
        put("Alunos Regulares", android.R.color.holo_green_dark);
    }};

    // Ordem grafico
    private final List<String> ordemCaptacao = Arrays.asList(
            "Potencial",
            "Interessado",
            "Inscrito parcial",
            "Inscrito",
            "Confirmado",
            "Convocado",
            "Matriculado"
    );

    private final List<String> ordemAcompanhamento = Arrays.asList(
            "Alunos Regulares",
            "Evadidos",
            "Em Alerta"
    );

    public LeadsFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_leads, container, false);
        initViews(view);



        setupCharts();
        setupClickListeners();
        updateButtonStates();
        btnToggleChart.setTextColor(ContextCompat.getColor(btnToggleChart.getContext(), android.R.color.holo_red_dark));
        btnToggleAtividades.setTextColor(ContextCompat.getColor(btnToggleAtividades.getContext(), android.R.color.holo_red_dark));
        btnToggleOportunidades.setTextColor(ContextCompat.getColor(btnToggleOportunidades.getContext(), android.R.color.holo_red_dark));
        updateChart();

        setupTarefas();
        setupOportunidades();

        return view;
    }

    private void initViews(View view){
        // Inicializar views
        pieChart = view.findViewById(R.id.pieChart);
        chartContainer = view.findViewById(R.id.chartContainer);
        atividadesContainer = view.findViewById(R.id.atividadesContainer);
        oportunidadesContainer = view.findViewById(R.id.oportunidadesContainer);
        btnToggleChart = view.findViewById(R.id.btnToggleChart);
        btnToggleAtividades = view.findViewById(R.id.btnToggleAtividades);
        btnToggleOportunidades = view.findViewById(R.id.btnToggleOportunidades);
        btnCaptacao = view.findViewById(R.id.btnCaptacao);
        btnAcompanhamento = view.findViewById(R.id.btnAcompanhamento);
        indicatorCaptacao = view.findViewById(R.id.indicatorCaptacao);
        indicatorAcompanhamento = view.findViewById(R.id.indicatorAcompanhamento);
        percentageContainer = view.findViewById(R.id.percentageContainer);

        //TextViews Tarefas Status
        tvTarefasPendentes = view.findViewById(R.id.tvTarefasPendentes);
        tvTarefasVencidas = view.findViewById(R.id.tvTarefasVencidas);
        tvTarefasTotal= view.findViewById(R.id.tvTotalTarefas);

        //TexttView Oportunidades Status
        tvCaptacaoTotal =  view.findViewById(R.id.tvTotalCaptacao);
        tvCaptacaoGanhas =  view.findViewById(R.id.tvCaptacaoGanhas);
        tvCaptacaoPerdidas =  view.findViewById(R.id.tvCaptacaoPerdidas);
        tvCaptacaoEmAberto =  view.findViewById(R.id.tvCaptacaoAberto);

        tvAcompanhamentoTotal = view.findViewById(R.id.tvTotalAcompanhamento);
        tvAcompanhamentoGanhas = view.findViewById(R.id.tvAcompanhamentoGanhas);
        tvAcompanhamentoPerdidas = view.findViewById(R.id.tvAcompanhamentoPerdidas);
        tvAcompanhamentoEmAberto = view.findViewById(R.id.tvAcompanhamentoAberto);


    }

    private void setupTarefas(){
        List<Agendamento> agendamentoList = MyApp.getAgendamentoList();
        int tarefasPendentes = 0, tarefasVencidas = 0;

        for(int i=0; i<agendamentoList.size(); i++){
            Agendamento agendamento = agendamentoList.get(i);
            if(agendamento.getStatus().equals("1")){
                tarefasPendentes++;
            } else if (agendamento.getStatus().equals("2")) {
                tarefasVencidas++;
            }
        }

        tvTarefasVencidas.setText(String.valueOf(tarefasVencidas));
        tvTarefasPendentes.setText(String.valueOf(tarefasPendentes));
        tvTarefasTotal.setText(String.valueOf(tarefasPendentes+tarefasVencidas));
    }

    private void setupOportunidades(){
        List<Oportunidade> oportunidadeList = MyApp.getOportunidadeList();
        int capAberto = 0;
        int acomAberto = 0;

        for(int i=0; i<oportunidadeList.size(); i++){
            Oportunidade oportunidade = oportunidadeList.get(i);
            if(etapasAcompanhamento.contains(oportunidade.getEtapaNome())){
                acomAberto++;
            } else {
                capAberto++;
            }
        }
        tvAcompanhamentoEmAberto.setText(String.valueOf(acomAberto));
        tvCaptacaoEmAberto.setText(String.valueOf(capAberto));
    }

    private void setupClickListeners() {
        // Toggle visibilidade do gráfico
        btnToggleChart.setOnClickListener(v -> {
            isChartVisible = !isChartVisible;
            chartContainer.setVisibility(isChartVisible ? View.VISIBLE : View.GONE);
            btnToggleChart.setText(isChartVisible ? "Esconder" : "Mostrar");

            if (isChartVisible) {
                btnToggleChart.setTextColor(Color.parseColor("#d14747"));
            } else {
                btnToggleChart.setTextColor(Color.parseColor("#5DA9D4"));
            }
        });

        // Toggle visibilidade das atividades
        btnToggleAtividades.setOnClickListener(v -> {
            isAtividadesVisible = !isAtividadesVisible;
            atividadesContainer.setVisibility(isAtividadesVisible ? View.VISIBLE : View.GONE);
            btnToggleAtividades.setText(isAtividadesVisible ? "Esconder" : "Mostrar");

            if (isAtividadesVisible) {
                btnToggleAtividades.setTextColor(Color.parseColor("#d14747"));
            } else {
                btnToggleAtividades.setTextColor(Color.parseColor("#5DA9D4"));
            }
        });

        // Toggle visibilidade das oportunidades
        btnToggleOportunidades.setOnClickListener(v -> {
            isOportunidadesVisible = !isOportunidadesVisible;
            oportunidadesContainer.setVisibility(isOportunidadesVisible ? View.VISIBLE : View.GONE);
            btnToggleOportunidades.setText(isOportunidadesVisible ? "Esconder" : "Mostrar");

            if (isOportunidadesVisible) {
                btnToggleOportunidades.setTextColor(Color.parseColor("#d14747"));
            } else {
                btnToggleOportunidades.setTextColor(Color.parseColor("#5DA9D4"));
            }
        });

        // Alternar para modo Captação
        btnCaptacao.setOnClickListener(v -> {
            if (!isCaptacaoMode) {
                isCaptacaoMode = true;
                updateButtonStates();
                updateChart();
            }
        });

        // Alternar para modo Acompanhamento
        btnAcompanhamento.setOnClickListener(v -> {
            if (isCaptacaoMode) {
                isCaptacaoMode = false;
                updateButtonStates();
                updateChart();
            }
        });
    }

    private void updateButtonStates() {
        if (isCaptacaoMode) {
            // Botões
            btnCaptacao.setBackgroundTintList(getResources().getColorStateList(R.color.purple_700, null));
            btnAcompanhamento.setBackgroundTintList(getResources().getColorStateList(R.color.purple_200, null));

            // Marcadores
            indicatorCaptacao.setVisibility(View.VISIBLE);
            indicatorAcompanhamento.setVisibility(View.INVISIBLE);
        } else {

            btnCaptacao.setBackgroundTintList(getResources().getColorStateList(R.color.purple_200, null));
            btnAcompanhamento.setBackgroundTintList(getResources().getColorStateList(R.color.purple_700, null));


            indicatorCaptacao.setVisibility(View.INVISIBLE);
            indicatorAcompanhamento.setVisibility(View.VISIBLE);
        }
    }

    private void updateChart() {
        List<Oportunidade> oportunidadeList = MyApp.getOportunidadeList();
        Map<String, Integer> etapaCountMap = new HashMap<>();

        for (Oportunidade oportunidade : oportunidadeList) {
            String etapa = oportunidade.getEtapaNome();
            if (etapa == null) continue;

            boolean incluirEtapa = isCaptacaoMode ?
                    !etapasAcompanhamento.contains(etapa) :
                    etapasAcompanhamento.contains(etapa);

            if (incluirEtapa) {
                if (etapaCountMap.containsKey(etapa)) {
                    etapaCountMap.put(etapa, etapaCountMap.get(etapa) + 1);
                } else {
                    etapaCountMap.put(etapa, 1);
                }
            }
        }

        // Organizar dados na ordem
        List<String> ordemAtual = isCaptacaoMode ? ordemCaptacao : ordemAcompanhamento;
        List<String> etapasOrdenadas = new ArrayList<>();
        List<Integer> valoresOrdenados = new ArrayList<>();

        for (String etapa : ordemAtual) {
            if (etapaCountMap.containsKey(etapa) && etapaCountMap.get(etapa) > 0) {
                etapasOrdenadas.add(etapa);
                valoresOrdenados.add(etapaCountMap.get(etapa));
            }
        }

        updateSteps(etapasOrdenadas);
        updateValues(valoresOrdenados);
    }

    private void setupCharts() {
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText(isCaptacaoMode ? "Captação" : "Acompanhamento");
        pieChart.setCenterTextSize(12f);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.getLegend().setEnabled(false);

        pieChart.setExtraOffsets(5, 5, 5, 5);
    }

    public void updateSteps(List<String> steps) {
        this.steps = new ArrayList<>(steps);
        // Atualizar texto central baseado no modo
        pieChart.setCenterText(isCaptacaoMode ? "Captação" : "Acompanhamento");
        pieChart.invalidate();
    }

    public void updateValues(List<Integer> pieValues) {
        if (pieValues.isEmpty()) {
            pieChart.clear();
            percentageContainer.removeAllViews();
            return;
        }

        List<PieEntry> pieEntries = new ArrayList<>();

        for (int i = 0; i < pieValues.size() && i < steps.size(); i++) {
            pieEntries.add(new PieEntry(pieValues.get(i), steps.get(i)));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");


        List<Integer> colors = new ArrayList<>();
        Map<String, Integer> coresAtual = isCaptacaoMode ? coresCaptacao : coresAcompanhamento;

        for (int i = 0; i < pieEntries.size(); i++) {
            String etapa = steps.get(i);
            Integer corEtapa = coresAtual.get(etapa);

            if (corEtapa != null) {
                colors.add(getResources().getColor(corEtapa, null));
            } else {
                // Cor padrão caso a etapa não esteja mapeada
                colors.add(getResources().getColor(R.color.purple_200, null));
            }
        }

        pieDataSet.setColors(colors);

        pieDataSet.setDrawValues(true);
        pieDataSet.setValueTextSize(11f);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setSliceSpace(1f);
        pieDataSet.setSelectionShift(3f);

        PieData pieData = new PieData(pieDataSet);

        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f", value);
            }
        });

        pieChart.setData(pieData);
        pieChart.invalidate();

        updatePercentageDisplay(pieValues);
    }

    private void updatePercentageDisplay(List<Integer> values) {
        percentageContainer.removeAllViews();

        if (values.isEmpty()) {
            return;
        }

        int total = 0;
        for (int value : values) {
            total += value;
        }

        Map<String, Integer> coresAtual = isCaptacaoMode ? coresCaptacao : coresAcompanhamento;

        for (int i = 0; i < values.size() && i < steps.size(); i++) {
            LinearLayout itemLayout = new LinearLayout(getContext());
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setGravity(Gravity.CENTER_VERTICAL);
            itemLayout.setPadding(0, 4, 0, 4);

            // Indicador de cor
            View colorIndicator = new View(getContext());
            LinearLayout.LayoutParams colorParams = new LinearLayout.LayoutParams(24, 24);
            colorParams.setMarginEnd(12);
            colorIndicator.setLayoutParams(colorParams);

            String etapa = steps.get(i);
            Integer corEtapa = coresAtual.get(etapa);
            if (corEtapa != null) {
                colorIndicator.setBackgroundColor(getResources().getColor(corEtapa, null));
            } else {
                colorIndicator.setBackgroundColor(getResources().getColor(R.color.purple_200, null));
            }

            TextView textView = new TextView(getContext());
            float percentage = (float) values.get(i) / total * 100;
            String text = String.format("%s: %d (%.1f%%)", steps.get(i), values.get(i), percentage);
            textView.setText(text);
            textView.setTextSize(14);
            textView.setTextColor(Color.parseColor("#333333"));

            itemLayout.addView(colorIndicator);
            itemLayout.addView(textView);
            percentageContainer.addView(itemLayout);
        }
    }
}