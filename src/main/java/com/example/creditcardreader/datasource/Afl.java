package com.example.creditcardreader.datasource;

public class Afl {
    private byte[] data;

    public static Afl from(byte[] data) {
        return new Afl(data);
    }

    private Afl(byte[] a) {
        this.data = a;
    }

    public int getFileIngicate() {
        int fi = Byte.toUnsignedInt((byte)((data[0]) | 0x04));
        return fi;
    }

    public int getRecordStart() {
        return Byte.toUnsignedInt(data[1]);
    }

    public int getRecordEnd() {
        return Byte.toUnsignedInt(data[2]);
    }
}
