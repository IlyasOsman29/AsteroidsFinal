Remove-Item -Recurse -Force build,plugins -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force build/api,build/core,plugins | Out-Null
$api = Get-ChildItem -Recurse api/src -Filter *.java | ForEach-Object FullName
javac -d build/api $api
$core = Get-ChildItem -Recurse core/src -Filter *.java | ForEach-Object FullName
javac --module-path build/api -d build/core $core
foreach($p in @('player','enemy','asteroids','weapon','collision')) {
  New-Item -ItemType Directory -Force "build/$p" | Out-Null
  $src = Get-ChildItem -Recurse "$p/src" -Filter *.java | ForEach-Object FullName
  javac --module-path build/api -d "build/$p" $src
  jar --create --file "plugins/dk.sdu.cbse.$p.jar" -C "build/$p" .
}
Write-Host 'Build complete. Run:'
Write-Host 'java --module-path build/core;build/api -m dk.sdu.cbse.core/dk.sdu.cbse.core.Main'
