package com.melidrive.vista;

import com.sun.tools.javac.Main;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MainView extends BorderPane {
    private StackPane areaCentral;
    public MainView() {
        // Barra de Busqueda
        TextField buscador = new TextField();
        buscador.setPromptText("Buscar en tus archivos...");

        HBox topBar = new HBox(buscador);
        topBar.setPadding(new Insets(10));

        setTop(topBar);

        // SideBar
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));

        Button btnExplorador = new Button("Explorador");
        Button btnFlashcards = new Button("Flashcards");

        sidebar.getChildren().addAll(btnExplorador, btnFlashcards);

        setLeft(sidebar);

        // Centro
        areaCentral = new StackPane();
        setCenter(areaCentral);

        // Eventos
        btnExplorador.setOnAction(e -> mostrarExplorador());
        btnFlashcards.setOnAction(e -> mostrarFlashcards());
    }
    private void mostrarExplorador() {
        areaCentral.getChildren().setAll(new ExploradorView());
    }
    private void mostrarFlashcards() {
        areaCentral.getChildren().setAll(new FlashcardView());
    }
}
