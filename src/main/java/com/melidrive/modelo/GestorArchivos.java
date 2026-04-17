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

    public DriveFolder getCarpetaRaiz() {
        return carpetaRaiz;
    }
}
