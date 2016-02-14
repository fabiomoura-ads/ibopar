package com.ibopar.business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ibopar.DetailActivity;

/**
 * Created by Fabio Moura on 18/10/2015.
 */
public class AvaliacaoBroadcast extends BroadcastReceiver {

    //private static String LOG = AvaliacaoBroadcast.class.getSimpleName().toString();
    private static final String LOG = "TESTE";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(LOG, "Executando broadcastreceiver para liberar avaliação");
        Toast.makeText(context, "Programação liberada para avaliação!", Toast.LENGTH_SHORT).show();
        //DetailActivity.liberaAvaliacao(true);
        DetailActivity.defineStadoComponentesEmAvaliacao();

    }
}
