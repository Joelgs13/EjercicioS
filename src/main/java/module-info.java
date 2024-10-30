module com.example.ejercicios {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ejercicios to javafx.fxml;
    exports com.example.ejercicios;
}