package com.melidrive.vista;

import com.melidrive.controlador.FlashcardController;
import com.melidrive.modelo.Flashcard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

/**
 * Vista del Modo Estudio (Flashcards).
 * Muestra tarjetas de repaso espaciado con funcionalidad de voltear
 * y calificar (Fácil / Difícil).
 */
public class FlashcardView extends VBox {

    private FlashcardController controller;
    private boolean mostrandoPregunta = true;

    private Label labelContenido;
    private Label labelProgreso;
    private Label labelIndicador;
    private Button btnFacil;
    private Button btnDificil;
    private StackPane tarjeta;
    private VBox contenedorBotones;

    public FlashcardView(FlashcardController controller) {
        this.controller = controller;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(30));
        this.setStyle("-fx-background-color: #ecf0f1;");

        // Título
        Label titulo = new Label("📚 Modo Estudio - Repaso Espaciado");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Progreso
        labelProgreso = new Label();
        labelProgreso.setStyle("-fx-font-size: 13px; -fx-text-fill: #7f8c8d;");

        // Indicador (Pregunta / Respuesta)
        labelIndicador = new Label("PREGUNTA");
        labelIndicador.setStyle("-fx-font-size: 11px; -fx-text-fill: #3498db; -fx-font-weight: bold;");

        // Tarjeta
        tarjeta = new StackPane();
        tarjeta.setPrefSize(500, 200);
        tarjeta.setMaxSize(500, 200);
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                + "-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 8, 0, 0, 4);");

        labelContenido = new Label();
        labelContenido.setStyle("-fx-font-size: 18px; -fx-text-fill: #2d3436; -fx-wrap-text: true;");
        labelContenido.setWrapText(true);
        labelContenido.setMaxWidth(450);
        labelContenido.setAlignment(Pos.CENTER);

        tarjeta.getChildren().add(labelContenido);

        Label instruccion = new Label("(Haz clic en la tarjeta para voltearla)");
        instruccion.setStyle("-fx-font-size: 11px; -fx-text-fill: #b2bec3;");

        tarjeta.setOnMouseClicked(e -> voltearTarjeta());

        // Botones de calificación
        btnFacil = new Button("✅ Fácil");
        btnFacil.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-cursor: hand; -fx-pref-width: 120;");
        btnFacil.setOnAction(e -> calificar(2));

        btnDificil = new Button("❌ Difícil");
        btnDificil.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-cursor: hand; -fx-pref-width: 120;");
        btnDificil.setOnAction(e -> calificar(1));

        contenedorBotones = new VBox(10);
        contenedorBotones.setAlignment(Pos.CENTER);
        HBox filaBotones = new HBox(20, btnDificil, btnFacil);
        filaBotones.setAlignment(Pos.CENTER);
        contenedorBotones.getChildren().add(filaBotones);

        // Botón iniciar sesión
        Button btnIniciar = new Button("▶ Iniciar Sesión de Estudio");
        btnIniciar.setStyle("-fx-background-color: #6c5ce7; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-cursor: hand;");
        btnIniciar.setOnAction(e -> {
            controller.iniciarSesionDeEstudio();
            mostrarTarjetaActual();
        });

        getChildren().addAll(titulo, labelProgreso, btnIniciar, labelIndicador, tarjeta, instruccion, contenedorBotones);

        // Estado inicial
        int pendientes = controller.getTarjetasPendientesHoy();
        labelProgreso.setText("Tarjetas pendientes hoy: " + pendientes
                + " | Total en el sistema: " + controller.getTotalFlashcards());
        labelContenido.setText("Presiona \"Iniciar Sesión\" para comenzar");
        contenedorBotones.setVisible(false);
        labelIndicador.setVisible(false);
    }

    /**
     * Muestra la tarjeta actual de la sesión (lado pregunta).
     */
    private void mostrarTarjetaActual() {
        Flashcard actual = controller.getFlashcardActual();
        if (actual == null) {
            mostrarSesionFinalizada();
            return;
        }

        mostrandoPregunta = true;
        labelContenido.setText(actual.getPregunta());
        labelIndicador.setText("PREGUNTA");
        labelIndicador.setStyle("-fx-font-size: 11px; -fx-text-fill: #3498db; -fx-font-weight: bold;");
        labelIndicador.setVisible(true);
        contenedorBotones.setVisible(false);

        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                + "-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 8, 0, 0, 4);");
    }

    /**
     * Voltea la tarjeta entre pregunta y respuesta.
     */
    private void voltearTarjeta() {
        Flashcard actual = controller.getFlashcardActual();
        if (actual == null) return;

        if (mostrandoPregunta) {
            labelContenido.setText(actual.getRespuesta());
            labelIndicador.setText("RESPUESTA");
            labelIndicador.setStyle("-fx-font-size: 11px; -fx-text-fill: #e17055; -fx-font-weight: bold;");
            tarjeta.setStyle("-fx-background-color: #ffeaa7; -fx-background-radius: 12; "
                    + "-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 8, 0, 0, 4);");
            contenedorBotones.setVisible(true);
        } else {
            labelContenido.setText(actual.getPregunta());
            labelIndicador.setText("PREGUNTA");
            labelIndicador.setStyle("-fx-font-size: 11px; -fx-text-fill: #3498db; -fx-font-weight: bold;");
            tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                    + "-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 8, 0, 0, 4);");
            contenedorBotones.setVisible(false);
        }
        mostrandoPregunta = !mostrandoPregunta;
    }

    /**
     * Califica la tarjeta actual y avanza a la siguiente.
     */
    private void calificar(int calificacion) {
        controller.calificarYAvanzar(calificacion);
        mostrarTarjetaActual();
    }

    /**
     * Muestra mensaje de sesión completada.
     */
    private void mostrarSesionFinalizada() {
        labelContenido.setText("🎉 ¡Sesión completada!\nNo hay más tarjetas por repasar hoy.");
        labelContenido.setStyle("-fx-font-size: 16px; -fx-text-fill: #27ae60; -fx-wrap-text: true;");
        labelIndicador.setVisible(false);
        contenedorBotones.setVisible(false);
        tarjeta.setStyle("-fx-background-color: #d5f5e3; -fx-background-radius: 12; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 6, 0, 0, 3);");
    }
}
