package com.example.creditcardreader.datasource;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GpoObjectValue {
    private byte[] aip;
    private byte[] afl;

    public static GpoObjectValue from(byte[] data) {
        //api 最初の2バイト
        byte[] api = Arrays.copyOfRange(data, 0, 2);
        //afl　残り　４バイトごとの塊
        byte[] afl = Arrays.copyOfRange(data, 2, data.length + 1);
        return new GpoObjectValue(api, afl);
    }

    public static GpoObjectValue fromFormat2(byte[] api,byte[] afl){
        return  new GpoObjectValue(api, afl);

    }

    private GpoObjectValue(byte[] api, byte[] afl) {
        this.aip = api;
        this.afl = afl;
    }

    public Afl[] getAfl() {
        List<Afl> list = new LinkedList<>();
        for (int i = 0; i < (afl.length) / 4; i++) {
            byte[] data = {afl[(4 * i)], afl[(4 * i) + 1], afl[(4 * i) + 2], afl[(4 * i) + 3]};
            list.add(Afl.from(data));
        }
        return list.toArray(new Afl[list.size()]);
    }
}
