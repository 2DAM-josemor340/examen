package com.example.ejercicio2;

import java.io.Serializable;

public class Pelicula implements Serializable {

    private String titulo;
    private String director;
    private String year;


    public Pelicula(String titulo, String director,String year) {

        this.titulo = titulo;
        this.director = director;
        this.year = year;

    }

    public String getTitulo() { return titulo; }
    public String getDirector() { return director; }
    public String getYear() { return year; }

}
