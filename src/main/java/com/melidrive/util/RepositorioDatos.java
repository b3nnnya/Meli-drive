package com.melidrive.util;

import com.melidrive.modelo.EstadoAplicacion;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Repositorio de persistencia. Guarda y restaura el estado completo
 * de la aplicación (EstadoAplicacion) en disco usando serialización Java.
 * El archivo se almacena en data/melidrive.dat.
 */
public class RepositorioDatos implements IRepositorio {

    private static final Path ARCHIVO_DATOS = Paths.get("data", "melidrive.dat");

    /**
     * Serializa y guarda el estado de la aplicación en disco.
     */
    @Override
    public void guardar(EstadoAplicacion estado) {
        try {
            if (ARCHIVO_DATOS.getParent() != null) {
                Files.createDirectories(ARCHIVO_DATOS.getParent());
            }
            try (ObjectOutputStream salida = new ObjectOutputStream(Files.newOutputStream(ARCHIVO_DATOS))) {
                salida.writeObject(estado);
            }
            System.out.println("Estado guardado en: " + ARCHIVO_DATOS.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error al guardar el estado: " + e.getMessage());
        }
    }

    /**
     * Restaura el estado de la aplicación desde disco.
     * @return el estado guardado, o null si no existe o no se pudo leer.
     */
    @Override
    public EstadoAplicacion cargar() {
        if (!Files.exists(ARCHIVO_DATOS)) {
            System.out.println("No hay estado previo. Se iniciará con datos de ejemplo.");
            return null;
        }
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(ARCHIVO_DATOS))) {
            EstadoAplicacion estado = (EstadoAplicacion) entrada.readObject();
            System.out.println("Estado restaurado desde: " + ARCHIVO_DATOS.toAbsolutePath());
            return estado;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar el estado: " + e.getMessage());
            return null;
        }
    }
}
