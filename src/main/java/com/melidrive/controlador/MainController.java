package com.melidrive.controlador;

import com.melidrive.modelo.GestorArchivos;

/**
 * Controlador principal de la aplicación S.I.G.E.A.
 * Se encarga de mantener el estado global (GestorArchivos) 
 * y orquestar el cambio de vistas principales.
 */
public class MainController {
    
    private GestorArchivos gestorArchivos;

    public MainController() {
        // Al instanciar el controlador principal, se inicializa el modelo de datos central
        this.gestorArchivos = new GestorArchivos();
        System.out.println("MainController inicializado. Modelo de datos cargado.");
    }

    /**
     * Devuelve la instancia global del gestor de archivos para que otros controladores 
     * o vistas puedan acceder al modelo de datos.
     */
    public GestorArchivos getGestorArchivos() {
        return gestorArchivos;
    }

    // TODO: Agregar métodos para cambiar la vista principal (ej. mostrarExplorador(), mostrarFlashcards())
    // Estos métodos interactuarán con la MainView (que se creará en el paquete vista) 
    // para reemplazar el contenido del centro del layout.
}
