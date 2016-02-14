package com.ibopar.business;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.ibopar.MainActivity;
import com.ibopar.model.Programacao;
import com.ibopar.model.Usuario;
import com.ibopar.service.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabio Moura on 18/10/2015.
 */
public class UsuarioBusiness {

    private Context contexto;
    //private static String LOG = Usuario.class.getName().toString();
    private static final String LOG = "TESTE";
    private Usuario usuario = null;
    private WebService webService;

    public UsuarioBusiness( Context contexto ){
        this.contexto = contexto;
    }

    public Context getContexto() {
        return contexto;
    }

    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void cadastrarUsuario(String login, String senha ){

        Log.i(LOG, "Cadastrando usuário");
        this.usuario = new Usuario();
        this.usuario.setLogin(login);
        this.usuario.setSenha(senha);
        this.usuario.setEmail("provisorio");

        webService = new WebService();
        webService.cadastrarUsuario(this);

    };

    public void autenticarUsuario(String login, String senha){

        this.usuario = new Usuario();
        this.usuario.setLogin(login);
        this.usuario.setSenha(senha);

        webService = new WebService();
        webService.autenticarUsuario(this);

    };

    public Usuario montaUsuario(JSONObject jsonObject){

        Log.i(LOG, "Criando objeto usuário");
        Usuario usuario = null;

        try {
            usuario = new Usuario();
            usuario.setId(jsonObject.getLong("id"));
            usuario.setLogin(jsonObject.getString("login"));
            usuario.setSenha(jsonObject.getString("senha"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return usuario;

    }

    public void resultUsuarioService(JSONObject jsonUsuario, String tipoRequisicao){

        Intent intent = null;
        Usuario usuario = null;

        if ( jsonUsuario != null ) {

            usuario = montaUsuario(jsonUsuario);

        }

        if ( usuario != null ) {

            Log.i(LOG, "Registrando usuário no SharedPreference");

            SharedPreferences preference = contexto.getSharedPreferences("usuario", Context.MODE_PRIVATE);
            Editor editor = preference.edit();
            editor.putLong("id", usuario.getId());
            editor.commit();

            Intent extra = ( (Activity) contexto ).getIntent();
            List<Programacao> listaProgramacao = extra.getParcelableArrayListExtra("lista");

            intent = new Intent(contexto, MainActivity.class);

            intent.putParcelableArrayListExtra("lista", (ArrayList<? extends Parcelable>) listaProgramacao);

            contexto.startActivity(intent);
            Toast.makeText(contexto, "Bem Vindo " + usuario.getLogin().toString(), Toast.LENGTH_SHORT).show();

            ( (Activity) contexto ).finish();

        } else {
            String msg = "";
            if ( tipoRequisicao.equals("usuario_service") ) {
                msg = "Não foi possível realizar o cadastro do usuário, tente mais tarde!";
            } else {
                msg = "Usuário ou senha inválido, tente novamente!";
            }

            Toast.makeText(contexto, msg, Toast.LENGTH_SHORT).show();
        }

    }

}
