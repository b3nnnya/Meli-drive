package com.melidrive.vista;

import com.melidrive.controlador.MainController;
import com.melidrive.controlador.VisorDocumentoController;
import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.Etiqueta;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Vista del Visor de Documentos.
 * Muestra los detalles de un archivo seleccionado,
 * permite gestionar sus etiquetas y volver al explorador.
 */
public class VisorDocumentoView extends HBox {

    private VisorDocumentoController controller;
    private MainController mainController;

    public VisorDocumentoView(VisorDocumentoController controller, MainController mainController) {
        this.controller = controller;
        this.mainController = mainController;
        this.setSpacing(0);

        DriveFile archivo = controller.getArchivoAbierto();

        // === PANEL IZQUIERDO: Contenido del documento ===
        VBox panelContenido = new VBox(15);
        panelContenido.setPadding(new Insets(20));
        panelContenido.setStyle("-fx-background-color: #ecf0f1;");
        HBox.setHgrow(panelContenido, Priority.ALWAYS);

        Button btnVolver = new Button("← Volver al Explorador");
        btnVolver.setStyle("-fx-background-color: #636e72; -fx-text-fill: white; -fx-cursor: hand;");
        btnVolver.setOnAction(e -> mainController.mostrarExplorador());

        Label titulo = new Label("📄 " + (archivo != null ? archivo.getNombre() : "Sin archivo"));
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Info del archivo
        VBox infoBox = new VBox(5);
        if (archivo != null) {
            Label tipo = new Label("Tipo: " + archivo.getTipoMime());
            Label size = new Label("Tamaño: " + formatearSize(archivo.getSizeEnBytes()));
            Label id = new Label("ID: " + archivo.getId());
            tipo.setStyle("-fx-text-fill: #636e72;");
            size.setStyle("-fx-text-fill: #636e72;");
            id.setStyle("-fx-text-fill: #b2bec3; -fx-font-size: 10px;");
            infoBox.getChildren().addAll(tipo, size, id);
        }

        // Área de contenido extraído
        Label labelContenido = new Label("Contenido extraído:");
        labelContenido.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        TextArea areaTexto = new TextArea();
        areaTexto.setEditable(false);
        areaTexto.setWrapText(true);
        areaTexto.setStyle("-fx-font-size: 13px;");
        VBox.setVgrow(areaTexto, Priority.ALWAYS);

        if (archivo != null && !archivo.getContenidoExtraido().isEmpty()) {
            areaTexto.setText(archivo.getContenidoExtraido());
        } else {
            areaTexto.setText("(Sin contenido extraído. El texto aparecerá aquí cuando se integre el OCR.)");
        }

        panelContenido.getChildren().addAll(btnVolver, titulo, infoBox, labelContenido, areaTexto);

        // === PANEL DERECHO: Etiquetas ===
        VBox panelEtiquetas = new VBox(10);
        panelEtiquetas.setPadding(new Insets(15));
        panelEtiquetas.setPrefWidth(220);
        panelEtiquetas.setStyle("-fx-background-color: #dfe6e9;");

        Label tituloEtiquetas = new Label("🏷️ Etiquetas");
        tituloEtiquetas.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox listaEtiquetas = new VBox(5);
        if (archivo != null) {
            for (Etiqueta etiqueta : archivo.getEtiquetas()) {
                HBox fila = new HBox(5);
                fila.setAlignment(Pos.CENTER_LEFT);

                Label chip = new Label("● " + etiqueta.getNombre());
                chip.setStyle("-fx-text-fill: " + etiqueta.getColorHex() + "; -fx-font-size: 12px;");

                Button btnEliminar = new Button("✕");
                btnEliminar.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; "
                        + "-fx-cursor: hand; -fx-font-size: 10px;");
                btnEliminar.setOnAction(e -> {
                    controller.eliminarEtiqueta(etiqueta);
                    mainController.mostrarVisorDocumento(archivo);
                });

                fila.getChildren().addAll(chip, btnEliminar);
                listaEtiquetas.getChildren().add(fila);
            }

            if (archivo.getEtiquetas().isEmpty()) {
                Label sinEtiquetas = new Label("Sin etiquetas");
                sinEtiquetas.setStyle("-fx-text-fill: #b2bec3;");
                listaEtiquetas.getChildren().add(sinEtiquetas);
            }
        }

        // Agregar etiqueta nueva
        Separator sep = new Separator();
        TextField campoEtiqueta = new TextField();
        campoEtiqueta.setPromptText("Nueva etiqueta...");

        Button btnAgregar = new Button("+ Agregar");
        btnAgregar.setStyle("-fx-background-color: #6c5ce7; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px;");
        btnAgregar.setOnAction(e -> {
            String nombre = campoEtiqueta.getText();
            if (nombre != null && !nombre.trim().isEmpty() && archivo != null) {
                Etiqueta nueva = new Etiqueta(
                        "tag-" + System.currentTimeMillis(),
                        nombre.trim(),
                        "#6c5ce7"
                );
                controller.agregarEtiqueta(nueva);
                mainController.mostrarVisorDocumento(archivo);
            }
        });

        panelEtiquetas.getChildren().addAll(tituloEtiquetas, listaEtiquetas, sep, campoEtiqueta, btnAgregar);

        getChildren().addAll(panelContenido, panelEtiquetas);
    }

    /**
     * Formatea bytes a una representación legible.
     */
    private String formatearSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024));
    }
}
