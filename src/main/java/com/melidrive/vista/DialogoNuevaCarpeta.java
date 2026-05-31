package com.melidrive.vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Diálogo modal para crear una nueva carpeta.
 * Devuelve el nombre ingresado o null si se canceló.
 */
public class DialogoNuevaCarpeta {

    private String resultado = null;

    /**
     * Muestra el diálogo y espera a que el usuario ingrese un nombre.
     * @return el nombre de la carpeta o null si se canceló
     */
    public String mostrar() {
        Stage ventana = new Stage();
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.setTitle("Nueva Carpeta");
        ventana.setResizable(false);

        Label label = new Label("Nombre de la carpeta:");
        label.getStyleClass().add("text-h3");

        TextField campo = new TextField();
        campo.setPromptText("Ej: Anatomía, Parcial 1...");
        campo.getStyleClass().add("modern-text-field");

        Label labelError = new Label("El nombre no puede estar vacío.");
        labelError.getStyleClass().add("text-caption");
        labelError.setStyle("-fx-text-fill: #e74c3c;");
        labelError.setVisible(false);

        Button btnCrear = new Button("Crear");
        btnCrear.getStyleClass().add("modern-button-primary");
        btnCrear.setOnAction(e -> {
            String texto = campo.getText();
            if (texto != null && !texto.trim().isEmpty()) {
                resultado = texto.trim();
                ventana.close();
            } else {
                labelError.setVisible(true);
                campo.setStyle("-fx-border-color: #e74c3c;");
            }
        });

        // Ocultar error al escribir
        campo.textProperty().addListener((obs, oldV, newV) -> {
            labelError.setVisible(false);
            campo.setStyle("");
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("modern-button-secondary");
        btnCancelar.setOnAction(e -> ventana.close());

        HBox botones = new HBox(10, btnCrear, btnCancelar);
        botones.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(8, label, campo, labelError, botones);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("content-area");

        Scene scene = new Scene(root, 360, 200);
        try {
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        } catch (Exception ex) { }
        ventana.setScene(scene);
        ventana.showAndWait();

        return resultado;
    }
}
