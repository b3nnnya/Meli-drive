package com.melidrive.controlador;

import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.Flashcard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlashcardControllerTest {

    @Test
    void crearFlashcardDebeAgregarla() {

        MainController main = new MainController();
        FlashcardController controller = new FlashcardController(main);

        DriveFile archivo =
                new DriveFile("1", "Doc", "pdf", 100);

        controller.crearFlashcard(
                "id",
                archivo,
                "Pregunta",
                "Respuesta"
        );

        assertEquals(1, controller.getTotalFlashcards());
    }

    @Test
    void eliminarFlashcardDebeDisminuirCantidad() {

        MainController main = new MainController();
        FlashcardController controller = new FlashcardController(main);

        DriveFile archivo =
                new DriveFile("1", "Doc", "pdf", 100);

        Flashcard f = controller.crearFlashcard(
                "id",
                archivo,
                "Pregunta",
                "Respuesta"
        );

        controller.eliminarFlashcard(f);

        assertEquals(0, controller.getTotalFlashcards());
    }

    @Test
    void iniciarSesionDebeRetornarFlashcardActual() {

        MainController main = new MainController();
        FlashcardController controller = new FlashcardController(main);

        DriveFile archivo =
                new DriveFile("1", "Doc", "pdf", 100);

        controller.crearFlashcard(
                "id",
                archivo,
                "Pregunta",
                "Respuesta"
        );

        controller.iniciarSesionDeEstudio();

        assertNotNull(controller.getFlashcardActual());
    }

    @Test
    void calificarDebeFinalizarSesionConUnaTarjeta() {

        MainController main = new MainController();
        FlashcardController controller = new FlashcardController(main);

        DriveFile archivo =
                new DriveFile("1", "Doc", "pdf", 100);

        controller.crearFlashcard(
                "id",
                archivo,
                "Pregunta",
                "Respuesta"
        );

        controller.iniciarSesionDeEstudio();

        controller.calificarYAvanzar(2);

        assertTrue(controller.sesionFinalizada());
    }
}