package com.melidrive.util;

import javafx.scene.Scene;

public class ThemeManager {

    public static void aplicarDarkMode(Scene scene) {

        scene.getStylesheets().clear();

        scene.getStylesheets().add(
                ThemeManager.class
                        .getResource("/styles/main.css")
                        .toExternalForm()
        );

        scene.getStylesheets().add(
                ThemeManager.class
                        .getResource("/styles/dark.css")
                        .toExternalForm()
        );
    }
}