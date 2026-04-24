package com.melidrive.vista;

import com.melidrive.controlador.ExploradorController;
import com.melidrive.controlador.MainController;
import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.DriveFolder;
import com.melidrive.util.IconosUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * Vista del Explorador de archivos.
 * Muestra carpetas y archivos de la ubicación actual con íconos,
 * permite navegar entre carpetas y crear nuevas.
 */
public class ExploradorView extends VBox {

    private ExploradorController controller;
    private MainController mainController;
    private FlowPane contenedorArchivos;
    private Label labelUbicacion;

    public ExploradorView(ExploradorController controller, MainController mainController) {
        this.controller = controller;
        this.mainController = mainController;
        this.setSpacing(0);

        // === BARRA DE HERRAMIENTAS ===
        HBox toolbar = new HBox(10);
        toolbar.setPadding(new Insets(10));
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setStyle("-fx-background-color: #dfe6e9;");

        Button btnVolver = new Button("← Atrás");
        btnVolver.setStyle("-fx-background-color: #636e72; -fx-text-fill: white; -fx-cursor: hand;");
        btnVolver.setOnAction(e -> {
            controller.volverAtras();
            refrescar();
        });

        Button btnNuevaCarpeta = new Button("📁 Nueva Carpeta");
        btnNuevaCarpeta.setStyle("-fx-background-color: #0984e3; -fx-text-fill: white; -fx-cursor: hand;");
        btnNuevaCarpeta.setOnAction(e -> {
            DialogoNuevaCarpeta dialogo = new DialogoNuevaCarpeta();
            String nombre = dialogo.mostrar();
            if (nombre != null) {
                controller.crearNuevaCarpeta(nombre);
                refrescar();
            }
        });

        labelUbicacion = new Label();
        labelUbicacion.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2d3436;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        toolbar.getChildren().addAll(btnVolver, btnNuevaCarpeta, spacer, labelUbicacion);
        getChildren().add(toolbar);

        // === CONTENEDOR DE ARCHIVOS (GRID/FLOW) ===
        contenedorArchivos = new FlowPane();
        contenedorArchivos.setPadding(new Insets(15));
        contenedorArchivos.setHgap(15);
        contenedorArchivos.setVgap(15);

        ScrollPane scroll = new ScrollPane(contenedorArchivos);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #ecf0f1; -fx-background-color: #ecf0f1;");
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
        labelUbicacion.setText("📍 " + carpetaActual.getNombre());

        // Mostrar subcarpetas
        for (DriveFolder subcarpeta : carpetaActual.getSubcarpetas()) {
            VBox item = crearItemCarpeta(subcarpeta);
            contenedorArchivos.getChildren().add(item);
        }

        // Mostrar archivos
        for (DriveFile archivo : carpetaActual.getArchivos()) {
            VBox item = crearItemArchivo(archivo);
            contenedorArchivos.getChildren().add(item);
        }

        // Mensaje si está vacía
        if (carpetaActual.getSubcarpetas().isEmpty() && carpetaActual.getArchivos().isEmpty()) {
            Label vacio = new Label("📭 Esta carpeta está vacía");
            vacio.setStyle("-fx-text-fill: #b2bec3; -fx-font-size: 16px;");
            contenedorArchivos.getChildren().add(vacio);
        }
    }

    /**
     * Crea un elemento visual para una carpeta.
     */
    private VBox crearItemCarpeta(DriveFolder carpeta) {
        VBox item = new VBox(5);
        item.setAlignment(Pos.CENTER);
        item.setPadding(new Insets(10));
        item.setPrefSize(120, 100);
        item.setStyle("-fx-background-color: white; -fx-background-radius: 8; "
                + "-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);");

        ImageView icono = crearIcono(IconosUtil.TipoIcono.CARPETA);
        Label nombre = new Label(carpeta.getNombre());
        nombre.setStyle("-fx-font-size: 11px; -fx-text-fill: #2d3436;");
        nombre.setWrapText(true);
        nombre.setMaxWidth(100);
        nombre.setAlignment(Pos.CENTER);

        item.getChildren().addAll(icono, nombre);

        // Doble clic para entrar
        item.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                controller.entrarACarpeta(carpeta);
                refrescar();
            }
        });

        // Hover effect
        item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: #dfe6e9; -fx-background-radius: 8; "
                + "-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 6, 0, 0, 3);"));
        item.setOnMouseExited(e -> item.setStyle("-fx-background-color: white; -fx-background-radius: 8; "
                + "-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);"));

        return item;
    }

    /**
     * Crea un elemento visual para un archivo.
     */
    private VBox crearItemArchivo(DriveFile archivo) {
        VBox item = new VBox(5);
        item.setAlignment(Pos.CENTER);
        item.setPadding(new Insets(10));
        item.setPrefSize(120, 100);
        item.setStyle("-fx-background-color: white; -fx-background-radius: 8; "
                + "-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);");

        IconosUtil.TipoIcono tipo = IconosUtil.getIconoPorMime(archivo.getTipoMime());
        ImageView icono = crearIcono(tipo);

        Label nombre = new Label(archivo.getNombre());
        nombre.setStyle("-fx-font-size: 11px; -fx-text-fill: #2d3436;");
        nombre.setWrapText(true);
        nombre.setMaxWidth(100);
        nombre.setAlignment(Pos.CENTER);

        item.getChildren().addAll(icono, nombre);

        // Doble clic para abrir en visor
        item.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                mainController.mostrarVisorDocumento(archivo);
            }
        });

        // Hover effect
        item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: #dfe6e9; -fx-background-radius: 8; "
                + "-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 6, 0, 0, 3);"));
        item.setOnMouseExited(e -> item.setStyle("-fx-background-color: white; -fx-background-radius: 8; "
                + "-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);"));

        return item;
    }

    /**
     * Intenta cargar el ícono desde resources. Si no existe, usa un emoji como fallback.
     */
    private ImageView crearIcono(IconosUtil.TipoIcono tipo) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);

        try {
            Image img = new Image(getClass().getClassLoader()
                    .getResourceAsStream(tipo.getRutaIcono()));
            imageView.setImage(img);
        } catch (Exception e) {
            // Fallback: si no se encuentra el ícono, no se muestra imagen
            System.out.println("Ícono no encontrado: " + tipo.getRutaIcono());
        }

        return imageView;
    }
}
