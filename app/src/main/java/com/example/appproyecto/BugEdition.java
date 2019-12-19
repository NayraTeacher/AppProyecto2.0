package com.example.appproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BugEdition extends AppCompatActivity {
    private Spinner sestados;
    private EditText title, description;
    private TextView code, fecha, adjunto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_edition);
        sestados=(Spinner)findViewById(R.id.sEstado);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Bug.estados);
        sestados.setAdapter(adapter);

        title = (EditText) findViewById(R.id.eTitle);
        description = (EditText) findViewById(R.id.eDescripcion);
        fecha = (TextView) findViewById(R.id.tfecha);
        code = (TextView) findViewById(R.id.tcode);
        adjunto = (TextView) findViewById(R.id.adjunto);


        adjunto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Choose File"), 1);
            }
        });

        clearBug();

        Bundle bundle = getIntent().getExtras();
        int codigo = bundle.getInt("code");
        if (codigo > -1){
            Bug b = searchBug(codigo);
            if (b != null)
                loadBug(b);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            //Cancelado por el usuario
        }
        if ((resultCode == RESULT_OK) && (requestCode == 1)) {
            //Procesar el resultado
            Uri uri = data.getData(); //obtener el uri content
            adjunto.setText(uri.toString());
        }
    }

    public void guardar(View v){
        int c = Integer.parseInt(code.getText().toString());
        Bug mybug = new Bug(c, title.getText().toString(), fecha.getText().toString(),
                description.getText().toString(), adjunto.getText().toString(), sestados.getSelectedItemPosition());
        if (mybug.code == -1)
            newBug(mybug);
        else
            updateBug(mybug);
    }


    private void newBug(Bug nuevo){
        BugsDB dbase = new BugsDB(this,
                "soporte", null, BugsDB.DATABASE_VERSION);
        SQLiteDatabase bd = dbase.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("title", nuevo.title);
        registro.put("fecha", nuevo.fecha);
        registro.put("descripcion", nuevo.descripcion);
        registro.put("adjunto", nuevo.adjunto);
        registro.put("estado", nuevo.estado);

        bd.insert("bugs", null, registro);
        bd.close();
        Toast.makeText(this,"Datos guardados", Toast.LENGTH_SHORT).show();
    }

    private void updateBug(Bug existe){
        BugsDB dbase = new BugsDB(this,
                "soporte", null, BugsDB.DATABASE_VERSION);
        SQLiteDatabase bd = dbase.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("title", existe.title);
        registro.put("fecha", existe.fecha);
        registro.put("descripcion", existe.descripcion);
        registro.put("adjunto", existe.adjunto);
        registro.put("estado", existe.estado);

        int cant = bd.update("bugs", registro,"code='" + existe.code + "'", null);
        bd.close();
        if (cant == 1)
            Toast.makeText(this,"Datos actualizados", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "No ha podido realizarse la actualizacion", Toast.LENGTH_SHORT).show();

    }

    private Bug searchBug(int code){
        Bug found = null;
        BugsDB dbase = new BugsDB(this,
                "soporte", null, BugsDB.DATABASE_VERSION);
        SQLiteDatabase bd = dbase.getWritableDatabase();
        Cursor fila = bd.rawQuery("select title, fecha, descripcion, adjunto, estado " +
                "from bugs where code='" + code + "'", null);
        if (fila.moveToFirst()) {
            found = new Bug(code,fila.getString(0),fila.getString(1),
                    fila.getString(2),fila.getString(3), fila.getInt(4));

        }
        bd.close();

        return found;
    }

    private void loadBug(Bug b){
        code.setText(String.valueOf(b.code));
        title.setText(b.title);
        fecha.setText(b.fecha);
        description.setText(b.descripcion);
        sestados.setSelection(b.estado);
        adjunto.setText(b.adjunto);
    }
    private void clearBug(){
        code.setText("-1");
        title.setText("");
        fecha.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        description.setText("");
        sestados.setSelection(0);
        adjunto.setText("not apply");
    }
}
