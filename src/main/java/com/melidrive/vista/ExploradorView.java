package com.melidrive.vista;

import com.melidrive.controlador.ExploradorController;
import com.melidrive.controlador.MainController;
import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.DriveFolder;
import com.melidrive.vista.componentes.ArchivoCard;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class ExploradorView extends VBox {

    private final ExploradorController controller;
    private final MainController mainController;

    private TilePane grid;

    public ExploradorView(ExploradorController controller,
                          MainController mainController) {

        this.controller = controller;
        this.mainController = mainController;

        construirUI();

        refrescar();
    }

    private void construirUI() {

        setSpacing(16);
        setPadding(new Insets(20));

        HBox toolbar = new HBox(10);
        toolbar.getStyleClass().add("toolbar");

        Button volver = new Button("← Atrás");
        volver.getStyleClass().add("secondary-button");

        Button nuevaCarpeta = new Button("+ Nueva Carpeta");
        nuevaCarpeta.getStyleClass().add("primary-button");

        Label ruta = new Label();
        ruta.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        volver.setOnAction(e -> {
            controller.volverAtras();
            refrescar();
        });

        toolbar.getChildren().addAll(volver, nuevaCarpeta, ruta);

        grid = new TilePane();
        grid.setHgap(18);
        grid.setVgap(18);
        grid.setPadding(new Insets(10));
        grid.setPrefColumns(5);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        getChildren().addAll(toolbar, scroll);
    }

    private void refrescar() {

        grid.getChildren().clear();

        DriveFolder actual = controller.getCarpetaActual();

        for (DriveFolder carpeta : actual.getSubcarpetas()) {

            VBox folder = new VBox(10);
            folder.getStyleClass().add("file-card");

            Label icono = new Label("📁");
            icono.setStyle("-fx-font-size: 42px;");

            Label nombre = new Label(carpeta.getNombre());
            nombre.getStyleClass().add("file-title");

            folder.getChildren().addAll(icono, nombre);

            folder.setOnMouseClicked(e -> {
                controller.entrarACarpeta(carpeta);
                refrescar();
            });

            grid.getChildren().add(folder);
        }

        for (DriveFile archivo : actual.getArchivos()) {

            ArchivoCard card = new ArchivoCard(archivo);

            card.setOnMouseClicked(e ->
                    mainController.mostrarVisorDocumento(archivo)
            );

            grid.getChildren().add(card);
        }
    }
}