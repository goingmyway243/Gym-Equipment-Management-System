
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import javax.swing.filechooser.FileFilter;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nguyen Hai Dang
 */
public class AddEquimentDetailsForm extends javax.swing.JFrame {

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
        this.setLocationRelativeTo(null);
        _eC = new EquipmentDetailsController();
        _sC = new SupplierController();
        _listOfSuppliers = new ArrayList<>();
        _fileNameList = new ArrayList<>();
        _mainMenu = (MainMenu) parent;

        _imageFolderPath = new File("").getAbsolutePath().concat("/src/images/");
        listFilesForFolder(new File(_imageFolderPath));
        getSupplierList();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Thêm chi tiết thiết bị");

    }

    public void getSupplierList() {
        cbNhaCungCap.removeAllItems();
        _listOfSuppliers = _sC.getSuppliersInfo();
        for (Supplier supplier : _listOfSuppliers) {
            cbNhaCungCap.addItem("Tên: " + supplier.getName() + ", Địa chỉ: " + supplier.getAddress());
        }
        cbNhaCungCap.addItem("Thêm nhà cung cấp");
    }
    
    public void createWindow() {
        JFrame frame = new JFrame("Choose Image");
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(720, 460);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        createUI(frame);
    }

    public void createUI(final JFrame frame) {
        JButton browseButton = new JButton("Chọn ảnh khác...");
        browseButton.setBounds(200, 380, 160, 40);

        JButton selectButton = new JButton("Chọn");
        selectButton.setBounds(380, 380, 100, 40);

        JButton cancelButton = new JButton("Thoát");
        cancelButton.setBounds(500, 380, 100, 40);

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
                labelImg.setIcon(ResizeImage(_imageFolderPath + "/" + _fileNameList.get(dem), labelImg));
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

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
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
            }
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

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                contentPane.removeAll();
                frame.remove(contentPane);
                frame.repaint();

                contentPane.add(scrollPane);
                contentPane.add(selectButton);
                browseButton.setBounds(200, 380, 160, 40);
                contentPane.add(browseButton);
                setImagePath(null);
                System.out.println(_imagePath);
                btnGetImage.setText("Chọn hình ảnh!");

            }
        });

        browseButton.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
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
                    labelImage.setIcon(ResizeImage(path, labelImage));

                    selectButton2.addActionListener((ActionEvent ae) -> {
                        setImagePath("/src/images/" + selectedFile.getName());
                        btnGetImage.setText(selectedFile.getName());
                        System.out.println(selectedFile.getAbsolutePath());
                        File dest = new File(_imageFolderPath, selectedFile.getName());
                        try {
                            if (!(selectedFile.getParent() + "/").equals(_imageFolderPath)) {
                                copyFileUsingStream(selectedFile, dest);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(AddEquimentDetailsForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        frame.dispose();
                    });
                } else if (result == JFileChooser.CANCEL_OPTION) {
                    System.out.println("\"Không có file nào được chọn\"");
                    frame.repaint();
                    browseButton.setBounds(200, 380, 160, 40);
                    frame.add(selectButton);
                }
            }
        }
        );
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

    static public ImageIcon ResizeImage(String _imagePath, JLabel label) {
        ImageIcon myImage = new ImageIcon(_imagePath);
        Image img = myImage.getImage();
        Image newImg = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Mã chi tiết thiết bị :");

        jLabel4.setText("Tên thiết bị :");

        jLabel5.setText("Nhà cung cấp :");
        jLabel5.setToolTipText("");

        jLabel6.setText("Giá :");

        jButton1.setText("Xác nhận");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Hủy");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel8.setText("Thời gian bảo hành:");

        jLabel9.setText("Hình ảnh:");

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

        jButton3.setText("Làm mới");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(jLabel4)
                    .addComponent(jLabel9)
                    .addComponent(jLabel5))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnGetImage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtTenThietBi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                                    .addComponent(txtMaChiTietTB, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtGia, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(144, 144, 144))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtThoiGianBH)
                                .addGap(45, 45, 45))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addGap(34, 34, 34)
                                .addComponent(jButton1)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtMaChiTietTB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtTenThietBi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(btnGetImage))
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cbNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtThoiGianBH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(27, 27, 27))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String maChiTietTB = txtMaChiTietTB.getText();
        String tenThietbi = txtTenThietBi.getText();
        String hinhAnh = getImagePath();
        String gia = txtGia.getText();
        int selectedNhaCungCap = cbNhaCungCap.getSelectedIndex();
        String thoigianBH = txtThoiGianBH.getText();
        int nhaCungCapId = 0;

        boolean isSatisfied = true;

        if (maChiTietTB.length() == 0) {
            JOptionPane.showMessageDialog(this, "Mã chi tiết thiết bị không được để trống!");
            isSatisfied = false;
        } else if (tenThietbi.length() == 0) {
            JOptionPane.showMessageDialog(this, "Tên thiết bị không được để trống!");
            isSatisfied = false;
        } else if (gia.length() == 0) {
            JOptionPane.showMessageDialog(this, "Giá thiết bị không được để trống!");
            isSatisfied = false;
        } else if (thoigianBH.length() == 0) {
            JOptionPane.showMessageDialog(this, "Thời gian bảo hành không được để trống!");
            isSatisfied = false;
        }

        if (maChiTietTB.length() > 0 && maChiTietTB.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Mã chi tiết thiết bị phải là kí tự chữ và số");
            isSatisfied = false;
        } else if (tenThietbi.length() > 0 && tenThietbi.matches("[^a-zA-Z]+")) {
            JOptionPane.showMessageDialog(this, "Tên thiết bị phải là chữ cái ");
            isSatisfied = false;
        } else if (gia.length() > 0 && !gia.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Gía thiết bị phải là chữ số ");
            isSatisfied = false;
        } else if (cbNhaCungCap.getSelectedItem().toString().equals("Thêm nhà cung cấp")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp ");
            isSatisfied = false;
        }

        if (maChiTietTB.length() > 5) {
            JOptionPane.showMessageDialog(this, "Mã chi tiết thiết bị không được vượt quá 5 ký tự");
            isSatisfied = false;
        } else if (thoigianBH.length() > 0 && Integer.parseInt(thoigianBH) > 10 && Integer.parseInt(thoigianBH) > 0) {
            JOptionPane.showMessageDialog(this, "Thời gian bảo hành không được vượt quá 10 năm và phải ít nhất là 1 năm");
            isSatisfied = false;
        }

        if (isSatisfied) {
            nhaCungCapId = _listOfSuppliers.get(selectedNhaCungCap).getSupplierId();
            int price = Integer.parseInt(gia);
            Equipment_Details eD = new Equipment_Details();
            eD.setId(maChiTietTB);
            eD.setName(tenThietbi);
            eD.setPicture(hinhAnh);
            eD.setPrice(price);
            eD.setSupplier_id(nhaCungCapId);
            eD.setWarranty_time(Integer.parseInt(thoigianBH));

            if (_eC.isIdExist(maChiTietTB)) {
                int option = JOptionPane.showConfirmDialog(this, "Mã chi tiết thiết bị đã tồn tại, bạn có muốn cập nhật thông tin không?");
                if (option == 0) {
                    if (_eC.updateEquipmentDetails(eD)) {
                        JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                        _mainMenu.loadDatabase();
                        resetField();
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật thất  bại");
                    }
                }
            } else {
                if (_eC.addNewEquipmentDetails(eD)) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công");
                    _mainMenu.loadDatabase();
                    resetField();
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
        this.resetField();
        if (_listOfSuppliers.size() != _sC.getSuppliersInfo().size()) {
            this.getSupplierList();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

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


    private EquipmentDetailsController _eC = null;
    private SupplierController _sC = null;
    private List<Supplier> _listOfSuppliers;
    private List<String> _fileNameList;
    private String _imagePath;
    private String _imageFolderPath;
    private int _size;
    private final MainMenu _mainMenu;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGetImage;
    private javax.swing.JComboBox<String> cbNhaCungCap;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtMaChiTietTB;
    private javax.swing.JTextField txtTenThietBi;
    private javax.swing.JTextField txtThoiGianBH;
    // End of variables declaration//GEN-END:variables
}

class ImageFilter extends FileFilter {

    public final static String JPEG = "jpeg";
    public final static String JPG = "jpg";
    public final static String GIF = "gif";
    public final static String TIFF = "tiff";
    public final static String TIF = "tif";
    public final static String PNG = "png";

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals(TIFF)
                    || extension.equals(TIF)
                    || extension.equals(GIF)
                    || extension.equals(JPEG)
                    || extension.equals(JPG)
                    || extension.equals(PNG)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Image Only";
    }

    String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
