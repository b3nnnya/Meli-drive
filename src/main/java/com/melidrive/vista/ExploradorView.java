package com.melidrive.vista;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class ExploradorView extends VBox {
    private TableView<String> tabla;
    public ExploradorView() {
        tabla = new TableView<>();

        TableColumn<String, String> colNombre = new TableColumn<>("Nombre");

        tabla.getColumns().add(colNombre);

        getChildren().add(tabla);
    }
    public void cargarArchivos() {}
}
