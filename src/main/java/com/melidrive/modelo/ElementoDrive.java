package com.melidrive.modelo;

import java.io.Serializable;

/**
 * Clase base abstracta de todo elemento que vive dentro de la unidad:
 * archivos ({@link DriveFile}) y carpetas ({@link DriveFolder}).
 *
 * Centraliza la identidad común (id y nombre) y define el comportamiento
 * polimórfico que cada tipo de elemento resuelve a su manera. Aplica los
 * pilares de la POO:
 *  - Herencia: DriveFile y DriveFolder heredan los atributos id/nombre y sus accesores.
 *  - Abstracción: declara "qué" sabe hacer un elemento (esCarpeta, getTamanioTotal)
 *    sin fijar el "cómo"; cada subclase aporta su implementación.
 *  - Encapsulamiento: los atributos quedan protegidos y se exponen vía getters/setters.
 */
public abstract class ElementoDrive implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String id;
    protected String nombre;

    /**
     * Constructor base usado por las subclases para fijar la identidad del elemento.
     */
    protected ElementoDrive(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Constructor vacío (sobrecarga) para escenarios de construcción sin datos iniciales.
     */
    protected ElementoDrive() {
    }

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

    /**
     * Indica el tipo de elemento de forma polimórfica.
     * @return true si es una carpeta; false si es un archivo.
     */
    public abstract boolean esCarpeta();

    /**
     * Tamaño del elemento en bytes. Para un archivo es su propio tamaño;
     * para una carpeta, la suma recursiva del tamaño de todo su contenido.
     */
    public abstract long getTamanioTotal();
}
