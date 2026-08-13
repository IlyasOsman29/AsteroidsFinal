#!/usr/bin/env bash
set -euo pipefail
rm -rf build plugins
mkdir -p build/api build/core plugins
javac -d build/api $(find api/src -name '*.java' | sort)
javac --module-path build/api -d build/core $(find core/src -name '*.java' | sort)
for plugin in player enemy asteroids weapon collision; do
  mkdir -p "build/$plugin"
  javac --module-path build/api -d "build/$plugin" $(find "$plugin/src" -name '*.java' | sort)
  jar --create --file "plugins/dk.sdu.cbse.$plugin.jar" -C "build/$plugin" .
done
echo "Build complete. Compiled plugin JARs are in plugins/."
echo "Run: java --module-path build/core:build/api -m dk.sdu.cbse.core/dk.sdu.cbse.core.Main"
