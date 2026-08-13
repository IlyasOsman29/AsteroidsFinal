package dk.sdu.cbse.asteroids;

import dk.sdu.cbse.api.Entity;
import dk.sdu.cbse.api.GameData;
import dk.sdu.cbse.api.IEntityProcessingService;
import dk.sdu.cbse.api.IGamePluginService;
import java.util.Random;

public final class AsteroidPlugin implements IGamePluginService, IEntityProcessingService {
    @Override public String name() { return "Asteroids"; }

    @Override
    public void start(GameData data) {
        if (!data.entitiesOfType(Entity.ASTEROID).isEmpty()) return;
        Random random = new Random(7);
        for (int i = 0; i < 6; i++) {
            Entity asteroid = new Entity(Entity.ASTEROID, 50 + random.nextInt(800),
                    50 + random.nextInt(500), 24 + random.nextInt(13));
            asteroid.setDx(-35 + random.nextDouble() * 70);
            asteroid.setDy(-35 + random.nextDouble() * 70);
            data.entities().add(asteroid);
        }
    }

    @Override public void stop(GameData data) {
        data.entities().removeIf(entity -> entity.getType().equals(Entity.ASTEROID));
    }

    @Override
    public void process(GameData data, double deltaSeconds) {
        for (Entity asteroid : data.entitiesOfType(Entity.ASTEROID)) {
            asteroid.setX(asteroid.getX() + asteroid.getDx() * deltaSeconds);
            asteroid.setY(asteroid.getY() + asteroid.getDy() * deltaSeconds);
            data.wrap(asteroid);
        }
    }
}
