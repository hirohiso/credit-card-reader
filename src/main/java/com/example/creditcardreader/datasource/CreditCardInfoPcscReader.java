package com.example.creditcardreader.datasource;

import javax.smartcardio.*;
import java.util.HashMap;
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
            Fci fci =selectAid(cardTransfer);
            Tl[] pdol = fci.pdol();
            processingOption(cardTransfer,pdol);

        } catch (CardException e) {
            e.printStackTrace();
            return;
        }
    }

    private void processingOption(CardTransfer transfer,Tl[] pdol) throws CardException {
        Map<BinaryData,BinaryData> map = new HashMap<>();
        BinaryData currencyCodeTag = BinaryData.from("9f1a");
        BinaryData currencyCode = BinaryData.from("0392");
        map.put(currencyCodeTag,currencyCode);
        String pdolHead = "80A80000";
        String pdolval = "";
        String pdolFoot = "00";
        for (int i = 0; i < pdol.length; i++) {
            BinaryData data = map.get(BinaryData.of(pdol[i].getTag()) );
            pdolval += data.toString();
        }
        String temp = String.format("%02x",pdolval.length() /2);
        String temp2 = String.format("%02x",pdolval.length() /2 +2);
        String pdolStr = pdolHead +temp2 +"83"+ temp + pdolval + pdolFoot;
        BinaryData binaryData = BinaryData.from(pdolStr);
        System.out.println(binaryData.toString());
        ResponseAPDU answer = transfer.rawCommand(new CommandAPDU(binaryData.toByteArray()));
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
