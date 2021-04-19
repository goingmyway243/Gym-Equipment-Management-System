
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
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
public class MainMenu extends javax.swing.JFrame implements SettingFrom.LogOutCallBack, SettingFrom.ExitCallBack {

    /**
     * Creates new form MainMenu
     */
    public MainMenu(int userID, String role) {
        initComponents();
        setResizable(false);

        _userID = userID;
        _role = role;

        eC = new EquipmentDetailsController();
        lED = new ArrayList<>();
        lED = eC.getListEquipment();

        addClickEventForImageCell();

        loadDatabase();
    }

    private void loadDatabase() {
        loadUserInfos();
        loadSuppliers();
        loadEquipmentDetails();
        loadEquipments();
        loadImportDetails();
    }

    private void loadUserInfos() {
        DefaultTableModel tableModel = (DefaultTableModel) usersTable.getModel();
        tableModel.setNumRows(0);

        Connection connector = ConnectMysql.getConnectDB();
        String sql = "select * from users";
        Vector vector;
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                vector = new Vector();
                vector.add(rs.getInt("id"));
                vector.add(rs.getString("lastName") + " " + rs.getString("firstName"));
                vector.add(rs.getDate("birthDay"));
                vector.add(rs.getString("email"));
                vector.add(rs.getString("contactNumber"));
                vector.add(rs.getString("profilePicture"));
                vector.add(rs.getTimestamp("updated_at"));
                tableModel.addRow(vector);
            }
            usersTable.setModel(tableModel);
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadSuppliers() {
        DefaultTableModel tableModel = (DefaultTableModel) suppliersTable.getModel();
        tableModel.setNumRows(0);

        Connection connector = ConnectMysql.getConnectDB();
        String sql = "select * from suppliers";
        Vector vector;
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                vector = new Vector();
                vector.add(rs.getInt("id"));
                vector.add(rs.getString("name"));
                vector.add(rs.getString("address"));
                vector.add(rs.getString("phone_number"));
                tableModel.addRow(vector);
            }
            suppliersTable.setModel(tableModel);
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadImportDetails() {
        DefaultTableModel tableModel = (DefaultTableModel) importDetailsTable.getModel();
        tableModel.setNumRows(0);

        Connection connector = ConnectMysql.getConnectDB();
        String sql = "select * from import_details";
        Vector vector;
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                vector = new Vector();
                vector.add(rs.getInt("id"));
                vector.add(rs.getInt("user_id"));
                vector.add(rs.getDate("date_import"));
                tableModel.addRow(vector);
            }
            importDetailsTable.setModel(tableModel);
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadEquipments() {
        DefaultTableModel tableModel = (DefaultTableModel) equipmentsTable.getModel();
        tableModel.setNumRows(0);

        Connection connector = ConnectMysql.getConnectDB();
        String sql = "select * from gym_equipments";
        Vector vector;
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                vector = new Vector();
                vector.add(rs.getString("id"));
                vector.add(rs.getString("status"));
                vector.add("");
                vector.add(rs.getString("detail_id"));
                vector.add(rs.getInt("import_id"));
                vector.add(rs.getTimestamp("updated_at"));
                tableModel.addRow(vector);
            }
            equipmentsTable.setModel(tableModel);
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadEquipmentDetails() {
        DefaultTableModel tblEquipDetails = (DefaultTableModel) categoriesTable.getModel();
        tblEquipDetails.setRowCount(0);
        List<Equipment_Details> lED = eC.getListEquipment();
        ImageColumnSetting();

        int dem = 1;

        for (Equipment_Details equipment_info : lED) {
            boolean isOdd = dem % 2 != 0 ? true : false;

            tblEquipDetails.addRow(new Object[]{equipment_info.getId(), equipment_info.getName(),
                equipment_info.getPicture() == null ? createLabel("Không có hình ảnh", isOdd) : createLabel(equipment_info.getPicture(), isOdd),
                equipment_info.getPrice(), equipment_info.getWarranty_time() + " năm",
                equipment_info.getSupplier_id()
            });
            dem++;
        }
    }

    public void addClickEventForImageCell() {
        categoriesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                categoriesTable = (JTable) evt.getSource();
                Point p = evt.getPoint();
                int row = categoriesTable.rowAtPoint(p);
                int column = categoriesTable.columnAtPoint(p);
                if (column == 2 && row <= lED.size()) {
                    if (lED.get(row).getPicture() == null) {
                        return;
                    }
                    ShowImageFrame sIF = ShowImageFrame.getObj();
                    sIF.setVisible(true);
                    String image = imageFolderPath + lED.get(row).getPicture();
                    sIF.showImageLbl.setIcon(ResizeImage(image, sIF.showImageLbl));
                }
            }
        });
    }

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

        if (imageIcon.equals("Không có hình ảnh")) {
            label.setText("Không có hình ảnh");
            return label;
        }

        label.setIcon(ResizeImage(imageFolderPath + imageIcon, label));
        return label;
    }

    public void ImageColumnSetting() {
        categoriesTable.getColumn("Hình ảnh").setCellRenderer(new categoriesTableCellRenderer());
    }

    class categoriesTableCellRenderer implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            TableColumn tb = categoriesTable.getColumn("Hình ảnh");
            tb.setMaxWidth(150);
            tb.setMinWidth(120);
            categoriesTable.setRowHeight(50);

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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainDesktopPane = new javax.swing.JDesktopPane();
        titleLabel = new javax.swing.JLabel();
        addCategoryButton = new javax.swing.JButton();
        addSupplierButton = new javax.swing.JButton();
        newImportButton = new javax.swing.JButton();
        settingButton = new javax.swing.JButton();
        mainTabbedPane = new javax.swing.JTabbedPane();
        equipmentsScrollPane = new javax.swing.JScrollPane();
        equipmentsTable = new javax.swing.JTable();
        importDetailsScrollPane = new javax.swing.JScrollPane();
        importDetailsTable = new javax.swing.JTable();
        usersScrollPane = new javax.swing.JScrollPane();
        usersTable = new javax.swing.JTable();
        categoriesScrollPane = new javax.swing.JScrollPane();
        categoriesTable = new javax.swing.JTable();
        suppliersScrollPane = new javax.swing.JScrollPane();
        suppliersTable = new javax.swing.JTable();
        editButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        MainDesktopPane.setBackground(new java.awt.Color(102, 102, 102));
        MainDesktopPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleLabel.setText("Hệ thống quản lý Thiết bị phòng Gym");

        addCategoryButton.setText("Thêm loại thiết bị");
        addCategoryButton.setPreferredSize(new java.awt.Dimension(107, 23));
        addCategoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCategoryButtonActionPerformed(evt);
            }
        });

        addSupplierButton.setText("Thêm nhà cc");
        addSupplierButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addSupplierButton.setPreferredSize(new java.awt.Dimension(107, 23));
        addSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSupplierButtonActionPerformed(evt);
            }
        });

        newImportButton.setText("Tạo phiếu nhập");
        newImportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newImportButtonActionPerformed(evt);
            }
        });

        settingButton.setText("Cài đặt");
        settingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingButtonActionPerformed(evt);
            }
        });

        equipmentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã thiết bị", "Trạng thái", "Hình ảnh", "Mã loại thiết bị", "Mã phiếu nhập", "Cập nhật lúc"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        equipmentsScrollPane.setViewportView(equipmentsTable);

        mainTabbedPane.addTab("Thiết bị đang quản lý", equipmentsScrollPane);

        importDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã phiếu nhập", "Mã người nhập", "Ngày nhập"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        importDetailsScrollPane.setViewportView(importDetailsTable);

        mainTabbedPane.addTab("Danh sách phiếu nhập", importDetailsScrollPane);

        usersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã nhân viên", "Họ tên", "Ngày sinh", "Email", "Số điện thoại", "Chân dung", "Cập nhật lúc"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        usersScrollPane.setViewportView(usersTable);

        mainTabbedPane.addTab("Danh sách nhân viên", usersScrollPane);

        categoriesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã loại", "Tên", "Hình ảnh", "Giá", "Hạn bảo hành", "Mã nhà cung cấp"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        categoriesScrollPane.setViewportView(categoriesTable);

        mainTabbedPane.addTab("Loại thiết bị", categoriesScrollPane);

        suppliersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã nhà cung cấp", "Tên nhà cung cấp", "Địa chỉ", "Số điện thoại"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        suppliersScrollPane.setViewportView(suppliersTable);

        mainTabbedPane.addTab("Nhà cung cấp", suppliersScrollPane);

        editButton.setText("Sửa...");
        editButton.setEnabled(false);
        editButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editButton.setPreferredSize(new java.awt.Dimension(107, 23));

        removeButton.setText("Xóa...");
        removeButton.setEnabled(false);
        removeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        removeButton.setPreferredSize(new java.awt.Dimension(107, 23));

        MainDesktopPane.setLayer(titleLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainDesktopPane.setLayer(addCategoryButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainDesktopPane.setLayer(addSupplierButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainDesktopPane.setLayer(newImportButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainDesktopPane.setLayer(settingButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainDesktopPane.setLayer(mainTabbedPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainDesktopPane.setLayer(editButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainDesktopPane.setLayer(removeButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout MainDesktopPaneLayout = new javax.swing.GroupLayout(MainDesktopPane);
        MainDesktopPane.setLayout(MainDesktopPaneLayout);
        MainDesktopPaneLayout.setHorizontalGroup(
            MainDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainDesktopPaneLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(mainTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 725, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 157, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainDesktopPaneLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(MainDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainDesktopPaneLayout.createSequentialGroup()
                        .addGroup(MainDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(newImportButton, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                            .addComponent(settingButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addCategoryButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addSupplierButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(editButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainDesktopPaneLayout.createSequentialGroup()
                        .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(197, 197, 197))))
        );
        MainDesktopPaneLayout.setVerticalGroup(
            MainDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainDesktopPaneLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(MainDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(MainDesktopPaneLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(newImportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(addCategoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(addSupplierButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(removeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(settingButton))
                    .addComponent(mainTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainDesktopPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainDesktopPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newImportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newImportButtonActionPerformed
        ImportForm importForm = new ImportForm(_userID);
        importForm.setLocationRelativeTo(this);
        importForm.setVisible(true);
    }//GEN-LAST:event_newImportButtonActionPerformed

    private void addCategoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCategoryButtonActionPerformed
        AddEquimentDetailsForm addEquipmentDetailsForm = new AddEquimentDetailsForm(this);
        addEquipmentDetailsForm.setLocationRelativeTo(this);
        addEquipmentDetailsForm.setVisible(true);
    }//GEN-LAST:event_addCategoryButtonActionPerformed

    private void addSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSupplierButtonActionPerformed
        AddSupplier addSupplier = new AddSupplier(this, true);
        addSupplier.setLocationRelativeTo(this);
        addSupplier.setVisible(true);
    }//GEN-LAST:event_addSupplierButtonActionPerformed

    private void settingButtonActionPerformed(java.awt.event.ActionEvent evt) {
        settingFrom = new SettingFrom(_userID);
        settingFrom.setLogOutCallBack(this);
        settingFrom.setExitCallBack(this);
        settingFrom.setLocationRelativeTo(null);
        settingFrom.setVisible(true);
        setEnabled(false);
    }

    private int _userID = 0;
    private String _role = "";
    private SettingFrom settingFrom = null;
    final String imageFolderPath = new File("").getAbsolutePath() + "/";

    private EquipmentDetailsController eC = null;
    private List<Equipment_Details> lED = null;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane MainDesktopPane;
    private javax.swing.JButton addCategoryButton;
    private javax.swing.JButton addSupplierButton;
    private javax.swing.JScrollPane categoriesScrollPane;
    private javax.swing.JTable categoriesTable;
    private javax.swing.JButton editButton;
    private javax.swing.JScrollPane equipmentsScrollPane;
    private javax.swing.JTable equipmentsTable;
    private javax.swing.JScrollPane importDetailsScrollPane;
    private javax.swing.JTable importDetailsTable;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JButton newImportButton;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton settingButton;
    private javax.swing.JScrollPane suppliersScrollPane;
    private javax.swing.JTable suppliersTable;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JScrollPane usersScrollPane;
    private javax.swing.JTable usersTable;
    // End of variables declaration//GEN-END:variables

    @Override
    public void exit() {
        setEnabled(true);
        setVisible(true);
        settingFrom = null;
    }

    @Override
    public void logout() {
        dispose();
        settingFrom = null;
    }

}
