package com.melidrive.vista.componentes;

import com.melidrive.controlador.MainController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class TopBar extends HBox {

    public TopBar(MainController mainController) {

        getStyleClass().add("topbar");
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(12);

        TextField buscador = new TextField();
        buscador.setPromptText("Buscar archivos...");
        buscador.getStyleClass().add("search-field");

        Button buscar = new Button("Buscar");
        buscar.getStyleClass().add("primary-button");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        Button darkMode = new Button("🌙");
        darkMode.getStyleClass().add("secondary-button");

        getChildren().addAll(
                buscador,
                buscar,
                spacer,
                darkMode
        );
    }
}