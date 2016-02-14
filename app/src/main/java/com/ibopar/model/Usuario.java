package com.ibopar.model;

/**
 * Created by Fabio Moura on 18/10/2015.
 */
public class Usuario {

    private Long id;
    private String login;
    private String senha;
    private String perfil;
    private String email;

    public Usuario(){
        this.setPerfil("app");
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public String getPerfil() {
        return this.perfil;
    }
    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }


}
