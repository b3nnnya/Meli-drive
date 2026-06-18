package com.melidrive.controlador;

import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.Etiqueta;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VisorDocumentoControllerTest {

    @Test
    void abrirDocumentoDebeActualizarEstado() {

        MainController main = new MainController();
        VisorDocumentoController controller =
                new VisorDocumentoController(main);

        DriveFile archivo =
                new DriveFile("1", "Doc", "pdf", 100);

        controller.abrirDocumento(archivo);

        assertTrue(controller.hayDocumentoAbierto());
        assertEquals(archivo, controller.getArchivoAbierto());
    }

    @Test
    void cerrarDocumentoDebeVaciarArchivoActual() {

        MainController main = new MainController();
        VisorDocumentoController controller =
                new VisorDocumentoController(main);

        DriveFile archivo =
                new DriveFile("1", "Doc", "pdf", 100);

        controller.abrirDocumento(archivo);
        controller.cerrarDocumento();

        assertFalse(controller.hayDocumentoAbierto());
        assertNull(controller.getArchivoAbierto());
    }

    @Test
    void agregarYEliminarEtiquetaDebeModificarArchivo() {

        MainController main = new MainController();
        VisorDocumentoController controller =
                new VisorDocumentoController(main);

        DriveFile archivo =
                new DriveFile("1", "Doc", "pdf", 100);

        Etiqueta etiqueta =
                new Etiqueta("1", "POO", "#000");

        controller.abrirDocumento(archivo);

        controller.agregarEtiqueta(etiqueta);

        assertEquals(1, archivo.getEtiquetas().size());

        controller.eliminarEtiqueta(etiqueta);

        assertTrue(archivo.getEtiquetas().isEmpty());
    }
}