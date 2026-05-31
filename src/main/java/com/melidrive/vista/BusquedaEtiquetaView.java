package com.melidrive.vista;

import com.melidrive.controlador.MainController;
import com.melidrive.modelo.DriveFile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

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
        this.setStyle("-fx-background-color: #ecf0f1;");

        // Titulo
        Label titulo = new Label("Buscar por Etiqueta");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label descripcion = new Label("Escribe el nombre de una etiqueta para encontrar archivos asociados.");
        descripcion.setStyle("-fx-font-size: 13px; -fx-text-fill: #7f8c8d;");

        // Campo de busqueda
        HBox barBusqueda = new HBox(10);
        barBusqueda.setAlignment(Pos.CENTER_LEFT);

        TextField campoBusqueda = new TextField();
        campoBusqueda.setPromptText("Nombre de etiqueta...");
        campoBusqueda.setPrefWidth(350);
        campoBusqueda.setStyle("-fx-background-color: white; -fx-background-radius: 8; "
                + "-fx-padding: 10; -fx-font-size: 13px;");

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle("-fx-background-color: #6c5ce7; -fx-text-fill: white; "
                + "-fx-cursor: hand; -fx-font-size: 13px; -fx-background-radius: 8; -fx-padding: 10 20;");

        barBusqueda.getChildren().addAll(campoBusqueda, btnBuscar);

        // Label de resultados
        labelResultados = new Label();
        labelResultados.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Contenedor de resultados
        contenedorResultados = new FlowPane();
        contenedorResultados.setHgap(12);
        contenedorResultados.setVgap(12);

        ScrollPane scroll = new ScrollPane(contenedorResultados);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #ecf0f1; -fx-background-color: #ecf0f1;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        // Accion de busqueda
        btnBuscar.setOnAction(e -> ejecutarBusqueda(campoBusqueda.getText()));
        campoBusqueda.setOnAction(e -> ejecutarBusqueda(campoBusqueda.getText()));

        // Boton volver
        Button btnVolver = new Button("Volver al Explorador");
        btnVolver.setStyle("-fx-background-color: #636e72; -fx-text-fill: white; "
                + "-fx-cursor: hand; -fx-background-radius: 8; -fx-padding: 8 16;");
        btnVolver.setOnAction(e -> mainController.mostrarExplorador());

        getChildren().addAll(titulo, descripcion, barBusqueda, labelResultados, scroll, btnVolver);
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
            sinResultados.setStyle("-fx-text-fill: #b2bec3; -fx-font-size: 14px;");
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
        item.setStyle("-fx-background-color: white; -fx-background-radius: 10; "
                + "-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 6, 0, 0, 2);");

        Label nombre = new Label(archivo.getNombre());
        nombre.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label tipo = new Label(archivo.getTipoMime());
        tipo.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d;");

        // Mostrar etiquetas del archivo
        HBox etiquetasBox = new HBox(5);
        archivo.getEtiquetas().forEach(et -> {
            Label chip = new Label(et.getNombre());
            chip.setStyle("-fx-background-color: " + et.getColorHex() + "22; "
                    + "-fx-text-fill: " + et.getColorHex() + "; "
                    + "-fx-padding: 2 8; -fx-background-radius: 10; -fx-font-size: 10px;");
            etiquetasBox.getChildren().add(chip);
        });

        item.getChildren().addAll(nombre, tipo, etiquetasBox);

        item.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                mainController.mostrarVisorDocumento(archivo);
            }
        });

        // Hover effect
        item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10; "
                + "-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.12), 8, 0, 0, 3);"));
        item.setOnMouseExited(e -> item.setStyle("-fx-background-color: white; -fx-background-radius: 10; "
                + "-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 6, 0, 0, 2);"));

        return item;
    }
}
