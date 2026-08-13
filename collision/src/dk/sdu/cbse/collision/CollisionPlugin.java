package dk.sdu.cbse.collision;

import dk.sdu.cbse.api.Entity;
import dk.sdu.cbse.api.GameData;
import dk.sdu.cbse.api.IGamePluginService;
import dk.sdu.cbse.api.IPostEntityProcessingService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CollisionPlugin implements IGamePluginService, IPostEntityProcessingService {
    private static final double MIN_ASTEROID_RADIUS = 8;

    @Override public String name() { return "Collision"; }
    @Override public void start(GameData data) { }
    @Override public void stop(GameData data) { }

    @Override
    public void process(GameData data, double deltaSeconds) {
        List<Entity> snapshot = new ArrayList<>(data.entities());
        Set<Entity> removed = new HashSet<>();
        List<Entity> created = new ArrayList<>();

        for (Entity bullet : snapshot.stream().filter(e -> e.getType().equals(Entity.BULLET)).toList()) {
            if (removed.contains(bullet)) continue;
            for (Entity target : snapshot) {
                if (target == bullet || removed.contains(target) || !isTarget(bullet, target)) continue;
                if (!collides(bullet, target)) continue;
                removed.add(bullet);
                if (target.getType().equals(Entity.ASTEROID)) {
                    removed.add(target);
                    split(target, created);
                } else {
                    target.setHealth(target.getHealth() - 1);
                    if (target.getHealth() <= 0) removed.add(target);
                }
                break;
            }
        }

        List<Entity> ships = snapshot.stream().filter(e -> e.getType().equals(Entity.PLAYER)
                || e.getType().equals(Entity.ENEMY)).toList();
        List<Entity> asteroids = snapshot.stream().filter(e -> e.getType().equals(Entity.ASTEROID)).toList();
        for (Entity ship : ships) {
            for (Entity asteroid : asteroids) {
                if (!removed.contains(ship) && !removed.contains(asteroid) && collides(ship, asteroid)) {
                    removed.add(ship);
                }
            }
        }

        data.entities().removeAll(removed);
        data.entities().addAll(created);
    }

    private boolean isTarget(Entity bullet, Entity target) {
        if (target.getType().equals(Entity.BULLET)) return false;
        if (target.getType().equals(Entity.ASTEROID)) return true;
        return (target.getType().equals(Entity.PLAYER) || target.getType().equals(Entity.ENEMY))
                && !target.getType().equals(bullet.getOwner());
    }

    private void split(Entity asteroid, List<Entity> created) {
        double radius = asteroid.getRadius() / 2.0;
        if (radius < MIN_ASTEROID_RADIUS) return;
        double speed = Math.max(35, Math.hypot(asteroid.getDx(), asteroid.getDy()));
        for (int direction : new int[]{-1, 1}) {
            Entity child = new Entity(Entity.ASTEROID, asteroid.getX(), asteroid.getY(), radius);
            child.setDx(-asteroid.getDy() * direction + speed * 0.25 * direction);
            child.setDy(asteroid.getDx() * direction - speed * 0.25);
            created.add(child);
        }
    }

    private boolean collides(Entity first, Entity second) {
        double dx = first.getX() - second.getX();
        double dy = first.getY() - second.getY();
        double radius = first.getRadius() + second.getRadius();
        return dx * dx + dy * dy <= radius * radius;
    }
}
