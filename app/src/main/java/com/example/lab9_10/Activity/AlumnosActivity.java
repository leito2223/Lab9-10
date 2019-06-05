package com.example.lab9_10.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.lab9_10.Adapter.AlumnoAdapter;
import com.example.lab9_10.Control.controlSQL;
import com.example.lab9_10.Helper.RecyclerItemTouchHelper;
import com.example.lab9_10.Logica.Alumno;
import com.example.lab9_10.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlumnosActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, AlumnoAdapter.AlumnoAdapterListener {

    private RecyclerView mRecyclerView;
    private AlumnoAdapter mAdapter;
    private List<Alumno> alumnoList;
    private CoordinatorLayout coordinatorLayout;
    private SearchView searchView;
    private FloatingActionButton fab;
    private controlSQL model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos);
        //Toolbar toolbar1 = findViewById(R.id.toolbarA);
//        setSupportActionBar(toolbar1);

        //toolbar fancy stuff
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.my_alumno));

        mRecyclerView = findViewById(R.id.recycler_alumno);
        alumnoList = new ArrayList<>();
        model = new controlSQL(this);
        alumnoList = model.listaAlumnos();
        mAdapter = new AlumnoAdapter(alumnoList, this);
        coordinatorLayout = findViewById(R.id.coordinator_layoutAlumno);

       // Alumno alumno = new Alumno("1","Leonardo","Arce",18);
       // model.insertAlumno(alumno);
        alumnoList = model.listaAlumnos();
        // white background notification bar
        whiteNotificationBar(mRecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        // go to update or add career
        fab = findViewById(R.id.addBtnA);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddUpdAlumno();
            }
        });

        //delete swiping left and right
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        //should use database info


        // Receive the Carrera sent by AddUpdCarreraActivity
        checkIntentInformation();

        //refresh view
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (direction == ItemTouchHelper.START) {
            if (viewHolder instanceof AlumnoAdapter.MyViewHolder) {
                // get the removed item name to display it in snack bar
                String name = alumnoList.get(viewHolder.getAdapterPosition()).getNombre();
                model.deleteAlumno(alumnoList.get(viewHolder.getAdapterPosition()).getIdAlumno());

                // save the index deleted
                final int deletedIndex = viewHolder.getAdapterPosition();
                // remove the item from recyclerView
                mAdapter.removeItem(viewHolder.getAdapterPosition());

                // showing snack bar with Undo option
                Snackbar snackbar = Snackbar.make(coordinatorLayout, name + " removido!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // undo is selected, restore the deleted item from adapter
                        mAdapter.restoreItem(deletedIndex);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        } else {
            //If is editing a row object
            Alumno aux = mAdapter.getSwipedItem(viewHolder.getAdapterPosition());
            //send data to Edit Activity
            Intent intent = new Intent(this, AddUpdAlumnosActivity.class);
            intent.putExtra("editable", true);
            intent.putExtra("alumno", aux);
            mAdapter.notifyDataSetChanged(); //restart left swipe view
            startActivity(intent);
        }
    }

    @Override
    public void onItemMove(int source, int target) {
        mAdapter.onItemMove(source, target);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds alumnoList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView   !IMPORTANT
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change, every type on input
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        Intent a = new Intent(this, MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }


    private void checkIntentInformation() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Alumno aux;
            aux = (Alumno) getIntent().getSerializableExtra("addAlumno");
            if (aux == null) {
                aux = (Alumno) getIntent().getSerializableExtra("editAlumno");
                if (aux != null) {
                    model.updateAlumno(aux);
                    alumnoList = model.listaAlumnos();
                    mAdapter = new AlumnoAdapter(alumnoList, this);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), aux.getNombre() + " editado correctamente", Toast.LENGTH_LONG).show();
                }
            }else{
                model.insertAlumno(aux);
                alumnoList = model.listaAlumnos();
                mAdapter = new AlumnoAdapter(alumnoList, this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), aux.getNombre() + " agregado correctamente", Toast.LENGTH_LONG).show();

            }
        }
    }

    private void goToAddUpdAlumno() {
            Intent intent = new Intent(this, AddUpdAlumnosActivity.class);
            intent.putExtra("editable", false);
            startActivity(intent);
    }

    @Override
    public void onContactSelected(Alumno alumno) {

    }
}
