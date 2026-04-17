package com.melidrive.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una Carpeta dentro de Meli-Drive.
 * Puede contener tanto archivos (DriveFile) como subcarpetas (DriveFolder).
 * Implementado usando el patrón de diseño "Composite".
 */
public class DriveFolder {
    
    private String id;
    private String nombre;
    private List<DriveFile> archivos;
    private List<DriveFolder> subcarpetas;

    public DriveFolder(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        // Inicializamos las listas para evitar NullPointerException
        this.archivos = new ArrayList<>();
        this.subcarpetas = new ArrayList<>();
    }

    /* 
     * ==========================================
     * METODOS DE GESTIÓN DE CONTENIDO
     * ==========================================
     */

    public void agregarArchivo(DriveFile archivo) {
        this.archivos.add(archivo);
    }

    public void eliminarArchivo(DriveFile archivo) {
        this.archivos.remove(archivo);
    }

    public void agregarSubcarpeta(DriveFolder carpeta) {
        this.subcarpetas.add(carpeta);
    }

    public void eliminarSubcarpeta(DriveFolder carpeta) {
        this.subcarpetas.remove(carpeta);
    }

    /* 
     * ==========================================
     * GETTERS Y SETTERS
     * ==========================================
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<DriveFile> getArchivos() {
        return archivos;
    }

    public List<DriveFolder> getSubcarpetas() {
        return subcarpetas;
    }
}
