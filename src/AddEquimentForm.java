
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nguyen Hai Dang
 */
public class AddEquimentForm extends javax.swing.JFrame {

    /**
     * Creates new form AddEquimentForm
     */
    
    public AddEquimentForm(MainMenu parent, String id)
    {
        _mainMenuForm = parent;
        _id = id;
        
        initComponents();     
        initRendererForEdit();
        loadCurrentData();
    }
    
    public AddEquimentForm(ImportForm parent) 
    {
        _importForm = parent;
        
        initComponents();     
        initRenderer();  
    }
    
    private void initRenderer()
    {
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        confirmEditButton.setVisible(false);
        
        setAlertVisible(false);
        loadStatusComboBox();
        loadDetailIDComboBox();
    }
    
    private void initRendererForEdit()
    {
        initRenderer();
        equipmentIDTextField.setText(_id);
        equipmentIDTextField.setEditable(false);
        amountLabel.setVisible(false);
        amountTextField.setVisible(false);
        confirmEditButton.setVisible(true);
        confirmButton.setVisible(false);
    }
    
    private void loadCurrentData()
    {
        String status = "", detailID = "";
        Connection connector = ConnectMysql.getConnectDB();
        String sql = "select * from gym_equipments where id = '"+ _id +"'";
        try {
            ResultSet rs = connector.createStatement().executeQuery(sql);
            if(rs.next())
            {
                status = rs.getString("status");
                detailID = rs.getString("detail_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddEquimentForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(!status.equals("") && !detailID.equals(""))
        {
            for(int i=0;i<statusComboBox.getItemCount();i++)
            {
                if(statusComboBox.getItemAt(i).toString().equals(status))
                {
                    statusComboBox.setSelectedIndex(i);
                }
            }
            
            for(int i=0;i<detailIDComboBox.getItemCount();i++)
            {
                if(detailIDComboBox.getItemAt(i).toString().equals(detailID))
                {
                    detailIDComboBox.setSelectedIndex(i);
                }
            }
        }
    }
            
    private void loadStatusComboBox()
    {
        statusComboBox.removeAllItems();
        statusComboBox.addItem("Đang hoạt động");
        statusComboBox.addItem("Đang bảo trì");
        statusComboBox.addItem("Bị hỏng");
    }
    
    private void loadDetailIDComboBox()
    {
        detailIDComboBox.removeAllItems();
        detailIDComboBox.addItem("");
        
        Connection connector = ConnectMysql.getConnectDB();
        String sql = "select distinct id from equipment_details";
        
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next())
            {
                detailIDComboBox.addItem(rs.getString("id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddEquimentForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadDetailInfo(String detailID)
    {
        String name = "", picture = "", supplier = "";
        int price = 0, warrantyTime = 0, supplierID = 0;
        
        Connection connector = ConnectMysql.getConnectDB();
        String sql  = "select * from equipment_details where id = '"+detailID+"'";
        
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
            Logger.getLogger(AddEquimentForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        sql = "select name from suppliers where id = '"+supplierID+"'";
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            supplier = rs.getString("name");
        } catch (SQLException ex) {
            Logger.getLogger(AddEquimentForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        equipmentNameTextField.setText(name);
        warrantyTextField.setText(warrantyTime+" năm");
        priceTextField.setText(price+"");
        supplierTextField.setText(supplier);
    }
    
    private void editEquipment(String id, String status, String detailID)
    {
        Connection connector = ConnectMysql.getConnectDB();
        String sql = "update gym_equipments set id = ?, status = ?, detail_id = ?, updated_at = ?";
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, status);
            ps.setString(3, detailID);
            ps.setTimestamp(4,  new java.sql.Timestamp(new Date().getTime()));
            ps.execute();
            System.out.println("Chỉnh sửa thành công");
        } catch (SQLException ex) {
            Logger.getLogger(AddEquimentForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int getMaxEquimentID(String equipmentID)
    {
        Connection connector = ConnectMysql.getConnectDB();
        String sql = "select count(id) as countID from gym_equipments where substring(id,1,3) = '" + equipmentID + "'";
        int idCount = 1;
        try {
            ResultSet rs = connector.createStatement().executeQuery(sql);
            if(rs.next())
            {
                idCount = rs.getInt("countID");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddEquimentForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i=0;i<_importForm.checkIDList.size();i++)
        {
            if(equipmentID.equals(_importForm.checkIDList.get(i)))
            {
                idCount++;
            }
        }
        
        return idCount;
    }
    
    private void setAlertVisible(boolean visible)
    {
        equipmentIDAlertLabel.setVisible(visible);
        amountAlertLabel.setVisible(visible);
        detailIDAlertLabel.setVisible(visible);
    }
    
    private boolean checkValues(String id, String amount, String detailID)
    {
        setAlertVisible(false);
        boolean check = true;
        String amountPattern = "\\d{1,2}";
        String idPattern = "[a-zA-Z]{3}";
        
        
        if(!id.matches(idPattern))
        {
            equipmentIDAlertLabel.setVisible(true);
            check = false;
        }
        if(!amountTextField.getText().matches(amountPattern))
        {
            amountAlertLabel.setVisible(true);
            check = false;
        }
        if(detailID.equals(""))
        {
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
        confirmButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        amountLabel = new javax.swing.JLabel();
        amountTextField = new javax.swing.JTextField();
        pictureLabel = new javax.swing.JLabel();
        pictureTextField = new javax.swing.JPanel();
        amountAlertLabel = new javax.swing.JLabel();
        equipmentIDAlertLabel = new javax.swing.JLabel();
        detailIDAlertLabel = new javax.swing.JLabel();
        confirmEditButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        equipmentIDLabel.setText("Mã thiết bị");

        statusLabel.setText("Trạng thái");

        detailIDLabel.setText("Mã loại");

        equipmentNameLabel.setText("Tên thiết bị");

        equipmentNameTextField.setEditable(false);

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

        warrantyLabel.setText("Bảo hành");

        warrantyTextField.setEditable(false);

        confirmButton.setText("Xác nhận");
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Hủy");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        amountLabel.setText("Số lượng");

        amountTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        amountTextField.setText("1");

        pictureLabel.setText("Hình ảnh");

        pictureTextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pictureTextFieldLayout = new javax.swing.GroupLayout(pictureTextField);
        pictureTextField.setLayout(pictureTextFieldLayout);
        pictureTextFieldLayout.setHorizontalGroup(
            pictureTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 170, Short.MAX_VALUE)
        );
        pictureTextFieldLayout.setVerticalGroup(
            pictureTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );

        amountAlertLabel.setForeground(new java.awt.Color(255, 0, 0));
        amountAlertLabel.setText("Nhập số lượng từ 1 - 99");

        equipmentIDAlertLabel.setForeground(new java.awt.Color(255, 0, 0));
        equipmentIDAlertLabel.setText("Nhập mã thiết bị 3 chữ");

        detailIDAlertLabel.setForeground(new java.awt.Color(255, 51, 0));
        detailIDAlertLabel.setText("Chọn 1 mã thiết bị");

        confirmEditButton.setText("Xác nhận");
        confirmEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmEditButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(equipmentIDLabel)
                                    .addComponent(statusLabel)
                                    .addComponent(equipmentNameLabel))
                                .addGap(0, 263, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(confirmEditButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(confirmButton)))
                        .addGap(18, 18, 18)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(priceLabel)
                                    .addComponent(pictureLabel))
                                .addGap(20, 20, 20)
                                .addComponent(pictureTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(supplierLabel)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(supplierTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(equipmentNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(equipmentIDAlertLabel)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(equipmentIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(statusComboBox, 0, 135, Short.MAX_VALUE)
                                            .addComponent(detailIDComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(46, 46, 46)
                                        .addComponent(detailIDAlertLabel))))
                            .addComponent(detailIDLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(240, 240, 240)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(warrantyLabel)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(63, 63, 63)
                                        .addComponent(warrantyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(amountLabel)
                                .addGap(20, 20, 20)
                                .addComponent(amountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(48, 48, 48)
                                .addComponent(amountAlertLabel)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(equipmentIDLabel)
                    .addComponent(equipmentIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(equipmentIDAlertLabel)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusLabel)
                    .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(detailIDLabel)
                    .addComponent(detailIDComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(detailIDAlertLabel))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(equipmentNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(equipmentNameLabel))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierLabel)
                    .addComponent(supplierTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(priceLabel)
                    .addComponent(priceTextField)
                    .addComponent(warrantyLabel)
                    .addComponent(warrantyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pictureLabel)
                    .addComponent(pictureTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(amountLabel)
                    .addComponent(amountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(amountAlertLabel))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(confirmButton)
                    .addComponent(confirmEditButton))
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void detailIDComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_detailIDComboBoxItemStateChanged
        String detailID = detailIDComboBox.getSelectedItem().toString();
        if(!detailID.equals(""))
        {
            loadDetailInfo(detailID);
        }
    }//GEN-LAST:event_detailIDComboBoxItemStateChanged

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonActionPerformed
        int amount = 0, price = 0;
        String id = equipmentIDTextField.getText().toUpperCase();
        String name = equipmentNameTextField.getText();
        String status = statusComboBox.getSelectedItem().toString();
        String picture = "";
        String detailID = detailIDComboBox.getSelectedItem().toString();
        
        if(!checkValues(id, amountTextField.getText(), detailID))
            return;
        
        amount = Integer.valueOf(amountTextField.getText());
        price = Integer.valueOf(priceTextField.getText());
        
        int incID = getMaxEquimentID(id);
        
        for(int i = 0; i < amount; i++)
        {
            incID++;
            String equipmentID = id + String.format("-%03d", incID);
            _importForm.addEquiment(equipmentID, name, status, price, picture, detailID);
        }
        
        this.dispose(); 
    }//GEN-LAST:event_confirmButtonActionPerformed

    private void confirmEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmEditButtonActionPerformed
        if(detailIDComboBox.getSelectedItem().toString().equals(""))
        {
            detailIDAlertLabel.setVisible(true);
            return;
        }
        
        String id = equipmentIDTextField.getText();
        String status = statusComboBox.getSelectedItem().toString();
        String detailID = detailIDComboBox.getSelectedItem().toString();
        
        editEquipment(id, status, detailID);
        _mainMenuForm.loadDatabase();
        this.dispose();
    }//GEN-LAST:event_confirmEditButtonActionPerformed


    ImportForm _importForm = null;
    MainMenu _mainMenuForm = null;
    String _id;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel amountAlertLabel;
    private javax.swing.JLabel amountLabel;
    private javax.swing.JTextField amountTextField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton confirmButton;
    private javax.swing.JButton confirmEditButton;
    private javax.swing.JLabel detailIDAlertLabel;
    private javax.swing.JComboBox<String> detailIDComboBox;
    private javax.swing.JLabel detailIDLabel;
    private javax.swing.JLabel equipmentIDAlertLabel;
    private javax.swing.JLabel equipmentIDLabel;
    private javax.swing.JTextField equipmentIDTextField;
    private javax.swing.JLabel equipmentNameLabel;
    private javax.swing.JTextField equipmentNameTextField;
    private javax.swing.JLabel pictureLabel;
    private javax.swing.JPanel pictureTextField;
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
