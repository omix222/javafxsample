module com.example.hellofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;

    opens com.example.hellofx to javafx.fxml;
    exports com.example.hellofx;
}
