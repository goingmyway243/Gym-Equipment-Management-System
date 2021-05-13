
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nguyen Hai Dang
 */
public class UserController {

    public List<User> getUsersInfo() {

        List<User> listOfUsers = new ArrayList<>();
        String query = "SELECT * FROM users";
        try {
            _connector = ConnectMysql.getConnectDB();
            Statement st = _connector.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                listOfUsers.add(new User(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getTimestamp(8),
                        rs.getTimestamp(8)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfUsers;
    }

    public boolean addNewUser(User user) {
        String query = "INSERT INTO users(id,firstName,lastName,birthDay,email,contactNumber,profilePicture,created_at,updated_at) "
                + "VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            _connector = ConnectMysql.getConnectDB();
            PreparedStatement ps = _connector.prepareStatement(query);
            ps.setInt(1, user.getUserID());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setDate(4, user.getBirthday());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getContactNumber());
            ps.setString(7, user.getProfilePicture());
            ps.setTimestamp(8, user.getCreatedAt());
            ps.setTimestamp(9, user.getUpdatedAt());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isIdExist(int id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try {
            _connector = ConnectMysql.getConnectDB();
            PreparedStatement ps = _connector.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean updateUser(User user) {
        String query = "UPDATE users SET firstName = ?, lastName = ?, birthDay = ?,"
                + "email = ?,contactNumber = ?,profilePicture = ?,updated_at = ?, where id = ?";
        try {
            PreparedStatement ps = _connector.prepareStatement(query);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setDate(3, user.getBirthday());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getContactNumber());
            ps.setString(6, user.getProfilePicture());
            ps.setTimestamp(7, user.getUpdatedAt());
            ps.setInt(8, user.getUserID());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    private Connection _connector = null;
    private UserController _uC = null;
}
