package dk.sdu.cbse.api;

import java.util.*;

public final class GameData {
    private final List<Entity> entities = new ArrayList<>();
    private final Set<String> keys = new HashSet<>();
    private int width = 900, height = 650;

    public List<Entity> entities(){ return entities; }
    public boolean isDown(String key){ return keys.contains(key); }
    public void setKey(String key, boolean down){ if(down) keys.add(key); else keys.remove(key); }
    public int width(){ return width; } public int height(){ return height; }
    public void setSize(int w, int h){ width=w; height=h; }
    public void wrap(Entity e){
        if(e.getX()<0)e.setX(width); if(e.getX()>width)e.setX(0);
        if(e.getY()<0)e.setY(height); if(e.getY()>height)e.setY(0);
    }
}
