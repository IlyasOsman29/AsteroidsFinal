package dk.sdu.cbse.enemy;
import dk.sdu.cbse.api.*;
public final class EnemyPlugin implements IGamePluginService, IEntityProcessingService {
 public String name(){return "Enemy";}
 public void start(GameData d){if(d.entities().stream().noneMatch(e->e.getType().equals("ENEMY"))){Entity e=new Entity("ENEMY",100,100,16);e.setHealth(3);d.entities().add(e);}}
 public void stop(GameData d){d.entities().removeIf(e->e.getType().equals("ENEMY"));}
 public void process(GameData d,double dt){Entity p=d.entities().stream().filter(x->x.getType().equals("PLAYER")).findFirst().orElse(null);for(Entity e:d.entities().stream().filter(x->x.getType().equals("ENEMY")).toList()){if(p!=null){double a=Math.atan2(p.getY()-e.getY(),p.getX()-e.getX());e.setRotation(Math.toDegrees(a));e.setX(e.getX()+Math.cos(a)*45*dt);e.setY(e.getY()+Math.sin(a)*45*dt);}d.wrap(e);}}
}