
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nguyen Hai Dang
 */
public class ConnectMysql {

    public static Connection getConnectDB() {
        if (_myConnect == null) {
            String user = "root";
            String pass = "";
            String url = "jdbc:mysql://localhost:3306/gym_equipment?zeroDateTimeBehavior=convertToNull";

            try {
                _myConnect = DriverManager.getConnection(url, user, pass);
                System.out.println("Kết nối thành công");
            } catch (SQLException ex) {
                System.out.println("Không thể kết nối với Database");
            }
        }
        return _myConnect;
    }

    private static Connection _myConnect = null;
}
