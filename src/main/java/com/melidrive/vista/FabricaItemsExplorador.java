package com.melidrive.vista;

import com.melidrive.controlador.ExploradorController;
import com.melidrive.controlador.MainController;
import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.DriveFolder;
import com.melidrive.util.IconosUtil;
import com.melidrive.util.Notificador;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Fábrica de los elementos visuales del explorador: tarjetas (cuadrícula) y
 * filas (lista) para carpetas y archivos, junto con sus menús contextuales.
 *
 * Extrae de {@link ExploradorView} la construcción de items y el manejo de su
 * borrado, dejando a la vista solo la orquestación del layout (SRP). Delega la
 * confirmación en {@link DialogosConfirmacion}.
 */
public class FabricaItemsExplorador {

    private final ExploradorController controller;
    private final MainController mainController;
    private final Pane contenedor;       // ancla los toasts y fija el ancho de las filas
    private final Runnable alRefrescar;  // refresca la vista tras navegar o eliminar

    public FabricaItemsExplorador(ExploradorController controller, MainController mainController,
                                  Pane contenedor, Runnable alRefrescar) {
        this.controller = controller;
        this.mainController = mainController;
        this.contenedor = contenedor;
        this.alRefrescar = alRefrescar;
    }

    /** Tarjeta (cuadrícula) de una carpeta. */
    public VBox crearItemCarpeta(DriveFolder carpeta) {
        VBox item = tarjetaBase();

        ImageView icono = IconosUtil.crearIcono(IconosUtil.TipoIcono.CARPETA.getRutaIcono(), 40);
        Label nombre = nombreTarjeta(carpeta.getNombre());

        int totalElementos = carpeta.getSubcarpetas().size() + carpeta.getArchivos().size();
        Label lblElementos = new Label(totalElementos + " elem.");
        lblElementos.getStyleClass().add("text-caption");

        item.getChildren().addAll(icono, nombre, lblElementos);

        item.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                controller.entrarACarpeta(carpeta);
                alRefrescar.run();
            }
        });
        Tooltip.install(item, new Tooltip(
                "Tamaño total: " + formatearTam(carpeta.getTamanioTotal()) + "\n"
                + "Doble clic para abrir · Clic derecho para eliminar"));
        instalarMenuEliminarCarpeta(item, carpeta);
        animarHover(item);
        return item;
    }

    /** Tarjeta (cuadrícula) de un archivo. */
    public VBox crearItemArchivo(DriveFile archivo) {
        VBox item = tarjetaBase();

        IconosUtil.TipoIcono tipo = IconosUtil.getIconoPorMime(archivo.getTipoMime());
        ImageView icono = IconosUtil.crearIcono(tipo.getRutaIcono(), 40);
        Label nombre = nombreTarjeta(archivo.getNombre());

        item.getChildren().addAll(icono, nombre);

        Tooltip.install(item, new Tooltip(
                "Nombre: " + archivo.getNombre() + "\n"
                + "Tipo: " + archivo.getTipoMime() + "\n"
                + "Tamaño: " + (archivo.getSizeEnBytes() / 1024) + " KB\n"
                + "Etiquetas: " + archivo.getEtiquetas().size() + "\n"
                + "Doble clic para abrir · Clic derecho para eliminar"));

        item.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                mainController.mostrarVisorDocumento(archivo);
            }
        });
        instalarMenuEliminarArchivo(item, archivo);
        animarHover(item);
        return item;
    }

    /** Fila (lista) de una carpeta. */
    public HBox crearFilaCarpeta(DriveFolder carpeta) {
        HBox fila = filaBase();

        ImageView icono = IconosUtil.crearIcono(IconosUtil.TipoIcono.CARPETA.getRutaIcono(), 28);
        Label nombre = nombreFila(carpeta.getNombre());
        int totalElementos = carpeta.getSubcarpetas().size() + carpeta.getArchivos().size();
        Label info = new Label(totalElementos + " elem.");
        info.getStyleClass().add("text-caption");

        fila.getChildren().addAll(icono, nombre, spacer(), info);

        fila.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                controller.entrarACarpeta(carpeta);
                alRefrescar.run();
            }
        });
        Tooltip.install(fila, new Tooltip(
                "Tamaño total: " + formatearTam(carpeta.getTamanioTotal()) + "\n"
                + "Doble clic para abrir · Clic derecho para eliminar"));
        instalarMenuEliminarCarpeta(fila, carpeta);
        return fila;
    }

    /** Fila (lista) de un archivo. */
    public HBox crearFilaArchivo(DriveFile archivo) {
        HBox fila = filaBase();

        IconosUtil.TipoIcono tipo = IconosUtil.getIconoPorMime(archivo.getTipoMime());
        ImageView icono = IconosUtil.crearIcono(tipo.getRutaIcono(), 28);
        Label nombre = nombreFila(archivo.getNombre());
        Label info = new Label((archivo.getSizeEnBytes() / 1024) + " KB");
        info.getStyleClass().add("text-caption");

        fila.getChildren().addAll(icono, nombre, spacer(), info);

        fila.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                mainController.mostrarVisorDocumento(archivo);
            }
        });
        Tooltip.install(fila, new Tooltip("Doble clic para abrir · Clic derecho para eliminar"));
        instalarMenuEliminarArchivo(fila, archivo);
        return fila;
    }

    // ==================== helpers privados ====================

    private VBox tarjetaBase() {
        VBox item = new VBox(5);
        item.setAlignment(Pos.CENTER);
        item.setPadding(new Insets(10));
        item.setPrefSize(140, 120);
        item.getStyleClass().add("modern-card");
        return item;
    }

    private HBox filaBase() {
        HBox fila = new HBox(12);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setPadding(new Insets(10, 14, 10, 14));
        fila.getStyleClass().add("modern-card");
        fila.prefWidthProperty().bind(contenedor.widthProperty().subtract(45));
        return fila;
    }

    private Label nombreTarjeta(String texto) {
        Label nombre = new Label(texto);
        nombre.getStyleClass().add("text-body");
        nombre.setWrapText(true);
        nombre.setMaxWidth(120);
        nombre.setAlignment(Pos.CENTER);
        return nombre;
    }

    private Label nombreFila(String texto) {
        Label nombre = new Label(texto);
        nombre.getStyleClass().add("text-body");
        nombre.setStyle("-fx-font-weight: bold;");
        return nombre;
    }

    private Region spacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private void instalarMenuEliminarCarpeta(Region nodo, DriveFolder carpeta) {
        ContextMenu menu = new ContextMenu();
        MenuItem itemEliminar = new MenuItem("Eliminar carpeta");
        itemEliminar.setOnAction(ev -> {
            if (DialogosConfirmacion.eliminarCarpeta(carpeta)) {
                controller.eliminarCarpeta(carpeta);
                alRefrescar.run();
                Notificador.mostrar(contenedor, "Carpeta \"" + carpeta.getNombre() + "\" eliminada",
                        Notificador.Tipo.DANGER);
            }
        });
        menu.getItems().add(itemEliminar);
        nodo.setOnContextMenuRequested(ev -> menu.show(nodo, ev.getScreenX(), ev.getScreenY()));
    }

    private void instalarMenuEliminarArchivo(Region nodo, DriveFile archivo) {
        ContextMenu menu = new ContextMenu();
        MenuItem itemEliminar = new MenuItem("Eliminar archivo");
        itemEliminar.setOnAction(ev -> {
            if (DialogosConfirmacion.eliminarArchivo(archivo)) {
                controller.eliminarArchivo(archivo);
                alRefrescar.run();
                Notificador.mostrar(contenedor, "Archivo \"" + archivo.getNombre() + "\" eliminado",
                        Notificador.Tipo.DANGER);
            }
        });
        menu.getItems().add(itemEliminar);
        nodo.setOnContextMenuRequested(ev -> menu.show(nodo, ev.getScreenX(), ev.getScreenY()));
    }

    private void animarHover(VBox item) {
        item.setOnMouseEntered(e -> animarEscala(item, 1.05));
        item.setOnMouseExited(e -> animarEscala(item, 1.0));
    }

    private void animarEscala(VBox nodo, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), nodo);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }

    private String formatearTam(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024));
    }
}
