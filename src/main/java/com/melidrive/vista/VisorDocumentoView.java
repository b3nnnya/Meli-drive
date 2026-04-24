package com.melidrive.vista;

import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class VisorDocumentoView extends HBox {

    public VisorDocumentoView() {

        TextArea areaTexto = new TextArea();
        areaTexto.setEditable(false);

        VBox panelEtiquetas = new VBox();

        getChildren().addAll(areaTexto, panelEtiquetas);
    }
}
