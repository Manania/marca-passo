package com.marcacorrida.registro;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.marcacorrida.R;
import com.marcacorrida.datasource.CorridaRepository;
import com.marcacorrida.model.Corrida;

import java.io.IOException;
import java.util.Date;

public class GravarCorridaFragment extends Fragment {
    long tMilliSec, tStart, tBuff;
    private int passoInicial, totalPassos;
    private boolean isResume;
    Chronometer chronometer; //Como os métodos de contagem e formatação do Chronometo não são utilizados, poderia ser substituido por uma TextView
    private TextView tvTotalPassos;
    ImageButton btStart, btStop, btRefresh;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    Handler handler;
    private final SensorEventListener stepCounterSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (passoInicial == 0) {
                passoInicial = (int) sensorEvent.values[0];
            }
            totalPassos = (int) sensorEvent.values[0] - passoInicial;
            tvTotalPassos.setText(String.valueOf(totalPassos));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    /**
     * Atualiza o cronometro.
     * É possivel controlar a frequencia alterando o atraso entre reagendamentos
     */
    private final Runnable updateClock = new Runnable() {
        @Override
        public void run() {
            tMilliSec = SystemClock.uptimeMillis() - tStart;
            long tUpdate = tBuff + tMilliSec;
            int sec = (int) (tUpdate / 1000);
            int min = sec / 60;
            sec = sec % 60;
            int milliSec = (int) (tUpdate % 100);
            chronometer.setText(String.format("%02d", min) + ":" + String.format("%02d", sec) + ":" + String.format("%02d", milliSec));
            handler.postDelayed(this, 60); //16hz
        }
    };

    public GravarCorridaFragment() {
    }

    public static GravarCorridaFragment newInstance() {
        GravarCorridaFragment fragment = new GravarCorridaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gravar_corrida, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTotalPassos = view.findViewById(R.id.tvTotalPassos);
        chronometer = view.findViewById(R.id.chronometer);
        btStart = view.findViewById(R.id.bt_start);
        btStop = view.findViewById(R.id.bt_stop);
        btRefresh = view.findViewById(R.id.bt_refresh);

        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        handler = new Handler(Looper.getMainLooper());
        btStart.setOnClickListener((btn) -> {
            startRecording();
        });
        btRefresh.setOnClickListener((btn) -> {
            reset();
        });
        btStop.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { //O sensor de passos requer essa permissão no android 10+
            if (ContextCompat.checkSelfPermission(
                    getContext(), Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                })
                        .launch(Manifest.permission.ACTIVITY_RECOGNITION);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void startRecording() {
        if (!isResume) {
            tStart = SystemClock.uptimeMillis();
            handler.post(updateClock);
            isResume = true;
            btStart.setImageResource(R.drawable.ic_pause);
            sensorManager.registerListener(stepCounterSensorListener, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            tBuff += tMilliSec;
            handler.removeCallbacks(updateClock);
            isResume = false;
            btStart.setImageResource(R.drawable.ic_play);
            sensorManager.unregisterListener(stepCounterSensorListener, stepCounterSensor);
        }
    }

    private void reset() {
        if (!isResume) {
            if (tMilliSec > 0) {
                salvar();
            }
            btStart.setImageResource(R.drawable.ic_play);
            tMilliSec = 0L;
            tStart = 0L;
            tBuff = 0L;
            totalPassos = 0;
            passoInicial = 0;
            chronometer.setText("00:00:00");
            tvTotalPassos.setText("0");
        }
    }

    /**
     * Cria uma nova thread para salvar a sessão atual.
     * Tecnicamente, é recomendável extrair trabalhos pesados, como acesso a banco de dados, da thread
     * principal para manter a responsividade.
     * Mas eu fiz isso por causa da assincronia de AlertDialog.
     */
    private void salvar() {
        Corrida corrida = new Corrida(null, totalPassos, tMilliSec, new Date(System.currentTimeMillis()));
        new Thread(new SalvarCorridaTask(corrida)).start();
    }

    private class SalvarCorridaTask implements Runnable {
        private final Corrida corrida;

        SalvarCorridaTask(Corrida corrida) {
            this.corrida = corrida;
        }

        @Override
        public void run() {
            showDialog();
        }

        /**
         * AlertDialog é assincrono e não tem métodos com retorno. Sem tempo e paciencia para melhorar isso.
         */
        private void showDialog() {
            AlertDialog.Builder nomeDialog = new AlertDialog.Builder(getContext());
            LinearLayout layout = new LinearLayout(getContext());
            EditText edtTxt = new EditText(getContext());
            edtTxt.setHint("Nome da corrida");
            layout.addView(edtTxt);
            layout.setOrientation(LinearLayout.VERTICAL);
            nomeDialog.setView(layout);
            nomeDialog.setNegativeButton("Cancelar", (dialogInterface, btn_id) -> dialogInterface.cancel());
            nomeDialog.setPositiveButton("Confirmar", (dialogInterface, btn_id) -> {
                salvar(edtTxt.getText().toString());
            });
            new Handler(Looper.getMainLooper()).post(nomeDialog::show); //A renderização sempre ocorre na thread principal.
        }

        private void salvar(String nome) {
            corrida.setNome(nome);
            try (CorridaRepository repository = new CorridaRepository(getContext())) {
                repository.criar(corrida);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}