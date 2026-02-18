package com.example.ejercicio2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DAOImpl extends SQLiteOpenHelper implements DAO {
    public static final String DATABASE_NAME = "peliculas.db";
    private static final int VERSION = 13;
    public static final String TABLE_NAME = "pelicula";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TITULO";
    public static final String COL_3 = "DIRECTOR";
    public static final String COL_4 = "YEAR";

    public DAOImpl(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITULO VARCHAR(20), DIRECTOR VARCHAR(50), YEAR VARCHAR(4)) ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }

    public boolean insert(String titulo, String director, String year) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Datos a insertar
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, titulo);
        contentValues.put(COL_3, director);
        contentValues.put(COL_4, year);
        long result = db.insert(TABLE_NAME, null, contentValues);

        // Devuelve el row id, -1 si ha ocurrido algún error
        return result != -1;
    }

    public boolean update(String id, String titulo, String director, String year) {
        int nrows = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        // Datos a actualizar
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, titulo);
        contentValues.put(COL_3, director);
        contentValues.put(COL_4, year);

        nrows = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});

        return nrows > 0;
    }

    public int delete(String id) {
        int result = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE ID=" + id);
            result = 1;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;


    }
}
