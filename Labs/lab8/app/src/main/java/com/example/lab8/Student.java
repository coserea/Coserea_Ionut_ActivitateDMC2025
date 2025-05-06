package com.example.lab8;

import androidx.annotation.NonNull;

public class Student {
    private int id;
    private String nume;
    private int varsta;

    public Student() { }  // necesar pentru Firebase

    public Student(int id, String nume, int varsta) {
        this.id = id;
        this.nume = nume;
        this.varsta = varsta;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public int getVarsta() { return varsta; }

    public void setId(int id) { this.id = id; }
    public void setNume(String nume) { this.nume = nume; }
    public void setVarsta(int varsta) { this.varsta = varsta; }

    @NonNull
    @Override
    public String toString() {
        return id + ": " + nume + " - " + varsta + " ani";
    }
}
