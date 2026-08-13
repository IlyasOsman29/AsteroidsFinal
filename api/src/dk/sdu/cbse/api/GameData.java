package dk.sdu.cbse.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Shared game state used by Core and the plugins. */
public final class GameData {
    private final List<Entity> entities = new ArrayList<>();
    private final Set<String> keys = new HashSet<>();
    private int width = 900;
    private int height = 650;

    public List<Entity> entities() { return entities; }
    public List<Entity> entitiesOfType(String type) {
        return entities.stream().filter(entity -> entity.getType().equals(type)).toList();
    }
    public Entity firstEntity(String type) {
        return entities.stream().filter(entity -> entity.getType().equals(type)).findFirst().orElse(null);
    }
    public boolean isDown(String key) { return keys.contains(key); }
    public void setKey(String key, boolean down) { if (down) keys.add(key); else keys.remove(key); }
    public int width() { return width; }
    public int height() { return height; }
    public void setSize(int width, int height) { this.width = Math.max(1, width); this.height = Math.max(1, height); }

    public void wrap(Entity entity) {
        if (entity.getX() < 0) entity.setX(width);
        if (entity.getX() > width) entity.setX(0);
        if (entity.getY() < 0) entity.setY(height);
        if (entity.getY() > height) entity.setY(0);
    }
}
