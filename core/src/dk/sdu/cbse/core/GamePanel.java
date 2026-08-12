package dk.sdu.cbse.core;

import dk.sdu.cbse.api.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Path;

final class GamePanel extends JPanel implements ActionListener, KeyListener {
    private final GameData data = new GameData();
    private final PluginManager plugins = new PluginManager(data);
    private long previous = System.nanoTime();

    GamePanel(){
        setBackground(Color.BLACK); setFocusable(true); addKeyListener(this);
        new Timer(16, this).start();
        SwingUtilities.invokeLater(() -> { requestFocusInWindow(); plugins.reload(Path.of("plugins")); });
    }
    @Override public void actionPerformed(ActionEvent e){
        long now=System.nanoTime(); double dt=Math.min((now-previous)/1_000_000_000.0,0.05); previous=now;
        data.setSize(Math.max(getWidth(),1),Math.max(getHeight(),1));
        plugins.process(dt); repaint();
    }
    @Override protected void paintComponent(Graphics g){
        super.paintComponent(g); Graphics2D gg=(Graphics2D)g; gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        for(Entity e:data.entities()) draw(gg,e);
        gg.setColor(Color.LIGHT_GRAY); gg.drawString("Arrows=move  Space=fire  R=reload plugins",12,20);
    }
    private void draw(Graphics2D g, Entity e){
        int x=(int)e.getX(), y=(int)e.getY(), r=(int)e.getRadius();
        switch(e.getType()){
            case "PLAYER" -> { g.setColor(Color.CYAN); polygon(g,x,y,r,e.getRotation()); }
            case "ENEMY" -> { g.setColor(Color.RED); polygon(g,x,y,r,e.getRotation()); }
            case "ASTEROID" -> { g.setColor(Color.GRAY); g.drawOval(x-r,y-r,r*2,r*2); }
            case "BULLET" -> { g.setColor(Color.YELLOW); g.fillOval(x-3,y-3,6,6); }
            default -> { g.setColor(Color.WHITE); g.drawOval(x-r,y-r,r*2,r*2); }
        }
    }
    private void polygon(Graphics2D g,int x,int y,int r,double rot){
        double a=Math.toRadians(rot); int[] xs={x+(int)(Math.cos(a)*r),x+(int)(Math.cos(a+2.5)*r),x+(int)(Math.cos(a-2.5)*r)};
        int[] ys={y+(int)(Math.sin(a)*r),y+(int)(Math.sin(a+2.5)*r),y+(int)(Math.sin(a-2.5)*r)}; g.drawPolygon(xs,ys,3);
    }
    @Override public void keyPressed(KeyEvent e){ key(e,true); if(e.getKeyCode()==KeyEvent.VK_R)plugins.reload(Path.of("plugins")); if(e.getKeyCode()==KeyEvent.VK_ESCAPE)System.exit(0); }
    @Override public void keyReleased(KeyEvent e){ key(e,false); }
    private void key(KeyEvent e,boolean down){ switch(e.getKeyCode()){case KeyEvent.VK_LEFT->data.setKey("LEFT",down);case KeyEvent.VK_RIGHT->data.setKey("RIGHT",down);case KeyEvent.VK_UP->data.setKey("UP",down);case KeyEvent.VK_SPACE->data.setKey("FIRE",down);} }
    @Override public void keyTyped(KeyEvent e){}
}
