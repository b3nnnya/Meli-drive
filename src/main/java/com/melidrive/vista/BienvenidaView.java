package com.melidrive.vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.InputStream;

/**
 * Pantalla de bienvenida (launcher) de SIGEA, inspirada en el inicio de Obsidian.
 * Muestra el logo, el nombre y la versión, y tres accesos: "Inicio rápido",
 * "Crear una bóveda nueva" y "Abrir una carpeta como bóveda".
 *
 * Es independiente del tema: usa una paleta oscura fija con el azul brillante
 * característico de SIGEA, de modo que se vea igual con o sin modo oscuro.
 */
public class BienvenidaView extends StackPane {

    /** Acción a ejecutar para entrar a la aplicación principal. */
    private final Runnable onEntrar;

    public BienvenidaView(Runnable onEntrar) {
        this.onEntrar = onEntrar;
        this.getStyleClass().add("bienvenida-root");
        // Fondo fijo (independiente del tema claro/oscuro de la app).
        this.setStyle("-fx-background-color: #0d0d12;");

        VBox centro = new VBox(18);
        centro.setAlignment(Pos.CENTER);
        centro.setPadding(new Insets(40));

        // === Logo ===
        ImageView logo = new ImageView();
        logo.setFitWidth(104);
        logo.setFitHeight(104);
        logo.setPreserveRatio(true);
        logo.setSmooth(true);
        logo.setCache(true);
        InputStream is = getClass().getClassLoader().getResourceAsStream("images/logo.png");
        if (is != null) {
            logo.setImage(new Image(is, 256, 256, true, true));
        }

        // === Título y versión ===
        Label titulo = new Label("SIGEA");
        titulo.getStyleClass().add("bienvenida-titulo");

        Label version = new Label("Versión 1.0");
        version.getStyleClass().add("bienvenida-version");

        // === Botón Inicio rápido (acción principal) ===
        Button btnInicioRapido = new Button("Inicio rápido");
        btnInicioRapido.getStyleClass().add("btn-inicio-rapido");
        btnInicioRapido.setMaxWidth(320);
        btnInicioRapido.setPrefWidth(320);
        btnInicioRapido.setOnAction(e -> entrar());

        // === Tarjeta con las opciones Crear / Abrir ===
        VBox card = new VBox(0);
        card.getStyleClass().add("bienvenida-card");
        card.setMaxWidth(560);

        card.getChildren().addAll(
                crearFilaOpcion(
                        "Crear una bóveda nueva",
                        "Crear una nueva bóveda de SIGEA en un directorio.",
                        "Crear", "btn-azul", e -> entrar()),
                crearSeparador(),
                crearFilaOpcion(
                        "Abrir una carpeta como bóveda",
                        "Seleccionar un directorio existente de archivos.",
                        "Abrir", "btn-gris-oscuro", e -> abrirCarpeta())
        );

        centro.getChildren().addAll(logo, titulo, version, btnInicioRapido, card);
        getChildren().add(centro);
    }

    /**
     * Construye una fila de opción: título + descripción a la izquierda y un botón a la derecha.
     */
    private HBox crearFilaOpcion(String tituloTexto, String descTexto,
                                 String textoBoton, String claseBoton,
                                 javafx.event.EventHandler<javafx.event.ActionEvent> accion) {
        VBox textos = new VBox(3);
        textos.setAlignment(Pos.CENTER_LEFT);

        Label tit = new Label(tituloTexto);
        tit.getStyleClass().add("bienvenida-opcion-titulo");
        Label desc = new Label(descTexto);
        desc.getStyleClass().add("bienvenida-opcion-desc");
        desc.setWrapText(true);
        textos.getChildren().addAll(tit, desc);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button boton = new Button(textoBoton);
        boton.getStyleClass().add(claseBoton);
        boton.setMinWidth(110);
        boton.setOnAction(accion);

        HBox fila = new HBox(16, textos, spacer, boton);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setPadding(new Insets(18, 6, 18, 6));
        return fila;
    }

    /** Línea divisoria sutil entre las opciones de la tarjeta. */
    private Region crearSeparador() {
        Region sep = new Region();
        sep.getStyleClass().add("bienvenida-sep");
        sep.setMinHeight(1);
        sep.setMaxHeight(1);
        return sep;
    }

    /** Abre un selector de carpeta; si el usuario elige una, entra a la aplicación. */
    private void abrirCarpeta() {
        Window ventana = getScene() != null ? getScene().getWindow() : null;
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Abrir una carpeta como bóveda");
        File carpeta = chooser.showDialog(ventana);
        if (carpeta != null) {
            entrar();
        }
    }

    /** Ejecuta la transición a la aplicación principal. */
    private void entrar() {
        if (onEntrar != null) {
            onEntrar.run();
        }
    }
}
