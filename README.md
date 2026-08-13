# AsteroidsFinal

AsteroidsFinal is a small Swing-rendered game focused on replaceable JPMS components. The stable `api` module contains only `Entity`, `GameData` and three service contracts. Core requires that API and declares `uses`; it has no source or module dependency on concrete plugins. Each gameplay JAR declares `provides ... with ...` and keeps its implementation package unexported.

Installed components:

- Player (three health, rotation and thrust)
- Enemy (three health, chooses the safest corner route at spawn, then follows Player)
- Asteroids (random movement and wrapping)
- Weapon/Bullet (Player input plus automatic Enemy fire)
- Collision (post-processing, ship damage, ship/asteroid destruction and recursive asteroid splitting)
- Core/Rendering (window, timing, input, drawing and runtime assembly)

## Build, test and run

Requirements: JDK 21 (compatible with JDK 17+).

```powershell
powershell -ExecutionPolicy Bypass -File .\build.ps1
java --module-path "build\core;build\api" -m dk.sdu.cbse.core/dk.sdu.cbse.core.PluginSmokeTest
java --module-path "build\core;build\api" -m dk.sdu.cbse.core/dk.sdu.cbse.core.GameplaySmokeTest
java --module-path "build\core;build\api" -m dk.sdu.cbse.core/dk.sdu.cbse.core.Main
```

Linux/macOS uses `./build.sh` and `:` instead of `;` in the module path.

Controls: Left/Right rotate, Up thrusts, Space fires, R reloads the installed JAR set, Esc closes. The second line in the game shows the active component names.

## Dynamic component demonstration

1. Build once and start the game.
2. Show `plugins/dk.sdu.cbse.player.jar`.
3. Move that compiled JAR into a separate `disabled` folder while the game stays open.
4. Press R. Core stops the old providers, builds a new `ModuleLayer` from the remaining JARs and Player disappears.
5. Move the exact same JAR back. Do not run the build script.
6. Press R. ServiceLoader discovers Player in the new layer and the ship returns.

On Windows, JPMS module readers normally lock JAR files. `PluginManager` therefore copies the current installed set to a private temporary snapshot before creating each layer. The layer reads the copies, leaving the user-facing files in `plugins/` movable. Reload creates a new layer; Java does not mutate or explicitly unload the old layer. After old services and the layer become unreachable, normal garbage collection can reclaim them.

`PluginSmokeTest` automates the same build-once scenario, restores the JAR in `finally`, and verifies that its SHA-256 content and timestamp are unchanged.

`GameplaySmokeTest` also simulates the first four seconds and requires Enemy to survive the opening asteroid field before checking asteroid splitting and three-hit ship damage.
