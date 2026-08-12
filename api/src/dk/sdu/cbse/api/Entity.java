package dk.sdu.cbse.api;

import java.util.UUID;

public final class Entity {
    private final String id = UUID.randomUUID().toString();
    private String type;
    private double x, y, dx, dy, rotation, radius;
    private int health = 1;
    private String owner = "";

    public Entity(String type, double x, double y, double radius) {
        this.type = type; this.x = x; this.y = y; this.radius = radius;
    }
    public String getId(){ return id; }
    public String getType(){ return type; }
    public double getX(){ return x; } public void setX(double v){ x=v; }
    public double getY(){ return y; } public void setY(double v){ y=v; }
    public double getDx(){ return dx; } public void setDx(double v){ dx=v; }
    public double getDy(){ return dy; } public void setDy(double v){ dy=v; }
    public double getRotation(){ return rotation; } public void setRotation(double v){ rotation=v; }
    public double getRadius(){ return radius; } public void setRadius(double v){ radius=v; }
    public int getHealth(){ return health; } public void setHealth(int v){ health=v; }
    public String getOwner(){ return owner; } public void setOwner(String v){ owner=v; }
}
