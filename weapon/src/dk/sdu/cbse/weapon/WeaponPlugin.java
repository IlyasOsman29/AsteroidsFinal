package dk.sdu.cbse.weapon;

import dk.sdu.cbse.api.Entity;
import dk.sdu.cbse.api.GameData;
import dk.sdu.cbse.api.IEntityProcessingService;
import dk.sdu.cbse.api.IGamePluginService;

public final class WeaponPlugin implements IGamePluginService, IEntityProcessingService {
    private double playerCooldown;
    private double enemyCooldown;

    @Override public String name() { return "Weapon/Bullet"; }
    @Override public void start(GameData data) { }

    @Override
    public void stop(GameData data) {
        data.entities().removeIf(entity -> entity.getType().equals(Entity.BULLET));
    }

    @Override
    public void process(GameData data, double deltaSeconds) {
        playerCooldown = Math.max(0, playerCooldown - deltaSeconds);
        enemyCooldown = Math.max(0, enemyCooldown - deltaSeconds);
        Entity player = data.firstEntity(Entity.PLAYER);
        Entity enemy = data.firstEntity(Entity.ENEMY);

        if (player != null && data.isDown("FIRE") && playerCooldown == 0) {
            fire(data, player, player.getRotation(), Entity.PLAYER);
            playerCooldown = 0.25;
        }
        if (player != null && enemy != null && enemyCooldown == 0) {
            double angle = Math.toDegrees(Math.atan2(player.getY() - enemy.getY(), player.getX() - enemy.getX()));
            fire(data, enemy, angle, Entity.ENEMY);
            enemyCooldown = 1.0;
        }

        for (Entity bullet : data.entitiesOfType(Entity.BULLET)) {
            bullet.setX(bullet.getX() + bullet.getDx() * deltaSeconds);
            bullet.setY(bullet.getY() + bullet.getDy() * deltaSeconds);
            bullet.setRemainingLife(bullet.getRemainingLife() - deltaSeconds);
            if (bullet.getRemainingLife() <= 0 || bullet.getX() < 0 || bullet.getY() < 0
                    || bullet.getX() > data.width() || bullet.getY() > data.height()) {
                data.entities().remove(bullet);
            }
        }
    }

    private void fire(GameData data, Entity ship, double rotation, String owner) {
        double angle = Math.toRadians(rotation);
        Entity bullet = new Entity(Entity.BULLET, ship.getX(), ship.getY(), 3);
        bullet.setOwner(owner);
        bullet.setDx(Math.cos(angle) * 300);
        bullet.setDy(Math.sin(angle) * 300);
        bullet.setRemainingLife(2.5);
        data.entities().add(bullet);
    }
}
