package com.example.ejercicio1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView tvStepCount, tvStatus, tvRecord, tvDistance;
    private static final String FILE_NAME = "historial.txt";
    private TareaPasos tarea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tvStepCount = findViewById(R.id.tvStepCount);
        tvStatus = findViewById(R.id.tvStatus);
        tvRecord = findViewById(R.id.tvRecord);
        tvDistance = findViewById(R.id.tvDistance);
        tarea = new TareaPasos();
        tarea.execute();
        calcularPasos();

    }
        private void calcularPasos() {
            // 1. Generar valores aleatorios [0.0 - 1.0]
            Random random = new Random();

            int valStepCount = random.nextInt() * 100;
            int valRecord = 0;
            int valDistance = valStepCount * 1000;
            String valStatus = "";

            tvStepCount.setText((int) valStepCount);
            tvRecord.setText((int) valRecord);
            tvDistance.setText((int) valDistance);
            tvStatus.setText(valStatus);


            if (valStepCount > 0) {
                valStatus = "Corriendo";

            } else {
                valStatus = "Parado";
            }

            if (valStepCount >= 100) {
                Toast.makeText(this, "El numero de pasos es : " + valStepCount, Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(this, "Estado detectado: " + valStatus, Toast.LENGTH_SHORT).show();
        }


        private class TareaPasos extends AsyncTask<Void, Integer, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                while (true) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        break;
                    }

                }
                return null;
            }


            protected void onProgressUpdate(Integer... values) {
                int valStepCount;

            }

            private void saveToInternalStorage(int valStepCount) {
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                String dataToSave = "Inicio: " + timeStamp + " - total: " + valStepCount + "";

                try (FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_APPEND)) {
                    fos.write(dataToSave.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


        private void saveSearchToPreferences ( int valRecord){
            SharedPreferences prefs = getSharedPreferences("contadorPasos", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            editor.putString("Record de pasos.:", String.valueOf(valRecord));

            editor.putString("Fecha-Hora.:", timeStamp);

            editor.apply();
        }
    }
