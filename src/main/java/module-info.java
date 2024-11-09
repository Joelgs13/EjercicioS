module com.example.ejercicios {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql.rowset;

    opens com.example.ejercicios.MODEL to javafx.base;
    exports com.example.ejercicios.MODEL;
    opens com.example.ejercicios to javafx.fxml;
    exports com.example.ejercicios;
}