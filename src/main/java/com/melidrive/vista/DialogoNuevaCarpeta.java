package com.melidrive.vista;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogoNuevaCarpeta {
    public void mostrar() {

        Stage ventana = new Stage();
        ventana.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label("Nombre de la carpeta:");
        TextField campo = new TextField();

        Button btnCrear = new Button("Crear");
        Button btnCancelar = new Button("Cancelar");

        btnCancelar.setOnAction(e -> ventana.close());
        VBox root = new VBox(10, label, campo, btnCrear, btnCancelar);

        ventana.setScene(new Scene(root, 300, 200));
        ventana.showAndWait();
    }
}
