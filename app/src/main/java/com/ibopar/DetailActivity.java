package com.ibopar;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ibopar.business.ProgramacaoBusiness;
import com.ibopar.model.Programacao;

public class DetailActivity extends AppCompatActivity {

    public static ImageView imageLogoPrograma;
    public static TextView textTituloProgramacao;
    public static TextView textHoraInicio;
    public static TextView textHoraFim;
    public static TextView textDescricao;
    public static TextView textQtdAv_positiva;
    public static TextView textQtdAv_negativa;
    public static LinearLayout linearAvaliacao;
    public static LinearLayout linearFeedBack;
    public static TextView textFeedBack;
    public static ImageView btnPositivo;
    public static ImageView btnNegativo;
    //private static final String LOG = DetailActivity.class.getSimpleName().toString();
    private static final String LOG = "TESTE";
    public static ProgramacaoBusiness programacaoBusiness;
    private View snk;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageLogoPrograma = (ImageView) findViewById(R.id.imageLogoPrograma);

        textTituloProgramacao = (TextView) findViewById(R.id.textTituloProgramacao);
        textHoraInicio = (TextView) findViewById(R.id.textHoraInicio);
        textHoraFim = (TextView) findViewById(R.id.textHoraFim);
        textDescricao = (TextView) findViewById(R.id.textDescricao);

        textQtdAv_positiva = (TextView) findViewById(R.id.textQtdAv_positiva);
        textQtdAv_negativa = (TextView) findViewById(R.id.textQtdAv_negativa);

        linearAvaliacao = (LinearLayout) findViewById(R.id.linearAvaliacao);
        linearFeedBack = (LinearLayout) findViewById(R.id.linearFeedBack);
        textFeedBack = (TextView) findViewById(R.id.textFeedBack);

        btnPositivo = ( ImageView) findViewById(R.id.avPositiva);
        btnPositivo.setOnClickListener(confirmar);

        btnNegativo = ( ImageView) findViewById(R.id.avNegativa);
        btnNegativo.setOnClickListener(confirmar);

        snk = (View) findViewById(R.id.snk);

        defineStadoComponentesPreAvaliacao();

        programacaoBusiness = new ProgramacaoBusiness(this);

        Programacao programacao = ( Programacao ) getIntent().getParcelableExtra("programacao");

        if ( programacao == null ) {
            programacaoBusiness.carregaProgramacao();
        }else{
            programacaoBusiness.retornoProgramacaoService(programacao);
        }

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    View.OnClickListener desfazer = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //exibeFeedBackDaAvaliacao(false);
            defineStadoComponentesEmAvaliacao();
        }

    };

    public void avaliar(View v){

        boolean avaliacao = false;

        if ( v.getId() == R.id.avPositiva ) {
            avaliacao = true;
        }

        Log.i(LOG, "Realizando avaliação: " + avaliacao);
        programacaoBusiness.realizarAvaliacao(avaliacao);
        defineStadoComponentesPosAvaliacao();

    }

    View.OnClickListener confirmar = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {

            //exibeFeedBackDaAvaliacao(true);

            Snackbar.make(snk, "Avaliação realizada ...", Snackbar.LENGTH_LONG)
                    .setAction("DESFAZER", desfazer)
                    .setActionTextColor(Color.RED)
                    .setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                                avaliar(v);
                            }
                        }
                    })
                    .show();
      }
    };


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if ( id == android.R.id.home ) {
            finish();
        } /*else if ( id == R.id.action_logout) {

            SharedPreferences preferences = getSharedPreferences("usuario", 0);
            preferences.edit().remove("id").commit();
            startActivity(new Intent(DetailActivity.this, LoginActivity.class));
            this.finish();

        } */else if ( id == R.id.action_sair ) {
            this.finish();
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        Log.i(LOG, "Cancelando alarmes existentes");
        Intent intent = null;
        PendingIntent pendingIntent = null;
        AlarmManager alarm = null;

        intent = new Intent("CONFIG_ALARM_PROXIMA_PROGRAMACAO");
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);

        intent = new Intent("CONFIG_ALARM_LIBERACAO_AVALIACAO");
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);

        super.onDestroy();
    };

    public static void exibeTotaisDaAvaliacao( boolean exibeFeedBack ){

        AlphaAnimation animation = null;
        LinearLayout linear = null;

        if ( exibeFeedBack ) {

            animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(5000);
            linear = (LinearLayout) linearFeedBack;
            linear.startAnimation(animation);
            linear.setVisibility(View.VISIBLE);

        } else {

            linear = (LinearLayout) linearFeedBack;
            linear.setVisibility(View.GONE);

        }
    }

    public static void defineStadoComponentesPreAvaliacao(){
        textFeedBack.setText(getMensagem("espera"));
        habilitaBotoes(false);
        exibeTotaisDaAvaliacao(false);
    }

    public static void defineStadoComponentesEmAvaliacao(){
        textFeedBack.setText(getMensagem("liberado"));
        habilitaBotoes(true);
        exibeTotaisDaAvaliacao(false);
    }

    public static void defineStadoComponentesPosAvaliacao(){
        textFeedBack.setText(getMensagem("avaliado"));
        habilitaBotoes(false);
        exibeTotaisDaAvaliacao(true);
    }

    public static void habilitaBotoes( boolean habilita){

        if ( habilita ) {
            btnPositivo.setImageResource(R.drawable.ic_positivo_on);
            btnNegativo.setImageResource(R.drawable.ic_negativo_on);
        } else {
            btnPositivo.setImageResource(R.drawable.ic_positivo_off);
            btnNegativo.setImageResource(R.drawable.ic_negativo_off);
        }

        btnPositivo.setClickable(habilita);
        btnPositivo.setEnabled(habilita);

        btnNegativo.setClickable(habilita);
        btnNegativo.setEnabled(habilita);

    }

    public static String getMensagem( String estado ){
        String msg = "";
        String[][] estados = {
                                { "espera","Aguarde o início da programação para registrar sua avaliação."},
                                { "liberado", "Avalie a programação assistida."},
                                { "avaliado", "Obrigado! Sua avaliação nos ajudará a fazer uma programação cada vez melhor!"}
                            };

        for ( int i = 0; i < estados.length; i++ ) {
            if ( estados[i][0].equals(estado) ) {
                msg = estados[i][1].toString();
                break;
            }
        }

        return msg;
    }

}