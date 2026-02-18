package com.example.ejercicio1;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView tvStepCount, tvStatus, tvRecord, tvDistance;
    private static final String FILE_NAME = "historial.txt";
    private TareaPasos tarea;

    private int totalPasos = 0;
    private int ultimoHitoSonido = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStepCount = findViewById(R.id.tvStepCount);
        tvStatus = findViewById(R.id.tvStatus);
        tvRecord = findViewById(R.id.tvRecord);
        tvDistance = findViewById(R.id.tvDistance);


        cargarRecord();

        tarea = new TareaPasos();
        tarea.execute();
    }

    private void actualizarInterfaz(int pasos) {
        this.totalPasos = pasos;
        double metros = pasos * 0.7;

        tvStepCount.setText(String.valueOf(pasos));
        tvDistance.setText(String.format(Locale.getDefault(), "%.2f m", metros));

        if (pasos > 0) {
            tvStatus.setText("Estado: En movimiento");
        } else {
            tvStatus.setText(getString(R.string.initializing));
        }

        if (pasos >= ultimoHitoSonido + 100) {
            reproducirAlerta();
            ultimoHitoSonido = (pasos / 100) * 100;
            Toast.makeText(this, "¡Objetivo de 100 pasos alcanzado!", Toast.LENGTH_SHORT).show();
        }

        verificarNuevoRecord(pasos);
    }

    private void reproducirAlerta() {
        try {
            MediaPlayer mp = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
            if (mp != null) {
                mp.start();
                mp.setOnCompletionListener(MediaPlayer::release);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class TareaPasos extends AsyncTask<Void, Integer, Void> {
        private Random random = new Random();
        private int contadorLocal = 0;

        @Override
        protected Void doInBackground(Void... voids) {
            while (!isCancelled()) {
                try {

                    Thread.sleep(10000);

                    contadorLocal += random.nextInt(51) + 10;

                    publishProgress(contadorLocal);

                } catch (InterruptedException e) {
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int pasosActuales = values[0];
            actualizarInterfaz(pasosActuales);

                       saveToInternalStorage(pasosActuales);
        }
    }

    private void saveToInternalStorage(int pasos) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String log = "Fecha: " + timeStamp + " | Pasos: " + pasos + "\n";

        try (FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_APPEND)) {
            fos.write(log.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void verificarNuevoRecord(int pasos) {
        SharedPreferences prefs = getSharedPreferences("PrefsPasos", MODE_PRIVATE);
        int mejorMarca = prefs.getInt("mejor_marca", 0);

        if (pasos > mejorMarca) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("mejor_marca", pasos);
            editor.putString("fecha_mejor_marca", new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
            editor.apply();

            tvRecord.setText("Nuevo Récord: " + pasos);
        }
    }

    private void cargarRecord() {
        SharedPreferences prefs = getSharedPreferences("PrefsPasos", MODE_PRIVATE);
        int mejorMarca = prefs.getInt("mejor_marca", 0);
        if (mejorMarca > 0) {
            tvRecord.setText("Récord: " + mejorMarca);
        } else {
            tvRecord.setText(getString(R.string.no_record));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tarea != null) {
            tarea.cancel(true);
        }
    }
}