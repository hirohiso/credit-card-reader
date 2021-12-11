module com.example.creditcardreader {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.smartcardio;
    requires org.junit.jupiter.api;

    opens com.example.creditcardreader to javafx.fxml;
    exports com.example.creditcardreader;
}