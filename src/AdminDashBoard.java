
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
public class AdminDashBoard extends javax.swing.JFrame {

    /**
     * Creates new form AdminDashBoard
     */
    Border defaultBorder = BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(120, 128, 181));
    //yellow border form menu items        
    Border borderColor = BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(215, 220, 234));

    JLabel[] menuLabels = new JLabel[10];

    JPanel[] panels = new JPanel[7];

    int hour, minute, second;

    public AdminDashBoard() {
    }

    public AdminDashBoard(int userID, String role) {

        initComponents();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        _userID = userID;
        _role = role;
        _listOfSuppliers = _sC.getSuppliersInfo();

        lbl_appLogo.setIcon(ImageGenerator.ResizeImage(new ImageGenerator().getImageFolderPath() + "/src/icon/appLogo.jpg", lbl_appLogo));

        Border panelBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(242, 243, 248));
        pnl_logoAndName.setBorder(panelBorder);

        Border containerBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(120, 128, 181));
        pnl_container.setBorder(containerBorder);

        //populate the menuLabels array
        menuLabels[0] = lbl_menuItem_1;
        menuLabels[1] = lbl_menuItem_2;
        menuLabels[2] = lbl_menuItem_3;
        menuLabels[3] = lbl_menuItem_4;
        menuLabels[4] = lbl_menuItem_5;
        menuLabels[5] = lbl_menuItem_6;
        menuLabels[6] = lbl_menuItem_7;
        menuLabels[7] = lbl_menuItem_8;
        menuLabels[8] = lbl_time;
        menuLabels[9] = lbl_date;

        panels[0] = pnl_dashboard;
        panels[1] = pnl_users;
        panels[2] = pnl_equipments;
        panels[3] = pnl_eqsDetail;
        panels[4] = pnl_suppliers;
        panels[5] = pnl_imports;
        panels[6] = pnl_settings;

        panels[0].setVisible(
                true);
        for (int i = 1;
                i < panels.length;
                i++) {
            panels[i].setVisible(false);
        }

        addActionToMenuLabels();

        initTables();

        loadUserInfo();

        theader(categoriesTable);
        theader(usersTable);
        theader(suppliersTable);
        theader(importDetailsTable);
        theader(equipmentsTable);

        _eC = new EquipmentDetailsController();
        _sC = new SupplierController();
        _listOfSuppliers = new ArrayList<>();
        _listOfEquipmentDetails = _eC.getListEquipmentDetails();

        initCategoriesAlert();
        initSuppliersAlert();
        btnConfirm.setText("Thêm");
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

    }

    private void initRendererForEdit(String id) {
        initRenderer();
        loadCurrentData(id);
        equipmentIDTextField.setText(id);
        equipmentIDTextField.setEditable(false);
        amountLabel.setVisible(false);
        amountTextField.setVisible(false);
        confirmEditButton.setVisible(true);
        confirmButton.setVisible(false);
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

            detailIDComboBox.addItem("Thêm loại thiết bị...");
        } catch (SQLException ex) {
            Logger.getLogger(AddEquipmentForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadDetailIDComboBox(String detailID) {
        loadDetailIDComboBox();
        detailIDComboBox.setSelectedItem(detailID);
    }

    private void initRenderer() {
        confirmEditButton.setVisible(false);

        setAlertVisible(false);
        loadStatusComboBox();
        loadDetailIDComboBox();
        _detailUpdated = false;
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

        for (int i = 0; i < _importForm.checkIDList.size(); i++) {
            if (equipmentID.equals(_importForm.checkIDList.get(i))) {
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

    public void fillOutCategoriesInfo(String id) {

        int dem = 0;
        for (Equipment_Details eD : _listOfEquipmentDetails) {
            if (eD.getId().equals(id)) {
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
                } else {
                    btnGetImage.setText("Chọn hình ảnh!");
                }

                txtGia.setText(eD.getPrice() + "");
                txtThoiGianBH.setText(eD.getWarranty_time() + "");
                btnConfirm.setText("Cập nhật");
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
    }

    public void setLabelBackground(JLabel label) {
        for (JLabel menuLabel : menuLabels) {
            menuLabel.setBackground(new Color(120, 128, 181));
            menuLabel.setForeground(Color.white);
        }

        label.setBackground(Color.white);
        label.setForeground(new Color(120, 128, 181));
    }

    public void addActionToMenuLabels() {
        //get labels in the appBar menu
        Component[] components = pnl_appBar.getComponents();

        for (Component component : components) {
            if (component instanceof JLabel) {

                JLabel label = (JLabel) component;

                label.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent me) {
                        setLabelBackground(label);
                        switch (label.getText().trim()) {
                            case "Trang chủ":
                                showPanel(pnl_dashboard);
                                filterComboBox.removeAllItems();
                                break;
                            case "Nhân viên":
                                showPanel(pnl_users);
                                _selectedTable = 3;
                                initFilterComboBox(_selectedTable);
                                break;
                            case "Thiết bị":
                                showPanel(pnl_equipments);
                                _selectedTable = 1;
                                initFilterComboBox(_selectedTable);
                                break;
                            case "Chi tiết thiết bị":
                                showPanel(pnl_eqsDetail);
                                _selectedTable = 4;
                                initFilterComboBox(_selectedTable);
                                break;
                            case "Nhà cung cấp":
                                showPanel(pnl_suppliers);
                                _selectedTable = 5;
                                initFilterComboBox(_selectedTable);
                                break;
                            case "Đơn nhập":
                                showPanel(pnl_imports);
                                _selectedTable = 2;
                                initFilterComboBox(_selectedTable);
                                break;
                            case "Thống kê":
                                DrawGraph.createAndShowGui();
                                new PieChart();
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

    private void initTables() {
        initSorter();
        initFilter(equipmentsTable);
        initFilter(importDetailsTable);
        initFilter(usersTable);
        initFilter(categoriesTable);
        initFilter(suppliersTable);
        loadDatabase();
    }

    private void initSorter() {
        equipmentsTable.setAutoCreateRowSorter(true);
        importDetailsTable.setAutoCreateRowSorter(true);
        usersTable.setAutoCreateRowSorter(true);
        categoriesTable.setAutoCreateRowSorter(true);
        suppliersTable.setAutoCreateRowSorter(true);
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
                        if (_selectedTable == 1 || _selectedTable == 4) {
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

    private void initFilterComboBox(int currentTableIndex) {
        filterComboBox.removeAllItems();
        filterComboBox.addItem("Toàn bộ");

        JTable table;
        switch (currentTableIndex) {
            case 1:
                table = equipmentsTable;
                break;
            case 2:
                table = importDetailsTable;
                break;
            case 3:
                table = usersTable;
                break;
            case 4:
                table = categoriesTable;
                break;
            default:
                table = suppliersTable;
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

    private void loadUserInfos() {
        DefaultTableModel tableModel = (DefaultTableModel) usersTable.getModel();
        tableModel.setNumRows(0);

        SimpleDateFormat dateFormater = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
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
                vector.add(dateFormater.format(rs.getTimestamp("updated_at")));
                tableModel.addRow(vector);
            }
            usersTable.setModel(tableModel);
            rs.close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(AdminDashBoard.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadUserInfo() {
        Connection connectDB = ConnectMysql.getConnectDB();
        String userQuery = "SELECT * FROM `users` WHERE id = " + _userID;

        try {
            ResultSet rs = connectDB.createStatement().executeQuery(userQuery);
            rs.next();
            fullNameLb.setText(rs.getString("firstName") + " " + rs.getString("lastName"));
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
            Logger.getLogger(AdminDashBoard.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            importDetailsTable.setModel(tableModel);
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
            equipmentsTable.setModel(tableModel);
            rs.close();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(AdminDashBoard.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
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
                _listOfSuppliers.get(equipment_info.getSupplier_id()).getName()
            });
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

        if (table == categoriesTable) {
            if (column == 2 && row <= lED.size()) {
                if (lED.get(row).getPicture() != null) {
                    ShowImageFrame sIF = ShowImageFrame.getObj();
                    sIF.setVisible(true);
                    String image = _imageFolderPath + lED.get(row).getPicture();
                    sIF.getShowImageLbl().setIcon(ImageGenerator.ResizeImage(image, sIF.getShowImageLbl()));
                }
            }
        } else if (table == equipmentsTable) {
            if (column == 2) {
                String imagePath = getEquipmentImage(table.getValueAt(row, 3).toString());
                imagePath = imagePath == null ? "" : imagePath;
                if (!imagePath.equals("")) {
                    ShowImageFrame sIF = ShowImageFrame.getObj();
                    sIF.setVisible(true);
                    String image = _imageFolderPath + imagePath;
                    sIF.getShowImageLbl().setIcon(ImageGenerator.ResizeImage(image, sIF.getShowImageLbl()));
                }
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
        pnl_function = new javax.swing.JPanel();
        lbl_close_hover = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        filterComboBox = new javax.swing.JComboBox<>();
        editButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        button2 = new Button();
        button3 = new Button();
        button4 = new Button();
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
        confirmButton = new javax.swing.JButton();
        amountLabel = new javax.swing.JLabel();
        amountTextField = new javax.swing.JTextField();
        pictureLabel = new javax.swing.JLabel();
        amountAlertLabel = new javax.swing.JLabel();
        equipmentIDAlertLabel = new javax.swing.JLabel();
        detailIDAlertLabel = new javax.swing.JLabel();
        equipmentIDLabel = new javax.swing.JLabel();
        confirmEditButton = new javax.swing.JButton();
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
        jButton2 = new javax.swing.JButton();
        pnl_settings = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        fullNameLb = new javax.swing.JLabel();
        birthDayLb = new javax.swing.JLabel();
        emailLb = new javax.swing.JLabel();
        contactLb = new javax.swing.JLabel();
        createLb = new javax.swing.JLabel();
        updateLb = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        button1 = new Button();
        pnl_eqsDetail = new javax.swing.JPanel();
        txtTenThietBi = new javax.swing.JTextField();
        btnRefreshCTTB = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        alertMCTTB = new javax.swing.JLabel();
        txtGia = new javax.swing.JTextField();
        alertTTB = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        alertNCC = new javax.swing.JLabel();
        btnConfirm = new javax.swing.JButton();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        pnl_container.setBackground(new java.awt.Color(255, 255, 255));

        pnl_appBar.setBackground(new java.awt.Color(120, 128, 181));

        pnl_logoAndName.setBackground(new java.awt.Color(120, 128, 181));

        lbl_appLogo.setBackground(new java.awt.Color(120, 128, 181));
        lbl_appLogo.setOpaque(true);

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(228, 231, 241));
        jLabel8.setText("My Application");

        javax.swing.GroupLayout pnl_logoAndNameLayout = new javax.swing.GroupLayout(pnl_logoAndName);
        pnl_logoAndName.setLayout(pnl_logoAndNameLayout);
        pnl_logoAndNameLayout.setHorizontalGroup(
            pnl_logoAndNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_logoAndNameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_appLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
        );
        pnl_logoAndNameLayout.setVerticalGroup(
            pnl_logoAndNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_logoAndNameLayout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(pnl_logoAndNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_appLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        lbl_menuItem_2.setBackground(new java.awt.Color(120, 128, 181));
        lbl_menuItem_2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_menuItem_2.setForeground(new java.awt.Color(255, 255, 255));
        lbl_menuItem_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/profile.png"))); // NOI18N
        lbl_menuItem_2.setText("     Nhân viên");
        lbl_menuItem_2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_menuItem_2.setOpaque(true);

        lbl_menuItem_3.setBackground(new java.awt.Color(120, 128, 181));
        lbl_menuItem_3.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_menuItem_3.setForeground(new java.awt.Color(255, 255, 255));
        lbl_menuItem_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/punching-bag.png"))); // NOI18N
        lbl_menuItem_3.setText("     Thiết bị");
        lbl_menuItem_3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_menuItem_3.setOpaque(true);

        lbl_menuItem_4.setBackground(new java.awt.Color(120, 128, 181));
        lbl_menuItem_4.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_menuItem_4.setForeground(new java.awt.Color(255, 255, 255));
        lbl_menuItem_4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/details.png"))); // NOI18N
        lbl_menuItem_4.setText("     Chi tiết thiết bị");
        lbl_menuItem_4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_menuItem_4.setOpaque(true);

        lbl_menuItem_5.setBackground(new java.awt.Color(120, 128, 181));
        lbl_menuItem_5.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_menuItem_5.setForeground(new java.awt.Color(255, 255, 255));
        lbl_menuItem_5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/inventory.png"))); // NOI18N
        lbl_menuItem_5.setText("     Nhà cung cấp");
        lbl_menuItem_5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_menuItem_5.setOpaque(true);

        lbl_menuItem_1.setBackground(new java.awt.Color(120, 128, 181));
        lbl_menuItem_1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_menuItem_1.setForeground(new java.awt.Color(255, 255, 255));
        lbl_menuItem_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/business-report.png"))); // NOI18N
        lbl_menuItem_1.setText("     Trang chủ");
        lbl_menuItem_1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_menuItem_1.setOpaque(true);

        lbl_menuItem_6.setBackground(new java.awt.Color(120, 128, 181));
        lbl_menuItem_6.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_menuItem_6.setForeground(new java.awt.Color(255, 255, 255));
        lbl_menuItem_6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/import.png"))); // NOI18N
        lbl_menuItem_6.setText("     Đơn nhập");
        lbl_menuItem_6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_menuItem_6.setOpaque(true);

        lbl_menuItem_7.setBackground(new java.awt.Color(120, 128, 181));
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

        lbl_menuItem_8.setBackground(new java.awt.Color(120, 128, 181));
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

        lbl_time.setBackground(new java.awt.Color(120, 128, 181));
        lbl_time.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_time.setForeground(new java.awt.Color(255, 255, 255));
        lbl_time.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/clock.png"))); // NOI18N
        lbl_time.setText("    Thời gian");
        lbl_time.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_time.setOpaque(true);
        lbl_time.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_timeMouseClicked(evt);
            }
        });

        lbl_date.setBackground(new java.awt.Color(120, 128, 181));
        lbl_date.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        lbl_date.setForeground(new java.awt.Color(255, 255, 255));
        lbl_date.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/clock.png"))); // NOI18N
        lbl_date.setText("    Ngày");
        lbl_date.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_date.setOpaque(true);
        lbl_date.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_dateMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnl_appBarLayout = new javax.swing.GroupLayout(pnl_appBar);
        pnl_appBar.setLayout(pnl_appBarLayout);
        pnl_appBarLayout.setHorizontalGroup(
            pnl_appBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_logoAndName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_menuItem_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_menuItem_5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_menuItem_3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_menuItem_4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_menuItem_1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_menuItem_7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_menuItem_6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_menuItem_8, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(lbl_time, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_date, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnl_appBarLayout.setVerticalGroup(
            pnl_appBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_appBarLayout.createSequentialGroup()
                .addComponent(pnl_logoAndName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_time, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_date, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnl_function.setBackground(new java.awt.Color(120, 128, 181));
        pnl_function.setPreferredSize(new java.awt.Dimension(1200, 115));

        lbl_close_hover.setBackground(new java.awt.Color(255, 255, 255));
        lbl_close_hover.setForeground(new java.awt.Color(255, 255, 255));
        lbl_close_hover.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_close_hover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/close1.png"))); // NOI18N
        lbl_close_hover.setOpaque(true);
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

        removeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/delete.png"))); // NOI18N
        removeButton.setEnabled(false);
        removeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        removeButton.setPreferredSize(new java.awt.Dimension(107, 23));
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(197, 255, 253));
        jLabel6.setText("Chức Năng:");

        jLabel36.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(197, 255, 253));
        jLabel36.setText("Tìm Kiếm:");

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

        javax.swing.GroupLayout pnl_functionLayout = new javax.swing.GroupLayout(pnl_function);
        pnl_function.setLayout(pnl_functionLayout);
        pnl_functionLayout.setHorizontalGroup(
            pnl_functionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_functionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_functionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_functionLayout.createSequentialGroup()
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_functionLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(pnl_functionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnl_functionLayout.createSequentialGroup()
                        .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(button4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnl_functionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_functionLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_close_hover, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl_functionLayout.createSequentialGroup()
                        .addGap(221, 221, 221)
                        .addComponent(removeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 74, Short.MAX_VALUE))))
        );
        pnl_functionLayout.setVerticalGroup(
            pnl_functionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_functionLayout.createSequentialGroup()
                .addGroup(pnl_functionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_functionLayout.createSequentialGroup()
                        .addComponent(lbl_close_hover, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnl_functionLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnl_functionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(filterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGroup(pnl_functionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_functionLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnl_functionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(removeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_functionLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnl_functionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(button3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_functionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6)))))
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
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Số tiền chi ");
        jLabel12.setOpaque(true);

        db_STC.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        db_STC.setForeground(new java.awt.Color(0, 0, 0));
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_usersLayout.createSequentialGroup()
                .addContainerGap(254, Short.MAX_VALUE)
                .addComponent(usersScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnl_usersLayout.setVerticalGroup(
            pnl_usersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_usersLayout.createSequentialGroup()
                .addGap(0, 285, Short.MAX_VALUE)
                .addComponent(usersScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnl_equipments.setBackground(new java.awt.Color(255, 255, 255));

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

        confirmButton.setText("Xác nhận");
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });
        jPanel1.add(confirmButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 670, -1, 36));

        amountLabel.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        amountLabel.setText("Số lượng:");
        jPanel1.add(amountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 620, -1, -1));

        amountTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        amountTextField.setText("1");
        jPanel1.add(amountTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 620, 42, 38));

        pictureLabel.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        pictureLabel.setText("Hình ảnh:");
        jPanel1.add(pictureLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 482, -1, -1));

        amountAlertLabel.setForeground(new java.awt.Color(255, 0, 0));
        amountAlertLabel.setText("Nhập số lượng từ 1 - 99");
        jPanel1.add(amountAlertLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 630, -1, -1));

        equipmentIDAlertLabel.setForeground(new java.awt.Color(255, 0, 0));
        equipmentIDAlertLabel.setText("Nhập mã thiết bị 3 chữ");
        jPanel1.add(equipmentIDAlertLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 98, -1, -1));

        detailIDAlertLabel.setForeground(new java.awt.Color(255, 51, 0));
        detailIDAlertLabel.setText("Chọn 1 mã thiết bị");
        jPanel1.add(detailIDAlertLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 245, -1, -1));

        equipmentIDLabel.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        equipmentIDLabel.setText("Mã thiết bị:");
        jPanel1.add(equipmentIDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 54, -1, -1));

        confirmEditButton.setText("Xác nhận");
        confirmEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmEditButtonActionPerformed(evt);
            }
        });
        jPanel1.add(confirmEditButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 670, -1, 36));
        jPanel1.add(equipmentIDTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 49, 247, 37));

        pictureFieldLabel.setBackground(new java.awt.Color(204, 204, 255));
        pictureFieldLabel.setOpaque(true);
        jPanel1.add(pictureFieldLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 482, 230, 123));

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

        jButton2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cancel.png"))); // NOI18N
        jButton2.setText("Hủy bỏ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 670, -1, -1));

        javax.swing.GroupLayout pnl_equipmentsLayout = new javax.swing.GroupLayout(pnl_equipments);
        pnl_equipments.setLayout(pnl_equipmentsLayout);
        pnl_equipmentsLayout.setHorizontalGroup(
            pnl_equipmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_equipmentsLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(equipmentsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );
        pnl_equipmentsLayout.setVerticalGroup(
            pnl_equipmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_equipmentsLayout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addComponent(equipmentsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
        );

        pnl_settings.setBackground(new java.awt.Color(254, 217, 155));
        pnl_settings.setForeground(new java.awt.Color(7, 16, 19));
        pnl_settings.setPreferredSize(new java.awt.Dimension(1200, 722));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("Tên:");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setText("Ngày sinh:");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel3.setText("Email:");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel4.setText("Số điện thoại:");

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel5.setText("Ngày tạo:");

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel7.setText("Ngày cập nhật:");

        fullNameLb.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        fullNameLb.setText("kjbsdkfblsbadfkbbsklabkvxczzxv");

        birthDayLb.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        birthDayLb.setText("sadfasdxczvxcvxzcvxzvc");

        emailLb.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        emailLb.setText("xzcvzxcvzxvczxcvxzcv");

        contactLb.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        contactLb.setText("asfdsadxzcvxcvz");

        createLb.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        createLb.setText("xzcvxzcvxzcvxzcv");

        updateLb.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        updateLb.setText("bkxc cbkxb kbxk bkx");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 0, 0));
        jLabel24.setText("CÀI ĐẶT");

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

        javax.swing.GroupLayout pnl_settingsLayout = new javax.swing.GroupLayout(pnl_settings);
        pnl_settings.setLayout(pnl_settingsLayout);
        pnl_settingsLayout.setHorizontalGroup(
            pnl_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_settingsLayout.createSequentialGroup()
                .addContainerGap(411, Short.MAX_VALUE)
                .addGroup(pnl_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_settingsLayout.createSequentialGroup()
                        .addGroup(pnl_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7))
                        .addGap(27, 27, 27)
                        .addGroup(pnl_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fullNameLb)
                            .addComponent(emailLb)
                            .addComponent(birthDayLb)
                            .addComponent(createLb)
                            .addComponent(contactLb)
                            .addComponent(updateLb))
                        .addGap(387, 387, 387))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_settingsLayout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(555, 555, 555))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_settingsLayout.createSequentialGroup()
                        .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))))
        );
        pnl_settingsLayout.setVerticalGroup(
            pnl_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_settingsLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel24)
                .addGap(106, 106, 106)
                .addGroup(pnl_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(fullNameLb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(birthDayLb))
                .addGap(18, 18, 18)
                .addGroup(pnl_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(emailLb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(contactLb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(createLb))
                .addGap(18, 18, 18)
                .addGroup(pnl_settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(updateLb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 253, Short.MAX_VALUE)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        pnl_eqsDetail.setBackground(new java.awt.Color(197, 255, 253));
        pnl_eqsDetail.setPreferredSize(new java.awt.Dimension(1200, 710));
        pnl_eqsDetail.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTenThietBi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTenThietBiKeyReleased(evt);
            }
        });
        pnl_eqsDetail.add(txtTenThietBi, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 160, 200, 40));

        btnRefreshCTTB.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnRefreshCTTB.setForeground(new java.awt.Color(0, 0, 255));
        btnRefreshCTTB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.png"))); // NOI18N
        btnRefreshCTTB.setText("Làm mới");
        btnRefreshCTTB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshCTTBActionPerformed(evt);
            }
        });
        pnl_eqsDetail.add(btnRefreshCTTB, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 300, -1, -1));

        jLabel22.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(36, 11, 54));
        jLabel22.setText("Nhà cung cấp :");
        jLabel22.setToolTipText("");
        pnl_eqsDetail.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 160, -1, -1));

        alertMCTTB.setForeground(new java.awt.Color(255, 0, 0));
        alertMCTTB.setText("Mã CTBB alert");
        pnl_eqsDetail.add(alertMCTTB, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 130, -1, -1));

        txtGia.setBorder(null);
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

        btnConfirm.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnConfirm.setForeground(new java.awt.Color(0, 153, 0));
        btnConfirm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/plus.png"))); // NOI18N
        btnConfirm.setText("Thêm");
        btnConfirm.setToolTipText("");
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
            }
        });
        pnl_eqsDetail.add(btnConfirm, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 300, 135, -1));

        alertGia.setForeground(new java.awt.Color(255, 0, 0));
        alertGia.setText("Giá TB alert");
        pnl_eqsDetail.add(alertGia, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 130, -1, -1));

        lbl_themCTTB.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lbl_themCTTB.setForeground(new java.awt.Color(51, 51, 255));
        lbl_themCTTB.setText("THÊM CHI TIẾT THIẾT BỊ");
        pnl_eqsDetail.add(lbl_themCTTB, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 12, -1, -1));

        jLabel26.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(36, 11, 54));
        jLabel26.setText("Thời gian bảo hành:");
        pnl_eqsDetail.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 240, -1, -1));

        alertTGBH.setForeground(new java.awt.Color(255, 0, 0));
        alertTGBH.setText("TGBH alert");
        pnl_eqsDetail.add(alertTGBH, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 270, -1, -1));

        txtThoiGianBH.setBorder(null);
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

        pnl_suppliers.setBackground(new java.awt.Color(255, 255, 255));
        pnl_suppliers.setPreferredSize(new java.awt.Dimension(1200, 710));

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
                .addComponent(suppliersScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 608, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnl_suppliersLayout.setVerticalGroup(
            pnl_suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(suppliersScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(pnl_suppliersLayout.createSequentialGroup()
                .addContainerGap(86, Short.MAX_VALUE)
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

        javax.swing.GroupLayout pnl_importsLayout = new javax.swing.GroupLayout(pnl_imports);
        pnl_imports.setLayout(pnl_importsLayout);
        pnl_importsLayout.setHorizontalGroup(
            pnl_importsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1189, Short.MAX_VALUE)
            .addGroup(pnl_importsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl_importsLayout.createSequentialGroup()
                    .addGap(0, 235, Short.MAX_VALUE)
                    .addComponent(importDetailsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 234, Short.MAX_VALUE)))
        );
        pnl_importsLayout.setVerticalGroup(
            pnl_importsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 710, Short.MAX_VALUE)
            .addGroup(pnl_importsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl_importsLayout.createSequentialGroup()
                    .addGap(0, 142, Short.MAX_VALUE)
                    .addComponent(importDetailsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 143, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout pnl_containerLayout = new javax.swing.GroupLayout(pnl_container);
        pnl_container.setLayout(pnl_containerLayout);
        pnl_containerLayout.setHorizontalGroup(
            pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_containerLayout.createSequentialGroup()
                .addComponent(pnl_appBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnl_function, javax.swing.GroupLayout.DEFAULT_SIZE, 1249, Short.MAX_VALUE)
                    .addComponent(pnl_dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 499, Short.MAX_VALUE)
                    .addComponent(pnl_users, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 235, Short.MAX_VALUE)
                    .addComponent(pnl_equipments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 236, Short.MAX_VALUE)
                    .addComponent(pnl_settings, javax.swing.GroupLayout.PREFERRED_SIZE, 1249, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 236, Short.MAX_VALUE)
                    .addComponent(pnl_eqsDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 1249, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 285, Short.MAX_VALUE)
                    .addComponent(pnl_suppliers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(284, 284, 284)
                    .addComponent(pnl_imports, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        pnl_containerLayout.setVerticalGroup(
            pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_appBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnl_containerLayout.createSequentialGroup()
                .addComponent(pnl_function, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 149, Short.MAX_VALUE)
                    .addComponent(pnl_users, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 123, Short.MAX_VALUE)
                    .addComponent(pnl_equipments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 111, Short.MAX_VALUE)
                    .addComponent(pnl_settings, javax.swing.GroupLayout.PREFERRED_SIZE, 762, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 115, Short.MAX_VALUE)
                    .addComponent(pnl_eqsDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 758, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(0, 137, Short.MAX_VALUE)
                    .addComponent(pnl_suppliers, javax.swing.GroupLayout.PREFERRED_SIZE, 722, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnl_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_containerLayout.createSequentialGroup()
                    .addGap(124, 124, 124)
                    .addComponent(pnl_imports, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        lbl_close_hover.setSize(24, 24);
        lbl_close_hover.setIcon(ImageGenerator.ResizeImage(new ImageGenerator().getImageFolderPath() + "/src/icon/close1.png", lbl_close_hover));
    }//GEN-LAST:event_lbl_close_hoverMouseExited

    private void usersTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usersTableMouseClicked
        editButton.setEnabled(false);
        removeButton.setEnabled(false);
    }//GEN-LAST:event_usersTableMouseClicked

    private void equipmentsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_equipmentsTableMouseClicked
        editButton.setEnabled(true);
        removeButton.setEnabled(true);
        tableImageCellCallback(evt, equipmentsTable);
    }//GEN-LAST:event_equipmentsTableMouseClicked

    private void importDetailsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_importDetailsTableMouseClicked
        editButton.setEnabled(false);
        removeButton.setEnabled(true);
    }//GEN-LAST:event_importDetailsTableMouseClicked

    private void suppliersTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_suppliersTableMouseClicked
        editButton.setEnabled(true);
        removeButton.setEnabled(true);
    }//GEN-LAST:event_suppliersTableMouseClicked

    private void lbl_menuItem_7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_menuItem_7MouseClicked


    }//GEN-LAST:event_lbl_menuItem_7MouseClicked

    private void filterComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterComboBoxItemStateChanged
        searchTextField.setText("");
    }//GEN-LAST:event_filterComboBoxItemStateChanged

    private void lbl_menuItem_8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_menuItem_8MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_menuItem_8MouseClicked

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        dispose();
        LoginForm.create();
    }//GEN-LAST:event_button1ActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        try {
            switch (_selectedTable) {
                case 1: {
                    String id = equipmentsTable.getValueAt(equipmentsTable.getSelectedRow(), 0).toString();
                    initRendererForEdit(id);
                    break;
                }
                case 2: {
                    break;
                }
                case 3: {
                    break;
                }
                case 4: {
                    String id = categoriesTable.getValueAt(categoriesTable.getSelectedRow(), 0).toString();
                    System.out.println(id);
                    fillOutCategoriesInfo(id);

                    _isCategoriesUpdate = true;
                    lbl_themCTTB.setText("CẬP NHẬT THIẾT BỊ");
                    btnConfirm.setText("Cập nhật");
                    break;
                }
                case 5: {
                    String index = suppliersTable.getValueAt(suppliersTable.getSelectedRow(), 0).toString();
                    int idx = Integer.parseInt(index);
                    fillOutSupplierInfo(idx);
                    lbl_themNCC.setText("CẬP NHẬT NHÀ CUNG CẤP");
                    btnSupplierConfirm.setText("Cập nhật");
                    break;
                }
                default:
                    break;
            }
            loadDatabase();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Vui lòng chọn dòng trước khi edit");
        }

    }//GEN-LAST:event_editButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        int result = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa?", "Cảnh báo!",
                JOptionPane.YES_NO_OPTION);
        switch (_selectedTable) {
            case 1: {
                if (result == JOptionPane.YES_OPTION) {
                    String id = equipmentsTable.getValueAt(equipmentsTable.getSelectedRow(), 0).toString();
                    _deleter.deleteEquipment(id);
                }
                break;
            }
            case 2: {
                if (result == JOptionPane.YES_OPTION) {
                    int id = Integer.valueOf(importDetailsTable.getValueAt(importDetailsTable.getSelectedRow(), 0).toString());
                    _deleter.deleteImport(id);
                }
                break;
            }
            case 3: {
                if (result == JOptionPane.YES_OPTION) {
                    int id = Integer.valueOf(usersTable.getValueAt(usersTable.getSelectedRow(), 0).toString());
                    _deleter.deleteUser(id);
                }

                break;
            }
            case 4: {
                if (result == JOptionPane.YES_OPTION) {
                    String id = categoriesTable.getValueAt(categoriesTable.getSelectedRow(), 0).toString();
                    _deleter.deleteEquipmentDetail(id);
                }

                break;
            }
            case 5: {
                if (result == JOptionPane.YES_OPTION) {
                    int id = Integer.valueOf(suppliersTable.getValueAt(suppliersTable.getSelectedRow(), 0).toString());
                    _deleter.deleteSupplier(id);
                }

                break;
            }
            default:
                break;
        }
        loadDatabase();
    }//GEN-LAST:event_removeButtonActionPerformed

    private void txtTenThietBiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTenThietBiKeyReleased
        String tenTB = txtTenThietBi.getText();

        if (tenTB.matches("^[a-zA-Z]+$") && tenTB.length() > 0) {
            initAlertLabel(alertTTB);
        }
    }//GEN-LAST:event_txtTenThietBiKeyReleased

    private void btnRefreshCTTBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshCTTBActionPerformed
        resetCategoriesField();
        initCategoriesAlert();
        if (_listOfSuppliers.size() != _sC.getSuppliersInfo().size()) {
            getSupplierList();
        }
    }//GEN-LAST:event_btnRefreshCTTBActionPerformed

    private void txtGiaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGiaKeyReleased
        String gia = txtGia.getText();
        if (gia.matches("^[\\d]+$") && Integer.parseInt(gia) >= 10000) {
            initAlertLabel(alertGia);
        }
    }//GEN-LAST:event_txtGiaKeyReleased

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        initCategoriesAlert();
        String maChiTietTB = txtMaChiTietTB.getText();
        String tenThietbi = txtTenThietBi.getText();
        String hinhAnh;

        if (_isCategoriesUpdate) {
            if (btnGetImage.getText().equals("Chọn hình ảnh!")) {
                hinhAnh = null;
            } else if (btnGetImage.getText().contains("/")) {
                hinhAnh = btnGetImage.getText();
            } else {
                hinhAnh = "/src/images/" + btnGetImage.getText();
            }
        } else {
            if (btnGetImage.getText().equals("Chọn hình ảnh!")) {
                hinhAnh = null;
            } else {
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
        } else if (Integer.parseInt(thoigianBH) > 10 || Integer.parseInt(thoigianBH) < 0) {
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
                    } catch (NullPointerException e) {
                        System.out.println("Waiting");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất  bại");
                }
            } else {
                if (_eC.addNewEquipmentDetails(eD)) {
                    new AlertFrame("Thêm thành công").setVisible(true);
                    if (hinhAnh != null) {
                        _addImgFrame.saveImage();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất  bại");
                }
            }
            loadDatabase();
            resetCategoriesField();
        }
    }//GEN-LAST:event_btnConfirmActionPerformed

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
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
                }
            } else {
                if (_sC.addNewSupplier(s)) {
                    new AlertFrame("Thêm thành công").setVisible(true);
                } else {
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
        editButton.setEnabled(true);
        removeButton.setEnabled(true);
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

    private void confirmEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmEditButtonActionPerformed
        if (detailIDComboBox.getSelectedItem().toString().equals("")) {
            detailIDAlertLabel.setVisible(true);
            return;
        }

        String id = equipmentIDTextField.getText();
        String status = statusComboBox.getSelectedItem().toString();
        String detailID = detailIDComboBox.getSelectedItem().toString();

        editEquipment(id, status, detailID);
        JOptionPane.showMessageDialog(null, "Chỉnh sửa thành công");
        loadDatabase();
    }//GEN-LAST:event_confirmEditButtonActionPerformed

    private void detailIDComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_detailIDComboBoxItemStateChanged

    }//GEN-LAST:event_detailIDComboBoxItemStateChanged

    private void supplierTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierTextFieldActionPerformed

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonActionPerformed
        int amount = 0, price = 0;
        String id = equipmentIDTextField.getText().toUpperCase();
        String name = equipmentNameTextField.getText();
        String status = statusComboBox.getSelectedItem().toString();
        String picture = _imagePath;
        String detailID = detailIDComboBox.getSelectedItem().toString();

        if (!checkValues(id, amountTextField.getText(), detailID)) {
            return;
        }

        amount = Integer.valueOf(amountTextField.getText());
        price = Integer.valueOf(priceTextField.getText());

        int incID = getMaxEquimentID(id);

        for (int i = 0; i < amount; i++) {
            incID++;
            String equipmentID = id + String.format("-%03d", incID);
            _importForm.addEquiment(equipmentID, name, status, price, picture, detailID);
        }
        _importForm.getParent().loadDatabase();
        this.dispose();
    }//GEN-LAST:event_confirmButtonActionPerformed

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        setLabelBackground(lbl_menuItem_4);
        showPanel(pnl_eqsDetail);
    }//GEN-LAST:event_button2ActionPerformed

    private void button3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button3ActionPerformed
        setLabelBackground(lbl_menuItem_5);
        showPanel(pnl_suppliers);
    }//GEN-LAST:event_button3ActionPerformed

    private void button4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button4ActionPerformed
        ImportForm importForm = new ImportForm(this, _userID);
        importForm.setLocationRelativeTo(this);
        importForm.setVisible(true);
    }//GEN-LAST:event_button4ActionPerformed

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
            } else {
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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        initRenderer();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void lbl_timeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_timeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_timeMouseClicked

    private void lbl_dateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_dateMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lbl_dateMouseClicked

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

        btnConfirm.setText("Cập nhật");
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
    private boolean _isCategoriesUpdate = false;
    private static AddEquipmentDetailsForm _obj = null;
    private String oldPicutre;
    private ImportForm _importForm = null;
    private AdminDashBoard _admDb = null;
    private String _id;
    private String _imagePath = "";
    private boolean _detailUpdated = false;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel alertDCNCC;
    private javax.swing.JLabel alertGia;
    private javax.swing.JLabel alertMCTTB;
    private javax.swing.JLabel alertNCC;
    private javax.swing.JLabel alertSDTNCC;
    private javax.swing.JLabel alertTGBH;
    private javax.swing.JLabel alertTNCC;
    private javax.swing.JLabel alertTTB;
    private javax.swing.JLabel amountAlertLabel;
    private javax.swing.JLabel amountLabel;
    private javax.swing.JTextField amountTextField;
    private javax.swing.JLabel birthDayLb;
    private javax.swing.JButton btnConfirm;
    private Button btnGetImage;
    private javax.swing.JButton btnRefreshCTTB;
    private javax.swing.JButton btnRefreshNCC;
    private javax.swing.JButton btnSupplierConfirm;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private javax.swing.JScrollPane categoriesScrollPane;
    private javax.swing.JTable categoriesTable;
    private javax.swing.JComboBox<String> cbNhaCungCap;
    private javax.swing.JButton confirmButton;
    private javax.swing.JButton confirmEditButton;
    private javax.swing.JLabel contactLb;
    private javax.swing.JLabel createLb;
    private javax.swing.JLabel db_STC;
    private javax.swing.JLabel db_TBNTT;
    private javax.swing.JLabel db_txtDNH;
    private javax.swing.JLabel db_txtLTT;
    private javax.swing.JLabel db_txtNCC;
    private javax.swing.JLabel db_txtTb;
    private javax.swing.JLabel detailIDAlertLabel;
    private javax.swing.JComboBox<String> detailIDComboBox;
    private javax.swing.JLabel detailIDLabel;
    private javax.swing.JButton editButton;
    private javax.swing.JLabel emailLb;
    private javax.swing.JLabel equipmentIDAlertLabel;
    private javax.swing.JLabel equipmentIDLabel;
    private javax.swing.JTextField equipmentIDTextField;
    private javax.swing.JLabel equipmentNameLabel;
    private javax.swing.JTextField equipmentNameTextField;
    private javax.swing.JScrollPane equipmentsScrollPane;
    private javax.swing.JTable equipmentsTable;
    private javax.swing.JComboBox<String> filterComboBox;
    private javax.swing.JLabel fullNameLb;
    private javax.swing.JScrollPane importDetailsScrollPane;
    private javax.swing.JTable importDetailsTable;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
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
    private javax.swing.JButton removeButton;
    private javax.swing.JTextField searchTextField;
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
