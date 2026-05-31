package com.melidrive.vista;

import com.melidrive.controlador.MainController;
import com.melidrive.controlador.VisorDocumentoController;
import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.Etiqueta;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelReader;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.embed.swing.SwingFXUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Vista del Visor de Documentos.
 * Muestra los detalles de un archivo seleccionado,
 * permite gestionar sus etiquetas, visualizar el documento (PDF/Imagen) y recortar ejercicios.
 */
public class VisorDocumentoView extends HBox {

    private VisorDocumentoController controller;

    private ImageView imageView;
    private Image currentImage;
    private Rectangle seleccionRect;
    private double startX, startY;
    private PDDocument pdfDoc;
    private PDFRenderer pdfRenderer;
    private Pane imageContainer;

    public VisorDocumentoView(VisorDocumentoController controller, MainController mainController) {
        this.controller = controller;
        this.setSpacing(0);

        DriveFile archivo = controller.getArchivoAbierto();

        // === PANEL IZQUIERDO: Contenido del documento ===
        VBox panelContenido = new VBox(15);
        panelContenido.setPadding(new Insets(20));
        panelContenido.getStyleClass().add("content-area");
        HBox.setHgrow(panelContenido, Priority.ALWAYS);

        Button btnVolver = new Button("Volver al Explorador");
        btnVolver.getStyleClass().add("modern-button-secondary");
        btnVolver.setOnAction(e -> {
            if (pdfDoc != null) {
                try { pdfDoc.close(); } catch (IOException ex) { ex.printStackTrace(); }
            }
            mainController.mostrarExplorador();
        });

        Label titulo = new Label(archivo != null ? archivo.getNombre() : "Sin archivo");
        titulo.getStyleClass().add("text-h1");

        // Info del archivo
        VBox infoBox = new VBox(5);
        if (archivo != null) {
            Label tipo = new Label("Tipo: " + archivo.getTipoMime());
            Label size = new Label("Tamaño: " + formatearSize(archivo.getSizeEnBytes()));
            Label id = new Label("ID: " + archivo.getId());
            tipo.getStyleClass().add("text-body");
            size.getStyleClass().add("text-body");
            id.getStyleClass().add("text-small");
            infoBox.getChildren().addAll(tipo, size, id);
        }

        // Visor y Herramienta de Recorte
        HBox toolbarVisor = new HBox(10);
        toolbarVisor.setAlignment(Pos.CENTER_LEFT);
        Label labelContenido = new Label("Visualización del Documento:");
        labelContenido.getStyleClass().add("text-h2");
        
        Button btnRecortar = new Button("Crear Ejercicio desde Recorte");
        btnRecortar.getStyleClass().add("modern-button-primary");
        btnRecortar.setDisable(true); // Se habilita cuando hay un recorte seleccionado
        
        toolbarVisor.getChildren().addAll(labelContenido, btnRecortar);

        imageContainer = new Pane();
        imageView = new ImageView();
        seleccionRect = new Rectangle(0, 0, 0, 0);
        seleccionRect.setStroke(Color.BLUE);
        seleccionRect.setStrokeWidth(2);
        seleccionRect.setFill(Color.rgb(0, 0, 255, 0.2));
        seleccionRect.setVisible(false);

        imageContainer.getChildren().addAll(imageView, seleccionRect);

        ScrollPane scrollVisor = new ScrollPane(imageContainer);
        scrollVisor.getStyleClass().add("scroll-pane");
        VBox.setVgrow(scrollVisor, Priority.ALWAYS);

        // Eventos de ratón para el recorte
        imageContainer.setOnMousePressed(e -> {
            startX = e.getX();
            startY = e.getY();
            seleccionRect.setX(startX);
            seleccionRect.setY(startY);
            seleccionRect.setWidth(0);
            seleccionRect.setHeight(0);
            seleccionRect.setVisible(true);
            btnRecortar.setDisable(true);
        });

        imageContainer.setOnMouseDragged(e -> {
            double currentX = Math.max(0, Math.min(e.getX(), currentImage != null ? currentImage.getWidth() : imageContainer.getWidth()));
            double currentY = Math.max(0, Math.min(e.getY(), currentImage != null ? currentImage.getHeight() : imageContainer.getHeight()));

            double x = Math.min(startX, currentX);
            double y = Math.min(startY, currentY);
            double width = Math.abs(currentX - startX);
            double height = Math.abs(currentY - startY);

            seleccionRect.setX(x);
            seleccionRect.setY(y);
            seleccionRect.setWidth(width);
            seleccionRect.setHeight(height);
        });

        imageContainer.setOnMouseReleased(e -> {
            if (seleccionRect.getWidth() > 10 && seleccionRect.getHeight() > 10) {
                btnRecortar.setDisable(false);
            } else {
                seleccionRect.setVisible(false);
            }
        });

        btnRecortar.setOnAction(e -> {
            if (currentImage != null && seleccionRect.isVisible()) {
                int x = (int) seleccionRect.getX();
                int y = (int) seleccionRect.getY();
                int w = (int) seleccionRect.getWidth();
                int h = (int) seleccionRect.getHeight();

                PixelReader reader = currentImage.getPixelReader();
                WritableImage cropped = new WritableImage(reader, x, y, w, h);

                controller.procesarRecorte(cropped);
                seleccionRect.setVisible(false);
                btnRecortar.setDisable(true);
            }
        });

        // Paginación
        HBox panelPaginacion = new HBox(10);
        panelPaginacion.setAlignment(Pos.CENTER);
        Button btnAnt = new Button("< Anterior");
        Button btnSig = new Button("Siguiente >");
        Label lblPagina = new Label("Página 1 / 1");
        panelPaginacion.getChildren().addAll(btnAnt, lblPagina, btnSig);
        panelPaginacion.setVisible(false);

        // Cargar el documento si hay ruta física
        if (archivo != null && archivo.getRutaFisica() != null) {
            File f = new File(archivo.getRutaFisica());
            if (f.exists()) {
                if ("application/pdf".equals(archivo.getTipoMime())) {
                    try {
                        pdfDoc = PDDocument.load(f);
                        pdfRenderer = new PDFRenderer(pdfDoc);
                        controller.setTotalPaginas(pdfDoc.getNumberOfPages());
                        panelPaginacion.setVisible(true);
                        renderizarPagina(controller.getPaginaActual(), lblPagina);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        mostrarError("No se pudo cargar el PDF: " + ex.getMessage());
                    }
                } else if (archivo.getTipoMime().startsWith("image/")) {
                    currentImage = new Image(f.toURI().toString());
                    actualizarImagen();
                } else {
                    mostrarError("Formato no soportado para visualización directa.");
                }
            } else {
                mostrarError("El archivo físico no se encuentra en la ruta.");
            }
        } else {
            TextArea areaExtraido = new TextArea();
            areaExtraido.setEditable(false);
            areaExtraido.setWrapText(true);
            if (archivo != null && !archivo.getContenidoExtraido().isEmpty()) {
                areaExtraido.setText(archivo.getContenidoExtraido());
            } else {
                areaExtraido.setText("(El archivo no tiene ruta física ni contenido extraído)");
            }
            scrollVisor.setContent(areaExtraido);
        }

        btnAnt.setOnAction(e -> {
            if (controller.getPaginaActual() > 0) {
                controller.setPaginaActual(controller.getPaginaActual() - 1);
                renderizarPagina(controller.getPaginaActual(), lblPagina);
                seleccionRect.setVisible(false);
                btnRecortar.setDisable(true);
            }
        });

        btnSig.setOnAction(e -> {
            if (controller.getPaginaActual() < controller.getTotalPaginas() - 1) {
                controller.setPaginaActual(controller.getPaginaActual() + 1);
                renderizarPagina(controller.getPaginaActual(), lblPagina);
                seleccionRect.setVisible(false);
                btnRecortar.setDisable(true);
            }
        });

        panelContenido.getChildren().addAll(btnVolver, titulo, infoBox, toolbarVisor, scrollVisor, panelPaginacion);

        // === PANEL DERECHO: Etiquetas ===
        VBox panelEtiquetas = new VBox(10);
        panelEtiquetas.setPadding(new Insets(15));
        panelEtiquetas.setPrefWidth(220);
        panelEtiquetas.getStyleClass().add("sidebar");

        Label tituloEtiquetas = new Label("Etiquetas");
        tituloEtiquetas.getStyleClass().add("text-h2");

        VBox listaEtiquetas = new VBox(5);
        if (archivo != null) {
            for (Etiqueta etiqueta : archivo.getEtiquetas()) {
                HBox fila = new HBox(5);
                fila.setAlignment(Pos.CENTER_LEFT);

                Label chip = new Label(etiqueta.getNombre());
                chip.setStyle("-fx-background-color: " + etiqueta.getColorHex() + "22; "
                        + "-fx-text-fill: " + etiqueta.getColorHex() + "; "
                        + "-fx-padding: 4 10; -fx-background-radius: 12; -fx-font-size: 11px;");

                Button btnEliminar = new Button("x");
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
                sinEtiquetas.getStyleClass().add("text-caption");
                listaEtiquetas.getChildren().add(sinEtiquetas);
            }
        }

        // Agregar etiqueta nueva
        Separator sep = new Separator();
        TextField campoEtiqueta = new TextField();
        campoEtiqueta.setPromptText("Nueva etiqueta...");
        campoEtiqueta.getStyleClass().add("modern-text-field");

        Button btnAgregar = new Button("+ Agregar");
        btnAgregar.getStyleClass().add("modern-button-primary");
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

    private void renderizarPagina(int pagina, Label lblPagina) {
        if (pdfRenderer != null) {
            try {
                // Renderizamos la página a 150 DPI para buena calidad de lectura/recorte
                BufferedImage bim = pdfRenderer.renderImageWithDPI(pagina, 150);
                currentImage = SwingFXUtils.toFXImage(bim, null);
                actualizarImagen();
                lblPagina.setText("Página " + (pagina + 1) + " / " + controller.getTotalPaginas());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void actualizarImagen() {
        if (currentImage != null) {
            imageView.setImage(currentImage);
            imageContainer.setPrefSize(currentImage.getWidth(), currentImage.getHeight());
        }
    }

    private void mostrarError(String mensaje) {
        Label lblError = new Label(mensaje);
        lblError.setStyle("-fx-text-fill: red; -fx-padding: 20;");
        imageContainer.getChildren().clear();
        imageContainer.getChildren().add(lblError);
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
