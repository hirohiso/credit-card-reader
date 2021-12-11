package com.example.creditcardreader.datasource;

import javax.smartcardio.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * クレジットカードからPCSC経由でデータを読みとる
 */
public class CreditCardInfoPcscReader {
    public void readCard() {
        try {
            TerminalFactory tf = TerminalFactory.getDefault();
            CardTerminals ct = tf.terminals();
            System.out.println(ct.list().toString());

            CardTerminal terminal = ct.list().get(0);

            Card card = null;

            card = terminal.connect("*");
            System.out.println(card.toString());
            CardChannel channel = card.getBasicChannel();
            CardTransfer cardTransfer = new CardTransfer(card);
            ATR atr = card.getATR();
            Fci fci = selectAid(cardTransfer);
            Tl[] pdol = fci.pdol();
            Afl[] afl = processingOption(cardTransfer, pdol);
            readRecord(cardTransfer, afl);

        } catch (CardException e) {
            e.printStackTrace();
            return;
        }
    }

    private void readRecord(CardTransfer cardTransfer, Afl[] afl) throws CardException {
        List<Tlv> list = new LinkedList<>();
        for (int i = 0; i < afl.length; i++) {
            int fi = afl[i].getFileIngicate();
            String temp = String.format("%02x", fi);
            int start = afl[i].getRecordStart();
            int end = afl[i].getRecordEnd();
            for (int j = start; j <= end; j++) {
                String num = String.format("%02x", j);
                String rrcmd = "00B2" + num + temp + "00";
                BinaryData binaryData = BinaryData.from(rrcmd);
                ResponseAPDU answer = cardTransfer.rawCommand(new CommandAPDU(binaryData.toByteArray()));
                Tlv tlv = Tlv.from(answer.getData());
                Tlv[] inner = tlv.getInnerTlv();
                for (int k = 0; k < inner.length; k++) {
                    list.add(inner[k]);
                }
            }
        }

        for(Tlv tlv : list){
            System.out.println("RR[tag]" + BinaryData.of(tlv.getTag()).toString());
            System.out.println("RR[value]" + BinaryData.of(tlv.getValue()).toString());
        }

    }

    private Afl[] processingOption(CardTransfer transfer, Tl[] pdol) throws CardException {
        Map<BinaryData, BinaryData> map = new HashMap<>();
        BinaryData currencyCodeTag = BinaryData.from("9f1a");
        BinaryData currencyCode = BinaryData.from("0392");
        map.put(currencyCodeTag, currencyCode);
        String pdolHead = "80A80000";
        String pdolval = "";
        String pdolFoot = "00";
        for (int i = 0; i < pdol.length; i++) {
            BinaryData data = map.get(BinaryData.of(pdol[i].getTag()));
            pdolval += data.toString();
        }
        String temp = String.format("%02x", pdolval.length() / 2);
        String temp2 = String.format("%02x", pdolval.length() / 2 + 2);
        String pdolStr = pdolHead + temp2 + "83" + temp + pdolval + pdolFoot;
        BinaryData binaryData = BinaryData.from(pdolStr);
        ResponseAPDU answer = transfer.rawCommand(new CommandAPDU(binaryData.toByteArray()));
        return new GpoAnalyzer().analyze(answer.getData());
    }

    private Fci selectAid(CardTransfer transfer) throws CardException {
        //Select AID
        byte[] visa = {
                (byte) 0x00, (byte) 0xA4,
                (byte) 0x04, (byte) 0x00,
                (byte) 0x07,
                (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x03, (byte) 0x10, (byte) 0x10,
                (byte) 0x00
        };

        ResponseAPDU answer = transfer.rawCommand(new CommandAPDU(visa));
        byte[] data = answer.getData();
        return new FciAnalyzer().analyze(data);
    }
}
