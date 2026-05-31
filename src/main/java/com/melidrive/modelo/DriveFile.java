package com.melidrive.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un archivo de Google Drive.
 * Aquí aplicamos el concepto de "Abstracción" (representando un concepto real en código)
 * y "Encapsulamiento" (protegiendo los datos haciéndolos privados).
 */
public class DriveFile {
    
    // Atributos privados: Nadie fuera de esta clase puede accederlos ni modificarlos directamente.
    private String id;
    private String nombre;
    private String tipoMime; // Ej: application/pdf, image/jpeg
    private long sizeEnBytes;
    
    // Atributo para texto extraído mediante OCR (Tess4j)
    private String contenidoExtraido;
    
    // Lista de etiquetas asociadas al documento (Reemplazo manual del OCR automatizado)
    private List<Etiqueta> etiquetas;

    // Opcional: ruta real del archivo en disco
    private String rutaFisica;

    /**
     * Constructor (Método especial)
     * Es el "molde" que usamos cuando creamos un nuevo objeto usando la palabra 'new'.
     * Ej: DriveFile miArchivo = new DriveFile("123", "foto.png", "image/png", 2048);
     */
    public DriveFile(String id, String nombre, String tipoMime, long sizeEnBytes) {
        this.id = id;
        this.nombre = nombre;
        this.tipoMime = tipoMime;
        this.sizeEnBytes = sizeEnBytes;
        this.contenidoExtraido = ""; // Inicialmente vacío
        this.etiquetas = new ArrayList<>(); // Inicializar lista para evitar nulidad
        this.rutaFisica = null;
    }

    /**
     * Constructor Vacío (Sobrecarga de Métodos)
     * Permite crear un objeto sin datos iniciales si lo necesitamos.
     */
    public DriveFile() {
    }

    /* 
     * ==========================================
     * GETTERS Y SETTERS (Encapsulamiento)
     * Métodos públicos controlados para permitir ver o cambiar los atributos.
     * ==========================================
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public long getSizeEnBytes() {
        return sizeEnBytes;
    }

    public void setSizeEnBytes(long sizeEnBytes) {
        // Ejemplo de encapsulamiento: podemos agregar reglas antes de guardar.
        if (sizeEnBytes < 0) {
            System.out.println("Error: El tamaño del archivo no puede ser negativo.");
            // No cambiamos el valor si no cumple la regla
        } else {
            this.sizeEnBytes = sizeEnBytes;
        }
    }

    public String getContenidoExtraido() {
        return contenidoExtraido;
    }

    public void setContenidoExtraido(String contenidoExtraido) {
        this.contenidoExtraido = contenidoExtraido;
    }

    public List<Etiqueta> getEtiquetas() {
        return etiquetas;
    }

    public void agregarEtiqueta(Etiqueta etiqueta) {
        this.etiquetas.add(etiqueta);
    }

    public void eliminarEtiqueta(Etiqueta etiqueta) {
        this.etiquetas.remove(etiqueta);
    }

    public String getRutaFisica() {
        return rutaFisica;
    }

    public void setRutaFisica(String rutaFisica) {
        this.rutaFisica = rutaFisica;
    }

    /**
     * Busca una etiqueta por nombre dentro de las etiquetas del archivo.
     * @param nombre el nombre de la etiqueta a buscar
     * @return la Etiqueta encontrada o null si no existe
     */
    public Etiqueta buscarEtiquetaPorNombre(String nombre) {
        if (nombre == null || etiquetas == null) return null;
        for (Etiqueta etiqueta : etiquetas) {
            if (etiqueta.getNombre().equalsIgnoreCase(nombre)) {
                return etiqueta;
            }
        }
        return null;
    }

    /**
     * Representación en texto del archivo para depuración.
     */
    @Override
    public String toString() {
        return "DriveFile{nombre='" + nombre + "', tipo='" + tipoMime + "', size=" + sizeEnBytes + "B}";
    }
}
