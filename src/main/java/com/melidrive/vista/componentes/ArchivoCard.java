package com.melidrive.vista.componentes;

import com.melidrive.modelo.DriveFile;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ArchivoCard extends VBox {

    public ArchivoCard(DriveFile archivo) {

        getStyleClass().add("file-card");
        setAlignment(Pos.TOP_LEFT);

        Label icono = new Label("📄");
        icono.setStyle("-fx-font-size: 42px;");

        Label nombre = new Label(archivo.getNombre());
        nombre.getStyleClass().add("file-title");
        nombre.setWrapText(true);

        Label subtitulo = new Label(archivo.getTipoMime());
        subtitulo.getStyleClass().add("file-subtitle");

        getChildren().addAll(icono, nombre, subtitulo);

        setOnMouseEntered(e -> animarHover(1.03));
        setOnMouseExited(e -> animarHover(1.0));
    }

    private void animarHover(double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(120), this);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }
}