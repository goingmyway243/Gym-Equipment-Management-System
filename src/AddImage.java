
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
 * @author skqist225
 */
public class AddImage extends javax.swing.JFrame {

    public JButton _button, browseButton, cancelButton, selectButton;
    public JPanel contentPane;

    public static AddImage getObj(JButton button, String title) {
        if (_obj == null) {
            _obj = new AddImage(button, title);
        }
        return _obj;
    }

    public static AddImage getObj() {
        return _obj;
    }

    public List<String> getFileNameList() {
        return _fileNameList;
    }

    public void setSelectedFile(File _selectedFile) {
        this._selectedFile = _selectedFile;
    }

    public AddImage(JButton button, String title) {
        _imagePath = null;
        setTitle(title);
        setSize(720, 460);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        contentPane = new JPanel(null);
        arrayLabel = new ArrayList<>();
        _fileNameList = new ArrayList<>();

        _imageFolderPath = new File("").getAbsolutePath().concat("/src/images/");
        listFilesForFolder(new File(_imageFolderPath));

        _button = button;
        createUI();

        closingWindow();
    }

    public void closingWindow() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                clearScreen();
            }
        });
    }

    public void clearScreen() {
        arrayLabel.forEach(label -> {
            label.setIcon(null);
            contentPane.remove(label);
        });
        contentPane.remove(browseButton);
        contentPane.remove(selectButton);
        contentPane.remove(cancelButton);
        contentPane.removeAll();
        _obj.remove(contentPane);
        _obj.repaint();
        AddImage._obj = null;
    }

    public String getImagePath() {
        return _imagePath;
    }

    public void setImagePath(String _imagePath) {
        this._imagePath = _imagePath;
    }

    public void createUI() {
        ImageIcon imgIcon = new ImageIcon(new File("").getAbsolutePath().concat("/src/icon/") + "add-image.png");
        Image newImage = imgIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon imgIconn = new ImageIcon(newImage);

        browseButton = new JButton(imgIconn);
        browseButton.setBounds(200, 380, 100, 40);

        selectButton = new JButton("Chọn");
        selectButton.setBounds(320, 380, 100, 40);

        cancelButton = new JButton("Thoát");
        cancelButton.setBounds(440, 380, 100, 40);

        JLabel labelImage = new JLabel();
        labelImage.setBounds(10, 10, 720, 340);
        JPanel panel = new JPanel();

        int row = (int) (_fileNameList.size() % 3 == 0 ? _fileNameList.size() / 3 : Math.round((_fileNameList.size() / 3) + 0.5));
        panel.setLayout(new GridLayout(row, 3, 5, 5));

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

        contentPane.setPreferredSize(new Dimension(740, 440));
        contentPane.add(scrollPane);
        contentPane.add(browseButton);
        contentPane.add(selectButton);
        contentPane.add(cancelButton);
        this.setContentPane(contentPane);
        this.pack();

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
            boolean hasItemChoosed = false;
            for (JLabel label : arrayLabel) {
                if (label.getBorder() != null) {
                    hasItemChoosed = true;
                }
            }

            if (hasItemChoosed) {
                int x = 0;
                for (JLabel label : arrayLabel) {
                    if (label.getBorder() != null) {
                        break;
                    }
                    x++;
                }
                _button.setText(_fileNameList.get(x));
                setImagePath("/src/images/" + _fileNameList.get(x));
                _selectedFile = new File(_imageFolderPath + _fileNameList.get(x));
                this.dispose();
                clearScreen();
            } else {
                return;
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                clearScreen();
                dispose();
            }
        });

        JButton backButton = new JButton("Quay lại");
        backButton.setBounds(200, 380, 160, 40);

        JButton selectButton2 = new JButton("Chọn");
        selectButton2.setBounds(380, 380, 100, 40);

        backButton.addActionListener((ActionEvent ae) -> {
            contentPane.removeAll();
            this.remove(contentPane);
            this.repaint();

            contentPane.add(scrollPane);
            contentPane.add(selectButton);
            browseButton.setBounds(200, 380, 100, 40);
            contentPane.add(browseButton);
            contentPane.add(cancelButton);
            setImagePath(null);
            _button.setText("Chọn hình ảnh!");
        });

        browseButton.addActionListener((ActionEvent e) -> {
            contentPane.remove(selectButton);
            contentPane.remove(cancelButton);
            this.repaint();
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
                this.remove(contentPane);
                this.repaint();
                contentPane.add(labelImage);
                contentPane.add(backButton);
                contentPane.add(selectButton2);
                labelImage.setIcon(ImageGenerator.ResizeImage(path, labelImage));

                selectButton2.addActionListener((ActionEvent ae) -> {
                    System.out.println(selectedFile);
                    _selectedFile = selectedFile;
                    _button.setText(selectedFile.getName());
                    setImagePath("/src/images/" + selectedFile.getName());
                    dispose();
                    clearScreen();
                });
            } else if (result == JFileChooser.CANCEL_OPTION) {
                System.out.println("\"Không có file nào được chọn\"");
                this.repaint();
                browseButton.setBounds(200, 380, 100, 40);
                this.add(selectButton);
                this.add(cancelButton);
            }
        });
    }

    public void saveImage() {
        File dest = new File(_imageFolderPath, _selectedFile.getName());
        try {
            if (!(_selectedFile.getParent() + "/").equals(_imageFolderPath)) {
                if (_fileNameList.contains(_selectedFile.getName())) {
                    dest = new File(_imageFolderPath, _selectedFile.getName() + "(copy)");
                }
                copyFileUsingStream(_selectedFile, dest);
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

    public static void copyFileUsingStream(File source, File dest) throws IOException {
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

    private String _imagePath;
    private String _imageFolderPath;
    private List<String> _fileNameList;
    private File _selectedFile;
    private List<JLabel> arrayLabel;
    private static AddImage _obj = null;
}
