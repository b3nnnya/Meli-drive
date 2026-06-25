package com.melidrive.vista;

import com.melidrive.controlador.MainController;
import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.Etiqueta;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

/**
 * Vista para buscar archivos por etiqueta.
 * Muestra un campo de busqueda y la lista de resultados clickeables.
 */
public class BusquedaEtiquetaView extends VBox {

    private MainController mainController;
    private FlowPane contenedorResultados;
    private Label labelResultados;

    public BusquedaEtiquetaView(MainController mainController) {
        this.mainController = mainController;
        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.getStyleClass().add("content-area");

        // Titulo
        Label titulo = new Label("Buscar por Etiqueta");
        titulo.getStyleClass().add("text-h1");

        Label descripcion = new Label("Escribe el nombre de una etiqueta para encontrar archivos asociados.");
        descripcion.getStyleClass().add("text-body");

        // Campo de busqueda
        HBox barBusqueda = new HBox(10);
        barBusqueda.setAlignment(Pos.CENTER_LEFT);

        TextField campoBusqueda = new TextField();
        campoBusqueda.setPromptText("Nombre de etiqueta...");
        campoBusqueda.setPrefWidth(350);
        campoBusqueda.getStyleClass().add("search-field");

        Button btnBuscar = new Button("Buscar");
        btnBuscar.getStyleClass().add("modern-button-primary");

        barBusqueda.getChildren().addAll(campoBusqueda, btnBuscar);

        // Label de resultados
        labelResultados = new Label();
        labelResultados.getStyleClass().add("text-h2");

        // Contenedor de resultados
        contenedorResultados = new FlowPane();
        contenedorResultados.setHgap(12);
        contenedorResultados.setVgap(12);

        ScrollPane scroll = new ScrollPane(contenedorResultados);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-pane");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        // Accion de busqueda
        btnBuscar.setOnAction(e -> ejecutarBusqueda(campoBusqueda.getText()));
        campoBusqueda.setOnAction(e -> ejecutarBusqueda(campoBusqueda.getText()));

        // Boton volver
        Button btnVolver = new Button("Volver al Explorador");
        btnVolver.getStyleClass().add("modern-button-secondary");
        btnVolver.setOnAction(e -> mainController.mostrarExplorador());

        // === ETIQUETAS EXISTENTES (clickeables) ===
        Label tituloExistentes = new Label("Etiquetas existentes:");
        tituloExistentes.getStyleClass().add("text-h3");

        FlowPane panelEtiquetasExistentes = new FlowPane();
        panelEtiquetasExistentes.setHgap(8);
        panelEtiquetasExistentes.setVgap(8);

        List<Etiqueta> existentes = mainController.getGestorArchivos().obtenerTodasLasEtiquetas();
        if (existentes.isEmpty()) {
            Label vacio = new Label("(Aún no hay etiquetas. Agrégalas a tus archivos desde el visor de documentos.)");
            vacio.getStyleClass().add("text-caption");
            panelEtiquetasExistentes.getChildren().add(vacio);
        } else {
            for (Etiqueta et : existentes) {
                Button chip = new Button(et.getNombre());
                chip.getStyleClass().add("tag-chip-lg");
                chip.setStyle("-fx-background-color: " + et.getColorHex() + "22; "
                        + "-fx-text-fill: " + et.getColorHex() + ";");
                chip.setOnAction(e -> {
                    campoBusqueda.setText(et.getNombre());
                    ejecutarBusqueda(et.getNombre());
                });
                panelEtiquetasExistentes.getChildren().add(chip);
            }
        }

        getChildren().addAll(titulo, descripcion, barBusqueda,
                tituloExistentes, panelEtiquetasExistentes,
                labelResultados, scroll, btnVolver);
    }

    /**
     * Ejecuta la busqueda de archivos por etiqueta y muestra los resultados.
     */
    private void ejecutarBusqueda(String termino) {
        contenedorResultados.getChildren().clear();

        if (termino == null || termino.trim().isEmpty()) {
            labelResultados.setText("Ingresa un termino de busqueda.");
            return;
        }

        List<DriveFile> resultados = mainController.getGestorArchivos()
                .buscarArchivosPorEtiqueta(termino.trim());

        labelResultados.setText("Resultados para \"" + termino + "\": " + resultados.size() + " archivo(s)");

        if (resultados.isEmpty()) {
            Label sinResultados = new Label("No se encontraron archivos con esa etiqueta.");
            sinResultados.getStyleClass().add("text-caption");
            contenedorResultados.getChildren().add(sinResultados);
        } else {
            for (DriveFile archivo : resultados) {
                VBox item = crearItemResultado(archivo);
                contenedorResultados.getChildren().add(item);
            }
        }
    }

    /**
     * Crea un elemento visual para un resultado de busqueda.
     */
    private VBox crearItemResultado(DriveFile archivo) {
        VBox item = new VBox(5);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(12));
        item.setPrefWidth(250);
        item.getStyleClass().add("modern-card");

        Label nombre = new Label(archivo.getNombre());
        nombre.getStyleClass().add("text-body");
        nombre.setStyle("-fx-font-weight: bold;");

        Label tipo = new Label(archivo.getTipoMime());
        tipo.getStyleClass().add("text-caption");

        // Mostrar etiquetas del archivo
        HBox etiquetasBox = new HBox(5);
        archivo.getEtiquetas().forEach(et -> {
            Label chip = new Label(et.getNombre());
            chip.getStyleClass().add("tag-chip");
            chip.setStyle("-fx-background-color: " + et.getColorHex() + "22; "
                    + "-fx-text-fill: " + et.getColorHex() + ";");
            etiquetasBox.getChildren().add(chip);
        });

        item.getChildren().addAll(nombre, tipo, etiquetasBox);

        item.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                mainController.mostrarVisorDocumento(archivo);
            }
        });

        // Hover effect animado
        item.setOnMouseEntered(e -> animarEscala(item, 1.03));
        item.setOnMouseExited(e -> animarEscala(item, 1.0));

        return item;
    }

    private void animarEscala(VBox nodo, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), nodo);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }
}
