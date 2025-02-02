import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

//A cél az lenne, hogy az elején lespawnol egy csomó pont, mindegyik más szinü, ha összeérnek, akkor
//a gyerekük megkapja a sük kombinációát
//egy idő után meghalnak
//esetleg huzzanak egy pontozott szakaszt maguk után?

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Black Window");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            MovingDotPanel panel = new MovingDotPanel();
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

class MovingDotPanel extends JPanel implements ActionListener {
    private int x = 400;
    private final int y = 300;
    private final int dotSize = 10;
    private final Timer timer;
    private Color dotColor = Color.RED;
    private final Random random;

    public MovingDotPanel() {
        setBackground(Color.BLACK);
        timer = new Timer(50, this);
        timer.start();
        random = new Random();
        dotColor = getRandomColor();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(dotColor);
        g.fillOval(x, y, dotSize, dotSize);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        x -= 5;

        if (x < 0) x = getWidth(); //ha eleri a szelet akkoe visszakerül az emeletre
        dotColor = getRandomColor();
        repaint(); //refresh
    }

    private Color getRandomColor() {
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

}
