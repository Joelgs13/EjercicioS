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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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
        esAniadir=true;
        s=new Stage();
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

    @FXML
    void borrarAnimal(ActionEvent event) {

    }

    @FXML
    void editarAnimal(ActionEvent event) {
        try {
            // Verificar si hay un animal seleccionado
            AnimalModel animalSeleccionado = tablaAnimales.getSelectionModel().getSelectedItem();
            if (animalSeleccionado == null) {
                showAlert("Advertencia", "Debe seleccionar un animal de la lista para editar.", Alert.AlertType.INFORMATION);
                return;
            }

            // Cargar el FXML de la pantalla de edición
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AniadirEditarAnimal.fxml"));
            Parent root = loader.load();

            // Configurar la ventana de edición como modal
            Stage stage = new Stage();
            stage.setTitle("Editar Animal");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            // Obtener el controlador de la ventana de edición y pasarle los datos actuales del animal
            AniadirEditarAnimalController controlador = loader.getController();
            controlador.setTablaAnimales(tablaAnimales);

            // Llenar los campos del formulario con los datos actuales del animal
            controlador.getTfNombre().setText(animalSeleccionado.getNombre());
            controlador.getTfEspecie().setText(animalSeleccionado.getEspecie());
            controlador.getTfRaza().setText(animalSeleccionado.getRaza());
            controlador.getTfSexo().setText(animalSeleccionado.getSexo());
            controlador.getTfEdad().setText(String.valueOf(animalSeleccionado.getEdad()));
            controlador.getTfPeso().setText(String.valueOf(animalSeleccionado.getPeso()));
            controlador.getTfObservaciones().setText(animalSeleccionado.getObservaciones());
            controlador.getFechaPrimeraConsulta().setValue(animalSeleccionado.getFechaPrimeraConsulta());
            controlador.setImagenBlob(animalSeleccionado.getFoto()); // Establece el Blob de imagen actual en el controlador

            // Mostrar la ventana de edición
            stage.showAndWait();

            // Crear un nuevo objeto `animalModificado` con los datos actualizados desde el formulario
            AnimalModel animalModificado = new AnimalModel(
                    animalSeleccionado.getId(),
                    controlador.getTfNombre().getText(),
                    controlador.getTfEspecie().getText(),
                    controlador.getTfRaza().getText(),
                    controlador.getTfSexo().getText(),
                    Integer.parseInt(controlador.getTfEdad().getText()),
                    Integer.parseInt(controlador.getTfPeso().getText()),
                    controlador.getTfObservaciones().getText(),
                    controlador.getFechaPrimeraConsulta().getValue(),
                    controlador.getImagenBlob()
            );

            // Validar que haya habido alguna modificación
            if (animalSeleccionado.equals(animalModificado)) {
                showAlert("Sin cambios", "No se realizaron modificaciones en el animal seleccionado.", Alert.AlertType.INFORMATION);
                return;
            }

            // Intentar actualizar el animal en la base de datos
            boolean actualizado = DaoAnimales.modificarAnimal(animalSeleccionado, animalModificado);

            if (actualizado) {
                // Actualizar los campos del objeto original para que se reflejen en la tabla
                animalSeleccionado.setNombre(animalModificado.getNombre());
                animalSeleccionado.setEspecie(animalModificado.getEspecie());
                animalSeleccionado.setRaza(animalModificado.getRaza());
                animalSeleccionado.setSexo(animalModificado.getSexo());
                animalSeleccionado.setEdad(animalModificado.getEdad());
                animalSeleccionado.setPeso(animalModificado.getPeso());
                animalSeleccionado.setObservaciones(animalModificado.getObservaciones());
                animalSeleccionado.setFechaPrimeraConsulta(animalModificado.getFechaPrimeraConsulta());
                animalSeleccionado.setFoto(animalModificado.getFoto());

                showAlert("Éxito", "El animal ha sido actualizado correctamente.", Alert.AlertType.INFORMATION);
                tablaAnimales.refresh(); // Refrescar la tabla para mostrar los cambios
            } else {
                showAlert("Error", "No se pudo actualizar el animal. Inténtelo nuevamente.", Alert.AlertType.ERROR);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Ocurrió un error al intentar cargar la ventana de edición.", Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            showAlert("Error", "Los campos de edad y peso deben ser números válidos.", Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void initialize() {
        //System.out.println("iniciando...");
        try {
            ConexionBBDD con=new ConexionBBDD();
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
        listaTodas= DaoAnimales.cargarLista();
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

    // Método para mostrar alertas
    private void showAlert(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}


