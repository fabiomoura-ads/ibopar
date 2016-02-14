package com.ibopar.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Fabio Moura on 18/10/2015.
 */
public class Programa implements Parcelable{

    private Long id;
    private String nome;
    private String hora_inicio;
    private String hora_fim;
    private String logo;
    private Bitmap logoBitmap;

    public Programa(){
    }

    protected Programa(Parcel in) {
        nome = in.readString();
        hora_inicio = in.readString();
        hora_fim = in.readString();
        logo = in.readString();
        logoBitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Programa> CREATOR = new Creator<Programa>() {
        @Override
        public Programa createFromParcel(Parcel in) {
            return new Programa(in);
        }

        @Override
        public Programa[] newArray(int size) {
            return new Programa[size];
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Bitmap getLogoBitmap() {
        return logoBitmap;
    }

    public void setLogoBitmap(Bitmap logoBitmap) {
        this.logoBitmap = logoBitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(hora_inicio);
        dest.writeString(hora_fim);
        dest.writeString(logo);
        dest.writeParcelable(logoBitmap, flags);
    }
}
