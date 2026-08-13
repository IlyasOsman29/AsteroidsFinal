$ErrorActionPreference = "Stop"
$project = Split-Path -Parent $MyInvocation.MyCommand.Path
Push-Location $project
try {
    foreach ($directory in @("build", "plugins")) {
        $path = Join-Path $project $directory
        if (Test-Path -LiteralPath $path) { Remove-Item -LiteralPath $path -Recurse -Force }
    }
    New-Item -ItemType Directory -Force build\api, build\core, plugins | Out-Null

    $apiSources = Get-ChildItem -LiteralPath api\src -Recurse -Filter *.java | Sort-Object FullName | ForEach-Object FullName
    & javac -d build\api $apiSources
    if ($LASTEXITCODE -ne 0) { throw "API compilation failed" }

    $coreSources = Get-ChildItem -LiteralPath core\src -Recurse -Filter *.java | Sort-Object FullName | ForEach-Object FullName
    & javac --module-path build\api -d build\core $coreSources
    if ($LASTEXITCODE -ne 0) { throw "Core compilation failed" }

    foreach ($plugin in @("player", "enemy", "asteroids", "weapon", "collision")) {
        New-Item -ItemType Directory -Force "build\$plugin" | Out-Null
        $sources = Get-ChildItem -LiteralPath "$plugin\src" -Recurse -Filter *.java | Sort-Object FullName | ForEach-Object FullName
        & javac --module-path build\api -d "build\$plugin" $sources
        if ($LASTEXITCODE -ne 0) { throw "$plugin compilation failed" }
        & jar --create --file "plugins\dk.sdu.cbse.$plugin.jar" -C "build\$plugin" .
        if ($LASTEXITCODE -ne 0) { throw "$plugin JAR creation failed" }
    }
    Write-Host "Build complete. Compiled plugin JARs are in plugins/."
    Write-Host "Run: java --module-path 'build\core;build\api' -m dk.sdu.cbse.core/dk.sdu.cbse.core.Main"
} finally {
    Pop-Location
}
