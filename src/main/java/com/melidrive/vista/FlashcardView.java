package com.melidrive.vista;

import com.melidrive.controlador.FlashcardController;
import com.melidrive.modelo.Flashcard;
import com.melidrive.util.Notificador;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Vista del Modo Estudio (Flashcards).
 * Muestra tarjetas de repaso espaciado con funcionalidad de voltear
 * y calificar (Fácil / Difícil).
 */
public class FlashcardView extends VBox {

    private FlashcardController controller;
    private boolean mostrandoPregunta = true;

    private Label labelContenido;
    private Label labelProgreso;
    private Label labelIndicador;
    private Label labelContador;
    private Label labelArchivoAsociado;
    private javafx.scene.image.ImageView imageViewAsociada;
    private Button btnFacil;
    private Button btnDificil;
    private StackPane tarjeta;
    private VBox contenedorBotones;
    private Button btnEliminarRepaso;
    private ProgressBar barraProgreso;

    public FlashcardView(FlashcardController controller) {
        this.controller = controller;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(30));
        this.getStyleClass().add("content-area");

        // Título
        Label titulo = new Label("Modo Estudio - Repaso Espaciado");
        titulo.getStyleClass().add("text-h1");

        // Progreso
        labelProgreso = new Label();
        labelProgreso.getStyleClass().add("text-body");

        // Indicador (Pregunta / Respuesta) y Contador
        labelIndicador = new Label("PREGUNTA");
        labelIndicador.getStyleClass().add("text-small");
        
        labelContador = new Label();
        labelContador.getStyleClass().add("text-caption");

        Region spacerIndicador = new Region();
        HBox.setHgrow(spacerIndicador, Priority.ALWAYS);
        HBox infoBar = new HBox(labelIndicador, spacerIndicador, labelContador);
        infoBar.setMaxWidth(500);

        // Barra de progreso de la sesión (porcentaje de tarjetas ya repasadas)
        barraProgreso = new ProgressBar(0);
        barraProgreso.getStyleClass().add("progress-estudio");
        barraProgreso.setPrefWidth(500);
        barraProgreso.setMaxWidth(500);

        // Tarjeta
        tarjeta = new StackPane();
        tarjeta.setPrefSize(500, 300);
        tarjeta.setMaxSize(500, 300);
        tarjeta.getStyleClass().add("flashcard");

        VBox tarjetaContenidoBox = new VBox(10);
        tarjetaContenidoBox.setAlignment(Pos.CENTER);
        
        labelContenido = new Label();
        labelContenido.getStyleClass().add("text-h2");
        labelContenido.setWrapText(true);
        labelContenido.setMaxWidth(450);
        labelContenido.setAlignment(Pos.CENTER);

        imageViewAsociada = new javafx.scene.image.ImageView();
        imageViewAsociada.setPreserveRatio(true);
        imageViewAsociada.setFitWidth(400);
        imageViewAsociada.setFitHeight(180);
        imageViewAsociada.setVisible(false);
        imageViewAsociada.setManaged(false);

        tarjetaContenidoBox.getChildren().addAll(labelContenido, imageViewAsociada);

        labelArchivoAsociado = new Label();
        labelArchivoAsociado.getStyleClass().add("text-small");
        StackPane.setAlignment(labelArchivoAsociado, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(labelArchivoAsociado, new Insets(10));

        tarjeta.getChildren().addAll(tarjetaContenidoBox, labelArchivoAsociado);

        Label instruccion = new Label("(Haz clic en la tarjeta para voltearla)");
        instruccion.getStyleClass().add("text-caption");

        tarjeta.setOnMouseClicked(e -> voltearTarjeta());

        // Botones de calificación
        btnFacil = new Button("Fácil");
        btnFacil.getStyleClass().add("modern-button-primary");
        btnFacil.setPrefWidth(120);
        btnFacil.setOnAction(e -> calificar(2));

        btnDificil = new Button("Difícil");
        btnDificil.getStyleClass().add("modern-button-secondary");
        btnDificil.setPrefWidth(120);
        btnDificil.setOnAction(e -> calificar(1));

        contenedorBotones = new VBox(10);
        contenedorBotones.setAlignment(Pos.CENTER);
        HBox filaBotones = new HBox(20, btnDificil, btnFacil);
        filaBotones.setAlignment(Pos.CENTER);
        contenedorBotones.getChildren().add(filaBotones);

        // Botón iniciar sesión
        Button btnIniciar = new Button("Iniciar Sesión de Estudio");
        btnIniciar.getStyleClass().add("modern-button-primary");
        btnIniciar.setOnAction(e -> {
            controller.iniciarSesionDeEstudio();
            mostrarTarjetaActual();
        });

        // Botón para eliminar el repaso (flashcard) que se está mostrando
        btnEliminarRepaso = new Button("Eliminar este repaso");
        btnEliminarRepaso.getStyleClass().addAll("modern-button-secondary", "btn-danger-text");
        btnEliminarRepaso.setOnAction(e -> confirmarEliminarRepaso());

        getChildren().addAll(titulo, labelProgreso, btnIniciar, infoBar, barraProgreso, tarjeta, instruccion, contenedorBotones, btnEliminarRepaso);

        // Estado inicial
        int pendientes = controller.getTarjetasPendientesHoy();
        labelProgreso.setText("Tarjetas pendientes hoy: " + pendientes
                + " | Total en el sistema: " + controller.getTotalFlashcards());
        labelContenido.setText("Presiona \"Iniciar Sesión\" para comenzar");
        contenedorBotones.setVisible(false);
        infoBar.setVisible(false);
        barraProgreso.setVisible(false);
        barraProgreso.setManaged(false);
        btnEliminarRepaso.setVisible(false);
        btnEliminarRepaso.setManaged(false);
    }

    /**
     * Muestra la tarjeta actual de la sesión (lado pregunta).
     */
    private void mostrarTarjetaActual() {
        Flashcard actual = controller.getFlashcardActual();
        if (actual == null) {
            mostrarSesionFinalizada();
            return;
        }

        mostrandoPregunta = true;
        labelContenido.setText(actual.getPregunta());
        labelIndicador.setText("PREGUNTA");
        
        labelContador.setText((controller.getIndiceActual() + 1) + " / " + controller.getTotalSesion());
        
        if (actual.getArchivoAsociado() != null) {
            labelArchivoAsociado.setText("Archivo: " + actual.getArchivoAsociado().getNombre());
            labelArchivoAsociado.setVisible(true);

            String ruta = actual.getArchivoAsociado().getRutaFisica();
            if (ruta != null && actual.getArchivoAsociado().getTipoMime().startsWith("image/")) {
                imageViewAsociada.setImage(new javafx.scene.image.Image(new java.io.File(ruta).toURI().toString()));
                imageViewAsociada.setVisible(true);
                imageViewAsociada.setManaged(true);
            } else {
                imageViewAsociada.setVisible(false);
                imageViewAsociada.setManaged(false);
            }
        } else {
            labelArchivoAsociado.setVisible(false);
            imageViewAsociada.setVisible(false);
            imageViewAsociada.setManaged(false);
        }

        labelIndicador.getParent().setVisible(true);
        contenedorBotones.setVisible(false);
        btnEliminarRepaso.setVisible(true);
        btnEliminarRepaso.setManaged(true);

        // Progreso = tarjetas ya repasadas / total de la sesión
        int total = controller.getTotalSesion();
        barraProgreso.setProgress(total > 0 ? (double) controller.getIndiceActual() / total : 0);
        barraProgreso.setVisible(true);
        barraProgreso.setManaged(true);
    }

    /**
     * Voltea la tarjeta desde un atajo de teclado (Espacio), si hay tarjeta activa.
     */
    public void voltearDesdeTeclado() {
        if (controller.getFlashcardActual() != null) {
            voltearTarjeta();
        }
    }

    /**
     * Califica desde un atajo de teclado (← difícil, → fácil), pero solo cuando
     * se está mostrando la respuesta (los botones de calificación están visibles).
     */
    public void calificarDesdeTeclado(int calificacion) {
        if (contenedorBotones.isVisible() && controller.getFlashcardActual() != null) {
            calificar(calificacion);
        }
    }

    /**
     * Voltea la tarjeta entre pregunta y respuesta.
     */
    private void voltearTarjeta() {
        Flashcard actual = controller.getFlashcardActual();
        if (actual == null) return;

        if (mostrandoPregunta) {
            labelContenido.setText(actual.getRespuesta());
            labelIndicador.setText("RESPUESTA");
            contenedorBotones.setVisible(true);
            
            imageViewAsociada.setVisible(false);
            imageViewAsociada.setManaged(false);
        } else {
            labelContenido.setText(actual.getPregunta());
            labelIndicador.setText("PREGUNTA");
            contenedorBotones.setVisible(false);
            
            if (actual.getArchivoAsociado() != null && actual.getArchivoAsociado().getRutaFisica() != null && actual.getArchivoAsociado().getTipoMime().startsWith("image/")) {
                imageViewAsociada.setVisible(true);
                imageViewAsociada.setManaged(true);
            }
        }
        mostrandoPregunta = !mostrandoPregunta;
    }

    /**
     * Califica la tarjeta actual y avanza a la siguiente.
     */
    private void calificar(int calificacion) {
        controller.calificarYAvanzar(calificacion);
        mostrarTarjetaActual();
    }

    /**
     * Muestra mensaje de sesión completada.
     */
    private void mostrarSesionFinalizada() {
        labelContenido.setText("¡Sesión completada!\nNo hay más tarjetas por repasar hoy.\n"
                + "Fácil: " + controller.getRespuestasFacil() + " | Difícil: " + controller.getRespuestasDificil());
        labelIndicador.getParent().setVisible(false);
        labelArchivoAsociado.setVisible(false);
        contenedorBotones.setVisible(false);
        imageViewAsociada.setVisible(false);
        imageViewAsociada.setManaged(false);
        barraProgreso.setVisible(false);
        barraProgreso.setManaged(false);
        btnEliminarRepaso.setVisible(false);
        btnEliminarRepaso.setManaged(false);
    }

    /**
     * Pide confirmación y elimina el repaso (flashcard) que se está mostrando.
     */
    private void confirmarEliminarRepaso() {
        Flashcard actual = controller.getFlashcardActual();
        if (actual == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar repaso");
        confirm.setHeaderText("¿Eliminar este repaso de ejercicio?");
        confirm.setContentText("Pregunta: " + actual.getPregunta());
        confirm.getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);

        java.util.Optional<ButtonType> respuesta = confirm.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            controller.eliminarFlashcardActual();
            Notificador.mostrar(this, "Repaso eliminado", Notificador.Tipo.DANGER);
            mostrarTarjetaActual();
        }
    }
}
