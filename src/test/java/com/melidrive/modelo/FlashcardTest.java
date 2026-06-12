package com.melidrive.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class FlashcardTest {

    @Test
    public void testCrearFlashcardYRepasar() {
        DriveFile archivoAsociado = new DriveFile("a-1", "Doc.pdf", "application/pdf", 100);
        Flashcard flashcard = new Flashcard("fc-1", archivoAsociado, "Pregunta?", "Respuesta!");
        
        assertEquals("fc-1", flashcard.getId());
        assertEquals("Pregunta?", flashcard.getPregunta());
        assertEquals("Respuesta!", flashcard.getRespuesta());
        assertEquals(archivoAsociado, flashcard.getArchivoAsociado());
        assertEquals(0, flashcard.getNivelEstudio());
        assertNotNull(flashcard.getProximaFechaRepaso());
        assertTrue(flashcard.toString().contains("Pregunta?"));
        
        // Simular un repaso exitoso
        flashcard.registrarRepaso(2); // 2 = Fácil
        assertEquals(1, flashcard.getNivelEstudio());
        assertTrue(flashcard.getProximaFechaRepaso().isAfter(LocalDate.now()));
    }

    @Test
    void registrarRepasoDificilNoDebeBajarDeCero() {

        DriveFile archivo =
                new DriveFile("1","doc","pdf",100);

        Flashcard flashcard =
                new Flashcard(
                        "1",
                        archivo,
                        "P",
                        "R");

        flashcard.registrarRepaso(1);

        assertEquals(
                0,
                flashcard.getNivelEstudio());

        assertEquals(
                LocalDate.now().plusDays(1),
                flashcard.getProximaFechaRepaso());
    }

    @Test
    void registrarRepasoFacilDebeSubirNivel() {

        DriveFile archivo =
                new DriveFile("1","doc","pdf",100);

        Flashcard flashcard =
                new Flashcard(
                        "1",
                        archivo,
                        "P",
                        "R");

        flashcard.registrarRepaso(2);

        assertEquals(
                1,
                flashcard.getNivelEstudio());

        assertEquals(
                LocalDate.now().plusDays(2),
                flashcard.getProximaFechaRepaso());
    }

    @Test
    void crecimientoDebeSerExponencial() {

        DriveFile archivo =
                new DriveFile("1","doc","pdf",100);

        Flashcard flashcard =
                new Flashcard(
                        "1",
                        archivo,
                        "P",
                        "R");

        flashcard.registrarRepaso(2);
        flashcard.registrarRepaso(2);
        flashcard.registrarRepaso(2);

        assertEquals(
                3,
                flashcard.getNivelEstudio());

        assertEquals(
                LocalDate.now().plusDays(8),
                flashcard.getProximaFechaRepaso());
    }
}
