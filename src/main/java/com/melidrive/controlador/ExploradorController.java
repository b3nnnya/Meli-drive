package com.melidrive.controlador;

import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.DriveFolder;
import com.melidrive.modelo.GestorArchivos;

/**
 * Controlador encargado de la navegación y gestión de archivos.
 * Maneja la lógica de entrar a carpetas, crear nuevas, y seleccionar archivos.
 */
public class ExploradorController {

    private GestorArchivos gestorArchivos;
    private DriveFolder carpetaActual;
    
    // Referencia al controlador principal para comunicarse con otras partes de la app
    private MainController mainController;

    /**
     * Constructor. Requiere el MainController para obtener el modelo (GestorArchivos).
     */
    public ExploradorController(MainController mainController) {
        this.mainController = mainController;
        this.gestorArchivos = mainController.getGestorArchivos();
        
        // Al iniciar el explorador, siempre comenzamos en la raíz ("Mi Unidad")
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
            this.carpetaActual = subcarpeta;
            System.out.println("Navegando a: " + carpetaActual.getNombre());
            // TODO: Notificar a la Vista para actualizar la interfaz (refrescar)
        }
    }

    /**
     * Acción: El usuario hace clic en el botón "Volver" o "Atrás".
     * Nota: Como DriveFolder actualmente no sabe quién es su "padre",
     * aquí el controlador podría manejar una "pila" (Stack) de historial de navegación en el futuro.
     */
    public void volverAtras() {
        System.out.println("Acción: Volver atrás (Requiere implementar historial o referencia al padre).");
        // Si estuviéramos en la raíz, no hacemos nada.
    }

    /**
     * Acción: El usuario hace clic en "Nueva Carpeta" e ingresa un nombre.
     */
    public void crearNuevaCarpeta(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            gestorArchivos.crearCarpeta(nombre, this.carpetaActual);
            System.out.println("Nueva carpeta creada: " + nombre);
            // TODO: Llamar a la Vista para refrescar la pantalla
        }
    }

    /**
     * Acción: El usuario hace doble clic en un archivo.
     */
    public void abrirArchivo(DriveFile archivo) {
        System.out.println("Solicitando abrir el archivo: " + archivo.getNombre());
        // TODO: Notificar al MainController para cambiar al Visor de Documentos
    }
}
