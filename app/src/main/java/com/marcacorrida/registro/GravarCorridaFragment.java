package com.marcacorrida.registro;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.marcacorrida.R;
import com.marcacorrida.datasource.CorridaRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GravarCorridaFragment extends Fragment {
    private Chronometer chronometer; //Como os métodos de contagem e formatação do Chronometo não são utilizados, poderia ser substituido por uma TextView
    private TextView tvTotalPassos;
    private ImageButton btStart, btRefresh;
    private GravarCorridaViewModel model;

    public GravarCorridaFragment() {
    }

    public static GravarCorridaFragment newInstance() {
        GravarCorridaFragment fragment = new GravarCorridaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this, new GravarCorridaViewModel.ViewModelFactory(
                new CorridaRepository(getActivity().getApplicationContext()),
                (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE)
                )).get(GravarCorridaViewModel.class);
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
        btRefresh = view.findViewById(R.id.bt_refresh);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { //O sensor de passos requer essa permissão no android 10+
            if (ContextCompat.checkSelfPermission(
                    getContext(), Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                })
                        .launch(Manifest.permission.ACTIVITY_RECOGNITION);
            }
        }

        btStart.setOnClickListener( (btn) -> {
            if (model.getIsPaused().getValue()) {
                model.startRecord();
            } else {
                model.pauseRecord();
            }
        });
        btRefresh.setOnClickListener( (btn) -> {
            if(model.getIsPaused().getValue() && model.getSessionTime().getValue() > 0) {
                showDialog();
            }
        });

        final Observer<Integer> stepObserver = (step) -> {
            tvTotalPassos.setText( String.format(Locale.ENGLISH, "%d", step) );
        };
        final Observer<Long> timeObserver = (milissec) -> {
            final long H = 3600 * 1000, M = 60 * 1000, S = 1000;
            long total = milissec;
            long hour = total / H;
            total -= hour * H;
            long min = total / M;
            total -= min * M;
            long sec = total / S;
            total -= sec * S;
            chronometer.setText( String.format(Locale.ENGLISH, "%02d:%02d:%02d,%02d", hour, min, sec, total / 10) );

        };
        final Observer<Boolean> pauseObserver = (isPaused) -> {
            btStart.setImageResource( isPaused ? R.drawable.ic_play : R.drawable.ic_pause );
        };

        model.getsessionSteps().observe(getViewLifecycleOwner(), stepObserver);
        model.getSessionTime().observe(getViewLifecycleOwner(), timeObserver);
        model.getIsPaused().observe(getViewLifecycleOwner(), pauseObserver);
    }

    private void showDialog() {
        AlertDialog.Builder nomeDialog = new AlertDialog.Builder(getContext());
        LinearLayout layout = new LinearLayout(getContext());
        EditText edtTxt = new EditText(getContext());
        edtTxt.setHint("Nome da corrida");
        layout.addView(edtTxt);
        layout.setOrientation(LinearLayout.VERTICAL);
        nomeDialog.setView(layout);

        nomeDialog.setNegativeButton("Cancelar", (dialogInterface, btn_id) -> {
            model.endRecord();
            dialogInterface.cancel();
            setButtonsClickable(true);
        });
        nomeDialog.setPositiveButton("Confirmar", (dialogInterface, btn_id) -> {
            model.saveRecord(edtTxt.getText().toString());
            model.endRecord();
            setButtonsClickable(true);
        });
        nomeDialog.setOnDismissListener((dialogInterface) -> {
            model.endRecord();
            setButtonsClickable(true);
        });
        setButtonsClickable(false);
        nomeDialog.show();
    }

    private void setButtonsClickable(boolean clickable) {
        btStart.setClickable(clickable);
        btRefresh.setClickable(clickable);
    }

    private void reset() {
        setButtonsClickable(true);
    }
}