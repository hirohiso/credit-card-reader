package com.example.creditcardreader.datasource;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Tlv {
    private byte[] tag;
    private byte[] length;
    private byte[] value;

    public static Tlv from(byte[] data) {
        int tagEnd = 0;
        int mask = 0x3F;
        System.out.println(data[tagEnd] & mask);
        while ((data[tagEnd] & mask) == 0x1F) {
            tagEnd++;
            mask = 0x80;
        }

        byte[] t = Arrays.copyOfRange(data, 0, tagEnd + 1);

        int lengthEnd = tagEnd + 1;
        int tagmask = 0x80;
        if ((data[lengthEnd] & tagmask) != 0) {
            lengthEnd += (data[lengthEnd] & 0x7F);
        }

        byte[] l = Arrays.copyOfRange(data, tagEnd + 1, lengthEnd + 1);
        int datalen = calLength(l);
        int valueEnd = lengthEnd + datalen;

        byte[] v = Arrays.copyOfRange(data, lengthEnd + 1, valueEnd + 1);
        return new Tlv(t, l, v);
    }

    private static int calLength(byte[] length) {
        if ((length[0] & 0x80) != 0) {
            int range = (length[0] & 0x7F);
            throw new UnsupportedOperationException("128byte以上のlengthに未対応");
        } else {
            return length[0];
        }
    }

    private Tlv(byte[] t, byte[] l, byte[] v) {
        tag = t;
        length = l;
        value = v;
    }

    public boolean isConstructed() {
        return (this.tag[0] & 0x20) == 0x20;
    }

    public byte[] getTag() {
        return tag;
    }

    public byte[] getValue() {
        return value;
    }

    public Tlv[] getInnerTlv() {
        List<Tlv> list = new LinkedList<>();
        byte[] data = this.value.clone();
        while (data != null && data.length != 0){
            Tlv tlv = Tlv.from(data);
            list.add(tlv);
            data = Arrays.copyOfRange(data,tlv.length(),data.length);
        }
        return list.toArray(new Tlv[list.size()]);
    }

    public int length() {
        return tag.length + length.length + value.length;
    }
}
