package dk.sdu.cbse.weapon;
import dk.sdu.cbse.api.*;
public final class WeaponPlugin implements IGamePluginService, IEntityProcessingService {
 private static long lastShot;
 public String name(){return "Weapon/Bullet";} public void start(GameData d){} public void stop(GameData d){d.entities().removeIf(e->e.getType().equals("BULLET"));}
 public void process(GameData d,double dt){long now=System.nanoTime();Entity p=d.entities().stream().filter(x->x.getType().equals("PLAYER")).findFirst().orElse(null);if(p!=null&&d.isDown("FIRE")&&(now-lastShot)>250_000_000L){double a=Math.toRadians(p.getRotation());Entity b=new Entity("BULLET",p.getX(),p.getY(),3);b.setOwner("PLAYER");b.setDx(Math.cos(a)*300);b.setDy(Math.sin(a)*300);d.entities().add(b);lastShot=now;}for(Entity b:d.entities().stream().filter(e->e.getType().equals("BULLET")).toList()){b.setX(b.getX()+b.getDx()*dt);b.setY(b.getY()+b.getDy()*dt);if(b.getX()<0||b.getY()<0||b.getX()>d.width()||b.getY()>d.height())d.entities().remove(b);}}
}