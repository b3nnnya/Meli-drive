package com.melidrive.controlador;

import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.Etiqueta;

/**
 * Controlador encargado de la visualización de un documento seleccionado.
 * Maneja la lógica de mostrar los detalles del archivo, gestionar sus etiquetas
 * y coordinar la acción de volver al explorador.
 */
public class VisorDocumentoController {

    private MainController mainController;
    private DriveFile archivoAbierto;

    /**
     * Constructor. Requiere el MainController para comunicarse con otras partes de la app.
     */
    public VisorDocumentoController(MainController mainController) {
        this.mainController = mainController;
        this.archivoAbierto = null;
        System.out.println("VisorDocumentoController inicializado. Ningún archivo abierto.");
    }

    /**
     * Abre un archivo para visualización, cargando sus datos en el visor.
     */
    public void abrirDocumento(DriveFile archivo) {
        this.archivoAbierto = archivo;
        System.out.println("Documento abierto: " + archivo.getNombre()
                + " | Tipo: " + archivo.getTipoMime()
                + " | Tamaño: " + archivo.getSizeEnBytes() + " bytes");
        // TODO: Notificar a la Vista para renderizar el contenido del documento
    }

    /**
     * Cierra el documento actual y regresa al explorador de archivos.
     */
    public void cerrarDocumento() {
        if (archivoAbierto != null) {
            System.out.println("Cerrando documento: " + archivoAbierto.getNombre());
            this.archivoAbierto = null;
            // TODO: Notificar al MainController para volver a la vista del Explorador
        }
    }

    /**
     * Agrega una etiqueta al documento actualmente abierto.
     */
    public void agregarEtiqueta(Etiqueta etiqueta) {
        if (archivoAbierto != null) {
            archivoAbierto.agregarEtiqueta(etiqueta);
            System.out.println("Etiqueta '" + etiqueta.getNombre()
                    + "' agregada a " + archivoAbierto.getNombre());
            // TODO: Notificar a la Vista para actualizar la lista de etiquetas
        }
    }

    /**
     * Elimina una etiqueta del documento actualmente abierto.
     */
    public void eliminarEtiqueta(Etiqueta etiqueta) {
        if (archivoAbierto != null) {
            archivoAbierto.eliminarEtiqueta(etiqueta);
            System.out.println("Etiqueta '" + etiqueta.getNombre()
                    + "' eliminada de " + archivoAbierto.getNombre());
            // TODO: Notificar a la Vista para actualizar la lista de etiquetas
        }
    }

    /**
     * Devuelve el archivo actualmente abierto en el visor.
     * Retorna null si no hay ningún documento abierto.
     */
    public DriveFile getArchivoAbierto() {
        return archivoAbierto;
    }

    /**
     * Verifica si hay un documento abierto actualmente.
     */
    public boolean hayDocumentoAbierto() {
        return archivoAbierto != null;
    }

    /**
     * Devuelve la referencia al controlador principal.
     */
    public MainController getMainController() {
        return mainController;
    }
}
