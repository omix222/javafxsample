# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Run the application
mvn javafx:run

# Compile only
mvn compile

# Clean build artifacts
mvn clean

# Clean and run
mvn clean javafx:run
```

## Architecture

JavaFX 21 + Maven application using the MVC pattern with FXML.

- **`HelloApplication.java`** — Entry point. Loads `hello-view.fxml` and sets up the `Stage`.
- **`HelloController.java`** — Handles button events (`onHelloButtonClick`, `onClearButtonClick`). Bound to the FXML via `fx:controller`.
- **`hello-view.fxml`** — Declares the UI layout (`VBox` with `Label`, `TextField`, `Button`). References controller via `fx:controller` and binds fields/handlers via `fx:id` and `onAction`.
- **`module-info.java`** — Java module descriptor. Opens `com.example.hellofx` to `javafx.fxml` for reflection-based FXML injection.

## Key Configuration

- JavaFX version: `21.0.5` (defined in `pom.xml` as `${javafx.version}`)
- Main class format for the plugin: `<module>/<fully-qualified-class>` — `com.example.hellofx/com.example.hellofx.HelloApplication`
- Compiler target is Java 21 (`-source 21 -target 21`). If upgrading, consider switching to `--release 21` to suppress the system modules warning.
