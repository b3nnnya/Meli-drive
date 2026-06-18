package com.melidrive.controlador;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SidebarControllerTest {

    @Test
    void debeIniciarEnMiUnidad() {

        SidebarController controller =
                new SidebarController(new MainController());

        assertEquals(
                SidebarController.SeccionApp.MI_UNIDAD,
                controller.getSeccionActiva()
        );
    }

    @Test
    void irAModoEstudioDebeActualizarSeccion() {

        SidebarController controller =
                new SidebarController(new MainController());

        controller.irAModoEstudio();

        assertEquals(
                SidebarController.SeccionApp.MODO_ESTUDIO,
                controller.getSeccionActiva()
        );
    }

    @Test
    void irABuscarPorEtiquetaDebeActualizarSeccion() {

        SidebarController controller =
                new SidebarController(new MainController());

        controller.irABuscarPorEtiqueta();

        assertEquals(
                SidebarController.SeccionApp.BUSCAR_POR_ETIQUETA,
                controller.getSeccionActiva()
        );
    }

    @Test
    void navegarADebeCambiarCorrectamente() {

        SidebarController controller =
                new SidebarController(new MainController());

        controller.navegarA(
                SidebarController.SeccionApp.MODO_ESTUDIO
        );

        assertEquals(
                SidebarController.SeccionApp.MODO_ESTUDIO,
                controller.getSeccionActiva()
        );
    }
}