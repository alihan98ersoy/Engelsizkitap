package com.cmpestudents.seslikitap;

public class Yazarlar {
    //Yazarlar objesi kitaplar arraylist i için lazım SQLdatabase class ındaki

    public int Yazarid;
    public String Yazarismi;
    public int Sesid;

    public Yazarlar(int yazarid, String yazarismi, int sesid) {
        Yazarid = yazarid;
        Yazarismi = yazarismi;
        Sesid = sesid;
    }

    public int getYazarid() {
        return Yazarid;
    }

    public void setYazarid(int yazarid) {
        Yazarid = yazarid;
    }

    public String getYazarismi() {
        return Yazarismi;
    }

    public void setYazarismi(String yazarismi) {
        Yazarismi = yazarismi;
    }

    public int getSesid() {
        return Sesid;
    }

    public void setSesid(int sesid) {
        Sesid = sesid;
    }
    @Override
    public String toString() {
        return  Yazarid +
                "//" + Yazarismi + '\'' +
                ", Sesid=" + Sesid +
                '}';
    }
}
