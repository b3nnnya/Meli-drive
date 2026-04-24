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

        Button btnCrear = new Button("Crear");
        btnCrear.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand;");
        btnCrear.setOnAction(e -> {
            String texto = campo.getText();
            if (texto != null && !texto.trim().isEmpty()) {
                resultado = texto.trim();
                ventana.close();
            }
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> ventana.close());

        HBox botones = new HBox(10, btnCrear, btnCancelar);
        botones.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(12, label, campo, botones);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #ecf0f1;");

        ventana.setScene(new Scene(root, 320, 160));
        ventana.showAndWait();

        return resultado;
    }
}
