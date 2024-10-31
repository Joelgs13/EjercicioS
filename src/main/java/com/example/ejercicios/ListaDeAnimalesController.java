package com.example.ejercicios;

import BBDD.ConexionBBDD;
import DAO.DaoAnimales;
import MODEL.AnimalModel;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

import static com.example.ejercicios.HelloApplication.stage;

public class ListaDeAnimalesController {

    @FXML
    public TableView<AnimalModel> tablaAnimales;
    @FXML
    private MenuItem miAniadirAnimal;

    @FXML
    private MenuItem miBorrarAnimal;

    @FXML
    private MenuItem miEditarAnimal;

    @FXML
    private MenuItem miInformacionAnimal;

    @FXML
    private MenuItem miInformacionAnimal1;

    @FXML
    private TableColumn<AnimalModel, Integer> tcEdad;

    @FXML
    private TableColumn<AnimalModel, String> tcEspecie;

    @FXML
    private TableColumn<AnimalModel, LocalDate> tcFecha;

    @FXML
    private TableColumn<?, ?> tcId;

    @FXML
    private TableColumn<AnimalModel, String> tcNombre;

    @FXML
    private TableColumn<AnimalModel, String> tcObservaciones;

    @FXML
    private TableColumn<AnimalModel, Integer> tcPeso;

    @FXML
    private TableColumn<AnimalModel, String> tcRaza;

    @FXML
    private TableColumn<AnimalModel, String> tcSexo;

    @FXML
    private TextField tfNombre;

    private FilteredList<AnimalModel> filtro;
    private static ObservableList<AnimalModel> listaTodas;
    private MenuItem selectedMenuItem;
    private static Stage s;
    private static boolean esAniadir;
    private static boolean borrar=true;
    @FXML
    void aniadirAnimal(ActionEvent event) {

    }

    @FXML
    void borrarAnimal(ActionEvent event) {

    }

    @FXML
    void editarAnimal(ActionEvent event) {

    }

    @FXML
    void informacionAnimal(ActionEvent event) {

    }

    @FXML
    public void initialize() {
        try {
            ConexionBBDD con=new ConexionBBDD();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tfNombre.setOnKeyReleased(event -> filtrarPorNombre());

        // Configurar el evento de doble clic en la tabla privada
        tablaAnimales.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Doble clic
                informacionAeropuerto(null); // Llamamos a la función con un evento nulo
            }
        });

        // Configurar el evento de doble clic en la tabla pública
        tablaAnimales.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Doble clic
                informacionAeropuerto(null); // Llamamos a la función con un evento nulo
            }
        });

        //Tabla
        listaTodas= DaoAnimales.cargarLista();
        tablaAnimales.setItems(listaTodas);
        tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcId.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcId.setCellValueFactory(new PropertyValueFactory<>("especie"));
        tcId.setCellValueFactory(new PropertyValueFactory<>("raza"));
        tcId.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        tcId.setCellValueFactory(new PropertyValueFactory<>("edad"));
        tcId.setCellValueFactory(new PropertyValueFactory<>("peso"));
        tcId.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        tcId.setCellValueFactory(new PropertyValueFactory<>("fecha_primera_consulta"));
        filtro=new FilteredList<AnimalModel>(listaTodas);
        tablaAnimales.setItems(listaTodas);

        addMenuNavigationhandlers();
    }

    private void addMenuNavigationhandlers() {
        // Asociar acciones a los elementos del menú
        miAniadirAnimal.setOnAction(this::aniadirAnimal);
        miBorrarAnimal.setOnAction(this::borrarAnimal);
        miEditarAnimal.setOnAction(this::editarAnimal);
        miInformacionAnimal.setOnAction(this::informacionAnimal);

        // Agregar un EventFilter al root de la escena
        Scene scene = stage.getScene();// Asumiendo que el MenuItem es parte de un Popup
        if (scene != null) {
            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (selectedMenuItem != null) {
                    if (event.getCode() == KeyCode.UP) {
                        navigateMenu(-1);
                    } else if (event.getCode() == KeyCode.DOWN) {
                        navigateMenu(1);
                    } else if (event.getCode() == KeyCode.ENTER) {
                        selectedMenuItem.fire(); // Ejecutar la acción del menú seleccionado
                    }
                }
            });
        }
    }

    private void navigateMenu(int direction) {
        // Obtener todos los elementos de menú en un array
        MenuItem[] menuItems = {
                miAniadirAnimal,
                miEditarAnimal,
                miBorrarAnimal,
                miInformacionAnimal,
        };

        // Encontrar el índice del elemento actualmente seleccionado
        int currentIndex = -1;
        for (int i = 0; i < menuItems.length; i++) {
            if (menuItems[i] == selectedMenuItem) {
                currentIndex = i;
                break;
            }
        }

        // Calcular el nuevo índice
        int newIndex = currentIndex + direction;

        // Ajustar el índice si está fuera de límites
        if (newIndex < 0) newIndex = menuItems.length - 1; // Volver al final si es el inicio
        else if (newIndex >= menuItems.length) newIndex = 0; // Volver al inicio si es el final

        // Cambiar la selección
        setSelectedMenuItem(menuItems[newIndex]);
    }

    private void setSelectedMenuItem(MenuItem menuItem) {
        if (selectedMenuItem != null) {
            selectedMenuItem.setStyle(""); // Limpiar el estilo del anterior
        }
        selectedMenuItem = menuItem;
        // Resalta el elemento seleccionado
        menuItem.setStyle("-fx-background-color: lightblue;");
    }

    private void informacionAeropuerto(Object o) {
        if (tablaAnimales.getSelectionModel().getSelectedItem() != null) {
            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setHeaderText(null);
            StringBuilder str = new StringBuilder();

            AnimalModel animal = tablaAnimales.getSelectionModel().getSelectedItem();
            str.append("ID: ").append(animal.getId()).append("\n");
            str.append("Nombre: ").append(animal.getNombre()).append("\n");
            str.append("Especie: ").append(animal.getEspecie()).append("\n");
            str.append("Raza: ").append(animal.getRaza()).append("\n");
            str.append("Sexo: ").append(animal.getSexo()).append("\n");
            str.append("Edad: ").append(animal.getEdad()).append(" años\n");
            str.append("Peso: ").append(animal.getPeso()).append(" kg\n");
            str.append("Observaciones: ").append(animal.getObservaciones()).append("\n");
            str.append("Fecha de primera consulta: ").append(animal.getFechaPrimeraConsulta()).append("\n");

            // Opción para mostrar si hay foto disponible
            if (animal.getFoto() != null) {
                str.append("Foto disponible\n");
            } else {
                str.append("Sin foto disponible\n");
            }

            al.setContentText(str.toString());
            al.showAndWait();
        }

    }

    private void filtrarPorNombre() {
        String filtroTexto = tfNombre.getText().toLowerCase();  // Convertir el texto del filtro a minúsculas para hacer una búsqueda no sensible a mayúsculas/minúsculas


        filtro.setPredicate(animal -> {
            if (filtroTexto == null || filtroTexto.isEmpty()) {
                return true;  // Muestra todos si el filtro está vacío
            }
            String nombreAnimal = animal.getNombre().toLowerCase();
            return nombreAnimal.contains(filtroTexto);  // Verifica si el nombre contiene el texto del filtro
        });
        tablaAnimales.setItems(filtro);
    }
    public static ObservableList<AnimalModel> getListaTodas() {
        return listaTodas;
    }

    public static Stage getS() {
        return s;
    }
    public static boolean isEsAniadir() {
        return esAniadir;
    }
}


