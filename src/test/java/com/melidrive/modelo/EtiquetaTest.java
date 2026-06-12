package com.melidrive.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EtiquetaTest {

    @Test
    public void testCrearEtiquetaYAtributos() {
        Etiqueta etiqueta = new Etiqueta("t-1", "Importante", "#FF0000");
        
        assertEquals("t-1", etiqueta.getId());
        assertEquals("Importante", etiqueta.getNombre());
        assertEquals("#FF0000", etiqueta.getColorHex());
        assertTrue(etiqueta.toString().contains("Importante"));
    }

    @Test
    public void testEqualsYHashCode() {
        Etiqueta tag1 = new Etiqueta("t-1", "Urgente", "#FF0000");
        Etiqueta tag2 = new Etiqueta("t-1", "Urgente", "#FF0000");
        Etiqueta tag3 = new Etiqueta("t-2", "Normal", "#00FF00");

        assertEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);
        assertEquals(tag1.hashCode(), tag2.hashCode());
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }

    @Test
    void toStringDebeRetornarNombre() {

        Etiqueta etiqueta =
                new Etiqueta("1","Parcial","#000");

        assertEquals("Parcial",
                etiqueta.toString());
    }
}
