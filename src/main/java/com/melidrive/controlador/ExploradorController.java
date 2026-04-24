package com.melidrive.controlador;

import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.DriveFolder;
import com.melidrive.modelo.GestorArchivos;

import java.util.ArrayDeque;
import java.util.Deque;

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
}
