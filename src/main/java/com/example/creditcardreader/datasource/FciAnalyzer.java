package com.example.creditcardreader.datasource;

import java.util.Arrays;

public class FciAnalyzer {
    private static final byte[] DfTag = {(byte)0x84};
    private static final byte[] FpiTag = {(byte)0xA5};
    private static final byte[] pdolTag = {(byte)0x9F,(byte)0x38};
    private static final byte[] apiTag = {(byte)0x87};
    private static final byte[] aplabelTag = {(byte)0x50};

    public Fci analyze(byte[] data){
        Fci result = new Fci();
        if(Byte.toUnsignedInt(data[0]) != 0x6F){
            throw new IllegalArgumentException();
        }
        Tlv fciTemp = Tlv.from(data);
        Tlv[] arr = fciTemp.getInnerTlv();
        for (int i = 0; i < arr.length; i++) {
            if(Arrays.equals(arr[i].getTag(),DfTag)){
                System.out.println(BinaryData.of(arr[i].getValue()).toString());
                result.dfName = arr[i].getValue();
            }
            if(Arrays.equals(arr[i].getTag(),FpiTag)){
                Tlv[] fciarr = arr[i].getInnerTlv();
                for (int j = 0; j < fciarr.length; j++) {
                    System.out.println("[tag]" +BinaryData.of(fciarr[j].getTag()).toString());
                    System.out.println("[value]" +BinaryData.of(fciarr[j].getValue()).toString());
                    if(Arrays.equals(fciarr[j].getTag(),aplabelTag)){
                        result.appLabel = fciarr[j].getValue();
                    }
                    if(Arrays.equals(fciarr[j].getTag(),apiTag)){
                        result.api = fciarr[j].getValue();
                    }
                    if(Arrays.equals(fciarr[j].getTag(),pdolTag)){
                        result.pdol = fciarr[j].getValue();
                    }
                }
            }
        }

        return result;
    }
}
