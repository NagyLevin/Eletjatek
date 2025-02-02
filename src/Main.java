import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

//A cél az lenne, hogy az elején lespawnol egy csomó pont, mindegyik más szinü, ha összeérnek, akkor
//a gyerekük megkapja a sük kombinációát
//egy idő után meghalnak
//miközben meghalnak halványuljanak el minél inkább
//esetleg huzzanak egy pontozott szakaszt maguk után?
//fix hogy kettő golyo ne tudjon egymásba buggolni



public class Main {


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BlackWindow window = new BlackWindow();
            window.setVisible(true);
        });
    }
}

class BlackWindow extends JFrame {
    public final int XX = 800;
    public final int YY = 600;
    public BlackWindow() {
        setTitle("Life Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(XX, YY);
        setLocationRelativeTo(null);

        MovingDotPanel panel = new MovingDotPanel(XX,YY);
        add(panel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                }
            }
        });
    }
}

class MovingDotPanel extends JPanel implements ActionListener {
    private final List<MovingDot> dots;
    private final Timer timer;
    private final Random random;
    private int filedsizeX;
    private int filedsizeY;

    BlackWindow BW;
    public MovingDotPanel(int XX, int YY) {
        filedsizeX = XX-20;
        filedsizeY = YY-20;
        setBackground(Color.BLACK);
        random = new Random();
        dots = new ArrayList<>();

        // Add multiple dots with different positions, directions, and colors
        for (int i = 0; i < 50; i++) {
            dots.add(new MovingDot(random.nextInt(filedsizeX)+10, random.nextInt(filedsizeY)+10, getRandomColor(), random.nextInt(200), random.nextInt(7) - 3, random.nextInt(7) - 3));
        }

        timer = new Timer(50, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (MovingDot dot : dots) {
            g.setColor(dot.getColor());
            g.fillOval(dot.getX(), dot.getY(), dot.getSize(), dot.getSize());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < dots.size(); i++) {
            MovingDot dot = dots.get(i);
            dot.move(getWidth(), getHeight());
            dot.decreaseLifetime();

            if (dot.isExpired()) {
                dots.remove(i);
                i--;
                continue;
            }

            // Check for collisions
            for (int j = i + 1; j < dots.size(); j++) {
                MovingDot other = dots.get(j);

                if (dot.collidesWith(other) && dot.isProtected()) {
                    dot.reverseDirection();
                    other.reverseDirection();
                    dot.protect();

                    // 50% chance to create a new dot at the collision point
                    if (dot.getlife() > dot.maxlife /2 && other.getlife() > dot.maxlife/2) {
                        dots.add(new MovingDot(dot.getX(), dot.getY(), getRandomColor(), random.nextInt(200), random.nextInt(7) - 3, random.nextInt(7) - 3));
                    }
                }

            }
        }
        repaint();
    }

    private Color getRandomColor() {
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}

class MovingDot {
    private int x, y;
    private final int dotSize = 10;
    private Color color;
    private int lifetime;
    private int speedX, speedY;
    public final int maxlife = 1000;
    private int spawnprot = 20;

    public MovingDot(int x, int y, Color color, int lifetime, int speedX, int speedY) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.lifetime = lifetime;
        this.speedX = (speedX == 0) ? 1 : speedX; // Avoid zero velocity
        this.speedY = (speedY == 0) ? 1 : speedY;
    }

    public void move(int panelWidth, int panelHeight) {
        x += speedX;
        y += speedY;

        if (x < 0 || x > panelWidth - dotSize) speedX = -speedX;
        if (y < 0 || y > panelHeight - dotSize) speedY = -speedY;
    }

    public void decreaseLifetime() {
        lifetime++;
        if (spawnprot != 0){
            spawnprot--;
        }
    }

    public boolean isExpired() {


        return  lifetime >= maxlife;
    }

    public boolean collidesWith(MovingDot other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        int distanceSquared = dx * dx + dy * dy;
        int radiusSum = this.dotSize / 2 + other.dotSize / 2;
        return distanceSquared <= radiusSum * radiusSum;
    }

    public void reverseDirection() {
        speedX = -speedX;
        speedY = -speedY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return dotSize;
    }

    public Color getColor() {
        return color;
    }

    public int getlife() {
        return lifetime;
    }

    public boolean isProtected() {
        return spawnprot == 0;
    }

    public void protect() {
        spawnprot = 5;
    }
}
