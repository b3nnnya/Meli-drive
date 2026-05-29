package com.melidrive.vista;

import com.melidrive.controlador.FlashcardController;
import com.melidrive.modelo.Flashcard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class FlashcardView extends VBox {

    private final FlashcardController controller;

    private Label contenido;
    private ProgressBar progressBar;

    private boolean mostrandoPregunta = true;

    public FlashcardView(FlashcardController controller) {

        this.controller = controller;

        setAlignment(Pos.CENTER);
        setSpacing(20);
        setPadding(new Insets(40));

        Label titulo = new Label("Modo Estudio");
        titulo.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        progressBar = new ProgressBar(0.4);
        progressBar.setPrefWidth(400);

        StackPane card = new StackPane();
        card.getStyleClass().add("flashcard");
        card.setPrefSize(650, 300);

        contenido = new Label("Inicia una sesión de estudio");
        contenido.getStyleClass().add("flashcard-question");
        contenido.setWrapText(true);
        contenido.setMaxWidth(500);

        card.getChildren().add(contenido);

        Button iniciar = new Button("Iniciar Sesión");
        iniciar.getStyleClass().add("primary-button");

        Button facil = new Button("✅ Fácil");
        facil.getStyleClass().add("primary-button");

        Button dificil = new Button("❌ Difícil");
        dificil.getStyleClass().add("secondary-button");

        iniciar.setOnAction(e -> {
            controller.iniciarSesionDeEstudio();
            mostrarActual();
        });

        facil.setOnAction(e -> {
            controller.calificarYAvanzar(2);
            mostrarActual();
        });

        dificil.setOnAction(e -> {
            controller.calificarYAvanzar(1);
            mostrarActual();
        });

        card.setOnMouseClicked(e -> voltear());

        getChildren().addAll(
                titulo,
                progressBar,
                iniciar,
                card,
                facil,
                dificil
        );
    }

    private void mostrarActual() {

        Flashcard actual = controller.getFlashcardActual();

        if (actual == null) {
            contenido.setText("🎉 Sesión completada");
            return;
        }

        mostrandoPregunta = true;
        contenido.setText(actual.getPregunta());
    }

    private void voltear() {

        Flashcard actual = controller.getFlashcardActual();

        if (actual == null) return;

        if (mostrandoPregunta) {
            contenido.setText(actual.getRespuesta());
        } else {
            contenido.setText(actual.getPregunta());
        }

        mostrandoPregunta = !mostrandoPregunta;
    }
}