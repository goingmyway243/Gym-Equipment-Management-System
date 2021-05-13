
import java.sql.Date;
import java.sql.Timestamp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nguyen Hai Dang
 */
public class User {

    public User(int userID, String firstName, String lastName, Date birthday, String email, String contactNumber,
            String profilePicture, Timestamp createAt, Timestamp updatedAt) {
        _userID = userID;
        _firstName = firstName;
        _lastName = lastName;
        _birthday = birthday;
        _email = email;
        _contactNumber = contactNumber;
        _profilePicture = profilePicture;
        _createdAt = createAt;
        _updatedAt = updatedAt;
    }

    public int getUserID() {
        return _userID;
    }

    public String getName() {
        return _lastName + " " + _firstName;
    }

    public String getFirstName() {
        return _firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public Date getBirthday() {
        return _birthday;
    }

    public String getEmail() {
        return _email;
    }

    public String getContactNumber() {
        return _contactNumber;
    }

    public String getProfilePicture() {
        return _profilePicture;
    }

    public Timestamp getCreatedAt() {
        return _createdAt;
    }

    public Timestamp getUpdatedAt() {
        return _updatedAt;
    }

    public void setUserID(int _userID) {
        this._userID = _userID;
    }

    public void setFirstName(String _firstName) {
        this._firstName = _firstName;
    }

    public void setLastName(String _lastName) {
        this._lastName = _lastName;
    }

    public void setBirthday(Date _birthday) {
        this._birthday = _birthday;
    }

    public void setEmail(String _email) {
        this._email = _email;
    }

    public void setContactNumber(String _contactNumber) {
        this._contactNumber = _contactNumber;
    }

    public void setProfilePicture(String _profilePicture) {
        this._profilePicture = _profilePicture;
    }

    public void setCreatedAt(Timestamp _createdAt) {
        this._createdAt = _createdAt;
    }

    public void setUpdatedAt(Timestamp _updatedAt) {
        this._updatedAt = _updatedAt;
    }

    private int _userID;
    private String _firstName;
    private String _lastName;
    private Date _birthday;
    private String _email;
    private String _contactNumber;
    private String _profilePicture;
    private Timestamp _createdAt;
    private Timestamp _updatedAt;
}
