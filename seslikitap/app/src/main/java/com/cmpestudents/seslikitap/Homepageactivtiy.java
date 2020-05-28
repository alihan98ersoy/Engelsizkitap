package com.cmpestudents.seslikitap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinodev.androidneomorphframelayout.NeomorphFrameLayout;

import java.util.ArrayList;

public class Homepageactivtiy extends AppCompatActivity {


    public SpeechRecognizer recognizer;

    //kullanıcıdansesal( ) kısmı için
    public Intent intent;
    public static final int request_code_voice = 1;
    public String konusmalogkaydı="";
    public String isleniceksonkonusma="";
    public int konusmalogcounter=0;
    Kullanicidansesal k;
    ArrayList<String> data=new ArrayList<>();
    //String [] anahtarkelimeler={"Admin paneli"};
    ArrayList<String> anahtarkelimeler=new ArrayList<>();
    ArrayList<String> yazarkitapliste=new ArrayList<>();
    ArrayList<String> kitapisimleri=new ArrayList<>();
    ArrayList<String> yazarisimleri=new ArrayList<>();


    //TexttoSpeech için
    TexttoSpeechclass t1;

    //Bunu mu demek istedin bi önce ki data
    String biönceki="";

    //SQL kısmı için
    SQLdatabase database;

    //kitapplayer ilk sefer
    public static int ilkseferkitap=0;
    ArrayList<String>açılmışkitaplar=new ArrayList<>();

TextView mainTxt;
NeomorphFrameLayout layout_logo, layout_logo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepageactivtiy);

        //text to speech initialize
        if(t1==null){
        t1 = new TexttoSpeechclass(this);}

        mainTxt = (TextView)findViewById(R.id.mainTxt);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Orchide.ttf");
        mainTxt.setTypeface(typeface);

        layout_logo = (NeomorphFrameLayout)findViewById(R.id.layout_logo);
        layout_logo.setVisibility(View.VISIBLE);
        layout_logo2 = (NeomorphFrameLayout)findViewById(R.id.layout_logo2);
        layout_logo2.setVisibility(View.INVISIBLE);

        //deneme int değer ile media oynatmak için
        // int deneme=getResources().getIdentifier("R.id.button2","id",getPackageName());
        //adminbutton = findViewById(deneme);


        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.INTERNET
                 };

        checkPermission(PERMISSIONS,1);


        //sql initialization  etmek için

        SQLKullan(getString(R.string.Databasename));//String variable dan çekiyor
        database.setContext(getApplicationContext());
        database.initialization();

        //anahtar kelime tanımlama
        anahtarkelimeler.add("admin paneli");
        anahtarkelimeler.add("evet");
        anahtarkelimeler.add("yardım");
        anahtarkelimeler.add("kitaplar");
        anahtarkelimeler.add("yazarlar");
        anahtarkelimeler.add("hakkında");
        Anahtarkelimeekle();






    }//onCreate sonu

        //Anahtar kelime oluşturuyor
        public void Anahtarkelimeekle() {
            String[] basket= new String[4];
            for(int i=0;i<SQLdatabase.kitaplarliste.size();i++){
                kitapisimleri.add(SQLdatabase.kitaplarliste.get(i).getKitapismi());
                basket=KitapiddenBulma(SQLdatabase.kitaplarliste.get(i).getKitapismi());
                if(!basket[1].equals("null")||basket[2]!=null){
                anahtarkelimeler.add(basket[1]+"   "+basket[2]);//yazar ismi + kitap okunuşu
                anahtarkelimeler.add(basket[1]);//yazar ismi ekle anahtar kelimelere
                anahtarkelimeler.add(basket[2]);//kitap ismi ekle anahtar kelimelere
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!anahtarkelimeeklem:  "+basket[1]+"--"+basket[2]);
                yazarkitapliste.add(basket[1]+"   "+basket[2]);//anahtar kelime kullanda kullancaz
                yazarisimleri.add(basket[1]);
                kitapisimleri.add(basket[2]);}
            }
            /*
            anahtarkelimeler.add(z+"@@"+SQLdatabase.yazarlarliste.get(i).getYazarismi()+" "+SQLdatabase.seslerlister.get(z).getSüresi());//yazar ismi + kitap okunuşu
            yazarkitapliste.add(z+"@@"+SQLdatabase.yazarlarliste.get(i).getYazarismi()+" "+SQLdatabase.seslerlister.get(z).getSüresi());//anahtar kelime kullanda kullancaz

            anahtarkelimeler.add(z+"@@"+SQLdatabase.seslerlister.get(z).getSüresi()+" "+SQLdatabase.yazarlarliste.get(i).getYazarismi());//kitap okunuşu + yazar ismi
            yazarkitapliste.add(z+"@@"+SQLdatabase.seslerlister.get(z).getSüresi()+" "+SQLdatabase.yazarlarliste.get(i).getYazarismi());//anahtar kelime kullanda kullancaz*/
        }

        public void checkPermission(String[] permission, int requestCode)
        {
            // Checking if permission is not granted
            if (ContextCompat.checkSelfPermission(Homepageactivtiy.this, permission[0]) == PackageManager.PERMISSION_DENIED||ContextCompat.checkSelfPermission(Homepageactivtiy.this, permission[1]) == PackageManager.PERMISSION_DENIED
            ||ContextCompat.checkSelfPermission(Homepageactivtiy.this, permission[2]) == PackageManager.PERMISSION_DENIED||ContextCompat.checkSelfPermission(Homepageactivtiy.this, permission[3]) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(Homepageactivtiy.this,permission,requestCode);
            }
            else {
                //Toast.makeText(Homepageactivtiy.this,"Permission already granted", Toast.LENGTH_SHORT).show();
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!permission granted"+permission);
            }
    }


    //didyoumean den sonra gelen en yüksek key i kullanma
    public void Anahtarkelimeyikullan(String[] keyarray){
        System.out.println("!!!!!!!!!!!!!!!anahtar kelime kullan"+keyarray[0]+"---"+keyarray[1]+"---"+keyarray[2]);
        boolean anlamadım=true;
        if(keyarray[1].equals("admin paneli")&&Double.parseDouble(keyarray[2])>0.984){
            Intent adminintent = new Intent(getApplicationContext(), AdminPanel.class);
            startActivity(adminintent);
            anlamadım=false;
        }else if(keyarray[1].equals("evet")&&Double.parseDouble(keyarray[2])>0.95){
            keyarray[1]=biönceki;
            anlamadım=false;
        }else if(keyarray[1].equals("yardım")&&Double.parseDouble(keyarray[2])>0.90){
                t1.SırayaekleveOku(getString(R.string.yardımhomepage));
             anlamadım=false;
        }else if(keyarray[1].equals("kitaplar")&&Double.parseDouble(keyarray[2])>0.9){
            anlamadım=false;
            t1.SırayaekleveOku("Kitaplar:");
            for(int i=0;i<SQLdatabase.kitaplarliste.size();i++){
                String[] basket= KitapiddenBulma(SQLdatabase.kitaplarliste.get(i).getKitapismi());
                t1.SırayaekleveOku(basket[2]);//okunuşu
            }
        }else if(keyarray[1].equals("yazarlar")&&Double.parseDouble(keyarray[2])>0.9){
            anlamadım=false;
            t1.SırayaekleveOku("Yazarlar:");
            for(int i=0;i<SQLdatabase.yazarlarliste.size();i++){
                t1.SırayaekleveOku(SQLdatabase.yazarlarliste.get(i).getYazarismi());
            }
        }else if(keyarray[1].equals("hakkında")&&Double.parseDouble(keyarray[2])>0.9){
            anlamadım=false;
            t1.SırayaekleveOku(getString(R.string.hakkında));
        }




        if(Listedevarmı(kitapisimleri,keyarray[1])){
            if(Double.parseDouble(keyarray[2])>0.9){
                Kitapokumaactivty(Okunusdanisimbulma(keyarray[1]));
                anlamadım=false;
            }else if(Double.parseDouble(keyarray[2])>0.75){
                String [] basket=new String[4];
                basket= KitapiddenBulma(Okunusdanisimbulma(keyarray[1]));
                System.out.println("!!!!!!!!!!!!!a"+basket[1]+" "+basket[2]+"Kitabınımı kastetdiniz");
                t1.SırayaekleveOku(basket[1]+" "+basket[2]+"Kitabınımı kastetdiniz");
                anlamadım=false;
            }
        }

        if(Listedevarmı(yazarisimleri,keyarray[1])){
            if(Double.parseDouble(keyarray[2])>0.9){
                anlamadım=false;
                int yazarid=-1;
                for(int i=0;i<SQLdatabase.yazarlarliste.size();i++){
                    if(SQLdatabase.yazarlarliste.get(i).getYazarismi().toLowerCase().equals(keyarray[1].toLowerCase())){
                        yazarid=SQLdatabase.yazarlarliste.get(i).getYazarid();
                    }
                }
                if(yazarid!=-1){
                    t1.SırayaekleveOku(keyarray[1]+" kitapları");
                for(int i=0;i<SQLdatabase.kitaplarliste.size();i++){
                    if(SQLdatabase.kitaplarliste.get(i).getYazarid()==yazarid){
                        t1.SırayaekleveOku(SQLdatabase.kitaplarliste.get(i).getKitapismi());
                    }
                }
                }
            }else if(Double.parseDouble(keyarray[2])>0.75){
                anlamadım=false;
                t1.SırayaekleveOku(keyarray[1]+" yazarının kitaplarını mı duymak istediniz");
            }
        }

         if(Listedevarmı(yazarkitapliste,keyarray[1])){
            if(Double.parseDouble(keyarray[2])>0.9){
                anlamadım=false;
                String[]basket=keyarray[1].split("   ");
                Kitapokumaactivty(Okunusdanisimbulma(basket[1]));


            }else if(Double.parseDouble(keyarray[2])>0.75) {
                anlamadım=false;
                t1.SırayaekleveOku(keyarray[1]+" mi demek istedin");
            }

        }

        if(anlamadım){
            t1.SırayaekleveOku("Ne demek istediğini anlamadım");
        }
        biönceki=keyarray[1];
    }


    //Kitap okumayı çalıştır
    public void Kitapokumaactivty(String ismi){
        boolean var=false;
        for(int i=0;i<açılmışkitaplar.size();i++){
            if(ismi.equals(açılmışkitaplar.get(i))){
                var=true; break;
            }
        }
        if(!var){
            ilkseferkitap=0;
            açılmışkitaplar.add(ismi);
        }

        t1.onPause();
        Kitapplayer k=new Kitapplayer();
        Intent adminintent = new Intent(getApplicationContext(),k.getClass());
        adminintent.putExtra("ismi",ismi);
        startActivity(adminintent);

    }

    //Kitap isimden yazar ismi kitap okunuşu ve ses id bulma kitap id
    public String [] KitapiddenBulma(String isim){
        String [] basket = new  String[4];
        for(int j=0;j<SQLdatabase.kitaplarliste.size();j++){
            if(SQLdatabase.kitaplarliste.get(j).getKitapismi().toLowerCase().equals(isim.toLowerCase())){
        for (int i = 0; i < SQLdatabase.yazarlarliste.size(); i++) {
            if (SQLdatabase.yazarlarliste.get(i).getYazarid() == SQLdatabase.kitaplarliste.get(j).getYazarid()) {//yazarın yazdığı kitabı bulursa ses tablosundan kitabın okunuşuna bakıcak
                for (int z = 0; z < SQLdatabase.seslerlister.size(); z++) {
                    if (SQLdatabase.kitaplarliste.get(j).getSesid() == SQLdatabase.seslerlister.get(z).getSesid()) {
                        basket[0] = SQLdatabase.kitaplarliste.get(j).getKitapismi();
                        basket[1] = SQLdatabase.yazarlarliste.get(i).getYazarismi();
                        basket[2] = SQLdatabase.seslerlister.get(z).getSüresi();
                        basket[3] = z + "";
                        return basket;
                    }
                }
            }
          }}//end of if
        }
        return basket;
    }

    //Okunuşundan kitapismi bulma
    public String Okunusdanisimbulma(String okun){
        for(int i=0;i<SQLdatabase.seslerlister.size();i++){
            if(SQLdatabase.seslerlister.get(i).getSüresi().toLowerCase().equals(okun.toLowerCase())){
                for(int j=0;j<SQLdatabase.kitaplarliste.size();j++){
                    if(SQLdatabase.kitaplarliste.get(j).getSesid()==SQLdatabase.seslerlister.get(i).getSesid()){
                        return SQLdatabase.kitaplarliste.get(j).getKitapismi();
                    }
                }
            }
        }
        return "null";
    }


    //listeden bul
    public boolean Listedevarmı(ArrayList<String> liste,String metin){
        for(String item : liste){
            if(item.toLowerCase().equals(metin.toLowerCase())){
                return true;
            }
        }

        return false;
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {//ekrana parmak basınca çalışıyor
        // System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!onTouchEvent...."+event);
        if(t1==null){
            t1 = new TexttoSpeechclass(this);
            layout_logo = (NeomorphFrameLayout)findViewById(R.id.layout_logo);
            layout_logo.setVisibility(View.INVISIBLE);
            layout_logo2 = (NeomorphFrameLayout)findViewById(R.id.layout_logo2);
            layout_logo2.setVisibility(View.VISIBLE);
        }


        if(event.getAction()==MotionEvent.ACTION_UP){//parmağını ekrandan kaldırdığında

                layout_logo = (NeomorphFrameLayout)findViewById(R.id.layout_logo);
                layout_logo.setVisibility(View.VISIBLE);
                layout_logo2 = (NeomorphFrameLayout)findViewById(R.id.layout_logo2);
                layout_logo2.setVisibility(View.INVISIBLE);

                k.getMSpeechRecognizer().cancel();
                data=k.getSpeechRecognitionListener().getData();
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!k.getSpeechRecognitionListener().getData()----------------"+data);
            if(data.size()>0) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!hataveren yer"+data);
                String[][] basket = Didyoumean.CheckSimilarity(data, anahtarkelimeler);
                if (basket[0][2] != null) {//anahtar kelime ile eşleşme olursa kullansın yoksa null hatası veriyor
                    PrintDoubleArray(basket);
                    Anahtarkelimeyikullan(basket[0]);
                }
                //t1.SırayaekleveOku(data.get(0));

            }
        }if(event.getAction()==MotionEvent.ACTION_DOWN){//parmağını ekrana basınca

               //mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
               // data=k.Kullanıcadanselalreturnlu();
                //t1.onPause();

                layout_logo = (NeomorphFrameLayout)findViewById(R.id.layout_logo);
                layout_logo.setVisibility(View.INVISIBLE);
                layout_logo2 = (NeomorphFrameLayout)findViewById(R.id.layout_logo2);
                layout_logo2.setVisibility(View.VISIBLE);

                t1.onPause();
                k=new Kullanicidansesal(this,t1);
        }


        return super.onTouchEvent(event);
    }



    //SQL KISMI
    public void SQLKullan(String databaseismi){
        database=new SQLdatabase(getBaseContext(),databaseismi+".db", null,1);
    }






/*
    public void KullanıcıdansesalReset(){
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                getApplication().getPackageName());
        Homepageactivtiy.SpeechRecognitionListener listener = new Homepageactivtiy.SpeechRecognitionListener();
        mSpeechRecognizer.setRecognitionListener(listener);
    }


    //Kullanıcıdan ses alma kısmı
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







    @Override
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
                    konusmalogkaydı+="\n"+konusmalogcounter+++"_"+gecicitext;
                    //PrintDoubleArray(Didyoumean.CheckSimilarity(isleniceksonkonusma,BasketforonActivityResult));

                }
                break;
            }
        }
    }

    public String Returnlukullanıcıdansesal(){
        kullanıcıdansesal();
        textView.setText(isleniceksonkonusma);
        return isleniceksonkonusma;

    }

    protected class SpeechRecognitionListener implements RecognitionListener
    {

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
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!onError...."+error);
            if(error==8){
                Toast.makeText(getApplicationContext(),"Error8--ERROR_RECOGNIZER_BUSY",Toast.LENGTH_LONG);
            }else if(error==1){
                Toast.makeText(getApplicationContext(),"Error1--ERROR_NETWORK_TIMEOUT",Toast.LENGTH_LONG);
            }else if(error==2){
                Toast.makeText(getApplicationContext(),"Error2--ERROR_NETWORK",Toast.LENGTH_LONG);
            }else if(error==3){
                Toast.makeText(getApplicationContext(),"Error3--ERROR_AUDIO",Toast.LENGTH_LONG);
            }else if(error==4){
                Toast.makeText(getApplicationContext(),"Error4--ERROR_SERVER",Toast.LENGTH_LONG);
            }else if(error==5){
                Toast.makeText(getApplicationContext(),"Error5--ERROR_CLIENT",Toast.LENGTH_LONG);
            }else if(error==6){
                Toast.makeText(getApplicationContext(),"Error6--ERROR_SPEECH_TIMEOUT",Toast.LENGTH_LONG);
            }else if(error==7){
                Toast.makeText(getApplicationContext(),"Error7--ERROR_NO_MATCH",Toast.LENGTH_LONG);
            }else if(error==9){
                Toast.makeText(getApplicationContext(),"Error9--ERROR_INSUFFICIENT_PERMISSIONS",Toast.LENGTH_LONG);
            }
            //Log.d(TAG, "error = " + error);
            if (mSpeechRecognizer != null)
            {
                mSpeechRecognizer.destroy();
            }
            KullanıcıdansesalReset();
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
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!onResults....."+results);
            // matches are the return values of speech recognition engine
            // Use these values for whatever you wish to do
        }

        @Override
        public void onRmsChanged(float rmsdB)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!onRmsChanged...."+rmsdB);
        }
    }


*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
            t1.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        t1.onPause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        t1.onPause();
    }


    public void PrintDoubleArray(String[][]array){

        for(int i=0;i<array.length;i++){
            if(array[i][0]==null){break;}
            else{
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!simularity------"+"i: "+i+"/()/ konuşma:   "+array[i][0]+"  key:  "+array[i][1] +"  Score: "+array[i][2]);
            }
        }
        //multiline_txt.setText(konusmalogkaydı);

    }

}
