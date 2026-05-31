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
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

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
        toolbar.getStyleClass().add("toolbar");

        Button btnVolver = new Button("Atrás");
        btnVolver.getStyleClass().add("modern-button-secondary");
        btnVolver.setOnAction(e -> {
            controller.volverAtras();
            refrescar();
        });

        Button btnNuevaCarpeta = new Button("Nueva Carpeta");
        btnNuevaCarpeta.getStyleClass().add("modern-button-primary");
        btnNuevaCarpeta.setOnAction(e -> {
            DialogoNuevaCarpeta dialogo = new DialogoNuevaCarpeta();
            String nombre = dialogo.mostrar();
            if (nombre != null) {
                controller.crearNuevaCarpeta(nombre);
                refrescar();
            }
        });

        Button btnImportar = new Button("Importar Archivo");
        btnImportar.setStyle("-fx-background-color: #00b894; -fx-text-fill: white; -fx-cursor: hand;");
        btnImportar.setOnAction(e -> {
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Seleccionar Archivo Físico");
            java.io.File file = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (file != null) {
                controller.importarArchivoReal(file);
                refrescar();
            }
        });

        labelUbicacion = new Label();
        labelUbicacion.getStyleClass().add("text-h3");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        toolbar.getChildren().addAll(btnVolver, btnNuevaCarpeta, btnImportar, spacer, labelUbicacion);
        getChildren().add(toolbar);

        // === CONTENEDOR DE ARCHIVOS (GRID/FLOW) ===
        contenedorArchivos = new FlowPane();
        contenedorArchivos.setPadding(new Insets(15));
        contenedorArchivos.setHgap(15);
        contenedorArchivos.setVgap(15);

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
        labelUbicacion.setText(carpetaActual.getNombre());

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
            Label vacio = new Label("Esta carpeta está vacía");
            vacio.getStyleClass().add("text-body");
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
        item.setPrefSize(140, 120);
        item.getStyleClass().add("modern-card");

        ImageView icono = crearIcono(IconosUtil.TipoIcono.CARPETA);
        Label nombre = new Label(carpeta.getNombre());
        nombre.getStyleClass().add("text-body");
        nombre.setWrapText(true);
        nombre.setMaxWidth(120);
        nombre.setAlignment(Pos.CENTER);

        int totalElementos = carpeta.getSubcarpetas().size() + carpeta.getArchivos().size();
        Label lblElementos = new Label(totalElementos + " elem.");
        lblElementos.getStyleClass().add("text-caption");

        item.getChildren().addAll(icono, nombre, lblElementos);

        // Doble clic para entrar
        item.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                controller.entrarACarpeta(carpeta);
                refrescar();
            }
        });

        // Hover effect animado
        item.setOnMouseEntered(e -> animarEscala(item, 1.05));
        item.setOnMouseExited(e -> animarEscala(item, 1.0));

        return item;
    }

    /**
     * Crea un elemento visual para un archivo.
     */
    private VBox crearItemArchivo(DriveFile archivo) {
        VBox item = new VBox(5);
        item.setAlignment(Pos.CENTER);
        item.setPadding(new Insets(10));
        item.setPrefSize(140, 120);
        item.getStyleClass().add("modern-card");

        IconosUtil.TipoIcono tipo = IconosUtil.getIconoPorMime(archivo.getTipoMime());
        ImageView icono = crearIcono(tipo);

        Label nombre = new Label(archivo.getNombre());
        nombre.getStyleClass().add("text-body");
        nombre.setWrapText(true);
        nombre.setMaxWidth(120);
        nombre.setAlignment(Pos.CENTER);

        item.getChildren().addAll(icono, nombre);

        // Tooltip con info detallada
        String infoTooltip = "Nombre: " + archivo.getNombre() + "\n"
                + "Tipo: " + archivo.getTipoMime() + "\n"
                + "Tamaño: " + (archivo.getSizeEnBytes() / 1024) + " KB\n"
                + "Etiquetas: " + archivo.getEtiquetas().size();
        Tooltip tooltip = new Tooltip(infoTooltip);
        Tooltip.install(item, tooltip);

        // Doble clic para abrir en visor
        item.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                mainController.mostrarVisorDocumento(archivo);
            }
        });

        // Hover effect animado
        item.setOnMouseEntered(e -> animarEscala(item, 1.05));
        item.setOnMouseExited(e -> animarEscala(item, 1.0));

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

    private void animarEscala(VBox nodo, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), nodo);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }
}
