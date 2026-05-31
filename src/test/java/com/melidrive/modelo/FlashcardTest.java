package com.melidrive.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class FlashcardTest {

    @Test
    public void testCrearFlashcardYRepasar() {
        DriveFile archivoAsociado = new DriveFile("a-1", "Doc.pdf", "application/pdf", 100, null);
        Flashcard flashcard = new Flashcard("fc-1", archivoAsociado, "Pregunta?", "Respuesta!");
        
        assertEquals("fc-1", flashcard.getId());
        assertEquals("Pregunta?", flashcard.getPregunta());
        assertEquals("Respuesta!", flashcard.getRespuesta());
        assertEquals(archivoAsociado, flashcard.getArchivoAsociado());
        assertEquals(0, flashcard.getNivelConocimiento());
        assertNotNull(flashcard.getProximoRepaso());
        assertTrue(flashcard.toString().contains("Pregunta?"));
        
        // Simular un repaso exitoso
        flashcard.repasar(2); // 2 = Fácil
        assertEquals(1, flashcard.getNivelConocimiento());
        assertTrue(flashcard.getProximoRepaso().isAfter(LocalDate.now()));
    }
}
