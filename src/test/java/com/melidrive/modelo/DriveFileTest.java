package com.melidrive.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DriveFileTest {

    @Test
    public void testCrearArchivoYAtributosBasicos() {
        DriveFolder carpetaPadre = new DriveFolder("f-1", "Carpeta Raiz", null);
        DriveFile archivo = new DriveFile("a-1", "Documento.pdf", "application/pdf", 1024, carpetaPadre);

        assertEquals("a-1", archivo.getId());
        assertEquals("Documento.pdf", archivo.getNombre());
        assertEquals("application/pdf", archivo.getTipoMime());
        assertEquals(1024, archivo.getSizeEnBytes());
        assertEquals(carpetaPadre, archivo.getCarpetaPadre());
        assertTrue(archivo.getEtiquetas().isEmpty());
    }

    @Test
    public void testGestionDeEtiquetas() {
        DriveFile archivo = new DriveFile("a-1", "Doc.pdf", "application/pdf", 1024, null);
        Etiqueta tag1 = new Etiqueta("t-1", "Importante", "#FF0000");

        archivo.agregarEtiqueta(tag1);
        assertEquals(1, archivo.getEtiquetas().size());
        assertTrue(archivo.tieneEtiqueta("Importante"));

        archivo.eliminarEtiqueta(tag1);
        assertEquals(0, archivo.getEtiquetas().size());
        assertFalse(archivo.tieneEtiqueta("Importante"));
    }

    @Test
    public void testToString() {
        DriveFile archivo = new DriveFile("a-1", "Doc.pdf", "application/pdf", 1024, null);
        assertTrue(archivo.toString().contains("Doc.pdf"));
        assertTrue(archivo.toString().contains("application/pdf"));
    }
}
