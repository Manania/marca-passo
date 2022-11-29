package com.marcacorrida.relatorios;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.marcacorrida.R;
import com.marcacorrida.datasource.CorridaRepository;
import com.marcacorrida.model.Corrida;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RelatoriosFragment extends Fragment {

    public static RelatoriosFragment newInstance() {
        return new RelatoriosFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_relatorios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LineChart lineChart = view.findViewById(R.id.lineChart);


        List<Corrida> rawData = getCorridaData();
        if(rawData == null) {
            return;
        }
        setUpLineChart(lineChart, rawData);
        LineDataSet stepsAllTime = new LineDataSet(buildChartEntry(rawData), "Quantidade de passos");
        stepsAllTime.setLineWidth(3f);
        stepsAllTime.setValueTextSize(15f);
        stepsAllTime.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        stepsAllTime.setColor( ContextCompat.getColor(getContext(), R.color.teal_200) );
        stepsAllTime.setValueTextColor( ContextCompat.getColor(getContext(), R.color.black) );
        //stepsAllTime.enableDashedLine(20f, 10f, 0f);

        LineData lineData = new LineData(stepsAllTime);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private void setUpLineChart(LineChart lineChart, List<Corrida> data) {
        lineChart.animateX(1200, Easing.EaseInSine);
        lineChart.getDescription().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1F);
        xAxis.setValueFormatter( new StepsOverTimeFormatter(data) );

        lineChart.getAxisLeft().setAxisMinimum(0f);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.setExtraRightOffset(30f);

        Legend legend = lineChart.getLegend();

        legend.setOrientation( Legend.LegendOrientation.VERTICAL );
        legend.setVerticalAlignment( Legend.LegendVerticalAlignment.TOP );
        legend.setHorizontalAlignment( Legend.LegendHorizontalAlignment.CENTER );
        legend.setTextSize(15f);
        legend.setForm(Legend.LegendForm.LINE);
    }

    private List<Entry> buildChartEntry(List<Corrida> corridas) {
        List<Entry> steps = new ArrayList<>();
        for(Corrida corrida : corridas) {
            steps.add( new Entry( (float) steps.size(), corrida.getNumPassos() ) );
        }
        return steps;
    }

    private List<Corrida> getCorridaData() {
        try(CorridaRepository repository = new CorridaRepository(getContext().getApplicationContext())){
            return repository.listar(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class StepsOverTimeFormatter extends IndexAxisValueFormatter {
        private List<Corrida> corridas;
        private DateFormat DDMMYY_FORMATER = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());

        public StepsOverTimeFormatter(List<Corrida> corridas) {
            this.corridas = corridas;
        }
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            int i = (int) value;
            if(i >= 0 && i < corridas.size()) {
                return DDMMYY_FORMATER.format(corridas.get(i).getData());
            }
            return super.getAxisLabel(value, axis);
        }
    }
}