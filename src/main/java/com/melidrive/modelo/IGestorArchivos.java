package com.melidrive.modelo;

import java.io.File;
import java.util.List;

/**
 * Abstracción del gestor central de archivos (Facade del árbol de carpetas).
 *
 * Los controladores dependen de esta interfaz y no de su implementación
 * concreta ({@link GestorArchivos}), aplicando el principio de Inversión de
 * Dependencias (DIP): el código de alto nivel depende de abstracciones.
 */
public interface IGestorArchivos {

    DriveFile registrarArchivo(String nombre, String tipoMime, long sizeEnBytes, DriveFolder carpetaDestino);

    DriveFile importarArchivoFisico(File archivoOriginal, DriveFolder carpetaDestino);

    DriveFolder crearCarpeta(String nombre, DriveFolder carpetaDestino);

    List<DriveFile> buscarArchivosPorNombre(String terminoBusqueda);

    List<DriveFile> buscarArchivosPorEtiqueta(String nombreEtiqueta);

    boolean eliminarArchivo(DriveFile archivo, DriveFolder carpeta);

    boolean moverArchivo(DriveFile archivo, DriveFolder origen, DriveFolder destino);

    DriveFolder getCarpetaRaiz();

    List<Etiqueta> obtenerTodasLasEtiquetas();

    void purgarContenidoNoReal();
}
