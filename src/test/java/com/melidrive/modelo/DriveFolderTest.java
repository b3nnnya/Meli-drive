package com.melidrive.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DriveFolderTest {

    @Test
    public void testGestionCarpetasYArchivos() {
        DriveFolder raiz = new DriveFolder("f-0", "Raiz", null);
        DriveFolder sub = new DriveFolder("f-1", "Subcarpeta", raiz);
        
        raiz.agregarSubcarpeta(sub);
        assertEquals(1, raiz.getSubcarpetas().size());
        
        DriveFile archivo = new DriveFile("a-1", "Doc.pdf", "application/pdf", 1000, raiz);
        raiz.agregarArchivo(archivo);
        assertEquals(1, raiz.getArchivos().size());
    }

    @Test
    public void testContarArchivosRecursivo() {
        DriveFolder raiz = new DriveFolder("f-0", "Raiz", null);
        DriveFolder sub1 = new DriveFolder("f-1", "Sub1", raiz);
        DriveFolder sub2 = new DriveFolder("f-2", "Sub2", sub1);
        
        raiz.agregarSubcarpeta(sub1);
        sub1.agregarSubcarpeta(sub2);
        
        raiz.agregarArchivo(new DriveFile("a-1", "Doc1.pdf", "application/pdf", 10, raiz));
        sub1.agregarArchivo(new DriveFile("a-2", "Doc2.pdf", "application/pdf", 10, sub1));
        sub2.agregarArchivo(new DriveFile("a-3", "Doc3.pdf", "application/pdf", 10, sub2));
        sub2.agregarArchivo(new DriveFile("a-4", "Doc4.pdf", "application/pdf", 10, sub2));
        
        assertEquals(4, raiz.contarArchivosRecursivo());
        assertEquals(3, sub1.contarArchivosRecursivo());
        assertEquals(2, sub2.contarArchivosRecursivo());
    }

    @Test
    public void testBuscarSubcarpetaPorNombre() {
        DriveFolder raiz = new DriveFolder("f-0", "Raiz", null);
        DriveFolder sub = new DriveFolder("f-1", "Buscada", raiz);
        raiz.agregarSubcarpeta(sub);
        
        DriveFolder encontrada = raiz.buscarSubcarpetaPorNombre("Buscada");
        assertNotNull(encontrada);
        assertEquals("f-1", encontrada.getId());
        
        DriveFolder noEncontrada = raiz.buscarSubcarpetaPorNombre("Inexistente");
        assertNull(noEncontrada);
    }
}
