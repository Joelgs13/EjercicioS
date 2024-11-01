package com.example.ejercicios;

import DAO.DaoAnimales;
import MODEL.AnimalModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.example.ejercicios.ListaDeAnimalesController.isEsAniadir;

public class AniadirEditarAnimalController {

    @FXML
    private DatePicker fechaPrimeraConsulta;
    @FXML
    private TextField tfNombre, tfEspecie, tfRaza, tfSexo, tfEdad, tfPeso, tfObservaciones;

    private TableView<AnimalModel> tablaAnimales; // Tabla para mostrar los animales
    private Blob imagenBlob; // Blob para almacenar la imagen del animal
    private AnimalModel animalSeleccionado; // Animal que se está editando o añadiendo

    /**
     * Método para cargar la imagen desde un archivo.
     *
     * @param event El evento de acción que se produce al hacer clic.
     */
    @FXML
    void cargarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(((Button) event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                imagenBlob = new javax.sql.rowset.serial.SerialBlob(fis.readAllBytes());
            } catch (IOException | SQLException e) {
                showAlert("Error", "No se ha podido cargar la imagen seleccionada.", Alert.AlertType.ERROR);
                imagenBlob = null; // Dejar el `Blob` en nulo si hay error
            }
        }
    }

    /**
     * Método para cancelar la operación actual y cerrar la ventana.
     *
     * @param event El evento de acción que se produce al hacer clic.
     */
    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Método para guardar o actualizar el animal.
     *
     * @param event El evento de acción que se produce al hacer clic.
     */
    @FXML
    void guardar(ActionEvent event) {
        // Validación de datos ingresados
        if (!validarFormulario()) return;

        AnimalModel nuevoAnimal = crearAnimalDesdeFormulario();
        boolean operacionExitosa = false;

        // Insertar o actualizar según el modo de operación
        if (isEsAniadir()) {
            operacionExitosa = DaoAnimales.insertarAnimal(nuevoAnimal) != -1;
            if (operacionExitosa) {
                tablaAnimales.getItems().add(nuevoAnimal);
            }
        } else {
            operacionExitosa = DaoAnimales.modificarAnimal(animalSeleccionado, nuevoAnimal);
        }

        // Mostrar resultado de la operación y cerrar ventana si es exitoso
        if (operacionExitosa) {
            showAlert("Éxito", "El animal ha sido " + (isEsAniadir() ? "guardado" : "actualizado") + " correctamente.", Alert.AlertType.INFORMATION);
            tablaAnimales.refresh();
            cerrarVentana(event);
        } else {
            showAlert("Error", "No se pudo " + (isEsAniadir() ? "guardar" : "actualizar") + " el animal. Inténtelo nuevamente.", Alert.AlertType.ERROR);
        }
        tablaAnimales.refresh();
        //no hay manera de actualizar la tabla al editar
    }

    /**
     * Método para cargar los datos del animal en el formulario (modo edición).
     *
     * @param animal El animal que se desea cargar en el formulario.
     */
    public void cargarAnimal(AnimalModel animal) {
        this.animalSeleccionado = animal;
        tfNombre.setText(animal.getNombre());
        tfEspecie.setText(animal.getEspecie());
        tfRaza.setText(animal.getRaza());
        tfSexo.setText(animal.getSexo());
        tfEdad.setText(String.valueOf(animal.getEdad()));
        tfPeso.setText(String.valueOf(animal.getPeso()));
        tfObservaciones.setText(animal.getObservaciones());
        fechaPrimeraConsulta.setValue(animal.getFechaPrimeraConsulta());
        imagenBlob = animal.getFoto();
    }

    /**
     * Método para crear un objeto AnimalModel a partir de los datos del formulario.
     *
     * @return Un nuevo objeto AnimalModel con los datos ingresados.
     */
    private AnimalModel crearAnimalDesdeFormulario() {
        return new AnimalModel(
                animalSeleccionado != null ? animalSeleccionado.getId() : 0,  // Mantener el ID si es edición
                tfNombre.getText(),
                tfEspecie.getText(),
                tfRaza.getText(),
                tfSexo.getText(),
                Integer.parseInt(tfEdad.getText()),
                Integer.parseInt(tfPeso.getText()),
                tfObservaciones.getText(),
                fechaPrimeraConsulta.getValue(),
                imagenBlob
        );
    }

    /**
     * Método para validar los datos ingresados en el formulario.
     *
     * @return true si los datos son válidos, false en caso contrario.
     */
    private boolean validarFormulario() {
        if (tfNombre.getText().isEmpty() || tfEspecie.getText().isEmpty() || tfRaza.getText().isEmpty() || tfSexo.getText().isEmpty()) {
            showAlert("Error en los datos", "Por favor, complete todos los campos.", Alert.AlertType.ERROR);
            return false;
        }
        try {
            Integer.parseInt(tfEdad.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Edad debe ser un número entero.", Alert.AlertType.ERROR);
            return false;
        }
        try {
            Integer.parseInt(tfPeso.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Peso debe ser un número entero.", Alert.AlertType.ERROR);
            return false;
        }
        if (fechaPrimeraConsulta.getValue() == null) {
            showAlert("Error", "Debe seleccionar una fecha válida para la primera consulta.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    /**
     * Método para mostrar alertas.
     *
     * @param titulo   El título de la alerta.
     * @param contenido El contenido de la alerta.
     * @param tipo      El tipo de alerta a mostrar.
     */
    private void showAlert(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    /**
     * Método para cerrar la ventana modal.
     *
     * @param event El evento de acción que se produce al hacer clic.
     */
    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Setter para inyectar la referencia a la tabla de animales.
     *
     * @param tablaAnimales La tabla donde se mostrarán los animales.
     */
    public void setTablaAnimales(TableView<AnimalModel> tablaAnimales) {
        this.tablaAnimales = tablaAnimales;
    }

}
