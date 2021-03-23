
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nguyen Hai Dang
 */
public class connectMysql {
    public static Connection getConnectDB()
    {
        Connection myConnect = null;
        String user = "root";
        String pass = "";
        String url = "jdbc:mysql://localhost:3306/gym_equipment?zeroDateTimeBehavior=convertToNull";
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            myConnect = DriverManager.getConnection(url, user, pass);
            System.out.println("Kết nối thành công");
        } catch (ClassNotFoundException|SQLException ex) {
            System.out.println("Không thể kết nối với Database");
        }
        
        return myConnect;
    }
}
