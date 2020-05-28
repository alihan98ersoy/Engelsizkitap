package com.cmpestudents.seslikitap;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

public class Adminpanelduzenle extends AppCompatActivity {

    //Spinner içerisine koyacağımız verileri tanımlıyoruz.
    private String[] tables={"Tablolardan birini şeç","Kitap","Yazar","Ses"};
    //Spinner ve Adapterını tanımlıyoruz.
    private Spinner spinner;
    ListView listview;
    static ArrayAdapter<String> dataadaptertables;
    static ArrayAdapter listviewarrayadapter;




    ImageView imageview;
    Button button;
    EditText editext1;
    EditText editext2;
    EditText editext3;
    EditText editext4;
    EditText editext5;
    TextView id;
    Switch switch1;


    static int cameragallery=0;
    int cameracheck=0;
    Bitmap bitmap;
    static Boolean resmisil=false;
    Pop pop = new Pop();

    int ilkgüncelleme=1;//en altta hepsini sil kısmı il güncellemede boş yapıyor.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpanelduzenle);
//spinner kısmı
        //xml kısmında hazırladığımğız spinnerı burda tanımladığımızla eşleştiriyoruz.
        spinner = (Spinner) findViewById(R.id.spinner);


        //Spinner için adapter hazırlıyoruz.
        dataadaptertables = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tables);


        //Listelenecek verilerin görünümünü belirliyoruz.
        dataadaptertables.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //Hazırladğımız Adapter'leri Spinner'a ekliyoruz.
        spinner.setAdapter(dataadaptertables);


        // Listeden bir eleman seçildiğinde yapılacakları tanımlıyoruz.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if(parent.getSelectedItem().toString().equals(tables[0])){//Boş
                    Hepsigörünmez();
                }else if(parent.getSelectedItem().toString().equals(tables[1])){//Kitaplar
                    Hepsigörünmez();
                    button.setVisibility(View.VISIBLE);
                    imageview.setVisibility(View.VISIBLE);
                    editext1.setVisibility(View.VISIBLE);
                    editext2.setVisibility(View.VISIBLE);
                    editext3.setVisibility(View.VISIBLE);
                    editext4.setVisibility(View.VISIBLE);
                    editext5.setVisibility(View.VISIBLE);
                    switch1.setVisibility(View.VISIBLE);

                    editext1.setHint(R.string.title1hintkitaplar);
                    editext2.setHint(R.string.title2hintkitaplar);
                    editext3.setHint(R.string.title3hintkitaplar);
                    editext4.setHint(R.string.title4hintkitaplar);
                    editext5.setHint(R.string.title5hintkitaplar);
                    switch1.setText(R.string.Switchkitaplar);
                }else if(parent.getSelectedItem().toString().equals(tables[2])){//Yazarlar
                    Hepsigörünmez();
                    button.setVisibility(View.VISIBLE);
                    editext1.setVisibility(View.VISIBLE);
                    editext2.setVisibility(View.VISIBLE);
                    editext3.setVisibility(View.VISIBLE);

                    editext1.setHint(R.string.title1hintyazarlar);
                    editext2.setHint(R.string.title2hintyazarlar);
                    editext3.setHint(R.string.title3hintyazarlar);
                }else if(parent.getSelectedItem().toString().equals(tables[3])){//Sesler
                    Hepsigörünmez();
                    button.setVisibility(View.VISIBLE);
                    editext1.setVisibility(View.VISIBLE);
                    editext2.setVisibility(View.VISIBLE);
                    editext3.setVisibility(View.VISIBLE);
                    editext4.setVisibility(View.VISIBLE);
                    switch1.setVisibility(View.VISIBLE);


                    editext1.setHint(R.string.title1hintsesler);
                    editext2.setHint(R.string.title2hintsesler);
                    editext3.setHint(R.string.title3hintsesler);
                    editext4.setHint(R.string.title4hintsesler);
                    switch1.setText(R.string.titleswitchhintsesler);

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        button = (Button) findViewById(R.id.button);
        imageview = (ImageView) findViewById(R.id.imageView2);
        editext1 = (EditText) findViewById(R.id.editTexttitle);

        editext1.setFocusable(false);
        editext1.setFocusableInTouchMode(false);
        editext1.setClickable(false);

        editext2 = (EditText) findViewById(R.id.editTexttitle2);
        editext3 = (EditText) findViewById(R.id.editTexttitle3);
        editext4 = (EditText) findViewById(R.id.editTexttitle4);
        editext5 = (EditText) findViewById(R.id.editTexttitle5);
        id = (TextView) findViewById(R.id.iddata);
        switch1 = (Switch) findViewById(R.id.switch1);

        Intent intent = getIntent();
        String iddata = intent.getStringExtra("id");
        String tabledata = intent.getStringExtra("table");
        String yeni = intent.getStringExtra("yeni");
        //System.out.println("********************************"+iddata);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+iddata+" hebelehübele "+tabledata);
        if(tabledata==null||tabledata.equals("")||tabledata.equals("0")||yeni.equals("yeni")){//Yeni data
            id.setText(R.string.Nullid);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!yeni?????"+yeni);
            if(tabledata.equals("1")){spinner.setSelection(1);
            }else if(tabledata.equals("2")){spinner.setSelection(2);
            }else if(tabledata.equals("3")){spinner.setSelection(3);
            }else{spinner.setSelection(0);}



            button.setText(R.string.Buttonekle);
        }else{//Data güncelleme kısmı
            button.setText(R.string.Buttondegistir);
            ilkgüncelleme=0;
            if(tabledata.equals("1")){//kitaplar
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+"Table admin panel duzeltme");
                spinner.setSelection(1);
                editext1.setText(iddata);
                Kitaplar kitap = (Kitaplar) SQLdatabase.LisetdenObjectal(SQLdatabase.kitaplarliste,iddata);
                editext2.setText(kitap.getKitapismi());
                editext3.setText(kitap.getYazarid()+"");
                editext4.setText(kitap.getIsimsesid()+"");
                editext5.setText(kitap.getSesid()+"");
                if(kitap.getKapakresmi()==null){
                }else{//kapak resmi var
                    imageview.setImageBitmap(kitap.getKapakresmi());
                }
            }else if(tabledata.equals("2")){//yazarlar
                spinner.setSelection(2);
                editext1.setText(iddata);
                Yazarlar yazar = (Yazarlar) SQLdatabase.LisetdenObjectal(SQLdatabase.yazarlarliste,iddata);
                editext2.setText(yazar.getYazarismi());
                editext3.setText(yazar.getSesid()+"");
            }else if(tabledata.equals("3")){//sesler
                spinner.setSelection(3);
                editext1.setText(iddata);
                Sesler ses = (Sesler) SQLdatabase.LisetdenObjectal(SQLdatabase.seslerlister,iddata);
                editext2.setText(ses.getSesuzantisi());
                editext3.setText(ses.getSüresi()+"");
                editext4.setText(ses.getKaldigidakika()+"");
                if(ses.getUrlmi()){
                    switch1.setChecked(true);
                }

            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String table = spinner.getSelectedItem().toString();

                if (editext1.getText().toString().equals("")) {//Insert into
                  // MainActivity.Insert(new SQLcakma(-1, title.getText().toString(), "yok", other.getText().toString(), bitmap));
                    if(table.equals("Kitap")){
                        editext3.setText(((editext3).equals("") ? editext3.getText().toString() : "-1"));
                        editext4.setText(((editext4).equals("") ? editext4.getText().toString() : "-1"));
                        editext5.setText(((editext5).equals("") ? editext5.getText().toString() : "-1"));
                        SQLdatabase.Inserttable(table,new Kitaplar(-1,editext2.getText().toString(),Integer.parseInt(editext3.getText().toString()),
                                Integer.parseInt(editext4.getText().toString()),Integer.parseInt(editext5.getText().toString()),bitmap));
                    }else if(table.equals("Yazar")){
                        editext3.setText(((editext3).equals("") ? editext3.getText().toString() : "-1"));
                        SQLdatabase.Inserttable(table,new Yazarlar(-1,editext2.getText().toString(),Integer.parseInt(editext3.getText().toString())));

                    }else if(table.equals("Ses")){

                        Sesler ses =new Sesler(-1,editext2.getText().toString(),switch1.isChecked(),editext3.getText().toString(),
                                editext4.getText().toString());
                        //SQLdatabase.seslerlister.add(ses);
                        SQLdatabase.Inserttable(table,ses);

                    }


                } else {//Update
                   //MainActivity.UPDATE(new SQLcakma(Integer.parseInt(id.getText().toString()), title.getText().toString(), "yok", other.getText().toString(), bitmap),resmisil);

                    if(table.equals("Kitap")){
                       // editext3.setText(((editext3).equals("") ? editext3.getText().toString() : "-1"));
                        //editext4.setText(((editext4).equals("") ? editext4.getText().toString() : "-1"));
                        //editext5.setText(((editext5).equals("") ? editext5.getText().toString() : "-1"));
                        SQLdatabase.UPDATE(table,new Kitaplar(Integer.parseInt(editext1.getText().toString()),editext2.getText().toString(),Integer.parseInt(editext3.getText().toString()),
                                Integer.parseInt(editext4.getText().toString()),Integer.parseInt(editext5.getText().toString()),bitmap),editext1.getText().toString());
                    }else if(table.equals("Yazar")){
                        editext3.setText(((editext3).equals("") ? editext3.getText().toString() : "-1"));
                        SQLdatabase.UPDATE(table,new Yazarlar(Integer.parseInt(editext1.getText().toString()),editext2.getText().toString(),Integer.parseInt(editext3.getText().toString())),editext1.getText().toString());

                    }else if(table.equals("Ses")){

                        Sesler ses =new Sesler(Integer.parseInt(editext1.getText().toString()),editext2.getText().toString(),switch1.isChecked(),editext3.getText().toString(),
                                editext4.getText().toString());
                        //SQLdatabase.seslerlister.add(ses);
                        SQLdatabase.UPDATE(table,ses,editext1.getText().toString());

                    }



                }

                SQLdatabase.ListelerGüncelle();
                AdminPanel.listviewarrayadapter.notifyDataSetChanged();
                switch1.setChecked(false);
                    finish();

            }
        });







        //resme tıklandığında resim ekleme büyütme
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {
                    if (switch1.isChecked()) {
                        if (cameragallery == 0) {
                            // Intent intent2 = new Intent(getApplicationContext(), Pop.class);
                            Intent intent2 = new Intent(getApplicationContext(), pop.getClass());
                            startActivity(intent2);
                        }
                        if (cameragallery == 1) {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);
                            cameragallery = 0;
                        } else if (cameragallery == 2) {
                            Intent pickPhoto = new Intent(android.content.Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, 1);
                            cameragallery = 0;
                        } else if (cameragallery == 3) {
                            cameragallery = 0;
                        }
                        if (cameragallery != 0) {
                        }
                    } else {
                        try{
                            // VectorDrawable vb = (VectorDrawable) imageview.getDrawable();
                            BitmapDrawable drawable = (BitmapDrawable) imageview.getDrawable();
                            // Bitmap bitmap = Bitmap.createBitmap(vb.getIntrinsicWidth(),vb.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
                            Bitmap bitmap = drawable.getBitmap();
                            ByteArrayOutputStream bs = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG,50,bs);
                            Intent intent = new Intent(getApplicationContext(), pop.getClass());
                            intent.putExtra("bytearray",bs.toByteArray());
                            startActivity(intent);
                        }catch (Exception e){
                            switch1.setChecked(true);
                            Snackbar.make(v, "Resim eklemeden resmi görüntüleyemezsiniz...", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }
            }
        });
        imageview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                imageview.setImageBitmap(null);
                resmisil=true;
                return false;
            }
        });
    }//oncreta sonu

    //image view listener için
    protected void onActivityResult(int requestCode, final int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Bundle selectedImage = intent.getExtras();
                    try {
                        bitmap = (Bitmap) selectedImage.get("data");
                        imageview.setImageBitmap(bitmap);
                    }catch (Exception e){
                    }
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = intent.getData();
                    try {
                        bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                        imageview.setImageBitmap(bitmap);
                    }catch (Exception e){
                    }
                }
                break;
        }
    }

    public void Hepsigörünmez(){
        button.setVisibility(View.INVISIBLE);
        imageview .setVisibility(View.INVISIBLE);
        editext1.setVisibility(View.INVISIBLE);
        editext2.setVisibility(View.INVISIBLE);
        editext3.setVisibility(View.INVISIBLE);
        editext4.setVisibility(View.INVISIBLE);
        editext5.setVisibility(View.INVISIBLE);
        switch1.setVisibility(View.INVISIBLE);

        //hintleri silme
        editext1.setHint("");
        editext2.setHint("");
        editext3.setHint("");
        editext4.setHint("");
        editext5.setHint("");
        //switch1.setChecked(false);

        if(ilkgüncelleme==1){
        button.setText(R.string.Buttonekle);
        editext1.setText("");
        editext2.setText("");
        editext3.setText("");
        editext4.setText("");
        editext5.setText("");
        }
        ilkgüncelleme=1;

    }


}
