package com.melidrive;

import com.melidrive.vista.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal de la Interfaz Gráfica (Vista).
 * Aquí arranca S.I.G.E.A de manera visual usando JavaFX.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainView mainView = new MainView();

        Scene scene = new Scene(mainView, 900, 600);

        primaryStage.setTitle("Meli-Drive - Gestión Académica");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
