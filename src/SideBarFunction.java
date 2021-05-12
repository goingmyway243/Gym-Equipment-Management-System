
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author skqist225
 */
public class SideBarFunction extends javax.swing.JDialog implements WindowListener {

    /**
     * Creates new form SideBarFunction2
     */
    public AdminDashBoard admDb;

    public SideBarFunction(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        admDb = (AdminDashBoard) parent;

        initFilter(admDb.getEquipmentsTable());
        initFilter(admDb.getImportDetailsTable());
        initFilter(admDb.getUsersTable());
        initFilter(admDb.getCategoriesTable());
        initFilter(admDb.getSuppliersTable());

        Dimension srcSize = Toolkit.getDefaultToolkit().getScreenSize();
//        System.out.println("");
        
        setLocation(srcSize.width / 2 - admDb.getWidth() / 2 - 80 + admDb.getWidth(), srcSize.height / 2 - admDb.getHeight() / 2 + 110);

        lbl_close_hover.setIcon(ImageGenerator.ResizeImage(new ImageGenerator().getImageFolderPath() + "/src/icon/close3.png", lbl_close_hover));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        admDb.getButton6().setEnabled(false);

    }

    public void initFilterComboBox(int currentTableIndex) {
        filterComboBox.removeAllItems();
        filterComboBox.addItem("Toàn bộ");

        JTable table;
        switch (currentTableIndex) {
            case 1:
                table = admDb.getEquipmentsTable();
                break;
            case 2:
                table = admDb.getImportDetailsTable();
                break;
            case 3:
                table = admDb.getUsersTable();
                break;
            case 4:
                table = admDb.getCategoriesTable();
                break;
            default:
                table = admDb.getSuppliersTable();
                break;
        }

        for (int i = 0; i < table.getColumnCount(); i++) {
            String columnName = table.getColumnName(i);
            if (columnName.equals("Hình ảnh")) {
                continue;
            }
            filterComboBox.addItem(columnName);
        }
    }

    private void initFilter(JTable table) {
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(rowSorter);

        searchTextField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = searchTextField.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    if (filterComboBox.getSelectedItem().toString().equals("Toàn bộ")) {
                        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    } else {
                        int column = filterComboBox.getSelectedIndex() - 1;
                        if (admDb.getSelectedTable() == 1 || admDb.getSelectedTable() == 4) {
                            column = column > 1 ? column + 1 : column;
                        }
                        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, column));
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = searchTextField.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    public JButton getEditButton() {
        return editButton;
    }

    public JComboBox<String> getFilterComboBox() {
        return filterComboBox;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

    public JTextField getSearchTextField() {
        return searchTextField;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        button4 = new Button();
        button3 = new Button();
        button2 = new Button();
        jLabel36 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        removeButton = new javax.swing.JButton();
        filterComboBox = new javax.swing.JComboBox<>();
        editButton = new javax.swing.JButton();
        searchTextField = new javax.swing.JTextField();
        lbl_close_hover = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(45, 53, 60));
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(45, 53, 60));

        jLabel37.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Lọc:");

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Chức Năng:");

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Sửa");

        button4.setBackground(new java.awt.Color(195, 20, 50));
        button4.setForeground(new java.awt.Color(255, 255, 255));
        button4.setText("Tạo Phiếu Nhập");
        button4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        button4.setGradientBackgroundColor(new java.awt.Color(36, 11, 54));
        button4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button4.setRounded(true);
        button4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button4ActionPerformed(evt);
            }
        });

        button3.setBackground(new java.awt.Color(195, 20, 50));
        button3.setForeground(new java.awt.Color(255, 255, 255));
        button3.setText("Thêm Nhà Cung Cấp");
        button3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        button3.setGradientBackgroundColor(new java.awt.Color(36, 11, 54));
        button3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button3.setRounded(true);
        button3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button3ActionPerformed(evt);
            }
        });

        button2.setBackground(new java.awt.Color(195, 20, 50));
        button2.setForeground(new java.awt.Color(255, 255, 255));
        button2.setText("Thêm Loại Thiết Bị");
        button2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        button2.setGradientBackgroundColor(new java.awt.Color(36, 11, 54));
        button2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button2.setRounded(true);
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Tìm Kiếm:");

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Xóa");

        removeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/delete.png"))); // NOI18N
        removeButton.setEnabled(false);
        removeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        removeButton.setPreferredSize(new java.awt.Dimension(107, 23));
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        filterComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                filterComboBoxItemStateChanged(evt);
            }
        });

        editButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/edit.png"))); // NOI18N
        editButton.setEnabled(false);
        editButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editButton.setPreferredSize(new java.awt.Dimension(107, 23));
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        lbl_close_hover.setBackground(new java.awt.Color(45, 53, 60));
        lbl_close_hover.setForeground(new java.awt.Color(255, 255, 255));
        lbl_close_hover.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_close_hover.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_close_hoverMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbl_close_hoverMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_close_hoverMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 29, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(10, 10, 10)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(removeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(button4, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(filterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(55, 55, 55))
                            .addComponent(lbl_close_hover, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(lbl_close_hover, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel8)
                .addGap(28, 28, 28)
                .addComponent(button4, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel7)
                .addGap(32, 32, 32)
                .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel6)
                .addGap(36, 36, 36)
                .addComponent(removeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button4ActionPerformed
        ImportForm importForm = new ImportForm(admDb, admDb.getUserID());
        importForm.setLocationRelativeTo(this);
        importForm.setVisible(true);
    }//GEN-LAST:event_button4ActionPerformed

    private void button3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button3ActionPerformed
        admDb.setLabelBackground(admDb.getLbl_menuItem_5());
        admDb.showPanel(admDb.getPnl_suppliers());
    }//GEN-LAST:event_button3ActionPerformed

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        admDb.setLabelBackground(admDb.getLbl_menuItem_4());
        admDb.showPanel(admDb.getPnl_eqsDetail());
    }//GEN-LAST:event_button2ActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        int result = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa?", "Cảnh báo!",
                JOptionPane.YES_NO_OPTION);
        switch (admDb.getSelectedTable()) {
            case 1: {
                if (result == JOptionPane.YES_OPTION) {
                    String id = admDb.getEquipmentsTable().getValueAt(admDb.getEquipmentsTable().getSelectedRow(), 0).toString();
                    admDb.getDeleter().deleteEquipment(id);
                }
                break;
            }
            case 2: {
                if (result == JOptionPane.YES_OPTION) {
                    int id = Integer.valueOf(admDb.getImportDetailsTable().getValueAt(admDb.getImportDetailsTable().getSelectedRow(), 0).toString());
                    admDb.getDeleter().deleteImport(id);
                }
                break;
            }
            case 3: {
                if (result == JOptionPane.YES_OPTION) {
                    int id = Integer.valueOf(admDb.getUsersTable().getValueAt(admDb.getUsersTable().getSelectedRow(), 0).toString());
                    admDb.getDeleter().deleteUser(id);
                }
                break;
            }
            case 4: {
                if (result == JOptionPane.YES_OPTION) {
                    String id = admDb.getCategoriesTable().getValueAt(admDb.getCategoriesTable().getSelectedRow(), 0).toString();
                    admDb.getDeleter().deleteEquipmentDetail(id);
                }
                break;
            }
            case 5: {
                if (result == JOptionPane.YES_OPTION) {
                    int id = Integer.valueOf(admDb.getSuppliersTable().getValueAt(admDb.getSuppliersTable().getSelectedRow(), 0).toString());
                    admDb.getDeleter().deleteSupplier(id);
                }
                break;
            }
            default:
                break;
        }
        admDb.loadDatabase();
    }//GEN-LAST:event_removeButtonActionPerformed

    private void filterComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterComboBoxItemStateChanged
        searchTextField.setText("");
    }//GEN-LAST:event_filterComboBoxItemStateChanged

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        try {
            switch (admDb.getSelectedTable()) {
                case 1: {
                    String id = admDb.getEquipmentsTable().getValueAt(admDb.getEquipmentsTable().getSelectedRow(), 0).toString();
                    admDb.initRendererForEdit(id);
                    break;
                }
                case 2: {
                    break;
                }
                case 3: {
                    break;
                }
                case 4: {
                    String id = admDb.getCategoriesTable().getValueAt(admDb.getCategoriesTable().getSelectedRow(), 0).toString();
                    admDb.fillOutCategoriesInfo(id);
                    admDb.setIsCategoriesUpdate(true);
                    admDb.getLbl_themCTTB().setText("CẬP NHẬT THIẾT BỊ");
                    admDb.getBtn_confirmCategories().setText("Cập nhật");
                    break;
                }
                case 5: {
                    String index = admDb.getSuppliersTable().getValueAt(admDb.getSuppliersTable().getSelectedRow(), 0).toString();
                    int idx = Integer.parseInt(index);
                    admDb.fillOutSupplierInfo(idx);
                    admDb.getLbl_themNCC().setText("CẬP NHẬT NHÀ CUNG CẤP");
                    admDb.getBtnSupplierConfirm().setText("Cập nhật");
                    break;
                }
                default:
                    break;
            }
            admDb.loadDatabase();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Vui lòng chọn dòng trước khi edit");
        }
    }//GEN-LAST:event_editButtonActionPerformed

    private void lbl_close_hoverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_close_hoverMouseClicked
        dispose();
        admDb.getButton6().setEnabled(true);
    }//GEN-LAST:event_lbl_close_hoverMouseClicked

    private void lbl_close_hoverMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_close_hoverMouseExited
        lbl_close_hover.setSize(40, 32);
        lbl_close_hover.setIcon(ImageGenerator.ResizeImage(new ImageGenerator().getImageFolderPath() + "/src/icon/close3.png", lbl_close_hover));
    }//GEN-LAST:event_lbl_close_hoverMouseExited

    private void lbl_close_hoverMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_close_hoverMouseEntered
        lbl_close_hover.setSize(32, 32);
        lbl_close_hover.setIcon(ImageGenerator.ResizeImage(new ImageGenerator().getImageFolderPath() + "/src/icon/close2.png", lbl_close_hover));
    }//GEN-LAST:event_lbl_close_hoverMouseEntered

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Button button2;
    private Button button3;
    private Button button4;
    private javax.swing.JButton editButton;
    private javax.swing.JComboBox<String> filterComboBox;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl_close_hover;
    private javax.swing.JButton removeButton;
    private javax.swing.JTextField searchTextField;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosing(WindowEvent we) {

    }

    @Override
    public void windowClosed(WindowEvent we) {
        admDb.getButton6().setEnabled(true);
    }

    @Override
    public void windowIconified(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
