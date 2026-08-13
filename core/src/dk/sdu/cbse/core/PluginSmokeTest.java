package dk.sdu.cbse.core;

import dk.sdu.cbse.api.Entity;
import dk.sdu.cbse.api.GameData;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.Arrays;

/** Integration smoke test for build-once, remove, reload, restore and reload. */
public final class PluginSmokeTest {
    private PluginSmokeTest() { }

    public static void main(String[] args) throws Exception {
        GameData data = new GameData();
        PluginManager manager = new PluginManager(data);
        Path plugins = Path.of("plugins");
        Path playerJar = plugins.resolve("dk.sdu.cbse.player.jar");
        Path disabledDirectory = Path.of("build", "disabled-for-smoke-test");
        Path disabledJar = disabledDirectory.resolve(playerJar.getFileName());
        Files.createDirectories(disabledDirectory);

        byte[] originalHash = sha256(playerJar);
        long originalModifiedTime = Files.getLastModifiedTime(playerJar).toMillis();
        manager.reload(plugins);
        require(hasPlayer(data), "Player was not loaded initially");

        Files.move(playerJar, disabledJar, StandardCopyOption.REPLACE_EXISTING);
        try {
            manager.reload(plugins);
            require(!hasPlayer(data), "Player remained after its JAR was removed");
        } finally {
            if (Files.exists(disabledJar)) {
                Files.move(disabledJar, playerJar, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        manager.reload(plugins);
        require(hasPlayer(data), "Player did not return after the same JAR was restored");
        require(Arrays.equals(originalHash, sha256(playerJar)), "Restored Player JAR content changed");
        require(originalModifiedTime == Files.getLastModifiedTime(playerJar).toMillis(),
                "Restored Player JAR timestamp changed");
        System.out.println("PASS: same compiled component removed and restored without recompilation");
    }

    private static boolean hasPlayer(GameData data) {
        return data.entities().stream().anyMatch(entity -> entity.getType().equals(Entity.PLAYER));
    }

    private static byte[] sha256(Path path) throws Exception {
        return MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(path));
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
