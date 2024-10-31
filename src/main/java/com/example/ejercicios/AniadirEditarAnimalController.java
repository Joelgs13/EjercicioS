package com.example.ejercicios;

import DAO.DaoAnimales;
import MODEL.AnimalModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AniadirEditarAnimalController {

    @FXML
    private DatePicker fechaPrimeraConsulta;

    @FXML
    private Label lbNumeroDeSocios;

    @FXML
    private TextField tfEdad;

    @FXML
    private TextField tfEspecie;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfNumeroDeSocios;

    @FXML
    private TextField tfObservaciones;

    @FXML
    private TextField tfPeso;

    @FXML
    private TextField tfRaza;

    @FXML
    private TextField tfSexo;
    private TableView<AnimalModel> tablaAnimales;

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void cargarImagen(ActionEvent event) {

    }

    @FXML
    void guardar(ActionEvent event) {
        try {
            // Validación de datos ingresados
            String nombre = tfNombre.getText();
            String especie = tfEspecie.getText();
            String raza = tfRaza.getText();
            String sexo = tfSexo.getText();

            // Validación de campos numéricos
            int edad;
            try {
                edad = Integer.parseInt(tfEdad.getText());
            } catch (NumberFormatException e) {
                showAlert("Error", "Edad debe ser un número entero.", Alert.AlertType.ERROR);
                return;
            }

            int peso;
            try {
                peso = Integer.parseInt(tfPeso.getText());
            } catch (NumberFormatException e) {
                showAlert("Error", "Peso debe ser un número entero.", Alert.AlertType.ERROR);
                return;
            }

            // Validación de fecha
            LocalDate fechaConsulta = fechaPrimeraConsulta.getValue();
            if (fechaConsulta == null) {
                showAlert("Error", "Debe seleccionar una fecha válida para la primera consulta.", Alert.AlertType.ERROR);
                return;
            }

            String observaciones = tfObservaciones.getText();

            // Crear un nuevo objeto AnimalModel
            AnimalModel animal = new AnimalModel(0, nombre, especie, raza, sexo, edad, peso, observaciones, fechaConsulta, null);

            boolean insertado;
            // Intentar insertar en la base de datos
            if (DaoAnimales.insertarAnimal(animal) != -1){
                insertado = true;
            } else {
                insertado = false;
            }


            if (insertado) {
                showAlert("Éxito", "El animal ha sido guardado correctamente.", Alert.AlertType.INFORMATION);
                tablaAnimales.getItems().add(animal); // Agregar a la tabla
                tablaAnimales.refresh(); // Actualizar tabla
                cerrarVentana(event); // Cerrar la ventana modal
            } else {
                showAlert("Error", "No se pudo guardar el animal. Inténtelo nuevamente.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Ocurrió un error inesperado.", Alert.AlertType.ERROR);
        }
    }

    // Método para mostrar alertas
    private void showAlert(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    // Método para cerrar la ventana modal
    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }


    public void setTablaAnimales(TableView<AnimalModel> idTablaAnimales) {
        this.tablaAnimales = idTablaAnimales;
    }
}
