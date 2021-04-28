
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nguyen Hai Dang
 */
public class ImportForm extends javax.swing.JFrame {

    public Vector<String> checkIDList = new Vector<>();

    /**
     * Creates new form ImportForm
     */
    public ImportForm(MainMenu mainMenu, int userID) {
        _mainMenuForm = mainMenu;
        _userID = userID;

        initComponents();
        initRenderer();
    }

    public void addEquiment(String id, String name, String status, int price, String picture, String detailID) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        boolean isOdd = _count % 2 == 0 ? true : false;

        _imgGenerator.ImageColumnSetting(table);

        tableModel.addRow(new Object[]{id, name, status, price,
            _imgGenerator.createLabel(picture, isOdd),
            detailID});
        checkIDList.add(id.substring(0, 3));
        _count++;
    }

    private void saveEquipmentToDatabase(String id, String status, String detailID, int importID, java.sql.Timestamp timeStamp) {
        Connection connector = ConnectMysql.getConnectDB();
        String sql = "insert into gym_equipments values (?,?,?,?,?,?)";
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, status);
            ps.setString(3, detailID);
            ps.setInt(4, importID);
            ps.setTimestamp(5, timeStamp);
            ps.setTimestamp(6, timeStamp);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ImportForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void saveImportDetailToDatabase(int importID, int userID, java.sql.Date date) {
        Connection connector = ConnectMysql.getConnectDB();
        String sql = "insert into import_details values (?,?,?)";
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ps.setInt(1, importID);
            ps.setInt(2, userID);
            ps.setDate(3, date);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ImportForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initRenderer() {
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        loadImportID();
        loadUserName();
        importDateTextField.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
    }

    private void loadUserName() {
        Connection connector = ConnectMysql.getConnectDB();
        String sql = "select * from users where id = '" + _userID + "'";
        String userName = "";
        try {
            ResultSet rs = connector.createStatement().executeQuery(sql);
            if (rs.next()) {
                userName = rs.getString("lastName") + " " + rs.getString("firstName");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImportForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        userNameTextField.setText(userName);
    }

    private void loadImportID() {
        Connection connector = ConnectMysql.getConnectDB();
        String sql = "select count(id) as countID from import_details";
        int id = 1;
        try {
            ResultSet rs = connector.createStatement().executeQuery(sql);
            if (rs.next()) {
                id = rs.getInt("countID") + 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImportForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        importIDTextField.setText(id + "");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        importIDLabel = new javax.swing.JLabel();
        importIDTextField = new javax.swing.JTextField();
        userNameLabel = new javax.swing.JLabel();
        userNameTextField = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        importDateLabel = new javax.swing.JLabel();
        importDateTextField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        importIDLabel.setText("Mã phiếu nhập");

        importIDTextField.setEditable(false);

        userNameLabel.setText("Tên nhân viên");

        userNameTextField.setEditable(false);

        addButton.setText("Thêm thiết bị");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã thiết bị", "Tên thiết bị", "Trạng thái", "Giá", "Hình ảnh", "Mã loại thiết bị"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPane.setViewportView(table);

        saveButton.setText("Lưu");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Hủy");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        importDateLabel.setText("Ngày nhập");

        importDateTextField.setEditable(false);

        jButton1.setText("Sửa");
        jButton1.setEnabled(false);

        jButton2.setText("Xóa");
        jButton2.setEnabled(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 255));
        jLabel1.setText("TẠO PHIẾU NHẬP");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(importIDLabel)
                            .addComponent(userNameLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(importIDTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                            .addComponent(userNameTextField))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(importDateLabel)
                                .addGap(17, 17, 17)
                                .addComponent(importDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)))
                        .addGap(19, 19, 19))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(320, 320, 320))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(importIDLabel)
                    .addComponent(importIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userNameLabel)
                    .addComponent(userNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2))
                    .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(cancelButton)
                    .addComponent(importDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(importDateLabel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        String id, status, detailID;
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(new Date().getTime());
        java.sql.Date date = new java.sql.Date(new Date().getTime());
        int importID = Integer.valueOf(importIDTextField.getText());

        if (table.getRowCount() > 0) {
            saveImportDetailToDatabase(importID, _userID, date);

            for (int i = 0; i < table.getRowCount(); i++) {
                id = table.getValueAt(i, 0).toString();
                status = table.getValueAt(i, 2).toString();
                detailID = table.getValueAt(i, 5).toString();
                saveEquipmentToDatabase(id, status, detailID, importID, timeStamp);
            }
            System.out.println("Lưu vào CSDL thành công");
        }
        JOptionPane.showMessageDialog(null, "Lưu vào CSDL thành công");
        _mainMenuForm.loadDatabase();
        this.dispose();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        AddEquipmentForm addEquimentForm = new AddEquipmentForm(this);
        addEquimentForm.setVisible(true);
        addEquimentForm.setLocationRelativeTo(this);
    }//GEN-LAST:event_addButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private MainMenu _mainMenuForm = null;
    private ImageGenerator _imgGenerator = new ImageGenerator();
    private int _userID = 0;
    private int _count = 0;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel importDateLabel;
    private javax.swing.JTextField importDateTextField;
    private javax.swing.JLabel importIDLabel;
    private javax.swing.JTextField importIDTextField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton saveButton;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable table;
    private javax.swing.JLabel userNameLabel;
    private javax.swing.JTextField userNameTextField;
    // End of variables declaration//GEN-END:variables
}
