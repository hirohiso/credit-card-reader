package com.example.creditcardreader.datasource;

import java.util.Arrays;

public class Tl {
    private byte[] tag;
    private byte[] length;

    public static Tl from(byte[] data) {
        int tagEnd = 0;
        int mask = 0x1F;
        while ((data[tagEnd] & mask) == mask) {
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
        return new Tl(t, l);
    }

    private static int calLength(byte[] length) {
        if ((length[0] & 0x80) != 0) {
            int range = (length[0] & 0x7F);
            throw new UnsupportedOperationException("128byte以上のlengthに未対応");
        } else {
            return length[0];
        }
    }

    private Tl(byte[] t, byte[] l) {
        tag = t;
        length = l;
    }

    public boolean isConstructed() {
        return (this.tag[0] & 0x20) == 0x20;
    }

    public byte[] getTag() {
        return tag;
    }

    public int length() {
        return tag.length + length.length;
    }
}
