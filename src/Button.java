
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author skqist225
 */
public class Button extends JButton implements MouseListener {

    private boolean rounded;
    private boolean backgroundPainted;
    private boolean linePainted;
    private boolean entered;
    private boolean pressed;

    private Color enteredColor;
    private Color pressedColor;
    private Color gradientBackgroundColor;
    private Color gradientLineColor;
    private Color lineColor;

    public Color savedBgColor;
    public Color savedBgGraColor;
    public Color savedFgColor;
    public boolean isBgGra;
    public boolean isLineGra;

    public Button() {
        super();
        rounded = false;
        backgroundPainted = true;
        linePainted = true;
        entered = false;
        pressed = false;

        enteredColor = getBackground().brighter();
        pressedColor = getBackground().darker();
        lineColor = Color.black;

        setContentAreaFilled(false);
        setFocusPainted(false);
        addMouseListener(this);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void setBackground(Color color) {
        super.setBackground(color);

        enteredColor = color.brighter();
        pressedColor = color.darker();
    }

    @Override
    public void setForeground(Color color) {
        super.setForeground(color);
        savedFgColor = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape s = rounded ? new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, getHeight() - 2, getHeight() - 2) : new Rectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2);
        if (backgroundPainted || pressed && !backgroundPainted) {
            if (gradientBackgroundColor == null) {
                g2d.setColor(color());
            } else {
                savedBgColor = getBackground();
                savedBgGraColor = gradientBackgroundColor;
                GradientPaint paint = new GradientPaint(0, 0, getBackground(), getWidth(), getHeight(), gradientBackgroundColor);
                g2d.setPaint(paint);
            }
            g2d.fill(s);
        }

        if (linePainted) {
            if (gradientLineColor == null) {
                g2d.setColor(isEnabled() ? lineColor : new Color(204, 204, 204));
            } else {
                GradientPaint paint = new GradientPaint(0, 0, lineColor, getWidth(), getHeight(), gradientLineColor);
                g2d.setPaint(paint);
            }
            g2d.draw(s);
        }

        super.paintComponent(g);
    }

    private Color color() {
        if (!isEnabled()) {
            return new Color(204, 204, 204);
        }

        Color temp = getBackground();
        if (pressed) {
            return pressedColor;
        }
        if (entered) {
            return enteredColor;
        }

        return temp;
    }

    public void setRounded(boolean rounded) {
        this.rounded = rounded;
    }

    public void setBackgroundPainted(boolean backgroundPainted) {
        this.backgroundPainted = backgroundPainted;
    }

    public void setLinePainted(boolean linePainted) {
        this.linePainted = linePainted;
    }

    public void setEntered(boolean entered) {
        this.entered = entered;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public void setEnteredColor(Color enteredColor) {
        this.enteredColor = enteredColor;
    }

    public void setPressdColor(Color pressedColor) {
        this.pressedColor = pressedColor;
    }

    public void setGradientBackgroundColor(Color gradientBackgroundColor) {
        this.gradientBackgroundColor = gradientBackgroundColor;
    }

    public void setGradientLineColor(Color gradientLineColor) {
        this.gradientLineColor = gradientLineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        pressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        pressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        entered = true;

        if (gradientBackgroundColor != null) {
            setGradientBackgroundColor(null);
            setBackground(savedFgColor);
            setForeground(savedBgColor);

            gradientLineColor = savedBgGraColor;
            setLineColor(savedBgColor);
            setGradientLineColor(gradientLineColor);

            setLinePainted(true);
            isBgGra = true;
        } 
//        else {
//            setForeground(getBackground());
//            setBackground(lineColor);
//            setGradientBackgroundColor(gradientLineColor);
//
//            setGradientLineColor(null);
//            setLineColor(null);
//            setLinePainted(false);
//
//isLineGra = true;
//        }
    }

    @Override
    public void mouseExited(MouseEvent me) {
        entered = false;

        if (isBgGra) {
            setForeground(getBackground());
            setBackground(savedBgColor);
            setGradientBackgroundColor(savedBgGraColor);

            setLinePainted(false);
        }
//        else if(isLineGra) {
//            setBackground(savedBgColor);
//            
//            setForeground(savedFgColor);
//            setLineColor(getBackground());
//            setGradientLineColor(savedBgGraColor);
//
//            setLinePainted(true);
//        }
    }
}
