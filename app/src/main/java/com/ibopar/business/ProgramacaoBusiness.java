package com.ibopar.business;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ibopar.DetailActivity;
import com.ibopar.MainActivity;
import com.ibopar.model.Programa;
import com.ibopar.model.Programacao;
import com.ibopar.service.WebService;
import com.ibopar.util.LoadImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Fabio Moura on 18/10/2015.
 */
public class ProgramacaoBusiness {

    private long idProgramacao;
    private long idUsuario;
    private Calendar dataRequisicaoProgramacao;
    private Calendar dataLiberacaoAvaliacao;
    private String dataHora;
    private static Context contexto;
    //private static final String LOG = ProgramacaoBusiness.class.getSimpleName().toString();
    private static final String LOG = "TESTE";
    private WebService webService;

    public ProgramacaoBusiness(Context context) {
        contexto = context;
    }

    public static Context getContexto() {
        return contexto;
    }

    public static void setContexto(Context context) {
        contexto = context;
    }

    public long getIdProgramacao() {
        return idProgramacao;
    }

    public void setIdProgramacao(long idProgramacao) {
        this.idProgramacao = idProgramacao;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public void carregaProgramacao(){

        this.dataRequisicaoProgramacao = Calendar.getInstance();
        this.dataRequisicaoProgramacao.setTime(new Date());

        this.dataHora = new Timestamp(this.dataRequisicaoProgramacao.getTime().getTime()).toString();
        this.idUsuario = contexto.getSharedPreferences("usuario", Context.MODE_PRIVATE).getLong("id", 0);

        Log.i(LOG, "Requisitando programação para usuário id = " + this.idUsuario + " , com data em: " + this.dataHora);

        webService = new WebService();
        webService.requisitaProximaProgramacao(this);
    }

    public void realizarAvaliacao(boolean avaliacao){

        Log.i(LOG, "Realizando avaliação, usuario: " + this.idUsuario + ", programação: " + this.idProgramacao + ", avaliacao: " + avaliacao);

        webService = new WebService();
        webService.realizaAvaliacao(this, avaliacao);

    }

    public void defineAlarmManagerProximaProgramacao( String horaFimProgramacaoAtual ){

        Intent intent = new Intent("CONFIG_ALARM_PROXIMA_PROGRAMACAO");
        PendingIntent pendingIntentProgramacao = PendingIntent.getBroadcast(contexto, 0, intent, 0);

        int hora = Integer.parseInt(horaFimProgramacaoAtual.substring(0,2));
        int minuto = Integer.parseInt(horaFimProgramacaoAtual.substring(3,5));

        dataRequisicaoProgramacao = Calendar.getInstance();
        dataRequisicaoProgramacao.setTimeInMillis(System.currentTimeMillis());
        dataRequisicaoProgramacao.set(Calendar.HOUR_OF_DAY, hora);
        dataRequisicaoProgramacao.set(Calendar.MINUTE, minuto + 1);
        dataRequisicaoProgramacao.set(Calendar.SECOND, 0);

        String data = new Timestamp(dataRequisicaoProgramacao.getTime().getTime()).toString();
        Log.i(LOG, "Configurando ALARM_SERVICE para próxima programação as : " + data );

        AlarmManager proximaProgramacao = (AlarmManager) contexto.getSystemService(Context.ALARM_SERVICE);
        proximaProgramacao.set(AlarmManager.RTC_WAKEUP, dataRequisicaoProgramacao.getTimeInMillis(), pendingIntentProgramacao);
    }

    public void defineAlarmManagerLiberacaoAvaliacao( String horaInicioProgramacao ){

        Intent intent = new Intent("CONFIG_ALARM_LIBERACAO_AVALIACAO");
        PendingIntent pendingIntentAvaliacao = PendingIntent.getBroadcast(contexto, 0, intent, 0);

        int hora = Integer.parseInt(horaInicioProgramacao.substring(0,2));
        int minuto = Integer.parseInt(horaInicioProgramacao.substring(3,5));

        dataLiberacaoAvaliacao = Calendar.getInstance();
        dataLiberacaoAvaliacao.setTimeInMillis(System.currentTimeMillis());
        dataLiberacaoAvaliacao.set(Calendar.HOUR_OF_DAY, hora);
        dataLiberacaoAvaliacao.set(Calendar.MINUTE, minuto);
        dataLiberacaoAvaliacao.set(Calendar.SECOND, 0);

        String data = new Timestamp(dataLiberacaoAvaliacao.getTime().getTime()).toString();
        Log.i(LOG, "Configurando ALARM_SERVICE para liberação de avaliação as: " + data );

        AlarmManager liberaAvaliacao = (AlarmManager) contexto.getSystemService(Context.ALARM_SERVICE);
        liberaAvaliacao.set(AlarmManager.RTC_WAKEUP, dataLiberacaoAvaliacao.getTimeInMillis(), pendingIntentAvaliacao);
    }

    public Programacao montaProgramacao(JSONObject jsonObject){

        Programacao programacao = null;
        Programa programa = null;

        try {

            Log.i(LOG, "Montando objeto de programação consultada");

            programacao = new Programacao();
            programacao.setId(jsonObject.getLong("id"));
            programacao.setNome(jsonObject.getString("nome"));
            programacao.setHora_inicio(jsonObject.getString("hora_inicio"));
            programacao.setHora_fim(jsonObject.getString("hora_fim"));

            programacao.setAv_positiva(jsonObject.getInt("av_positiva"));
            programacao.setAv_negativa(jsonObject.getInt("av_negativa"));
            programacao.setDescricao(jsonObject.getString("descricao"));

            //programacao.setProgramaId(jsonObject.getJSONObject("programa").getLong("id"));

            jsonObject = jsonObject.getJSONObject("programa");

            programa = new Programa();
            programa.setId(jsonObject.getLong("id"));
            programa.setNome(jsonObject.getString("nome"));
            programa.setHora_inicio(jsonObject.getString("hora_inicio"));
            programa.setHora_fim(jsonObject.getString("hora_fim"));
            programa.setLogo(jsonObject.getString("logo"));

            programacao.setPrograma(programa);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return programacao;

    }

    public void retornoProgramacaoService(JSONObject jsonObject, boolean novaProgramacao){
        boolean ehMesmoPrograma = false;
        if ( jsonObject != null ) {

            Programacao programacao = montaProgramacao(jsonObject);

            if ( this.getIdProgramacao() == programacao.getId() ) {
                ehMesmoPrograma = true;
            }

            this.atualizaInterface(programacao, ehMesmoPrograma);
            this.setIdProgramacao(programacao.getId());
            this.defineAlarmManagerProximaProgramacao(programacao.getHora_fim().toString());
            this.verificaLiberacaoAvalicao(programacao.getHora_inicio().toString(), programacao.getHora_fim().toString());
        }
    }

    public void retornoProgramacaoService(Programacao programacao){
        this.atualizaInterface(programacao, false);
        this.setIdProgramacao(programacao.getId());
        this.defineAlarmManagerProximaProgramacao(programacao.getHora_fim().toString());

        this.idUsuario = contexto.getSharedPreferences("usuario", Context.MODE_PRIVATE).getLong("id", 0);

        if ( programacao.getAvaliado() != 1 ) {
            this.verificaLiberacaoAvalicao(programacao.getHora_inicio().toString(), programacao.getHora_fim().toString());
        } else {
            DetailActivity.defineStadoComponentesPosAvaliacao();
        }
    }

    //TODO: TESTE PARA IMPLEMENTAÇÂO DA LISTVIEW
    public List<Programacao> carregaListaProgramacaoService(JSONArray jsonArray){
        List<Programacao> lista = new ArrayList<>();
        Programacao programacao;
        JSONObject jsonObject;
        if ( jsonArray != null ) {

            for( int i = 0; i < jsonArray.length(); i++ ) {

                try {

                    jsonObject = jsonArray.getJSONObject(i);
                    programacao = montaProgramacao(jsonObject);
                    lista.add(programacao);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return lista;
    };

    //TODO: Isso vai mudar, possivelmente havera uma classe AvaliaçãoBusiness
    public void retornoAvaliacaoService(JSONObject jsonObject, boolean novaProgramacao){
        boolean ehMesmoPrograma = false;
        if ( jsonObject != null ) {

            Programacao programacao = montaProgramacao(jsonObject);

            if ( this.getIdProgramacao() == programacao.getId() ) {
                ehMesmoPrograma = true;
            }

            programacao.setAvaliado(1);

            for (int i = 0; i <  MainActivity.listaProgramacao.size(); i++ ) {
                if ( MainActivity.listaProgramacao.get(i).getId().equals(programacao.getId()) ) {
                    MainActivity.listaProgramacao.set(i, programacao);
                }
            }

            this.atualizaInterface(programacao, ehMesmoPrograma);
            this.setIdProgramacao(programacao.getId());
            //this.defineAlarmManagerProximaProgramacao(programacao.getHora_fim().toString());
            //this.verificaLiberacaoAvalicao(programacao.getHora_inicio().toString(), programacao.getHora_fim().toString());

        }
    }

    public void atualizaInterface(Programacao programacao, boolean ehMesmoPrograma ){

        if ( programacao != null) {

            Log.i(LOG, "Atualizando interface com programação encontrada.");

            if ( !ehMesmoPrograma )
                LoadImage.load(DetailActivity.imageLogoPrograma, contexto, programacao.getPrograma().getLogo());

            DetailActivity.textTituloProgramacao.setText(programacao.getNome().toString());
            DetailActivity.textHoraInicio.setText(programacao.getHora_inicio().toString());
            DetailActivity.textHoraFim.setText(programacao.getHora_fim().toString());
            DetailActivity.textQtdAv_positiva.setText(""+programacao.getAv_positiva());
            DetailActivity.textQtdAv_negativa.setText(""+programacao.getAv_negativa());
            DetailActivity.textDescricao.setText("" + programacao.getDescricao());

            //Atualiza objeto programação da DetailActivity
            DetailActivity.programacaoBusiness = this;

        } else {

            Log.i(LOG, "Atualizando interface sem programação encontrada.");

            DetailActivity.textTituloProgramacao.setText("Sem mais programação para hoje.");
            LoadImage.load(DetailActivity.imageLogoPrograma, contexto, "" );

        }

    }

    public void verificaLiberacaoAvalicao(String inicio, String fim){

        Calendar dataInicio = null;
        Calendar dataFim = null;
        Calendar agora = null;
        int hora = 0;
        int minuto = 0;

        agora = Calendar.getInstance();
        agora.setTimeInMillis(System.currentTimeMillis());

        dataInicio = Calendar.getInstance();
        hora = Integer.parseInt(inicio.substring(0, 2));
        minuto = Integer.parseInt(inicio.substring(3,5));
        dataInicio.set(Calendar.HOUR_OF_DAY, hora);
        dataInicio.set(Calendar.MINUTE, minuto);
        dataInicio.set(Calendar.SECOND, 0);

        dataFim = Calendar.getInstance();
        hora = Integer.parseInt(fim.substring(0, 2));
        minuto = Integer.parseInt(fim.substring(3,5));
        dataFim.set(Calendar.HOUR_OF_DAY, hora);
        dataFim.set(Calendar.MINUTE, minuto);
        dataFim.set(Calendar.SECOND, 99);

        String horaAtual = new Timestamp(agora.getTime().getTime()).toString();
        String horaInicio = new Timestamp(dataInicio.getTime().getTime()).toString();
        String horaFim = new Timestamp(dataFim.getTime().getTime()).toString();

        Log.i(LOG, "Verifica se pode habilitar avaliação: data hora atual = " + horaAtual + ", inicio= " + horaInicio + ", fim= " + horaFim );

        if ( agora.getTimeInMillis() >= dataInicio.getTimeInMillis() && agora.getTimeInMillis() <= dataFim.getTimeInMillis() ) {

            Log.i(LOG, "Programação liberada para avaliação");
            //DetailActivity.liberaAvaliacao(true);
            DetailActivity.defineStadoComponentesEmAvaliacao();

        } else {

            Log.i(LOG, "Programação ainda não pode ser liberada para avaliação");
            //DetailActivity.liberaAvaliacao(false);
            DetailActivity.defineStadoComponentesPreAvaliacao();
            this.defineAlarmManagerLiberacaoAvaliacao(inicio);


        }
    }
}
