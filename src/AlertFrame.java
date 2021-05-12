
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author skqist225
 */
public class AlertFrame extends javax.swing.JFrame {

    /**
     * Creates new form AlertFrame
     */
    public AlertFrame(String text) {
        initComponents();

        setAlwaysOnTop(true);
        Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        Dimension srcSize = Toolkit.getDefaultToolkit().getScreenSize();

        txt_Content.setText(text);

        setResizable(false);
        setLocation(srcSize.width - toolHeight.top - getWidth() + 10, srcSize.height - toolHeight.top - getHeight() + 10);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < srcSize.height * 1 / 2 - getHeight()*2 - 36; i++) {
                    setLocation(srcSize.width - toolHeight.top - getWidth() + 10, srcSize.height - toolHeight.top - getHeight() - i);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(AlertFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                dispose();
            }
        }).start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txt_Content = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);

        jLabel1.setBackground(new java.awt.Color(247, 247, 247));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Success.gif"))); // NOI18N
        jLabel1.setOpaque(true);

        txt_Content.setBackground(new java.awt.Color(247, 247, 247));
        txt_Content.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_Content.setForeground(new java.awt.Color(0, 102, 0));
        txt_Content.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_Content.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txt_Content, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(txt_Content, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel txt_Content;
    // End of variables declaration//GEN-END:variables
}
