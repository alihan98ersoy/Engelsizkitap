package com.cmpestudents.seslikitap;

import java.util.ArrayList;

public class Didyoumean {

    public static String[][] CheckSimilarity(ArrayList<String> txt, ArrayList<String> keywords){
        String[][] basket=new String[99][3];//ismi,benzerlik oranı
        int counter=0;
        for(String text : txt) {
            for (String key : keywords) {
                SimilarityStrategy strategy = new JaroWinklerStrategy();
                String target = key;
                String source = text;
                StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
                double score = service.score(source, target);
                //System.out.println("!!!!!!!!!!!!!!!"+key+" "+score);
                if (score > 0.6) {//benzerlik yuzde 50 ve altında olursa döndürmüyor geriye
                    basket[counter][0] = text;
                    basket[counter][1] = key;
                    basket[counter++][2] = score + "";
                }
            }
        }
        return Order(basket);
    }


    public static String[][] Order(String[][] array){
        String[][] basket = new String[99][3];
        basket=array;

        for(int i=0;i<basket.length-1;i++){
            if(basket[i][2]==null){break;}
            else{
                double max=Double.parseDouble(array[i][2]);
                int order=i;
                for(int j=i+1;j<basket.length;j++){
                    if(basket[j][2]==null)break;
                    else{
                        if(max<Double.parseDouble(basket[j][2])){
                            max=Double.parseDouble(basket[j][2]);
                            order=j;
                        }
                    }
                }
                String a=basket[order][0];
                String b=basket[order][1];
                String c=basket[order][2];
                basket[order][0]=basket[i][0];
                basket[order][1]=basket[i][1];
                basket[order][2]=basket[i][2];
                basket[i][0]=a;
                basket[i][1]=b;
                basket[i][2]=c;
            }
        }
        return basket;
    }





}
