package dk.sdu.cbse.core;

import dk.sdu.cbse.api.Entity;
import dk.sdu.cbse.api.GameData;
import java.nio.file.Path;

/** Runs the main game rules without opening a window. */
public final class GameplaySmokeTest {
    private GameplaySmokeTest() { }

    public static void main(String[] args) {
        GameData data = new GameData();
        PluginManager manager = new PluginManager(data);
        manager.reload(Path.of("plugins"));

        for (int frame = 0; frame < 80; frame++) {
            manager.process(0.05);
        }
        require(data.firstEntity(Entity.ENEMY) != null,
                "Enemy collided with an asteroid during the four-second opening");

        data.entities().clear();

        Entity asteroid = new Entity(Entity.ASTEROID, 100, 100, 32);
        asteroid.setDx(20);
        Entity bullet = bullet(Entity.PLAYER, 100, 100);
        data.entities().add(asteroid);
        data.entities().add(bullet);
        manager.process(0);
        require(data.entitiesOfType(Entity.ASTEROID).size() == 2,
                "Large asteroid did not split into two children");

        Entity player = new Entity(Entity.PLAYER, 300, 300, 16);
        player.setHealth(3);
        data.entities().add(player);
        for (int hit = 0; hit < 3; hit++) {
            data.entities().add(bullet(Entity.ENEMY, 300, 300));
            manager.process(0);
        }
        require(data.firstEntity(Entity.PLAYER) == null, "Three enemy bullets did not destroy Player");
        System.out.println("PASS: safe Enemy opening, asteroid splitting and three-hit ship damage");
    }

    private static Entity bullet(String owner, double x, double y) {
        Entity bullet = new Entity(Entity.BULLET, x, y, 3);
        bullet.setOwner(owner);
        bullet.setRemainingLife(2.5);
        return bullet;
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
