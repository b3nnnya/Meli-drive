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
    void registrarArchivoDebeGenerarIdsUnicos() {

        GestorArchivos gestor =
                new GestorArchivos();

        DriveFolder raiz =
                gestor.getCarpetaRaiz();

        DriveFile a =
                gestor.registrarArchivo(
                        "a","pdf",1,raiz);

        DriveFile b =
                gestor.registrarArchivo(
                        "b","pdf",1,raiz);

        assertNotEquals(a.getId(), b.getId());
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
    void buscarDebeIgnorarMayusculas() {

        GestorArchivos gestor =
                new GestorArchivos();

        gestor.registrarArchivo(
                "ParcialJava.pdf",
                "pdf",
                10,
                gestor.getCarpetaRaiz());

        List<DriveFile> resultado =
                gestor.buscarArchivosPorNombre("JAVA");

        assertEquals(1,
                resultado.size());
    }

    @Test
    void buscarDebeRetornarVacioSiNoExiste() {

        GestorArchivos gestor =
                new GestorArchivos();

        List<DriveFile> resultado =
                gestor.buscarArchivosPorNombre("xyz");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarDebeRetornarMultiplesCoincidencias() {

        GestorArchivos gestor =
                new GestorArchivos();

        DriveFolder raiz =
                gestor.getCarpetaRaiz();

        gestor.registrarArchivo(
                "POO1.pdf","pdf",1,raiz);

        gestor.registrarArchivo(
                "POO2.pdf","pdf",1,raiz);

        gestor.registrarArchivo(
                "Java.pdf","pdf",1,raiz);

        List<DriveFile> resultado =
                gestor.buscarArchivosPorNombre("poo");

        assertEquals(2,
                resultado.size());
    }

    @Test
    public void testEliminarArchivo() {
        DriveFolder carpeta = gestor.crearCarpeta("Docs", gestor.getCarpetaRaiz());
        DriveFile archivo = gestor.registrarArchivo("Nota.txt", "text/plain", 100, carpeta);
        
        assertEquals(1, carpeta.getArchivos().size());
        boolean eliminado = gestor.eliminarArchivo(archivo, carpeta);
        
        assertTrue(eliminado);
        assertEquals(0, carpeta.getArchivos().size());
    }
}
