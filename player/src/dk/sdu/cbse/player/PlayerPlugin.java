package dk.sdu.cbse.player;

import dk.sdu.cbse.api.Entity;
import dk.sdu.cbse.api.GameData;
import dk.sdu.cbse.api.IEntityProcessingService;
import dk.sdu.cbse.api.IGamePluginService;

public final class PlayerPlugin implements IGamePluginService, IEntityProcessingService {
    @Override public String name() { return "Player"; }

    @Override
    public void start(GameData data) {
        if (data.firstEntity(Entity.PLAYER) == null) {
            Entity player = new Entity(Entity.PLAYER, data.width() / 2.0, data.height() / 2.0, 16);
            player.setHealth(3);
            data.entities().add(player);
        }
    }

    @Override public void stop(GameData data) {
        data.entities().removeIf(entity -> entity.getType().equals(Entity.PLAYER));
    }

    @Override
    public void process(GameData data, double deltaSeconds) {
        Entity player = data.firstEntity(Entity.PLAYER);
        if (player == null) return;
        if (data.isDown("LEFT")) player.setRotation(player.getRotation() - 180 * deltaSeconds);
        if (data.isDown("RIGHT")) player.setRotation(player.getRotation() + 180 * deltaSeconds);
        if (data.isDown("UP")) {
            double angle = Math.toRadians(player.getRotation());
            player.setDx(player.getDx() + Math.cos(angle) * 90 * deltaSeconds);
            player.setDy(player.getDy() + Math.sin(angle) * 90 * deltaSeconds);
        }
        player.setX(player.getX() + player.getDx() * deltaSeconds);
        player.setY(player.getY() + player.getDy() * deltaSeconds);
        player.setDx(player.getDx() * 0.99);
        player.setDy(player.getDy() * 0.99);
        data.wrap(player);
    }
}
