# AsteroidsFinal

This is the final Asteroids game. It is split into JPMS modules, and the game parts are loaded as services from the `plugins` folder.

## Course progression

| Step | Repository | Main subject |
|---|---|---|
| 1 | [GameLab](https://github.com/IlyasOsman29/GameLab) | JavaFX and basic game components |
| 2 | [JavaLab](https://github.com/IlyasOsman29/JavaLab) | Java `ServiceLoader` |
| 3 | [JPMSLabs](https://github.com/IlyasOsman29/JPMSLabs) | Modules, services and `ModuleLayer` |
| 4 | [SpringLab](https://github.com/IlyasOsman29/SpringLab) | Spring dependency injection |
| 5 | [TestLab](https://github.com/IlyasOsman29/TestLab) | Component tests with JUnit |
| 6 | [MicroServiceLab](https://github.com/IlyasOsman29/MicroServiceLab) | Score service and `RestTemplate` |
| 7 | AsteroidsFinal | Combined dynamic plugin demonstration |

The project contains these parts:

- `api`: shared entity classes and service interfaces
- `core`: game loop, input, drawing and plugin loading
- `player`, `enemy`, `asteroids`, `weapon`: game objects and movement
- `collision`: collision checks after movement

Core only knows the interfaces in `api`. The other modules register their implementations with `provides` in `module-info.java`.

## Build and test

Requirements: JDK 21 and Maven.

```text
mvn clean verify
```

This also creates the plugin JAR files and runs two simple smoke tests.

## Run the game

On Windows:

```text
java --module-path "core/target/classes;api/target/classes" -m dk.sdu.cbse.core/dk.sdu.cbse.core.Main
```

Controls: Left/Right turns, Up moves forward, Space shoots, R reloads plugins and Esc closes the game.

## Try dynamic loading

1. Build and start the game.
2. Move `plugins/dk.sdu.cbse.player.jar` out of the folder.
3. Press R. The player disappears.
4. Move the same JAR back and press R again.

The game does not need to be rebuilt during these steps. Core creates a new `ModuleLayer` from the JAR files that are currently in the folder.
