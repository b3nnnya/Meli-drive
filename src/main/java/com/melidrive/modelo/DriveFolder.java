package com.melidrive.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una Carpeta dentro de Meli-Drive.
 * Puede contener tanto archivos (DriveFile) como subcarpetas (DriveFolder).
 * Hereda de {@link ElementoDrive} la identidad común (id y nombre) y aplica
 * el patrón de diseño "Composite".
 */
public class DriveFolder extends ElementoDrive {

    private static final long serialVersionUID = 2L;

    // id y nombre ahora viven en la superclase ElementoDrive (herencia).
    private List<DriveFile> archivos;
    private List<DriveFolder> subcarpetas;

    public DriveFolder(String id, String nombre) {
        super(id, nombre); // delega la identidad (id/nombre) a la superclase
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

    // getId/setId/getNombre/setNombre se heredan de ElementoDrive (encapsulamiento común).

    public List<DriveFile> getArchivos() {
        return archivos;
    }

    public List<DriveFolder> getSubcarpetas() {
        return subcarpetas;
    }

    /**
     * Cuenta el total de archivos en esta carpeta y todas sus subcarpetas.
     * @return el número total de archivos recursivamente
     */
    public int contarArchivosRecursivo() {
        int total = archivos.size();
        for (DriveFolder subcarpeta : subcarpetas) {
            total += subcarpeta.contarArchivosRecursivo();
        }
        return total;
    }

    /**
     * Busca una subcarpeta por nombre dentro de esta carpeta.
     * @param nombre el nombre de la subcarpeta a buscar
     * @return la DriveFolder encontrada o null si no existe
     */
    public DriveFolder buscarSubcarpetaPorNombre(String nombre) {
        if (nombre == null) return null;
        for (DriveFolder subcarpeta : subcarpetas) {
            if (subcarpeta.getNombre().equalsIgnoreCase(nombre)) {
                return subcarpeta;
            }
        }
        return null;
    }

    /** Una carpeta siempre es carpeta (polimorfismo desde ElementoDrive). */
    @Override
    public boolean esCarpeta() {
        return true;
    }

    /**
     * Tamaño total de la carpeta: la suma recursiva del tamaño de los archivos
     * que contiene y de los de todas sus subcarpetas.
     */
    @Override
    public long getTamanioTotal() {
        long total = 0;
        for (DriveFile archivo : archivos) {
            total += archivo.getTamanioTotal();
        }
        for (DriveFolder subcarpeta : subcarpetas) {
            total += subcarpeta.getTamanioTotal();
        }
        return total;
    }

    /**
     * Representación en texto de la carpeta para depuración.
     */
    @Override
    public String toString() {
        return "DriveFolder{nombre='" + nombre + "', archivos=" + archivos.size()
                + ", subcarpetas=" + subcarpetas.size() + "}";
    }
}
