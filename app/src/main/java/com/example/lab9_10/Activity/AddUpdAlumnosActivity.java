package com.example.lab9_10.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab9_10.Logica.Alumno;
import com.example.lab9_10.Logica.Curso;
import com.example.lab9_10.R;

public class AddUpdAlumnosActivity extends AppCompatActivity {
    private FloatingActionButton fBtn;
    private boolean editable = true;
    private EditText idAlumno;
    private EditText nombre;
    private EditText apellido;
    private EditText edad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_upd_alumnos);
        editable = true;

        // button check
        fBtn = findViewById(R.id.addUpdAlumnoBtn);

        //cleaning stuff
        idAlumno = findViewById(R.id.idAddUpdAlum);
        nombre= findViewById(R.id.nombreAddUpdAlum);
        apellido = findViewById(R.id.apellidoAddUpdAlum);
        edad=findViewById(R.id.edadAddUpdAlum);

        idAlumno.setText("");
        nombre.setText("");
        apellido.setText("");
        edad.setText("");

        //receiving data from admCursoActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            editable = extras.getBoolean("editable");
            if (editable) {   // is editing some row
                Alumno aux = (Alumno) getIntent().getSerializableExtra("alumno");
                idAlumno.setText(aux.getIdAlumno());
                idAlumno.setEnabled(false);
                nombre.setText(aux.getNombre());
                edad.setText(String.valueOf(aux.getEdad()));
                apellido.setText(aux.getApellido());

                //edit action
                fBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editAlumno();
                    }
                });
            } else {         // is adding new Carrera object
                //add new action
                fBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addAlumno();
                    }
                });
            }
        }
    }

    public void addAlumno() {
        if (validateForm()) {
            //do something
            Alumno prof = new Alumno(idAlumno.getText().toString(), nombre.getText().toString(),
                    apellido.getText().toString(),
                    Integer.parseInt(edad.getText().toString()));
            Intent intent = new Intent(getBaseContext(), AlumnosActivity.class);
            //sending Curso data
            intent.putExtra("addAlumno", prof);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public void editAlumno() {
        if (validateForm()) {
            Alumno prof = new Alumno(idAlumno.getText().toString(), nombre.getText().toString(),
                    apellido.getText().toString(),
                    Integer.parseInt(edad.getText().toString()));
            Intent intent = new Intent(getBaseContext(), AlumnosActivity.class);
            //sending Curso data
            intent.putExtra("editAlumno", prof);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public boolean validateForm() {
        int error = 0;
        if (TextUtils.isEmpty(this.idAlumno.getText())) {
            idAlumno.setError("IdAlumno requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.nombre.getText())) {
            nombre.setError("Nombre requerida");
            error++;
        }
        if (TextUtils.isEmpty(this.apellido.getText())) {
            apellido.setError("Apellido requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.edad.getText())) {
            edad.setError("Edad requerido");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Algunos errores", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
