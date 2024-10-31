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

public class AniadirEditarAnimalController {

    public DatePicker getFechaPrimeraConsulta() {
        return fechaPrimeraConsulta;
    }

    public void setFechaPrimeraConsulta(DatePicker fechaPrimeraConsulta) {
        this.fechaPrimeraConsulta = fechaPrimeraConsulta;
    }

    public Blob getImagenBlob() {
        return imagenBlob;
    }

    public void setImagenBlob(Blob imagenBlob) {
        this.imagenBlob = imagenBlob;
    }

    @FXML
    private DatePicker fechaPrimeraConsulta;

    @FXML
    private TextField tfEdad;

    @FXML
    private TextField tfEspecie;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfObservaciones;

    @FXML
    private TextField tfPeso;

    @FXML
    private TextField tfRaza;

    public TextField getTfEdad() {
        return tfEdad;
    }

    public void setTfEdad(TextField tfEdad) {
        this.tfEdad = tfEdad;
    }

    public TextField getTfEspecie() {
        return tfEspecie;
    }

    public void setTfEspecie(TextField tfEspecie) {
        this.tfEspecie = tfEspecie;
    }

    public TextField getTfNombre() {
        return tfNombre;
    }

    public void setTfNombre(TextField tfNombre) {
        this.tfNombre = tfNombre;
    }

    public TextField getTfObservaciones() {
        return tfObservaciones;
    }

    public void setTfObservaciones(TextField tfObservaciones) {
        this.tfObservaciones = tfObservaciones;
    }

    public TextField getTfPeso() {
        return tfPeso;
    }

    public void setTfPeso(TextField tfPeso) {
        this.tfPeso = tfPeso;
    }

    public TextField getTfRaza() {
        return tfRaza;
    }

    public void setTfRaza(TextField tfRaza) {
        this.tfRaza = tfRaza;
    }

    public TextField getTfSexo() {
        return tfSexo;
    }

    public void setTfSexo(TextField tfSexo) {
        this.tfSexo = tfSexo;
    }

    @FXML
    private TextField tfSexo;
    private TableView<AnimalModel> tablaAnimales;
    private Blob imagenBlob;
    private AnimalModel animalSeleccionado;


    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

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
                showAlert("Error", "No se ha podido cargar la imagen seleccionada.",Alert.AlertType.ERROR);
                imagenBlob = null; // Dejar el `Blob` en nulo si hay error
            }
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        try {
            // Validación de datos ingresados
            String nombre = tfNombre.getText();
            String especie = tfEspecie.getText();
            String raza = tfRaza.getText();
            String sexo = tfSexo.getText();

            if (nombre.isEmpty() || especie.isEmpty() || raza.isEmpty() || sexo.isEmpty()) {
                showAlert("Error en los datos", "Por favor, complete todos los campos.", Alert.AlertType.ERROR);
                return;
            }

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

            // Crear o actualizar un objeto AnimalModel con el Blob
            AnimalModel animal = new AnimalModel(
                    (animalSeleccionado != null) ? animalSeleccionado.getId() : 0, // Usar ID existente si estamos en edición
                    nombre, especie, raza, sexo, edad, peso, observaciones,
                    java.sql.Date.valueOf(fechaConsulta).toLocalDate(), imagenBlob);

            boolean operacionExitosa;

            if (animalSeleccionado != null) {  // Editar animal si es un objeto existente
                operacionExitosa = DaoAnimales.modificarAnimal(animalSeleccionado, animal);
            } else { // Crear un nuevo animal si no hay objeto seleccionado
                operacionExitosa = DaoAnimales.insertarAnimal(animal) != -1;
                if (operacionExitosa) {
                    tablaAnimales.getItems().add(animal); // Agregar a la tabla si es una inserción
                }
            }

            if (operacionExitosa) {
                showAlert("Éxito", "El animal ha sido " +
                        (animalSeleccionado != null ? "actualizado" : "guardado") +
                        " correctamente.", Alert.AlertType.INFORMATION);
                tablaAnimales.refresh(); // Refrescar tabla
                cerrarVentana(event); // Cerrar la ventana modal
            } else {
                showAlert("Error", "No se pudo " +
                        (animalSeleccionado != null ? "actualizar" : "guardar") +
                        " el animal. Inténtelo nuevamente.", Alert.AlertType.ERROR);
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

    public void cargarAnimal(AnimalModel animal) {
        this.animalSeleccionado = animal;

        // Rellenar los campos del formulario con los datos de `animalSeleccionado`
        tfNombre.setText(animal.getNombre());
        tfEspecie.setText(animal.getEspecie());
        tfRaza.setText(animal.getRaza());
        tfSexo.setText(animal.getSexo());
        tfEdad.setText(String.valueOf(animal.getEdad()));
        tfPeso.setText(String.valueOf(animal.getPeso()));
        tfObservaciones.setText(animal.getObservaciones());
        fechaPrimeraConsulta.setValue(animal.getFechaPrimeraConsulta());
        imagenBlob = animal.getFoto(); // Establecer el Blob de imagen
    }

    // Método para crear el objeto modificado con los datos actuales del formulario
    public AnimalModel crearAnimalDesdeFormulario() {
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
                imagenBlob  // Mantener la imagen existente o asignar una nueva si se cargó
        );
    }


}
