package com.cmpestudents.seslikitap;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Kullanicidansesal  {

    public SpeechRecognizer mSpeechRecognizer;
    public Intent mSpeechRecognizerIntent;
    private boolean mIslistening;
    SpeechRecognitionListener listener;
    Activity act;
    TexttoSpeechclass t1;

    public Kullanicidansesal(Activity act,TexttoSpeechclass t1) {

        //requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 2);
        this.act=act;
        this.t1=t1;

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(act);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                act.getPackageName());


        listener = new SpeechRecognitionListener();
        mSpeechRecognizer.setRecognitionListener(listener);

        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
    }

    public SpeechRecognizer getMSpeechRecognizer(){
        return mSpeechRecognizer;
    }
    public SpeechRecognitionListener getSpeechRecognitionListener(){
        return listener;
    }


    public void KullanıcıdansesalReset(){
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(act.getApplicationContext());
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                act.getApplication().getPackageName());
        listener = new SpeechRecognitionListener();
        mSpeechRecognizer.setRecognitionListener(listener);
    }

    public ArrayList<String> Kullanıcadanselalreturnlu(){

        if (!mIslistening) {
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        }
        if(listener.getData()==null){
            try {
                for(int i=0;i<1000;i++){
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1----"+i+"");
                    new Thread().sleep(1);
                }

            }catch (Exception e){

            }
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!kullanıcıdan ses al return olan data "+listener.getData());
        return listener.getData();
    }

    public void KullanıcıdanSesAlIptal(){
        if(mSpeechRecognizer != null){
        mSpeechRecognizer.cancel();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!kullanıcıdan ses al ipta if içi");
        }else {System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!kullanıcıdan ses al ipta if dışı");}

    }



/*
    public void onWindowFocusChanged(boolean hasFocus) {
        act.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            if (!mIslistening)
            {
                //mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            }
        }else{
            if (mSpeechRecognizer != null)
            {
                mSpeechRecognizer.destroy();
            }
        }

    }*/
    protected class SpeechRecognitionListener implements RecognitionListener
    {
        ArrayList<String> data=new ArrayList<>();

       public ArrayList<String> getData(){
           return data;
       }


        @Override
        public void onBeginningOfSpeech()
        {
            //Log.d(TAG, "onBeginingOfSpeech");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!onBeginningOfSpeech...");
        }

        @Override
        public void onBufferReceived(byte[] buffer)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!onBufferReceived..."+buffer);
        }

        @Override
        public void onEndOfSpeech()
        {
            //Log.d(TAG, "onEndOfSpeech");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!onEndOfSpeech...");
        }

        @Override
        public void onError(int error)
        {
            //mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!onError...."+error);
            if(error==8){
                Toast.makeText(act.getApplicationContext(),"Error8--ERROR_RECOGNIZER_BUSY",Toast.LENGTH_LONG);
            }else if(error==1){
                Toast.makeText(act.getApplicationContext(),"Error1--ERROR_NETWORK_TIMEOUT",Toast.LENGTH_LONG);
            }else if(error==2){
                Toast.makeText(act.getApplicationContext(),"Error2--ERROR_NETWORK",Toast.LENGTH_LONG);
                t1.SırayaekleveOku("Lütfen internet bağlantınızı kontrol ediniz.");
            }else if(error==3){
                Toast.makeText(act.getApplicationContext(),"Error3--ERROR_AUDIO",Toast.LENGTH_LONG);
            }else if(error==4){
                Toast.makeText(act.getApplicationContext(),"Error4--ERROR_SERVER",Toast.LENGTH_LONG);
                t1.SırayaekleveOku("Lütfen internet bağlantınızı kontrol ediniz.");
            }else if(error==5){
                Toast.makeText(act.getApplicationContext(),"Error5--ERROR_CLIENT",Toast.LENGTH_LONG);
            }else if(error==6){
                Toast.makeText(act.getApplicationContext(),"Error6--ERROR_SPEECH_TIMEOUT",Toast.LENGTH_LONG);
            }else if(error==7){
                Toast.makeText(act.getApplicationContext(),"Error7--ERROR_NO_MATCH",Toast.LENGTH_LONG);
                //t1.SırayaekleveOku("hata kodu yedi. Eşleşme olmadı.");
            }else if(error==9){
                Toast.makeText(act.getApplicationContext(),"Error9--ERROR_INSUFFICIENT_PERMISSIONS",Toast.LENGTH_LONG);
                t1.SırayaekleveOku("Lütfen ayarlardan engelsiz kitap için mikrofon izinlerini verin.");
            }
            //Log.d(TAG, "error = " + error);
            if (mSpeechRecognizer != null)
            {
                mSpeechRecognizer.destroy();
            }
            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(act.getApplicationContext());
            mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                    act.getApplication().getPackageName());
            SpeechRecognitionListener listener = new SpeechRecognitionListener();
            mSpeechRecognizer.setRecognitionListener(listener);
        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!onEvent...."+eventType+"  "+params);
        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!onPartialResults......."+partialResults);
        }

        @Override
        public void onReadyForSpeech(Bundle params)
        {
            // Log.d(Tag, "onReadyForSpeech"); //$NON-NLS-1$
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!onReadyForSpeechonReadyForSpeech...."+params);
        }

        @Override
        public void onResults(Bundle results)
        {
            //Log.d(TAG, "onResults"); //$NON-NLS-1$
            data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION); //result ı data arraylist e koyma
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!onResults....."+results);

            // matches are the return values of speech recognition engine
            // Use these values for whatever you wish to do
        }

        @Override
        public void onRmsChanged(float rmsdB)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!onRmsChanged...."+rmsdB);

           //if(rmsdB<0&&rmsdB!=-2.0)mSpeechRecognizer.stopListening();


        }
    }

    /*
    public Intent intent;
    public static final int request_code_voice = 1;
    public SpeechRecognizer recognizer;
    public String konusmalogkaydı="";
    public String isleniceksonkonusma="";
    public int konusmalogcounter=0;

    String [] Keyworddeneme={"deneme1","deneme2","deneme3"};
    String [] BasketforonActivityResult;



    public void kullanıcıdansesal( ){//String [] keyword){
       // BasketforonActivityResult=keyword;
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); // intent i oluşturduk sesi tanıyabilmesi için
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        try{
            startActivityForResult(intent, request_code_voice);  // activityi başlattık belirlediğimiz sabit değer ile birlikte
        }catch(ActivityNotFoundException e)
        {// activity bulunamadığı zaman hatayı göstermek için alert dialog kullandım
            e.printStackTrace();
          /*  AlertDialog.Builder builder = new AlertDialog.Builder(Homepageactivtiy.);
            builder.setMessage("Üzgünüz Telefonunuz bu sistemi desteklemiyor!!!")
                    .setTitle("Mobilhanem")
                    .setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case request_code_voice: {
                if (resultCode == RESULT_OK && data != null)
                {
                    // intent boş olmadığında ve sonuç tamam olduğu anda konuşmayı alıp listenin içine attık
                    ArrayList<String> speech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String gecicitext = speech.get(0);
                    isleniceksonkonusma=gecicitext;
                    //konusmalogkaydı+="\n"+konusmalogcounter+++"_"+gecicitext;
                    //PrintDoubleArray(Didyoumean.CheckSimilarity(isleniceksonkonusma,BasketforonActivityResult));

                }
                break;
            }
        }
    }

    public String Returnlukullanıcıdansesal(){
        //kullanıcıdansesal();
        return isleniceksonkonusma;
    }

    public void PrintDoubleArray(String[][]array){

        for(int i=0;i<array.length;i++){
            if(array[i][0]==null){break;}
            else{
                konusmalogkaydı+=konusmalogcounter+++array[i][0]+"---->"+array[i][1]+"\n";
            }
        }
        //multiline_txt.setText(konusmalogkaydı);
        konusmalogkaydı="";
    }
*/
}
