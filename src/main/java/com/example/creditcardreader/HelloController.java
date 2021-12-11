package com.example.creditcardreader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onReadRecordButtonClick() {
    }

    @FXML
    public void onVerifyPin() {
        System.out.println("verify called");
    }
}