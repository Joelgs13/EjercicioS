package com.example.ejercicios.MODEL;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Clase que representa un modelo de animal.
 */
public class AnimalModel {
    private int id; // Identificador del animal
    private String nombre; // Nombre del animal
    private String especie; // Especie del animal
    private String raza; // Raza del animal
    private String sexo; // Sexo del animal
    private int edad; // Edad del animal
    private int peso; // Peso del animal
    private String observaciones; // Observaciones adicionales sobre el animal
    private LocalDate fechaPrimeraConsulta; // Fecha de la primera consulta
    private Blob foto; // Foto del animal

    /**
     * Constructor para crear una instancia de AnimalModel.
     *
     * @param id Identificador del animal.
     * @param nombre Nombre del animal.
     * @param especie Especie del animal.
     * @param raza Raza del animal.
     * @param sexo Sexo del animal.
     * @param edad Edad del animal.
     * @param peso Peso del animal.
     * @param observaciones Observaciones sobre el animal.
     * @param fechaPrimeraConsulta Fecha de la primera consulta.
     * @param foto Foto del animal en formato Blob.
     */
    public AnimalModel(int id, String nombre, String especie, String raza, String sexo, int edad, int peso, String observaciones, LocalDate fechaPrimeraConsulta, Blob foto) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.edad = edad;
        this.peso = peso;
        this.observaciones = observaciones;
        this.fechaPrimeraConsulta = fechaPrimeraConsulta;
        this.foto = foto;
    }

    // Métodos de acceso (getters y setters)

    /**
     * Obtiene el identificador del animal.
     *
     * @return El identificador del animal.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Establece el identificador del animal.
     *
     * @param id El nuevo identificador del animal.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del animal.
     *
     * @return El nombre del animal.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del animal.
     *
     * @param nombre El nuevo nombre del animal.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la especie del animal.
     *
     * @return La especie del animal.
     */
    public String getEspecie() {
        return especie;
    }

    /**
     * Establece la especie del animal.
     *
     * @param especie La nueva especie del animal.
     */
    public void setEspecie(String especie) {
        this.especie = especie;
    }

    /**
     * Obtiene la raza del animal.
     *
     * @return La raza del animal.
     */
    public String getRaza() {
        return raza;
    }

    /**
     * Establece la raza del animal.
     *
     * @param raza La nueva raza del animal.
     */
    public void setRaza(String raza) {
        this.raza = raza;
    }

    /**
     * Obtiene el sexo del animal.
     *
     * @return El sexo del animal.
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * Establece el sexo del animal.
     *
     * @param sexo El nuevo sexo del animal.
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    /**
     * Obtiene la edad del animal.
     *
     * @return La edad del animal.
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Establece la edad del animal.
     *
     * @param edad La nueva edad del animal.
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Obtiene el peso del animal.
     *
     * @return El peso del animal.
     */
    public int getPeso() {
        return peso;
    }

    /**
     * Establece el peso del animal.
     *
     * @param peso El nuevo peso del animal.
     */
    public void setPeso(int peso) {
        this.peso = peso;
    }

    /**
     * Obtiene las observaciones sobre el animal.
     *
     * @return Las observaciones sobre el animal.
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Establece las observaciones sobre el animal.
     *
     * @param observaciones Las nuevas observaciones sobre el animal.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Obtiene la fecha de la primera consulta del animal.
     *
     * @return La fecha de la primera consulta.
     */
    public LocalDate getFechaPrimeraConsulta() {
        return fechaPrimeraConsulta;
    }

    /**
     * Establece la fecha de la primera consulta del animal.
     *
     * @param fechaPrimeraConsulta La nueva fecha de la primera consulta.
     */
    public void setFechaPrimeraConsulta(LocalDate fechaPrimeraConsulta) {
        this.fechaPrimeraConsulta = fechaPrimeraConsulta;
    }

    /**
     * Obtiene la foto del animal.
     *
     * @return La foto del animal en formato Blob.
     */
    public Blob getFoto() {
        return foto;
    }

    /**
     * Establece la foto del animal.
     *
     * @param foto La nueva foto del animal en formato Blob.
     */
    public void setFoto(Blob foto) {
        this.foto = foto;
    }

    /**
     * Compara este objeto con otro para determinar si son iguales.
     *
     * @param o El objeto a comparar.
     * @return true si los objetos son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Verificar si son el mismo objeto
        if (o == null || getClass() != o.getClass()) return false; // Verificar si el objeto es nulo o de diferente clase
        AnimalModel animal = (AnimalModel) o; // Convertir el objeto a AnimalModel
        return id == animal.id; // Comparar los identificadores
    }

    /**
     * Devuelve un código hash para el objeto.
     *
     * @return Un código hash basado en el identificador del animal.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id); // Generar código hash usando el identificador
    }
}
