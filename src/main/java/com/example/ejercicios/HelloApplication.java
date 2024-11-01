package com.example.ejercicios;

import BBDD.ConexionBBDD;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class HelloApplication extends Application {

    static Stage stage; // Variable estática para almacenar la instancia del escenario principal

    /**
     * Método de inicio de la aplicación JavaFX.
     *
     * @param s El escenario principal de la aplicación.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage s) throws IOException {
        Properties connConfig = ConexionBBDD.loadProperties(); // Cargar la configuración de conexión a la base de datos
        stage = s; // Asignar el escenario principal
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ListaDeAnimales.fxml")); // Cargar el archivo FXML
        Scene scene = new Scene(fxmlLoader.load()); // Crear una escena con el contenido del archivo FXML
        stage.setResizable(false); // Desactivar la opción de redimensionar la ventana
        stage.setTitle("ANIMALES!"); // Establecer el título de la ventana
        stage.setScene(scene); // Asignar la escena al escenario principal
        stage.show(); // Mostrar la ventana
    }

    /**
     * Método para obtener la instancia del escenario principal.
     *
     * @return El escenario principal de la aplicación.
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * Método principal que se utiliza para lanzar la aplicación JavaFX.
     *
     * @param args Argumentos de línea de comando.
     */
    public static void main(String[] args) {
        launch(); // Iniciar la aplicación
    }
}
