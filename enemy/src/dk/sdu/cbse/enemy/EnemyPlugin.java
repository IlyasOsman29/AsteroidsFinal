package dk.sdu.cbse.enemy;

import dk.sdu.cbse.api.Entity;
import dk.sdu.cbse.api.GameData;
import dk.sdu.cbse.api.IEntityProcessingService;
import dk.sdu.cbse.api.IGamePluginService;

public final class EnemyPlugin implements IGamePluginService, IEntityProcessingService {
    private static final double ENEMY_RADIUS = 16;
    private static final double SPAWN_MARGIN = 80;

    @Override public String name() { return "Enemy"; }

    @Override
    public void start(GameData data) {
        if (data.firstEntity(Entity.ENEMY) == null) {
            double[] spawn = safestCorner(data);
            Entity enemy = new Entity(Entity.ENEMY, spawn[0], spawn[1], ENEMY_RADIUS);
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

    /** Chooses the corner whose route towards Player has most asteroid clearance. */
    private double[] safestCorner(GameData data) {
        double right = Math.max(SPAWN_MARGIN, data.width() - SPAWN_MARGIN);
        double bottom = Math.max(SPAWN_MARGIN, data.height() - SPAWN_MARGIN);
        double[][] candidates = {
                {SPAWN_MARGIN, SPAWN_MARGIN}, {right, SPAWN_MARGIN},
                {SPAWN_MARGIN, bottom}, {right, bottom}
        };
        Entity player = data.firstEntity(Entity.PLAYER);
        double targetX = player == null ? data.width() / 2.0 : player.getX();
        double targetY = player == null ? data.height() / 2.0 : player.getY();

        double[] best = candidates[0];
        double bestClearance = Double.NEGATIVE_INFINITY;
        for (double[] candidate : candidates) {
            double clearance = data.entitiesOfType(Entity.ASTEROID).stream()
                    .mapToDouble(asteroid -> distanceToSegment(asteroid.getX(), asteroid.getY(),
                            candidate[0], candidate[1], targetX, targetY)
                            - asteroid.getRadius() - ENEMY_RADIUS)
                    .min().orElse(Double.POSITIVE_INFINITY);
            if (clearance > bestClearance) {
                best = candidate;
                bestClearance = clearance;
            }
        }
        return best;
    }

    private double distanceToSegment(double pointX, double pointY, double startX, double startY,
            double endX, double endY) {
        double segmentX = endX - startX;
        double segmentY = endY - startY;
        double lengthSquared = segmentX * segmentX + segmentY * segmentY;
        if (lengthSquared == 0) return Math.hypot(pointX - startX, pointY - startY);
        double projection = ((pointX - startX) * segmentX + (pointY - startY) * segmentY)
                / lengthSquared;
        double t = Math.max(0, Math.min(1, projection));
        double closestX = startX + t * segmentX;
        double closestY = startY + t * segmentY;
        return Math.hypot(pointX - closestX, pointY - closestY);
    }
}
