
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.border.Border;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author skqist225
 */
public class LoginForm extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    public static void create() {
        java.awt.EventQueue.invokeLater(() -> {
            javax.swing.JFrame frame = new LoginForm();
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        create();
    }

    public LoginForm() {
        initComponents();
        alertLb.setText("");
        alertLb.setSize(0, 0);

        Border containerBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(78, 84, 200));
        pnl_bg.setBorder(containerBorder);
        loader.setBorder(containerBorder);

        jPanel3.add(new TextAnimation(0, 20, "Thực hiện tốt 5K. Chung tay phòng chống dịch COVID 19"));

        lblGymImg2.setIcon(ImageGenerator.ResizeImage(new ImageGenerator().getImageFolderPath() + "/src/icon/1.jpg", lblGymImg2));
        lblGymImg3.setIcon(ImageGenerator.ResizeImage(new ImageGenerator().getImageFolderPath() + "/src/icon/2.jpg", lblGymImg3));
        lblGymImg4.setIcon(ImageGenerator.ResizeImage(new ImageGenerator().getImageFolderPath() + "/src/icon/gyn_logo.jpg", lblGymImg4));
        lblGymImg5.setIcon(ImageGenerator.ResizeImage(new ImageGenerator().getImageFolderPath() + "/src/icon/5.jpg", lblGymImg5));
        ptitLogo.setIcon(ImageGenerator.ResizeImage(new ImageGenerator().getImageFolderPath() + "src/icon/logo_ptit.png", ptitLogo));

        showPwdLbl1.setVisible(false);
        pwdShowed.setVisible(false);
        pnl_forgotPassword.setVisible(false);
        loader.setVisible(false);

        txtEmailErr.setVisible(false);
        txtVerErr.setVisible(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                System.out.println("Login Form Loading");
            }
        });
    }

    private boolean loginAction(String username, String password) {
        Connection connectDB = ConnectMysql.getConnectDB();
        String sql = "SELECT * FROM `login_info` WHERE userName = '" + username + "' and password = '" + password + "'";
        int roleID = 0;
        try {
            ResultSet rs = connectDB.createStatement().executeQuery(sql);
            if (rs.next()) {
                roleID = rs.getInt("role_id");
                _userID = rs.getInt("userId");
                _role = getRole(roleID);
                return true;
            }
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }

    private String getRole(int roleID) {
        Connection connectDB = ConnectMysql.getConnectDB();
        String sql = "SELECT * FROM `role` WHERE id = '" + roleID + "'";
        try {
            ResultSet rs = connectDB.createStatement().executeQuery(sql);
            if (rs.next()) {
                return rs.getString("role");
            }
            return "Nhân viên";
        } catch (SQLException ex) {
            return "Nhân viên";
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

        jTextField1 = new javax.swing.JTextField();
        pnl_bg = new javax.swing.JPanel();
        jPanel3 = new JPanelGradient();
        jLabel8 = new javax.swing.JLabel();
        lblGymImg4 = new javax.swing.JLabel();
        lblGymImg1 = new javax.swing.JLabel();
        lblGymImg2 = new javax.swing.JLabel();
        lblGymImg3 = new javax.swing.JLabel();
        ptitLogo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblGymImg5 = new javax.swing.JLabel();
        login = new javax.swing.JPanel();
        pwdShowed = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        showPwdLbl = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        passwordTf = new javax.swing.JPasswordField();
        alertLb = new javax.swing.JLabel();
        showPwdLbl1 = new javax.swing.JLabel();
        usernameTf = new javax.swing.JTextField();
        loginBtn = new Button();
        lbl_forgotPassword = new javax.swing.JLabel();
        lbl_close_hover = new javax.swing.JLabel();
        pnl_forgotPassword = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        txtVer = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        loginBtn3 = new Button();
        loginBtn4 = new Button();
        lbl_backIcon = new javax.swing.JLabel();
        txtEmailErr = new javax.swing.JLabel();
        txtVerErr = new javax.swing.JLabel();
        loader = new javax.swing.JPanel();
        img_loader = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        pnl_bg.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("PHẦN MỀM QUẢN LÝ THIẾT BỊ PHÒNG GYM");
        jLabel8.setIconTextGap(3);

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ĐỒ ÁN NHẬP MÔN CÔNG NGHỆ PHẦN MỀM");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(70, 70, 70)
                    .addComponent(lblGymImg4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(lblGymImg3, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblGymImg1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(lblGymImg5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(lblGymImg2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addGap(48, 48, 48))
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(45, 45, 45)
                    .addComponent(ptitLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel2)))
            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGymImg1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblGymImg4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lblGymImg3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblGymImg5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblGymImg2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(ptitLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(33, 33, 33))))
        );

        login.setBackground(new java.awt.Color(255, 255, 255));
        login.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pwdShowed.setForeground(new java.awt.Color(38, 84, 124));
        pwdShowed.setBorder(null);
        login.add(pwdShowed, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 210, 290, 40));

        jSeparator1.setBackground(new java.awt.Color(98, 121, 184));
        jSeparator1.setForeground(new java.awt.Color(98, 121, 184));
        jSeparator1.setRequestFocusEnabled(false);
        login.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 190, 339, 10));

        jSeparator2.setBackground(new java.awt.Color(98, 121, 184));
        jSeparator2.setForeground(new java.awt.Color(98, 121, 184));
        login.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 339, 10));

        showPwdLbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/invisible-symbol.png"))); // NOI18N
        showPwdLbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showPwdLblMouseClicked(evt);
            }
        });
        login.add(showPwdLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 220, 40, 26));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/user.png"))); // NOI18N
        login.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, -1, -1));

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(93, 38, 193));
        jLabel4.setText("Đăng Nhập");
        login.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(199, 56, -1, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/padlock.png"))); // NOI18N
        login.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 220, -1, 40));

        passwordTf.setForeground(new java.awt.Color(38, 84, 124));
        passwordTf.setText("Password");
        passwordTf.setBorder(null);
        passwordTf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordTfFocusGained(evt);
            }
        });
        login.add(passwordTf, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 220, 289, 31));

        alertLb.setForeground(new java.awt.Color(255, 0, 0));
        alertLb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        alertLb.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/warning.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        alertLb.setText("Sai tên đăng nhập hoặc mật khẩu!");
        login.add(alertLb, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 260, 339, 30));
        alertLb.setVisible(false);

        showPwdLbl1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/eye.png"))); // NOI18N
        showPwdLbl1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showPwdLbl1MouseClicked(evt);
            }
        });
        login.add(showPwdLbl1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 220, 40, 26));

        usernameTf.setForeground(new java.awt.Color(38, 84, 124));
        usernameTf.setBorder(null);
        login.add(usernameTf, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 160, 339, 40));

        loginBtn.setBackground(new java.awt.Color(97, 144, 232));
        loginBtn.setForeground(new java.awt.Color(255, 255, 255));
        loginBtn.setText("Đăng Nhập");
        loginBtn.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        loginBtn.setGradientBackgroundColor(new java.awt.Color(28, 181, 224));
        loginBtn.setLineColor(null);
        loginBtn.setLinePainted(false);
        loginBtn.setRounded(false);
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });
        login.add(loginBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 320, 160, 48));

        lbl_forgotPassword.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lbl_forgotPassword.setText("Quên mật khẩu?");
        lbl_forgotPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_forgotPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_forgotPasswordMousePressed(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbl_forgotPasswordMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_forgotPasswordMouseEntered(evt);
            }
        });
        login.add(lbl_forgotPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 330, -1, -1));

        lbl_close_hover.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_close_hover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/close1.png"))); // NOI18N
        lbl_close_hover.setMaximumSize(new java.awt.Dimension(32, 32));
        lbl_close_hover.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lbl_close_hoverMousePressed(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbl_close_hoverMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_close_hoverMouseEntered(evt);
            }
        });

        pnl_forgotPassword.setBackground(new java.awt.Color(255, 255, 255));
        pnl_forgotPassword.setPreferredSize(new java.awt.Dimension(534, 580));
        pnl_forgotPassword.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jLabel6.setText("Nhập Email:");
        pnl_forgotPassword.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(61, 180, 128, 35));

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jLabel7.setText("Nhập mã xác thực:");
        pnl_forgotPassword.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(61, 262, 167, 35));

        txtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmailKeyReleased(evt);
            }
        });
        pnl_forgotPassword.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(272, 182, 213, 35));

        txtVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVerActionPerformed(evt);
            }
        });
        txtVer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtVerKeyReleased(evt);
            }
        });
        pnl_forgotPassword.add(txtVer, new org.netbeans.lib.awtextra.AbsoluteConstraints(272, 264, 213, 35));

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(93, 38, 193));
        jLabel9.setText("QUÊN MẬT KHẨU");
        pnl_forgotPassword.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 42, -1, -1));

        loginBtn3.setBackground(new java.awt.Color(97, 144, 232));
        loginBtn3.setForeground(new java.awt.Color(255, 255, 255));
        loginBtn3.setText("Gửi Mã");
        loginBtn3.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        loginBtn3.setGradientBackgroundColor(new java.awt.Color(28, 181, 224));
        loginBtn3.setLineColor(null);
        loginBtn3.setLinePainted(false);
        loginBtn3.setRounded(true);
        loginBtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtn3ActionPerformed(evt);
            }
        });
        pnl_forgotPassword.add(loginBtn3, new org.netbeans.lib.awtextra.AbsoluteConstraints(141, 396, 163, 42));

        loginBtn4.setBackground(new java.awt.Color(97, 144, 232));
        loginBtn4.setForeground(new java.awt.Color(255, 255, 255));
        loginBtn4.setText("Xác Nhận");
        loginBtn4.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        loginBtn4.setGradientBackgroundColor(new java.awt.Color(28, 181, 224));
        loginBtn4.setLineColor(null);
        loginBtn4.setLinePainted(false);
        loginBtn4.setRounded(true);
        loginBtn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtn4ActionPerformed(evt);
            }
        });
        pnl_forgotPassword.add(loginBtn4, new org.netbeans.lib.awtextra.AbsoluteConstraints(322, 396, 163, 43));

        lbl_backIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_backIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/previous (1).png"))); // NOI18N
        lbl_backIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_backIconMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbl_backIconMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbl_backIconMouseEntered(evt);
            }
        });
        pnl_forgotPassword.add(lbl_backIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 12, 34, 35));

        txtEmailErr.setForeground(new java.awt.Color(255, 0, 0));
        txtEmailErr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtEmailErr.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/warning.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        txtEmailErr.setText("Email không tồn tại");
        pnl_forgotPassword.add(txtEmailErr, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 220, 339, 30));
        alertLb.setVisible(false);

        txtVerErr.setForeground(new java.awt.Color(255, 0, 0));
        txtVerErr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtVerErr.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/warning.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        txtVerErr.setText("Mã xác thực không đúng");
        pnl_forgotPassword.add(txtVerErr, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 310, 339, 30));
        alertLb.setVisible(false);

        javax.swing.GroupLayout pnl_bgLayout = new javax.swing.GroupLayout(pnl_bg);
        pnl_bg.setLayout(pnl_bgLayout);
        pnl_bgLayout.setHorizontalGroup(
            pnl_bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_bgLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnl_bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(login, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
                    .addGroup(pnl_bgLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_close_hover, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(pnl_bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_bgLayout.createSequentialGroup()
                    .addGap(0, 503, Short.MAX_VALUE)
                    .addComponent(pnl_forgotPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        pnl_bgLayout.setVerticalGroup(
            pnl_bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnl_bgLayout.createSequentialGroup()
                .addComponent(lbl_close_hover, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(login, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnl_bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_bgLayout.createSequentialGroup()
                    .addGap(0, 38, Short.MAX_VALUE)
                    .addComponent(pnl_forgotPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        loader.setBackground(new java.awt.Color(255, 255, 255));

        img_loader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        img_loader.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Eclipse-1s-200px.gif"))); // NOI18N

        jLabel1.setBackground(new java.awt.Color(41, 168, 73));
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(78, 84, 200));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Đang đăng nhập...");

        javax.swing.GroupLayout loaderLayout = new javax.swing.GroupLayout(loader);
        loader.setLayout(loaderLayout);
        loaderLayout.setHorizontalGroup(
            loaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loaderLayout.createSequentialGroup()
                .addGap(414, 414, 414)
                .addGroup(loaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(img_loader, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(loaderLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel1)))
                .addContainerGap(452, Short.MAX_VALUE))
        );
        loaderLayout.setVerticalGroup(
            loaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loaderLayout.createSequentialGroup()
                .addGap(202, 202, 202)
                .addComponent(img_loader, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addContainerGap(161, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(loader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(loader, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void passwordTfFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordTfFocusGained
        passwordTf.setText("");
    }//GEN-LAST:event_passwordTfFocusGained

    private void lbl_close_hoverMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_close_hoverMousePressed
        System.exit(0);
    }//GEN-LAST:event_lbl_close_hoverMousePressed

    private void showPwdLblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showPwdLblMouseClicked
        pwdShowed.setText(String.valueOf(passwordTf.getPassword()));
        pwdShowed.requestFocus(true);

        showPwdLbl.setVisible(false);
        pwdShowed.setVisible(true);
        passwordTf.setVisible(false);
        showPwdLbl1.setVisible(true);

    }//GEN-LAST:event_showPwdLblMouseClicked

    private void showPwdLbl1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showPwdLbl1MouseClicked
        passwordTf.setText(pwdShowed.getText());
        passwordTf.requestFocus(true
        );

        showPwdLbl.setVisible(true);
        pwdShowed.setVisible(false);
        passwordTf.setVisible(true);
        showPwdLbl1.setVisible(false);
    }//GEN-LAST:event_showPwdLbl1MouseClicked

    private void lbl_close_hoverMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_close_hoverMouseEntered
        lbl_close_hover.setSize(36, 36);
        lbl_close_hover.setIcon(ImageGenerator.ResizeImage(new ImageGenerator().getImageFolderPath() + "/src/icon/close2.png", lbl_close_hover));
    }//GEN-LAST:event_lbl_close_hoverMouseEntered

    private void lbl_close_hoverMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_close_hoverMouseExited
        lbl_close_hover.setSize(24, 24);
        lbl_close_hover.setIcon(ImageGenerator.ResizeImage(new ImageGenerator().getImageFolderPath() + "/src/icon/close1.png", lbl_close_hover));
    }//GEN-LAST:event_lbl_close_hoverMouseExited

    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtnActionPerformed
        if (loginAction(usernameTf.getText(), String.valueOf(passwordTf.getPassword())) || loginAction(usernameTf.getText(), pwdShowed.getText())) {
            java.awt.EventQueue.invokeLater(() -> {
                alertLb.setVisible(false);
                loader.setVisible(true);
                pnl_bg.setVisible(false);

                AdminDashBoard admDb = new AdminDashBoard(_userID, _role);
                admDb.setLocationRelativeTo(this);
                AlertFrame alertFrame = new AlertFrame("Đăng nhập thành công!");

                new java.util.Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        admDb.setVisible(true);
                        dispose();

                    }
                }, 1000 * 2);
                alertFrame.setVisible(true);
            });
        }
        else {
            createAlert(alertLb, "Sai tên đăng nhập hoặc tài khoản!");
        }
    }//GEN-LAST:event_loginBtnActionPerformed

    private void lbl_forgotPasswordMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_forgotPasswordMousePressed
        login.setVisible(false);
        pnl_forgotPassword.setVisible(true);
    }//GEN-LAST:event_lbl_forgotPasswordMousePressed

    private void txtVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVerActionPerformed

    private void lbl_forgotPasswordMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_forgotPasswordMouseEntered
        lbl_forgotPassword.setForeground(Color.decode("#5D26C1"));
        Font font = lbl_forgotPassword.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        lbl_forgotPassword.setFont(font.deriveFont(attributes));
    }//GEN-LAST:event_lbl_forgotPasswordMouseEntered

    private void lbl_forgotPasswordMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_forgotPasswordMouseExited
        lbl_forgotPassword.setForeground(Color.black);
        Font font = lbl_forgotPassword.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, -1);
        lbl_forgotPassword.setFont(font.deriveFont(attributes));
    }//GEN-LAST:event_lbl_forgotPasswordMouseExited

    private void loginBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtn3ActionPerformed
        String email = txtEmail.getText();
        String sql = "SELECT email from users where email = ?";
        boolean isEmailExist = false;
        try {
            Connection con = ConnectMysql.getConnectDB();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("true");
                isEmailExist = true;
            }
        } catch (Exception e) {
        }

        if (email.length() == 0 || !isEmailExist) {
            txtEmailErr.setVisible(true);
            return;
        }

        try {
            Random rand = new Random();
            _randomCode = rand.nextInt(999999);

            String from = "thuan.leminhthuan.10.2@gmail.com";
            String pass = "hafohpanyvvtpvlv";
            String to = txtEmail.getText();
            String message = "Your resetting code is " + _randomCode;
            String subject = "Reseting Code";

            Properties props = System.getProperties();

            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.setProperty("mail.debug", "true");

            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Session mailSession = Session.getDefaultInstance(props, null);

            MimeMessage msg = new MimeMessage(mailSession);

            msg.setFrom(new InternetAddress(from));
            msg.setSubject(subject);
            msg.setText(message);
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, to);

            Transport transport = mailSession.getTransport("smtp");
            transport.connect("smtp.gmail.com", from, pass);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();

            new AlertFrame("Gưi mã thành công").setVisible(true);
        } catch (AddressException ex) {
            Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_loginBtn3ActionPerformed

    private void loginBtn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtn4ActionPerformed
        String code = txtVer.getText();

        if (code.length() == 0 || _randomCode != Integer.valueOf(code)) {
            txtVerErr.setVisible(true);
            return;
        }

        if (Integer.valueOf(txtVer.getText()) == _randomCode) {
            new ResetPasswordFrm(txtEmail.getText()).setVisible(true);
        }
    }//GEN-LAST:event_loginBtn4ActionPerformed

    private void lbl_backIconMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_backIconMouseEntered
        lbl_backIcon.setIcon(null);
        lbl_backIcon.setSize(32, 32);
        lbl_backIcon.setIcon(ImageGenerator.ResizeImage(new ImageGenerator()._imageFolderPath + "/src/icon/previous.png", lbl_backIcon));

    }//GEN-LAST:event_lbl_backIconMouseEntered

    private void lbl_backIconMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_backIconMouseExited
        lbl_backIcon.setIcon(null);
        lbl_backIcon.setSize(24, 24);
        lbl_backIcon.setIcon(ImageGenerator.ResizeImage(new ImageGenerator()._imageFolderPath + "/src/icon/previous (1).png", lbl_backIcon));

    }//GEN-LAST:event_lbl_backIconMouseExited

    private void lbl_backIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_backIconMouseClicked
        pnl_forgotPassword.setVisible(false);
        login.setVisible(true);
    }//GEN-LAST:event_lbl_backIconMouseClicked

    private void txtEmailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailKeyReleased
        if (txtEmail.getText().length() > 0) {
            txtEmailErr.setVisible(false);
        }
    }//GEN-LAST:event_txtEmailKeyReleased

    private void txtVerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVerKeyReleased
        if (txtVer.getText().length() > 0) {
            txtVerErr.setVisible(false);
        }
    }//GEN-LAST:event_txtVerKeyReleased

    private void createAlert(JLabel label, String alertContent) {
        label.setSize(339, 30);
        label.setText(alertContent);
        label.setVisible(true);
    }

    @Override
    public JRootPane getRootPane() {
        JRootPane rootPane = super.getRootPane();
        rootPane.setDefaultButton(loginBtn);

        return rootPane;
    }
    private int _userID = 0;
    private String _role = "";

    private int _randomCode;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel alertLb;
    private javax.swing.JLabel img_loader;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblGymImg1;
    private javax.swing.JLabel lblGymImg2;
    private javax.swing.JLabel lblGymImg3;
    private javax.swing.JLabel lblGymImg4;
    private javax.swing.JLabel lblGymImg5;
    private javax.swing.JLabel lbl_backIcon;
    private javax.swing.JLabel lbl_close_hover;
    private javax.swing.JLabel lbl_forgotPassword;
    private javax.swing.JPanel loader;
    private javax.swing.JPanel login;
    private Button loginBtn;
    private Button loginBtn3;
    private Button loginBtn4;
    private javax.swing.JPasswordField passwordTf;
    private javax.swing.JPanel pnl_bg;
    private javax.swing.JPanel pnl_forgotPassword;
    private javax.swing.JLabel ptitLogo;
    private javax.swing.JTextField pwdShowed;
    private javax.swing.JLabel showPwdLbl;
    private javax.swing.JLabel showPwdLbl1;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JLabel txtEmailErr;
    private javax.swing.JTextField txtVer;
    private javax.swing.JLabel txtVerErr;
    private javax.swing.JTextField usernameTf;
    // End of variables declaration//GEN-END:variables
}

class JPanelGradient extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        Color color1 = new Color(78, 84, 200);
        Color color2 = new Color(143, 148, 251);

        GradientPaint gp = new GradientPaint(0, 0, color1, 180, height, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }
}

class JPanelGradient2 extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        Color color1 = new Color(211, 204, 227);
        Color color2 = new Color(233, 228, 240);

        GradientPaint gp = new GradientPaint(0, 0, color1, 180, height, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }
}
