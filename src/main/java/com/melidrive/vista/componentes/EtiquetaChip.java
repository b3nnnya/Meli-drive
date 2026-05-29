package com.melidrive.vista.componentes;

import com.melidrive.modelo.Etiqueta;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * Componente visual reutilizable para mostrar etiquetas tipo chip.
 */
public class EtiquetaChip extends HBox {

    private final Etiqueta etiqueta;

    public EtiquetaChip(Etiqueta etiqueta) {

        this.etiqueta = etiqueta;

        setAlignment(Pos.CENTER_LEFT);
        setSpacing(6);
        setPadding(new Insets(6, 12, 6, 12));

        // Color de fondo suave basado en el color de la etiqueta
        String color = etiqueta.getColorHex();

        setStyle(
                "-fx-background-color: " + convertirColorSuave(color) + ";" +
                        "-fx-background-radius: 18;" +
                        "-fx-border-radius: 18;"
        );

        // Punto de color
        Label punto = new Label("●");
        punto.setStyle(
                "-fx-text-fill: " + color + ";" +
                        "-fx-font-size: 11px;"
        );

        // Texto
        Label texto = new Label(etiqueta.getNombre());
        texto.setStyle(
                "-fx-text-fill: #1f2937;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;"
        );

        getChildren().addAll(punto, texto);
    }

    /**
     * Convierte un color fuerte HEX a una versión pastel/suave.
     */
    private String convertirColorSuave(String hex) {

        try {

            Color color = Color.web(hex);

            int r = (int) ((color.getRed() * 255 + 255) / 2);
            int g = (int) ((color.getGreen() * 255 + 255) / 2);
            int b = (int) ((color.getBlue() * 255 + 255) / 2);

            return String.format(
                    "rgb(%d,%d,%d)",
                    r, g, b
            );

        } catch (Exception e) {

            return "#e5e7eb";
        }
    }

    public Etiqueta getEtiqueta() {
        return etiqueta;
    }
}