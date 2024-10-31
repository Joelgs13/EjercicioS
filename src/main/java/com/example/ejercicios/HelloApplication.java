package com.example.ejercicios;

import BBDD.ConexionBBDD;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class HelloApplication extends Application {

    static Stage stage;
    @Override
    public void start(Stage s) throws IOException {
        Properties connConfig = ConexionBBDD.loadProperties();
        stage = s;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ListaDeAnimales.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setTitle("ANIMALES!");
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getStage() {return stage;}

    public static void main(String[] args) {
        launch();
    }
}