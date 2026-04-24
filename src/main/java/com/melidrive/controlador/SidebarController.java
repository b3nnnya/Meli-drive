package com.melidrive.controlador;

/**
 * Controlador del menú lateral (Sidebar).
 * Responsabilidad: Manejar la navegación principal entre las secciones de la aplicación.
 * Gestiona los botones: "Mi Unidad", "Buscar por Etiqueta" y "Modo Estudio (Flashcards)".
 */
public class SidebarController {

    /**
     * Enum que representa las secciones navegables desde el Sidebar.
     */
    public enum SeccionApp {
        MI_UNIDAD,
        BUSCAR_POR_ETIQUETA,
        MODO_ESTUDIO
    }

    private MainController mainController;
    private SeccionApp seccionActiva;

    /**
     * Constructor. Requiere el MainController para delegar los cambios de vista.
     */
    public SidebarController(MainController mainController) {
        this.mainController = mainController;
        this.seccionActiva = SeccionApp.MI_UNIDAD;
        System.out.println("SidebarController inicializado. Sección activa: " + seccionActiva);
    }

    /**
     * Acción: El usuario hace clic en "Mi Unidad".
     * Navega al explorador de archivos mostrando la carpeta raíz.
     */
    public void irAMiUnidad() {
        this.seccionActiva = SeccionApp.MI_UNIDAD;
        System.out.println("Navegando a: Mi Unidad");
        // TODO: Notificar al MainController para mostrar la vista del Explorador
    }

    /**
     * Acción: El usuario hace clic en "Buscar por Etiqueta".
     * Navega a la vista de búsqueda y filtrado por etiquetas.
     */
    public void irABuscarPorEtiqueta() {
        this.seccionActiva = SeccionApp.BUSCAR_POR_ETIQUETA;
        System.out.println("Navegando a: Buscar por Etiqueta");
        // TODO: Notificar al MainController para mostrar la vista de búsqueda por etiquetas
    }

    /**
     * Acción: El usuario hace clic en "Modo Estudio (Flashcards)".
     * Navega al módulo de repaso espaciado con flashcards.
     */
    public void irAModoEstudio() {
        this.seccionActiva = SeccionApp.MODO_ESTUDIO;
        System.out.println("Navegando a: Modo Estudio (Flashcards)");
        // TODO: Notificar al MainController para mostrar la vista de Flashcards
    }

    /**
     * Navega a una sección específica mediante su enum.
     */
    public void navegarA(SeccionApp seccion) {
        switch (seccion) {
            case MI_UNIDAD:
                irAMiUnidad();
                break;
            case BUSCAR_POR_ETIQUETA:
                irABuscarPorEtiqueta();
                break;
            case MODO_ESTUDIO:
                irAModoEstudio();
                break;
        }
    }

    /**
     * Devuelve la sección actualmente seleccionada en el Sidebar.
     */
    public SeccionApp getSeccionActiva() {
        return seccionActiva;
    }

    /**
     * Devuelve la referencia al controlador principal.
     */
    public MainController getMainController() {
        return mainController;
    }
}
