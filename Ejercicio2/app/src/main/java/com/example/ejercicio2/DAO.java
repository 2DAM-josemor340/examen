package com.example.ejercicio2;

import android.database.Cursor;

public interface DAO {
    public Cursor getAllData();
    public boolean insert(String titulo, String director, String year);
    public boolean update(String id, String titulo, String director, String year);
    public int delete(String id);
}
