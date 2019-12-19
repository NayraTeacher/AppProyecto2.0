package com.example.appproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BugAdapter mAdapter;
    private RecyclerView recyclerViewBugs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        recyclerViewBugs = findViewById(R.id.rvBugs);

        // use a linear layout manager
        recyclerViewBugs.setLayoutManager(new LinearLayoutManager(this));
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerViewBugs.setHasFixedSize(true);
        // specify an adapter with the list to show
        mAdapter = new BugAdapter(this, getData());
        recyclerViewBugs.setAdapter(mAdapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close:
                salir();
                return true;

            case R.id.action_refresh:
                // abriremos intent de edicion
                refrescar();
                return true;

            case R.id.action_edit:
                // abriremos intent de edicion
                nuevo();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    private List<Bug> getData(){
        List<Bug> found = new ArrayList<>();
        Bug onebug = null;
        BugsDB dbase = new BugsDB(this,
                "soporte", null, BugsDB.DATABASE_VERSION);
        SQLiteDatabase bd = dbase.getWritableDatabase();
        Cursor fila = bd.rawQuery("select code, title, fecha, descripcion, adjunto, estado " +
                "from bugs", null);
        if (fila.moveToFirst()) {
            onebug = new Bug(fila.getInt(0),fila.getString(1),fila.getString(2),
                    fila.getString(3),fila.getString(4), fila.getInt(5));
            found.add(onebug);
            while(fila.moveToNext()){
                onebug = new Bug(fila.getInt(0),fila.getString(1),fila.getString(2),
                        fila.getString(3),fila.getString(4), fila.getInt(5));
                found.add(onebug);
            }
        }
        bd.close();

        return found;
    }

    private void nuevo(){
        Intent i = new Intent(this, BugEdition.class);
        i.putExtra("code", -1);
        startActivity(i);
    }

    private void salir(){
        finish();
    }
    private void refrescar(){
        mAdapter.setBugList(getData());
        mAdapter.notifyDataSetChanged();
        Context context = getApplicationContext();
        Toast.makeText(context, "Elementos cargados: "+mAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
    }

    private void editar(int position){
        Intent i = new Intent(this, BugEdition.class);
        i.putExtra("code", position);
        startActivity(i);
    }

}
