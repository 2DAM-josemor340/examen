package com.example.ejercicio2;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DAO dao;
    private EditText etSearchDirector;
    private Button btnSearch, btnAddMovie;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Pelicula> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao = new DAOImpl(this);
        movieList = new ArrayList<>();

        etSearchDirector = findViewById(R.id.etSearchDirector);
        btnSearch = findViewById(R.id.btnSearch);
        btnAddMovie = findViewById(R.id.btnAddMovie);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        refreshList(null);

        // Eventos
        btnSearch.setOnClickListener(v -> {
            String query = etSearchDirector.getText().toString().trim();
            refreshList(query);
        });

        btnAddMovie.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_movie, null);
        EditText etTitle = dialogView.findViewById(R.id.etTitle);
        EditText etDirector = dialogView.findViewById(R.id.etDirector);
        EditText etYear = dialogView.findViewById(R.id.etYear);

        new AlertDialog.Builder(this)
                .setTitle("Nueva Película")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String t = etTitle.getText().toString();
                    String d = etDirector.getText().toString();
                    String y = etYear.getText().toString();

                    if (!t.isEmpty() && !d.isEmpty()) {
                        boolean inserted = dao.insert(t, d, y);
                        if (inserted) {
                            Toast.makeText(this, "Añadida con éxito", Toast.LENGTH_SHORT).show();
                            refreshList(null);
                        }
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void refreshList(String filterDirector) {
        movieList.clear();
        Cursor res = dao.getAllData();

        if (res != null && res.moveToFirst()) {
            do {
                String titulo = res.getString(1);
                String director = res.getString(2);
                String year = res.getString(3);

                if (filterDirector == null || filterDirector.isEmpty() ||
                        director.toLowerCase().contains(filterDirector.toLowerCase())) {
                    movieList.add(new Pelicula(titulo, director, year));
                }
            } while (res.moveToNext());
        }

        adapter = new MovieAdapter(movieList);
        recyclerView.setAdapter(adapter);
    }
}