package com.melidrive.vista;

import com.melidrive.controlador.*;
import com.melidrive.vista.componentes.SidebarButton;
import com.melidrive.vista.componentes.TopBar;
import javafx.scene.layout.*;

public class MainView extends BorderPane {

    private final MainController mainController;
    private final StackPane contentArea;

    private SidebarButton btnExplorador;
    private SidebarButton btnFlashcards;

    public MainView(MainController mainController) {
        this.mainController = mainController;

        getStylesheets().add(
                getClass().getResource("/styles/main.css").toExternalForm()
        );

        // TOPBAR
        TopBar topBar = new TopBar(mainController);
        setTop(topBar);

        // SIDEBAR
        VBox sidebar = construirSidebar();
        setLeft(sidebar);

        // CONTENT
        contentArea = new StackPane();
        contentArea.getStyleClass().add("content-area");
        setCenter(contentArea);
    }

    private VBox construirSidebar() {

        VBox sidebar = new VBox(10);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(230);

        javafx.scene.control.Label titulo = new javafx.scene.control.Label("Meli Drive");
        titulo.getStyleClass().add("sidebar-title");

        btnExplorador = new SidebarButton("📁 Mi Unidad");
        btnFlashcards = new SidebarButton("📚 Flashcards");

        btnExplorador.activar();

        btnExplorador.setOnAction(e -> {
            activarBoton(btnExplorador);
            mostrarExplorador(mainController.getExploradorController());
        });

        btnFlashcards.setOnAction(e -> {
            activarBoton(btnFlashcards);
            mostrarFlashcards(mainController.getFlashcardController());
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sidebar.getChildren().addAll(
                titulo,
                btnExplorador,
                btnFlashcards,
                spacer
        );

        return sidebar;
    }

    private void activarBoton(SidebarButton activo) {
        btnExplorador.desactivar();
        btnFlashcards.desactivar();

        activo.activar();
    }

    public void mostrarExplorador(ExploradorController controller) {
        contentArea.getChildren().setAll(
                new ExploradorView(controller, mainController)
        );
    }

    public void mostrarFlashcards(FlashcardController controller) {
        contentArea.getChildren().setAll(
                new FlashcardView(controller)
        );
    }

    public void mostrarVisorDocumento(VisorDocumentoController controller) {
        contentArea.getChildren().setAll(
                new VisorDocumentoView(controller, mainController)
        );
    }
}