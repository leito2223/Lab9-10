package com.example.lab9_10.Logica;

import java.io.Serializable;

public class Curso implements Serializable {
    private String id;
    private String descripcion;
    private int creditos;
    private String idAlumno;

    public Curso(String id, String descripcion, int creditos, String idAlumno) {
        this.id = id;
        this.descripcion = descripcion;
        this.creditos = creditos;
        this.idAlumno = idAlumno;
    }

    public Curso() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public String getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(String idAlumno) {
        this.idAlumno = idAlumno;
    }

    @Override
    public String toString() {
        return "Curso{" +
                "id='" + id + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", creditos=" + creditos +
                ", idAlumno='" + idAlumno + '\'' +
                '}';
    }
}
