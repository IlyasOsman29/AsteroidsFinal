package dk.sdu.cbse.core;

import dk.sdu.cbse.api.GameData;
import dk.sdu.cbse.api.IEntityProcessingService;
import dk.sdu.cbse.api.IGamePluginService;
import dk.sdu.cbse.api.IPostEntityProcessingService;
import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

/** Discovers a fresh set of JPMS service providers from the current plugin directory. */
final class PluginManager {
    private final GameData data;
    private List<IGamePluginService> plugins = List.of();
    private List<IEntityProcessingService> processors = List.of();
    private List<IPostEntityProcessingService> postProcessors = List.of();
    private ModuleLayer layer;

    PluginManager(GameData data) { this.data = data; }

    void reload(Path pluginDirectory) {
        stopCurrentPlugins();
        plugins = List.of();
        processors = List.of();
        postProcessors = List.of();
        layer = null;

        try {
            Files.createDirectories(pluginDirectory);
            List<Path> installedJars;
            try (var files = Files.list(pluginDirectory)) {
                installedJars = files.filter(path -> path.getFileName().toString().endsWith(".jar"))
                        .sorted().toList();
            }
            if (installedJars.isEmpty()) {
                System.out.println("No plugins installed.");
                return;
            }

            // Module readers keep JARs open on Windows. Loading private copies leaves the user-facing
            // plugin JARs movable while the game remains alive.
            List<Path> layerJars = makeLayerSnapshot(installedJars);
            ModuleFinder finder = ModuleFinder.of(layerJars.toArray(Path[]::new));
            Set<String> roots = new HashSet<>();
            finder.findAll().forEach(reference -> roots.add(reference.descriptor().name()));
            Configuration configuration = ModuleLayer.boot().configuration()
                    .resolve(finder, ModuleFinder.of(), roots);
            layer = ModuleLayer.boot().defineModulesWithOneLoader(
                    configuration, ClassLoader.getSystemClassLoader());

            plugins = ServiceLoader.load(layer, IGamePluginService.class).stream()
                    .map(ServiceLoader.Provider::get)
                    .sorted(Comparator.comparing(IGamePluginService::name)).toList();
            processors = ServiceLoader.load(layer, IEntityProcessingService.class).stream()
                    .map(ServiceLoader.Provider::get)
                    .sorted(Comparator.comparing(service -> service.getClass().getModule().getName())).toList();
            postProcessors = ServiceLoader.load(layer, IPostEntityProcessingService.class).stream()
                    .map(ServiceLoader.Provider::get).toList();
            plugins.forEach(plugin -> {
                plugin.start(data);
                System.out.println("Loaded: " + plugin.name());
            });
        } catch (Exception exception) {
            System.err.println("Plugin reload failed: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private List<Path> makeLayerSnapshot(List<Path> installedJars) throws IOException {
        Path snapshot = Files.createTempDirectory("asteroids-plugin-layer-");
        snapshot.toFile().deleteOnExit();
        List<Path> copies = new ArrayList<>();
        for (Path installed : installedJars) {
            Path copy = snapshot.resolve(installed.getFileName());
            Files.copy(installed, copy, StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.COPY_ATTRIBUTES);
            copy.toFile().deleteOnExit();
            copies.add(copy);
        }
        return copies;
    }

    private void stopCurrentPlugins() {
        for (IGamePluginService plugin : plugins) {
            try {
                plugin.stop(data);
            } catch (RuntimeException exception) {
                System.err.println("Could not stop " + plugin.name() + ": " + exception.getMessage());
            }
        }
    }

    void process(double deltaSeconds) {
        processors.forEach(processor -> processor.process(data, deltaSeconds));
        postProcessors.forEach(processor -> processor.process(data, deltaSeconds));
    }

    List<String> componentNames() { return plugins.stream().map(IGamePluginService::name).toList(); }
}
