module com.projeto.restaurante {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.projeto.restaurante.main to javafx.graphics;
    opens com.projeto.restaurante.controller to javafx.fxml;
}