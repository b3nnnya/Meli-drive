package com.melidrive.modelo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
     * Constructor que reutiliza una carpeta raíz ya existente.
     * Se usa al restaurar el árbol de carpetas desde disco (persistencia).
     */
    public GestorArchivos(DriveFolder carpetaRaiz) {
        this.carpetaRaiz = (carpetaRaiz != null) ? carpetaRaiz : new DriveFolder("root", "Mi Unidad");
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
     * Importa un archivo físico al sistema, copiándolo a una carpeta local y registrándolo.
     */
    public DriveFile importarArchivoFisico(File archivoOriginal, DriveFolder carpetaDestino) {
        try {
            Path dirDestino = Paths.get("data", "archivos");
            if (!Files.exists(dirDestino)) {
                Files.createDirectories(dirDestino);
            }
            
            String nombreArchivo = archivoOriginal.getName();
            String idGenerado = UUID.randomUUID().toString();
            // Para evitar colisiones, usamos un prefijo con el ID
            String nombreUnico = idGenerado.substring(0, 8) + "_" + nombreArchivo;
            Path pathDestino = dirDestino.resolve(nombreUnico);
            
            Files.copy(archivoOriginal.toPath(), pathDestino, StandardCopyOption.REPLACE_EXISTING);
            
            // Determinar tipo Mime básico
            String tipoMime = "application/octet-stream";
            if (nombreArchivo.toLowerCase().endsWith(".pdf")) tipoMime = "application/pdf";
            else if (nombreArchivo.toLowerCase().endsWith(".png")) tipoMime = "image/png";
            else if (nombreArchivo.toLowerCase().endsWith(".jpg") || nombreArchivo.toLowerCase().endsWith(".jpeg")) tipoMime = "image/jpeg";
            
            DriveFile nuevoArchivo = new DriveFile(idGenerado, nombreArchivo, tipoMime, archivoOriginal.length());
            nuevoArchivo.setRutaFisica(pathDestino.toAbsolutePath().toString());
            
            carpetaDestino.agregarArchivo(nuevoArchivo);
            System.out.println("Archivo físico importado: " + nombreArchivo);
            return nuevoArchivo;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al importar el archivo físico: " + e.getMessage());
            return null;
        }
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

    /**
     * Recolecta todas las etiquetas existentes (sin repetir por nombre)
     * recorriendo recursivamente todo el árbol de carpetas.
     */
    public List<Etiqueta> obtenerTodasLasEtiquetas() {
        List<Etiqueta> resultado = new ArrayList<>();
        Set<String> nombresVistos = new HashSet<>();
        recolectarEtiquetas(carpetaRaiz, resultado, nombresVistos);
        return resultado;
    }

    private void recolectarEtiquetas(DriveFolder carpeta, List<Etiqueta> acumulador, Set<String> nombresVistos) {
        for (DriveFile archivo : carpeta.getArchivos()) {
            if (archivo.getEtiquetas() != null) {
                for (Etiqueta etiqueta : archivo.getEtiquetas()) {
                    if (nombresVistos.add(etiqueta.getNombre().toLowerCase())) {
                        acumulador.add(etiqueta);
                    }
                }
            }
        }
        for (DriveFolder sub : carpeta.getSubcarpetas()) {
            recolectarEtiquetas(sub, acumulador, nombresVistos);
        }
    }

    /**
     * Elimina del árbol todo el contenido que no existe en realidad:
     * archivos sin respaldo físico (rutaFisica nula o inexistente) y las
     * carpetas que, tras quitar esos archivos, queden completamente vacías.
     * Conserva los archivos que sí apuntan a un archivo real en disco.
     */
    public void purgarContenidoNoReal() {
        purgarRecursivo(carpetaRaiz);
    }

    private void purgarRecursivo(DriveFolder carpeta) {
        carpeta.getArchivos().removeIf(archivo ->
                archivo.getRutaFisica() == null || !new File(archivo.getRutaFisica()).exists());

        for (DriveFolder sub : carpeta.getSubcarpetas()) {
            purgarRecursivo(sub);
        }

        carpeta.getSubcarpetas().removeIf(sub ->
                sub.getArchivos().isEmpty() && sub.getSubcarpetas().isEmpty());
    }
}
