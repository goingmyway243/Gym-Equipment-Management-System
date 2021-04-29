
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

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

    //===========================||==========================================
    //======================PUBLIC ZONE=====================================
    //===========================V===========================================
    public MainMenu(int userID, String role) {
        initComponents();
        setResizable(false);
        initTables();

        _userID = userID;
        _role = role;
    }

    @Override
    public void exit() {
        setEnabled(true);
        setVisible(true);
        _settingFrom = null;
    }

    @Override
    public void logout() {
        dispose();
        _settingFrom = null;
    }

    public void loadDatabase() {
        loadUserInfos();
        loadSuppliers();
        loadEquipmentDetails();
        loadEquipments();
        loadImportDetails();
    }

    //===========================||==========================================
    //======================PRIVATE ZONE=====================================
    //===========================V===========================================
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
        if (currentTableIndex == 1) {
            table = equipmentsTable;
        } else if (currentTableIndex == 2) {
            table = importDetailsTable;
        } else if (currentTableIndex == 3) {
            table = usersTable;
        } else if (currentTableIndex == 4) {
            table = categoriesTable;
        } else {
            table = suppliersTable;
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
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadSuppliers() {
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
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadEquipments() {
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
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadEquipmentDetails() {
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
                equipment_info.getSupplier_id()
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
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
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
        searchTextField = new javax.swing.JTextField();
        searchLabel = new javax.swing.JLabel();
        filterComboBox = new javax.swing.JComboBox<>();

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

        mainTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mainTabbedPaneStateChanged(evt);
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
        equipmentsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                equipmentsTableMouseClicked(evt);
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
        importDetailsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                importDetailsTableMouseClicked(evt);
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
        usersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                usersTableMouseClicked(evt);
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
        categoriesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                categoriesTableMouseClicked(evt);
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
        suppliersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                suppliersTableMouseClicked(evt);
            }
        });
        suppliersScrollPane.setViewportView(suppliersTable);

        mainTabbedPane.addTab("Nhà cung cấp", suppliersScrollPane);

        editButton.setText("Sửa...");
        editButton.setEnabled(false);
        editButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editButton.setPreferredSize(new java.awt.Dimension(107, 23));
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        removeButton.setText("Xóa...");
        removeButton.setEnabled(false);
        removeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        removeButton.setPreferredSize(new java.awt.Dimension(107, 23));
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        searchLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        searchLabel.setForeground(new java.awt.Color(204, 204, 255));
        searchLabel.setText("Tìm kiếm");

        filterComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                filterComboBoxItemStateChanged(evt);
            }
        });

        MainDesktopPane.setLayer(titleLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainDesktopPane.setLayer(addCategoryButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainDesktopPane.setLayer(addSupplierButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainDesktopPane.setLayer(newImportButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainDesktopPane.setLayer(settingButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainDesktopPane.setLayer(mainTabbedPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainDesktopPane.setLayer(editButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainDesktopPane.setLayer(removeButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainDesktopPane.setLayer(searchTextField, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainDesktopPane.setLayer(searchLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        MainDesktopPane.setLayer(filterComboBox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout MainDesktopPaneLayout = new javax.swing.GroupLayout(MainDesktopPane);
        MainDesktopPane.setLayout(MainDesktopPaneLayout);
        MainDesktopPaneLayout.setHorizontalGroup(
            MainDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainDesktopPaneLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(MainDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MainDesktopPaneLayout.createSequentialGroup()
                        .addGroup(MainDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(MainDesktopPaneLayout.createSequentialGroup()
                                .addComponent(searchLabel)
                                .addGap(18, 18, 18)
                                .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(filterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(mainTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 725, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 152, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainDesktopPaneLayout.createSequentialGroup()
                        .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(197, 197, 197))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(MainDesktopPaneLayout.createSequentialGroup()
                            .addComponent(settingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainDesktopPaneLayout.createSequentialGroup()
                            .addGroup(MainDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(removeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                                .addComponent(editButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addSupplierButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(newImportButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addCategoryButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(17, 17, 17)))))
        );
        MainDesktopPaneLayout.setVerticalGroup(
            MainDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainDesktopPaneLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(MainDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchLabel)
                    .addComponent(filterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainDesktopPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainDesktopPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newImportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newImportButtonActionPerformed
        ImportForm importForm = new ImportForm(this, _userID);
        importForm.setLocationRelativeTo(this);
        importForm.setVisible(true);
    }//GEN-LAST:event_newImportButtonActionPerformed

    private void addCategoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCategoryButtonActionPerformed
        AddEquipmentDetailsForm addEquipmentDetailsForm = AddEquipmentDetailsForm.getObj(this, -1, false);
        addEquipmentDetailsForm.setLocationRelativeTo(this);
        addEquipmentDetailsForm.setVisible(true);
    }//GEN-LAST:event_addCategoryButtonActionPerformed

    private void addSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSupplierButtonActionPerformed
        AddSupplierForm addSupplier = new AddSupplierForm(this, -1, false, rootPaneCheckingEnabled);
        addSupplier.setLocationRelativeTo(this);
        addSupplier.setVisible(true);
    }//GEN-LAST:event_addSupplierButtonActionPerformed

    private void equipmentsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_equipmentsTableMouseClicked
        editButton.setEnabled(true);
        removeButton.setEnabled(true);
        tableImageCellCallback(evt, equipmentsTable);
    }//GEN-LAST:event_equipmentsTableMouseClicked

    private void categoriesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_categoriesTableMouseClicked
        editButton.setEnabled(true);
        removeButton.setEnabled(true);
        tableImageCellCallback(evt, categoriesTable);
    }//GEN-LAST:event_categoriesTableMouseClicked

    private void suppliersTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_suppliersTableMouseClicked
        editButton.setEnabled(true);
        removeButton.setEnabled(true);
    }//GEN-LAST:event_suppliersTableMouseClicked

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        switch (_selectedTable) {
            case 1: {
                String id = equipmentsTable.getValueAt(equipmentsTable.getSelectedRow(), 0).toString();
                AddEquipmentForm addEquimentForm = new AddEquipmentForm(this, id);
                addEquimentForm.setVisible(true);
                addEquimentForm.setLocationRelativeTo(this);
                break;
            }
            case 2: {
                break;
            }
            case 3: {
                break;
            }
            case 4: {
                int index = categoriesTable.getSelectedRow();
                AddEquipmentDetailsForm addEquimentDetailsForm = new AddEquipmentDetailsForm(this, index, true);
                addEquimentDetailsForm.setVisible(true);
                break;
            }
            case 5: {
                int index = suppliersTable.getSelectedRow();
                AddSupplierForm addSupplier = new AddSupplierForm(this, index, true, rootPaneCheckingEnabled);
                addSupplier.setVisible(true);
                break;
            }
            default:
                break;
        }
        loadDatabase();
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

    private void importDetailsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_importDetailsTableMouseClicked
        editButton.setEnabled(false);
        removeButton.setEnabled(true);
    }//GEN-LAST:event_importDetailsTableMouseClicked

    private void usersTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usersTableMouseClicked
        editButton.setEnabled(false);
        removeButton.setEnabled(false);
    }//GEN-LAST:event_usersTableMouseClicked

    private void mainTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mainTabbedPaneStateChanged
        _selectedTable = mainTabbedPane.getSelectedIndex() + 1;
        initFilterComboBox(_selectedTable);
        editButton.setEnabled(false);
        removeButton.setEnabled(false);
    }//GEN-LAST:event_mainTabbedPaneStateChanged

    private void filterComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterComboBoxItemStateChanged
        searchTextField.setText("");
    }//GEN-LAST:event_filterComboBoxItemStateChanged

    private void settingButtonActionPerformed(java.awt.event.ActionEvent evt) {
        _settingFrom = new SettingFrom(_userID);
        _settingFrom.setLogOutCallBack(this);
        _settingFrom.setExitCallBack(this);
        _settingFrom.setLocationRelativeTo(null);
        _settingFrom.setVisible(true);
    }

    final String _imageFolderPath = new File("").getAbsolutePath() + "/";
    private int _selectedTable = 0;
    private int _userID = 0;
    private String _role = "";
    private SettingFrom _settingFrom = null;
    private DeleteValue _deleter = new DeleteValue();
    private ImageGenerator _imgGenerator = new ImageGenerator();
    private EquipmentDetailsController _eC = new EquipmentDetailsController();
    private SupplierController _sC = new SupplierController();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane MainDesktopPane;
    private javax.swing.JButton addCategoryButton;
    private javax.swing.JButton addSupplierButton;
    private javax.swing.JScrollPane categoriesScrollPane;
    private javax.swing.JTable categoriesTable;
    private javax.swing.JButton editButton;
    private javax.swing.JScrollPane equipmentsScrollPane;
    private javax.swing.JTable equipmentsTable;
    private javax.swing.JComboBox<String> filterComboBox;
    private javax.swing.JScrollPane importDetailsScrollPane;
    private javax.swing.JTable importDetailsTable;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JButton newImportButton;
    private javax.swing.JButton removeButton;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JButton settingButton;
    private javax.swing.JScrollPane suppliersScrollPane;
    private javax.swing.JTable suppliersTable;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JScrollPane usersScrollPane;
    private javax.swing.JTable usersTable;
    // End of variables declaration//GEN-END:variables

}
