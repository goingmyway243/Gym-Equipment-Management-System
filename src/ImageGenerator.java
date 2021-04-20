
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nguyen Hai Dang
 */
public class ImageGenerator {
    public JLabel createLabel(String imageIcon, boolean isOdd) {
        JLabel label = new JLabel();
        label.setSize(50, 50);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.setBackground(javax.swing.UIManager.getColor("Table.dropCellForeground"));
        if(isOdd) {
            label.setBackground(Color.white);
        }
        label.setOpaque(true);
        label.setFocusable(true);

        if (imageIcon.equals("Không có hình ảnh") || imageIcon.equals("")) {
            label.setText("Không có hình ảnh");
            return label;
        }

        label.setIcon(ResizeImage(_imageFolderPath + imageIcon, label));
        return label;
    }
    
    public void ImageColumnSetting(javax.swing.JTable table) {
        table.getColumn("Hình ảnh").setCellRenderer(new imageTableCellRenderer());
    }

    class imageTableCellRenderer implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            TableColumn tb = table.getColumn("Hình ảnh");
            tb.setMaxWidth(150);
            tb.setMinWidth(120);
            table.setRowHeight(50);

            return (Component) value;
        }
    }

    static public ImageIcon ResizeImage(String imagePath, JLabel label) {
        ImageIcon myImage = new ImageIcon(imagePath);
        Image img = myImage.getImage();
        Image newImg = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
    }
    
    final String _imageFolderPath = new File("").getAbsolutePath() + "/";
}
