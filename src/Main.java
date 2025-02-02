import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {
    private static final int XX = 900;     //golbális méretek
    private static final int YY = 600;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Eletjatek");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(XX, YY);
            frame.setLocationRelativeTo(null); //ablak középre

            JPanel panel = new JPanel();
            panel.setBackground(Color.BLACK); //hatter fekete
            frame.add(panel);

            frame.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        frame.dispose();
                    }
                }
            });

            frame.setVisible(true);
        });
    }
}
