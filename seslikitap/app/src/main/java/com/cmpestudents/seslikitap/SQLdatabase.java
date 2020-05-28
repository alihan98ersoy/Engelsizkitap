package com.cmpestudents.seslikitap;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;


public class SQLdatabase extends SQLiteOpenHelper {

    Connection con; //uzak bağlantı için
    String username,password,database,ip;//uzak bağlantı için


    //Arraylistler
    static ArrayList<Kitaplar>kitaplarliste;
    static ArrayList<Yazarlar>yazarlarliste;
    static ArrayList<Sesler>seslerlister;
    Context context;

    static SQLiteDatabase sqllite;

    public void setContext(Context context){
        this.context=context;
    }


    public void initialization(){
        try {
            sqllite = getWritableDatabase();

            sqllite.execSQL("create TABLE IF not EXISTS `Kitap` (\n" +
                    "\t`Kitapid` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`Kitapismi` VARCHAR(50) DEFAULT '',\n" +
                    "\t`Yazarid` INT DEFAULT '-1',\n" +
                    "\t`Isimsesid` INT DEFAULT '-1',\n" +
                    "\t`Sesid` INT DEFAULT '-1',\n" +
                    "`Kapakresmi` BLOB)");

            sqllite.execSQL("create TABLE IF not EXISTS `Yazar` (\n" +
                    "\t`Yazarid` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`Yazarismi` VARCHAR(50) DEFAULT '',\n" +
                    "\t`Sesid` INT DEFAULT '-1')");

            sqllite.execSQL("create TABLE IF not EXISTS `Ses` (\n" +
                    "\t`Sesid` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t`Sesuzantisi` VARCHAR(150) DEFAULT '',\n" +
                    "\t`Url` BOOLEAN,\n" +
                    "\t`Süresi` VARCHAR(50) DEFAULT '',\n" +
                    "\t`Kaldigidakika` VARCHAR(10) DEFAULT '0')");



            ListelerGüncelle();
            if(kitaplarliste.size()==0){

                sqllite.execSQL("INSERT INTO Yazar (\n" +
                        "   Yazarid,\n" +
                        "    Yazarismi,\n" +
                        "    Sesid\n" +
                        ")\n" +
                        "VALUES\n" +
                        "    (\n" +
                        "        1,\n" +
                        "        'Stefan Zweig',\n" +
                        "        -1\n" +
                        "    ),\n" +
                        "    (\n" +
                        "        2,\n" +
                        "        'Robert Louis Stevenson',\n" +
                        "        -1\n" +
                        "    ),\n" +
                        "    (\n" +
                        "        3,\n" +
                        "        'Franz Kafka',\n" +
                        "        -1\n" +
                        "    );");

                sqllite.execSQL("INSERT INTO Ses (\n" +
                        "   Sesid,\n" +
                        "    Sesuzantisi,\n" +
                        "    Url,\n" +
                        "    Süresi,\n" +
                        "    Kaldigidakika\n" +
                        ")\n" +
                        "VALUES\n" +
                        "    (\n" +
                        "        1,\n" +
                        "       'satranc',\n" +
                        "        1,\n" +
                        "        'Satranç',\n" +
                        "        '0'\n" +
                        "    ),\n" +
                        "    (\n" +
                        "        2,\n" +
                        "       'drjek',\n" +
                        "        1,\n" +
                        "        'doktor ceykıl ve mister hayt',\n" +
                        "        '0'\n" +
                        "    ),\n" +
                        "    (\n" +
                        "        3,\n" +
                        "       'donusum',\n" +
                        "        1,\n" +
                        "        'dönüşüm',\n" +
                        "        '0'\n" +
                        "    );");

            Kitaplar k1=new Kitaplar(0,"Satranç",1,-1,1,drawableToBitmap(context.getDrawable(R.drawable.satranc)));
            Inserttable("Kitap",k1);

            Kitaplar k2=new Kitaplar(2,"Dr.Jekyll ve Mr.Hyde",2,-1,2,drawableToBitmap(context.getDrawable(R.drawable.drjek)));
            Inserttable("Kitap",k2);

            Kitaplar k3=new Kitaplar(3,"Dönüşüm",3,-1,3,drawableToBitmap(context.getDrawable(R.drawable.donusum)));
            Inserttable("Kitap",k3);

            ListelerGüncelle();
            }
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!tamam");
        }catch (Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!sqlhata"+"    "+e.getMessage());
        }
    }


    //Arraylistleri güncelleme güncelleme kısmı


    public static void ListelerGüncelle(){

        kitaplarliste=new ArrayList<>();
        yazarlarliste=new ArrayList<>();
        seslerlister=new ArrayList<>();

        //Kitaplar
            try {
                Cursor cursor = sqllite.rawQuery("SELECT * FROM Kitap",null);

                cursor.moveToFirst();
                while(cursor != null){

                    if(cursor.getBlob(5)!=null){
                        byte[] bytes = cursor.getBlob(5);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        kitaplarliste.add(new Kitaplar(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4),bitmap));
                    }else{
                        kitaplarliste.add(new Kitaplar(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4),null));
                    }
                cursor.moveToNext();
            }
        }catch (Exception e){System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!ArrayUpdatekitaplar"+e.getMessage());}

        //Sesler
        try {
            Cursor cursor = sqllite.rawQuery("SELECT * FROM Ses",null);

            cursor.moveToFirst();
            while(cursor != null) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!updateurlmi"+cursor.getInt(2));
                seslerlister.add(new Sesler(cursor.getInt(0), cursor.getString(1), cursor.getInt(2) > 0, cursor.getString(3), cursor.getString(4)));
                cursor.moveToNext();
            }
        }catch (Exception e){System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!ArrayUpdatesesler"+e.getMessage());}

        //Yazarlar
        try {
            Cursor cursor = sqllite.rawQuery("SELECT * FROM Yazar",null);

            cursor.moveToFirst();
            while(cursor != null) {
                yazarlarliste.add(new Yazarlar(cursor.getInt(0), cursor.getString(1), cursor.getInt(2) ));
                cursor.moveToNext();
            }
        }catch (Exception e){System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!ArrayUpdateyazarlar"+e.getMessage());}
                }






//InsertInto
    public static void Inserttable(String tablename,Object nesne){
        if(tablename.equals("Kitap")){
            try {
                byte[]bytes;
                Kitaplar k = (Kitaplar) nesne;
                String sqlstring = "INSERT INTO Kitap (kitapismi,Yazarid,Isimsesid,Sesid,Kapakresmi) VALUES (?,?,?,?,?)";
                SQLiteStatement statement = sqllite.compileStatement(sqlstring);
                statement.bindString(1,k.getKitapismi());
                statement.bindString(2,k.getYazarid()+"");
                statement.bindString(3,k.getIsimsesid()+"");
                statement.bindString(4,k.getSesid()+"");
                if(((Kitaplar) nesne).getKapakresmi()!=null){
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ((Kitaplar) nesne).getKapakresmi().compress(Bitmap.CompressFormat.PNG,50,outputStream);
                    bytes=outputStream.toByteArray();
                    statement.bindBlob(5,bytes);
                }
                kitaplarliste.add(k);
                statement.execute();
                //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+"işlem tamam");
            }catch (Exception e){
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!kitapinsert"+e.getMessage());
            }
        }else if(tablename.equals("Ses")){
            try {
                byte[]bytes;
                Sesler k = (Sesler) nesne;
                int boo=0;
                String sqlstring = "INSERT INTO Ses (Sesuzantisi,url,süresi,Kaldigidakika) VALUES (?,?,?,?)";
                //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!urlmi"+k.getUrlmi());
                SQLiteStatement statement = sqllite.compileStatement(sqlstring);
                statement.bindString(1,k.getSesuzantisi());
                if(k.getUrlmi()){boo=1;}
                statement.bindLong(2,boo);
                statement.bindString(3,k.getSüresi());
                statement.bindString(4,k.getKaldigidakika());
                statement.execute();
                seslerlister.add(k);
               // System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+"işlem tamam");
            }catch (Exception e){
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!sesinsert"+e.getMessage());
            }
        }else if(tablename.equals("Yazar")){
            try {
                byte[]bytes;
                Yazarlar k = (Yazarlar) nesne;
                String sqlstring = "INSERT INTO Yazar (Yazarismi,Sesid) VALUES (?,?)";
                SQLiteStatement statement = sqllite.compileStatement(sqlstring);
                statement.bindString(1,k.getYazarismi());
                statement.bindString(2,k.getSesid()+"");
                statement.execute();
                yazarlarliste.add(k);
                // System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+"işlem tamam");
            }catch (Exception e){
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Yazarinsert"+e.getMessage());
            }
        }
    }


    public static void UPDATE(String tablename,Object nesne,String id){
        try {
        if(tablename.equals("Kitap")){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Kitap tablosunda update"+"  object:"+nesne+"   id:: "+id);
            byte[]bytes;
            Kitaplar kitap=(Kitaplar) nesne;
            if(kitap.getKapakresmi()!=null){
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                kitap.getKapakresmi().compress(Bitmap.CompressFormat.PNG,50,outputStream);
                bytes=outputStream.toByteArray();}else {bytes=null;}
            String sqlstring;
            if(kitap.getKapakresmi()!=null||Adminpanelduzenle.resmisil){
                sqlstring = "UPDATE Kitap SET Kitapismi = ?, Yazarid = ?,Isimsesid = ?,Sesid = ?,Kapakresmi = ? WHERE  Kitapid = ?";}
            else{ sqlstring = "UPDATE Kitap SET Kitapismi = ?, Yazarid = ?,Isimsesid = ?,Sesid = ? WHERE Kitapid = ?";}
            SQLiteStatement statement = sqllite.compileStatement(sqlstring);

            statement.bindString(1,kitap.Kitapismi);
            statement.bindString(2,kitap.getYazarid()+"");
            statement.bindString(3,kitap.getIsimsesid()+"");
            statement.bindString(4,kitap.getSesid()+"");
            if(kitap.getKapakresmi()!=null){
                statement.bindBlob(5,bytes);
                statement.bindString(6,id+"");}else{statement.bindString(5,id+"");}
            statement.execute();

            kitaplarliste.set(Lisetdenindexal(kitaplarliste,id),kitap);

        }else if(tablename.equals("Yazar")){
        Yazarlar yazar=(Yazarlar)nesne;
            String sqlstring = "UPDATE Yazar SET Yazarismi = ?, Sesid = ? WHERE Yazarid = ?";
            SQLiteStatement statement = sqllite.compileStatement(sqlstring);
            statement.bindString(3,id+"");
            statement.bindString(1,yazar.getYazarismi());
            statement.bindString(2,yazar.getSesid()+"");
            statement.execute();

            yazarlarliste.set(Lisetdenindexal(yazarlarliste,id),yazar);
        }else if(tablename.equals("Ses")){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!ses tablosunda update"+"  object:"+nesne+"   id:: "+id);
            Sesler ses=(Sesler)nesne;
            int boo=0;
            if(ses.getUrlmi()){boo=1;}
            String sqlstring = "UPDATE Ses SET Sesuzantisi = ?, Url = ?,Süresi = ?, Kaldigidakika = ? WHERE Sesid = ?";
            SQLiteStatement statement = sqllite.compileStatement(sqlstring);
            statement.bindString(1,ses.getSesuzantisi());
            statement.bindLong(2,boo);
            statement.bindString(3,ses.getSüresi());
            statement.bindString(4,ses.getKaldigidakika());
            statement.bindString(5,id+"");
            statement.execute();

            seslerlister.set(Lisetdenindexal(seslerlister,id),ses);


        }


            AdminPanel.listviewarrayadapter.notifyDataSetChanged();
        }catch (Exception e){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!sqlupdate"+e.getMessage());
        }
    }

    public static void DELETE(String tablename,int id){
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1Delete212"+id);
        if(tablename.equals("Kitap")){
            kitaplarliste.remove(Lisetdenindexal(kitaplarliste,id+""));
        }else if(tablename.equals("Yazar")){
            yazarlarliste.remove(Lisetdenindexal(yazarlarliste,id+""));
        }else if(tablename.equals("Ses")){
            seslerlister.remove(Lisetdenindexal(seslerlister,id+""));
        }

        String sqlstring = "DELETE FROM "+tablename +" WHERE "+ tablename+"id=?;";
        SQLiteStatement statement = sqllite.compileStatement(sqlstring);
        statement.bindString(1,id+"");
        statement.execute();

        AdminPanel.listviewarrayadapter.notifyDataSetChanged();
    }






    public SQLdatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    //listeden object çelkme
    public static Object LisetdenObjectal(ArrayList list,String id){
        Object nesne=null;

        for(int i=0;i<list.size();i++){
         if(Listedenidalma(list.get(i).toString()).equals(id)){
          return list.get(i);
         };
        }
        return nesne;
    }
    //listeden index al
    public static int Lisetdenindexal(ArrayList list,String id){
        int nesne=-1;

        for(int i=0;i<list.size();i++){
            if(Listedenidalma(list.get(i).toString()).equals(id)){
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1indexal"+id+"  ---"+i);
                return i;
            };
        }
        return nesne;
    }


    //listeden seçilen objenin to stringde idsini alma
    public static String Listedenidalma(String text){

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


    //ilk sql yokken kitap resimlerini drawabledan alıyor onun için method
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }




}
