package com.example.ejercicios;

import BBDD.ConexionBBDD;
import DAO.DaoAnimales;
import MODEL.AnimalModel;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.example.ejercicios.HelloApplication.stage;

/**
 * Controlador de la vista que muestra y gestiona la lista de animales.
 */
public class ListaDeAnimalesController {

    @FXML
    public TableView<AnimalModel> tablaAnimales; // Tabla que muestra la lista de animales.

    @FXML
    private MenuItem miAniadirAnimal; // Opción del menú para añadir un nuevo animal.

    @FXML
    private MenuItem miBorrarAnimal; // Opción del menú para borrar un animal.

    @FXML
    private MenuItem miEditarAnimal; // Opción del menú para editar un animal.

    @FXML
    private MenuItem miInformacionAnimal; // Opción del menú para mostrar información de un animal.

    @FXML
    private MenuItem miInformacionAnimal1; // Otra opción del menú para mostrar información.

    @FXML
    private TableColumn<AnimalModel, Integer> tcEdad; // Columna de la tabla que muestra la edad del animal.

    @FXML
    private TableColumn<AnimalModel, String> tcEspecie; // Columna de la tabla que muestra la especie del animal.

    @FXML
    private TableColumn<AnimalModel, LocalDate> tcFecha; // Columna de la tabla que muestra la fecha de la primera consulta.

    @FXML
    private TableColumn<AnimalModel, Integer> tcId; // Columna de la tabla que muestra el ID del animal.

    @FXML
    private TableColumn<AnimalModel, String> tcNombre; // Columna de la tabla que muestra el nombre del animal.

    @FXML
    private TableColumn<AnimalModel, String> tcObservaciones; // Columna de la tabla que muestra observaciones del animal.

    @FXML
    private TableColumn<AnimalModel, Integer> tcPeso; // Columna de la tabla que muestra el peso del animal.

    @FXML
    private TableColumn<AnimalModel, String> tcRaza; // Columna de la tabla que muestra la raza del animal.

    @FXML
    private TableColumn<AnimalModel, String> tcSexo; // Columna de la tabla que muestra el sexo del animal.

    @FXML
    private TextField tfNombre; // Campo de texto para filtrar animales por nombre.

    private FilteredList<AnimalModel> filtro; // Lista filtrada de animales.
    private static ObservableList<AnimalModel> listaTodas; // Lista de todos los animales.
    private MenuItem selectedMenuItem; // Elemento del menú actualmente seleccionado.
    private static Stage s; // Ventana para añadir o editar animales.
    private static boolean esAniadir; // Bandera para determinar si se está añadiendo un animal.
    private static boolean borrar = true; // Bandera para habilitar o deshabilitar la opción de borrar.

    /**
     * Método que se llama al hacer clic en la opción para añadir un animal.
     *
     * @param event El evento de acción que se produce al hacer clic.
     */
    @FXML
    void aniadirAnimal(ActionEvent event) {
        esAniadir = true;
        s = new Stage();
        Scene scene;
        try {
            FXMLLoader controlador = new FXMLLoader(HelloApplication.class.getResource("aniadirEditarAnimal.fxml"));
            scene = new Scene(controlador.load());
            s.setTitle("AÑADIR ANIMAL");
            s.setScene(scene);
            AniadirEditarAnimalController controller = controlador.getController();
            controller.setTablaAnimales(tablaAnimales);
        } catch (IOException e) {
            e.printStackTrace();
        }
        s.setResizable(false);
        s.initOwner(HelloApplication.getStage());
        s.initModality(javafx.stage.Modality.WINDOW_MODAL);
        s.showAndWait();
        filtrarPorNombre();
        tablaAnimales.refresh();
        initialize();
    }

    /**
     * Método que se llama al hacer clic en la opción para borrar un animal.
     *
     * @param event El evento de acción que se produce al hacer clic.
     */
    @FXML
    void borrarAnimal(ActionEvent event) {
        // Verificar si hay un animal seleccionado en la tabla
        AnimalModel animalSeleccionado = tablaAnimales.getSelectionModel().getSelectedItem();
        if (animalSeleccionado == null) {
            showAlert("Advertencia", "Debe seleccionar un animal para borrar.", Alert.AlertType.WARNING);
            return;
        }

        // Confirmación de eliminación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de que desea eliminar el animal seleccionado?");

        // Espera la respuesta del usuario
        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            boolean eliminado = DaoAnimales.eliminarAnimal(animalSeleccionado);

            if (eliminado) {
                // Si se eliminó de la BBDD, quitar de la tabla y mostrar mensaje de éxito
                tablaAnimales.getItems().remove(animalSeleccionado);
                showAlert("Éxito", "El animal ha sido eliminado correctamente.", Alert.AlertType.INFORMATION);
            } else {
                // Si no se pudo eliminar, mostrar mensaje de error
                showAlert("Error", "No se pudo eliminar el animal. Inténtelo nuevamente.", Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Método que se llama al hacer clic en la opción para editar un animal.
     *
     * @param event El evento de acción que se produce al hacer clic.
     */
    @FXML
    void editarAnimal(ActionEvent event) {
        esAniadir = false; // Modo edición
        AnimalModel animalSeleccionado = tablaAnimales.getSelectionModel().getSelectedItem();

        if (animalSeleccionado != null) {
            s = new Stage();
            Scene scene;
            try {
                FXMLLoader controlador = new FXMLLoader(HelloApplication.class.getResource("aniadirEditarAnimal.fxml"));
                scene = new Scene(controlador.load());
                s.setTitle("EDITAR ANIMAL");
                s.setScene(scene);

                // Pasar el animal seleccionado al controlador de edición
                AniadirEditarAnimalController controller = controlador.getController();
                controller.setTablaAnimales(tablaAnimales);
                controller.cargarAnimal(animalSeleccionado); // Cargar datos en el formulario

            } catch (IOException e) {
                e.printStackTrace();
            }

            s.setResizable(false);
            s.initOwner(HelloApplication.getStage());
            s.initModality(Modality.WINDOW_MODAL);
            s.showAndWait();
            filtrarPorNombre();
            tablaAnimales.refresh();
        } else {
            showAlert("Error", "Ningún animal seleccionado.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Método que se llama al inicializar el controlador. Configura la tabla y los eventos.
     */
    @FXML
    public void initialize() {
        //System.out.println("iniciando...");
        try {
            ConexionBBDD con = new ConexionBBDD();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tfNombre.setOnKeyReleased(event -> filtrarPorNombre());

        // Configurar el evento de doble clic en la tabla
        tablaAnimales.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Doble clic
                informacionAnimal(null); // Llamamos a la función con un evento nulo
            }
        });

        //Tabla
        //System.out.println("antes de cargar");
        listaTodas = DaoAnimales.cargarLista();
        /*for (AnimalModel am : listaTodas) {
            System.out.println(am);
        }
        System.out.println(listaTodas.size());*/
        tablaAnimales.setItems(listaTodas);
        tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        tcRaza.setCellValueFactory(new PropertyValueFactory<>("raza"));
        tcSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        tcEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        tcPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        tcObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        tcFecha.setCellValueFactory(new PropertyValueFactory<>("fechaPrimeraConsulta"));
        filtro = new FilteredList<AnimalModel>(listaTodas);
        tablaAnimales.setItems(listaTodas);

        addMenuNavigationhandlers();
    }

    /**
     * Método para asociar las acciones de los elementos del menú.
     */
    private void addMenuNavigationhandlers() {
        // Asociar acciones a los elementos del menú
        miAniadirAnimal.setOnAction(this::aniadirAnimal);
        miBorrarAnimal.setOnAction(this::borrarAnimal);
        miEditarAnimal.setOnAction(this::editarAnimal);
        miInformacionAnimal.setOnAction(this::informacionAnimal);

        // Agregar un EventFilter al root de la escena
        Scene scene = stage.getScene(); // Asumiendo que el MenuItem es parte de un Popup
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

    /**
     * Método para navegar por los elementos del menú usando las teclas de dirección.
     *
     * @param direction La dirección en la que navegar (-1 para arriba, 1 para abajo).
     */
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

    /**
     * Método para establecer el elemento del menú seleccionado y resaltar su apariencia.
     *
     * @param menuItem El elemento del menú a seleccionar.
     */
    private void setSelectedMenuItem(MenuItem menuItem) {
        if (selectedMenuItem != null) {
            selectedMenuItem.setStyle(""); // Limpiar el estilo del anterior
        }
        selectedMenuItem = menuItem;
        // Resalta el elemento seleccionado
        menuItem.setStyle("-fx-background-color: lightblue;");
    }

    /**
     * Método que muestra la información del animal seleccionado en un cuadro de diálogo.
     *
     * @param event El evento de acción que se produce al hacer clic.
     */
    @FXML
    private void informacionAnimal(ActionEvent event) {
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

    /**
     * Método para filtrar la lista de animales según el texto ingresado en el campo de nombre.
     */
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

    /**
     * Método para obtener la lista completa de animales.
     *
     * @return ObservableList de AnimalModel con todos los animales.
     */
    public static ObservableList<AnimalModel> getListaTodas() {
        return listaTodas;
    }

    /**
     * Método para obtener la instancia de la ventana de añadir o editar animales.
     *
     * @return La ventana actual (Stage).
     */
    public static Stage getS() {
        return s;
    }

    /**
     * Método para verificar si se está en modo añadir.
     *
     * @return true si se está añadiendo un animal, false de lo contrario.
     */
    public static boolean isEsAniadir() {
        return esAniadir;
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
}
