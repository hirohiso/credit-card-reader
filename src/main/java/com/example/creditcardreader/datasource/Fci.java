package com.example.creditcardreader.datasource;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Fci {
    //DFNAME
    byte[] dfName;
    //Application Label
    byte[] appLabel;
    //Application Priority Indicator
    byte[] api;
    //PDOL
    byte[] pdol;


    public String dfName(){
        return BinaryData.of(dfName).toString();
    }
    public String apLabel(){
        return new String(appLabel);
    }

    public Tl[] pdol(){
        List<Tl> list = new LinkedList<>();
        byte[] data = this.pdol.clone();
        while (data != null && data.length != 0){
            Tl tl = Tl.from(data);
            list.add(tl);
            System.out.println("[PDOL/tag]:"+ BinaryData.of(tl.getTag()).toString());
            data = Arrays.copyOfRange(data,tl.length(),data.length);
        }
        return list.toArray(new Tl[list.size()]);
    }
}
