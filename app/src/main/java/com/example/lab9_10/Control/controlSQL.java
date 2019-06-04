package com.example.lab9_10.Control;
import android.app.Activity;
import android.database.sqlite.*;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lab9_10.R;


public class controlSQL extends Activity {
    SQLiteDatabase db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// filePath is a complete destination of the form
// "/data/data/<namespace>/<databaseName>"
// "/sdcard/<databasename>"
        try {
            db = SQLiteDatabase.openDatabase(
                    "/data/data/cis493.sqldatabases/labDB",
                    null,
                    SQLiteDatabase.CREATE_IF_NECESSARY);
            db.close();
        }
        catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


        db.beginTransaction();
        try {
            db.execSQL("create table tblAlumno (" +
                    "idAlumno text PRIMARY KEY," +
                    "nombre text," +
                    "apellido text,"+
                    "edad integer);");
            db.execSQL("create table Curso (" +
                    "id text PRIMARY KEY," +
                    "descripcion text," +
                    "creditos integer,"+
                    "idAlumno text NOT NULL);");
            db.setTransactionSuccessful(); //commit your changes
        }
        catch (SQLiteException e) {
//report problem
        }
        finally {
            db.endTransaction();
        }

    }// onCreate

}
