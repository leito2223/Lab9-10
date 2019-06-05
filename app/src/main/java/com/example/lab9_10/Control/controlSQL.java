package com.example.lab9_10.Control;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.support.annotation.Nullable;

import com.example.lab9_10.Logica.Alumno;
import com.example.lab9_10.Logica.Curso;

import java.util.ArrayList;


public class controlSQL extends SQLiteOpenHelper {

    String sqlAlumno= "CREATE TABLE IF NOT EXISTS Alumno (idAlumno TEXT PRIMARY KEY, nombre TEXT,apellido TEXT,edad INTEGER)";
    String sqlCurso= "CREATE TABLE IF NOT EXISTS Curso (id TEXT PRIMARY KEY, descripcion TEXT,creditos INTEGER,idAlumno TEXT,FOREIGN KEY(idAlumno) REFERENCES Alumno(idAlumno))";

    public controlSQL(Context context) {
        super(context, "labBD.sql" , null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlAlumno);
        db.execSQL(sqlCurso);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Alumno");
        db.execSQL("DROP TABLE IF EXISTS Curso");
        onCreate(db);
    }
    public boolean insertAlumno (Alumno alumno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("idAlumno", alumno.getIdAlumno());
        contentValues.put("nombre", alumno.getNombre());
        contentValues.put("apellido", alumno.getApellido());
        contentValues.put("edad", alumno.getEdad());
        db.insert("Alumno", null, contentValues);
        return true;
    }
    public boolean insertCurso (Curso curso) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("creditos", curso.getCreditos());
        contentValues.put("idAlumno", curso.getIdAlumno());
        contentValues.put("id", curso.getId());
        contentValues.put("descripcion", curso.getDescripcion());
        db.insert("Curso", null, contentValues);
        return true;
    }
    public boolean updateCurso (Curso curso) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", curso.getId());
        contentValues.put("descripcion", curso.getDescripcion());
        contentValues.put("creditos", curso.getCreditos());
        contentValues.put("idAlumno", curso.getIdAlumno());
        db.update("Curso", contentValues, "id = ? ", new String[] { curso.getId() } );
        return true;
    }
    public boolean updateAlumno (Alumno alumno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("idAlumno", alumno.getIdAlumno());
        contentValues.put("nombre", alumno.getNombre());
        contentValues.put("apellido", alumno.getApellido());
        contentValues.put("edad", alumno.getEdad());
        db.update("Alumno", contentValues, "idAlumno = ? ", new String[] { alumno.getIdAlumno() } );
        return true;
    }
    public Integer deleteAlumno (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Alumno",
                "idAlumno = ? ",
                new String[] { id });
    }
    public Integer deleteCurso (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Curso",
                "id = ? ",
                new String[] { id });
    }
    public ArrayList<Alumno> listaAlumnos() {
        ArrayList<Alumno> array_list = new ArrayList<Alumno>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Alumno", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Alumno alumno = new Alumno();
            alumno.setIdAlumno(String.valueOf(res.getString(res.getColumnIndex("idAlumno"))));
            alumno.setNombre(String.valueOf(res.getString(res.getColumnIndex("nombre"))));
            alumno.setApellido(String.valueOf(res.getString(res.getColumnIndex("apellido"))));
            alumno.setEdad(res.getInt(res.getColumnIndex("edad")));
            array_list.add(alumno);
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<Curso> listaCursos() {
        ArrayList<Curso> array_list = new ArrayList<Curso>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Curso", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Curso curso = new Curso();
            curso.setId(String.valueOf(res.getString(res.getColumnIndex("id"))));
            curso.setDescripcion(String.valueOf(res.getString(res.getColumnIndex("descripcion"))));
            curso.setCreditos(res.getInt(res.getColumnIndex("creditos")));
            curso.setIdAlumno(String.valueOf(res.getString(res.getColumnIndex("idAlumno"))));
            array_list.add(curso);
            res.moveToNext();
        }
        return array_list;
    }
}