package com.melidrive.util;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * Utilidad para mostrar notificaciones "toast": un banner flotante y temporal
 * que confirma una acción (crear, importar, eliminar...) sin interrumpir al usuario.
 *
 * Se apoya en un {@link Popup} para flotar por encima de cualquier vista, de modo
 * que no depende de la jerarquía de la escena (que se reemplaza al cambiar de vista).
 * Hereda las hojas de estilo de la escena para respetar el tema claro/oscuro.
 */
public class Notificador {

    /** Tipo de notificación; define el color del borde lateral del toast. */
    public enum Tipo { SUCCESS, DANGER, INFO }

    private static final Duration FADE_IN = Duration.millis(180);
    private static final Duration VISIBLE = Duration.seconds(2);
    private static final Duration FADE_OUT = Duration.millis(350);

    /**
     * Muestra un toast anclado abajo-centro de la ventana del nodo dado.
     *
     * @param origen  cualquier nodo que ya esté en la escena (para ubicar la ventana)
     * @param mensaje texto a mostrar
     * @param tipo    color/semántica del toast
     */
    public static void mostrar(Node origen, String mensaje, Tipo tipo) {
        if (origen == null || origen.getScene() == null) {
            return;
        }
        Window ventana = origen.getScene().getWindow();
        if (ventana == null) {
            return;
        }

        Label texto = new Label(mensaje);
        texto.getStyleClass().add("toast-text");

        StackPane contenedor = new StackPane(texto);
        contenedor.getStyleClass().add("toast");
        contenedor.getStyleClass().add(claseDeTipo(tipo));
        // Heredar los estilos de la escena para que el toast respete el tema actual.
        contenedor.getStylesheets().addAll(origen.getScene().getStylesheets());

        Popup popup = new Popup();
        popup.getContent().add(contenedor);
        popup.setAutoFix(true);

        popup.show(ventana);

        // Forzar el cálculo de tamaño para poder centrar horizontalmente.
        contenedor.applyCss();
        contenedor.layout();
        double ancho = contenedor.getWidth() > 0 ? contenedor.getWidth() : contenedor.prefWidth(-1);
        popup.setX(ventana.getX() + (ventana.getWidth() - ancho) / 2);
        popup.setY(ventana.getY() + ventana.getHeight() - 90);

        FadeTransition aparecer = new FadeTransition(FADE_IN, contenedor);
        aparecer.setFromValue(0);
        aparecer.setToValue(1);

        PauseTransition esperar = new PauseTransition(VISIBLE);

        FadeTransition desaparecer = new FadeTransition(FADE_OUT, contenedor);
        desaparecer.setFromValue(1);
        desaparecer.setToValue(0);

        SequentialTransition secuencia = new SequentialTransition(aparecer, esperar, desaparecer);
        secuencia.setOnFinished(e -> popup.hide());
        secuencia.play();
    }

    private static String claseDeTipo(Tipo tipo) {
        switch (tipo) {
            case SUCCESS: return "toast-success";
            case DANGER:  return "toast-danger";
            default:      return "toast-info";
        }
    }
}
