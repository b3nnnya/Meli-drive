package com.melidrive.util;

import javafx.scene.Scene;

/**
 * Gestor de temas visuales de la aplicación.
 * Permite alternar entre modo claro y modo oscuro
 * aplicando o removiendo la hoja de estilos dark.css.
 */
public class ThemeManager {

    private static final String DARK_CSS = "styles/dark.css";
    private static final String MAIN_CSS = "styles/main.css";

    private boolean modoOscuro;
    private Scene scene;

    public ThemeManager() {
        this.modoOscuro = false;
    }

    /**
     * Vincula la Scene principal para poder manipular sus stylesheets.
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Alterna entre modo claro y modo oscuro.
     * Agrega o remueve el stylesheet dark.css de la Scene.
     */
    public void toggleModoOscuro() {
        if (scene == null) {
            System.out.println("ThemeManager: Scene no vinculada.");
            return;
        }

        modoOscuro = !modoOscuro;

        String darkCssUrl = getClass().getClassLoader().getResource(DARK_CSS) != null
                ? getClass().getClassLoader().getResource(DARK_CSS).toExternalForm()
                : null;

        if (darkCssUrl == null) {
            System.out.println("ThemeManager: No se encontró el archivo dark.css");
            return;
        }

        if (modoOscuro) {
            if (!scene.getStylesheets().contains(darkCssUrl)) {
                scene.getStylesheets().add(darkCssUrl);
            }
            System.out.println("ThemeManager: Modo oscuro activado.");
        } else {
            scene.getStylesheets().remove(darkCssUrl);
            System.out.println("ThemeManager: Modo claro activado.");
        }
    }

    /**
     * Aplica el stylesheet principal (main.css) a la Scene.
     */
    public void aplicarEstiloPrincipal() {
        if (scene == null) return;

        String mainCssUrl = getClass().getClassLoader().getResource(MAIN_CSS) != null
                ? getClass().getClassLoader().getResource(MAIN_CSS).toExternalForm()
                : null;

        if (mainCssUrl != null && !scene.getStylesheets().contains(mainCssUrl)) {
            scene.getStylesheets().add(mainCssUrl);
            System.out.println("ThemeManager: Estilo principal aplicado.");
        }
    }

    /**
     * Indica si el modo oscuro está activo.
     */
    public boolean isModoOscuro() {
        return modoOscuro;
    }
}
