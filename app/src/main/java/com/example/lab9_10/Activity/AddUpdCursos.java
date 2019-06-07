package com.example.lab9_10.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lab9_10.Control.controlSQL;
import com.example.lab9_10.Logica.Alumno;
import com.example.lab9_10.Logica.Curso;
import com.example.lab9_10.R;

import java.util.ArrayList;
import java.util.List;

public class AddUpdCursos extends AppCompatActivity {
    private FloatingActionButton fBtn;
    private boolean editable = true;
    private EditText id;
    private EditText descripcion;
    private EditText creditos;
   // private EditText idAlumno;
    private Spinner spiALum;
    private List<Alumno> usuarioList;
    private List<String> UsuarioListId = new ArrayList<>();
    private controlSQL model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_upd_cursos);
        editable = true;

        // button check
        fBtn = findViewById(R.id.addUpdCursoBtn);

        //cleaning stuff
        id = findViewById(R.id.idAddUpdCur);
        descripcion = findViewById(R.id.descripcionAddUpdCur);
        creditos = findViewById(R.id.creditosAddUpdCur);
       // idAlumno=findViewById(R.id.alumnoAddUpdCur);
        spiALum = findViewById(R.id.id_AlumnoSP);
        model = new controlSQL(this);

        id.setText("");
        descripcion.setText("");
        creditos.setText("");
      //  idAlumno.setText("");
        rellenaSpinner();

        //receiving data from admCursoActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            editable = extras.getBoolean("editable");
            if (editable) {   // is editing some row
                Curso aux = (Curso) getIntent().getSerializableExtra("Curso");
                id.setText(aux.getId());
                id.setEnabled(false);
                descripcion.setText(aux.getDescripcion());
                creditos.setText(String.valueOf(aux.getCreditos()));
                //idAlumno.setText(aux.getIdAlumno());
                ArrayAdapter<String> array_spinnerG=(ArrayAdapter<String>)spiALum.getAdapter();
                spiALum.setSelection(array_spinnerG.getPosition(aux.getIdAlumno()));
                //edit action
                fBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editCurso();
                    }
                });
            } else {         // is adding new Carrera object
                //add new action
                fBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addCurso();
                    }
                });
            }
        }
    }

    private void rellenaSpinner() {
        usuarioList = model.listaAlumnos();
        //Alumno aux = (Alumno) getIntent().getSerializableExtra("Curso");
        for(int i=0;i<usuarioList.size();i++)
        {

            UsuarioListId.add(usuarioList.get(i).getIdAlumno());
        }
     //   Log.w("RolList",RolListId.toString());
        spiALum.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,UsuarioListId));
        ArrayAdapter<String> array_spinnerG=(ArrayAdapter<String>)spiALum.getAdapter();
       // spiALum.setSelection(array_spinnerG.getPosition(aux.getIdAlumno()));
    }

    public void addCurso() {
        if (validateForm()) {
            //do something
            Curso prof = new Curso(id.getText().toString(), descripcion.getText().toString(),
                    Integer.parseInt(creditos.getText().toString()),
                    spiALum.getSelectedItem().toString());
                   // idAlumno.getText().toString());
            Intent intent = new Intent(getBaseContext(), Cursos.class);
            //sending Curso data
            intent.putExtra("addCurso", prof);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public void editCurso() {
        if (validateForm()) {
            Curso prof = new Curso(id.getText().toString(), descripcion.getText().toString(),
                    Integer.parseInt(creditos.getText().toString()),
                    spiALum.getSelectedItem().toString());
                   // idAlumno.getText().toString());
            Intent intent = new Intent(getBaseContext(), Cursos.class);
            //sending Curso data
            intent.putExtra("editCurso", prof);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public boolean validateForm() {
        int error = 0;
        if (TextUtils.isEmpty(this.id.getText())) {
            id.setError("ID requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.descripcion.getText())) {
            descripcion.setError("Descripcion requerida");
            error++;
        }
        if (TextUtils.isEmpty(this.creditos.getText())) {
            creditos.setError("Creditos requerido");
            error++;
        }

        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Algunos errores", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
