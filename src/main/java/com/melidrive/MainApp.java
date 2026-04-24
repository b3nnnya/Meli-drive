package com.melidrive;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Clase principal de la Interfaz Gráfica (Vista).
 * Aquí arranca S.I.G.E.A de manera visual usando JavaFX.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Crear un texto básico de saludo
        Label etiquetaSaludo = new Label("¡Bienvenido a S.I.G.E.A (Meli-Drive)!");
        etiquetaSaludo.setStyle("-fx-font-size: 24px; -fx-font-family: 'Arial'; -fx-text-fill: #2c3e50;");

        // 2. Layout base (StackPane centra todo por defecto)
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #ecf0f1;"); // Fondo claro moderno
        root.getChildren().add(etiquetaSaludo);

        // 3. Crear la escena (tamaño inicial)
        Scene scene = new Scene(root, 800, 600);

        // 4. Configurar la ventana principal (Stage)
        primaryStage.setTitle("Meli-Drive - Gestión Académica");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Enciende el motor de JavaFX
        launch(args);
    }
}
