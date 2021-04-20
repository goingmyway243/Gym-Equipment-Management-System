
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class DeleteValue {
    private final Connection _connector = ConnectMysql.getConnectDB();
    private javax.swing.JFrame _parentFrame = null;
    
    public void setParentFrame(javax.swing.JFrame parent)
    {
        _parentFrame = parent;
    }
    
    public void deleteEquipment(String id)
    {
        String sql = "delete from gym_equipments where id = ?";
        try {
            PreparedStatement ps = _connector.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
            if(_parentFrame != null)
            {
                JOptionPane.showMessageDialog(_parentFrame, "Xóa thành công");
            }
            System.out.println("Xóa thành công");
        } catch (SQLException ex) {
            Logger.getLogger(DeleteValue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteImport(int id)
    {
        if(!checkImportDetail(id))
        {
            if(_parentFrame != null)
                JOptionPane.showMessageDialog(_parentFrame, "Không thể xóa phiếu nhập \n(tồn tại thiết bị đang quản lý thuộc phiếu này)");
            
            return;
        }
        String sql = "delete from import_details where id = ?";
        try {
            PreparedStatement ps = _connector.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            if(_parentFrame != null)
            {
                JOptionPane.showMessageDialog(_parentFrame, "Xóa thành công");
            }
            System.out.println("Xóa thành công");
        } catch (SQLException ex) {
            Logger.getLogger(DeleteValue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteEquipmentDetail(String id)
    {
        if(!checkEquipmentDetail(id))
        {
            if(_parentFrame != null)
                JOptionPane.showMessageDialog(_parentFrame, "Không thể xóa loại thiết bị \n(tồn tại thiết bị đang quản lý thuộc loại thiết bị này)");
            
            return;
        }
        
        String sql = "delete from equipment_details where id = ?";
        try {
            PreparedStatement ps = _connector.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
            if(_parentFrame != null)
            {
                JOptionPane.showMessageDialog(_parentFrame, "Xóa thành công");
            }
            System.out.println("Xóa thành công");
        } catch (SQLException ex) {
            Logger.getLogger(DeleteValue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteSupplier(int id)
    {
        if(!checkSupplier(id))
        {
            if(_parentFrame != null)
                JOptionPane.showMessageDialog(_parentFrame, "Không thể xóa nhà cung cấp \n(tồn tại loại thiết bị đang quản lý thuộc nhà cung cấp này)");
            
            return;
        }
        String sql = "delete from supplier where id = ?";
        try {
            PreparedStatement ps = _connector.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            if(_parentFrame != null)
            {
                JOptionPane.showMessageDialog(_parentFrame, "Xóa thành công");
            }
            System.out.println("Xóa thành công");
        } catch (SQLException ex) {
            Logger.getLogger(DeleteValue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteUser(int id)
    {
        if(!checkUser(id))
        {
            if(_parentFrame != null)
                JOptionPane.showMessageDialog(_parentFrame, "Không thể xóa thông tin nhân viên hiện tại \n(tồn tại phiếu nhập do nhân viên này quản lý)");
            
            return;
        }
        String sql = "delete from equipment_details where id = ?";
        try {
            PreparedStatement ps = _connector.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            if(_parentFrame != null)
            {
                JOptionPane.showMessageDialog(_parentFrame, "Xóa thành công");
            }
            System.out.println("Xóa thành công");
        } catch (SQLException ex) {
            Logger.getLogger(DeleteValue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean checkImportDetail(int id)
    {
        String sql = "select * from gym_equipments where import_id = ?";
        try {
            PreparedStatement ps = _connector.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(DeleteValue.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    private boolean checkEquipmentDetail(String id)
    {
        String sql = "select * from gym_equipments where detail_id = ?";
        try {
            PreparedStatement ps = _connector.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(DeleteValue.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    private boolean checkUser(int id)
    {
        String sql = "select * from import_details where user_id = ?";
        try {
            PreparedStatement ps = _connector.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(DeleteValue.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    private boolean checkSupplier(int id)
    {
        String sql = "select * from equipment_details where supplier_id = ?";
        try {
            PreparedStatement ps = _connector.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(DeleteValue.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
