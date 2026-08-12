package dk.sdu.cbse.player;
import dk.sdu.cbse.api.*;
public final class PlayerPlugin implements IGamePluginService, IEntityProcessingService {
 public String name(){return "Player";}
 public void start(GameData d){ if(d.entities().stream().noneMatch(e->e.getType().equals("PLAYER"))){Entity p=new Entity("PLAYER",d.width()/2.0,d.height()/2.0,16);p.setHealth(3);d.entities().add(p);} }
 public void stop(GameData d){d.entities().removeIf(e->e.getType().equals("PLAYER"));}
 public void process(GameData d,double dt){Entity p=d.entities().stream().filter(e->e.getType().equals("PLAYER")).findFirst().orElse(null);if(p==null)return;if(d.isDown("LEFT"))p.setRotation(p.getRotation()-180*dt);if(d.isDown("RIGHT"))p.setRotation(p.getRotation()+180*dt);if(d.isDown("UP")){double a=Math.toRadians(p.getRotation());p.setDx(p.getDx()+Math.cos(a)*90*dt);p.setDy(p.getDy()+Math.sin(a)*90*dt);}p.setX(p.getX()+p.getDx()*dt);p.setY(p.getY()+p.getDy()*dt);p.setDx(p.getDx()*0.99);p.setDy(p.getDy()*0.99);d.wrap(p);}
}