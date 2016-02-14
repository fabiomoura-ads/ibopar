package com.ibopar.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/**
 * Created by Fabio Moura on 18/10/2015.
 */
public class Programacao implements Parcelable{

    private Long id;
    private String nome;
    private String hora_inicio;
    private String hora_fim;
    private Date data;
    private Programa programa;
    //private Categoria categoria;
    private int av_positiva;
    private int av_negativa;
    private String descricao;
    private int avaliado;

    public Programacao(){
    }

    protected Programacao(Parcel in) {
        id = in.readLong();
        nome = in.readString();
        hora_inicio = in.readString();
        hora_fim = in.readString();
        programa = in.readParcelable(Programa.class.getClassLoader());
        av_positiva = in.readInt();
        av_negativa = in.readInt();
        descricao = in.readString();
        avaliado = in.readInt();
    }

    public static final Creator<Programacao> CREATOR = new Creator<Programacao>() {
        @Override
        public Programacao createFromParcel(Parcel in) {
            return new Programacao(in);
        }

        @Override
        public Programacao[] newArray(int size) {
            return new Programacao[size];
        }
    };

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_fim() {
        return hora_fim;
    }

    public void setHora_fim(String hora_fim) {
        this.hora_fim = hora_fim;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    /*public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }*/

    public int getAv_positiva() {
        return av_positiva;
    }

    public void setAv_positiva(int av_positiva) {
        this.av_positiva = av_positiva;
    }

    public int getAv_negativa() {
        return av_negativa;
    }

    public void setAv_negativa(int av_negativa) {
        this.av_negativa = av_negativa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getAvaliado(){
        return this.avaliado;
    }

    public void setAvaliado(int avaliado){
        this.avaliado = avaliado;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nome);
        dest.writeString(hora_inicio);
        dest.writeString(hora_fim);
        dest.writeParcelable(programa, flags);
        dest.writeInt(av_positiva);
        dest.writeInt(av_negativa);
        dest.writeString(descricao);
        dest.writeInt(avaliado);
    }
}