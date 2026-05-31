# 📁 Meli-Drive — S.I.G.E.A.

**Sistema Inteligente de Gestión y Estudio Académico**

Aplicación de escritorio desarrollada en Java con JavaFX que simula un gestor de archivos estilo Google Drive orientado al ámbito académico. Incluye explorador de archivos, importación de archivos físicos, visualización de PDFs, recortador de imágenes, etiquetado manual de documentos y un módulo de estudio con tarjetas de repaso espaciado (Flashcards visuales).

---

## 🖥️ Capturas

| Explorador de Archivos | Visor PDF y Recortes | Modo Estudio (Flashcards) |
|:-:|:-:|:-:|
| Interfaz moderna para gestionar carpetas e importar archivos físicos. | Visualización de PDF integrada con herramienta para recortar ejercicios. | Tarjetas con repaso espaciado que soportan imágenes como preguntas. |

---

## ✨ Funcionalidades Principales

- **Explorador de Archivos (Modernizado)** — Navegación fluida por carpetas y archivos con un sistema de diseño UI moderno y animaciones.
- **Importación de Archivos Físicos** — Permite cargar archivos PDF o imágenes reales desde tu computadora y copiarlos de forma segura a la base de datos local.
- **Visor de Documentos Interactivo** — Renderiza páginas de archivos PDF en alta calidad (vía *Apache PDFBox*).
- **Herramienta de Recortes (Ejercicios)** — Selecciona cualquier área de un documento (ej: un problema de física) arrastrando el ratón para capturarlo como imagen.
- **Buscador y Sistema de Etiquetas** — Agrega etiquetas con colores personalizados y busca documentos rápidamente.
- **Flashcards Visuales (Spaced Repetition)** — Crea tarjetas de estudio usando tus recortes de imágenes como pregunta y una solución en texto. El algoritmo programa automáticamente el siguiente repaso en función de si te resultó fácil o difícil.

---

## 🏗️ Arquitectura MVC y Patrones Aplicados

El proyecto sigue el patrón de arquitectura **MVC (Modelo - Vista - Controlador)**, además de patrones de diseño como **Composite** y **Facade**:

```
src/main/java/com/melidrive/
├── Launcher.java                   # Puente de entrada (Evita config. de módulos JavaFX)
├── MainApp.java                    # Entrypoint JavaFX
├── modelo/                         # Capa de Datos (Lógica de Negocio Pura)
│   ├── DriveFile.java              # Abstracción de un Archivo (Físico y Lógico)
│   ├── DriveFolder.java            # Carpeta (Implementa patrón Composite)
│   ├── Etiqueta.java               # Etiqueta manual de clasificación
│   ├── Flashcard.java              # Tarjeta de estudio con algoritmo de repaso
│   └── GestorArchivos.java         # Fachada (Facade) para manejar todo el árbol
├── controlador/                    # Controladores (Intermediarios)
│   ├── MainController.java         # Orquestador de navegación de vistas
│   ├── ExploradorController.java   # Maneja eventos del explorador e importaciones
│   ├── VisorDocumentoController.java # Controla el renderizado y recortes de PDFs
│   └── FlashcardController.java    # Controla la lógica de la sesión de estudio
└── vista/                          # Capa de Presentación (GUIs JavaFX)
    ├── MainView.java               # Layout envolvente y buscador
    ├── ExploradorView.java         # Interfaz de grillas y navegación
    ├── FlashcardView.java          # Interfaz interactiva de tarjetas
    ├── VisorDocumentoView.java     # Canvas y visor de imágenes/PDF
    └── DialogoNuevoEjercicio.java  # Diálogo de creación con previsualización
```

---

## 🛠️ Stack Tecnológico

| Tecnología | Versión | Propósito |
|---|---|---|
| **Java** | 17 | Lenguaje base orientado a objetos |
| **JavaFX** | 17.0.6 | Motor de la interfaz gráfica y estilos CSS |
| **Apache PDFBox** | 2.0.29 | Renderizado nativo de PDFs a imágenes |
| **JavaFX Swing** | 17.0.6 | Puente para procesar BufferedImage a vistas FX |
| **Maven** | 3.x | Orquestador de dependencias y construcción |
| **JUnit 5** | 5.10.0 | Pruebas unitarias de modelos de negocio |

---

## 🚀 Instalación y Ejecución

### Requisitos previos
- **JDK 17** o superior
- **Maven 3.x** instalado en el PATH

### 1. Clonar el repositorio
```bash
git clone https://github.com/b3nnnya/Meli-drive.git
cd Meli-drive
```

### 2. Ejecutar la aplicación (Sin configurar módulos)
Gracias al `Launcher.java`, puedes ejecutar el proyecto directamente desde tu IDE (Run) o mediante Maven:
```bash
mvn clean compile javafx:run
```

---

## 📂 Gestión de Datos Locales

El sistema almacena el contenido en el directorio del proyecto automáticamente:
- `Meli-Drive/data/archivos/`: Copias de los PDFs e Imágenes importadas.
- `Meli-Drive/data/recortes/`: Las capturas de ejercicios creadas con el visor.

---

## 👥 Autores
Proyecto universitario desarrollado para la aplicación de POO y UML.

## 📄 Licencia
Uso exclusivamente académico.
