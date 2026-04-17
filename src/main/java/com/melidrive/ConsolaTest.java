package com.melidrive;

import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.DriveFolder;
import com.melidrive.modelo.GestorArchivos;

import java.util.List;

public class ConsolaTest {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBA DE GESTION DE ARCHIVOS (SIN INTERFAZ) ===");

        // 1. Instanciamos el Gestor Central
        GestorArchivos gestor = new GestorArchivos();
        DriveFolder miUnidad = gestor.getCarpetaRaiz();
        
        System.out.println("Carpeta principal creada: " + miUnidad.getNombre() + " (ID: " + miUnidad.getId() + ")");

        // 2. Crear Estructura de Carpetas
        DriveFolder carpetaAnatomia = gestor.crearCarpeta("Anatomía", miUnidad);
        DriveFolder carpetaMatematicas = gestor.crearCarpeta("Matemáticas", miUnidad);
        
        System.out.println("Subcarpetas creadas: " + carpetaAnatomia.getNombre() + ", " + carpetaMatematicas.getNombre());

        // 3. Registrar Archivos
        gestor.registrarArchivo("Huesos del brazo.pdf", "application/pdf", 1024, carpetaAnatomia);
        gestor.registrarArchivo("Músculos faciales.png", "image/png", 512, carpetaAnatomia);
        gestor.registrarArchivo("Apuntes Álgebra.docx", "application/vnd", 2048, carpetaMatematicas);
        gestor.registrarArchivo("Examen_Anatomia_2025.pdf", "application/pdf", 800, miUnidad);

        System.out.println("\nArchivos registrados exitosamente.");
        System.out.println("Total de elementos directos en Mi Unidad: " + miUnidad.getArchivos().size() + " archivo(s) y " + miUnidad.getSubcarpetas().size() + " subcarpeta(s)");

        // 4. Probar Búsqueda Recursiva
        System.out.println("\n--- Prueba de Búsqueda ---");
        String termino = "anatomia";
        System.out.println("Buscando por: '" + termino + "'...");
        
        List<DriveFile> resultados = gestor.buscarArchivosPorNombre(termino);
        
        if (resultados.isEmpty()) {
            System.out.println("No se encontraron resultados.");
        } else {
            System.out.println("Encontrados " + resultados.size() + " archivo(s):");
            for (DriveFile f : resultados) {
                System.out.println(" -> " + f.getNombre() + " (Tipo: " + f.getTipoMime() + ")");
            }
        }
        
        System.out.println("\n=== PRUEBA FINALIZADA ===");
    }
}
