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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BugAdapter mAdapter;
    private RecyclerView recyclerViewBugs;
    List<Bug> found = new ArrayList<>();

    private Query mListBug;
    private ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;

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
        

        mListBug = FirebaseDatabase.getInstance().getReference()
                .child("bugs").orderByChild("code");
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                //Refrescar lista
                refrescar();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                //Refrescar lista
                refrescar();
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                //Refrescar lista
                refrescar();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //Refrescar lista
                refrescar();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mListBug.addChildEventListener(childEventListener);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                found.clear();
                for (DataSnapshot bugsSnapshot: dataSnapshot.getChildren()) {
                    found.add(bugsSnapshot.getValue(Bug.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mListBug.addValueEventListener(valueEventListener);

        // specify an adapter with the list to show
        mAdapter = new BugAdapter(this, found);
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


    private void nuevo(){
        int seq = 0;
        if (found.size() > 0)
            seq = found.get(found.size()-1).code+1;
        Intent i = new Intent(this, BugEdition.class);
        i.putExtra("code", seq);
        startActivity(i);
    }

    private void salir(){
        finish();
    }
    private void refrescar(){
        mAdapter.setBugList(found);
        mAdapter.notifyDataSetChanged();
        Context context = getApplicationContext();
        Toast.makeText(context, "Elementos cargados: "+mAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
    }



}
