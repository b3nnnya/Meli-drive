package com.melidrive.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class GestorArchivosTest {

    private GestorArchivos gestor;

    @BeforeEach
    public void setUp() {
        gestor = new GestorArchivos();
    }

    @Test
    public void testCrearCarpetaYRegistrarArchivo() {
        DriveFolder carpeta = gestor.crearCarpeta("Mi Carpeta", gestor.getCarpetaRaiz());
        assertNotNull(carpeta);
        assertEquals("Mi Carpeta", carpeta.getNombre());
        
        DriveFile archivo = gestor.registrarArchivo("Doc.pdf", "application/pdf", 1024, carpeta);
        assertNotNull(archivo);
        assertEquals(1, carpeta.getArchivos().size());
        assertEquals("Doc.pdf", carpeta.getArchivos().get(0).getNombre());
    }

    @Test
    public void testBusquedaPorEtiqueta() {
        DriveFolder carpeta = gestor.crearCarpeta("Docs", gestor.getCarpetaRaiz());
        DriveFile archivo = gestor.registrarArchivo("Nota.txt", "text/plain", 100, carpeta);
        
        archivo.agregarEtiqueta(new Etiqueta("t-1", "Urgente", "#FF0000"));
        
        List<DriveFile> resultados = gestor.buscarArchivosPorEtiqueta("Urgente");
        assertEquals(1, resultados.size());
        assertEquals(archivo, resultados.get(0));
        
        List<DriveFile> sinResultados = gestor.buscarArchivosPorEtiqueta("Inexistente");
        assertTrue(sinResultados.isEmpty());
    }

    @Test
    public void testEliminarArchivo() {
        DriveFolder carpeta = gestor.crearCarpeta("Docs", gestor.getCarpetaRaiz());
        DriveFile archivo = gestor.registrarArchivo("Nota.txt", "text/plain", 100, carpeta);
        
        assertEquals(1, carpeta.getArchivos().size());
        boolean eliminado = gestor.eliminarArchivo(archivo);
        
        assertTrue(eliminado);
        assertEquals(0, carpeta.getArchivos().size());
    }
}
