package com.melidrive.vista;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class FlashcardView extends VBox {
    private boolean mostrandoPregunta = true;
    public FlashcardView () {
        StackPane tarjeta = new StackPane();
        Label contenido = new Label("Pregunta");

        tarjeta.getChildren().add(contenido);

        tarjeta.setOnMouseClicked(e -> {
            if (mostrandoPregunta) {
                contenido.setText("Respuesta");
            } else {
                contenido.setText("Pregunta");
            }
            mostrandoPregunta = !mostrandoPregunta;
        });

        Button btnBien = new Button("Fácil");
        Button btnMal = new Button("Difícil");

        getChildren().addAll(tarjeta, btnBien, btnMal);
    }

}
