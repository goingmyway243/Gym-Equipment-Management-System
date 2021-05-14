
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nguyen Hai Dang
 */
public class AddEquipmentForm extends javax.swing.JFrame {

    /**
     * Creates new form AddEquimentForm
     */
    public AddEquipmentForm(AdminDashBoard parent, String id) {
        _admDb = parent;
        _id = id;
        _editMode = true;

        initComponents();
        initRendererForEdit();
        loadCurrentData();
    }

    public AddEquipmentForm(AdminDashBoard parent) {
        _admDb = parent;

        initComponents();
        initRenderer();
    }

    public void loadDetailIDComboBox() {
        detailIDComboBox.removeAllItems();
        detailIDComboBox.addItem("");
        _detailUpdated = true;

        Connection connector = ConnectMysql.getConnectDB();
        String sql = "select id from equipment_details";

        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                detailIDComboBox.addItem(rs.getString("id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddEquipmentForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadDetailIDComboBox(String detailID) {
        loadDetailIDComboBox();
        detailIDComboBox.setSelectedItem(detailID);
    }

    private void initRenderer() {
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setAlertVisible(false);
        loadStatusComboBox();
        loadDetailIDComboBox();
        _detailUpdated = false;
    }

    private void initRendererForEdit() {
        initRenderer();
        equipmentIDTextField.setText(_id);
        equipmentIDTextField.setEditable(false);
        amountLabel.setVisible(false);
        amountTextField.setVisible(false);
    }

    private void loadCurrentData() {
        String status = "", detailID = "";
        Connection connector = ConnectMysql.getConnectDB();
        String sql = "select * from gym_equipments where id = '" + _id + "'";
        try {
            ResultSet rs = connector.createStatement().executeQuery(sql);
            if (rs.next()) {
                status = rs.getString("status");
                detailID = rs.getString("detail_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddEquipmentForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!status.equals("") && !detailID.equals("")) {
            for (int i = 0; i < statusComboBox.getItemCount(); i++) {
                if (statusComboBox.getItemAt(i).toString().equals(status)) {
                    statusComboBox.setSelectedIndex(i);
                }
            }

            for (int i = 0; i < detailIDComboBox.getItemCount(); i++) {
                if (detailIDComboBox.getItemAt(i).toString().equals(detailID)) {
                    detailIDComboBox.setSelectedIndex(i);
                }
            }
        }
    }

    private void loadStatusComboBox() {
        statusComboBox.removeAllItems();
        statusComboBox.addItem("Đang hoạt động");
        statusComboBox.addItem("Bảo trì");
        statusComboBox.addItem("Bị hỏng");
    }

    private void loadDetailInfo(String detailID) {
        String name = "", picture = "", supplier = "";
        int price = 0, warrantyTime = 0, supplierID = 0;

        Connection connector = ConnectMysql.getConnectDB();
        String sql = "select * from equipment_details where id = '" + detailID + "'";

        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            name = rs.getString("name");
            picture = rs.getString("picture");
            price = rs.getInt("price");
            warrantyTime = rs.getInt("warranty_time");
            supplierID = rs.getInt("supplier_id");
        } catch (SQLException ex) {
            Logger.getLogger(AddEquipmentForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        sql = "select name from suppliers where id = '" + supplierID + "'";
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            supplier = rs.getString("name");
        } catch (SQLException ex) {
            Logger.getLogger(AddEquipmentForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        _imagePath = picture;
        if (picture != null) {
            picture = new File("").getAbsolutePath().concat(picture);
        }

        equipmentNameTextField.setText(name);
        warrantyTextField.setText(warrantyTime + " năm");
        priceTextField.setText(price + "");
        supplierTextField.setText(supplier);
        pictureFieldLabel.setIcon(ImageGenerator.ResizeImage(picture, pictureFieldLabel));
    }

    private void editEquipment(String id, String status, String detailID) {
        Connection connector = ConnectMysql.getConnectDB();
        String sql = "update gym_equipments SET status = ?, detail_id = ?, updated_at = ? WHERE id = ?";
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, detailID);
            ps.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));
            ps.setString(4, id);
            ps.execute();
            System.out.println("Chỉnh sửa thành công");
        } catch (SQLException ex) {
            Logger.getLogger(AddEquipmentForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getMaxEquimentID(String equipmentID) {
        Connection connector = ConnectMysql.getConnectDB();
        String sql = "select count(id) as countID from gym_equipments where substring(id,1,3) = '" + equipmentID + "'";
        int idCount = 1;
        try {
            ResultSet rs = connector.createStatement().executeQuery(sql);
            if (rs.next()) {
                idCount = rs.getInt("countID");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddEquipmentForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < _admDb._checkEquipmentIDList.size(); i++) {
            if (equipmentID.equals(_admDb._checkEquipmentIDList.get(i))) {
                idCount++;
            }
        }

        return idCount;
    }

    private void setAlertVisible(boolean visible) {
        equipmentIDAlertLabel.setVisible(visible);
        amountAlertLabel.setVisible(visible);
        detailIDAlertLabel.setVisible(visible);
    }

    private boolean checkValues(String id, String amount, String detailID) {
        setAlertVisible(false);
        boolean check = true;
        String amountPattern = "\\d{1,2}";
        String idPattern = "[a-zA-Z]{3}";

        if (!id.matches(idPattern)) {
            equipmentIDAlertLabel.setVisible(true);
            check = false;
        }
        if (!amount.matches(amountPattern)) {
            amountAlertLabel.setVisible(true);
            check = false;
        }
        if (detailID.equals("")) {
            detailIDAlertLabel.setVisible(true);
            check = false;
        }

        return check;
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
        equipmentIDLabel = new javax.swing.JLabel();
        equipmentIDTextField = new javax.swing.JTextField();
        statusLabel = new javax.swing.JLabel();
        detailIDLabel = new javax.swing.JLabel();
        equipmentNameLabel = new javax.swing.JLabel();
        equipmentNameTextField = new javax.swing.JTextField();
        statusComboBox = new javax.swing.JComboBox<>();
        detailIDComboBox = new javax.swing.JComboBox<>();
        supplierLabel = new javax.swing.JLabel();
        supplierTextField = new javax.swing.JTextField();
        priceLabel = new javax.swing.JLabel();
        priceTextField = new javax.swing.JTextField();
        warrantyLabel = new javax.swing.JLabel();
        warrantyTextField = new javax.swing.JTextField();
        amountLabel = new javax.swing.JLabel();
        amountTextField = new javax.swing.JTextField();
        pictureLabel = new javax.swing.JLabel();
        amountAlertLabel = new javax.swing.JLabel();
        equipmentIDAlertLabel = new javax.swing.JLabel();
        detailIDAlertLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        pictureFieldLabel = new javax.swing.JLabel();
        addEquipmentConfirmButton = new Button();
        addEquipmentCancelButton = new Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 5, true));

        equipmentIDLabel.setText("Mã thiết bị");

        equipmentIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        statusLabel.setText("Trạng thái");

        detailIDLabel.setText("Mã loại");

        equipmentNameLabel.setText("Tên thiết bị");

        equipmentNameTextField.setEditable(false);
        equipmentNameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        statusComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        detailIDComboBox.setModel(new javax.swing.DefaultComboBoxModel<>());
        detailIDComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                detailIDComboBoxItemStateChanged(evt);
            }
        });

        supplierLabel.setText("Nhà cung cấp");
        supplierLabel.setToolTipText("");

        supplierTextField.setEditable(false);

        priceLabel.setText("Giá");

        priceTextField.setEditable(false);
        priceTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        warrantyLabel.setText("Bảo hành");

        warrantyTextField.setEditable(false);
        warrantyTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        amountLabel.setText("Số lượng");

        amountTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        amountTextField.setText("1");

        pictureLabel.setText("Hình ảnh");

        amountAlertLabel.setForeground(new java.awt.Color(255, 0, 0));
        amountAlertLabel.setText("Nhập số lượng từ 1 - 99");

        equipmentIDAlertLabel.setForeground(new java.awt.Color(255, 0, 0));
        equipmentIDAlertLabel.setText("Nhập mã thiết bị 3 chữ");

        detailIDAlertLabel.setForeground(new java.awt.Color(255, 51, 0));
        detailIDAlertLabel.setText("Chọn 1 mã thiết bị");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 255));
        jLabel1.setText("THÊM THIẾT BỊ");

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 255), 3, true));

        pictureFieldLabel.setBackground(new java.awt.Color(204, 51, 255));
        pictureFieldLabel.setForeground(new java.awt.Color(204, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pictureFieldLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pictureFieldLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        addEquipmentConfirmButton.setText("Xác nhận");
        addEquipmentConfirmButton.setRounded(true);
        addEquipmentConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEquipmentConfirmButtonActionPerformed(evt);
            }
        });

        addEquipmentCancelButton.setText("Hủy");
        addEquipmentCancelButton.setRounded(true);
        addEquipmentCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEquipmentCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(153, 153, 153)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(pictureLabel)
                        .addGap(41, 41, 41)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(amountLabel)
                        .addGap(18, 18, 18)
                        .addComponent(amountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(amountAlertLabel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(216, 216, 216)
                        .addComponent(addEquipmentConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(addEquipmentCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(supplierLabel)
                                    .addComponent(equipmentNameLabel)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(detailIDLabel))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(priceLabel))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(statusLabel)))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43)
                                .addComponent(warrantyLabel)
                                .addGap(18, 18, 18)
                                .addComponent(warrantyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(supplierTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(equipmentIDTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(detailIDComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(42, 42, 42)
                                    .addComponent(detailIDAlertLabel))
                                .addComponent(equipmentIDAlertLabel, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(equipmentNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(equipmentIDLabel)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(equipmentIDLabel)
                    .addComponent(equipmentIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(equipmentIDAlertLabel)
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusLabel)
                    .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(detailIDAlertLabel)
                    .addComponent(detailIDComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(detailIDLabel))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(equipmentNameLabel)
                    .addComponent(equipmentNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(supplierLabel)
                    .addComponent(supplierTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(warrantyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(warrantyLabel)
                            .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(priceLabel)))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pictureLabel)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(amountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(amountLabel)
                            .addComponent(amountAlertLabel))))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addEquipmentCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addEquipmentConfirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void detailIDComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_detailIDComboBoxItemStateChanged
        if (detailIDComboBox.getSelectedItem() == null) {
            return;
        }

        String detailID = detailIDComboBox.getSelectedItem().toString();
        if (!detailID.equals("")) {
            loadDetailInfo(detailID);
        }
    }//GEN-LAST:event_detailIDComboBoxItemStateChanged

    private void addEquipmentConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEquipmentConfirmButtonActionPerformed
        int amount = 0, price = 0;
        String id = equipmentIDTextField.getText().toUpperCase();
        String name = equipmentNameTextField.getText();
        String status = statusComboBox.getSelectedItem().toString();
        String picture = _imagePath;
        String detailID = detailIDComboBox.getSelectedItem().toString();

        if (_editMode) {
            if (detailIDComboBox.getSelectedItem().toString().equals("")) {
                detailIDAlertLabel.setVisible(true);
                return;
            }

            editEquipment(id, status, detailID);
            JOptionPane.showMessageDialog(null, "Chỉnh sửa thành công");
        }
        else {
            if (!checkValues(id, amountTextField.getText(), detailID)) {
                return;
            }

            amount = Integer.valueOf(amountTextField.getText());
            price = Integer.valueOf(priceTextField.getText());

            int incID = getMaxEquimentID(id);

            for (int i = 0; i < amount; i++) {
                incID++;
                String equipmentID = id + String.format("-%03d", incID);
                _admDb.addEquimentForImport(equipmentID, name, status, price, picture, detailID);
            }
        }
        
        _admDb.setEnabled(true);
        _admDb.loadDatabase();
        this.dispose();
    }//GEN-LAST:event_addEquipmentConfirmButtonActionPerformed

    private void addEquipmentCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEquipmentCancelButtonActionPerformed
        if (_detailUpdated) {
            _admDb.loadDatabase();
        }
        _admDb.setEnabled(true);
        this.dispose();
    }//GEN-LAST:event_addEquipmentCancelButtonActionPerformed

    private AdminDashBoard _admDb = null;
    private String _id;
    private String _imagePath = "";
    private boolean _detailUpdated = false;
    private boolean _editMode = false;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Button addEquipmentCancelButton;
    private Button addEquipmentConfirmButton;
    private javax.swing.JLabel amountAlertLabel;
    private javax.swing.JLabel amountLabel;
    private javax.swing.JTextField amountTextField;
    private javax.swing.JLabel detailIDAlertLabel;
    private javax.swing.JComboBox<String> detailIDComboBox;
    private javax.swing.JLabel detailIDLabel;
    private javax.swing.JLabel equipmentIDAlertLabel;
    private javax.swing.JLabel equipmentIDLabel;
    private javax.swing.JTextField equipmentIDTextField;
    private javax.swing.JLabel equipmentNameLabel;
    private javax.swing.JTextField equipmentNameTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel pictureFieldLabel;
    private javax.swing.JLabel pictureLabel;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JTextField priceTextField;
    private javax.swing.JComboBox<String> statusComboBox;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel supplierLabel;
    private javax.swing.JTextField supplierTextField;
    private javax.swing.JLabel warrantyLabel;
    private javax.swing.JTextField warrantyTextField;
    // End of variables declaration//GEN-END:variables
}
