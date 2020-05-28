package com.cmpestudents.seslikitap;

import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TexttoSpeechclass {

    Activity activity;
    TextToSpeech t1;
    int ilksefer=0;
    Kitapplayer kp;

    public TexttoSpeechclass(final Activity a) {
        this.activity = a;
        this.kp=kp;
       // System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!TTSsuccesstexttospeech15");
        t1 = new TextToSpeech(activity.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!TTSsuccesstexttospeech18");
                if (status == TextToSpeech.SUCCESS) {

                    int ttsLang = t1.setLanguage(new Locale("tr", "TR"));
                    //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!TTS"+t1.getAvailableLanguages());
                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Intent installIntent = new Intent();
                        installIntent.setAction(
                                TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        a.startActivity(installIntent);//tts verisi indirme ekranı çıkıyor
                        System.out.println("TTS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!TTS" + "The Language is not supported!");
                    } else {
                        System.out.println("TTS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!TTS" + "Language Supported.");
                    }
                    System.out.println("TTS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!TTS" + "Initialization success.");
                    System.out.println("TTS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!activity "+activity.getIntent().toString());

                    //Hoşgeldiniz mesajları
                    if(ilksefer==0&&activity.getIntent().toString().equals("Intent { cmp=com.cmpestudents.seslikitap/.Homepageactivtiy }")){
                        Oku(activity.getString(R.string.Hoşgeldinmesajı));
                        ilksefer++;
                    }else if(Homepageactivtiy.ilkseferkitap==0&&activity.getIntent().toString().equals("Intent { cmp=com.cmpestudents.seslikitap/.Kitapplayer (has extras) }")){
                        SırayaekleveOku(Kitapplayer.yazarismi+" yazarının "+Kitapplayer.kitapokunus+" "+activity.getString(R.string.Hoşgeldinmesajıkitapplayer));
                        Homepageactivtiy.ilkseferkitap++;
                    }




                } else {
                    System.out.println("TTS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!TTS" + "TTS Initialization failed!");
                }
            }
        });

    }

    public void Oku(String metin){
        //onPause();

        t1.speak(metin,TextToSpeech.QUEUE_FLUSH,null);

    }
    public boolean İsspeaking(){
        return t1.isSpeaking();
    }

    public void SırayaekleveOku(String metin){

        t1.speak(metin,TextToSpeech.QUEUE_ADD,null);

    }



    public void onPause(){
        if(t1 !=null){
            t1.stop();
        }
    }



}
