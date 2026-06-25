package com.melidrive.vista;

import com.melidrive.controlador.ExploradorController;
import com.melidrive.controlador.MainController;
import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.DriveFolder;
import com.melidrive.util.IconosUtil;
import com.melidrive.util.Notificador;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import java.util.List;

/**
 * Vista del Explorador de archivos.
 * Muestra carpetas y archivos de la ubicación actual con íconos,
 * permite navegar entre carpetas y crear nuevas.
 */
public class ExploradorView extends VBox {

    private ExploradorController controller;
    private MainController mainController;
    private FlowPane contenedorArchivos;
    private HBox breadcrumbBar;
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

        Button btnVolver = new Button("⬅  Atrás");
        btnVolver.getStyleClass().add("modern-button-secondary");
        btnVolver.setOnAction(e -> {
            controller.volverAtras();
            refrescar();
        });

        Button btnNuevaCarpeta = new Button("➕  Nueva Carpeta");
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

        Button btnImportar = new Button("📥  Importar Archivo");
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

        Button btnVista = new Button("☰  Lista");
        btnVista.getStyleClass().add("modern-button-secondary");
        btnVista.setOnAction(e -> {
            vistaLista = !vistaLista;
            btnVista.setText(vistaLista ? "▦  Cuadrícula" : "☰  Lista");
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

        // Mostrar subcarpetas (en cuadrícula o en lista según el modo activo)
        for (DriveFolder subcarpeta : carpetaActual.getSubcarpetas()) {
            contenedorArchivos.getChildren().add(
                    vistaLista ? crearFilaCarpeta(subcarpeta) : crearItemCarpeta(subcarpeta));
        }

        // Mostrar archivos
        for (DriveFile archivo : carpetaActual.getArchivos()) {
            contenedorArchivos.getChildren().add(
                    vistaLista ? crearFilaArchivo(archivo) : crearItemArchivo(archivo));
        }

        // Estado vacío: ilustración + invitación a actuar
        if (carpetaActual.getSubcarpetas().isEmpty() && carpetaActual.getArchivos().isEmpty()) {
            VBox estadoVacio = new VBox();
            estadoVacio.getStyleClass().add("empty-state");

            Label icono = new Label("📂");
            icono.getStyleClass().add("empty-state-icon");

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

        // Tooltip: ayuda + tamaño total recursivo (usa el polimorfismo de ElementoDrive)
        Tooltip.install(item, new Tooltip(
                "Tamaño total: " + formatearTam(carpeta.getTamanioTotal()) + "\n"
                + "Doble clic para abrir · Clic derecho para eliminar"));

        // Menú contextual: eliminar carpeta (con doble verificación)
        ContextMenu menu = new ContextMenu();
        MenuItem itemEliminar = new MenuItem("Eliminar carpeta");
        itemEliminar.setOnAction(ev -> confirmarYEliminarCarpeta(carpeta));
        menu.getItems().add(itemEliminar);
        item.setOnContextMenuRequested(ev -> menu.show(item, ev.getScreenX(), ev.getScreenY()));

        // Hover effect animado
        item.setOnMouseEntered(e -> animarEscala(item, 1.05));
        item.setOnMouseExited(e -> animarEscala(item, 1.0));

        return item;
    }

    /**
     * Pide doble confirmación y elimina la carpeta indicada.
     */
    private void confirmarYEliminarCarpeta(DriveFolder carpeta) {
        int total = carpeta.getSubcarpetas().size() + carpeta.getArchivos().size();

        // Primera verificación
        Alert confirm1 = new Alert(Alert.AlertType.CONFIRMATION);
        confirm1.setTitle("Eliminar carpeta");
        confirm1.setHeaderText("¿Eliminar la carpeta \"" + carpeta.getNombre() + "\"?");
        confirm1.setContentText(total > 0
                ? "Contiene " + total + " elemento(s) que también se eliminarán."
                : "Esta carpeta está vacía.");
        confirm1.getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);
        java.util.Optional<ButtonType> r1 = confirm1.showAndWait();
        if (r1.isEmpty() || r1.get() != ButtonType.OK) {
            return;
        }

        // Segunda verificación
        Alert confirm2 = new Alert(Alert.AlertType.WARNING);
        confirm2.setTitle("Confirmación final");
        confirm2.setHeaderText("Esta acción no se puede deshacer.");
        confirm2.setContentText("¿Seguro que deseas eliminar \"" + carpeta.getNombre() + "\" definitivamente?");
        confirm2.getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);
        java.util.Optional<ButtonType> r2 = confirm2.showAndWait();
        if (r2.isPresent() && r2.get() == ButtonType.OK) {
            controller.eliminarCarpeta(carpeta);
            refrescar();
            Notificador.mostrar(this, "Carpeta \"" + carpeta.getNombre() + "\" eliminada", Notificador.Tipo.DANGER);
        }
    }

    /**
     * Pide confirmación y elimina el archivo indicado de la carpeta actual.
     * Al ser un solo elemento, basta con una verificación (a diferencia de las carpetas).
     */
    private void confirmarYEliminarArchivo(DriveFile archivo) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar archivo");
        confirm.setHeaderText("¿Eliminar el archivo \"" + archivo.getNombre() + "\"?");
        confirm.setContentText("Se quitará de tu unidad. Esta acción no se puede deshacer.");
        confirm.getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);

        java.util.Optional<ButtonType> r = confirm.showAndWait();
        if (r.isPresent() && r.get() == ButtonType.OK) {
            controller.eliminarArchivo(archivo);
            refrescar();
            Notificador.mostrar(this, "Archivo \"" + archivo.getNombre() + "\" eliminado", Notificador.Tipo.DANGER);
        }
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
                + "Etiquetas: " + archivo.getEtiquetas().size() + "\n"
                + "Doble clic para abrir · Clic derecho para eliminar";
        Tooltip tooltip = new Tooltip(infoTooltip);
        Tooltip.install(item, tooltip);

        // Doble clic para abrir en visor
        item.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                mainController.mostrarVisorDocumento(archivo);
            }
        });

        // Menú contextual: eliminar archivo (con confirmación)
        ContextMenu menu = new ContextMenu();
        MenuItem itemEliminar = new MenuItem("Eliminar archivo");
        itemEliminar.setOnAction(ev -> confirmarYEliminarArchivo(archivo));
        menu.getItems().add(itemEliminar);
        item.setOnContextMenuRequested(ev -> menu.show(item, ev.getScreenX(), ev.getScreenY()));

        // Hover effect animado
        item.setOnMouseEntered(e -> animarEscala(item, 1.05));
        item.setOnMouseExited(e -> animarEscala(item, 1.0));

        return item;
    }

    /**
     * Crea una fila horizontal compacta para una carpeta (vista de lista).
     */
    private HBox crearFilaCarpeta(DriveFolder carpeta) {
        HBox fila = new HBox(12);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setPadding(new Insets(10, 14, 10, 14));
        fila.getStyleClass().add("modern-card");
        fila.prefWidthProperty().bind(contenedorArchivos.widthProperty().subtract(45));

        ImageView icono = crearIcono(IconosUtil.TipoIcono.CARPETA);
        icono.setFitWidth(28);
        icono.setFitHeight(28);

        Label nombre = new Label(carpeta.getNombre());
        nombre.getStyleClass().add("text-body");
        nombre.setStyle("-fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        int totalElementos = carpeta.getSubcarpetas().size() + carpeta.getArchivos().size();
        Label info = new Label(totalElementos + " elem.");
        info.getStyleClass().add("text-caption");

        fila.getChildren().addAll(icono, nombre, spacer, info);

        fila.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                controller.entrarACarpeta(carpeta);
                refrescar();
            }
        });
        Tooltip.install(fila, new Tooltip(
                "Tamaño total: " + formatearTam(carpeta.getTamanioTotal()) + "\n"
                + "Doble clic para abrir · Clic derecho para eliminar"));

        ContextMenu menu = new ContextMenu();
        MenuItem itemEliminar = new MenuItem("Eliminar carpeta");
        itemEliminar.setOnAction(ev -> confirmarYEliminarCarpeta(carpeta));
        menu.getItems().add(itemEliminar);
        fila.setOnContextMenuRequested(ev -> menu.show(fila, ev.getScreenX(), ev.getScreenY()));

        return fila;
    }

    /**
     * Crea una fila horizontal compacta para un archivo (vista de lista).
     */
    private HBox crearFilaArchivo(DriveFile archivo) {
        HBox fila = new HBox(12);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setPadding(new Insets(10, 14, 10, 14));
        fila.getStyleClass().add("modern-card");
        fila.prefWidthProperty().bind(contenedorArchivos.widthProperty().subtract(45));

        IconosUtil.TipoIcono tipo = IconosUtil.getIconoPorMime(archivo.getTipoMime());
        ImageView icono = crearIcono(tipo);
        icono.setFitWidth(28);
        icono.setFitHeight(28);

        Label nombre = new Label(archivo.getNombre());
        nombre.getStyleClass().add("text-body");
        nombre.setStyle("-fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label info = new Label((archivo.getSizeEnBytes() / 1024) + " KB");
        info.getStyleClass().add("text-caption");

        fila.getChildren().addAll(icono, nombre, spacer, info);

        fila.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                mainController.mostrarVisorDocumento(archivo);
            }
        });
        Tooltip.install(fila, new Tooltip("Doble clic para abrir · Clic derecho para eliminar"));

        ContextMenu menu = new ContextMenu();
        MenuItem itemEliminar = new MenuItem("Eliminar archivo");
        itemEliminar.setOnAction(ev -> confirmarYEliminarArchivo(archivo));
        menu.getItems().add(itemEliminar);
        fila.setOnContextMenuRequested(ev -> menu.show(fila, ev.getScreenX(), ev.getScreenY()));

        return fila;
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

    /** Formatea un tamaño en bytes a una representación legible. */
    private String formatearTam(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024));
    }

    private void animarEscala(VBox nodo, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), nodo);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }
}
