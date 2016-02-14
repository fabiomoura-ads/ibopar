package com.ibopar.service;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import com.ibopar.business.UsuarioBusiness;
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
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class UsuarioService extends AsyncTask<String[][], Void, JSONObject> {

    private UsuarioBusiness usuarioBusiness;
    private ProgressDialog ringProgressDialog;
    private String tipoRequisicao = null;

    public UsuarioService(UsuarioBusiness usuarioBusiness){
        this.usuarioBusiness = usuarioBusiness;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.ringProgressDialog = ProgressDialog.show(this.usuarioBusiness.getContexto(), "Aguarde ...", "Checando informações...", true);
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
//                    .appendQueryParameter("login", params[2].toString())
//                    .appendQueryParameter("senha", params[3].toString());

            tipoRequisicao = params[0][1][0].toString();

            for(int i = 1; i < params[0].length; i++ ) {
                builder.appendQueryParameter(params[0][i][0].toString(), params[0][i][1].toString());
            }

            String query = builder.build().getEncodedQuery();
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(outputStream));
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
        super.onPostExecute(result);
        String msg = "";
        if ( result != null ) {
            msg = "Acesso liberado!";
        }
        this.ringProgressDialog.setMessage(msg);
        this.usuarioBusiness.resultUsuarioService(result, tipoRequisicao);
        this.ringProgressDialog.dismiss();

    }

}
