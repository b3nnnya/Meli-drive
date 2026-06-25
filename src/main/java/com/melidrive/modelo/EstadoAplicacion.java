package com.melidrive.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Contenedor serializable con el estado completo de la aplicación.
 * Agrupa el árbol de carpetas (carpetaRaiz) y la lista de flashcards
 * en un único grafo de objetos, de modo que la serialización preserve
 * las referencias compartidas entre cada Flashcard y su DriveFile asociado.
 */
public class EstadoAplicacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private DriveFolder carpetaRaiz;
    private List<Flashcard> flashcards;
    private boolean modoOscuro;
    private boolean limpiezaEjemploHecha;

    public EstadoAplicacion(DriveFolder carpetaRaiz, List<Flashcard> flashcards, boolean modoOscuro) {
        this.carpetaRaiz = carpetaRaiz;
        this.flashcards = (flashcards != null) ? new ArrayList<>(flashcards) : new ArrayList<>();
        this.modoOscuro = modoOscuro;
    }

    public DriveFolder getCarpetaRaiz() {
        return carpetaRaiz;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    public boolean isModoOscuro() {
        return modoOscuro;
    }

    public boolean isLimpiezaEjemploHecha() {
        return limpiezaEjemploHecha;
    }

    public void setLimpiezaEjemploHecha(boolean limpiezaEjemploHecha) {
        this.limpiezaEjemploHecha = limpiezaEjemploHecha;
    }
}
