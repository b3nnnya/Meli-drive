package com.melidrive.vista;

import com.melidrive.controlador.FlashcardController;
import com.melidrive.controlador.ExploradorController;
import com.melidrive.controlador.MainController;
import com.melidrive.controlador.VisorDocumentoController;
import com.melidrive.modelo.DriveFile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

/**
 * Vista principal de la aplicación. Define el layout general:
 * barra superior (buscador), sidebar izquierdo (navegación) y área central (contenido dinámico).
 */
public class MainView extends BorderPane {

    private MainController mainController;
    private StackPane areaCentral;
    private Button btnExplorador;
    private Button btnFlashcards;

    public MainView(MainController mainController) {
        this.mainController = mainController;

        // === BARRA SUPERIOR ===
        TextField buscador = new TextField();
        buscador.setPromptText("Buscar en tus archivos...");
        buscador.setPrefWidth(400);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setOnAction(e -> {
            String termino = buscador.getText();
            if (termino != null && !termino.trim().isEmpty()) {
                List<DriveFile> resultados = mainController.getGestorArchivos()
                        .buscarArchivosPorNombre(termino);
                mostrarResultadosBusqueda(resultados, termino);
            }
        });

        HBox topBar = new HBox(10, buscador, btnBuscar);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #2c3e50;");
        buscador.setStyle("-fx-background-color: #ecf0f1; -fx-prompt-text-fill: #7f8c8d;");
        btnBuscar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;");

        setTop(topBar);

        // === SIDEBAR ===
        VBox sidebar = new VBox(8);
        sidebar.setPadding(new Insets(15, 10, 10, 10));
        sidebar.setPrefWidth(180);
        sidebar.setStyle("-fx-background-color: #34495e;");

        Label tituloSidebar = new Label("Meli-Drive");
        tituloSidebar.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ecf0f1;");

        Separator separador = new Separator();

        btnExplorador = new Button("📁  Mi Unidad");
        btnFlashcards = new Button("📚  Modo Estudio");

        String estiloBoton = "-fx-background-color: transparent; -fx-text-fill: #bdc3c7; "
                + "-fx-font-size: 13px; -fx-cursor: hand; -fx-alignment: CENTER-LEFT; -fx-pref-width: 160;";
        String estiloBotonActivo = "-fx-background-color: #2c3e50; -fx-text-fill: #ecf0f1; "
                + "-fx-font-size: 13px; -fx-cursor: hand; -fx-alignment: CENTER-LEFT; -fx-pref-width: 160;";

        btnExplorador.setStyle(estiloBotonActivo);
        btnFlashcards.setStyle(estiloBoton);

        btnExplorador.setOnAction(e -> {
            mainController.mostrarExplorador();
            btnExplorador.setStyle(estiloBotonActivo);
            btnFlashcards.setStyle(estiloBoton);
        });

        btnFlashcards.setOnAction(e -> {
            mainController.mostrarFlashcards();
            btnFlashcards.setStyle(estiloBotonActivo);
            btnExplorador.setStyle(estiloBoton);
        });

        sidebar.getChildren().addAll(tituloSidebar, separador, btnExplorador, btnFlashcards);
        setLeft(sidebar);

        // === ÁREA CENTRAL ===
        areaCentral = new StackPane();
        areaCentral.setStyle("-fx-background-color: #ecf0f1;");
        setCenter(areaCentral);
    }

    /**
     * Reemplaza el contenido central con la vista del Explorador.
     */
    public void mostrarExplorador(ExploradorController controller) {
        ExploradorView vista = new ExploradorView(controller, mainController);
        areaCentral.getChildren().setAll(vista);
    }

    /**
     * Reemplaza el contenido central con la vista de Flashcards.
     */
    public void mostrarFlashcards(FlashcardController controller) {
        FlashcardView vista = new FlashcardView(controller);
        areaCentral.getChildren().setAll(vista);
    }

    /**
     * Reemplaza el contenido central con el Visor de Documentos.
     */
    public void mostrarVisorDocumento(VisorDocumentoController controller) {
        VisorDocumentoView vista = new VisorDocumentoView(controller, mainController);
        areaCentral.getChildren().setAll(vista);
    }

    /**
     * Muestra resultados de búsqueda en el área central.
     */
    private void mostrarResultadosBusqueda(List<DriveFile> resultados, String termino) {
        VBox contenedor = new VBox(10);
        contenedor.setPadding(new Insets(15));

        Label titulo = new Label("Resultados para: \"" + termino + "\" (" + resultados.size() + ")");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        contenedor.getChildren().add(titulo);

        if (resultados.isEmpty()) {
            Label sinResultados = new Label("No se encontraron archivos.");
            sinResultados.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");
            contenedor.getChildren().add(sinResultados);
        } else {
            for (DriveFile archivo : resultados) {
                Button item = new Button("📄  " + archivo.getNombre()
                        + "  (" + archivo.getTipoMime() + ")");
                item.setStyle("-fx-background-color: white; -fx-cursor: hand; "
                        + "-fx-font-size: 13px; -fx-pref-width: 500; -fx-alignment: CENTER-LEFT;");
                item.setOnAction(e -> mainController.mostrarVisorDocumento(archivo));
                contenedor.getChildren().add(item);
            }
        }

        Button btnVolver = new Button("← Volver al Explorador");
        btnVolver.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;");
        btnVolver.setOnAction(e -> mainController.mostrarExplorador());
        contenedor.getChildren().add(btnVolver);

        areaCentral.getChildren().setAll(contenedor);
    }
}
