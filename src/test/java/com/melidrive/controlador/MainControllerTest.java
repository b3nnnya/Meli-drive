package com.melidrive.controlador;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainControllerTest {

    @Test
    void constructorDebeInicializarTodosLosControladores() {

        MainController controller = new MainController();

        assertNotNull(controller.getGestorArchivos());
        assertNotNull(controller.getExploradorController());
        assertNotNull(controller.getFlashcardController());
        assertNotNull(controller.getVisorDocumentoController());
    }

    @Test
    void debeCargarDatosDeEjemplo() {

        MainController controller = new MainController();

        assertEquals(
                3,
                controller.getGestorArchivos()
                        .getCarpetaRaiz()
                        .getSubcarpetas()
                        .size()
        );

        assertEquals(
                2,
                controller.getGestorArchivos()
                        .getCarpetaRaiz()
                        .getArchivos()
                        .size()
        );
    }

    @Test
    void debeCrearTresFlashcardsIniciales() {

        MainController controller = new MainController();

        assertEquals(
                3,
                controller.getFlashcardController()
                        .getTotalFlashcards()
        );
    }

    @Test
    void lasCarpetasInicialesDebenExistir() {

        MainController controller = new MainController();

        var carpetas =
                controller.getGestorArchivos()
                        .getCarpetaRaiz()
                        .getSubcarpetas();

        assertTrue(
                carpetas.stream()
                        .anyMatch(c -> c.getNombre().equals("Anatomía"))
        );

        assertTrue(
                carpetas.stream()
                        .anyMatch(c -> c.getNombre().equals("Matemáticas"))
        );

        assertTrue(
                carpetas.stream()
                        .anyMatch(c -> c.getNombre().equals("Programación"))
        );
    }

    @Test
    void losArchivosInicialesEnRaizDebenExistir() {

        MainController controller = new MainController();

        var archivos =
                controller.getGestorArchivos()
                        .getCarpetaRaiz()
                        .getArchivos();

        assertTrue(
                archivos.stream()
                        .anyMatch(a -> a.getNombre().equals("Calendario Académico.pdf"))
        );

        assertTrue(
                archivos.stream()
                        .anyMatch(a -> a.getNombre().equals("Notas Finales.xlsx"))
        );
    }

    @Test
    void exploradorDebeComenzarEnLaCarpetaRaiz() {

        MainController controller = new MainController();

        assertEquals(
                controller.getGestorArchivos().getCarpetaRaiz(),
                controller.getExploradorController().getCarpetaActual()
        );
    }

    @Test
    void todasLasFlashcardsDebenTenerArchivoAsociado() {

        MainController controller = new MainController();

        controller.getFlashcardController()
                .getTodasLasFlashcards()
                .forEach(f ->
                        assertNotNull(f.getArchivoAsociado())
                );
    }

}