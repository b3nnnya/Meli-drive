package com.melidrive.controlador;

import com.melidrive.modelo.DriveFolder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExploradorControllerTest {

    @Test
    void debeIniciarEnCarpetaRaiz() {
        MainController main = new MainController();
        ExploradorController controller = main.getExploradorController();

        assertEquals(
                main.getGestorArchivos().getCarpetaRaiz(),
                controller.getCarpetaActual()
        );
    }

    @Test
    void crearNuevaCarpetaDebeAgregarla() {
        MainController main = new MainController();
        ExploradorController controller = main.getExploradorController();

        int cantidadInicial =
                controller.getCarpetaActual().getSubcarpetas().size();

        controller.crearNuevaCarpeta("Nueva");

        assertEquals(
                cantidadInicial + 1,
                controller.getCarpetaActual().getSubcarpetas().size()
        );
    }

    @Test
    void noDebeCrearCarpetaConNombreVacio() {
        MainController main = new MainController();
        ExploradorController controller = main.getExploradorController();

        int cantidadInicial =
                controller.getCarpetaActual().getSubcarpetas().size();

        controller.crearNuevaCarpeta("   ");

        assertEquals(
                cantidadInicial,
                controller.getCarpetaActual().getSubcarpetas().size()
        );
    }

    @Test
    void entrarYVolverDebeFuncionar() {
        MainController main = new MainController();
        ExploradorController controller = main.getExploradorController();

        DriveFolder sub =
                controller.getCarpetaActual().getSubcarpetas().get(0);

        controller.entrarACarpeta(sub);

        assertEquals(sub, controller.getCarpetaActual());

        controller.volverAtras();

        assertEquals(
                main.getGestorArchivos().getCarpetaRaiz(),
                controller.getCarpetaActual()
        );
    }
}