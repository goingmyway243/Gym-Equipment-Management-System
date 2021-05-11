/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author skqist225
 */
import java.awt.Color;
import java.awt.Font;
import static java.awt.Font.BOLD;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TextAnimation extends JPanel {

    int x, y;
    String text;

    TextAnimation(int x, int y, String text) {
        this.setBounds(0, 0, 500, 30);
        this.x = x;
        this.y = y;
        this.text = text;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        Color color1 = new Color(78, 84, 200);
        Color color2 = new Color(143, 148, 251);

        GradientPaint gp = new GradientPaint(0, 0, color1, 180, height, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }

    public void paint(Graphics gp) {
        super.paint(gp);
        Graphics2D g2d = (Graphics2D) gp;
        g2d.setColor(Color.yellow);
        g2d.setFont(new Font("BOLD", BOLD, 14));

        g2d.drawString(text, x, y);
        try {
            Thread.sleep(10);
            x += 1;

            if (x > getWidth()) {
                x = 30;
            }
            repaint();

        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }

    }
}
