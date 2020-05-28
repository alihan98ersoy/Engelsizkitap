package com.cmpestudents.seslikitap;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AdminPanel extends AppCompatActivity {

    //Spinner içerisine koyacağımız verileri tanımlıyoruz.
    private String[] tables={"Tablolardan birini şeç","Kitap","Yazar","Ses"};

    //Spinner ve Adapterını tanımlıyoruz.
    private Spinner spinner;
    ListView listview;

    static ArrayAdapter<String> dataadaptertables;
    static ArrayAdapter listviewarrayadapter;



    @Override // Bu metod uygulama açıldığında çalıştırılan metod.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SQLdatabase.ListelerGüncelle();
        listview = (ListView) findViewById(R.id.listview);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent adminpanelduzenle = new Intent(getApplicationContext(),Adminpanelduzenle.class);
                // System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+sifreler.get(position).id+"!!!"+position);
                //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+parent.getItemAtPosition(position).toString());
                if(Listedenidalma(parent.getItemAtPosition(position).toString()).equals("-1")){
                    Toast.makeText(getApplicationContext(),"-1 geldi hatalı", Toast.LENGTH_SHORT).show();
                }else{
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!spinnerposition"+spinner.getSelectedItemPosition());

                adminpanelduzenle.putExtra("id",Listedenidalma(parent.getItemAtPosition(position).toString()));
                adminpanelduzenle.putExtra("table",spinner.getSelectedItemPosition()+"");
                adminpanelduzenle.putExtra("yeni","eski");
                startActivity(adminpanelduzenle);}
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if(Listedenidalma(parent.getItemAtPosition(position).toString()).equals("-1")){
                    Toast.makeText(getApplicationContext(),"-1 geldi hatalı", Toast.LENGTH_SHORT).show();
                }else{
                    String tablename="";
                    if(spinner.getSelectedItemPosition()==3){
                        tablename="Ses";
                    }else if(spinner.getSelectedItemPosition()==1){
                        tablename="Kitap";
                    }else if(spinner.getSelectedItemPosition()==2){
                        tablename="Yazar";
                    }
                    SQLdatabase.DELETE(tablename,Integer.parseInt(Listedenidalma(parent.getItemAtPosition(position).toString())));
                }


                return false;
            }
        });



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
                    listviewarrayadapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,new ArrayList());
                    listview.setAdapter(listviewarrayadapter);
                }else if(parent.getSelectedItem().toString().equals(tables[1])){//Kitaplar
                    listviewarrayadapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,SQLdatabase.kitaplarliste);
                    listview.setAdapter(listviewarrayadapter);
                }else if(parent.getSelectedItem().toString().equals(tables[2])){//Yazar
                    listviewarrayadapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,SQLdatabase.yazarlarliste);
                    listview.setAdapter(listviewarrayadapter);
                }else if(parent.getSelectedItem().toString().equals(tables[3])){//Ses
                    listviewarrayadapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,SQLdatabase.seslerlister);
                    listview.setAdapter(listviewarrayadapter);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(),Adminpanelduzenle.class);
                intent.putExtra("table",spinner.getSelectedItemPosition()+"");
                intent.putExtra("yeni","yeni");
                startActivity(intent);

            }
        });
    }//oncreat sonu



    //Listview selected itemin id sini çekme
    public String Listedenidalma(String text){

        String basket="";
        for(int i=0;i<text.length();i++){
            if(text.charAt(i)=='/'){
                if(text.charAt(i+1)=='/'){
                    return basket;
                }
            }else{
                basket=basket+text.charAt(i);
            }
        }
        return "-1";
    }
}




