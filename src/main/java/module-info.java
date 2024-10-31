module com.example.ejercicios {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens MODEL to javafx.base;
    exports MODEL;
    opens com.example.ejercicios to javafx.fxml;
    exports com.example.ejercicios;
}