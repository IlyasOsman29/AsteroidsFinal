package dk.sdu.cbse.collision;
import dk.sdu.cbse.api.*;
import java.util.*;
public final class CollisionPlugin implements IGamePluginService, IPostEntityProcessingService {
    public String name(){return "Collision";} public void start(GameData d){} public void stop(GameData d){}
    public void process(GameData d,double dt){
        List<Entity> copy=new ArrayList<>(d.entities()); Set<Entity> remove=new HashSet<>();
        for(int i=0;i<copy.size();i++) for(int j=i+1;j<copy.size();j++){
            Entity a=copy.get(i),b=copy.get(j); double dx=a.getX()-b.getX(),dy=a.getY()-b.getY();
            if(Math.sqrt(dx*dx+dy*dy)>a.getRadius()+b.getRadius())continue;
            if(a.getType().equals("BULLET")&&isTarget(a,b)){remove.add(a);hit(b,remove);} else if(b.getType().equals("BULLET")&&isTarget(b,a)){remove.add(b);hit(a,remove);}
            if((a.getType().equals("PLAYER")||a.getType().equals("ENEMY"))&&b.getType().equals("ASTEROID"))remove.add(a);
            if((b.getType().equals("PLAYER")||b.getType().equals("ENEMY"))&&a.getType().equals("ASTEROID"))remove.add(b);
        }
        d.entities().removeAll(remove);
    }
    private boolean isTarget(Entity bullet,Entity target){return !target.getType().equals("BULLET")&&!target.getType().equals(bullet.getOwner());}
    private void hit(Entity e,Set<Entity> remove){e.setHealth(e.getHealth()-1);if(e.getType().equals("ASTEROID")||e.getHealth()<=0)remove.add(e);}
}
