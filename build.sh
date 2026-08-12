#!/usr/bin/env bash
set -e
rm -rf build plugins
mkdir -p build/api build/core plugins build/tmp
javac -d build/api $(find api/src -name '*.java')
javac --module-path build/api -d build/core $(find core/src -name '*.java')
for p in player enemy asteroids weapon collision; do
  mkdir -p build/$p
  javac --module-path build/api -d build/$p $(find $p/src -name '*.java')
  jar --create --file plugins/dk.sdu.cbse.$p.jar -C build/$p .
done
echo "Build complete. Plugin JARs are in plugins/."
echo "Run: java --module-path build/core:build/api -m dk.sdu.cbse.core/dk.sdu.cbse.core.Main"
