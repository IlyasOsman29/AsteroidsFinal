package dk.sdu.cbse.core;

import dk.sdu.cbse.api.Entity;
import dk.sdu.cbse.api.GameData;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.file.Path;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

final class GamePanel extends JPanel implements ActionListener, KeyListener {
    private final GameData data = new GameData();
    private final PluginManager plugins = new PluginManager(data);
    private long previous = System.nanoTime();
    private String status = "Loading plugins...";

    GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        new Timer(16, this).start();
        SwingUtilities.invokeLater(() -> {
            requestFocusInWindow();
            reloadPlugins();
        });
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        long now = System.nanoTime();
        double delta = Math.min((now - previous) / 1_000_000_000.0, 0.05);
        previous = now;
        data.setSize(getWidth(), getHeight());
        plugins.process(delta);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D painter = (Graphics2D) graphics;
        painter.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        data.entities().forEach(entity -> draw(painter, entity));
        painter.setColor(Color.LIGHT_GRAY);
        painter.drawString("Arrows=move  Space=fire  R=reload plugins  Esc=close", 12, 20);
        painter.drawString(status, 12, 40);
    }

    private void draw(Graphics2D graphics, Entity entity) {
        int x = (int) entity.getX();
        int y = (int) entity.getY();
        int radius = (int) entity.getRadius();
        switch (entity.getType()) {
            case Entity.PLAYER -> { graphics.setColor(Color.CYAN); polygon(graphics, x, y, radius, entity.getRotation()); }
            case Entity.ENEMY -> { graphics.setColor(Color.RED); polygon(graphics, x, y, radius, entity.getRotation()); }
            case Entity.ASTEROID -> { graphics.setColor(Color.GRAY); graphics.drawOval(x - radius, y - radius, radius * 2, radius * 2); }
            case Entity.BULLET -> {
                graphics.setColor(entity.getOwner().equals(Entity.ENEMY) ? Color.ORANGE : Color.YELLOW);
                graphics.fillOval(x - 3, y - 3, 6, 6);
            }
            default -> { graphics.setColor(Color.WHITE); graphics.drawOval(x - radius, y - radius, radius * 2, radius * 2); }
        }
    }

    private void polygon(Graphics2D graphics, int x, int y, int radius, double rotation) {
        double angle = Math.toRadians(rotation);
        int[] xs = {x + (int) (Math.cos(angle) * radius),
                x + (int) (Math.cos(angle + 2.5) * radius),
                x + (int) (Math.cos(angle - 2.5) * radius)};
        int[] ys = {y + (int) (Math.sin(angle) * radius),
                y + (int) (Math.sin(angle + 2.5) * radius),
                y + (int) (Math.sin(angle - 2.5) * radius)};
        graphics.drawPolygon(xs, ys, 3);
    }

    private void reloadPlugins() {
        plugins.reload(Path.of("plugins"));
        status = "Active components: " + plugins.componentNames();
        System.out.println(status);
    }

    @Override public void keyPressed(KeyEvent event) {
        key(event, true);
        if (event.getKeyCode() == KeyEvent.VK_R) reloadPlugins();
        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) System.exit(0);
    }
    @Override public void keyReleased(KeyEvent event) { key(event, false); }
    @Override public void keyTyped(KeyEvent event) { }

    private void key(KeyEvent event, boolean down) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_LEFT -> data.setKey("LEFT", down);
            case KeyEvent.VK_RIGHT -> data.setKey("RIGHT", down);
            case KeyEvent.VK_UP -> data.setKey("UP", down);
            case KeyEvent.VK_SPACE -> data.setKey("FIRE", down);
            default -> { }
        }
    }
}
