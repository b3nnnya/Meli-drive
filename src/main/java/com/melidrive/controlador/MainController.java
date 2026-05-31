package com.melidrive.controlador;

import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.DriveFolder;
import com.melidrive.modelo.GestorArchivos;
import com.melidrive.util.ThemeManager;
import com.melidrive.vista.MainView;

/**
 * Controlador principal de la aplicación S.I.G.E.A.
 * Se encarga de mantener el estado global (GestorArchivos),
 * orquestar el cambio de vistas principales y coordinar sub-controladores.
 */
public class MainController {

    private GestorArchivos gestorArchivos;
    private MainView mainView;
    private ThemeManager themeManager;

    private ExploradorController exploradorController;
    private FlashcardController flashcardController;
    private VisorDocumentoController visorDocumentoController;
    private SidebarController sidebarController;

    public MainController() {
        this.gestorArchivos = new GestorArchivos();

        this.exploradorController = new ExploradorController(this);
        this.flashcardController = new FlashcardController(this);
        this.visorDocumentoController = new VisorDocumentoController(this);
        this.sidebarController = new SidebarController(this);

        cargarDatosDeEjemplo();

        System.out.println("MainController inicializado. Modelo de datos cargado.");
    }

    /**
     * Vincula la vista principal con este controlador.
     * Se llama desde MainApp después de crear ambos.
     */
    public void setThemeManager(ThemeManager themeManager) {
        this.themeManager = themeManager;
    }
    
    public ThemeManager getThemeManager() {
        return themeManager;
    }
    
    public void toggleModoOscuro() {
        if (themeManager != null) {
            themeManager.toggleModoOscuro();
        }
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
        mostrarExplorador();
    }

    /**
     * Cambia el área central a la vista del Explorador de archivos.
     */
    public void mostrarExplorador() {
        if (mainView != null) {
            mainView.mostrarExplorador(exploradorController);
        }
    }

    /**
     * Cambia el área central a la vista de Flashcards (Modo Estudio).
     */
    public void mostrarFlashcards() {
        if (mainView != null) {
            mainView.mostrarFlashcards(flashcardController);
        }
    }

    /**
     * Cambia el área central al Visor de Documentos con el archivo seleccionado.
     */
    public void mostrarVisorDocumento(DriveFile archivo) {
        if (mainView != null) {
            visorDocumentoController.abrirDocumento(archivo);
            mainView.mostrarVisorDocumento(visorDocumentoController);
        }
    }

    /**
     * Crea carpetas y archivos de ejemplo para demostración.
     */
    private void cargarDatosDeEjemplo() {
        DriveFolder raiz = gestorArchivos.getCarpetaRaiz();

        DriveFolder anatomia = gestorArchivos.crearCarpeta("Anatomía", raiz);
        DriveFolder matematicas = gestorArchivos.crearCarpeta("Matemáticas", raiz);
        DriveFolder programacion = gestorArchivos.crearCarpeta("Programación", raiz);

        gestorArchivos.registrarArchivo("Huesos del brazo.pdf", "application/pdf", 2048, anatomia);
        gestorArchivos.registrarArchivo("Músculos faciales.png", "image/png", 512, anatomia);
        gestorArchivos.registrarArchivo("Resumen Parcial 1.pdf", "application/pdf", 1024, anatomia);

        gestorArchivos.registrarArchivo("Álgebra Lineal.pdf", "application/pdf", 3072, matematicas);
        gestorArchivos.registrarArchivo("Ejercicios Cálculo.docx", "application/msword", 1536, matematicas);

        gestorArchivos.registrarArchivo("Apuntes Java.pdf", "application/pdf", 4096, programacion);
        gestorArchivos.registrarArchivo("Diagrama UML.png", "image/png", 768, programacion);

        gestorArchivos.registrarArchivo("Calendario Académico.pdf", "application/pdf", 500, raiz);
        gestorArchivos.registrarArchivo("Notas Finales.xlsx", "application/vnd.ms-excel", 256, raiz);

        // Crear flashcards de ejemplo
        DriveFile archivoAnatomia = anatomia.getArchivos().get(0);
        flashcardController.crearFlashcard("fc-1", archivoAnatomia,
                "¿Cuántos huesos tiene el brazo humano?", "3: húmero, cúbito y radio");
        flashcardController.crearFlashcard("fc-2", archivoAnatomia,
                "¿Qué hueso conecta el hombro con el codo?", "El húmero");
        flashcardController.crearFlashcard("fc-3", archivoAnatomia,
                "¿Cuál es el hueso más largo del cuerpo?", "El fémur");
    }

    public GestorArchivos getGestorArchivos() {
        return gestorArchivos;
    }

    public ExploradorController getExploradorController() {
        return exploradorController;
    }

    public FlashcardController getFlashcardController() {
        return flashcardController;
    }

    public VisorDocumentoController getVisorDocumentoController() {
        return visorDocumentoController;
    }

    public SidebarController getSidebarController() {
        return sidebarController;
    }

    /**
     * Cambia el area central a la vista de busqueda por etiquetas.
     */
    public void mostrarBusquedaPorEtiqueta() {
        if (mainView != null) {
            mainView.mostrarBusquedaPorEtiqueta(this);
        }
    }
}
