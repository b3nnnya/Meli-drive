package com.melidrive.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Gestor Central de Archivos que administra el sistema de carpetas
 * a partir de un directorio raíz ("Root").
 * Aquí aplicamos el concepto de "Fachada" para simplificar la creación y búsqueda.
 */
public class GestorArchivos {

    private DriveFolder carpetaRaiz;

    public GestorArchivos() {
        // Al instanciar el gestor, se crea automáticamente la carpeta principal
        this.carpetaRaiz = new DriveFolder("root", "Mi Unidad");
    }

    /**
     * Registra un nuevo archivo lógico dentro de una carpeta específica.
     */
    public DriveFile registrarArchivo(String nombre, String tipoMime, long sizeEnBytes, DriveFolder carpetaDestino) {
        String idGenerado = UUID.randomUUID().toString();
        DriveFile nuevoArchivo = new DriveFile(idGenerado, nombre, tipoMime, sizeEnBytes);
        
        carpetaDestino.agregarArchivo(nuevoArchivo);
        return nuevoArchivo;
    }

    /**
     * Crea una nueva subcarpeta dentro de una carpeta existente.
     */
    public DriveFolder crearCarpeta(String nombre, DriveFolder carpetaDestino) {
        String idGenerado = "folder-" + UUID.randomUUID().toString();
        DriveFolder nuevaCarpeta = new DriveFolder(idGenerado, nombre);
        
        carpetaDestino.agregarSubcarpeta(nuevaCarpeta);
        return nuevaCarpeta;
    }

    /**
     * Búsqueda recursiva de archivos por nombre en todo el árbol de carpetas.
     */
    public List<DriveFile> buscarArchivosPorNombre(String terminoBusqueda) {
        List<DriveFile> resultados = new ArrayList<>();
        buscarRecursivo(carpetaRaiz, terminoBusqueda.toLowerCase(), resultados);
        return resultados;
    }

    private void buscarRecursivo(DriveFolder actual, String termino, List<DriveFile> resultados) {
        // Buscar en archivos de esta carpeta
        for (DriveFile archivo : actual.getArchivos()) {
            if (archivo.getNombre().toLowerCase().contains(termino)) {
                resultados.add(archivo);
            }
        }
        
        // Entrar recursivamente en subcarpetas
        for (DriveFolder subcarpeta : actual.getSubcarpetas()) {
            buscarRecursivo(subcarpeta, termino, resultados);
        }
    }

    /**
     * Búsqueda recursiva de archivos que tengan una etiqueta con el nombre dado.
     * @param nombreEtiqueta nombre de la etiqueta a buscar
     * @return lista de archivos que contienen esa etiqueta
     */
    public List<DriveFile> buscarArchivosPorEtiqueta(String nombreEtiqueta) {
        List<DriveFile> resultados = new ArrayList<>();
        buscarPorEtiquetaRecursivo(carpetaRaiz, nombreEtiqueta.toLowerCase(), resultados);
        return resultados;
    }

    private void buscarPorEtiquetaRecursivo(DriveFolder actual, String termino, List<DriveFile> resultados) {
        for (DriveFile archivo : actual.getArchivos()) {
            if (archivo.getEtiquetas() != null) {
                for (Etiqueta etiqueta : archivo.getEtiquetas()) {
                    if (etiqueta.getNombre().toLowerCase().contains(termino)) {
                        resultados.add(archivo);
                        break;
                    }
                }
            }
        }
        for (DriveFolder subcarpeta : actual.getSubcarpetas()) {
            buscarPorEtiquetaRecursivo(subcarpeta, termino, resultados);
        }
    }

    /**
     * Elimina un archivo de una carpeta específica.
     * @param archivo el archivo a eliminar
     * @param carpeta la carpeta de donde se elimina
     * @return true si se eliminó exitosamente, false si no se encontró
     */
    public boolean eliminarArchivo(DriveFile archivo, DriveFolder carpeta) {
        if (archivo != null && carpeta != null) {
            boolean eliminado = carpeta.getArchivos().remove(archivo);
            if (eliminado) {
                System.out.println("Archivo eliminado: " + archivo.getNombre());
            }
            return eliminado;
        }
        return false;
    }

    /**
     * Mueve un archivo de una carpeta origen a una carpeta destino.
     * @param archivo el archivo a mover
     * @param origen la carpeta donde se encuentra actualmente
     * @param destino la carpeta a donde se moverá
     * @return true si se movió exitosamente
     */
    public boolean moverArchivo(DriveFile archivo, DriveFolder origen, DriveFolder destino) {
        if (archivo == null || origen == null || destino == null) return false;

        if (origen.getArchivos().remove(archivo)) {
            destino.agregarArchivo(archivo);
            System.out.println("Archivo '" + archivo.getNombre()
                    + "' movido de '" + origen.getNombre()
                    + "' a '" + destino.getNombre() + "'");
            return true;
        }
        return false;
    }

    public DriveFolder getCarpetaRaiz() {
        return carpetaRaiz;
    }
}
