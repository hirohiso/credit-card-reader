package com.example.creditcardreader;

import com.example.creditcardreader.service.ReadRecordService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onReadRecordButtonClick() {
        ReadRecordService service = new ReadRecordService();
        service.readRecord();
    }

    @FXML
    public void onVerifyPin() {
        System.out.println("verify called");
    }
}