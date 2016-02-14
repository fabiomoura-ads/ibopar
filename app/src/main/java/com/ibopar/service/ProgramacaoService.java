package com.ibopar.service;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;

import com.ibopar.business.ProgramacaoBusiness;
import com.ibopar.util.ReadStream;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Fabio Moura on 18/10/2015.
 */
public class ProgramacaoService extends AsyncTask<String[][], Void, JSONObject> {

    private ProgramacaoBusiness programacaoBusiness = null;
    private ProgressDialog ringProgressDialog;

    public ProgramacaoService( ProgramacaoBusiness programacaoBusiness){
        this.programacaoBusiness = programacaoBusiness;
        this.ringProgressDialog = ProgressDialog.show(this.programacaoBusiness.getContexto(), "Aguarde ...", "Buscando programação ...", true);
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }


    @Override
    protected JSONObject doInBackground(String[][]... params) {


        JSONObject jsonObject = null;

        try {

            URL url = new URL(params[0][0][1].toString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder();
//                    .appendQueryParameter("servico", params[1].toString() )
//                    .appendQueryParameter("data", params[2].toString())
//                    .appendQueryParameter("usuario_id", params[3].toString());

            for(int i = 1; i < params[0].length; i++ ) {
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
            jsonObject = new JSONObject(jsonString);

        } catch (Exception e ){
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        // TODO Auto-generated method stub
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        this.ringProgressDialog.dismiss();
        this.programacaoBusiness.retornoProgramacaoService(result, true);
    }

}
