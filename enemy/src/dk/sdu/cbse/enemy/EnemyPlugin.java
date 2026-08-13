package dk.sdu.cbse.enemy;

import dk.sdu.cbse.api.Entity;
import dk.sdu.cbse.api.GameData;
import dk.sdu.cbse.api.IEntityProcessingService;
import dk.sdu.cbse.api.IGamePluginService;

public final class EnemyPlugin implements IGamePluginService, IEntityProcessingService {
    @Override public String name() { return "Enemy"; }

    @Override
    public void start(GameData data) {
        if (data.firstEntity(Entity.ENEMY) == null) {
            Entity enemy = new Entity(Entity.ENEMY, 100, 100, 16);
            enemy.setHealth(3);
            data.entities().add(enemy);
        }
    }

    @Override public void stop(GameData data) {
        data.entities().removeIf(entity -> entity.getType().equals(Entity.ENEMY));
    }

    @Override
    public void process(GameData data, double deltaSeconds) {
        Entity player = data.firstEntity(Entity.PLAYER);
        if (player == null) return;
        for (Entity enemy : data.entitiesOfType(Entity.ENEMY)) {
            double angle = Math.atan2(player.getY() - enemy.getY(), player.getX() - enemy.getX());
            enemy.setRotation(Math.toDegrees(angle));
            enemy.setX(enemy.getX() + Math.cos(angle) * 45 * deltaSeconds);
            enemy.setY(enemy.getY() + Math.sin(angle) * 45 * deltaSeconds);
            data.wrap(enemy);
        }
    }
}
