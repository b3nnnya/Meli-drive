# 📁 Meli-Drive — S.I.G.E.A.

**Sistema Inteligente de Gestión y Estudio Académico**

Aplicación de escritorio desarrollada en Java con JavaFX que simula un gestor de archivos estilo Google Drive orientado al ámbito académico. Incluye explorador de archivos, etiquetado manual de documentos y un módulo de estudio con tarjetas de repaso espaciado (Flashcards).

---

## 🖥️ Capturas

| Explorador de Archivos | Modo Estudio (Flashcards) |
|:-:|:-:|
| Navegación por carpetas con íconos | Tarjetas con repaso espaciado |

---

## ✨ Funcionalidades

- **Explorador de Archivos** — Navegación por carpetas y archivos con íconos según tipo MIME
- **Creación de Carpetas** — Diálogo modal para crear nuevas carpetas dentro de la ubicación actual
- **Visor de Documentos** — Visualización de detalles del archivo (nombre, tipo, tamaño)
- **Sistema de Etiquetas** — Agregar y eliminar etiquetas manuales a cada documento
- **Buscador** — Búsqueda recursiva de archivos por nombre en todo el árbol de carpetas
- **Flashcards** — Módulo de estudio con repaso espaciado (Spaced Repetition)
  - Volteo de tarjetas (pregunta ↔ respuesta)
  - Calificación: Fácil / Difícil
  - Algoritmo de incremento exponencial para programar repasos

---

## 🏗️ Arquitectura

El proyecto sigue el patrón **MVC (Modelo - Vista - Controlador)**:

```
src/main/java/com/melidrive/
├── MainApp.java                    # Punto de entrada JavaFX
├── ConsolaTest.java                # Pruebas por consola (sin GUI)
├── modelo/                         # Capa de datos
│   ├── DriveFile.java              # Archivo lógico
│   ├── DriveFolder.java            # Carpeta (patrón Composite)
│   ├── Etiqueta.java               # Etiqueta manual
│   ├── Flashcard.java              # Tarjeta de estudio
│   └── GestorArchivos.java         # Fachada central del modelo
├── controlador/                    # Lógica de negocio
│   ├── MainController.java         # Orquestador central
│   ├── ExploradorController.java   # Navegación de archivos
│   ├── SidebarController.java      # Menú lateral
│   ├── VisorDocumentoController.java # Visualización de documentos
│   └── FlashcardController.java    # Sesiones de repaso
├── vista/                          # Interfaz gráfica (JavaFX)
│   ├── MainView.java               # Layout principal
│   ├── ExploradorView.java         # Vista del explorador
│   ├── FlashcardView.java          # Vista de flashcards
│   ├── VisorDocumentoView.java     # Vista del visor
│   └── DialogoNuevaCarpeta.java    # Diálogo modal
└── util/                           # Utilidades
    └── IconosUtil.java             # Asignación de íconos por MIME
```

---

## 🛠️ Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje principal |
| JavaFX | 17.0.6 | Interfaz gráfica |
| Maven | 3.x | Gestión de dependencias y build |
| Tess4j | 5.13.0 | OCR (extracción de texto de imágenes) |
| JUnit 5 | 5.10.0 | Testing |

---

## 🚀 Instalación y Ejecución

### Requisitos previos
- **JDK 17** o superior
- **Maven 3.x**

### Clonar el repositorio
```bash
git clone https://github.com/b3nnnya/Meli-drive.git
cd Meli-drive
```

### Compilar y ejecutar
```bash
mvn javafx:run
```

### Ejecutar pruebas por consola (sin interfaz)
```bash
mvn compile exec:java -Dexec.mainClass="com.melidrive.ConsolaTest"
```

---

## 📂 Recursos

Los íconos de tipos de archivo se encuentran en:
```
src/main/resources/icons/
```

| Ícono | Archivo |
|---|---|
| 📁 Carpeta | `folder.png` |
| 📄 PDF | `pdf.png` |
| 🖼️ Imagen | `image.png` |
| 📝 Documento | `document.png` |
| 📊 Hoja de cálculo | `spreadsheet.png` |
| 📽️ Presentación | `presentation.png` |
| 🎬 Video | `video.png` |
| 🎵 Audio | `audio.png` |
| 📃 Texto | `text.png` |
| 📎 Genérico | `generic.png` |

---

## 👥 Autores

Proyecto universitario desarrollado por el equipo Meli-Drive.

---

## 📄 Licencia

Este proyecto es de uso académico.
