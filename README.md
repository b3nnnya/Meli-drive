# SIGEA — Sistema Inteligente de Gestión y Estudio Académico

Aplicación de escritorio desarrollada en **Java + JavaFX** que funciona como un gestor de archivos académico estilo Google Drive / Obsidian. Integra un explorador de archivos, importación de archivos físicos, visor de PDF con zoom, recortador de ejercicios, etiquetado de documentos y un módulo de estudio con tarjetas de repaso espaciado (flashcards). Todo el estado se guarda en disco y se restaura al volver a abrir la aplicación.

> El repositorio se llama `Meli-drive` por motivos históricos; el producto es **SIGEA**.

---

## ✨ Funcionalidades

### Pantalla de inicio y apariencia
- **Pantalla de bienvenida (launcher)** estilo Obsidian con accesos *Inicio rápido*, *Crear una bóveda nueva* y *Abrir una carpeta como bóveda*.
- **Tema claro / oscuro** conmutable y **persistente** entre sesiones.
- **Identidad visual azul brillante** y **iconografía propia** (sin emojis) en navegación, barra de herramientas y tipos de archivo.

### Explorador de archivos
- Navegación por carpetas con **breadcrumb** (ruta clickeable) y botón *Atrás*.
- **Vista de cuadrícula o de lista** conmutable.
- **Importación de archivos físicos** (PDF/imágenes) que se copian de forma segura a la base local.
- **Eliminación con menú contextual** (clic derecho): carpetas con doble confirmación y archivos con confirmación simple.
- **Estado vacío** ilustrado e **íconos por tipo de archivo** (PDF, imagen, documento, video, audio, etc.).

### Visor de documentos
- Renderizado de **PDF en alta calidad** (vía *Apache PDFBox*).
- **Controles de zoom** (−, %, +, 1:1) y **divisor ajustable** entre el documento y el panel de etiquetas.
- **Herramienta de recorte**: selecciona un área del documento (un ejercicio) y captúrala como imagen.
- **Etiquetas con color personalizable** (selector de color) y búsqueda por etiqueta con chips de las etiquetas existentes.

### Modo Estudio (Flashcards)
- Tarjetas de **repaso espaciado**: el algoritmo programa el siguiente repaso según si fue *Fácil* o *Difícil*.
- Soporta **imágenes** (recortes) como pregunta.
- **Barra de progreso** de la sesión y **eliminación** de un repaso desde la propia tarjeta.

### Productividad
- **Notificaciones toast** que confirman acciones (crear, importar, eliminar, etiquetar).
- **Atajos de teclado**: `Ctrl+F` enfoca el buscador, `Esc` vuelve al explorador, y en Modo Estudio `Espacio` voltea la tarjeta mientras `←` / `→` la califican.
- **Persistencia completa**: el árbol de carpetas, archivos, etiquetas y flashcards se serializan a disco y se restauran al abrir.

---

## 🏗️ Arquitectura MVC y Patrones Aplicados

El proyecto sigue el patrón **MVC (Modelo - Vista - Controlador)** y aplica patrones de diseño como **Composite**, **Facade** y **Plantilla/Abstracción** (clase base abstracta `ElementoDrive`).

```
src/main/java/com/melidrive/
├── MainApp.java                     # Entrypoint JavaFX (muestra la bienvenida y la app)
├── modelo/                          # Capa de Datos (lógica de negocio, serializable)
│   ├── ElementoDrive.java           # Clase ABSTRACTA base: id, nombre, esCarpeta(), getTamanioTotal()
│   ├── DriveFile.java               # Archivo  (extiende ElementoDrive)
│   ├── DriveFolder.java             # Carpeta  (extiende ElementoDrive · patrón Composite)
│   ├── Etiqueta.java                # Etiqueta manual con color
│   ├── Flashcard.java               # Tarjeta de estudio con algoritmo de repaso
│   ├── GestorArchivos.java          # Fachada (Facade) del árbol de carpetas
│   └── EstadoAplicacion.java        # Contenedor serializable del estado completo
├── controlador/                     # Controladores (intermediarios)
│   ├── MainController.java          # Orquesta navegación, tema y persistencia
│   ├── ExploradorController.java    # Navegación, importación y borrado
│   ├── VisorDocumentoController.java # Renderizado, zoom y recortes de PDF
│   ├── FlashcardController.java     # Lógica de la sesión de estudio
│   └── SidebarController.java       # Navegación entre secciones
├── vista/                           # Capa de Presentación (GUIs JavaFX)
│   ├── BienvenidaView.java          # Pantalla de inicio (launcher)
│   ├── MainView.java                # Layout, buscador, sidebar y atajos de teclado
│   ├── ExploradorView.java          # Cuadrícula/lista, breadcrumb y menús contextuales
│   ├── FlashcardView.java           # Tarjetas con barra de progreso
│   ├── VisorDocumentoView.java      # Visor de PDF/imagen con zoom y recorte
│   ├── BusquedaEtiquetaView.java    # Búsqueda por etiqueta con chips
│   ├── DialogoNuevaCarpeta.java     # Diálogo de creación de carpeta
│   └── DialogoNuevoEjercicio.java   # Diálogo de creación de ejercicio
└── util/                            # Utilidades transversales
    ├── ThemeManager.java            # Tema claro/oscuro
    ├── RepositorioDatos.java        # Persistencia en disco (serialización Java)
    ├── Notificador.java             # Notificaciones toast
    └── IconosUtil.java              # Carga e íconos por tipo de archivo
```

### Pilares de POO destacados
- **Herencia / Abstracción:** `DriveFile` y `DriveFolder` heredan de la clase abstracta `ElementoDrive`, que centraliza la identidad (`id`, `nombre`) y declara el comportamiento polimórfico (`esCarpeta()`, `getTamanioTotal()`).
- **Composite:** una `DriveFolder` puede contener archivos y otras carpetas.
- **Facade:** `GestorArchivos` simplifica la creación, búsqueda y recorrido del árbol.
- **Encapsulamiento:** atributos protegidos expuestos mediante getters/setters.

---

## 🛠️ Stack Tecnológico

| Tecnología | Versión | Propósito |
|---|---|---|
| **Java** | 17 | Lenguaje base orientado a objetos |
| **JavaFX** | 17.0.6 | Interfaz gráfica y estilos CSS (temas claro/oscuro) |
| **Apache PDFBox** | 2.0.29 | Renderizado nativo de PDFs a imágenes |
| **JavaFX Swing** | 17.0.6 | Puente `BufferedImage` → vistas FX |
| **Serialización Java** | — | Persistencia del estado en `data/melidrive.dat` |
| **Maven** | 3.x | Gestión de dependencias y construcción |
| **JUnit 5** | 5.10.0 | Pruebas unitarias de la capa de modelo |

---

## 🚀 Instalación y Ejecución

### Requisitos previos
- **JDK 17** o superior
- **Maven 3.x** en el PATH

### 1. Clonar el repositorio
```bash
git clone https://github.com/b3nnnya/Meli-drive.git
cd Meli-drive
```

### 2. Ejecutar la aplicación
```bash
mvn clean compile javafx:run
```

Al abrir, aparece la **pantalla de bienvenida**; pulsa *Inicio rápido* para entrar a la aplicación.

---

## 📂 Gestión de Datos Locales

El estado y los archivos se guardan automáticamente dentro del proyecto (la carpeta `data/` está excluida del control de versiones):
- `data/melidrive.dat` — Estado serializado (carpetas, archivos, etiquetas y flashcards).
- `data/archivos/` — Copias de los PDFs e imágenes importadas.
- `data/recortes/` — Capturas de ejercicios creadas con el visor.

El estado se persiste al cerrar la aplicación y se restaura al volver a abrirla.

---

## 👥 Autores
Proyecto universitario desarrollado para la aplicación de POO y UML.

## 📄 Licencia
Uso exclusivamente académico.
