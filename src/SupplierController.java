
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author skqist225
 */
public class SupplierController {
    
    public List<Supplier> getSuppliersInfo() {

        List<Supplier> listOfSuppliers = new ArrayList<>();
        String query = "SELECT * FROM suppliers";
        try {
            _con = ConnectMysql.getConnectDB();
            Statement st = _con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                listOfSuppliers.add(new Supplier(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfSuppliers;
    }

    public boolean addNewSupplier(Supplier supplier) {
        String query = "INSERT INTO suppliers(id,name,address,phone_number) VALUES(?,?,?,?)";
        try {
            _con = ConnectMysql.getConnectDB();
            PreparedStatement ps = _con.prepareStatement(query);
            ps.setInt(1, supplier.getSupplierId());
            ps.setString(2, supplier.getName());
            ps.setString(3, supplier.getAddress());
            ps.setString(4, supplier.getPhoneNumber());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isIdExist(int id) {
        String query = "SELECT * FROM suppliers WHERE id = ?";
        try {
            _con = ConnectMysql.getConnectDB();
            PreparedStatement ps = _con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean updateSupplier(Supplier s) {
        String query = "UPDATE suppliers SET name = ?, address = ?, phone_number = ? where id = ?";
        try {
            PreparedStatement ps = _con.prepareStatement(query);
            ps.setString(1, s.getName());
            ps.setString(2, s.getAddress());
            ps.setString(3, s.getPhoneNumber());
            ps.setInt(4, s.getSupplierId());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean deleteSupplier(int id) {
        _eDC = new EquipmentDetailsController();
        for (int idd : _eDC.getSupplierId()) {
            if (idd == id) {
                return false;
            }
        }
        String query = "DELETE FROM suppliers WHERE id = ?";
        try {

            _con = ConnectMysql.getConnectDB();
            PreparedStatement ps = _con.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private Connection _con = null;
    private EquipmentDetailsController _eDC = null;
}
