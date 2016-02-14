package com.ibopar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibopar.R;
import com.ibopar.model.Programacao;

import java.util.List;

/**
 * Created by Fabio Moura on 25/10/2015.
 */
public class AdapterCustom extends BaseAdapter {

    private Context context;
    private List<Programacao> lista;

    public AdapterCustom(Context context, List<Programacao> lista){
        this.lista = lista;
        this.context = context;
    }

    @Override
    public int getCount() {
           return this.lista.size();
   }

    @Override
    public int getViewTypeCount() {
        if ( getCount() != 0 ) {
            return getCount();
        }
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return this.lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.item_lista,null);

        ImageView img = (ImageView) layout.findViewById(R.id.img);
        TextView titulo = (TextView ) layout.findViewById(R.id.titulo);
        TextView descricao = (TextView ) layout.findViewById(R.id.descricao);
        TextView inicio = (TextView ) layout.findViewById(R.id.inicio);
        TextView fim = (TextView ) layout.findViewById(R.id.fim);

        //Bitmap bitmap = Bitmap.createScaledBitmap(this.lista.get(position).getPrograma().getLogoBitmap(), 120, 80, true);
        //img.setImageBitmap(bitmap);
        //Glide.with(context).load(this.lista.get(position).getPrograma().getLogo()).into(img);
        //Glide.with(context).load(this.lista.get(position).getPrograma().getLogo()).into(img);

        titulo.setText(this.lista.get(position).getNome());
        descricao.setText( reduzDescricao(this.lista.get(position).getDescricao()));
        inicio.setText(this.lista.get(position).getHora_inicio());
        fim.setText(this.lista.get(position).getHora_fim());

        return layout;
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
