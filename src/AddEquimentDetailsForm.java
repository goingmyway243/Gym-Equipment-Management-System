
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nguyen Hai Dang
 */
public final class AddEquimentDetailsForm extends javax.swing.JFrame {

    @Override
    public MainMenu getParent() {
        return _mainMenu;
    }

    public void setImagePath(String _imagePath) {
        this._imagePath = _imagePath;
    }

    public String getImagePath() {
        return _imagePath;
    }

    /**
     * Creates new form AddEquimentForm
     *
     * @param parent
     */
    public AddEquimentDetailsForm(java.awt.Frame parent) {
        initComponents();
        this.setTitle("Thêm chi tiết thiết bị");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        _eC = new EquipmentDetailsController();
        _sC = new SupplierController();
        _listOfSuppliers = new ArrayList<>();
        _fileNameList = new ArrayList<>();
        _mainMenu = (MainMenu) parent;

        _imageFolderPath = new File("").getAbsolutePath().concat("/src/images/");
        listFilesForFolder(new File(_imageFolderPath));
        getSupplierList();

        initAlert();
    }

    public void initAlert() {
        initAlertLabel(alertMCTTB);
        initAlertLabel(alertTTB);
        initAlertLabel(alertGia);
        initAlertLabel(alertNCC);
        initAlertLabel(alertTGBH);
    }

    public void initAlertLabel(JLabel label) {
        label.setText("");
        label.setSize(0, 0);
    }

    public void getSupplierList() {
        cbNhaCungCap.removeAllItems();
        _listOfSuppliers = _sC.getSuppliersInfo();
        _listOfSuppliers.forEach((supplier) -> {
            cbNhaCungCap.addItem("Tên: " + supplier.getName() + ", Địa chỉ: " + supplier.getAddress());
        });
        cbNhaCungCap.addItem("Thêm nhà cung cấp");
    }

    public void createWindow() {
        JFrame frame = new JFrame("Chọn hình ảnh thiết bị");
        frame.setSize(720, 460);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        createUI(frame);
    }

    public void createUI(final JFrame frame) {

        ImageIcon imgIcon = new ImageIcon(new File("").getAbsolutePath().concat("/src/icon/") + "add-image.png");
        Image newImage = imgIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon imgIconn = new ImageIcon(newImage);

        JButton browseButton = new JButton(imgIconn);
        browseButton.setBounds(200, 380, 100, 40);

        JButton selectButton = new JButton("Chọn");
        selectButton.setBounds(320, 380, 100, 40);

        JButton cancelButton = new JButton("Thoát");
        cancelButton.setBounds(440, 380, 100, 40);

        JLabel labelImage = new JLabel();
        labelImage.setBounds(10, 10, 720, 340);
        JPanel panel = new JPanel();

        int row = (int) (_fileNameList.size() % 3 == 0 ? _fileNameList.size() / 3 : Math.round((_fileNameList.size() / 3) + 0.5));
        panel.setLayout(new GridLayout(row, 3, 5, 5));
        List<JLabel> arrayLabel = new ArrayList<>();

        int dem = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < 3; j++) {
                JLabel labelImg = new JLabel();
                labelImg.setSize(230, 130);
                labelImg.setBorder(null);
                if (dem > _fileNameList.size() - 1) {
                    break;
                }
                labelImg.setIcon(ImageGenerator.ResizeImage(_imageFolderPath + "/" + _fileNameList.get(dem), labelImg));
                panel.add(labelImg);
                arrayLabel.add(labelImg);
                dem++;
            }
        }

        JScrollPane scrollPane = new JScrollPane(panel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(0, 0, 740, 360);

        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(740, 440));
        contentPane.add(scrollPane);
        contentPane.add(browseButton);
        contentPane.add(selectButton);
        contentPane.add(cancelButton);
        frame.setContentPane(contentPane);
        frame.pack();

        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);

        for (JLabel label : arrayLabel) {
            label.setCursor(cursor);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    arrayLabel.stream().filter((label) -> (label != evt.getSource())).forEachOrdered((label) -> {
                        label.setBorder(null);
                    });
                    Border border = BorderFactory.createLineBorder(Color.BLUE, 2);
                    if (label.getBorder() != null) {
                        label.setBorder(null);
                    } else if (label.getBorder() == null) {
                        label.setBorder(border);
                    }
                }
            });
        }

        selectButton.addActionListener((ActionEvent ae) -> {
            int x = 0;
            for (JLabel label : arrayLabel) {
                if (label.getBorder() != null) {
                    break;
                }
                x++;
            }
            btnGetImage.setText(_fileNameList.get(x));
            setImagePath("/src/images/" + _fileNameList.get(x));
            frame.dispose();
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                frame.dispose();
            }
        });

        JButton backButton = new JButton("Quay lại");
        backButton.setBounds(200, 380, 160, 40);

        JButton selectButton2 = new JButton("Chọn");
        selectButton2.setBounds(380, 380, 100, 40);

        backButton.addActionListener((ActionEvent ae) -> {
            contentPane.removeAll();
            frame.remove(contentPane);
            frame.repaint();

            contentPane.add(scrollPane);
            contentPane.add(selectButton);
            browseButton.setBounds(200, 380, 100, 40);
            contentPane.add(browseButton);
            contentPane.add(cancelButton);
            setImagePath(null);
            btnGetImage.setText("Chọn hình ảnh!");
        });

        browseButton.addActionListener((ActionEvent e) -> {
            contentPane.remove(selectButton);
            contentPane.remove(cancelButton);
            frame.repaint();
            browseButton.setBounds(250, 380, 160, 40);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(_imageFolderPath));
            fileChooser.addChoosableFileFilter(new ImageFilter());
            fileChooser.setAcceptAllFileFilterUsed(false);

            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                String path = selectedFile.getAbsolutePath();

                contentPane.removeAll();
                frame.remove(contentPane);
                frame.repaint();
                contentPane.add(labelImage);
                contentPane.add(backButton);
                contentPane.add(selectButton2);
                labelImage.setIcon(ImageGenerator.ResizeImage(path, labelImage));

                selectButton2.addActionListener((ActionEvent ae) -> {
                    _selectedFile = selectedFile;
                    btnGetImage.setText(selectedFile.getName());
                    frame.dispose();
                });
            } else if (result == JFileChooser.CANCEL_OPTION) {
                System.out.println("\"Không có file nào được chọn\"");
                frame.repaint();
                browseButton.setBounds(200, 380, 100, 40);
                frame.add(selectButton);
                frame.add(cancelButton);
            }
        });
    }

    private void saveImage(File selectedFile) {
        setImagePath("/src/images/" + selectedFile.getName());
        File dest = new File(_imageFolderPath, selectedFile.getName());
        try {
            if (_fileNameList.contains(selectedFile.getName())) {
                dest = new File(_imageFolderPath, selectedFile.getName() + "(copy)");

            }
            if (!(selectedFile.getParent() + "/").equals(_imageFolderPath)) {
                copyFileUsingStream(selectedFile, dest);
            }
        } catch (IOException ex) {
            Logger.getLogger(AddEquimentDetailsForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                _fileNameList.add(fileEntry.getName());
            }
        }
    }

    public void resetField() {
        txtMaChiTietTB.setText("");
        txtTenThietBi.setText("");
        btnGetImage.setText("Chọn hình ảnh!");
        txtGia.setText("");
        txtThoiGianBH.setText("");
        cbNhaCungCap.setSelectedIndex(0);
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
        txtMaChiTietTB = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTenThietBi = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtGia = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtThoiGianBH = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cbNhaCungCap = new javax.swing.JComboBox<>();
        btnGetImage = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        alertMCTTB = new javax.swing.JLabel();
        alertTTB = new javax.swing.JLabel();
        alertNCC = new javax.swing.JLabel();
        alertGia = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        alertTGBH = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("Mã chi tiết thiết bị :");

        txtMaChiTietTB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMaChiTietTBKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setText("Tên thiết bị :");

        txtTenThietBi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTenThietBiKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel5.setText("Nhà cung cấp :");
        jLabel5.setToolTipText("");

        txtGia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGiaKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setText("Giá :");

        jButton1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/plus.png"))); // NOI18N
        jButton1.setText("Xác nhận");
        jButton1.setToolTipText("");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cancel.png"))); // NOI18N
        jButton2.setText("Hủy bỏ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel8.setText("Thời gian bảo hành:");

        txtThoiGianBH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtThoiGianBHKeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel9.setText("Hình ảnh:");

        cbNhaCungCap.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbNhaCungCapItemStateChanged(evt);
            }
        });
        cbNhaCungCap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNhaCungCapActionPerformed(evt);
            }
        });

        btnGetImage.setText("Chọn hình ảnh!");
        btnGetImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGetImageActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.png"))); // NOI18N
        jButton3.setText("Làm mới");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        alertMCTTB.setForeground(new java.awt.Color(255, 0, 0));
        alertMCTTB.setLabelFor(txtMaChiTietTB);
        alertMCTTB.setText("Mã CTBB alert");

        alertTTB.setForeground(new java.awt.Color(255, 0, 0));
        alertTTB.setText("Tên TB alert");

        alertNCC.setForeground(new java.awt.Color(255, 0, 0));
        alertNCC.setText("NCC alert");

        alertGia.setForeground(new java.awt.Color(255, 0, 0));
        alertGia.setText("Giá TB alert");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 255));
        jLabel2.setText("THÊM CHI TIẾT THIẾT BỊ");

        alertTGBH.setForeground(new java.awt.Color(255, 0, 0));
        alertTGBH.setText("TGBH alert");

        jLabel3.setText("VND");

        jLabel7.setText("năm");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(185, 185, 185)
                        .addComponent(alertMCTTB))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel4)
                        .addGap(86, 86, 86)
                        .addComponent(txtTenThietBi, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(185, 185, 185)
                        .addComponent(alertTTB))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(185, 185, 185)
                        .addComponent(alertNCC))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel8)
                        .addGap(35, 35, 35)
                        .addComponent(txtThoiGianBH, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel1)
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtMaChiTietTB, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(45, 45, 45)
                            .addComponent(jButton3)
                            .addGap(30, 30, 30)
                            .addComponent(jButton1)
                            .addGap(26, 26, 26)
                            .addComponent(jButton2))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addComponent(jLabel5)
                            .addGap(71, 71, 71)
                            .addComponent(cbNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel9)
                        .addGap(106, 106, 106)
                        .addComponent(btnGetImage, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(185, 185, 185)
                        .addComponent(alertTGBH))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel6)
                        .addGap(143, 143, 143)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(alertGia)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)))))
                .addGap(8, 8, 8))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtMaChiTietTB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alertMCTTB, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(txtTenThietBi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alertTTB, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGetImage)
                    .addComponent(jLabel9))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alertGia, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alertNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtThoiGianBH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alertTGBH, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        initAlert();
        String maChiTietTB = txtMaChiTietTB.getText();
        String tenThietbi = txtTenThietBi.getText();
        String hinhAnh = getImagePath();
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
            eD.setId(maChiTietTB);
            eD.setName(tenThietbi);
            eD.setPicture(hinhAnh);
            eD.setPrice(price);
            eD.setSupplier_id(nhaCungCapId);
            if (thoigianBH.length() > 0) {
                eD.setWarranty_time(Integer.parseInt(thoigianBH));
            }

            if (_eC.isIdExist(maChiTietTB)) {
                int option = JOptionPane.showConfirmDialog(this, "Mã chi tiết thiết bị đã tồn tại, bạn có muốn cập nhật thông tin không?");
                if (option == 0) {
                    if (_eC.updateEquipmentDetails(eD)) {
                        JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                        saveImage(_selectedFile);
                        _mainMenu.loadDatabase();
                        resetField();
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật thất  bại");
                    }
                }
            } else {
                if (_eC.addNewEquipmentDetails(eD)) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công");
                    saveImage(_selectedFile);
                    _mainMenu.loadDatabase();
                    resetField();
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất  bại");
                }
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnGetImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGetImageActionPerformed
        createWindow();
    }//GEN-LAST:event_btnGetImageActionPerformed

    private void cbNhaCungCapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNhaCungCapActionPerformed
        int selectedItem = cbNhaCungCap.getSelectedIndex();

        if (selectedItem == _listOfSuppliers.size()) {
            cbNhaCungCap.hidePopup();
            new AddSupplier(this, rootPaneCheckingEnabled).setVisible(true);
        }
    }//GEN-LAST:event_cbNhaCungCapActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        resetField();
        initAlert();
        if (_listOfSuppliers.size() != _sC.getSuppliersInfo().size()) {
            this.getSupplierList();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtMaChiTietTBKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaChiTietTBKeyReleased
        String maCTTB = txtMaChiTietTB.getText();

        if (maCTTB.matches("^[a-zA-Z]{3}[\\d]{2}$") && txtMaChiTietTB.getText().length() > 0) {
            initAlertLabel(alertMCTTB);
        }
    }//GEN-LAST:event_txtMaChiTietTBKeyReleased

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

    private void cbNhaCungCapItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbNhaCungCapItemStateChanged
        if (!cbNhaCungCap.getSelectedItem().equals("Thêm nhà cung cấp")) {
            initAlertLabel(alertNCC);
        }
    }//GEN-LAST:event_cbNhaCungCapItemStateChanged

    private void txtThoiGianBHKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtThoiGianBHKeyReleased
        String thoigianBH = txtThoiGianBH.getText();
        if (thoigianBH.matches("^[\\d]{1,2}$")) {
            if (Integer.parseInt(thoigianBH) <= 10 && Integer.parseInt(thoigianBH) >= 1) {
                initAlertLabel(alertTGBH);
            }
        }
    }//GEN-LAST:event_txtThoiGianBHKeyReleased

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    private void createAlert(JLabel label, String alertContent) {
        label.setSize(alertContent.length(), 17);
        label.setText(alertContent);
        label.setVisible(true);
    }

    private EquipmentDetailsController _eC = null;
    private SupplierController _sC = null;
    private List<Supplier> _listOfSuppliers;
    private final List<String> _fileNameList;
    private String _imagePath;
    private final String _imageFolderPath;
    private int _size;
    private final MainMenu _mainMenu;
    private File _selectedFile;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel alertGia;
    private javax.swing.JLabel alertMCTTB;
    private javax.swing.JLabel alertNCC;
    private javax.swing.JLabel alertTGBH;
    private javax.swing.JLabel alertTTB;
    private javax.swing.JButton btnGetImage;
    private javax.swing.JComboBox<String> cbNhaCungCap;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtMaChiTietTB;
    private javax.swing.JTextField txtTenThietBi;
    private javax.swing.JTextField txtThoiGianBH;
    // End of variables declaration//GEN-END:variables
}
