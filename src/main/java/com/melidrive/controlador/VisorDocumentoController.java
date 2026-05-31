package com.melidrive.controlador;

import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.Etiqueta;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import com.melidrive.vista.DialogoNuevoEjercicio;

/**
 * Controlador encargado de la visualización de un documento seleccionado.
 * Maneja la lógica de mostrar los detalles del archivo, gestionar sus etiquetas
 * y coordinar la acción de volver al explorador.
 */
public class VisorDocumentoController {

    private MainController mainController;
    private DriveFile archivoAbierto;
    private int paginaActual = 0;
    private int totalPaginas = 0;

    /**
     * Constructor. Requiere el MainController para comunicarse con otras partes de la app.
     */
    public VisorDocumentoController(MainController mainController) {
        this.mainController = mainController;
        this.archivoAbierto = null;
        this.paginaActual = 0;
        this.totalPaginas = 0;
        System.out.println("VisorDocumentoController inicializado. Ningún archivo abierto.");
    }

    /**
     * Abre un archivo para visualización, cargando sus datos en el visor.
     */
    public void abrirDocumento(DriveFile archivo) {
        this.archivoAbierto = archivo;
        this.paginaActual = 0;
        this.totalPaginas = 0;
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
            mainController.mostrarExplorador();
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

    public int getPaginaActual() { return paginaActual; }
    public void setPaginaActual(int paginaActual) { this.paginaActual = paginaActual; }
    
    public int getTotalPaginas() { return totalPaginas; }
    public void setTotalPaginas(int totalPaginas) { this.totalPaginas = totalPaginas; }

    /**
     * Procesa la imagen recortada: abre el diálogo y, si se confirma, la guarda y crea la flashcard.
     */
    public void procesarRecorte(Image imagenRecortada) {
        if (imagenRecortada == null) return;
        
        DialogoNuevoEjercicio.ResultadoEjercicio resultado = DialogoNuevoEjercicio.mostrar(imagenRecortada);
        if (resultado != null) {
            try {
                Path dirDestino = Paths.get("data", "recortes");
                if (!Files.exists(dirDestino)) {
                    Files.createDirectories(dirDestino);
                }
                String idGen = UUID.randomUUID().toString();
                String nombreArchivo = "recorte_" + idGen.substring(0, 8) + ".png";
                File fileDestino = dirDestino.resolve(nombreArchivo).toFile();
                
                ImageIO.write(SwingFXUtils.fromFXImage(imagenRecortada, null), "png", fileDestino);
                
                // Registrar archivo en el Gestor de Archivos (en la carpeta raíz por defecto o en la actual)
                DriveFile recorteFile = mainController.getGestorArchivos()
                        .registrarArchivo(nombreArchivo, "image/png", fileDestino.length(), mainController.getGestorArchivos().getCarpetaRaiz());
                recorteFile.setRutaFisica(fileDestino.getAbsolutePath());
                
                // Crear Flashcard
                mainController.getFlashcardController().crearFlashcard(
                        "fc-" + idGen,
                        recorteFile,
                        resultado.titulo, // Para la UI puede usarse como pregunta
                        resultado.solucion
                );
                
                System.out.println("Ejercicio guardado exitosamente: " + resultado.titulo);
                
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Error al guardar el recorte: " + ex.getMessage());
            }
        }
    }
}
