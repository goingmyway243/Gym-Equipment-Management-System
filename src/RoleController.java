
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class RoleController {
    public RoleController()
    {    
    }
    
    public String getRole(int id)
    {
        String sql = "select * from role where id = '" + id +"'";
        
        try {
            PreparedStatement ps = _connector.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return rs.getString("role");
            
        } catch (SQLException ex) {
            Logger.getLogger(RoleController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    Connection _connector = ConnectMysql.getConnectDB();
}
