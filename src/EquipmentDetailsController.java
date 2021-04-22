/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author skqist225
 */
public class EquipmentDetailsController {

    public List<Equipment_Details> getListEquipmentDetails() {

        List<Equipment_Details> lEI = new ArrayList<>();
        String query = "SELECT * FROM equipment_details";
        try {
            _con = ConnectMysql.getConnectDB();
            Statement st = _con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                lEI.add(new Equipment_Details(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4), rs.getInt(5), rs.getInt(6)
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lEI;
    }

    public boolean addNewEquipmentDetails(Equipment_Details eD) {
        String query = "INSERT INTO equipment_details(id,name,picture,price,warranty_time,supplier_id) VALUES(?,?,?,?,?,?)";
        try {
            _con = ConnectMysql.getConnectDB();
            PreparedStatement ps = _con.prepareStatement(query);
            ps.setString(1, eD.getId());
            ps.setString(2, eD.getName());
            ps.setString(3, eD.getPicture());
            ps.setInt(4, eD.getPrice());
            ps.setInt(5, eD.getWarranty_time());
            ps.setInt(6, eD.getSupplier_id());

            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isIdExist(String id) {
        String query = "SELECT * FROM equipment_details WHERE id = ?";
        try {
            _con = ConnectMysql.getConnectDB();
            PreparedStatement ps = _con.prepareStatement(query);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean updateEquipmentDetails(Equipment_Details eD) {
        String query = "UPDATE equipment_details SET name = ?, picture = ?, price = ?, warranty_time = ?,  supplier_id = ? where id = ?";
        try {
            PreparedStatement ps = _con.prepareStatement(query);
            ps.setString(1, eD.getName());
            ps.setString(2, eD.getPicture());
            ps.setInt(3, eD.getPrice());
            ps.setInt(4, eD.getWarranty_time());
            ps.setInt(5, eD.getSupplier_id());
            ps.setString(6, eD.getId());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean deleteEquipmentDetails(String id) {
        String query = "DELETE FROM equipment_details WHERE id not exist= ?";
        try {
            _con = ConnectMysql.getConnectDB();
            PreparedStatement ps = _con.prepareStatement(query);
            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Integer> getSupplierId() {
        List<Integer> listOfSuppliersId = new ArrayList<>();

        String query = "SELECT supplier_id FROM equipment_details";
        try {
            _con = ConnectMysql.getConnectDB();
            Statement st = _con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                listOfSuppliersId.add(rs.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listOfSuppliersId;
    }
    
    private Connection _con = null;
}
