package com.melidrive.modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa una Etiqueta manual que el usuario puede
 * asociar a los archivos para luego poder buscarlos o filtrarlos.
 * Este fue el reemplazo sugerido para el OCR automatizado.
 */
public class Etiqueta implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String nombre; // e.g., "Anatomía", "Parcial 1"
    private String colorHex; // Solo para propósitos visuales (JavaFX)

    public Etiqueta(String id, String nombre, String colorHex) {
        this.id = id;
        this.nombre = nombre;
        this.colorHex = colorHex;
    }

    /* GETTERS Y SETTERS */
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    @Override
    public String toString() {
        return nombre;
    }

    /**
     * Compara dos etiquetas por su identificador único.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Etiqueta etiqueta = (Etiqueta) o;
        return Objects.equals(id, etiqueta.id);
    }

    /**
     * Genera el hash basado en el identificador único.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
