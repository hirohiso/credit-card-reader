package com.example.creditcardreader.datasource;

public class GpoAnalyzer {
    public Afl[] analyze(byte[] data){
        BinaryData format1 = BinaryData.from("80");
        BinaryData format2 = BinaryData.from("77");
        Tlv gpoObject = Tlv.from(data);
        BinaryData tag = BinaryData.of(gpoObject.getTag());
        if(format1.equals(tag)){
            BinaryData b  = BinaryData.of(gpoObject.getValue());
            System.out.println(b.toString());
            GpoObjectValue value =  GpoObjectValue.from(b.toByteArray());
            Afl[] afl = value.getAfl();
            return afl;
        }else if(format2.equals(tag)){
            Tlv[] apiafl = gpoObject.getInnerTlv();
            GpoObjectValue value =  GpoObjectValue.fromFormat2(apiafl[0].getValue(),apiafl[1].getValue());
            Afl[] afl = value.getAfl();
            return afl;
        }
        throw  new IllegalArgumentException();
    }
}
