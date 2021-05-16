
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class FileController {

    public static void saveImportDetailFile(File file, ArrayList<EquipmentData> dataList) {
        FileOutputStream fileOutput = null;
        ObjectOutputStream objectOuput = null;
        try {
            fileOutput = new FileOutputStream(file);
            objectOuput = new ObjectOutputStream(fileOutput);
            objectOuput.writeObject(dataList);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Không thể lưu file!");
        } finally {
            try {
                fileOutput.close();
                objectOuput.close();
                new AlertFrame("Lưu file thành công").setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(FileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static Object readImportDetailFile(File file) {
        FileInputStream fileInput = null;
        ObjectInputStream objectInput = null;
        Object readData = null;

        try {
            fileInput = new FileInputStream(file);
            objectInput = new ObjectInputStream(fileInput);
            readData = objectInput.readObject();
            return readData;
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            if (readData != null) {
                new AlertFrame("Load dữ liệu thành công").setVisible(true);
            }
            else {
                JOptionPane.showMessageDialog(null, "Không thể đọc dữ liệu từ file!");
            }
            try {
                fileInput.close();
                objectInput.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        return null;
    }
}
