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
    private Button btnBuscarEtiqueta;

    public MainView(MainController mainController) {
        this.mainController = mainController;

        try {
            this.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("No se pudo cargar main.css en MainView");
        }

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

        Button btnModoOscuro = new Button("Modo Oscuro");
        btnModoOscuro.getStyleClass().add("modern-button-secondary");
        btnModoOscuro.setOnAction(e -> {
            mainController.toggleModoOscuro();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, buscador, btnBuscar, spacer, btnModoOscuro);
        topBar.getStyleClass().add("topbar");
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        buscador.getStyleClass().add("search-field");
        btnBuscar.getStyleClass().add("modern-button-primary");

        setTop(topBar);

        // === SIDEBAR ===
        VBox sidebar = new VBox(8);
        sidebar.setPrefWidth(220);
        sidebar.getStyleClass().add("sidebar");

        Label tituloSidebar = new Label("Meli-Drive");
        tituloSidebar.getStyleClass().add("sidebar-title");

        Separator separador = new Separator();

        btnExplorador = new Button("Mi Unidad");
        btnBuscarEtiqueta = new Button("Buscar por Etiqueta");
        btnFlashcards = new Button("Modo Estudio");

        actualizarEstiloBotonesSidebar(btnExplorador);

        btnExplorador.setOnAction(e -> {
            mainController.getSidebarController().irAMiUnidad();
            actualizarEstiloBotonesSidebar(btnExplorador);
        });

        btnBuscarEtiqueta.setOnAction(e -> {
            mainController.getSidebarController().irABuscarPorEtiqueta();
            actualizarEstiloBotonesSidebar(btnBuscarEtiqueta);
        });

        btnFlashcards.setOnAction(e -> {
            mainController.getSidebarController().irAModoEstudio();
            actualizarEstiloBotonesSidebar(btnFlashcards);
        });

        sidebar.getChildren().addAll(tituloSidebar, separador, btnExplorador, btnBuscarEtiqueta, btnFlashcards);
        setLeft(sidebar);

        // === ÁREA CENTRAL ===
        areaCentral = new StackPane();
        areaCentral.getStyleClass().add("content-area");
        setCenter(areaCentral);
    }

    private void actualizarEstiloBotonesSidebar(Button activo) {
        Button[] botones = {btnExplorador, btnBuscarEtiqueta, btnFlashcards};
        for (Button b : botones) {
            b.getStyleClass().removeAll("sidebar-button", "sidebar-button-active");
            if (b == activo) {
                b.getStyleClass().add("sidebar-button-active");
            } else {
                b.getStyleClass().add("sidebar-button");
            }
        }
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
     * Reemplaza el contenido central con la vista de Búsqueda por Etiqueta.
     */
    public void mostrarBusquedaPorEtiqueta(MainController controller) {
        BusquedaEtiquetaView vista = new BusquedaEtiquetaView(controller);
        areaCentral.getChildren().setAll(vista);
    }

    /**
     * Muestra resultados de búsqueda en el área central.
     */
    private void mostrarResultadosBusqueda(List<DriveFile> resultados, String termino) {
        VBox contenedor = new VBox(10);
        contenedor.setPadding(new Insets(15));

        Label titulo = new Label("Resultados para: \"" + termino + "\" (" + resultados.size() + ")");
        titulo.getStyleClass().add("text-h2");
        contenedor.getChildren().add(titulo);

        if (resultados.isEmpty()) {
            Label sinResultados = new Label("No se encontraron archivos.");
            sinResultados.getStyleClass().add("text-body");
            contenedor.getChildren().add(sinResultados);
        } else {
            for (DriveFile archivo : resultados) {
                Button item = new Button(archivo.getNombre()
                        + "  (" + archivo.getTipoMime() + ")");
                item.getStyleClass().add("modern-list-cell");
                item.setPrefWidth(500);
                item.setAlignment(Pos.CENTER_LEFT);
                item.setOnAction(e -> mainController.mostrarVisorDocumento(archivo));
                contenedor.getChildren().add(item);
            }
        }

        Button btnVolver = new Button("Volver al Explorador");
        btnVolver.getStyleClass().add("modern-button-primary");
        btnVolver.setOnAction(e -> mainController.mostrarExplorador());
        contenedor.getChildren().add(btnVolver);

        areaCentral.getChildren().setAll(contenedor);
    }
}
