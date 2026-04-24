package com.melidrive.util;

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

        return TipoIcono.ARCHIVO_GENERICO;
    }
}
