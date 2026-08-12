package dk.sdu.cbse.core;

import dk.sdu.cbse.api.*;
import java.lang.module.*;
import java.nio.file.*;
import java.util.*;

final class PluginManager {
    private final GameData data;
    private List<IGamePluginService> plugins = List.of();
    private List<IEntityProcessingService> processors = List.of();
    private List<IPostEntityProcessingService> postProcessors = List.of();
    private ModuleLayer layer;

    PluginManager(GameData data){ this.data=data; }

    void reload(Path pluginDir) {
        for (IGamePluginService p : plugins) {
            try { p.stop(data); } catch (Exception ignored) {}
        }
        plugins = List.of(); processors = List.of(); postProcessors = List.of(); layer = null;
        try {
            Files.createDirectories(pluginDir);
            List<Path> jars;
            try (var stream = Files.list(pluginDir)) {
                jars = stream.filter(p -> p.toString().endsWith(".jar")).toList();
            }
            if (jars.isEmpty()) { System.out.println("No plugins installed."); return; }
            ModuleFinder finder = ModuleFinder.of(jars.toArray(Path[]::new));
            Set<String> roots = new HashSet<>();
            finder.findAll().forEach(r -> roots.add(r.descriptor().name()));
            Configuration config = ModuleLayer.boot().configuration()
                    .resolve(finder, ModuleFinder.of(), roots);
            layer = ModuleLayer.boot().defineModulesWithOneLoader(config, ClassLoader.getSystemClassLoader());
            plugins = ServiceLoader.load(layer, IGamePluginService.class).stream().map(ServiceLoader.Provider::get).toList();
            processors = ServiceLoader.load(layer, IEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).toList();
            postProcessors = ServiceLoader.load(layer, IPostEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).toList();
            plugins.forEach(p -> { p.start(data); System.out.println("Loaded: " + p.name()); });
        } catch (Exception e) {
            System.err.println("Plugin reload failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    void process(double dt){ processors.forEach(p -> p.process(data, dt)); postProcessors.forEach(p -> p.process(data, dt)); }
}
