package com.melidrive.vista;

import com.melidrive.controlador.ExploradorController;
import com.melidrive.controlador.MainController;
import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.DriveFolder;
import com.melidrive.util.IconosUtil;
import com.melidrive.util.Notificador;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.List;

/**
 * Vista del Explorador de archivos.
 * Orquesta el layout (toolbar, breadcrumb y contenedor) y delega la creación
 * de tarjetas/filas en {@link FabricaItemsExplorador} y los diálogos de borrado
 * en {@link DialogosConfirmacion} (Responsabilidad Única).
 */
public class ExploradorView extends VBox {

    private ExploradorController controller;
    private MainController mainController;
    private FlowPane contenedorArchivos;
    private HBox breadcrumbBar;
    private FabricaItemsExplorador fabrica;
    private boolean vistaLista = false;

    public ExploradorView(ExploradorController controller, MainController mainController) {
        this.controller = controller;
        this.mainController = mainController;
        this.setSpacing(0);

        // === BARRA DE HERRAMIENTAS ===
        HBox toolbar = new HBox(10);
        toolbar.setPadding(new Insets(10));
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.getStyleClass().add("toolbar");

        Button btnVolver = new Button("Atrás");
        btnVolver.setGraphic(IconosUtil.crearIcono("icons/ui/tb-atras.png", 16));
        btnVolver.setGraphicTextGap(7);
        btnVolver.getStyleClass().add("modern-button-secondary");
        btnVolver.setOnAction(e -> {
            controller.volverAtras();
            refrescar();
        });

        Button btnNuevaCarpeta = new Button("Nueva Carpeta");
        btnNuevaCarpeta.setGraphic(IconosUtil.crearIcono("icons/ui/tb-nueva.png", 16));
        btnNuevaCarpeta.setGraphicTextGap(7);
        btnNuevaCarpeta.getStyleClass().add("modern-button-primary");
        btnNuevaCarpeta.setOnAction(e -> {
            DialogoNuevaCarpeta dialogo = new DialogoNuevaCarpeta();
            String nombre = dialogo.mostrar();
            if (nombre != null) {
                controller.crearNuevaCarpeta(nombre);
                refrescar();
                Notificador.mostrar(this, "Carpeta \"" + nombre + "\" creada", Notificador.Tipo.SUCCESS);
            }
        });

        Button btnImportar = new Button("Importar Archivo");
        btnImportar.setGraphic(IconosUtil.crearIcono("icons/ui/tb-importar.png", 16));
        btnImportar.setGraphicTextGap(7);
        btnImportar.getStyleClass().add("btn-success");
        btnImportar.setOnAction(e -> {
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Seleccionar Archivo Físico");
            java.io.File file = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (file != null) {
                controller.importarArchivoReal(file);
                refrescar();
                Notificador.mostrar(this, "Archivo \"" + file.getName() + "\" importado", Notificador.Tipo.SUCCESS);
            }
        });

        Button btnVista = new Button("Lista");
        btnVista.setGraphic(IconosUtil.crearIcono("icons/ui/tb-lista.png", 16));
        btnVista.setGraphicTextGap(7);
        btnVista.getStyleClass().add("modern-button-secondary");
        btnVista.setOnAction(e -> {
            vistaLista = !vistaLista;
            btnVista.setText(vistaLista ? "Cuadrícula" : "Lista");
            btnVista.setGraphic(IconosUtil.crearIcono(
                    vistaLista ? "icons/ui/tb-cuadricula.png" : "icons/ui/tb-lista.png", 16));
            refrescar();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        toolbar.getChildren().addAll(btnVolver, btnNuevaCarpeta, btnImportar, btnVista, spacer);
        getChildren().add(toolbar);

        // Breadcrumb de navegación: muestra la ruta y permite saltar a un ancestro.
        breadcrumbBar = new HBox(2);
        breadcrumbBar.setAlignment(Pos.CENTER_LEFT);
        breadcrumbBar.setPadding(new Insets(0, 15, 8, 15));
        getChildren().add(breadcrumbBar);

        // === CONTENEDOR DE ARCHIVOS (GRID/FLOW) ===
        contenedorArchivos = new FlowPane();
        contenedorArchivos.setPadding(new Insets(15));
        contenedorArchivos.setHgap(15);
        contenedorArchivos.setVgap(15);

        // Fábrica que construye las tarjetas/filas y gestiona su borrado.
        fabrica = new FabricaItemsExplorador(controller, mainController, contenedorArchivos, this::refrescar);

        ScrollPane scroll = new ScrollPane(contenedorArchivos);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-pane");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        getChildren().add(scroll);

        refrescar();
    }

    /**
     * Reconstruye la vista con el contenido de la carpeta actual.
     */
    public void refrescar() {
        contenedorArchivos.getChildren().clear();
        DriveFolder carpetaActual = controller.getCarpetaActual();
        construirBreadcrumb();

        // Subcarpetas y archivos (en cuadrícula o en lista según el modo activo)
        for (DriveFolder subcarpeta : carpetaActual.getSubcarpetas()) {
            contenedorArchivos.getChildren().add(
                    vistaLista ? fabrica.crearFilaCarpeta(subcarpeta) : fabrica.crearItemCarpeta(subcarpeta));
        }
        for (DriveFile archivo : carpetaActual.getArchivos()) {
            contenedorArchivos.getChildren().add(
                    vistaLista ? fabrica.crearFilaArchivo(archivo) : fabrica.crearItemArchivo(archivo));
        }

        // Estado vacío: ilustración + invitación a actuar
        if (carpetaActual.getSubcarpetas().isEmpty() && carpetaActual.getArchivos().isEmpty()) {
            VBox estadoVacio = new VBox();
            estadoVacio.getStyleClass().add("empty-state");

            ImageView icono = IconosUtil.crearIcono("icons/ui/empty.png", 56);

            Label titulo = new Label("Esta carpeta está vacía");
            titulo.getStyleClass().add("empty-state-title");

            Label sugerencia = new Label("Usa \"Importar Archivo\" o \"Nueva Carpeta\" para empezar.");
            sugerencia.getStyleClass().add("empty-state-text");

            estadoVacio.getChildren().addAll(icono, titulo, sugerencia);
            contenedorArchivos.getChildren().add(estadoVacio);
        }
    }

    /**
     * Reconstruye el breadcrumb con la ruta actual. Cada ancestro es un enlace
     * que navega a esa carpeta; la carpeta actual se muestra resaltada y sin acción.
     */
    private void construirBreadcrumb() {
        breadcrumbBar.getChildren().clear();
        List<DriveFolder> ruta = controller.getRutaActual();
        for (int i = 0; i < ruta.size(); i++) {
            DriveFolder carpeta = ruta.get(i);
            boolean esActual = (i == ruta.size() - 1);

            if (esActual) {
                Label actual = new Label(carpeta.getNombre());
                actual.getStyleClass().add("crumb-current");
                breadcrumbBar.getChildren().add(actual);
            } else {
                Button enlace = new Button(carpeta.getNombre());
                enlace.getStyleClass().add("crumb-link");
                enlace.setOnAction(e -> {
                    controller.irARuta(carpeta);
                    refrescar();
                });
                Label sep = new Label("›");
                sep.getStyleClass().add("crumb-sep");
                breadcrumbBar.getChildren().addAll(enlace, sep);
            }
        }
    }
}
