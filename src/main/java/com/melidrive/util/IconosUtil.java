package com.melidrive.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.List;
import java.util.function.Predicate;

/**
 * Clase utilitaria para asignar íconos visuales según el tipo de contenido.
 * Mapea tipos MIME de DriveFile y carpetas (DriveFolder) a rutas de íconos.
 */
public final class IconosUtil {

    /**
     * Enum que agrupa las categorías de íconos disponibles en la aplicación.
     */
    public enum TipoIcono {
        CARPETA("icons/folder.png"),
        PDF("icons/pdf.png"),
        IMAGEN("icons/image.png"),
        DOCUMENTO("icons/document.png"),
        HOJA_CALCULO("icons/spreadsheet.png"),
        PRESENTACION("icons/presentation.png"),
        VIDEO("icons/video.png"),
        AUDIO("icons/audio.png"),
        TEXTO("icons/text.png"),
        COMPRIMIDO("icons/zip.png"),
        ARCHIVO_GENERICO("icons/generic.png");

        private final String rutaIcono;

        TipoIcono(String rutaIcono) {
            this.rutaIcono = rutaIcono;
        }

        public String getRutaIcono() {
            return rutaIcono;
        }
    }

    // Constructor privado para evitar instanciación (clase utilitaria)
    private IconosUtil() {
    }

    /**
     * Crea un ImageView con un ícono cargado desde resources, a un tamaño dado.
     * Si el recurso no existe, devuelve un ImageView vacío (sin romper la vista).
     *
     * @param ruta ruta del recurso (p. ej. "icons/ui/nav-unidad.png")
     * @param size ancho/alto en píxeles
     */
    public static ImageView crearIcono(String ruta, double size) {
        ImageView vista = new ImageView();
        vista.setFitWidth(size);
        vista.setFitHeight(size);
        vista.setPreserveRatio(true);
        vista.setSmooth(true);
        vista.setCache(true);
        InputStream is = IconosUtil.class.getClassLoader().getResourceAsStream(ruta);
        if (is != null) {
            // Se carga la imagen a mayor resolución (HiDPI) con suavizado para que
            // el ícono se vea nítido al reducirse a su tamaño final de visualización.
            vista.setImage(new Image(is, size * 3, size * 3, true, true));
        }
        return vista;
    }

    /**
     * Determina el ícono correspondiente para una carpeta.
     * @return TipoIcono.CARPETA siempre
     */
    public static TipoIcono getIconoCarpeta() {
        return TipoIcono.CARPETA;
    }

    /**
     * Regla que asocia una condición sobre el tipo MIME con su ícono.
     * (Principio Abierto/Cerrado): para soportar un tipo nuevo basta con
     * añadir una entrada a {@link #REGLAS_MIME}, sin modificar la lógica de
     * {@link #getIconoPorMime}.
     */
    private record ReglaIcono(Predicate<String> aplica, TipoIcono tipo) { }

    /**
     * Reglas de asignación de íconos por MIME, evaluadas en orden (la primera
     * que coincide gana). El orden importa: lo específico antes que lo genérico.
     */
    private static final List<ReglaIcono> REGLAS_MIME = List.of(
            new ReglaIcono(m -> m.equals("application/pdf"), TipoIcono.PDF),
            new ReglaIcono(m -> m.startsWith("image/"), TipoIcono.IMAGEN),
            new ReglaIcono(m -> m.startsWith("video/"), TipoIcono.VIDEO),
            new ReglaIcono(m -> m.startsWith("audio/"), TipoIcono.AUDIO),
            new ReglaIcono(m -> m.startsWith("text/"), TipoIcono.TEXTO),
            new ReglaIcono(m -> m.contains("document") || m.contains("msword")
                    || m.contains("wordprocessingml"), TipoIcono.DOCUMENTO),
            new ReglaIcono(m -> m.contains("spreadsheet") || m.contains("excel")
                    || m.contains("spreadsheetml"), TipoIcono.HOJA_CALCULO),
            new ReglaIcono(m -> m.contains("presentation") || m.contains("powerpoint")
                    || m.contains("presentationml"), TipoIcono.PRESENTACION),
            new ReglaIcono(m -> m.contains("zip") || m.contains("rar")
                    || m.contains("tar") || m.contains("7z"), TipoIcono.COMPRIMIDO)
    );

    /**
     * Determina el ícono correspondiente según el tipo MIME de un archivo,
     * evaluando las {@link #REGLAS_MIME} en orden.
     * @param tipoMime el tipo MIME del archivo (ej. "application/pdf", "image/png")
     * @return el TipoIcono que corresponde al MIME proporcionado
     */
    public static TipoIcono getIconoPorMime(String tipoMime) {
        if (tipoMime == null || tipoMime.isEmpty()) {
            return TipoIcono.ARCHIVO_GENERICO;
        }
        String mime = tipoMime.toLowerCase();
        return REGLAS_MIME.stream()
                .filter(regla -> regla.aplica().test(mime))
                .map(ReglaIcono::tipo)
                .findFirst()
                .orElse(TipoIcono.ARCHIVO_GENERICO);
    }
}
