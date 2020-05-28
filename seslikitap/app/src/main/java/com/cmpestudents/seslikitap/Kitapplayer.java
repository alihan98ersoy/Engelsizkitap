package com.cmpestudents.seslikitap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chinodev.androidneomorphframelayout.NeomorphFrameLayout;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

public class Kitapplayer extends AppCompatActivity {

    private ImageButton geri,ileri,oynat,durdur;
    private ImageView kapak;
    private TextView anlık,toplam,kitapismi;
    private MediaPlayer mPlayer;
    private SeekBar seekbar;
    private int oTime =0,sTime,eTime=0,fTime = 5000, bTime=5000;
    private Handler handler;
    Runnable run;
    private Handler hdir = new Handler();

    private NeomorphFrameLayout oynatframe, durdurframe, ileriframe, ileriframe2, geriframe, geriframe2;
    //uptade için
    static String yazarismi="";
    static String kitapisim="";
    static String kitapokunus="";
    String sesurl="";
    String kaldığıyer="";
    String acıklama="";
    int sesid=-1;



    //TexttoSpeech için
    TexttoSpeechclass t1 ;
    ArrayList<String> data=new ArrayList<>();
    Kullanicidansesal k;
    ArrayList<String> anahtarkelimeler=new ArrayList<>();
    //Bunu mu demek istedin bi önce ki data
    String biönceki="";
    boolean oynuyormu=true;//ekrana kısa dokunmada oynuyorsa durdur duruyorsa oynat için

    private static Context mContext;
    private static Kitapplayer instance;

    public static Kitapplayer getInstance() {
        return instance;
    }

    public static Context getContext() {
        //  return instance.getApplicationContext();
        return mContext;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitapplayer);

        mContext = getApplicationContext();

        geri = (ImageButton)findViewById(R.id.imageButtongeri);
        ileri = (ImageButton)findViewById(R.id.imageButtonileri);
        oynat = (ImageButton)findViewById(R.id.imageButtonbaslatdurdur);
        durdur = (ImageButton)findViewById(R.id.imageButtonbaslatdurdur2);
        durdur.setVisibility(View.INVISIBLE);
        kitapismi = (TextView)findViewById(R.id.txtkitapismi);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Orchide.ttf");
        kitapismi.setTypeface(typeface);
        kitapismi.setSelected(true);
        kapak=(ImageView)findViewById(R.id.imageView);
        anlık = (TextView)findViewById(R.id.txtanlıkzamanı);
        toplam = (TextView)findViewById(R.id.txttoplamsüresi);
        seekbar = (SeekBar)findViewById(R.id.seekBar);

        oynatframe = (NeomorphFrameLayout)findViewById(R.id.layout_btn6);
        durdurframe = (NeomorphFrameLayout)findViewById(R.id.layout_btn7);
        durdurframe.setVisibility(View.INVISIBLE);

        ileriframe = (NeomorphFrameLayout)findViewById(R.id.layout_btn8);
        ileriframe2 = (NeomorphFrameLayout)findViewById(R.id.layout_btn11);
        ileriframe2.setVisibility(View.INVISIBLE);

        geriframe = (NeomorphFrameLayout)findViewById(R.id.layout_btn5);
        geriframe2 = (NeomorphFrameLayout)findViewById(R.id.layout_btn9);
        geriframe2.setVisibility(View.INVISIBLE);

        kitapisim = getIntent().getStringExtra("ismi");
        //System.out.println("kitapplayer!!!!!!!!!!!!!!!!!!!!kitapismi"+kitapisim);


        if(kitapisim!=null||!kitapisim.equals("")) {
            for (int i = 0; i < SQLdatabase.kitaplarliste.size(); i++) {
                if (SQLdatabase.kitaplarliste.get(i).getKitapismi().toLowerCase().equals(kitapisim.toLowerCase())) {
                    Kitaplar k = SQLdatabase.kitaplarliste.get(i);
                    kitapisim = k.getKitapismi();
                    kapak.setImageBitmap(k.getKapakresmi());
                    for (int j = 0; j < SQLdatabase.yazarlarliste.size(); j++) {
                        if (SQLdatabase.yazarlarliste.get(j).getYazarid() == k.getYazarid()) {
                            yazarismi = SQLdatabase.yazarlarliste.get(j).getYazarismi();
                        }
                    }
                    for (int j = 0; j < SQLdatabase.seslerlister.size(); j++) {
                        if (SQLdatabase.seslerlister.get(j).getSesid() == k.getSesid()) {
                            sesid=SQLdatabase.seslerlister.get(j).getSesid();
                            sesurl = SQLdatabase.seslerlister.get(j).getSesuzantisi();
                            kaldığıyer = SQLdatabase.seslerlister.get(j).getKaldigidakika();
                            acıklama=SQLdatabase.seslerlister.get(j).getSüresi();
                            // System.out.println("kitapplayer!!!!!!!!!!!!!!!!initialize:"+sesid+" "+sesurl+" "+acıklama+" "+kaldığıyer);
                        }
                    }
                }
            }
        }

        kitapismi.setText(kitapisim + " - " + yazarismi);
        kitapokunus=acıklama;
        String url="";
        //mPlayer=MediaPlayer.create(this,R.raw.okanbayulgen_drjekyllvemr_hyde);
        //String url ="https://drive.google.com/uc?authuser=0&id=1a7n59xU-wYZ1A5tyrfuO5vAFZIZCVC-5&export=download";// initialize Uri here//

        url ="https://audio-book.azurewebsites.net/"+sesurl+".mp3";
        String yedekurl="http://192.168.1.21/"+sesurl+".mp3";


        // System.out.println("kitapplayer!!!!!!!!!!!!!!!!!!!url:  "+url);
        //String uri="http://www.all-birds.com/Sound/western%20bluebird.wav";
        mPlayer = new MediaPlayer();
        //mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        while(true) {
            try {
                mPlayer.setDataSource(url);
                mPlayer.prepare();
                break;
            } catch (Exception e) {
                System.out.println("Kitapplayer!!!!!!!!!!!!!!!!!!!!!!!!!!!!!mPlayerpreparecatch " + e.getMessage());
                url=yedekurl;


            }
        }


        oynat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(run);
                oynat();
            }
        });
        durdur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Durdur();
            }
        });
        ileri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ileri();
            }
        });
        ileri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ileriframe.setVisibility(View.INVISIBLE);
                        ileriframe2.setVisibility(View.VISIBLE);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        break;

                    case MotionEvent.ACTION_UP:
                        ileriframe.setVisibility(View.VISIBLE);
                        ileriframe2.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Geri();
            }

        });
        geri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        geriframe.setVisibility(View.INVISIBLE);
                        geriframe2.setVisibility(View.VISIBLE);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        break;

                    case MotionEvent.ACTION_UP:
                        geriframe.setVisibility(View.VISIBLE);
                        geriframe2.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        //kitapismi.setText("deneme");
        oynat();
        Durdur();
        Stimeayarla();

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sTime=progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //  System.out.println("kitapplayer!!!!!!!!!!!!!!!!!!!!!!!!onStartTrackingTouch"+seekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlayer.seekTo(sTime);
            }
        });
        int süre=0;
        if(Homepageactivtiy.ilkseferkitap==0){
        //anahtar kelimeleri initialize etme
        AnahtarKelimeEkle();
       süre=10000;
        süre=süre+((kitapismi.length()+yazarismi.length())*50);}else {süre=1000;}
        System.out.println("kitapplayer!!!!!!!!!!!!!!!!!!!kitapisimokunuş"+yazarismi+kitapokunus);
        //Hoşgeldin mesajı
        t1=new TexttoSpeechclass(this);

        handler = new Handler();
        run=new Runnable() {
            @Override
            public void run() {
                oynat();
            }
        };
        handler.postDelayed(run,süre);
    }//on crete sonu





    @Override
    protected void onDestroy() {//kapanırken
        super.onDestroy();
        //System.out.println("kitapplayer!!!!!!!!!!!!!!!!!!!!!!!!ondestroy oldu");
        Durdur();
        t1.onPause();
        //System.out.println("kitapplayer!!!!!!!!!!!!!!!!!!!!!!!stime: "+sTime);
        //SQLdatabase.UPDATE("Ses",new Sesler(sesid,sesurl,true,acıklama,sTime+""),sesid+"");
        handler.removeCallbacks(run);
        hdir.removeCallbacks(UpdateSongTime);
        mPlayer.stop();
        mPlayer.reset();
    }

    @Override
    protected void onStop() {//Geri gittiğinde
        super.onStop();
        Durdur();
        t1.onPause();
        //System.out.println("kitapplayer!!!!!!!!!!!!!!!!!!!!!!!stime: "+sTime);
        SQLdatabase.UPDATE("Ses",new Sesler(sesid,sesurl,true,acıklama,sTime+""),sesid+"");
        // System.out.println("kitapplayer!!!!!!!!!!!!!!!!!!!!!!!sesnesnesi: "+new Sesler(sesid,sesurl,true,acıklama,sTime+""));
        handler.removeCallbacks(run);
        hdir.removeCallbacks(UpdateSongTime);
        mPlayer.stop();
        mPlayer.reset();
    }



    public void Stimeayarla(){
        if(kaldığıyer.equals("null")||kaldığıyer.equals("")){
            //System.out.println("kitapplayer!!!!!!!!!!!!!!!stime güncellenmedi:  "+kaldığıyer);
        }else{
            sTime=Integer.parseInt(kaldığıyer);
            mPlayer.seekTo(sTime);
            //System.out.println("kitapplayer!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!stimeayarla mplayergetcurrentpoz:"+mPlayer.getCurrentPosition());
            //System.out.println("kitapplayer!!!!!!!!!!!!!!!!!!!!!!!!!stime değişti: "+ sTime);
        }
    }
    public void Geri(){
        if((sTime - bTime) > 0)
        {
            sTime = sTime - bTime;
            mPlayer.seekTo(sTime);
        }
        else
        {
            //Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
            t1.SırayaekleveOku(((sTime)/1000)+"saniye kadar geri alabilirsiniz 5 saniye geri alamadım");
        }
                /*if(!playbtn.isEnabled()){
                    playbtn.setEnabled(true);
                }*/
    }
    public void Ileri(){
        if((sTime + fTime) <= eTime)
        {
            sTime = sTime + fTime;
            mPlayer.seekTo(sTime);
        }
        else
        {
            // Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
            t1.SırayaekleveOku(((eTime-sTime)/1000)+"saniye kadar ileri alabilirsiniz 5 saniye ileri alamadım");
        }
                /*if(!oynat.){
                    playbtn.setEnabled(true);
                }*/
    }
    public void Durdur(){
        mPlayer.pause();
        sTime=mPlayer.getCurrentPosition();
        oynat.setVisibility(View.VISIBLE);
        durdur.setVisibility(View.INVISIBLE);
        oynatframe.setVisibility(View.VISIBLE);
        durdurframe.setVisibility(View.INVISIBLE);
        // Toast.makeText(getApplicationContext(),"Pausing Audio", Toast.LENGTH_SHORT).show();
    }
    public void oynat(){
        //Toast.makeText(Kitapplayer.this, "Playing Audio", Toast.LENGTH_SHORT).show();
        if(t1!=null){ t1.onPause();}

        mPlayer.seekTo(sTime);
        mPlayer.start();
        eTime = mPlayer.getDuration();
        //System.out.println("kitapplayer!!!!!!!!!!!!!!!!!oynatstime önce 219  "+sTime);
        sTime = mPlayer.getCurrentPosition();
        //System.out.println("kitapplayer!!!!!!!!!!!!!!!!!oynatstime sonra 221  "+sTime);
        if(oTime == 0){
            seekbar.setMax(eTime);
            oTime =1;
        }
        toplam.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
                TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
        anlık.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );
        seekbar.setProgress(sTime);
        hdir.postDelayed(UpdateSongTime, 100);
        oynat.setVisibility(View.INVISIBLE);
        durdur.setVisibility(View.VISIBLE);
        oynatframe.setVisibility(View.INVISIBLE);
        durdurframe.setVisibility(View.VISIBLE);
        //System.out.println("kitapplayer!!!!!!!!!!!!!!!!!oynatstime enson 234  "+sTime);
    }

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            //System.out.println("kitapplayer!!!!!!!!!!!!!runnablebaşı stime: "+sTime);
            sTime = mPlayer.getCurrentPosition();
            anlık.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                    TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))) );
            seekbar.setProgress(sTime);
            hdir.postDelayed(this, 100);
            // System.out.println("kitapplayer!!!!!!!!!!!!!runnablesonu stime: "+sTime);
        }
    };




    public void AnahtarKelimeEkle(){
        //Anahtar Kelimeler
        anahtarkelimeler.add("evet");

        anahtarkelimeler.add("oynat");
        anahtarkelimeler.add("başlat");
        anahtarkelimeler.add("oku");
        anahtarkelimeler.add("play");
        anahtarkelimeler.add("devam et");
        anahtarkelimeler.add("neyse");

        anahtarkelimeler.add("yardım");

        anahtarkelimeler.add("geri dön");
        anahtarkelimeler.add("kapat");
        anahtarkelimeler.add("yeter");
        anahtarkelimeler.add("anasayfaya geri dön");
        anahtarkelimeler.add("anasayfaya dön");
        anahtarkelimeler.add("anasayfa");

        anahtarkelimeler.add("ileri al");
        anahtarkelimeler.add("ileriye alır mısın");
        anahtarkelimeler.add("biraz ileriye alır mısın");
        anahtarkelimeler.add("biraz ileri al");


        anahtarkelimeler.add("geri al");
        anahtarkelimeler.add("geriye alır mısın");
        anahtarkelimeler.add("biraz geri alır mısın");
        anahtarkelimeler.add("biraz geri al");



    }




    public void Anahtarkelimeyikullan(String[] keyarray){
        System.out.println("kitapplayer!!!!!!!!!!!!!!!anahtar kelime kullan"+keyarray[0]+"---"+keyarray[1]+"---"+keyarray[2]);
        boolean anlamadım=true;
        if(keyarray[1].equals("evet")&&Double.parseDouble(keyarray[2])>0.95){
            keyarray[1]=biönceki;
            anlamadım=false;
        }

        if(keyarray[1].equals("oynat")||keyarray[1].equals("başlat")||keyarray[1].equals("oku")||keyarray[1].equals("play")||keyarray[1].equals("devam et")
                ||keyarray[1].equals("neyse")){
            if(Double.parseDouble(keyarray[2])>0.95){
                oynat();
                anlamadım=false;
            }else if(Double.parseDouble(keyarray[2])>0.85){
                anlamadım=false;
                t1.SırayaekleveOku(keyarray[1]+" mı demek istediniz");
            }
        }

        if(keyarray[1].equals("yardım")){
            if(Double.parseDouble(keyarray[2])>0.97){
                t1.SırayaekleveOku(getString(R.string.yardımkitapplayer));
                anlamadım=false;
            }else if(Double.parseDouble(keyarray[2])>0.85){
                anlamadım=false;
                t1.SırayaekleveOku(keyarray[1]+" mı demek istediniz");
            }
        }

        if(keyarray[1].equals("geri dön")||keyarray[1].equals("kapat")||keyarray[1].equals("yeter")||keyarray[1].equals("anasayfa")||keyarray[1].equals("anasayfaya geri dön")||
                keyarray[1].equals("anasayfaya dön")){

            if(Double.parseDouble(keyarray[2])>0.89){
                finish();
                anlamadım=false;
            }else if(Double.parseDouble(keyarray[2])>0.8){
                anlamadım=false;
                t1.SırayaekleveOku( "anasayfaya geri dön mü demek istediniz");
            }

        }


        if(keyarray[1].equals("ileri al")||keyarray[1].equals("ileriye alır mısın")||keyarray[1].equals("biraz ileriye alır mısın")||keyarray[1].equals("biraz ileri al")){

            if((keyarray[1].equals("biraz ileriye alır mısın")||keyarray[1].equals("biraz ileri al"))&&Double.parseDouble(keyarray[2])>0.8){
                anlamadım=false;
                Ileri();//5 saniye ileri alma
                oynat();
            }else if((keyarray[1].equals("biraz ileriye alır mısın")||keyarray[1].equals("biraz ileri al"))&&Double.parseDouble(keyarray[2])>0.7){
                anlamadım=false;
                t1.SırayaekleveOku(keyarray[1]+" mı demek istediniz");
            }



            if((keyarray[1].equals("ileri al")||keyarray[1].equals("ileriye alır mısın"))&&Double.parseDouble(keyarray[2])>0.8){
                anlamadım=false;
                if((sTime + 15000) <= eTime)
                {
                    sTime = sTime + 15000;
                    mPlayer.seekTo(sTime);
                    oynat();
                }
                else
                {
                    // Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                    t1.SırayaekleveOku(((eTime-sTime)/1000)+"saniye kadar ileri alabilirsiniz 15 saniye ileri alamadım");
                }

            }else if((keyarray[1].equals("ileri al")||keyarray[1].equals("ileriye alır mısın"))&&Double.parseDouble(keyarray[2])>0.7){
                anlamadım=false;
                t1.SırayaekleveOku( keyarray[1]+" mı demek istediniz");
            }

        }//if ileri sonu





        if(keyarray[1].equals("geri al")||keyarray[1].equals("geriye alır mısın")||keyarray[1].equals("biraz geriye alır mısın")||keyarray[1].equals("biraz geri al")){

            if((keyarray[1].equals("biraz geriye alır mısın")||keyarray[1].equals("biraz geri al"))&&Double.parseDouble(keyarray[2])>0.8){
                anlamadım=false;
                Geri();//5 saniye geri alma
                oynat();
            }else if((keyarray[1].equals("biraz geriye alır mısın")||keyarray[1].equals("biraz geri al"))&&Double.parseDouble(keyarray[2])>0.7){
                anlamadım=false;
                t1.SırayaekleveOku(keyarray[1]+" mı demek istediniz");
            }



            if((keyarray[1].equals("geri al")||keyarray[1].equals("geriye alır mısın"))&&Double.parseDouble(keyarray[2])>0.8){
                anlamadım=false;
                if((sTime - 15000) > 0)
                {
                    sTime = sTime - 15000;
                    mPlayer.seekTo(sTime);
                    oynat();
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                    t1.SırayaekleveOku(((sTime)/1000)+"saniye kadar geri alabilirsiniz 15 saniye geri alamadım");
                }

            }else if((keyarray[1].equals("geri al")||keyarray[1].equals("geriye alır mısın"))&&Double.parseDouble(keyarray[2])>0.7){
                anlamadım=false;
                t1.SırayaekleveOku( keyarray[1]+" mı demek istediniz");
            }

        }







        if(anlamadım){
            t1.SırayaekleveOku("Ne demek istediğini anlamadım");
            oynat();
        }
        biönceki=keyarray[1];
    }





    @Override
    public boolean onTouchEvent(MotionEvent event) {//ekrana parmak basınca çalışıyor
        // System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!onTouchEvent...."+event);
        if(t1==null){
            t1 = new TexttoSpeechclass(this);
        }


        if(event.getAction()==MotionEvent.ACTION_UP){//parmağını ekrandan kaldırdığında

            k.getMSpeechRecognizer().cancel();
            data=k.getSpeechRecognitionListener().getData();
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!k.getSpeechRecognitionListener().getData()----------------"+data);
            if(data.size()>0) {
                // System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!hataveren yer"+data);
                String[][] basket = Didyoumean.CheckSimilarity(data, anahtarkelimeler);
                if (basket[0][2] != null) {//anahtar kelime ile eşleşme olursa kullansın yoksa null hatası veriyor
                    PrintDoubleArray(basket);
                    Anahtarkelimeyikullan(basket[0]);
                }
                //t1.SırayaekleveOku(data.get(0));

            }else{
                //System.out.println("kitapplayer!!!!!!!!!!!!oynuyormu"+oynuyormu);
                if(oynuyormu){
                    oynat();
                }
            }
        }if(event.getAction()==MotionEvent.ACTION_DOWN){//parmağını ekrana basınca

            //mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            // data=k.Kullanıcadanselalreturnlu();
            //t1.onPause();
            handler.removeCallbacks(run);
            oynuyormu=oynat.getVisibility()==View.VISIBLE;
            t1.onPause();
            Durdur();
            k=new Kullanicidansesal(this,t1);
        }


        return super.onTouchEvent(event);
    }



    public void PrintDoubleArray(String[][]array){

        for(int i=0;i<array.length;i++){
            if(array[i][0]==null){break;}
            else{
                System.out.println("kitapplayer!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!simularity------"+"i: "+i+"/()/ konuşma:   "+array[i][0]+"  key:  "+array[i][1] +"  Score: "+array[i][2]);
            }
        }
        //multiline_txt.setText(konusmalogkaydı);

    }

}