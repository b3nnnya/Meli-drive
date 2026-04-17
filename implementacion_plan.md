# S.I.G.E.A - Plan de Implementación (Versión Acotada Curso POO)

## Veredicto y Ajuste
Reestructuración del proyecto omitiendo Drive API y OCR, usando Java y JavaFX localmente.

## 1. Modelo (Reglas de Negocio en POO)
- **Documento Academico**: Remplaza la interacción Drive, guardando solo atributos locales `(id, nombre, rutaLocal)`.
- **Etiqueta**: El usuario clasifica a mano (sustituto inteligente del OCR).
- **Asignatura**: Manejo intensivo de colecciones (List/Map) manejando múltiples Documentos.
- **Flashcard**: Sistema simple que calcula la próxima fecha de repaso de un Documento.
- **GestorAcademico**: Fachada principal para serializar/guardar los datos localmente.

## 2. Vista y Controladores (JavaFX)
- **Pantalla Principal (MainView)**: Dashboard mostrando lista de materias y tabla de apuntes en el disco.
- **Pantalla Estudio (StudyView)**: Mostrará las "Flashcards" pendientes del día con interfaz de botones (Bien/Mal).
