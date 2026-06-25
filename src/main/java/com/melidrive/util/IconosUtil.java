package com.melidrive.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

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
     * Determina el ícono correspondiente según el tipo MIME de un archivo.
     * @param tipoMime el tipo MIME del archivo (ej. "application/pdf", "image/png")
     * @return el TipoIcono que corresponde al MIME proporcionado
     */
    public static TipoIcono getIconoPorMime(String tipoMime) {
        if (tipoMime == null || tipoMime.isEmpty()) {
            return TipoIcono.ARCHIVO_GENERICO;
        }

        String mime = tipoMime.toLowerCase();

        // PDF
        if (mime.equals("application/pdf")) {
            return TipoIcono.PDF;
        }

        // Imágenes
        if (mime.startsWith("image/")) {
            return TipoIcono.IMAGEN;
        }

        // Video
        if (mime.startsWith("video/")) {
            return TipoIcono.VIDEO;
        }

        // Audio
        if (mime.startsWith("audio/")) {
            return TipoIcono.AUDIO;
        }

        // Texto plano
        if (mime.startsWith("text/")) {
            return TipoIcono.TEXTO;
        }

        // Documentos de oficina (Google Docs, Word, etc.)
        if (mime.contains("document") || mime.contains("msword")
                || mime.contains("wordprocessingml")) {
            return TipoIcono.DOCUMENTO;
        }

        // Hojas de cálculo (Google Sheets, Excel, etc.)
        if (mime.contains("spreadsheet") || mime.contains("excel")
                || mime.contains("spreadsheetml")) {
            return TipoIcono.HOJA_CALCULO;
        }

        // Presentaciones (Google Slides, PowerPoint, etc.)
        if (mime.contains("presentation") || mime.contains("powerpoint")
                || mime.contains("presentationml")) {
            return TipoIcono.PRESENTACION;
        }

        // Archivos comprimidos
        if (mime.contains("zip") || mime.contains("rar") 
                || mime.contains("tar") || mime.contains("7z")) {
            return TipoIcono.COMPRIMIDO;
        }

        return TipoIcono.ARCHIVO_GENERICO;
    }
}
