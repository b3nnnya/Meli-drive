package com.melidrive.vista;

import com.melidrive.modelo.DriveFile;
import com.melidrive.modelo.DriveFolder;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Diálogos de confirmación reutilizables.
 *
 * Centraliza la construcción de los Alert de borrado para que las vistas solo
 * decidan qué hacer con el resultado (true/false). Antes esta lógica estaba
 * incrustada en las vistas; extraerla aplica el principio de Responsabilidad
 * Única (SRP): la vista orquesta, este componente confirma.
 */
public final class DialogosConfirmacion {

    private DialogosConfirmacion() { }

    /**
     * Doble confirmación para eliminar una carpeta (puede arrastrar contenido).
     * @return true si el usuario confirma ambas veces.
     */
    public static boolean eliminarCarpeta(DriveFolder carpeta) {
        int total = carpeta.getSubcarpetas().size() + carpeta.getArchivos().size();

        Alert confirm1 = new Alert(Alert.AlertType.CONFIRMATION);
        confirm1.setTitle("Eliminar carpeta");
        confirm1.setHeaderText("¿Eliminar la carpeta \"" + carpeta.getNombre() + "\"?");
        confirm1.setContentText(total > 0
                ? "Contiene " + total + " elemento(s) que también se eliminarán."
                : "Esta carpeta está vacía.");
        confirm1.getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);
        Optional<ButtonType> r1 = confirm1.showAndWait();
        if (r1.isEmpty() || r1.get() != ButtonType.OK) {
            return false;
        }

        Alert confirm2 = new Alert(Alert.AlertType.WARNING);
        confirm2.setTitle("Confirmación final");
        confirm2.setHeaderText("Esta acción no se puede deshacer.");
        confirm2.setContentText("¿Seguro que deseas eliminar \"" + carpeta.getNombre() + "\" definitivamente?");
        confirm2.getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);
        Optional<ButtonType> r2 = confirm2.showAndWait();
        return r2.isPresent() && r2.get() == ButtonType.OK;
    }

    /**
     * Confirmación simple para eliminar un archivo (un solo elemento).
     * @return true si el usuario confirma.
     */
    public static boolean eliminarArchivo(DriveFile archivo) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar archivo");
        confirm.setHeaderText("¿Eliminar el archivo \"" + archivo.getNombre() + "\"?");
        confirm.setContentText("Se quitará de tu unidad. Esta acción no se puede deshacer.");
        confirm.getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);
        Optional<ButtonType> r = confirm.showAndWait();
        return r.isPresent() && r.get() == ButtonType.OK;
    }

    /**
     * Confirmación simple para eliminar un repaso (flashcard).
     * @return true si el usuario confirma.
     */
    public static boolean eliminarRepaso(String pregunta) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar repaso");
        confirm.setHeaderText("¿Eliminar este repaso de ejercicio?");
        confirm.setContentText("Pregunta: " + pregunta);
        confirm.getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.OK);
        Optional<ButtonType> r = confirm.showAndWait();
        return r.isPresent() && r.get() == ButtonType.OK;
    }
}
