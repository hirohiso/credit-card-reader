package com.example.creditcardreader;

import com.example.creditcardreader.service.ReadRecordResult;
import com.example.creditcardreader.service.ReadRecordService;
import com.example.creditcardreader.service.VerifyPinService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField panText;
    @FXML
    private TextField expiredDateText;
    @FXML
    private TextField nameText;
    @FXML
    private PasswordField pinText;

    @FXML
    protected void onReadRecordButtonClick() {
        ReadRecordService service = new ReadRecordService();
        ReadRecordResult result =  service.readRecord();
        this.panText.setText(result.getPan());
        this.expiredDateText.setText(result.getDate());
        this.nameText.setText(result.getName());
    }

    @FXML
    public void onVerifyPin() {
        System.out.println("verify called");
        String str = this.pinText.getText();
        System.out.println(str);
        VerifyPinService service = new VerifyPinService();
        service.verifyPin(str);
    }
}