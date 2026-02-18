package com.example.ejercicio2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private DAOImpl myDb;
    private DAO dao;
    private EditText title;
    private EditText director;
    private EditText etSearchDirector;
    private EditText year;
    private Button search;
    private Button addMovie;
    private RecyclerView recyclerView;


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        dao = new DAOImpl(this);
        title = findViewById(R.id.tvTitle);
        director = findViewById(R.id.tvDirector);
        year = findViewById(R.id.tvYear);
        addMovie = (Button) findViewById(R.id.btnAddMovie);
        search = (Button) findViewById(R.id.btnSearch);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        search.setOnClickListener(v -> searchPelicula());
        addMovie.setOnClickListener(v -> addPelicula());

    }
    public void addPelicula() {
        addMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInserted = dao.insert(title.getText().toString(), director.getText().toString(), year.getText().toString());
                if (isInserted) {
                    Toast.makeText(MainActivity.this, "Datos insertados", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error al insertar", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void searchPelicula() {

        setContentView(R.layout.item_movie);
       
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = dao.getAllData();
                if (res.getCount() > 0) {
                    StringBuffer buffer = new StringBuffer();

                    while (res.moveToNext()) {
                        buffer.append("ID :" + res.getString(0) + "\n");
                        buffer.append("titulo :" + res.getString(1) + "\n");
                        buffer.append("director :" + res.getString(2) + "\n");
                        buffer.append("año :" + res.getString(3) + "\n\n");
                    }

                    showMessage("Datos", buffer.toString());
                } else
                    showMessage("Error", "Ningún dato encontrado");
            }
        });

    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


}





