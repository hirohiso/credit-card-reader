package com.example.creditcardreader.datasource;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

public class Afl {
    private byte[] data;

    public static Afl from(byte[] data) {
        return new Afl(data);
    }

    private Afl(byte[] a) {
        this.data = a;
    }

    public int getFileIndicate() {
        int fi = Byte.toUnsignedInt((byte) ((data[0]) | 0x04));
        return fi;
    }

    public int getRecordStart() {
        return Byte.toUnsignedInt(data[1]);
    }

    public int getRecordEnd() {
        return Byte.toUnsignedInt(data[2]);
    }

    public Iterator<BinaryData> getSelectParameterIterator() {
        List<BinaryData> list = new LinkedList<>();
        int fi = this.getFileIndicate();
        String temp = String.format("%02x", fi);
        int start = this.getRecordStart();
        int end = this.getRecordEnd();

        for (int j = start; j <= end; j++) {
            String num = String.format("%02x", j);
            String rrcmd = num + temp;
            BinaryData binaryData = BinaryData.from(rrcmd);
            list.add(binaryData);
        }
        return list.iterator();
    }
}
