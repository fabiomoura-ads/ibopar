package com.ibopar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ibopar.business.ProgramacaoBusiness;
import com.ibopar.model.Programa;
import com.ibopar.model.Programacao;
import com.ibopar.service.WebService;
import com.ibopar.util.ReadStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabio Moura on 18/10/2015.
 * Classe SplashActivity é responsável por efetuar o carregamento inicial das programaçãoes a serem listadas,
 *
 */
public class SplashActivity extends AppCompatActivity {

    private ContentLoadingProgressBar loadingProgressBar;
    private ProgramacaoBusiness programacaoBusiness;
    private static final String LOG = "Projeto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        this.loadingProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_splash);
        this.loadingProgressBar.setVisibility(View.VISIBLE);
        this.loadingProgressBar.show();

        this.programacaoBusiness = new ProgramacaoBusiness(this);
        new ListProgramacaoTask().execute(new WebService().pegaParametrosListarProgramacao());

        //temporizadorLoadingProgressBar();

    }

    //@Deprecated
    public void temporizadorLoadingProgressBar(){
        MyCountDownTimer time_splash = new MyCountDownTimer(3000, 500);
        time_splash.start();
    }
    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
        }
        @Override
        public void onFinish() {
            Intent intent;
            if ( getSharedPreferences("usuario", Context.MODE_PRIVATE).getLong("id", 0) != 0 ) {
                intent = new Intent(SplashActivity.this, DetailActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }
    }

    //TODO: VERIFICAR PORQUE O OBJETO COMPOSTO NÂO CHEVA NA MAINACTIVITY
    public void preparaDados(List<Programacao> listaProgramacao){
        //Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        Intent intent = null;

        if ( this.getSharedPreferences("usuario", Context.MODE_PRIVATE).getLong("id", 0) != 0 ) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }

        intent.putParcelableArrayListExtra("lista", (ArrayList<? extends Parcelable>) listaProgramacao);

        startActivity(intent);
        finish();
        this.loadingProgressBar.hide();
    }

    public List<Programa> pegaProgramas(List<Programacao> lista){
        List<Programa> listaPrograma = new ArrayList<>();
        for( int i = 0; i < lista.size(); i++ ) {
            if ( lista.get(i).getPrograma() != null ) {
                listaPrograma.add(lista.get(i).getPrograma());
            }
        }
        return listaPrograma;
    }

    public List<Programacao> carregaListaProgramacaoService(JSONArray jsonArray){
        List<Programacao> lista = new ArrayList<>();
        Programacao programacao;
        JSONObject jsonObject;
        if ( jsonArray != null ) {
            for( int i = 0; i < jsonArray.length(); i++ ) {
                try {
                    jsonObject = jsonArray.getJSONObject(i);
                    programacao = programacaoBusiness.montaProgramacao(jsonObject);
                    lista.add(programacao);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return lista;
    };

    public class ListProgramacaoTask extends AsyncTask<String[][], Void, List> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List doInBackground(String[][]... params) {

            JSONArray jsonArray = null;
            List<Programacao> listaCompleta = null;

            try {

                URL url = new URL(params[0][0][1].toString());

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder();

                for(int i = 1; i < params[0].length; i++) {
                    builder.appendQueryParameter(params[0][i][0].toString(), params[0][i][1].toString());
                }

                String query = builder.build().getEncodedQuery();
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                outputStream.close();
                connection.connect();

                //Leitura do stream retornado
                InputStream inputStream = connection.getInputStream();
                String jsonString = ReadStream.realizaLeituraDoStream(inputStream);
                jsonArray = new JSONArray(jsonString);

                listaCompleta = carregaListaProgramacaoService(jsonArray);
                String urlImage;
                InputStream input;
                Bitmap myBitmap;

                //Carrega Imagens
                /*for ( int i = 0; i < listaCompleta.size(); i++) {

                    try {

                        urlImage = listaCompleta.get(i).getPrograma().getLogo();
                        url = new URL(urlImage);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        input = connection.getInputStream();
                        myBitmap = BitmapFactory.decodeStream(input);

                        //myBitmap = Bitmap.createScaledBitmap(myBitmap, 150, 100, true);
                        listaCompleta.get(i).getPrograma().setLogoBitmap(myBitmap);

                    } catch (IOException e) {
                        return null;
                    }
                }*/

            } catch (Exception e ){
                e.printStackTrace();
            }
            return listaCompleta;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List result) {
            super.onPostExecute(result);
            preparaDados(result);
        }
    }
}