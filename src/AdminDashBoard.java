
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author skqist225
 */
public class AdminDashBoard extends javax.swing.JFrame {

    /**
     * Creates new form AdminDashBoard
     */
    Border defaultBorder = BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(45, 53, 60));
    Border borderColor = BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(255, 255, 255));

    JLabel[] menuLabels = new JLabel[8];
    JPanel[] panels = new JPanel[7];
    int hour, minute, second;

    public JPanel getPnl_eqsDetail() {
        return pnl_eqsDetail;
    }

    public JPanel getPnl_suppliers() {
        return pnl_suppliers;
    }

    public int getSelectedTable() {
        return _selectedTable;
    }

    public JLabel getLbl_menuItem_4() {
        return lbl_menuItem_4;
    }

    public JLabel getLbl_menuItem_5() {
        return lbl_menuItem_5;
    }

    public JTable getCategoriesTable() {
        return categoriesTable;
    }

    public JTable getSuppliersTable() {
        return suppliersTable;
    }

    public DeleteValue getDeleter() {
        return _deleter;
    }

    public JTable getEquipmentsTable() {
        return equipmentsTable;
    }

    public JTable getImportDetailsTable() {
        return importDetailsTable;
    }

    public JTable getUsersTable() {
        return usersTable;
    }

    public JButton getBtnSupplierConfirm() {
        return btnSupplierConfirm;
    }

    public Button getBtn_confirmCategories() {
        return btn_confirmCategories;
    }

    public JLabel getLbl_themCTTB() {
        return lbl_themCTTB;
    }

    public JLabel getLbl_themNCC() {
        return lbl_themNCC;
    }

    public void setIsCategoriesUpdate(boolean _isCategoriesUpdate) {
        this._isCategoriesUpdate = _isCategoriesUpdate;
    }

    public int getUserID() {
        return _userID;
    }

    public Button getButton6() {
        return button6;
    }

    public boolean isIsPopupShow() {
        return _isPopupShow;
    }

    public void setIsPopupShow(boolean _isPopupShow) {
        this._isPopupShow = _isPopupShow;
    }

    public AdminDashBoard(int userID, String role) {
        initComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension srcSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(srcSize.width / 2 - getWidth() / 2 - 100, srcSize.height / 2 - getHeight() / 2);

        _userID = userID;
        _role = role;
        _listOfSuppliers = _sC.getSuppliersInfo();

        lbl_appLogo.setIcon(ImageGenerator.ResizeImage(new ImageGenerator().getImageFolderPath() + "/src/icon/appLogo.png", lbl_appLogo));

        Border panelBorder = BorderFactory.createMatteBorder(0, 0, 6, 0, new Color(242, 243, 248));
        pnl_logoAndName.setBorder(panelBorder);

        //populate the menuLabels array        
        menuLabels[0] = lbl_menuItem_1;
        menuLabels[1] = lbl_menuItem_2;
        menuLabels[2] = lbl_menuItem_3;
        menuLabels[3] = lbl_menuItem_4;
        menuLabels[4] = lbl_menuItem_5;
        menuLabels[5] = lbl_menuItem_6;
        menuLabels[6] = lbl_menuItem_7;
        menuLabels[7] = lbl_menuItem_8;

        panels[0] = pnl_dashboard;
        panels[1] = pnl_users;
        panels[2] = pnl_equipments;
        panels[3] = pnl_eqsDetail;
        panels[4] = pnl_suppliers;
        panels[5] = pnl_imports;
        panels[6] = pnl_settings;

        panels[0].setVisible(true);

        for (int i = 1; i < panels.length; i++) {
            panels[i].setVisible(false);
        }

        addActionToLabelsInAppBarPanel();

        _this = this;
        loadUserInfo();
        initTables();

        theader(categoriesTable);
        theader(usersTable);
        theader(suppliersTable);
        theader(importDetailsTable);
        theader(equipmentsTable);
        theader(loginInfoTable);
        theader(importEquipmentTable);

        _eC = new EquipmentDetailsController();
        _sC = new SupplierController();
        _listOfSuppliers = new ArrayList<>();
        _listOfEquipmentDetails = _eC.getListEquipmentDetails();

        initCategoriesAlert();
        initSuppliersAlert();
        btn_confirmCategories.setText("Thêm");
        btnGetImage.setText("Chọn hình ảnh!");

        getSupplierList();
        txtMaNCC.setText(_listOfSuppliers.get(_listOfSuppliers.size() - 1).getSupplierId() + 1 + "");

        txtMaNCC.setEditable(false);
        txtMaNCC.setFocusable(false);
        txtTenNCC.requestFocus();

        initRenderer();
        suppliersTable.setRowHeight(40);
        importDetailsTable.setRowHeight(40);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Calendar cal = Calendar.getInstance();
                    hour = cal.get(Calendar.HOUR_OF_DAY);
                    minute = cal.get(Calendar.MINUTE);
                    second = cal.get(Calendar.SECOND);

                    SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm:ss");
                    Date date = cal.getTime();
                    String time24 = sdf24.format(date);

                    lbl_time.setText("     " + time24);
                }
            }
        }).start();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        lbl_date.setText("     " + sdf.format(date));
        lbl_close_hover.setIcon(ImageGenerator.ResizeImage(new ImageGenerator().getImageFolderPath() + "/src/icon/close3.png", lbl_close_hover));

        if (!role.equals(ADMIN_ROLE)) {
            signupButton.setVisible(false);
            showLoginInfoButton.setVisible(false);
        }
    }

    //////////////////////////|///////////////|///////////////////////////////
    //========================| ADD EQUIPMENT | ==============================
    //////////////////////////V///////////////V///////////////////////////////
    private void initRenderer() {
        loadStatusComboBox();
        loadDetailIDComboBox();
    }

    public void initRendererForEdit(String id) {
        initRenderer();
        detailIDComboBox.removeItemAt(0);
        loadCurrentData(id);
        equipmentIDTextField.setText(id);
    }

    public void refreshEquipmentEditBox() {
        initRenderer();
        equipmentIDTextField.setText("");
        statusComboBox.setSelectedIndex(0);
        detailIDComboBox.setSelectedIndex(0);
        equipmentNameTextField.setText("");
        supplierTextField.setText("");
        priceTextField.setText("");
        warrantyTextField.setText("");
        pictureFieldLabel.setIcon(null);
    }

    private void loadCurrentData(String id) {
        String status = "", detailID = "";
        Connection connector = ConnectMysql.getConnectDB();
        String sql = "select * from gym_equipments where id = '" + id + "'";
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

    public void loadDetailIDComboBox() {
        detailIDComboBox.removeAllItems();
        detailIDComboBox.addItem("");

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

    //////////////////////////^///////////////^///////////////////////////////
    //========================| ADD EQUIPMENT | ==============================
    //////////////////////////|///////////////|///////////////////////////////
    //////////////////////////|///////////////|///////////////////////////////
    //========================| IMPORT DETAIL | ==============================
    //////////////////////////V///////////////V///////////////////////////////
    public Vector<String> _checkEquipmentIDList = new Vector<>();
    private int _importCount = 0;

    public void addEquimentForImport(String id, String name, String status, int price, String picture, String detailID) {
        DefaultTableModel tableModel = (DefaultTableModel) importEquipmentTable.getModel();
        boolean isOdd = _importCount % 2 == 0 ? true : false;

        _imgGenerator.ImageColumnSetting(importEquipmentTable);

        tableModel.addRow(new Object[]{id, name, status, price,
            _imgGenerator.createLabel(picture, isOdd),
            detailID});
        _checkEquipmentIDList.add(id.substring(0, 3));
        _importCount++;
    }

    private void loadImportData() {
        DefaultTableModel tableModel = (DefaultTableModel) importEquipmentTable.getModel();
        tableModel.setNumRows(0);
        loadImportID();
        importDateTextField.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        importEditButton.setEnabled(false);
        importDeleteButton.setEnabled(false);
        _checkEquipmentIDList.clear();
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

    //////////////////////////^///////////////^///////////////////////////////
    //========================| IMPORT DETAIL | ==============================
    //////////////////////////|///////////////|///////////////////////////////
    public void fillOutCategoriesInfo(String id) {
        int dem = 0;
        _listOfEquipmentDetails = _eC.getListEquipmentDetails();

        for (Equipment_Details eD : _listOfEquipmentDetails) {
            if (eD.getId().equals(id)) {
                System.out.println("true");
                for (Supplier s : _listOfSuppliers) {
                    if (s.getSupplierId() == eD.getSupplier_id()) {
                        cbNhaCungCap.setSelectedIndex(dem);
                        break;
                    }
                    dem++;
                }

                txtMaChiTietTB.setText(eD.getId());
                txtMaChiTietTB.setEditable(false);
                txtMaChiTietTB.setFocusable(false);
                txtTenThietBi.setText(eD.getName());
                txtTenThietBi.requestFocus();

                if (eD.getPicture() != null) {
                    btnGetImage.setText(eD.getPicture());
                    oldPicutre = eD.getPicture();
                }
                else {
                    btnGetImage.setText("Chọn hình ảnh!");
                }

                txtGia.setText(eD.getPrice() + "");
                txtThoiGianBH.setText(eD.getWarranty_time() + "");
                btn_confirmCategories.setText("Cập nhật");
                break;
            }
        }

    }

    public void getSupplierList() {
        cbNhaCungCap.removeAllItems();
        _listOfSuppliers = _sC.getSuppliersInfo();
        _listOfSuppliers.forEach((supplier) -> {
            cbNhaCungCap.addItem("Tên: " + supplier.getName() + ", Địa chỉ: " + supplier.getAddress());
        });
        cbNhaCungCap.addItem("Thêm nhà cung cấp");
    }

    public void loadDatabase() {
        loadUserInfos();
        loadSuppliers();
        loadEquipmentDetails();
        loadEquipments();
        loadImportDetails();
        processMoneyTaken();

        if (_role.equals(ADMIN_ROLE)) {
            loadLoginInfos();
        }
    }

    public void setLabelBackground(JLabel label) {
        for (JLabel menuLabel : menuLabels) {
            menuLabel.setBackground(new Color(45, 53, 60));
            menuLabel.setForeground(new Color(255, 255, 255));
        }

        label.setBackground(new Color(255, 255, 255));
        label.setForeground(new Color(45, 53, 60));
    }

    public void addActionToLabelsInAppBarPanel() {
        for (Component component : menuLabels) {
            if (component instanceof JLabel) {

                JLabel label = (JLabel) component;

                label.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent me) {
                        setLabelBackground(label);
                        switch (label.getText().trim()) {
                            case "Trang chủ":
                                showPanel(pnl_dashboard);
                                if (_sideBarFunctionFrm != null) {
                                    _sideBarFunctionFrm.getFilterComboBox().removeAllItems();
                                }
                                break;
                            case "Nhân viên":
                                showPanel(pnl_users);
                                _selectedTable = 3;
                                if (_sideBarFunctionFrm != null) {
                                    _sideBarFunctionFrm.initFilterComboBox(_selectedTable);
                                }
                                break;
                            case "Thiết bị":
                                showPanel(pnl_equipments);
                                _selectedTable = 1;
                                if (_sideBarFunctionFrm != null) {
                                    _sideBarFunctionFrm.initFilterComboBox(_selectedTable);
                                }

                                break;
                            case "Chi tiết thiết bị":
                                showPanel(pnl_eqsDetail);
                                _selectedTable = 4;
                                if (_sideBarFunctionFrm != null) {
                                    _sideBarFunctionFrm.initFilterComboBox(_selectedTable);
                                }

                                break;
                            case "Nhà cung cấp":
                                showPanel(pnl_suppliers);
                                _selectedTable = 5;
                                if (_sideBarFunctionFrm != null) {
                                    _sideBarFunctionFrm.initFilterComboBox(_selectedTable);
                                }
                                break;
                            case "Đơn nhập":
                                showPanel(pnl_imports);
                                _selectedTable = 2;
                                if (_sideBarFunctionFrm != null) {
                                    _sideBarFunctionFrm.initFilterComboBox(_selectedTable);
                                }
                                break;
                            case "Thống kê":
                                if (!_isPopupShow) {
                                    DrawGraph.createAndShowGui();
                                    new PieChart(_this).start();
                                    _isPopupShow = true;
                                }
                                break;
                            case "Cài đặt":
                                showPanel(pnl_settings);
                                break;
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent me) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent me) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent me) {
                        label.setBorder(borderColor);
                    }

                    @Override
                    public void mouseExited(MouseEvent me) {
                        label.setBorder(defaultBorder);
                    }
                });
            }
        }
    }

    public void initCategoriesAlert() {
        initAlertLabel(alertMCTTB);
        initAlertLabel(alertTTB);
        initAlertLabel(alertGia);
        initAlertLabel(alertNCC);
        initAlertLabel(alertTGBH);
    }

    public final void initSuppliersAlert() {
        initAlertLabel(alertTNCC);
        initAlertLabel(alertDCNCC);
        initAlertLabel(alertSDTNCC);
    }

    public void initAlertLabel(JLabel label) {
        label.setText("");
        label.setSize(0, 0);
    }

    private void createAlert(JLabel label, String alertContent) {
        label.setSize(alertContent.length(), 17);
        label.setText(alertContent);
        label.setVisible(true);
    }

    public void showPanel(JPanel panel) {
        for (JPanel pnl : panels) {
            pnl.setVisible(false);
        }
        panel.setVisible(true);
    }

    public void resetCategoriesField() {
        txtMaChiTietTB.setText("");
        txtTenThietBi.setText("");
        btnGetImage.setText("Chọn hình ảnh!");
        txtGia.setText("");
        txtThoiGianBH.setText("");
        cbNhaCungCap.setSelectedIndex(0);
    }

    public void initTables() {
        loginInfoScrollPane.setVisible(false);
        loginInfoPanel.setVisible(false);
        loadDatabase();
        loadImportData();
    }

    private void loadLoginInfos() {
        DefaultTableModel tableModel = (DefaultTableModel) loginInfoTable.getModel();
        tableModel.setNumRows(0);

        Connection connector = ConnectMysql.getConnectDB();
        String sql = "select * from login_info";
        RoleController controller = new RoleController();
        Vector vector;
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                vector = new Vector();
                vector.add(rs.getString("userName"));
                vector.add(rs.getString("password"));
                vector.add(rs.getString("userId"));
                vector.add(controller.getRole(rs.getInt("role_id")));
                tableModel.addRow(vector);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(AdminDashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadUserInfos() {
        DefaultTableModel tableModel = (DefaultTableModel) usersTable.getModel();
        tableModel.setNumRows(0);
        _imgGenerator.ImageColumnSetting(usersTable, "Chân dung");

        List<User> listOfUser = new UserController().getUsersInfo();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        boolean isOdd = false;
        int dem = 0;

        for (User user : listOfUser) {
            isOdd = dem % 2 != 0;
            tableModel.addRow(new Object[]{
                user.getUserID(), user.getName(),
                formatter.format(user.getBirthday()),
                user.getEmail(), user.getContactNumber(),
                _imgGenerator.createLabel(user.getProfilePicture(), isOdd),
                formatter.format(user.getUpdatedAt())
            });
            dem++;
        }
    }

    private void loadUserInfo() {
        Connection connectDB = ConnectMysql.getConnectDB();
        String userQuery = "SELECT * FROM `users` WHERE id = " + _userID;

        try {
            ResultSet rs = connectDB.createStatement().executeQuery(userQuery);
            rs.next();
            fullNameLb.setText(rs.getString("lastName") + " " + rs.getString("firstName"));
            birthDayLb.setText(_format.format(rs.getDate("birthDay")));
            emailLb.setText(rs.getString("email"));
            contactLb.setText(rs.getString("contactNumber"));
            createLb.setText(_format.format(rs.getDate("created_at")));
            updateLb.setText(_format.format(rs.getDate("updated_at")));
        } catch (SQLException ex) {
            Logger.getLogger(AdminDashBoard.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadSuppliers() {
        db_txtNCC.setText(countAllRecordFromTable("SELECT COUNT(*) FROM suppliers") + "");
        DefaultTableModel tableModel = (DefaultTableModel) suppliersTable.getModel();
        tableModel.setNumRows(0);

        _listOfSuppliers = _sC.getSuppliersInfo();

        for (Supplier supplier : _listOfSuppliers) {
            tableModel.addRow(new Object[]{supplier.getSupplierId(), supplier.getName(),
                supplier.getAddress(), supplier.getPhoneNumber()});
        }
    }

    class KeyValue {

        String detail_id;
        int soLuong;

        public String getDetail_id() {
            return detail_id;
        }

        public int getSoLuong() {
            return soLuong;
        }

        public KeyValue(String detail_id, int soLuong) {
            this.detail_id = detail_id;
            this.soLuong = soLuong;
        }

    }

    private int processMoneyTaken() {
        int money = 0;
        List<KeyValue> keyVal = new ArrayList<>();
        String sql = "SELECT detail_id, COUNT(detail_id) FROM gym_equipments,equipment_details WHERE detail_id = equipment_details.id GROUP BY detail_id";
        String sql2 = "SELECT price from equipment_details WHERE id = ?";
        Connection connector = null;
        try {
            connector = ConnectMysql.getConnectDB();
            PreparedStatement ps = connector.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KeyValue kV = new KeyValue(rs.getString(1), rs.getInt(2));
                keyVal.add(kV);
            }
            PreparedStatement ps2 = connector.prepareStatement(sql2);
            for (KeyValue kV : keyVal) {
                try {
                    ps2.setString(1, kV.getDetail_id());
                    ResultSet rs2 = ps2.executeQuery();
                    if (rs2.next()) {
                        money += rs2.getInt(1) * kV.getSoLuong();
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }

        db_STC.setText(money + "VND");

        return money;
    }

    private void loadImportDetails() {
        db_txtDNH.setText(countAllRecordFromTable("SELECT COUNT(*) FROM import_details") + "");
        DefaultTableModel tableModel = (DefaultTableModel) importDetailsTable.getModel();
        tableModel.setNumRows(0);

        SimpleDateFormat dateFormater = new SimpleDateFormat("dd-MM-yyyy");
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
                vector.add(dateFormater.format(rs.getDate("date_import")));
                tableModel.addRow(vector);
            }
            rs.close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(AdminDashBoard.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int countAllRecordFromTable(String statement) {
        String sql = statement;
        int numberOfTB = 0;
        try {
            Connection connector = ConnectMysql.getConnectDB();
            PreparedStatement s = connector.prepareStatement(sql);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                numberOfTB = rs.getInt(1);
            }
        } catch (Exception e) {
        }

        return numberOfTB;
    }

    private void loadEquipments() {
        db_txtTb.setText(countAllRecordFromTable("SELECT COUNT(*) FROM gym_equipments") + "");
        DefaultTableModel tableModel = (DefaultTableModel) equipmentsTable.getModel();
        tableModel.setNumRows(0);
        _imgGenerator.ImageColumnSetting(equipmentsTable);

        SimpleDateFormat dateFormater = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Connection connector = ConnectMysql.getConnectDB();
        String sql = "select * from gym_equipments";
        Vector vector;
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int dem = 1;
            boolean isOdd;
            while (rs.next()) {
                isOdd = dem % 2 != 0;
                String imagePath = getEquipmentImage(rs.getString("detail_id"));

                vector = new Vector();
                vector.add(rs.getString("id"));
                vector.add(rs.getString("status"));
                vector.add(_imgGenerator.createLabel(imagePath, isOdd));
                vector.add(rs.getString("detail_id"));
                vector.add(rs.getInt("import_id"));
                vector.add(dateFormater.format(rs.getTimestamp("updated_at")));
                tableModel.addRow(vector);
                dem++;
            }
            rs.close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(AdminDashBoard.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        equipmentsTable.getRowSorter().toggleSortOrder(1);
        equipmentsTable.getRowSorter().toggleSortOrder(1);
    }

    private void loadEquipmentDetails() {
        db_txtLTT.setText(countAllRecordFromTable("SELECT COUNT(*) FROM equipment_details") + "");
        DefaultTableModel tblEquipDetails = (DefaultTableModel) categoriesTable.getModel();
        tblEquipDetails.setRowCount(0);
        List<Equipment_Details> lED = _eC.getListEquipmentDetails();
        _imgGenerator.ImageColumnSetting(categoriesTable);

        int dem = 1;
        boolean isOdd;
        for (Equipment_Details equipment_info : lED) {
            isOdd = dem % 2 != 0;

            tblEquipDetails.addRow(new Object[]{equipment_info.getId(), equipment_info.getName(),
                equipment_info.getPicture() == null ? _imgGenerator.createLabel("Không có hình ảnh", isOdd) : _imgGenerator.createLabel(equipment_info.getPicture(), isOdd),
                equipment_info.getPrice(), equipment_info.getWarranty_time() + " năm",
                _listOfSuppliers.get(equipment_info.getSupplier_id() - 1).getName(),});
            dem++;
        }
    }

    private String getEquipmentImage(String id) {
        Connection connector = ConnectMysql.getConnectDB();
        String sql = "select * from equipment_details where id = ?";
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("picture");

            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminDashBoard.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private void tableImageCellCallback(MouseEvent evt, javax.swing.JTable table) {
        List<Equipment_Details> lED = _eC.getListEquipmentDetails();
        table = (JTable) evt.getSource();
        Point p = evt.getPoint();
        int row = table.rowAtPoint(p);
        int column = table.columnAtPoint(p);
        int imageColumn = 2;

        String imageFolderPath = _imageFolderPath;
        String imagePath = null;

        if (table == usersTable) {
            imageFolderPath = new File("").getAbsolutePath().concat("/src/profile/");
            imageColumn = 5;
        }

        if (column == imageColumn) {
            if (table == equipmentsTable) {
                imagePath = getEquipmentImage(table.getValueAt(row, 3).toString());
            }
            else if (table == categoriesTable) {
                imagePath = lED.get(row).getPicture();
            }

            imagePath = imagePath == null ? "" : imagePath;
            if (!imagePath.equals("")) {
                ShowImageFrame sIF = ShowImageFrame.getObj();
                sIF.setVisible(true);
                String image = imageFolderPath + imagePath;
                sIF.getShowImageLbl().setIcon(ImageGenerator.ResizeImage(image, sIF.getShowImageLbl()));
            }
        }

    }

    private void theader(JTable table) {
        JTableHeader thead = table.getTableHeader();
        thead.setForeground(new Color(29, 94, 74));
        thead.setFont(new Font("Tahome", Font.BOLD, 16));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer centerRenderer1;
        centerRenderer1 = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        centerRenderer1.setHorizontalAlignment(JLabel.CENTER);

        for (int x = 0; x < table.getColumnCount(); x++) {
            if (!"Hình ảnh".equals(table.getColumnName(x))) {
                table.getColumnModel().getColumn(x).setCellRenderer(centerRenderer);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator3 = new javax.swing.JSeparator();
        pnl_container = new javax.swing.JPanel();
        pnl_appBar = new javax.swing.JPanel();
        pnl_logoAndName = new javax.swing.JPanel();
        lbl_appLogo = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lbl_menuItem_2 = new javax.swing.JLabel();
        lbl_menuItem_3 = new javax.swing.JLabel();
        lbl_menuItem_4 = new javax.swing.JLabel();
        lbl_menuItem_5 = new javax.swing.JLabel();
        lbl_menuItem_1 = new javax.swing.JLabel();
        lbl_menuItem_6 = new javax.swing.JLabel();
        lbl_menuItem_7 = new javax.swing.JLabel();
        lbl_menuItem_8 = new javax.swing.JLabel();
        lbl_time = new javax.swing.JLabel();
        lbl_date = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        pnl_function = new javax.swing.JPanel();
        lbl_close_hover = new javax.swing.JLabel();
        button6 = new Button();
        pnl_dashboard = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        db_txtTb = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        db_txtLTT = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        db_txtNCC = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        db_STC = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        db_TBNTT = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        db_txtDNH = new javax.swing.JLabel();
        pnl_users = new javax.swing.JPanel();
        usersScrollPane = new javax.swing.JScrollPane();
        usersTable = new javax.swing.JTable();
        pnl_equipments = new javax.swing.JPanel();
        equipmentsScrollPane = new javax.swing.JScrollPane();
        equipmentsTable = new javax.swing.JTable();
        jPanel1 = new JPanelGradient2();
        pictureLabel = new javax.swing.JLabel();
        equipmentIDLabel = new javax.swing.JLabel();
        equipmentIDTextField = new javax.swing.JTextField();
        pictureFieldLabel = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        detailIDLabel = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
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
        EquipmentEditConfirmButton = new Button();
        EquipmentRefreshButton = new Button();
        pnl_settings = new javax.swing.JPanel();
        loginInfoPanel = new javax.swing.JPanel();
        loginInfoScrollPane = new javax.swing.JScrollPane();
        loginInfoTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        birthDayLb = new javax.swing.JLabel();
        fullNameLb = new javax.swing.JLabel();
        emailLb = new javax.swing.JLabel();
        contactLb = new javax.swing.JLabel();
        createLb = new javax.swing.JLabel();
        updateLb = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        button1 = new Button();
        signupButton = new Button();
        profilePictureLabel = new javax.swing.JLabel();
        showLoginInfoButton = new Button();
        pnl_eqsDetail = new javax.swing.JPanel();
        txtTenThietBi = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        alertMCTTB = new javax.swing.JLabel();
        txtGia = new javax.swing.JTextField();
        alertTTB = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        alertNCC = new javax.swing.JLabel();
        alertGia = new javax.swing.JLabel();
        lbl_themCTTB = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        alertTGBH = new javax.swing.JLabel();
        txtThoiGianBH = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtMaChiTietTB = new javax.swing.JTextField();
        cbNhaCungCap = new javax.swing.JComboBox<>();
        jLabel31 = new javax.swing.JLabel();
        btnGetImage = new Button();
        categoriesScrollPane = new javax.swing.JScrollPane();
        categoriesTable = new javax.swing.JTable();
        button5 = new Button();
        btn_confirmCategories = new Button();
        pnl_suppliers = new javax.swing.JPanel();
        suppliersScrollPane = new javax.swing.JScrollPane();
        suppliersTable = new javax.swing.JTable();
        btnSupplierConfirm = new javax.swing.JButton();
        btnRefreshNCC = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        lbl_themNCC = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        alertTNCC = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        alertDCNCC = new javax.swing.JLabel();
        txtMaNCC = new javax.swing.JTextField();
        alertSDTNCC = new javax.swing.JLabel();
        txtTenNCC = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        txtDiaChiNCC = new javax.swing.JTextField();
        pnl_imports = new javax.swing.JPanel();
        importDetailsScrollPane = new javax.swing.JScrollPane();
        importDetailsTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        importIDLabel = new javax.swing.JLabel();
        importIDTextField = new javax.swing.JTextField();
        importAddButton = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        importEquipmentTable = new javax.swing.JTable();
        importDateLabel = new javax.swing.JLabel();
        importDateTextField = new javax.swing.JTextField();
        importEditButton = new javax.swing.JButton();
        importDeleteButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        saveImportButton = new Button();
        refreshImportButton = new Button();
        loadImportButton = new Button();
        makeImportButton = new Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnl_container.setBackground(new java.awt.Color(255, 255, 255));

        pnl_appBar.setBackground(new java.awt.Color(45, 53, 60));

        pnl_logoAndName.setBackground(new java.awt.Color(45, 53, 60));
        pnl_logoAndName.setPreferredSize(new java.awt.Dimension(230, 105));

        lbl_appLogo.setBackground(new java.awt.Color(45, 53, 60));
        lbl_appLogo.setOpaque(true);

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(30, 215, 96));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("QUẢN LÝ THIẾT BỊ");

        jLabel15.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(30, 215, 96));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("PHÒNG GYM");

        javax.swing.GroupLayout pnl_logoAndNameLayout = new javax.swing.GroupLayout(pnl_logoAndName);
        pnl_logoAndName.setLayout(pnl_logoAndNameLayout);
        pnl_logoAndNameLayout.setHorizontalGroup(
            pnl_logoAndNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_logoAndNameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_appLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_logoAndNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)))
        );
        pnl_logoAndNameLayout.setVerticalGroup(
            pnl_logoAndNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_logoAndNameLayout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addGroup(pnl_logoAndNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_logoAndNameLayout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbl_appLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        lbl_menuItem_2.setBackground(new java.awt.Color(45, 53, 60));
        lbl_menuItem_2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_menuItem_2.setForeground(new java.awt.Color(255, 255, 255));
        lbl_menuItem_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/profile.png"))); // NOI18N
        lbl_menuItem_2.setText("     Nhân viên");
        lbl_menuItem_2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_menuItem_2.setOpaque(true);

        lbl_menuItem_3.setBackground(new java.awt.Color(45, 53, 60));
        lbl_menuItem_3.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_menuItem_3.setForeground(new java.awt.Color(255, 255, 255));
        lbl_menuItem_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/punching-bag.png"))); // NOI18N
        lbl_menuItem_3.setText("     Thiết bị");
        lbl_menuItem_3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_menuItem_3.setOpaque(true);

        lbl_menuItem_4.setBackground(new java.awt.Color(45, 53, 60));
        lbl_menuItem_4.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_menuItem_4.setForeground(new java.awt.Color(255, 255, 255));
        lbl_menuItem_4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/details.png"))); // NOI18N
        lbl_menuItem_4.setText("     Chi tiết thiết bị");
        lbl_menuItem_4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_menuItem_4.setOpaque(true);

        lbl_menuItem_5.setBackground(new java.awt.Color(45, 53, 60));
        lbl_menuItem_5.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_menuItem_5.setForeground(new java.awt.Color(255, 255, 255));
        lbl_menuItem_5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/inventory.png"))); // NOI18N
        lbl_menuItem_5.setText("     Nhà cung cấp");
        lbl_menuItem_5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_menuItem_5.setOpaque(true);

        lbl_menuItem_1.setBackground(new java.awt.Color(45, 53, 60));
        lbl_menuItem_1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_menuItem_1.setForeground(new java.awt.Color(255, 255, 255));
        lbl_menuItem_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/business-report.png"))); // NOI18N
        lbl_menuItem_1.setText("     Trang chủ");
        lbl_menuItem_1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_menuItem_1.setOpaque(true);

        lbl_menuItem_6.setBackground(new java.awt.Color(45, 53, 60));
        lbl_menuItem_6.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_menuItem_6.setForeground(new java.awt.Color(255, 255, 255));
        lbl_menuItem_6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/import.png"))); // NOI18N
        lbl_menuItem_6.setText("     Đơn nhập");
        lbl_menuItem_6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_menuItem_6.setOpaque(true);

        lbl_menuItem_7.setBackground(new java.awt.Color(45, 53, 60));
        lbl_menuItem_7.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_menuItem_7.setForeground(new java.awt.Color(255, 255, 255));
        lbl_menuItem_7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/settings.png"))); // NOI18N
        lbl_menuItem_7.setText("     Cài đặt");
        lbl_menuItem_7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_menuItem_7.setOpaque(true);
        lbl_menuItem_7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_menuItem_7MouseClicked(evt);
            }
        });

        lbl_menuItem_8.setBackground(new java.awt.Color(45, 53, 60));
        lbl_menuItem_8.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_menuItem_8.setForeground(new java.awt.Color(255, 255, 255));
        lbl_menuItem_8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/analytics.png"))); // NOI18N
        lbl_menuItem_8.setText("     Thống kê");
        lbl_menuItem_8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_menuItem_8.setOpaque(true);
        lbl_menuItem_8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_menuItem_8MouseClicked(evt);
            }
        });

        lbl_time.setBackground(new java.awt.Color(45, 53, 60));
        lbl_time.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_time.setForeground(new java.awt.Color(255, 255, 255));
        lbl_time.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_time.setText("   Thời gian");
        lbl_time.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_time.setOpaque(true);
        lbl_time.setPreferredSize(new java.awt.Dimension(98, 26));

        lbl_date.setBackground(new java.awt.Color(45, 53, 60));
        lbl_date.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_date.setForeground(new java.awt.Color(255, 255, 255));
        lbl_date.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_date.setText("   Ngày");
        lbl_date.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbl_date.setOpaque(true);
        lbl_date.setPreferredSize(new java.awt.Dimension(62, 26));

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/clock.png"))); // NOI18N

        javax.swing.GroupLayout pnl_appBarLayout = new javax.swing.GroupLayout(pnl_appBar);
        pnl_appBar.setLayout(pnl_appBarLayout);
        pnl_appBarLayout.setHorizontalGroup(
            pnl_appBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_logoAndName, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
            .addComponent(lbl_menuItem_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_menuItem_5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_menuItem_3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_menuItem_4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_menuItem_7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_menuItem_6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_menuItem_8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_appBarLayout.createSequentialGroup()
                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addGroup(pnl_appBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_time, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(lbl_date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(lbl_menuItem_1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnl_appBarLayout.setVerticalGroup(
            pnl_appBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_appBarLayout.createSequentialGroup()
                .addComponent(pnl_logoAndName, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_menuItem_1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_menuItem_2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_menuItem_3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_menuItem_4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_menuItem_5, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_menuItem_6, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_menuItem_8, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_menuItem_7, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addGroup(pnl_appBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnl_appBarLayout.createSequentialGroup()
                        .addComponent(lbl_time, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        pnl_function.setBackground(new java.awt.Color(45, 53, 60));
        pnl_function.setPreferredSize(new java.awt.Dimension(1200, 115));

        lbl_close_hover.setBackground(new java.awt.Color(45, 53, 60));
        lbl_close_hover.setForeground(new java.awt.Color(255, 255, 255));
        lbl_close_hover.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_close_hover.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_close_hoverMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_close_hoverMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbl_close_hoverMouseExited(evt);
            }
        });

        button6.setBackground(new java.awt.Color(195, 20, 50));
        button6.setForeground(new java.awt.Color(255, 255, 255));
        button6.setText("Chức năng");
        button6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        button6.setGradientBackgroundColor(new java.awt.Color(36, 11, 54));
        button6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button6.setRounded(true);
        button6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_functionLayout = new javax.swing.GroupLayout(pnl_function);
        pnl_function.setLayout(pnl_functionLayout);
        pnl_functionLayout.setHorizontalGroup(
            pnl_functionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_functionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_close_hover, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnl_functionLayout.createSequentialGroup()
                .addGap(1102, 1102, 1102)
                .addComponent(button6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnl_functionLayout.setVerticalGroup(
            pnl_functionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_functionLayout.createSequentialGroup()
                .addComponent(lbl_close_hover, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 17, Short.MAX_VALUE)
                .addComponent(button6, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnl_dashboard.setBackground(new java.awt.Color(222, 226, 230));

        jPanel3.setBackground(new java.awt.Color(241, 90, 34));
        jPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel3MouseClicked(evt);
            }
        });

        jLabel10.setBackground(new java.awt.Color(230, 126, 34));
        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Thiết Bị");
        jLabel10.setOpaque(true);

        db_txtTb.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        db_txtTb.setForeground(new java.awt.Color(255, 255, 255));
        db_txtTb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        db_txtTb.setText("20");

        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("popup Info (later) : hover");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(db_txtTb, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(db_txtTb, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(68, 108, 179));
        jPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
        });

        jLabel9.setBackground(new java.awt.Color(65, 131, 215));
        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Loại Thiết Bị");
        jLabel9.setOpaque(true);

        db_txtLTT.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        db_txtLTT.setForeground(new java.awt.Color(255, 255, 255));
        db_txtLTT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        db_txtLTT.setText("10");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(db_txtLTT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(db_txtLTT, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(3, 166, 120));
        jPanel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel5MouseClicked(evt);
            }
        });

        jLabel11.setBackground(new java.awt.Color(35, 203, 167));
        jLabel11.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Nhà Cung Cấp");
        jLabel11.setOpaque(true);

        db_txtNCC.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        db_txtNCC.setForeground(new java.awt.Color(255, 255, 255));
        db_txtNCC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        db_txtNCC.setText("10");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(db_txtNCC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(db_txtNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 56, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(247, 202, 24));
        jPanel7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel12.setBackground(new java.awt.Color(245, 229, 27));
        jLabel12.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Số tiền chi ");
        jLabel12.setOpaque(true);

        db_STC.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        db_STC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        db_STC.setText("400.000.000 VND");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(db_STC, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(db_STC, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(150, 54, 148));
        jPanel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel13.setBackground(new java.awt.Color(142, 68, 173));
        jLabel13.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Thiết bị nhập trong tháng");
        jLabel13.setOpaque(true);

        db_TBNTT.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        db_TBNTT.setForeground(new java.awt.Color(255, 255, 255));
        db_TBNTT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        db_TBNTT.setText("10");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(db_TBNTT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(db_TBNTT, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(108, 122, 137));
        jPanel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel8MouseClicked(evt);
            }
        });

        jLabel14.setBackground(new java.awt.Color(103, 128, 159));
        jLabel14.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Đơn Nhập Hàng");
        jLabel14.setOpaque(true);

        db_txtDNH.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        db_txtDNH.setForeground(new java.awt.Color(255, 255, 255));
        db_txtDNH.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        db_txtDNH.setText("10");
        db_txtDNH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                db_txtDNHMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(db_txtDNH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(db_txtDNH, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnl_dashboardLayout = new javax.swing.GroupLayout(pnl_dashboard);
        pnl_dashboard.setLayout(pnl_dashboardLayout);
        pnl_dashboardLayout.setHorizontalGroup(
            pnl_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_dashboardLayout.createSequentialGroup()
                .addContainerGap(250, Short.MAX_VALUE)
                .addGroup(pnl_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(pnl_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(73, 73, 73)
                .addGroup(pnl_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(195, 195, 195))
        );
        pnl_dashboardLayout.setVerticalGroup(
            pnl_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_dashboardLayout.createSequentialGroup()
                .addContainerGap(229, Short.MAX_VALUE)
                .addGroup(pnl_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnl_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(pnl_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(170, 170, 170))
        );

        pnl_users.setBackground(new java.awt.Color(255, 255, 255));

        usersTable.setAutoCreateRowSorter(true);
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
        usersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                usersTableMouseClicked(evt);
            }
        });
        usersScrollPane.setViewportView(usersTable);

        javax.swing.GroupLayout pnl_usersLayout = new javax.swing.GroupLayout(pnl_users);
        pnl_users.setLayout(pnl_usersLayout);
        pnl_usersLayout.setHorizontalGroup(
            pnl_usersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_usersLayout.createSequentialGroup()
                .addGap(180, 180, 180)
                .addComponent(usersScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(357, Short.MAX_VALUE))
        );
        pnl_usersLayout.setVerticalGroup(
            pnl_usersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_usersLayout.createSequentialGroup()
                .addGap(148, 148, 148)
                .addComponent(usersScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(194, Short.MAX_VALUE))
        );

        pnl_equipments.setBackground(new java.awt.Color(255, 255, 255));

        equipmentsTable.setAutoCreateRowSorter(true);
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
        equipmentsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                equipmentsTableMouseClicked(evt);
            }
        });
        equipmentsScrollPane.setViewportView(equipmentsTable);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pictureLabel.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        pictureLabel.setText("Hình ảnh:");
        jPanel1.add(pictureLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 482, -1, -1));

        equipmentIDLabel.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        equipmentIDLabel.setText("Mã thiết bị:");
        jPanel1.add(equipmentIDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 54, -1, -1));

        equipmentIDTextField.setEditable(false);
        jPanel1.add(equipmentIDTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 49, 247, 37));

        pictureFieldLabel.setBackground(new java.awt.Color(204, 204, 255));
        pictureFieldLabel.setOpaque(true);
        jPanel1.add(pictureFieldLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 482, 230, 140));

        statusLabel.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        statusLabel.setText("Trạng thái:");
        jPanel1.add(statusLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 134, -1, -1));

        detailIDLabel.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        detailIDLabel.setText("Mã loại:");
        jPanel1.add(detailIDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 201, -1, -1));

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(51, 51, 255));
        jLabel32.setText("THÊM THIẾT BỊ");
        jPanel1.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(169, 11, -1, -1));

        equipmentNameLabel.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        equipmentNameLabel.setText("Tên thiết bị:");
        jPanel1.add(equipmentNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 280, -1, -1));

        equipmentNameTextField.setEditable(false);
        jPanel1.add(equipmentNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 274, 247, 40));

        statusComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(statusComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 127, 135, 40));

        detailIDComboBox.setModel(new javax.swing.DefaultComboBoxModel<>());
        detailIDComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                detailIDComboBoxItemStateChanged(evt);
            }
        });
        detailIDComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailIDComboBoxActionPerformed(evt);
            }
        });
        jPanel1.add(detailIDComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 195, 135, 38));

        supplierLabel.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        supplierLabel.setText("Nhà cung cấp:");
        supplierLabel.setToolTipText("");
        jPanel1.add(supplierLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 350, -1, -1));

        supplierTextField.setEditable(false);
        supplierTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierTextFieldActionPerformed(evt);
            }
        });
        jPanel1.add(supplierTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 344, 247, 40));

        priceLabel.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        priceLabel.setText("Giá:");
        jPanel1.add(priceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 419, -1, -1));

        priceTextField.setEditable(false);
        jPanel1.add(priceTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 412, 100, 41));

        warrantyLabel.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        warrantyLabel.setText("Bảo hành:");
        jPanel1.add(warrantyLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 420, -1, -1));

        warrantyTextField.setEditable(false);
        jPanel1.add(warrantyTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(356, 413, 120, 40));

        EquipmentEditConfirmButton.setBackground(new java.awt.Color(255, 255, 255));
        EquipmentEditConfirmButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/plus.png"))); // NOI18N
        EquipmentEditConfirmButton.setText("Xác nhận");
        EquipmentEditConfirmButton.setEntered(false);
        EquipmentEditConfirmButton.setEnteredColor(java.awt.Color.white);
        EquipmentEditConfirmButton.setGradientBackgroundColor(new java.awt.Color(102, 255, 102));
        EquipmentEditConfirmButton.setRounded(true);
        EquipmentEditConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EquipmentEditConfirmButtonActionPerformed(evt);
            }
        });
        jPanel1.add(EquipmentEditConfirmButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 670, 120, 40));

        EquipmentRefreshButton.setBackground(new java.awt.Color(255, 255, 255));
        EquipmentRefreshButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.png"))); // NOI18N
        EquipmentRefreshButton.setText("Làm mới");
        EquipmentRefreshButton.setEntered(false);
        EquipmentRefreshButton.setEnteredColor(java.awt.Color.white);
        EquipmentRefreshButton.setGradientBackgroundColor(new java.awt.Color(204, 204, 255));
        EquipmentRefreshButton.setRounded(true);
        EquipmentRefreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EquipmentRefreshButtonActionPerformed(evt);
            }
        });
        jPanel1.add(EquipmentRefreshButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 670, 120, 40));

        javax.swing.GroupLayout pnl_equipmentsLayout = new javax.swing.GroupLayout(pnl_equipments);
        pnl_equipments.setLayout(pnl_equipmentsLayout);
        pnl_equipmentsLayout.setHorizontalGroup(
            pnl_equipmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_equipmentsLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(equipmentsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );
        pnl_equipmentsLayout.setVerticalGroup(
            pnl_equipmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_equipmentsLayout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addComponent(equipmentsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
        );

        pnl_settings.setBackground(new java.awt.Color(254, 217, 155));
        pnl_settings.setForeground(new java.awt.Color(7, 16, 19));
        pnl_settings.setPreferredSize(new java.awt.Dimension(1200, 722));
        pnl_settings.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        loginInfoPanel.setBackground(new java.awt.Color(254, 217, 155));

        loginInfoTable.setAutoCreateRowSorter(true);
        loginInfoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tài khoản", "Mật khẩu", "Mã nhân viên", "Chức vụ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        loginInfoScrollPane.setViewportView(loginInfoTable);

        javax.swing.GroupLayout loginInfoPanelLayout = new javax.swing.GroupLayout(loginInfoPanel);
        loginInfoPanel.setLayout(loginInfoPanelLayout);
        loginInfoPanelLayout.setHorizontalGroup(
            loginInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 790, Short.MAX_VALUE)
            .addGroup(loginInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(loginInfoPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(loginInfoScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 770, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        loginInfoPanelLayout.setVerticalGroup(
            loginInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 449, Short.MAX_VALUE)
            .addGroup(loginInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(loginInfoPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(loginInfoScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pnl_settings.add(loginInfoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 170, -1, -1));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("Tên:");
        pnl_settings.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 190, -1, -1));

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setText("Ngày sinh:");
        pnl_settings.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 220, -1, -1));

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel3.setText("Email:");
        pnl_settings.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 270, -1, -1));

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel4.setText("Số điện thoại:");
        pnl_settings.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 300, -1, -1));

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel5.setText("Ngày tạo:");
        pnl_settings.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 340, -1, -1));

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel7.setText("Ngày cập nhật:");
        pnl_settings.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 380, -1, -1));

        birthDayLb.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        birthDayLb.setText("sadfasdxczvxcvxzcvxzvc");
        pnl_settings.add(birthDayLb, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 220, -1, -1));

        fullNameLb.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        fullNameLb.setText("kjbsdkfblsbadfkbbsklabkvxczzxv");
        pnl_settings.add(fullNameLb, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 190, -1, -1));

        emailLb.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        emailLb.setText("xzcvzxcvzxvczxcvxzcv");
        pnl_settings.add(emailLb, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 270, -1, -1));

        contactLb.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        contactLb.setText("asfdsadxzcvxcvz");
        pnl_settings.add(contactLb, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 300, -1, -1));

        createLb.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        createLb.setText("xzcvxzcvxzcvxzcv");
        pnl_settings.add(createLb, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 340, -1, -1));

        updateLb.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        updateLb.setText("bkxc cbkxb kbxk bkx");
        pnl_settings.add(updateLb, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 380, -1, -1));

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 0, 0));
        jLabel24.setText("CÀI ĐẶT");
        pnl_settings.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 46, -1, -1));

        button1.setBackground(new java.awt.Color(71, 87, 133));
        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setText("Đăng xuất");
        button1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        button1.setRounded(true);
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });
        pnl_settings.add(button1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1084, 683, 149, 49));

        signupButton.setBackground(new java.awt.Color(0, 153, 153));
        signupButton.setForeground(new java.awt.Color(255, 255, 255));
        signupButton.setText("Đăng ký");
        signupButton.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        signupButton.setRounded(true);
        signupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signupButtonActionPerformed(evt);
            }
        });
        pnl_settings.add(signupButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 500, 149, 49));

        profilePictureLabel.setBackground(new java.awt.Color(255, 255, 255));
        pnl_settings.add(profilePictureLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 190, 207, 213));

        showLoginInfoButton.setBackground(new java.awt.Color(153, 0, 51));
        showLoginInfoButton.setForeground(new java.awt.Color(255, 255, 255));
        showLoginInfoButton.setText("Danh sách tài khoản");
        showLoginInfoButton.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        showLoginInfoButton.setRounded(true);
        showLoginInfoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showLoginInfoButtonActionPerformed(evt);
            }
        });
        pnl_settings.add(showLoginInfoButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 560, 210, 49));

        pnl_eqsDetail.setBackground(new java.awt.Color(197, 255, 253));
        pnl_eqsDetail.setPreferredSize(new java.awt.Dimension(1200, 710));
        pnl_eqsDetail.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTenThietBi.setBackground(new java.awt.Color(247, 247, 247));
        txtTenThietBi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTenThietBiKeyReleased(evt);
            }
        });
        pnl_eqsDetail.add(txtTenThietBi, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 160, 200, 40));

        jLabel22.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(36, 11, 54));
        jLabel22.setText("Nhà cung cấp :");
        jLabel22.setToolTipText("");
        pnl_eqsDetail.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 160, -1, -1));

        alertMCTTB.setForeground(new java.awt.Color(255, 0, 0));
        alertMCTTB.setText("Mã CTBB alert");
        pnl_eqsDetail.add(alertMCTTB, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 130, -1, -1));

        txtGia.setBackground(new java.awt.Color(247, 247, 247));
        txtGia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGiaKeyReleased(evt);
            }
        });
        pnl_eqsDetail.add(txtGia, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 80, 100, 30));

        alertTTB.setForeground(new java.awt.Color(255, 0, 0));
        alertTTB.setText("Tên TB alert");
        pnl_eqsDetail.add(alertTTB, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 200, -1, -1));

        jLabel23.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(36, 11, 54));
        jLabel23.setText("Giá :");
        pnl_eqsDetail.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 80, -1, -1));

        alertNCC.setForeground(new java.awt.Color(255, 0, 0));
        alertNCC.setText("NCC alert");
        pnl_eqsDetail.add(alertNCC, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 200, -1, -1));

        alertGia.setForeground(new java.awt.Color(255, 0, 0));
        alertGia.setText("Giá TB alert");
        pnl_eqsDetail.add(alertGia, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 130, -1, -1));

        lbl_themCTTB.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        lbl_themCTTB.setForeground(new java.awt.Color(51, 51, 255));
        lbl_themCTTB.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_themCTTB.setText("THÊM CHI TIẾT THIẾT BỊ");
        pnl_eqsDetail.add(lbl_themCTTB, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 10, 1240, 40));

        jLabel26.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(36, 11, 54));
        jLabel26.setText("Thời gian bảo hành:");
        pnl_eqsDetail.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 240, -1, -1));

        alertTGBH.setForeground(new java.awt.Color(255, 0, 0));
        alertTGBH.setText("TGBH alert");
        pnl_eqsDetail.add(alertTGBH, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 270, -1, -1));

        txtThoiGianBH.setBackground(new java.awt.Color(247, 247, 247));
        txtThoiGianBH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtThoiGianBHKeyReleased(evt);
            }
        });
        pnl_eqsDetail.add(txtThoiGianBH, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 240, 50, 30));

        jLabel27.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel27.setText("VND");
        pnl_eqsDetail.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 90, -1, -1));

        jLabel28.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(36, 11, 54));
        jLabel28.setText("Mã chi tiết thiết bị :");
        pnl_eqsDetail.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, -1, -1));

        jLabel29.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(36, 11, 54));
        jLabel29.setText("Hình ảnh:");
        pnl_eqsDetail.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 240, -1, -1));

        jLabel30.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel30.setText("năm");
        pnl_eqsDetail.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 250, -1, -1));

        txtMaChiTietTB.setBackground(new java.awt.Color(247, 247, 247));
        txtMaChiTietTB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMaChiTietTBKeyReleased(evt);
            }
        });
        pnl_eqsDetail.add(txtMaChiTietTB, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 80, 200, 42));

        cbNhaCungCap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNhaCungCapActionPerformed(evt);
            }
        });
        pnl_eqsDetail.add(cbNhaCungCap, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 160, 320, 35));

        jLabel31.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(36, 11, 54));
        jLabel31.setText("Tên thiết bị :");
        pnl_eqsDetail.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, -1, -1));

        btnGetImage.setBackground(new java.awt.Color(252, 70, 107));
        btnGetImage.setForeground(new java.awt.Color(255, 255, 255));
        btnGetImage.setText("Chọn hình ảnh!");
        btnGetImage.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        btnGetImage.setGradientBackgroundColor(new java.awt.Color(63, 94, 251));
        btnGetImage.setRounded(true);
        btnGetImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGetImageActionPerformed(evt);
            }
        });
        pnl_eqsDetail.add(btnGetImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 230, 200, -1));

        categoriesTable.setAutoCreateRowSorter(true);
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
        categoriesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                categoriesTableMouseClicked(evt);
            }
        });
        categoriesScrollPane.setViewportView(categoriesTable);

        pnl_eqsDetail.add(categoriesScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 1250, 390));

        button5.setBackground(new java.awt.Color(255, 255, 255));
        button5.setForeground(new java.awt.Color(0, 0, 255));
        button5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.png"))); // NOI18N
        button5.setText("Làm mới");
        button5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        button5.setRounded(true);
        button5.setSelected(true);
        button5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button5ActionPerformed(evt);
            }
        });
        pnl_eqsDetail.add(button5, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 300, 135, 45));

        btn_confirmCategories.setBackground(new java.awt.Color(255, 255, 255));
        btn_confirmCategories.setForeground(new java.awt.Color(0, 153, 0));
        btn_confirmCategories.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/plus.png"))); // NOI18N
        btn_confirmCategories.setText("Thêm");
        btn_confirmCategories.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btn_confirmCategories.setRounded(true);
        btn_confirmCategories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_confirmCategoriesActionPerformed(evt);
            }
        });
        pnl_eqsDetail.add(btn_confirmCategories, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 300, 145, 45));

        pnl_suppliers.setBackground(new java.awt.Color(255, 255, 255));

        suppliersTable.setAutoCreateRowSorter(true);
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
        suppliersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                suppliersTableMouseClicked(evt);
            }
        });
        suppliersScrollPane.setViewportView(suppliersTable);
        if (suppliersTable.getColumnModel().getColumnCount() > 0) {
            suppliersTable.getColumnModel().getColumn(0).setResizable(false);
            suppliersTable.getColumnModel().getColumn(1).setResizable(false);
            suppliersTable.getColumnModel().getColumn(2).setResizable(false);
            suppliersTable.getColumnModel().getColumn(3).setResizable(false);
        }

        btnSupplierConfirm.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnSupplierConfirm.setForeground(new java.awt.Color(51, 153, 0));
        btnSupplierConfirm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/plus.png"))); // NOI18N
        btnSupplierConfirm.setText("Thêm");
        btnSupplierConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierConfirmActionPerformed(evt);
            }
        });

        btnRefreshNCC.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnRefreshNCC.setForeground(new java.awt.Color(51, 51, 255));
        btnRefreshNCC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.png"))); // NOI18N
        btnRefreshNCC.setText("Làm mới");
        btnRefreshNCC.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnRefreshNCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshNCCActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel25.setText("Mã:");

        lbl_themNCC.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lbl_themNCC.setForeground(new java.awt.Color(51, 51, 255));
        lbl_themNCC.setText("THÊM NHÀ CUNG CẤP");

        jLabel33.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel33.setText("Tên:");

        jLabel34.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel34.setText("Địa chỉ");

        alertTNCC.setForeground(new java.awt.Color(255, 0, 0));
        alertTNCC.setText("Tên NCC alert");

        jLabel35.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel35.setText("Số điện thoại:");

        alertDCNCC.setForeground(new java.awt.Color(255, 0, 0));
        alertDCNCC.setText("Địa chỉ NCC alert");

        alertSDTNCC.setForeground(new java.awt.Color(255, 0, 0));
        alertSDTNCC.setText("SDT NCC alert");

        txtTenNCC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTenNCCKeyReleased(evt);
            }
        });

        txtSDT.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtSDT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSDTKeyReleased(evt);
            }
        });

        txtDiaChiNCC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiaChiNCCKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnl_suppliersLayout = new javax.swing.GroupLayout(pnl_suppliers);
        pnl_suppliers.setLayout(pnl_suppliersLayout);
        pnl_suppliersLayout.setHorizontalGroup(
            pnl_suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_suppliersLayout.createSequentialGroup()
                .addGroup(pnl_suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_suppliersLayout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addComponent(btnRefreshNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(78, 78, 78)
                        .addComponent(btnSupplierConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_suppliersLayout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addGroup(pnl_suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addComponent(jLabel34)
                            .addComponent(jLabel35)
                            .addComponent(jLabel33))
                        .addGap(27, 27, 27)
                        .addGroup(pnl_suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(alertSDTNCC)
                            .addComponent(txtDiaChiNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(alertTNCC)
                            .addComponent(alertDCNCC)))
                    .addGroup(pnl_suppliersLayout.createSequentialGroup()
                        .addGap(187, 187, 187)
                        .addComponent(lbl_themNCC)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 195, Short.MAX_VALUE)
                .addComponent(suppliersScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 608, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnl_suppliersLayout.setVerticalGroup(
            pnl_suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(suppliersScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(pnl_suppliersLayout.createSequentialGroup()
                .addContainerGap(128, Short.MAX_VALUE)
                .addComponent(lbl_themNCC)
                .addGap(100, 100, 100)
                .addGroup(pnl_suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addGap(35, 35, 35)
                .addGroup(pnl_suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alertTNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDiaChiNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alertDCNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alertSDTNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(128, 128, 128)
                .addGroup(pnl_suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSupplierConfirm)
                    .addComponent(btnRefreshNCC))
                .addGap(88, 88, 88))
        );

        pnl_imports.setBackground(new java.awt.Color(255, 255, 255));

        importDetailsTable.setAutoCreateRowSorter(true);
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
        importDetailsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                importDetailsTableMouseClicked(evt);
            }
        });
        importDetailsScrollPane.setViewportView(importDetailsTable);

        importIDLabel.setText("Mã phiếu nhập");

        importIDTextField.setEditable(false);
        importIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        importAddButton.setText("Thêm thiết bị");
        importAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importAddButtonActionPerformed(evt);
            }
        });

        importEquipmentTable.setAutoCreateRowSorter(true);
        importEquipmentTable.setModel(new javax.swing.table.DefaultTableModel(
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
        scrollPane.setViewportView(importEquipmentTable);

        importDateLabel.setText("Ngày nhập");

        importDateTextField.setEditable(false);
        importDateTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        importEditButton.setText("Sửa");
        importEditButton.setEnabled(false);

        importDeleteButton.setText("Xóa");
        importDeleteButton.setEnabled(false);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 255));
        jLabel6.setText("TẠO PHIẾU NHẬP");

        saveImportButton.setText("Lưu phiếu");
        saveImportButton.setGradientBackgroundColor(null);
        saveImportButton.setRounded(true);
        saveImportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveImportButtonActionPerformed(evt);
            }
        });

        refreshImportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.png"))); // NOI18N
        refreshImportButton.setText("Làm mới");
        refreshImportButton.setRounded(true);
        refreshImportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshImportButtonActionPerformed(evt);
            }
        });

        loadImportButton.setText("Chọn phiếu...");
        loadImportButton.setGradientBackgroundColor(null);
        loadImportButton.setRounded(true);
        loadImportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadImportButtonActionPerformed(evt);
            }
        });

        makeImportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/add (1).png"))); // NOI18N
        makeImportButton.setText("Lập phiếu");
        makeImportButton.setRounded(true);
        makeImportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeImportButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(543, 543, 543)
                        .addComponent(jLabel6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(importAddButton)
                            .addComponent(importEditButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(importDeleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(importDateLabel)
                        .addGap(18, 18, 18)
                        .addComponent(importDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refreshImportButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(makeImportButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(importIDLabel)
                        .addGap(18, 18, 18)
                        .addComponent(importIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(loadImportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(saveImportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel6)
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(loadImportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(saveImportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(importIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(importIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(importAddButton)
                        .addGap(17, 17, 17)
                        .addComponent(importEditButton)
                        .addGap(17, 17, 17)
                        .addComponent(importDeleteButton))
                    .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(refreshImportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(makeImportButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(importDateLabel)
                            .addComponent(importDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnl_importsLayout = new javax.swing.GroupLayout(pnl_imports);
        pnl_imports.setLayout(pnl_importsLayout);
        pnl_importsLayout.setHorizontalGroup(
            pnl_importsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_importsLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(importDetailsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1052, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnl_importsLayout.setVerticalGroup(
            pnl_importsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_importsLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(importDetailsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout pnl_containerLayout = new javax.swing.GroupLayout(pnl_container);
        pnl_container.setLayout(pnl_containerLayout);
        pnl_containerLayout.setHorizontalGroup(
            pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_containerLayout.createSequentialGroup()
                .addComponent(pnl_appBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnl_function, javax.swing.GroupLayout.DEFAULT_SIZE, 1257, Short.MAX_VALUE)
                    .addComponent(pnl_dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 232, Short.MAX_VALUE)
                    .addComponent(pnl_users, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 232, Short.MAX_VALUE)
                    .addComponent(pnl_equipments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 232, Short.MAX_VALUE)
                    .addComponent(pnl_settings, javax.swing.GroupLayout.PREFERRED_SIZE, 1257, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 232, Short.MAX_VALUE)
                    .addComponent(pnl_eqsDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 1257, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 232, Short.MAX_VALUE)
                    .addComponent(pnl_suppliers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 233, Short.MAX_VALUE)
                    .addComponent(pnl_imports, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        pnl_containerLayout.setVerticalGroup(
            pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_containerLayout.createSequentialGroup()
                .addComponent(pnl_function, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(pnl_appBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 112, Short.MAX_VALUE)
                    .addComponent(pnl_users, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 113, Short.MAX_VALUE)
                    .addComponent(pnl_equipments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 117, Short.MAX_VALUE)
                    .addComponent(pnl_settings, javax.swing.GroupLayout.PREFERRED_SIZE, 762, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 113, Short.MAX_VALUE)
                    .addComponent(pnl_eqsDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 766, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 115, Short.MAX_VALUE)
                    .addComponent(pnl_suppliers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 115, Short.MAX_VALUE)
                    .addComponent(pnl_imports, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lbl_close_hoverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_close_hoverMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lbl_close_hoverMouseClicked

    private void lbl_close_hoverMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_close_hoverMouseEntered
        lbl_close_hover.setSize(32, 32);
        lbl_close_hover.setIcon(ImageGenerator.ResizeImage(new ImageGenerator().getImageFolderPath() + "/src/icon/close2.png", lbl_close_hover));
    }//GEN-LAST:event_lbl_close_hoverMouseEntered

    private void lbl_close_hoverMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_close_hoverMouseExited
        lbl_close_hover.setSize(40, 32);
        lbl_close_hover.setIcon(ImageGenerator.ResizeImage(new ImageGenerator().getImageFolderPath() + "/src/icon/close3.png", lbl_close_hover));
    }//GEN-LAST:event_lbl_close_hoverMouseExited

    private void usersTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usersTableMouseClicked
        if (_sideBarFunctionFrm != null) {
            _sideBarFunctionFrm.getEditButton().setEnabled(false);
            _sideBarFunctionFrm.getRemoveButton().setEnabled(false);
        }
    }//GEN-LAST:event_usersTableMouseClicked

    private void equipmentsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_equipmentsTableMouseClicked
        if (_sideBarFunctionFrm != null) {
            _sideBarFunctionFrm.getEditButton().setEnabled(true);
            _sideBarFunctionFrm.getRemoveButton().setEnabled(true);
        }

        tableImageCellCallback(evt, equipmentsTable);
    }//GEN-LAST:event_equipmentsTableMouseClicked

    private void importDetailsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_importDetailsTableMouseClicked
        if (_sideBarFunctionFrm != null) {
            _sideBarFunctionFrm.getEditButton().setEnabled(false);
            _sideBarFunctionFrm.getRemoveButton().setEnabled(true);
        }
    }//GEN-LAST:event_importDetailsTableMouseClicked

    private void suppliersTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_suppliersTableMouseClicked
        if (_sideBarFunctionFrm != null) {
            _sideBarFunctionFrm.getEditButton().setEnabled(true);
            _sideBarFunctionFrm.getRemoveButton().setEnabled(true);
        }
    }//GEN-LAST:event_suppliersTableMouseClicked

    private void lbl_menuItem_7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_menuItem_7MouseClicked


    }//GEN-LAST:event_lbl_menuItem_7MouseClicked

    private void lbl_menuItem_8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_menuItem_8MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_menuItem_8MouseClicked

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        dispose();
        LoginForm.create();
    }//GEN-LAST:event_button1ActionPerformed

    private void txtTenThietBiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTenThietBiKeyReleased
        String tenTB = txtTenThietBi.getText();

        if (tenTB.matches("^[a-zA-Z]+$") && tenTB.length() > 0) {
            initAlertLabel(alertTTB);
        }
    }//GEN-LAST:event_txtTenThietBiKeyReleased

    private void txtGiaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGiaKeyReleased
        String gia = txtGia.getText();
        if (gia.matches("^[\\d]+$") && Integer.parseInt(gia) >= 10000) {
            initAlertLabel(alertGia);
        }
    }//GEN-LAST:event_txtGiaKeyReleased

    private void txtThoiGianBHKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtThoiGianBHKeyReleased
        String thoigianBH = txtThoiGianBH.getText();
        if (thoigianBH.matches("^[\\d]{1,2}$")) {
            if (Integer.parseInt(thoigianBH) <= 10 && Integer.parseInt(thoigianBH) >= 1) {
                initAlertLabel(alertTGBH);
            }
        }
    }//GEN-LAST:event_txtThoiGianBHKeyReleased

    private void txtMaChiTietTBKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaChiTietTBKeyReleased
        String maCTTB = txtMaChiTietTB.getText();

        if (maCTTB.matches("^[a-zA-Z]{3}[\\d]{2}$") && txtMaChiTietTB.getText().length() > 0) {
            initAlertLabel(alertMCTTB);
        }
    }//GEN-LAST:event_txtMaChiTietTBKeyReleased

    private void cbNhaCungCapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNhaCungCapActionPerformed
        int selectedItem = cbNhaCungCap.getSelectedIndex();

        if (selectedItem == _listOfSuppliers.size()) {
            cbNhaCungCap.hidePopup();
            new AddSupplierForm(this, -1, false, rootPaneCheckingEnabled).setVisible(true);
        }
    }//GEN-LAST:event_cbNhaCungCapActionPerformed

    private void btnGetImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGetImageActionPerformed
        _addImgFrame = AddImage.getObj(btnGetImage, "Thêm hình ảnh thiết bị");
        _addImgFrame.setVisible(true);
    }//GEN-LAST:event_btnGetImageActionPerformed

    private void btnSupplierConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierConfirmActionPerformed
        initSuppliersAlert();
        String maNCCs = txtMaNCC.getText();
        String tenNCC = txtTenNCC.getText();
        String diaChiNCC = txtDiaChiNCC.getText();
        String sdtNCC = txtSDT.getText();
        boolean check = true;

        if (tenNCC.length() == 0) {
            createAlert(alertTNCC, "Tên không được để trống");
            check = false;
        }
        if (diaChiNCC.equals("")) {
            createAlert(alertDCNCC, "Địa chỉ không được để trống");
            check = false;
        }

        if (tenNCC.length() < 0 || tenNCC.matches("[^a-zA-Z]+")) {
            createAlert(alertTNCC, "Tên NCC phải là chữ cái");
            check = false;
        }
        if (!sdtNCC.matches("^[0]{1}[0-9]{9}$") || sdtNCC.length() < 0) {
            createAlert(alertSDTNCC, "SDT không hợp lệ (10 chữ số)");
            check = false;
        }

        if (check) {
            int maNCC = Integer.parseInt(maNCCs);
            Supplier s = new Supplier(maNCC, tenNCC, diaChiNCC, sdtNCC);
            if (_sC.isIdExist(maNCC)) {
                if (_sC.updateSupplier(s)) {
                    new AlertFrame("Cập nhật thành công").setVisible(true);
                    btnSupplierConfirm.setText("Thêm");
                    lbl_themNCC.setText("Thêm nhà cung cấp");
                }
                else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
                }
            }
            else {
                if (_sC.addNewSupplier(s)) {
                    new AlertFrame("Thêm thành công").setVisible(true);
                }
                else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại");
                }
            }
            loadDatabase();
            resetSupplierField();
        }
    }//GEN-LAST:event_btnSupplierConfirmActionPerformed

    private void btnRefreshNCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshNCCActionPerformed
        resetSupplierField();
        initSuppliersAlert();
    }//GEN-LAST:event_btnRefreshNCCActionPerformed

    private void txtTenNCCKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTenNCCKeyReleased
        String tenNCC = txtTenNCC.getText();
        if (tenNCC.matches("[a-zA-Z]+")) {
            initAlertLabel(alertTNCC);
        }
    }//GEN-LAST:event_txtTenNCCKeyReleased

    private void txtSDTKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSDTKeyReleased
        String sdtNCC = txtSDT.getText();
        if (sdtNCC.matches("^[0-9]{10}$")) {
            initAlertLabel(alertSDTNCC);
        }
    }//GEN-LAST:event_txtSDTKeyReleased

    private void txtDiaChiNCCKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiaChiNCCKeyReleased
        String diaChiNCC = txtDiaChiNCC.getText();
        if (diaChiNCC.length() > 0) {
            initAlertLabel(alertDCNCC);
        }
    }//GEN-LAST:event_txtDiaChiNCCKeyReleased

    private void categoriesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_categoriesTableMouseClicked
        if (_sideBarFunctionFrm != null) {
            _sideBarFunctionFrm.getEditButton().setEnabled(true);
            _sideBarFunctionFrm.getRemoveButton().setEnabled(true);
        }
        tableImageCellCallback(evt, categoriesTable);
    }//GEN-LAST:event_categoriesTableMouseClicked

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        showPanel(pnl_eqsDetail);
        setLabelBackground(lbl_menuItem_4);
    }//GEN-LAST:event_jPanel4MouseClicked

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
        showPanel(pnl_suppliers);
        setLabelBackground(lbl_menuItem_5);
    }//GEN-LAST:event_jPanel5MouseClicked

    private void db_txtDNHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_db_txtDNHMouseClicked

    }//GEN-LAST:event_db_txtDNHMouseClicked

    private void detailIDComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_detailIDComboBoxItemStateChanged

    }//GEN-LAST:event_detailIDComboBoxItemStateChanged

    private void supplierTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierTextFieldActionPerformed

    private void detailIDComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailIDComboBoxActionPerformed
        if (detailIDComboBox.getSelectedItem() == null) {
            return;
        }

        String detailID = detailIDComboBox.getSelectedItem().toString();
        if (!detailID.equals("")) {
            if (detailID.equals("Thêm loại thiết bị...")) {
                AddEquipmentDetailsForm detailForm = AddEquipmentDetailsForm.getObj(this, -1, false);
                detailForm.setLocationRelativeTo(this);
                detailForm.setVisible(true);
            }
            else {
                loadDetailInfo(detailID);
            }
        }
    }//GEN-LAST:event_detailIDComboBoxActionPerformed

    private void jPanel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8MouseClicked
        showPanel(pnl_imports);
        setLabelBackground(lbl_menuItem_6);
    }//GEN-LAST:event_jPanel8MouseClicked

    private void jPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseClicked
        showPanel(pnl_equipments);
        setLabelBackground(lbl_menuItem_3);
    }//GEN-LAST:event_jPanel3MouseClicked

    private void button5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button5ActionPerformed
        resetCategoriesField();
        initCategoriesAlert();
        if (_listOfSuppliers.size() != _sC.getSuppliersInfo().size()) {
            getSupplierList();
        }
    }//GEN-LAST:event_button5ActionPerformed

    private void btn_confirmCategoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_confirmCategoriesActionPerformed
        initCategoriesAlert();
        String maChiTietTB = txtMaChiTietTB.getText();
        String tenThietbi = txtTenThietBi.getText();
        String hinhAnh;

        if (_isCategoriesUpdate) {
            if (btnGetImage.getText().equals("Chọn hình ảnh!")) {
                hinhAnh = null;
            }
            else if (btnGetImage.getText().contains("/")) {
                hinhAnh = btnGetImage.getText();
            }
            else {
                hinhAnh = "/src/images/" + btnGetImage.getText();
            }
        }
        else {
            if (btnGetImage.getText().equals("Chọn hình ảnh!")) {
                hinhAnh = null;
            }
            else {
                hinhAnh = "/src/images/" + btnGetImage.getText();
            }
        }

        String gia = txtGia.getText();
        String thoigianBH = txtThoiGianBH.getText();
        int selectedNhaCungCap = cbNhaCungCap.getSelectedIndex();
        int nhaCungCapId = 0;

        boolean check = true;

        if (maChiTietTB.length() == 0) {
            createAlert(alertMCTTB, "Mã CTTB không được để trống");
            check = false;
        }
        if (tenThietbi.length() == 0) {
            createAlert(alertTTB, "Tên thiết bị không được để trống");
            check = false;
        }
        if (gia.length() == 0) {
            createAlert(alertGia, "Giá TB không được để trống");
            check = false;
        }

        if (!maChiTietTB.matches("^[a-zA-Z]{3}[\\d]{2}$")) {
            createAlert(alertMCTTB, "Mã CTTB phải đúng định dạng 3 ký tự chữ và 2 ký tự số");
            check = false;
        }
        if (tenThietbi.length() > 0 && tenThietbi.matches("[^a-zA-Z]+")) {
            createAlert(alertTTB, "Tên TB không để trống và phải là ký tự chữ");
            check = false;
        }
        if (!gia.matches("\\d+") || Integer.parseInt(gia) < 10000) {
            createAlert(alertGia, "Giá TB ít nhất là 10000 và là ký tự số");
            check = false;
        }
        if (cbNhaCungCap.getSelectedItem().toString().equals("Thêm nhà cung cấp")) {
            createAlert(alertNCC, "Chọn 1 nhà cung cấp");
            check = false;
        }

        if (maChiTietTB.length() > 5) {
            createAlert(alertMCTTB, "Mã CTTB không được vượt quá 5 ký tự");
            check = false;
        }

        if (!thoigianBH.matches("[\\d]{1,2}")) {
            createAlert(alertTGBH, "Vui lòng nhập năm từ 0 đến 10");
            check = false;
        }
        else if (Integer.parseInt(thoigianBH) > 10 || Integer.parseInt(thoigianBH) < 0) {
            createAlert(alertTGBH, "Vui lòng nhập năm từ 0 đến 10");
            check = false;
        }

        if (check) {
            nhaCungCapId = _listOfSuppliers.get(selectedNhaCungCap).getSupplierId();
            int price = Integer.parseInt(gia);
            Equipment_Details eD = new Equipment_Details();
            eD.setId(maChiTietTB.toUpperCase());
            eD.setName(tenThietbi);
            eD.setPicture(hinhAnh);
            eD.setPrice(price);
            eD.setSupplier_id(nhaCungCapId);
            if (thoigianBH.length() > 0) {
                eD.setWarranty_time(Integer.parseInt(thoigianBH));
            }

            if (_eC.isIdExist(maChiTietTB)) {
                if (_eC.updateEquipmentDetails(eD)) {
                    new AlertFrame("Cập nhật thành công").setVisible(true);
                    try {
                        if (hinhAnh != null) {
                            _addImgFrame.saveImage();
                        }
                        lbl_themCTTB.setText("THÊM THIẾT BỊ");
                        btn_confirmCategories.setText("Thêm");
                        txtMaChiTietTB.setFocusable(true);
                        txtMaChiTietTB.setEditable(true);
                        txtMaChiTietTB.requestFocus(true);

                    } catch (NullPointerException e) {
                        System.out.println("Waiting");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất  bại");
                }
            }
            else {
                if (_eC.addNewEquipmentDetails(eD)) {
                    new AlertFrame("Thêm thành công").setVisible(true);
                    if (hinhAnh != null) {
                        _addImgFrame.saveImage();
                    }
                }
                else {
                    JOptionPane.showMessageDialog(this, "Thêm thất  bại");
                }
            }
            loadDatabase();
            resetCategoriesField();
        }
    }//GEN-LAST:event_btn_confirmCategoriesActionPerformed

    private void button6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button6ActionPerformed
        new Thread(new Runnable() {
            @Override
            public void run() {
                SideBarFunction sBF = new SideBarFunction(_this, false);
                _sideBarFunctionFrm = sBF;
                sBF.setVisible(true);
            }
        }).start();
    }//GEN-LAST:event_button6ActionPerformed

    private void signupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signupButtonActionPerformed
        CreateUserForm signupForm = new CreateUserForm(this, true);
        signupForm.setLocationRelativeTo(this);
        signupForm.setVisible(true);
    }//GEN-LAST:event_signupButtonActionPerformed

    private void showLoginInfoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showLoginInfoButtonActionPerformed
        loginInfoPanel.setVisible(!loginInfoPanel.isVisible());
        loginInfoScrollPane.setVisible(!loginInfoScrollPane.isVisible());
    }//GEN-LAST:event_showLoginInfoButtonActionPerformed

    private void importAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importAddButtonActionPerformed
        AddEquipmentForm addEquimentForm = new AddEquipmentForm(this);
        addEquimentForm.setLocationRelativeTo(this);
        addEquimentForm.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_importAddButtonActionPerformed

    private void loadImportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadImportButtonActionPerformed
        JFileChooser fileChooser = new JFileChooser(_importFolderPath);
        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }

                String extension = new ImageFilter().getExtension(f);
                if (extension != null) {
                    if (extension.equals("bin")) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "Import Binary Text File";
            }
        });

        fileChooser.setAcceptAllFileFilterUsed(false);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            loadImportData();

            File file = fileChooser.getSelectedFile();
            ArrayList<EquipmentData> dataList = (ArrayList<EquipmentData>) FileController.readImportDetailFile(file);
            List<Equipment_Details> detailList = _eC.getListEquipmentDetails();

            for (EquipmentData data : dataList) {
                int maxID = (AddEquipmentForm.getMaxEquimentID(data.getEquipmentID(), _checkEquipmentIDList));
                data.setEquipmentID(data.getEquipmentID() + String.format("-%03d", maxID));
                Equipment_Details dataDetail = null;

                for (Equipment_Details detail : detailList) {
                    if (detail.getId().equals(data.getDetailID())) {
                        dataDetail = detail;
                        break;
                    }
                }

                if (dataDetail != null) {
                    addEquimentForImport(data.getEquipmentID(), dataDetail.getName(), data.getStatus(),
                            dataDetail.getPrice(), dataDetail.getPicture(), data.getDetailID());
                }
            }
        }
    }//GEN-LAST:event_loadImportButtonActionPerformed

    private void makeImportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makeImportButtonActionPerformed
        String id, status, detailID;
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(new Date().getTime());
        java.sql.Date date = new java.sql.Date(new Date().getTime());
        int importID = Integer.valueOf(importIDTextField.getText());

        if (importEquipmentTable.getRowCount() > 0) {
            saveImportDetailToDatabase(importID, _userID, date);

            for (int i = 0; i < importEquipmentTable.getRowCount(); i++) {
                id = importEquipmentTable.getValueAt(i, 0).toString();
                status = importEquipmentTable.getValueAt(i, 2).toString();
                detailID = importEquipmentTable.getValueAt(i, 5).toString();
                saveEquipmentToDatabase(id, status, detailID, importID, timeStamp);
            }
        }
        new AlertFrame("Lưu vào CSDL thành công").setVisible(true);
        loadImportData();
        loadDatabase();
    }//GEN-LAST:event_makeImportButtonActionPerformed

    private void saveImportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveImportButtonActionPerformed
        if (importEquipmentTable.getRowCount() < 1) {
            return;
        }

        Object result = JOptionPane.showInputDialog(null, "Nhập tên file để lưu:", "Lưu phiếu nhập", JOptionPane.PLAIN_MESSAGE);
        if (result != null) {
            File file = new File(_importFolderPath + result.toString() + ".bin");

            if (file.exists()) {
                int choice = JOptionPane.showConfirmDialog(null, "Tồn tại file cùng tên, bạn có muốn ghi đè?",
                        "File đã tồn tại", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            else {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(AdminDashBoard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            ArrayList<EquipmentData> dataList = new ArrayList<>();
            for (int i = 0; i < importEquipmentTable.getRowCount(); i++) {
                String equipmentID = importEquipmentTable.getValueAt(i, 0).toString().substring(0, 3);
                String status = importEquipmentTable.getValueAt(i, 2).toString();
                String detailID = importEquipmentTable.getValueAt(i, 5).toString();
                dataList.add(new EquipmentData(equipmentID, status, detailID));
            }
            FileController.saveImportDetailFile(file, dataList);
        }
    }//GEN-LAST:event_saveImportButtonActionPerformed

    private void refreshImportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshImportButtonActionPerformed
        loadImportData();
    }//GEN-LAST:event_refreshImportButtonActionPerformed

    private void EquipmentEditConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EquipmentEditConfirmButtonActionPerformed
        if (equipmentIDTextField.getText().isEmpty()) {
            return;
        }

        String id = equipmentIDTextField.getText();
        String status = statusComboBox.getSelectedItem().toString();
        String detailID = detailIDComboBox.getSelectedItem().toString();

        editEquipment(id, status, detailID);
        new AlertFrame("Chỉnh sửa thành công").setVisible(true);
        refreshEquipmentEditBox();
        loadDatabase();
    }//GEN-LAST:event_EquipmentEditConfirmButtonActionPerformed

    private void EquipmentRefreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EquipmentRefreshButtonActionPerformed
        refreshEquipmentEditBox();
    }//GEN-LAST:event_EquipmentRefreshButtonActionPerformed

    public void fillOutSupplierInfo(int id) {
        for (Supplier s : _listOfSuppliers) {
            if (s.getSupplierId() == id) {
                System.out.println("true");
                txtMaNCC.setText(s.getSupplierId() + "");
                txtTenNCC.setText(s.getName());
                txtDiaChiNCC.setText(s.getAddress());
                txtSDT.setText(s.getPhoneNumber());
                break;
            }
        }

        btn_confirmCategories.setText("Cập nhật");
    }

    public void resetSupplierField() {
        txtTenNCC.setText("");
        txtDiaChiNCC.setText("");
        txtSDT.setText("");
    }
    /**
     * @param args the command line arguments
     */
    private SimpleDateFormat _format = new SimpleDateFormat("dd/MM/yyyy");
    final String _imageFolderPath = new File("").getAbsolutePath() + "/";
    private int _userID = 0;
    private String _role = "";
    final String ADMIN_ROLE = "Quản lý";
    private int _selectedTable = 0;

    private DeleteValue _deleter = new DeleteValue();
    private ImageGenerator _imgGenerator = new ImageGenerator();
    private EquipmentDetailsController _eC = new EquipmentDetailsController();
    private SupplierController _sC = new SupplierController();
    private List<Supplier> _listOfSuppliers;
    private AddImage _addImgFrame;
    private AddEquipmentDetailsForm _eDsForm;

    private List<Equipment_Details> _listOfEquipmentDetails;
    private javax.swing.JFrame _parent;
    private javax.swing.JFrame _this;
    private SideBarFunction _sideBarFunctionFrm;
    private boolean _isCategoriesUpdate = false;
    private boolean _isPopupShow = false;
    private static AddEquipmentDetailsForm _obj = null;
    private String oldPicutre;
    private String _id;
    private String _imagePath = "";
    private String _importFolderPath = new File("").getAbsolutePath().concat("/src/file_import_details/");
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Button EquipmentEditConfirmButton;
    private Button EquipmentRefreshButton;
    private javax.swing.JLabel alertDCNCC;
    private javax.swing.JLabel alertGia;
    private javax.swing.JLabel alertMCTTB;
    private javax.swing.JLabel alertNCC;
    private javax.swing.JLabel alertSDTNCC;
    private javax.swing.JLabel alertTGBH;
    private javax.swing.JLabel alertTNCC;
    private javax.swing.JLabel alertTTB;
    private javax.swing.JLabel birthDayLb;
    private Button btnGetImage;
    private javax.swing.JButton btnRefreshNCC;
    private javax.swing.JButton btnSupplierConfirm;
    private Button btn_confirmCategories;
    private Button button1;
    private Button button5;
    private Button button6;
    private javax.swing.JScrollPane categoriesScrollPane;
    private javax.swing.JTable categoriesTable;
    private javax.swing.JComboBox<String> cbNhaCungCap;
    private javax.swing.JLabel contactLb;
    private javax.swing.JLabel createLb;
    private javax.swing.JLabel db_STC;
    private javax.swing.JLabel db_TBNTT;
    private javax.swing.JLabel db_txtDNH;
    private javax.swing.JLabel db_txtLTT;
    private javax.swing.JLabel db_txtNCC;
    private javax.swing.JLabel db_txtTb;
    private javax.swing.JComboBox<String> detailIDComboBox;
    private javax.swing.JLabel detailIDLabel;
    private javax.swing.JLabel emailLb;
    private javax.swing.JLabel equipmentIDLabel;
    private javax.swing.JTextField equipmentIDTextField;
    private javax.swing.JLabel equipmentNameLabel;
    private javax.swing.JTextField equipmentNameTextField;
    private javax.swing.JScrollPane equipmentsScrollPane;
    private javax.swing.JTable equipmentsTable;
    private javax.swing.JLabel fullNameLb;
    private javax.swing.JButton importAddButton;
    private javax.swing.JLabel importDateLabel;
    private javax.swing.JTextField importDateTextField;
    private javax.swing.JButton importDeleteButton;
    private javax.swing.JScrollPane importDetailsScrollPane;
    private javax.swing.JTable importDetailsTable;
    private javax.swing.JButton importEditButton;
    private javax.swing.JTable importEquipmentTable;
    private javax.swing.JLabel importIDLabel;
    private javax.swing.JTextField importIDTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lbl_appLogo;
    private javax.swing.JLabel lbl_close_hover;
    private javax.swing.JLabel lbl_date;
    private javax.swing.JLabel lbl_menuItem_1;
    private javax.swing.JLabel lbl_menuItem_2;
    private javax.swing.JLabel lbl_menuItem_3;
    private javax.swing.JLabel lbl_menuItem_4;
    private javax.swing.JLabel lbl_menuItem_5;
    private javax.swing.JLabel lbl_menuItem_6;
    private javax.swing.JLabel lbl_menuItem_7;
    private javax.swing.JLabel lbl_menuItem_8;
    private javax.swing.JLabel lbl_themCTTB;
    private javax.swing.JLabel lbl_themNCC;
    private javax.swing.JLabel lbl_time;
    private Button loadImportButton;
    private javax.swing.JPanel loginInfoPanel;
    private javax.swing.JScrollPane loginInfoScrollPane;
    private javax.swing.JTable loginInfoTable;
    private Button makeImportButton;
    private javax.swing.JLabel pictureFieldLabel;
    private javax.swing.JLabel pictureLabel;
    private javax.swing.JPanel pnl_appBar;
    private javax.swing.JPanel pnl_container;
    private javax.swing.JPanel pnl_dashboard;
    private javax.swing.JPanel pnl_eqsDetail;
    private javax.swing.JPanel pnl_equipments;
    private javax.swing.JPanel pnl_function;
    private javax.swing.JPanel pnl_imports;
    private javax.swing.JPanel pnl_logoAndName;
    private javax.swing.JPanel pnl_settings;
    private javax.swing.JPanel pnl_suppliers;
    private javax.swing.JPanel pnl_users;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JTextField priceTextField;
    private javax.swing.JLabel profilePictureLabel;
    private Button refreshImportButton;
    private Button saveImportButton;
    private javax.swing.JScrollPane scrollPane;
    private Button showLoginInfoButton;
    private Button signupButton;
    private javax.swing.JComboBox<String> statusComboBox;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel supplierLabel;
    private javax.swing.JTextField supplierTextField;
    private javax.swing.JScrollPane suppliersScrollPane;
    private javax.swing.JTable suppliersTable;
    private javax.swing.JTextField txtDiaChiNCC;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtMaChiTietTB;
    private javax.swing.JTextField txtMaNCC;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenNCC;
    private javax.swing.JTextField txtTenThietBi;
    private javax.swing.JTextField txtThoiGianBH;
    private javax.swing.JLabel updateLb;
    private javax.swing.JScrollPane usersScrollPane;
    private javax.swing.JTable usersTable;
    private javax.swing.JLabel warrantyLabel;
    private javax.swing.JTextField warrantyTextField;
    // End of variables declaration//GEN-END:variables
}
