package dk.sdu.cbse.core;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public final class Main {
    private Main() { }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SB-KOM Component Asteroids");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 650);
            frame.setLocationRelativeTo(null);
            frame.add(new GamePanel());
            frame.setVisible(true);
        });
    }
}
