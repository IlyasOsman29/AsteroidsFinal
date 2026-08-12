package dk.sdu.cbse.asteroids;
import dk.sdu.cbse.api.*; import java.util.Random;
public final class AsteroidPlugin implements IGamePluginService, IEntityProcessingService {
 public String name(){return "Asteroids";}
 public void start(GameData d){if(d.entities().stream().anyMatch(e->e.getType().equals("ASTEROID")))return;Random rnd=new Random(7);for(int i=0;i<6;i++){Entity a=new Entity("ASTEROID",50+rnd.nextInt(800),50+rnd.nextInt(500),18+rnd.nextInt(18));a.setDx(-35+rnd.nextDouble()*70);a.setDy(-35+rnd.nextDouble()*70);d.entities().add(a);}}
 public void stop(GameData d){d.entities().removeIf(e->e.getType().equals("ASTEROID"));}
 public void process(GameData d,double dt){for(Entity a:d.entities().stream().filter(e->e.getType().equals("ASTEROID")).toList()){a.setX(a.getX()+a.getDx()*dt);a.setY(a.getY()+a.getDy()*dt);d.wrap(a);}}
}