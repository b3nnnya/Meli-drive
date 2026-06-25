package com.melidrive.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un archivo de Google Drive.
 * Hereda de {@link ElementoDrive} la identidad común (id y nombre) y aporta
 * los atributos propios del archivo. Aquí aplicamos "Abstracción" y
 * "Encapsulamiento" (datos protegidos, expuestos mediante getters/setters).
 */
public class DriveFile extends ElementoDrive {

    private static final long serialVersionUID = 2L;

    // id y nombre ahora viven en la superclase ElementoDrive (herencia).
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
        super(id, nombre); // delega la identidad (id/nombre) a la superclase
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

    // getId/setId/getNombre/setNombre se heredan de ElementoDrive (encapsulamiento común).

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

    /** Un archivo nunca es una carpeta (polimorfismo desde ElementoDrive). */
    @Override
    public boolean esCarpeta() {
        return false;
    }

    /** El tamaño total de un archivo es su propio tamaño en bytes. */
    @Override
    public long getTamanioTotal() {
        return sizeEnBytes;
    }

    /**
     * Representación en texto del archivo para depuración.
     */
    @Override
    public String toString() {
        return "DriveFile{nombre='" + nombre + "', tipo='" + tipoMime + "', size=" + sizeEnBytes + "B}";
    }
}
