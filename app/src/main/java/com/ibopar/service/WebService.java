package com.ibopar.service;

import com.ibopar.business.ProgramacaoBusiness;
import com.ibopar.business.UsuarioBusiness;
import com.ibopar.model.Usuario;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Fabio Moura on 18/10/2015.
 * Classe WebService, criada para encapsular os métodos parâmetros das requisições, assim
 * como também a chamada dos AsyncTasks das requisições;
 */
public class WebService {

    public static final String WS_SERVIDOR	  = "http://ibopar.com.br/service.php";
    //public static final String WS_API 		  = "service.php" ;
    public static final String WS_CAD_USUARIO = "usuario_service" ;
    public static final String WS_AUTENTICAR  = "autenticar_usuario" ;
    public static final String WS_PROGRAMACAO = "programacao_service" ;
    public static final String WS_AVALIACAO   = "avaliacao_service" ;

    public WebService(){}

    @Deprecated
    public String[] criaURLAvaliacao( ProgramacaoBusiness programacaoBusiness , boolean avaliacao ){

        String[] params = null;

        params = new String[5];
        //params[0] = ( WS_SERVIDOR + "/" + WS_API );
        params[0] = WS_SERVIDOR;
        params[1] = WS_AVALIACAO;
        params[2] = "" + programacaoBusiness.getIdUsuario();
        params[3] = "" + programacaoBusiness.getIdProgramacao();
        params[4] = "" + avaliacao;

        return params;

    }

    public String[][] pegaParametrosAvaliacao( ProgramacaoBusiness programacaoBusiness , boolean avaliacao ){

        String[][] params = {
                {"url", WS_SERVIDOR},
                {"servico", WS_AVALIACAO},
                {"usuario_id", "" + programacaoBusiness.getIdUsuario()},
                {"programacao_id", "" + programacaoBusiness.getIdProgramacao()},
                {"avaliacao", "" + avaliacao}
        };

        return params;
    }

    @Deprecated
    public String[] criaURLConsulta(ProgramacaoBusiness programacaoBusiness){
        String[] params = null;

        params = new String[4];
        //params[0] = ( WS_SERVIDOR + "/" + WS_API );
        params[0] = WS_SERVIDOR;
        params[1] = WS_PROGRAMACAO;
        params[2] = "" + programacaoBusiness.getDataHora();
        params[3] = "" + programacaoBusiness.getIdUsuario();

        return params;

    }

    public String[][] pegaParametrosConsulta(ProgramacaoBusiness programacaoBusiness){

        String[][] params = {
                                {"url", WS_SERVIDOR},
                                {"servico", WS_PROGRAMACAO},
                                {"data", "" + programacaoBusiness.getDataHora()},
                                {"usuario_id", "" + programacaoBusiness.getIdUsuario()}
                            };
        return params;

    }

    @Deprecated
    public String[] criaURLUsuario(Usuario usuario){

        String[] params = null;

        params = new String[5];
        //params[0] = ( WS_SERVIDOR + "/" + WS_API );
        params[0] = WS_SERVIDOR;
        params[1] = WS_CAD_USUARIO;
        params[2] = usuario.getLogin().toString();
        params[3] = usuario.getSenha().toString();

        return params;

    }

    public String[][] pegaParametrosCadastrarUsuario(Usuario usuario){

        String[][] params = {
                                {"url", WS_SERVIDOR},
                                {"servico", WS_CAD_USUARIO},
                                {"login", usuario.getLogin().toString()},
                                {"senha", usuario.getSenha().toString()},
                                {"perfil",usuario.getPerfil().toString()},
                                {"email",usuario.getEmail().toString() } };
        return params;

    };

    @Deprecated
    public String[] criaURLAntenticar(Usuario usuario){

        String[] params = null;

        params = new String[5];
        //params[0] = ( WS_SERVIDOR + "/" + WS_API );
        params[0] = WS_SERVIDOR;
        params[1] = WS_AUTENTICAR;
        params[2] = usuario.getLogin().toString();
        params[3] = usuario.getSenha().toString();

        return params;

    }

    public String[][] pegaParametrosAutenticarUsuario(Usuario usuario){

        String[][] params = {
                                {"url", WS_SERVIDOR},
                                {"servico", WS_AUTENTICAR},
                                {"login", usuario.getLogin().toString()},
                                {"senha", usuario.getSenha().toString()}
                            };

        return params;

    }

    @Deprecated
    public String[] criaURLListarProgramacao(){

        String[] params = null;
        Date data;
        String dataString;

        data = new Date();
        dataString = new Timestamp(data.getTime()).toString();

        params = new String[5];
        //params[0] = ( WS_SERVIDOR + "/" + WS_API );
        params[0] = WS_SERVIDOR;
        params[1] = WS_PROGRAMACAO;
        params[2] = "" + dataString;
        params[3] = "true";

        return params;

    }

    public String[][] pegaParametrosListarProgramacao(){

        Date data;
        String dataString;
        data = new Date();
        dataString = new Timestamp(data.getTime()).toString();

        String[][] params = {
                                {"url", WS_SERVIDOR},
                                {"servico", WS_PROGRAMACAO},
                                {"data", dataString},
                                {"carregaList", "True"}
                            };
        return params;

    }

    public void requisitaProximaProgramacao(ProgramacaoBusiness programacaoBusiness){

        new ProgramacaoService(programacaoBusiness).execute(pegaParametrosConsulta(programacaoBusiness));

    }

    public void realizaAvaliacao(ProgramacaoBusiness programacaoBusiness, boolean avaliacao ){

        new AvaliacaoService(programacaoBusiness).execute(pegaParametrosAvaliacao(programacaoBusiness, avaliacao));
    }

    public void cadastrarUsuario(UsuarioBusiness usuarioBusiness){

        new UsuarioService(usuarioBusiness).execute(pegaParametrosCadastrarUsuario(usuarioBusiness.getUsuario()));

    }

    public void autenticarUsuario(UsuarioBusiness usuarioBusiness){

        new UsuarioService(usuarioBusiness).execute(pegaParametrosAutenticarUsuario(usuarioBusiness.getUsuario()));

    }

}
