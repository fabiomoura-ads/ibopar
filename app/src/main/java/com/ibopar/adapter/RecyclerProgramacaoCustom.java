package com.ibopar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ibopar.DetailActivity;
import com.ibopar.R;
import com.ibopar.model.Programacao;

import java.util.List;

/**
 * Created by Fabio Moura on 25/10/2015.
 */
public class RecyclerProgramacaoCustom extends RecyclerView.Adapter<RecyclerProgramacaoCustom.ViewHolder> {

    private Context context;
    private List<Programacao> lista;
    private LayoutInflater mLayoutInflater;



    public RecyclerProgramacaoCustom( List<Programacao> lista, Context context){
        this.lista = lista;
        this.context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerProgramacaoCustom.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_lista, viewGroup, false);
        View itemView = mLayoutInflater.inflate(R.layout.item_lista, viewGroup, false);//.from(viewGroup.getContext()).inflate(R.layout.item_lista, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        Programacao item = lista.get(i);

        holder.titulo.setText(item.getNome());
        holder.descricao.setText(item.getDescricao());
        holder.descricao.setText(reduzDescricao(item.getDescricao()));
        holder.inicio.setText(item.getHora_inicio());
        holder.fim.setText(item.getHora_fim());

        Glide.with(context).load(item.getPrograma().getLogo()).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                //Toast.makeText(context, lista.get(i).getPrograma().getNome().toString(), Toast.LENGTH_LONG).show();
                Programacao p = lista.get(i);

                intent.putExtra("programacao", p);
                context.startActivity(intent);
            }
        });
    }


    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected ImageView img;
        protected TextView titulo;
        protected TextView descricao;
        protected TextView inicio;
        protected TextView fim;

        public ViewHolder(View itemView) {

            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.img);
            titulo = (TextView ) itemView.findViewById(R.id.titulo);
            descricao = (TextView ) itemView.findViewById(R.id.descricao);
            inicio = (TextView ) itemView.findViewById(R.id.inicio);
            fim = (TextView ) itemView.findViewById(R.id.fim);

        }
    }

    public String reduzDescricao(String descricao){
        String descricaoReduzida = descricao;
        int limiteCaracteres = 60;
        int ultimoEspacoEntrePalavras = 0;

        if ( descricao.length() >= limiteCaracteres ) {
            ultimoEspacoEntrePalavras = descricao.indexOf(" ", limiteCaracteres);
            descricaoReduzida = descricao.substring(0, ultimoEspacoEntrePalavras);
        }

        descricaoReduzida += "...";

        return descricaoReduzida;
    }

}
