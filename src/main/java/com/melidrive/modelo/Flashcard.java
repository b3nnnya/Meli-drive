package com.melidrive.modelo;

import java.time.LocalDate;

/**
 * Representa una tarjeta de estudio para el sistema de Spaced Repetition (Repaso Espaciado).
 * Relaciona una pregunta y una respuesta con un Documento (DriveFile).
 */
public class Flashcard {
    
    private String id;
    private DriveFile archivoAsociado; // Composición: El documento del que sale este repaso
    private String pregunta;
    private String respuesta;
    private LocalDate proximaFechaRepaso;
    private int nivelEstudio; // Cuántas veces se ha acertado seguidas

    public Flashcard(String id, DriveFile archivoAsociado, String pregunta, String respuesta) {
        this.id = id;
        this.archivoAsociado = archivoAsociado;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.proximaFechaRepaso = LocalDate.now(); // Para estudiar hoy
        this.nivelEstudio = 0; // Tarjeta nueva
    }

    /**
     * Lógica núcleo del "Repaso Espaciado".
     * @param calificacion 1=Difícil, 2=Fácil
     */
    public void registrarRepaso(int calificacion) {
        if (calificacion == 1) { // Le costó
            this.nivelEstudio = Math.max(0, this.nivelEstudio - 1); 
            this.proximaFechaRepaso = LocalDate.now().plusDays(1); // Repasar mañana
        } else if (calificacion == 2) { // Sabía bien la respuesta
            this.nivelEstudio++;
            // Incremento exponencial simple de los días de repaso
            long diasSumar = (long) Math.pow(2, this.nivelEstudio); 
            this.proximaFechaRepaso = LocalDate.now().plusDays(diasSumar);
        }
    }

    /* GETTERS Y SETTERS */
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public DriveFile getArchivoAsociado() { return archivoAsociado; }
    public void setArchivoAsociado(DriveFile archivoAsociado) { this.archivoAsociado = archivoAsociado; }

    public String getPregunta() { return pregunta; }
    public void setPregunta(String pregunta) { this.pregunta = pregunta; }

    public String getRespuesta() { return respuesta; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }

    public LocalDate getProximaFechaRepaso() { return proximaFechaRepaso; }
    public int getNivelEstudio() { return nivelEstudio; }
}
