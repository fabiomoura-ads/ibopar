package com.ibopar.business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ibopar.DetailActivity;

/**
 * Created by Fabio Moura on 18/10/2015.
 */
public class ProgramacaoBroadcast extends BroadcastReceiver {

    //private static String LOG = ProgramacaoBroadcast.class.getSimpleName().toString();
    private static final String LOG = "TESTE";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(LOG, "Executando broadcastreceiver para nova programação");

        Context mainActivity = ProgramacaoBusiness.getContexto();

        ProgramacaoBusiness programacaoBusiness = new ProgramacaoBusiness(mainActivity);
        programacaoBusiness.carregaProgramacao();

        //DetailActivity.exibeFeedBackDaAvaliacao(false);
        DetailActivity.defineStadoComponentesPreAvaliacao();

    }
}
