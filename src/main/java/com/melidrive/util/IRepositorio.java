package com.melidrive.util;

import com.melidrive.modelo.EstadoAplicacion;

/**
 * Abstracción de la persistencia del estado de la aplicación.
 *
 * El controlador depende de esta interfaz y no de una implementación concreta
 * ({@link RepositorioDatos}), aplicando la Inversión de Dependencias (DIP).
 * Así se puede sustituir el mecanismo de persistencia (disco, base de datos,
 * un doble de prueba...) sin tocar el controlador.
 */
public interface IRepositorio {

    /** Persiste el estado completo de la aplicación. */
    void guardar(EstadoAplicacion estado);

    /** Restaura el estado guardado, o devuelve null si no existe o no se pudo leer. */
    EstadoAplicacion cargar();
}
