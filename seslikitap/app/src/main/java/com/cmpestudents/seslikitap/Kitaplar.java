package com.cmpestudents.seslikitap;

import android.graphics.Bitmap;

public class Kitaplar {
//Kitaplar objesi kitaplar arraylist i için lazım SQLdatabase class ındaki

    public int Kitapid;
    public String Kitapismi;
    public int Yazarid;
    public int Isimsesid;
    public int Sesid;
    public Bitmap Kapakresmi;

    public Kitaplar(int kitapid, String kitapismi,int yazarid ,int isimsesid, int sesid, Bitmap bitmap) {
        Kitapid = kitapid;
        Kitapismi = kitapismi;
        Yazarid=yazarid;
        Isimsesid = isimsesid;
        Sesid = sesid;
        Kapakresmi = bitmap;
    }

    public int getKitapid() {
        return Kitapid;
    }

    public void setKitapid(int kitapid) {
        Kitapid = kitapid;
    }

    public String getKitapismi() {
        return Kitapismi;
    }

    public void setKitapismi(String kitapismi) {
        Kitapismi = kitapismi;
    }

    public int getYazarid() {
        return Yazarid;
    }

    public void setYazarid(int yazarid) {
        Yazarid = yazarid;
    }

    public int getIsimsesid() {
        return Isimsesid;
    }

    public void setIsimsesid(int isimsesid) {
        Isimsesid = isimsesid;
    }

    public int getSesid() {
        return Sesid;
    }

    public void setSesid(int sesid) {
        Sesid = sesid;
    }

    public Bitmap getKapakresmi() {
        return Kapakresmi;
    }

    public void setKapakresmi(Bitmap kapakresmi) {
        Kapakresmi = kapakresmi;
    }

    @Override
    public String toString() {
        return  Kitapid +"//"+Yazarid+
                "//" + Kitapismi +"//"+
                ", Isimsesid=" + Isimsesid +
                ", Sesid=" + Sesid +
                '}';
    }
}



