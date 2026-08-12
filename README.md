# AsteroidsFinal

A small component-based Asteroids-style game for the SB-KOM exam.

## What it demonstrates
- Java Platform Module System (JPMS)
- `ServiceLoader` service interfaces
- Runtime plugin discovery through a new `ModuleLayer`
- Player, Enemy, Asteroid and Weapon/Bullet components
- Collision handling as a post-processing component
- Components can be installed/removed as JAR files without recompiling source code

## Requirements
- JDK 21 (JDK 17+ should also work)

## Build
Linux/macOS/Git Bash:
```bash
./build.sh
```
Windows PowerShell:
```powershell
./build.ps1
```

## Run
```bash
java --module-path build/core:build/api -m dk.sdu.cbse.core/dk.sdu.cbse.core.Main
```
On Windows replace `:` with `;` if running the command manually.

## Controls
- Arrow keys: rotate / thrust
- Space: fire
- R: reload the `plugins/` folder
- Esc: close

## Dynamic component demo
1. Start the game with the Player JAR in `plugins/`.
2. While the game is running, move `dk.sdu.cbse.player.jar` out of `plugins/`.
3. Press `R`. The player disappears. No source is compiled.
4. Move the JAR back into `plugins/`.
5. Press `R`. The player component is loaded again.

The same idea can be shown with the Enemy, Asteroids or Weapon component.
