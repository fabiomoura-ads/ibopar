package com.ibopar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.ibopar.adapter.RecyclerProgramacaoCustom;
import com.ibopar.business.ProgramacaoBusiness;
import com.ibopar.model.Programa;
import com.ibopar.model.Programacao;

import java.util.List;

/**
 * Created by Fabio Moura on 25/10/2015.
 */
public class MainActivity extends AppCompatActivity{

    ProgramacaoBusiness programacaoBusiness;
    RecyclerView recyclerMain;
    RecyclerView.LayoutManager layoutManager;
    public static List<Programacao> listaProgramacao;
    List<Programa> listaPrograma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_main);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        this.listaProgramacao = i.getParcelableArrayListExtra("lista");

        /*AdapterCustom adapter = new AdapterCustom(this, listaProgramacao);
        ListView listView = (ListView) findViewById(R.id.lista);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(visualizaDetail); */

        recyclerMain = (RecyclerView) findViewById(R.id.recyclerMain);
        recyclerMain.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerMain.setLayoutManager(layoutManager);

        RecyclerProgramacaoCustom adapter = new RecyclerProgramacaoCustom(this.listaProgramacao, this);
        recyclerMain.setAdapter(adapter);
        //recyclerMain.setOnClickListener(visualizaDetail);

    }

    public void relacionaProgramProgramacao(){
        long chavePrograma = 0;
        for (int i = 0 ; i < this.listaProgramacao.size(); i++) {
            chavePrograma = this.listaProgramacao.get(i).getPrograma().getId();

            for (int j = 0; j < this.listaPrograma.size(); j++ ){
                if ( this.listaPrograma.get(j).getId() == chavePrograma ) {
                    this.listaProgramacao.get(i).setPrograma( this.listaPrograma.get(j) );
                    break;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerMain.getAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if ( id == android.R.id.home ) {
            finish();
        } /*else if ( id == R.id.action_logout) {

            SharedPreferences preferences = getSharedPreferences("usuario", 0);
            preferences.edit().remove("id").commit();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            this.finish();

        } */else if ( id == R.id.action_sair ) {
            this.finish();
        }

        return true;
    }

    AdapterView.OnItemClickListener visualizaDetail = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            //Toast.makeText(MainActivity.this, listaProgramacao.get(position).getPrograma().getNome().toString(), Toast.LENGTH_LONG).show();

            Programacao p = listaProgramacao.get(position);

            intent.putExtra("programacao", p);
            startActivity(intent);
        }
    };
}
