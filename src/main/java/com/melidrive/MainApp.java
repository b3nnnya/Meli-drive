package com.melidrive;

import com.melidrive.controlador.MainController;
import com.melidrive.vista.MainView;
import com.melidrive.vista.BienvenidaView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Clase principal de la Interfaz Gráfica (Vista).
 * Aquí arranca S.I.G.E.A de manera visual usando JavaFX.
 */
public class MainApp extends Application {

    private MainController mainController;

    @Override
    public void start(Stage primaryStage) {
        // Inversión de Dependencias: se inyecta la implementación de persistencia.
        mainController = new MainController(new com.melidrive.util.RepositorioDatos());
        MainView mainView = new MainView(mainController);
        mainController.setMainView(mainView);

        // La escena arranca con un contenedor temporal; mostramos primero la
        // pantalla de bienvenida y solo al elegir una opción asociamos la app
        // principal (así sus atajos de teclado se registran una única vez).
        StackPane raizTemporal = new StackPane();
        Scene scene = new Scene(raizTemporal, 960, 640);
        
        com.melidrive.util.ThemeManager themeManager = new com.melidrive.util.ThemeManager();
        themeManager.setScene(scene);
        themeManager.aplicarEstiloPrincipal();
        
        mainController.setThemeManager(themeManager);

        // Pantalla de bienvenida (launcher): cualquier opción entra a la app principal.
        BienvenidaView bienvenida = new BienvenidaView(() -> scene.setRoot(mainView));
        scene.setRoot(bienvenida);

        primaryStage.setTitle("SIGEA - Gestión Académica");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Al cerrar la aplicación, persistir el estado completo en disco.
        if (mainController != null) {
            mainController.guardarEstado();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
