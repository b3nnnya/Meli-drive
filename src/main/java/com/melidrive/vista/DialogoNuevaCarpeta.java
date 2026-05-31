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
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #2c3e50;");

        TextField campo = new TextField();
        campo.setPromptText("Ej: Anatomía, Parcial 1...");
        campo.setStyle("-fx-padding: 8; -fx-background-radius: 5;");

        Label labelError = new Label("El nombre no puede estar vacío.");
        labelError.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 11px;");
        labelError.setVisible(false);

        Button btnCrear = new Button("Crear");
        btnCrear.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 8 16; -fx-background-radius: 5;");
        btnCrear.setOnAction(e -> {
            String texto = campo.getText();
            if (texto != null && !texto.trim().isEmpty()) {
                resultado = texto.trim();
                ventana.close();
            } else {
                labelError.setVisible(true);
                campo.setStyle("-fx-padding: 8; -fx-background-radius: 5; -fx-border-color: #e74c3c; -fx-border-radius: 5;");
            }
        });

        // Ocultar error al escribir
        campo.textProperty().addListener((obs, oldV, newV) -> {
            labelError.setVisible(false);
            campo.setStyle("-fx-padding: 8; -fx-background-radius: 5;");
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 8 16; -fx-background-radius: 5;");
        btnCancelar.setOnAction(e -> ventana.close());

        HBox botones = new HBox(10, btnCrear, btnCancelar);
        botones.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(8, label, campo, labelError, botones);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #ecf0f1;");

        ventana.setScene(new Scene(root, 340, 180));
        ventana.showAndWait();

        return resultado;
    }
}
