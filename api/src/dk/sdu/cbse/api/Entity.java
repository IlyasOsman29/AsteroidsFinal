package dk.sdu.cbse.api;

import java.util.UUID;

/** Generic mutable game object shared by independent plugins. */
public final class Entity {
    public static final String PLAYER = "PLAYER";
    public static final String ENEMY = "ENEMY";
    public static final String ASTEROID = "ASTEROID";
    public static final String BULLET = "BULLET";

    private final String id = UUID.randomUUID().toString();
    private final String type;
    private double x;
    private double y;
    private double dx;
    private double dy;
    private double rotation;
    private double radius;
    private double remainingLife = Double.POSITIVE_INFINITY;
    private int health = 1;
    private String owner = "";

    public Entity(String type, double x, double y, double radius) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public String getId() { return id; }
    public String getType() { return type; }
    public double getX() { return x; }
    public void setX(double value) { x = value; }
    public double getY() { return y; }
    public void setY(double value) { y = value; }
    public double getDx() { return dx; }
    public void setDx(double value) { dx = value; }
    public double getDy() { return dy; }
    public void setDy(double value) { dy = value; }
    public double getRotation() { return rotation; }
    public void setRotation(double value) { rotation = value; }
    public double getRadius() { return radius; }
    public void setRadius(double value) { radius = value; }
    public double getRemainingLife() { return remainingLife; }
    public void setRemainingLife(double value) { remainingLife = value; }
    public int getHealth() { return health; }
    public void setHealth(int value) { health = value; }
    public String getOwner() { return owner; }
    public void setOwner(String value) { owner = value; }
}
