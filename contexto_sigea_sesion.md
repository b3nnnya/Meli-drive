# Contexto General de Meli-Drive (S.I.G.E.A) - Handoff

## Estado del Proyecto
S.I.G.E.A (Sistema Integrado de Gestión y Edición Académica) es el rediseño del proyecto original "Meli-Drive". 
Debido a la retroalimentación del profesor Samuel Sepúlveda, el alcance fue **drásticamente acotado** para viabilizarlo en un semestre formativo de Java Orientado a Objetos (POO).

### Decisiones Arquitectónicas Actuales (Importante para IA o Devs):
1. **NO se usará la API de Google Drive**: Todo será gestionado de manera local (simulando un sistema de archivos jerárquico).
2. **NO se implementará automatización OCR de imágenes real**: El proyecto incluye dependencias `tess4j` en base a peticiones anteriores, pero el diseño principal de la búsqueda indexada se apoyará orgánicamente en un sistema de **Etiquetas manuales** para explotar Streams y Colecciones en Java.
3. **UI en JavaFX**: La interfaz de usuario no será CLI pura, sino que estará basada en Escritorio (JavaFX).

## ¿Qué está implementado hasta ahora? (Capa de Modelo - MVC)
El patrón de diseño subyacente incluye *Composite, Encapsulamiento y Composición*:

- `pom.xml`: Inicializado con dependencias para JavaFX y JUnit.
- `DriveFolder.java` y `DriveFile.java`: Árbol de directorios implementando patrón composite. `DriveFile` admite ahora una colección de etiquetas adjuntas.
- `GestorArchivos.java`: "Controlador" o Gestor Lógico que simula un Root Manager, permitiendo meter y sacar cosas y aplicar búsquedas recursivas en la jerarquía de las carpetas.
- `Etiqueta.java`: Objeto clasificador de metadatos manual (sustituto inteligente del viejo motor de OCR).
- `Flashcard.java`: Tarjeta de objeto de repaso que envuelve un `DriveFile` para el motor "Spaced Repetition".
- `ConsolaTest.java`: Un script rápido y feo en la raíz del backend solo para probar que el GestorArchivos almacena y busca bien las cosas por detrás antes de conectarlo a la UI.

Todo lo anterior se probó exitosamente y está sincronizado (pusheado) en la rama origin de GitHub del usuario.

## ¿Qué falta implementar en la PRÓXIMA Sesión?
1. **Vista JavaFX (Main / VistaPrincipal)**: 
   - Crear el `AppSigea.java` que herede de `javafx.application.Application`.
   - Implementar el Dashboard Izquierdo (arbol visual reflejando la jerarquía de `GestorArchivos`) y el Panel Central (cuadricula de archivos `DriveFile`).
2. **Controladores UI**: Enchufar botones de "Nueva Etiqueta" a los métodos de POO ya creados.
3. **Persistencia de Datos**: Ocupar `java.io.Serializable` (o JSON local) al interior de `GestorArchivos` para poder guardar la memoria en un archivo de texto en el disco del alumno y cargarlo cada vez que abra JavaFX.
4. **Vista de Estudio (Flashcards)**: Una vista iterativa interactiva que consuma los días que queden en las lógicas de `Flashcard.java`.
