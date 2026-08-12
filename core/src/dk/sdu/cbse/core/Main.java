package dk.sdu.cbse.core;
import javax.swing.*;
public final class Main {
    public static void main(String[] args){ SwingUtilities.invokeLater(() -> { JFrame f=new JFrame("CBSE Asteroids"); f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); f.setSize(900,650); f.setLocationRelativeTo(null); f.add(new GamePanel()); f.setVisible(true); }); }
}
