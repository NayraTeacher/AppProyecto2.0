package com.example.appproyecto;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



public class BugAdapter extends RecyclerView.Adapter<BugAdapter.ViewHolder> {


    private List<Bug> bugList;
    private Context contexto;

    public BugAdapter(Context contexto, List<Bug> bugList) {
        this.bugList = bugList;
        this.contexto = contexto;
    }

    public void setBugList(List<Bug> bugList) {
        this.bugList = bugList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView tvdate;
        private TextView tvcode;
        private ImageView imagen;
        private ImageButton edit;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.ttitle);
            tvdate = (TextView) v.findViewById(R.id.tdate);
            tvcode = (TextView) v.findViewById(R.id.tvcode);
            imagen = (ImageView) v.findViewById(R.id.ivEstado);
            edit = (ImageButton) v.findViewById(R.id.bEditar);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bug_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String titulo = bugList.get(position).title;
        String fecha = bugList.get(position).fecha;
        holder.title.setText(titulo);
        holder.tvdate.setText(fecha);
        holder.tvcode.setText("#"+String.valueOf(bugList.get(position).code));
        switch (bugList.get(position).estado){
            case 0:
                holder.imagen.setImageResource(R.drawable.abierto);
                break;
            case 1:
                holder.imagen.setImageResource(R.drawable.enproceso);
                break;
            case 2:
                holder.imagen.setImageResource(R.drawable.closed);
                break;
        }

        holder.edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(contexto, BugEdition.class);
                i.putExtra("code", bugList.get(position).code);
                contexto.startActivity(i);
                //Toast.makeText(contexto, "Pulsado el : "+bugList.get(position).code, Toast.LENGTH_SHORT).show();

            }

        });

    }

    @Override
    public int getItemCount() {
        return bugList.size();
    }





}
