package com.marcacorrida.registro;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.marcacorrida.datasource.CorridaRepository;
import com.marcacorrida.model.Corrida;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class GravarCorridaViewModel extends ViewModel {
    private final MutableLiveData<Long> sessionTime;
    private final MutableLiveData<Integer> sessionSteps;
    private final MutableLiveData<Boolean> isPaused;

    private long tempoAtual, instanteInicial, tempoAcumulado;
    private int passoAtual, passoInicial, passoAcumulado;
    private final SensorManager sensorManager;
    private final Sensor stepCounterSensor;
    private final SensorEventListener stepCounterSensorListener;
    private final Handler handler;
    private final Runnable updateClock;
    private final CorridaRepository repository;

    public GravarCorridaViewModel(SensorManager sensorManager, CorridaRepository repository) {
        this.sensorManager = sensorManager;
        this.stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        this.repository = repository;
        this.handler = new Handler(Looper.myLooper());
        this.updateClock = new Runnable() {
            @Override
            public void run() {
                tempoAtual = SystemClock.uptimeMillis() - instanteInicial;
                sessionTime.postValue(tempoAtual + tempoAcumulado);
                handler.postDelayed(this, 60);
            }
        };
        stepCounterSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(passoInicial == 0) {
                    passoInicial = (int) sensorEvent.values[0];
                }
                passoAtual = ((int) sensorEvent.values[0]) - passoInicial;
                sessionSteps.postValue(passoAtual + passoAcumulado);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        };
        sessionTime = new MutableLiveData<>();
        sessionSteps = new MutableLiveData<>();
        isPaused = new MutableLiveData<>();
        isPaused.setValue(true);
    }

    @NotNull
    public LiveData<Long> getSessionTime() {
        return sessionTime;
    }

    @NotNull
    public LiveData<Integer> getsessionSteps() {
        return sessionSteps;
    }

    @NotNull
    public LiveData<Boolean> getIsPaused() {
        return isPaused;
    }

    public void startRecord() {
        isPaused.setValue(false);
        instanteInicial = SystemClock.uptimeMillis();
        passoInicial = 0;
        handler.post(updateClock);
        sensorManager.registerListener(stepCounterSensorListener, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void pauseRecord() {
        isPaused.setValue(true);
        tempoAcumulado += tempoAtual;
        passoAcumulado += passoAtual;
        tempoAtual = 0;
        passoAtual = 0;
        handler.removeCallbacks(updateClock);
        sensorManager.unregisterListener(stepCounterSensorListener, stepCounterSensor);
    }

    public void endRecord() {
        isPaused.setValue(true);
        tempoAtual = 0L;
        instanteInicial = 0L;
        tempoAcumulado = 0L;
        passoAtual = 0;
        passoInicial = 0;
        passoAcumulado = 0;
        sessionTime.setValue(0L);
        sessionSteps.setValue(0);
    }

    public void saveRecord(String name) {
        Corrida corrida = new Corrida(name, passoAtual + passoAcumulado, tempoAtual + tempoAcumulado, new Date(System.currentTimeMillis()));
        new Thread( () -> repository.criar(corrida) ).start();
    }

    public static class ViewModelFactory implements ViewModelProvider.Factory {
        private final CorridaRepository repository;
        private final SensorManager manager;

        public ViewModelFactory(CorridaRepository repository, SensorManager manager) {
            this.repository = repository;
            this.manager = manager;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            try {
                    return modelClass.getConstructor(SensorManager.class, CorridaRepository.class)
                            .newInstance(manager, repository);
            } catch (IllegalAccessException |
                    InstantiationException |
                    InvocationTargetException|
                    NoSuchMethodException e) {
                    e.printStackTrace();
                return null;
            }
        }
    }
}
