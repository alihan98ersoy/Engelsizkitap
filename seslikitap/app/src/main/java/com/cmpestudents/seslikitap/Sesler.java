package com.cmpestudents.seslikitap;

public class Sesler {
    //Sesler objesi kitaplar arraylist i için lazım SQLdatabase class ındaki

    public int Sesid;
    public String Sesuzantisi;
    public Boolean Urlmi;
    public String Süresi;//süresi yok açıklaması oldu bu açıklama yeri unutmuşum
    public String Kaldigidakika;

    public Sesler(int sesid, String sesuzantisi, Boolean url, String süresi, String kaldigidakika) {
        Sesid = sesid;
        Sesuzantisi = sesuzantisi;
        Urlmi = url;
        Süresi = süresi;
        Kaldigidakika = kaldigidakika;
    }

    public int getSesid() {
        return Sesid;
    }

    public void setSesid(int sesid) {
        Sesid = sesid;
    }

    public String getSesuzantisi() {
        return Sesuzantisi;
    }

    public void setSesuzantisi(String sesuzantisi) {
        Sesuzantisi = sesuzantisi;
    }

    public Boolean getUrlmi() {
        return Urlmi;
    }

    public void setUrlmi(Boolean url) {
        this.Urlmi = url;
    }

    public String getSüresi() {
        return Süresi;
    }

    public void setSüresi(String süresi) {
        Süresi = süresi;
    }

    public String getKaldigidakika() {
        return Kaldigidakika;
    }

    public void setKaldigidakika(String kaldigidakika) {
        Kaldigidakika = kaldigidakika;
    }

    @Override
    public String toString() {
        return Sesid +
                "//" + Sesuzantisi + '\'' +", Açıklama='" + Süresi + '\'' +
                ", Urlmi=" + Urlmi +
                ", Kaldigidakika='" + Kaldigidakika + '\'' +
                '}';
    }
}
