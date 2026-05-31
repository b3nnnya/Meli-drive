package com.melidrive.vista;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogoNuevoEjercicio {

    public static class ResultadoEjercicio {
        public final String titulo;
        public final String solucion;

        public ResultadoEjercicio(String titulo, String solucion) {
            this.titulo = titulo;
            this.solucion = solucion;
        }
    }

    public static ResultadoEjercicio mostrar(Image recorteImagen) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Nuevo Ejercicio (Flashcard)");
        window.setMinWidth(400);

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label labelInstrucciones = new Label("Se creará una nueva Flashcard con la siguiente imagen como pregunta.");
        labelInstrucciones.setWrapText(true);

        ImageView imageView = new ImageView(recorteImagen);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(350);
        imageView.setFitHeight(200);
        
        ScrollPane scrollPane = new ScrollPane(imageView);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);

        TextField fieldTitulo = new TextField();
        fieldTitulo.setPromptText("Título / Pregunta breve");

        TextArea areaSolucion = new TextArea();
        areaSolucion.setPromptText("Escribe la solución detallada aquí...");
        areaSolucion.setWrapText(true);

        Button btnGuardar = new Button("Guardar Ejercicio");
        btnGuardar.setStyle("-fx-background-color: #6c5ce7; -fx-text-fill: white; -fx-cursor: hand;");

        final ResultadoEjercicio[] resultado = {null};

        btnGuardar.setOnAction(e -> {
            String titulo = fieldTitulo.getText().trim();
            String solucion = areaSolucion.getText().trim();
            if (!titulo.isEmpty() && !solucion.isEmpty()) {
                resultado[0] = new ResultadoEjercicio(titulo, solucion);
                window.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor llena el título y la solución.");
                alert.showAndWait();
            }
        });

        layout.getChildren().addAll(labelInstrucciones, scrollPane, fieldTitulo, areaSolucion, btnGuardar);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return resultado[0];
    }
}
