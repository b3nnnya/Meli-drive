package com.melidrive.controlador;

import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.DriveFolder;
import com.melidrive.modelo.GestorArchivos;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Controlador encargado de la navegación y gestión de archivos.
 * Maneja la lógica de entrar a carpetas, crear nuevas, y seleccionar archivos.
 * Utiliza una pila de historial para la navegación hacia atrás.
 */
public class ExploradorController {

    private GestorArchivos gestorArchivos;
    private DriveFolder carpetaActual;
    private Deque<DriveFolder> historial;

    private MainController mainController;

    /**
     * Constructor. Requiere el MainController para obtener el modelo (GestorArchivos).
     */
    public ExploradorController(MainController mainController) {
        this.mainController = mainController;
        this.gestorArchivos = mainController.getGestorArchivos();
        this.historial = new ArrayDeque<>();

        this.carpetaActual = gestorArchivos.getCarpetaRaiz();
        System.out.println("ExploradorController inicializado. Ubicación actual: " + carpetaActual.getNombre());
    }

    /**
     * Devuelve la carpeta donde estamos ubicados actualmente.
     * Utilizado por la capa de Vista para renderizar la ubicación actual.
     */
    public DriveFolder getCarpetaActual() {
        return carpetaActual;
    }

    /**
     * Devuelve la ruta de navegación desde la raíz hasta la carpeta actual.
     * Se reconstruye a partir de la pila de historial: su tope es el padre
     * directo, por lo que al invertirla y añadir la carpeta actual se obtiene
     * la ruta en orden (raíz → ... → actual). La usa el breadcrumb de la vista.
     */
    public List<DriveFolder> getRutaActual() {
        List<DriveFolder> ruta = new ArrayList<>(historial);
        Collections.reverse(ruta);
        ruta.add(carpetaActual);
        return ruta;
    }

    /**
     * Navega directamente a una carpeta de la ruta actual (un ancestro o la
     * carpeta actual), retrocediendo en el historial hasta alcanzarla.
     * Pensado para los clics en el breadcrumb.
     */
    public void irARuta(DriveFolder destino) {
        if (destino == null) return;
        while (carpetaActual != destino && !historial.isEmpty()) {
            carpetaActual = historial.pop();
        }
    }

    /**
     * Acción: El usuario hace doble clic en una subcarpeta para entrar.
     */
    public void entrarACarpeta(DriveFolder subcarpeta) {
        if (this.carpetaActual.getSubcarpetas().contains(subcarpeta)) {
            historial.push(this.carpetaActual);
            this.carpetaActual = subcarpeta;
            System.out.println("Navegando a: " + carpetaActual.getNombre());
        }
    }

    /**
     * Acción: El usuario hace clic en el botón "Volver" o "Atrás".
     * Utiliza la pila de historial para volver a la carpeta anterior.
     */
    public void volverAtras() {
        if (!historial.isEmpty()) {
            this.carpetaActual = historial.pop();
            System.out.println("Volviendo a: " + carpetaActual.getNombre());
        }
    }

    /**
     * Acción: El usuario hace clic en "Nueva Carpeta" e ingresa un nombre.
     */
    public void crearNuevaCarpeta(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            gestorArchivos.crearCarpeta(nombre, this.carpetaActual);
            System.out.println("Nueva carpeta creada: " + nombre);
        }
    }

    /**
     * Acción: El usuario hace doble clic en un archivo.
     */
    public void abrirArchivo(DriveFile archivo) {
        System.out.println("Solicitando abrir el archivo: " + archivo.getNombre());
        mainController.mostrarVisorDocumento(archivo);
    }

    /**
     * Acción: El usuario importa un archivo real desde su disco.
     */
    public void importarArchivoReal(java.io.File archivo) {
        if (archivo != null && archivo.exists()) {
            DriveFile nuevo = gestorArchivos.importarArchivoFisico(archivo, this.carpetaActual);
            if (nuevo != null) {
                System.out.println("Archivo importado correctamente: " + nuevo.getNombre());
            } else {
                System.out.println("No se pudo importar el archivo.");
            }
        }
    }

    /**
     * Elimina una subcarpeta de la carpeta actual.
     * @return true si se eliminó correctamente.
     */
    public boolean eliminarCarpeta(DriveFolder carpeta) {
        if (carpeta != null && carpetaActual.getSubcarpetas().contains(carpeta)) {
            carpetaActual.eliminarSubcarpeta(carpeta);
            System.out.println("Carpeta eliminada: " + carpeta.getNombre());
            return true;
        }
        return false;
    }

    /**
     * Elimina un archivo de la carpeta actual.
     * Quita la referencia lógica del árbol; no borra el archivo físico del disco.
     * @return true si se eliminó correctamente.
     */
    public boolean eliminarArchivo(DriveFile archivo) {
        if (archivo != null && carpetaActual.getArchivos().contains(archivo)) {
            return gestorArchivos.eliminarArchivo(archivo, carpetaActual);
        }
        return false;
    }
}
