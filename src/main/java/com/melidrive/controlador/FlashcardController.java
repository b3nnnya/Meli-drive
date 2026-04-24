package com.melidrive.controlador;

import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.Flashcard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador del módulo de Modo Estudio (Flashcards).
 * Maneja la creación de tarjetas, la sesión de repaso espaciado
 * y el registro de calificaciones del usuario.
 */
public class FlashcardController {

    private MainController mainController;
    private List<Flashcard> todasLasFlashcards;
    private List<Flashcard> sesionActual;
    private int indiceActual;

    /**
     * Constructor. Requiere el MainController para acceder al modelo global.
     */
    public FlashcardController(MainController mainController) {
        this.mainController = mainController;
        this.todasLasFlashcards = new ArrayList<>();
        this.sesionActual = new ArrayList<>();
        this.indiceActual = 0;
        System.out.println("FlashcardController inicializado.");
    }

    /**
     * Crea una nueva flashcard asociada a un archivo y la registra en el sistema.
     */
    public Flashcard crearFlashcard(String id, DriveFile archivo, String pregunta, String respuesta) {
        Flashcard nueva = new Flashcard(id, archivo, pregunta, respuesta);
        todasLasFlashcards.add(nueva);
        System.out.println("Flashcard creada: " + pregunta);
        return nueva;
    }

    /**
     * Elimina una flashcard del sistema.
     */
    public void eliminarFlashcard(Flashcard flashcard) {
        todasLasFlashcards.remove(flashcard);
        System.out.println("Flashcard eliminada: " + flashcard.getPregunta());
    }

    /**
     * Inicia una sesión de estudio con las tarjetas que deben repasarse hoy
     * según el algoritmo de repaso espaciado.
     */
    public void iniciarSesionDeEstudio() {
        LocalDate hoy = LocalDate.now();
        this.sesionActual = todasLasFlashcards.stream()
                .filter(f -> !f.getProximaFechaRepaso().isAfter(hoy))
                .collect(Collectors.toList());
        this.indiceActual = 0;

        System.out.println("Sesión de estudio iniciada. Tarjetas pendientes: " + sesionActual.size());
        // TODO: Notificar a la Vista para mostrar la primera tarjeta
    }

    /**
     * Devuelve la flashcard actual de la sesión de estudio.
     * Retorna null si la sesión terminó o no hay tarjetas.
     */
    public Flashcard getFlashcardActual() {
        if (sesionActual.isEmpty() || indiceActual >= sesionActual.size()) {
            return null;
        }
        return sesionActual.get(indiceActual);
    }

    /**
     * Registra la calificación del usuario sobre la tarjeta actual
     * y avanza a la siguiente.
     * @param calificacion 1=Difícil, 2=Fácil
     */
    public void calificarYAvanzar(int calificacion) {
        Flashcard actual = getFlashcardActual();
        if (actual != null) {
            actual.registrarRepaso(calificacion);
            System.out.println("Tarjeta calificada (" + calificacion + "): " + actual.getPregunta());
            indiceActual++;

            if (indiceActual >= sesionActual.size()) {
                System.out.println("Sesión de estudio completada.");
                // TODO: Notificar a la Vista para mostrar resumen de sesión
            } else {
                // TODO: Notificar a la Vista para mostrar la siguiente tarjeta
            }
        }
    }

    /**
     * Devuelve la cantidad de tarjetas pendientes de repaso para hoy.
     */
    public int getTarjetasPendientesHoy() {
        LocalDate hoy = LocalDate.now();
        return (int) todasLasFlashcards.stream()
                .filter(f -> !f.getProximaFechaRepaso().isAfter(hoy))
                .count();
    }

    /**
     * Devuelve el total de flashcards registradas en el sistema.
     */
    public int getTotalFlashcards() {
        return todasLasFlashcards.size();
    }

    /**
     * Devuelve todas las flashcards del sistema.
     */
    public List<Flashcard> getTodasLasFlashcards() {
        return todasLasFlashcards;
    }

    /**
     * Indica si la sesión de estudio actual ya finalizó.
     */
    public boolean sesionFinalizada() {
        return sesionActual.isEmpty() || indiceActual >= sesionActual.size();
    }

    /**
     * Devuelve la referencia al controlador principal.
     */
    public MainController getMainController() {
        return mainController;
    }
}
