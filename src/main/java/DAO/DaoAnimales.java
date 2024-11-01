package DAO;

import BBDD.ConexionBBDD;
import MODEL.AnimalModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class DaoAnimales {

    private static Connection conection;

    public static ObservableList<AnimalModel> selectTodos() {
        ObservableList<AnimalModel> animales = FXCollections.observableArrayList();
        try{
            conection = ConexionBBDD.getConnection();
            String consulta = "SELECT id,nombre,especie,raza,sexo,edad,peso,observaciones,fecha_primera_consulta,foto FROM Animales";
            PreparedStatement pstmt = conection.prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id_animal = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String especie = rs.getString("especie");
                String raza = rs.getString("raza");
                String sexo = rs.getString("sexo");
                int edad = rs.getInt("edad");
                int peso = rs.getInt("peso");
                String observaciones = rs.getString("observaciones");
                LocalDate fecha_primera_consulta = rs.getDate("fecha_primera_consulta").toLocalDate();
                Blob foto = rs.getBlob("foto");
                AnimalModel animal = new AnimalModel(id_animal,nombre,especie,raza,sexo,edad,peso,observaciones,fecha_primera_consulta,foto);
                animales.add(animal);
            }
            rs.close();
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return animales;
    }


    public static AnimalModel selectPorId(int id) {
        AnimalModel animal = null;
        try {
            conection = ConexionBBDD.getConnection();
            String select = "SELECT id,nombre,especie,raza,sexo,edad,peso,observaciones,fecha_primera_consulta,foto FROM Animales WHERE id = ?";
            PreparedStatement pstmt = conection.prepareStatement(select);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_animal = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String especie = rs.getString("especie");
                String raza = rs.getString("raza");
                String sexo = rs.getString("sexo");
                int edad = rs.getInt("edad");
                int peso = rs.getInt("peso");
                String observaciones = rs.getString("observaciones");
                LocalDate fecha_primera_consulta = rs.getDate("fecha_primera_consulta").toLocalDate();
                Blob foto = rs.getBlob("foto");
                animal = new AnimalModel(id_animal,nombre,especie,raza,sexo,edad,peso,observaciones,fecha_primera_consulta,foto);
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return animal;
    }

    public static ObservableList<AnimalModel> cargarLista(){
        ObservableList<AnimalModel> lst = FXCollections.observableArrayList();
        try {
            conection = ConexionBBDD.getConnection();
            String select = "SELECT id,nombre,especie,raza,sexo,edad,peso,observaciones,fecha_primera_consulta,foto FROM ejercicios.Animales";
            PreparedStatement pstmt = ConexionBBDD.getConnection().prepareStatement(select);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                AnimalModel modelo = new AnimalModel(rs.getInt("id"),rs.getString("nombre"),rs.getString("especie"),rs.getString("raza"),rs.getString("sexo"),rs.getInt("edad"),rs.getInt("peso"),rs.getString("observaciones"),rs.getDate("fecha_primera_consulta").toLocalDate(),rs.getBlob("foto"));
                modelo.setId(rs.getInt("id"));
                lst.add(modelo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lst;
    }

    public static boolean modificarAnimal(AnimalModel animal, AnimalModel animalNuevo) {
        ConexionBBDD connection;
        PreparedStatement pstmt;
        try {
            connection = new ConexionBBDD();

            String consulta = "UPDATE Animales SET nombre = ?, especie = ?, raza = ?, sexo = ?, edad = ?, peso = ?, observaciones = ?, fecha_primera_consulta = ?, foto = ? WHERE id = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);

            // Configuración de parámetros con verificaciones de null
            pstmt.setString(1, animalNuevo.getNombre() != null ? animalNuevo.getNombre() : "");
            pstmt.setString(2, animalNuevo.getEspecie() != null ? animalNuevo.getEspecie() : "");
            pstmt.setString(3, animalNuevo.getRaza() != null ? animalNuevo.getRaza() : "");
            pstmt.setString(4, animalNuevo.getSexo() != null ? animalNuevo.getSexo() : "");

            Integer animalNuevoEdad= animalNuevo.getEdad();
            Integer animalNuevoPeso= animalNuevo.getEdad();

            // Usar el valor o un predeterminado si es null
            pstmt.setInt(5, animalNuevoEdad != null ? animalNuevo.getEdad() : 0);  // edad predeterminada 0
            pstmt.setInt(6, animalNuevoPeso != null ? animalNuevo.getPeso() : 0);  // peso predeterminado 0

            pstmt.setString(7, animalNuevo.getObservaciones() != null ? animalNuevo.getObservaciones() : "");
            pstmt.setDate(8, animalNuevo.getFechaPrimeraConsulta() != null ? Date.valueOf(animalNuevo.getFechaPrimeraConsulta()) : null);
            pstmt.setBlob(9, animalNuevo.getFoto() != null ? animalNuevo.getFoto() : null);

            // ID en WHERE
            pstmt.setInt(10, animal.getId()); // Suponiendo que getId() siempre devuelve un valor válido de int

            // Ejecutar la actualización
            int filasAfectadas = pstmt.executeUpdate();

            // Cerrar recursos
            pstmt.close();
            connection.closeConnection();

            // Retornar verdadero si hubo filas afectadas
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }




    public  static int insertarAnimal(AnimalModel animal) {
        ConexionBBDD connection;
        PreparedStatement pstmt;
        try {
            connection = new ConexionBBDD();
            String consulta = "INSERT INTO Animales (nombre,especie,raza,sexo,edad,peso,observaciones,fecha_primera_consulta,foto) VALUES (?,?,?,?,?,?,?,?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, animal.getNombre());
            pstmt.setString(2, animal.getEspecie());
            pstmt.setString(3, animal.getRaza());
            pstmt.setString(4, animal.getSexo());
            pstmt.setInt(5, animal.getEdad());
            pstmt.setInt(6, animal.getPeso());
            pstmt.setString(7, animal.getObservaciones());
            pstmt.setDate(8, Date.valueOf(animal.getFechaPrimeraConsulta()));
            pstmt.setBlob(9, animal.getFoto());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Nueva entrada en animal");
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    pstmt.close();
                    connection.closeConnection();
                    return id;
                }
            }
            pstmt.close();
            connection.closeConnection();
            return -1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    public static boolean eliminarAnimal(AnimalModel animal) {
        ConexionBBDD connection;
        PreparedStatement pstmt;
        try {
            connection = new ConexionBBDD();
            String consulta = "DELETE FROM Animales WHERE id = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, animal.getId());  // Usamos el id del objeto AnimalModel para eliminar el registro

            int filasAfectadas = pstmt.executeUpdate(); // Ejecuta la consulta de eliminación
            pstmt.close();
            connection.closeConnection();

            return filasAfectadas > 0; // Retorna true si se eliminó alguna fila, false si no
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }



}
