package com.melidrive.vista.componentes;

import javafx.scene.control.Button;

public class SidebarButton extends Button {

    public SidebarButton(String texto) {
        super(texto);
        getStyleClass().add("sidebar-button");
        setMaxWidth(Double.MAX_VALUE);
    }

    public void activar() {
        if (!getStyleClass().contains("sidebar-button-active")) {
            getStyleClass().add("sidebar-button-active");
        }
    }

    public void desactivar() {
        getStyleClass().remove("sidebar-button-active");
    }
}