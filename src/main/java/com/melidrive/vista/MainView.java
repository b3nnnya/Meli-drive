package com.melidrive.vista;

import com.melidrive.controlador.FlashcardController;
import com.melidrive.controlador.ExploradorController;
import com.melidrive.controlador.MainController;
import com.melidrive.controlador.VisorDocumentoController;
import com.melidrive.modelo.DriveFile;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
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
    private Button btnModoOscuro;
    private TextField buscador;
    private FlashcardView flashcardViewActiva;

    public MainView(MainController mainController) {
        this.mainController = mainController;

        // NOTA: main.css se aplica a nivel de Scene desde ThemeManager.aplicarEstiloPrincipal().
        // No debe cargarse aquí sobre el nodo raíz: en JavaFX las hojas de estilo de un Parent
        // tienen prioridad sobre las de la Scene, lo que impediría que dark.css (aplicado en la
        // Scene) sobrescriba a main.css y dejaría el modo oscuro sin efecto.

        // === BARRA SUPERIOR ===
        buscador = new TextField();
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

        btnModoOscuro = new Button("🌙  Modo Oscuro");
        btnModoOscuro.getStyleClass().add("modern-button-secondary");
        btnModoOscuro.setOnAction(e -> {
            mainController.toggleModoOscuro();
            actualizarBotonTema();
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

        btnExplorador = new Button("📁  Mi Unidad");
        btnBuscarEtiqueta = new Button("🏷  Buscar por Etiqueta");
        btnFlashcards = new Button("📚  Modo Estudio");

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

        // === ÁREA CENTRAL ===
        areaCentral = new StackPane();
        areaCentral.getStyleClass().add("content-area");

        // === DIVISOR ARRASTRABLE ENTRE SIDEBAR Y ÁREA CENTRAL ===
        // SplitPane con divisor sutil (ver estilo .split-pane en los CSS): casi invisible
        // en reposo y con un leve resalte al pasar el mouse, para no recargar la interfaz.
        SplitPane splitPrincipal = new SplitPane(sidebar, areaCentral);
        splitPrincipal.setOrientation(Orientation.HORIZONTAL);
        // El sidebar arranca ocupando ~23% del ancho (≈220px en 960px).
        splitPrincipal.setDividerPositions(0.23);
        // Límites para que el sidebar no se colapse ni crezca demasiado al arrastrar.
        sidebar.setMinWidth(170);
        sidebar.setMaxWidth(380);
        // Al redimensionar la ventana, el espacio extra va al área central, no al sidebar.
        SplitPane.setResizableWithParent(sidebar, false);

        setCenter(splitPrincipal);

        // Configurar los atajos de teclado en cuanto la escena esté disponible.
        sceneProperty().addListener((obs, anterior, nueva) -> {
            if (nueva != null) {
                configurarAtajos(nueva);
            }
        });
    }

    /**
     * Registra los atajos de teclado globales:
     * Ctrl+F enfoca el buscador, Esc vuelve al explorador, y en el Modo Estudio
     * Espacio voltea la tarjeta mientras ← / → la califican (difícil / fácil).
     * No interfiere cuando el foco está en un campo de texto.
     */
    private void configurarAtajos(Scene scene) {
        final KeyCombination ctrlF = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, ev -> {
            if (ctrlF.match(ev)) {
                buscador.requestFocus();
                buscador.selectAll();
                ev.consume();
                return;
            }
            if (ev.getCode() == KeyCode.ESCAPE) {
                mainController.mostrarExplorador();
                ev.consume();
                return;
            }

            // Si se está escribiendo en un campo de texto, no robar las teclas.
            if (scene.getFocusOwner() instanceof TextInputControl) {
                return;
            }

            // Atajos exclusivos del Modo Estudio.
            if (flashcardViewActiva != null) {
                switch (ev.getCode()) {
                    case SPACE:
                        flashcardViewActiva.voltearDesdeTeclado();
                        ev.consume();
                        break;
                    case LEFT:
                        flashcardViewActiva.calificarDesdeTeclado(1);
                        ev.consume();
                        break;
                    case RIGHT:
                        flashcardViewActiva.calificarDesdeTeclado(2);
                        ev.consume();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * Actualiza el texto y el icono del botón de tema según el estado actual
     * (sol = pasar a claro cuando está oscuro; luna = pasar a oscuro cuando está claro).
     * Es público para que el controlador lo invoque tras restaurar el tema guardado.
     */
    public void actualizarBotonTema() {
        boolean oscuro = mainController.getThemeManager() != null
                && mainController.getThemeManager().isModoOscuro();
        btnModoOscuro.setText(oscuro ? "☀  Modo Claro" : "🌙  Modo Oscuro");
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
        flashcardViewActiva = null;
        ExploradorView vista = new ExploradorView(controller, mainController);
        areaCentral.getChildren().setAll(vista);
    }

    /**
     * Reemplaza el contenido central con la vista de Flashcards.
     */
    public void mostrarFlashcards(FlashcardController controller) {
        FlashcardView vista = new FlashcardView(controller);
        flashcardViewActiva = vista;
        areaCentral.getChildren().setAll(vista);
    }

    /**
     * Reemplaza el contenido central con el Visor de Documentos.
     */
    public void mostrarVisorDocumento(VisorDocumentoController controller) {
        flashcardViewActiva = null;
        VisorDocumentoView vista = new VisorDocumentoView(controller, mainController);
        areaCentral.getChildren().setAll(vista);
    }

    /**
     * Reemplaza el contenido central con la vista de Búsqueda por Etiqueta.
     */
    public void mostrarBusquedaPorEtiqueta(MainController controller) {
        flashcardViewActiva = null;
        BusquedaEtiquetaView vista = new BusquedaEtiquetaView(controller);
        areaCentral.getChildren().setAll(vista);
    }

    /**
     * Muestra resultados de búsqueda en el área central.
     */
    private void mostrarResultadosBusqueda(List<DriveFile> resultados, String termino) {
        flashcardViewActiva = null;
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
