/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author skqist225
 */
public class Supplier {
    public Supplier(int _supplierId, String _name, String _address, String _phoneNumber) {
        this._supplierId = _supplierId;
        this._name = _name;
        this._address = _address;
        this._phoneNumber = _phoneNumber;
    }

    public Supplier() {
    }

    public int getSupplierId() {
        return _supplierId;
    }

    public String getName() {
        return _name;
    }

    public String getAddress() {
        return _address;
    }

    public String getPhoneNumber() {
        return _phoneNumber;
    }

    public void setSupplierId(int _supplierId) {
        this._supplierId = _supplierId;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public void setAddress(String _address) {
        this._address = _address;
    }

    public void setPhoneNumber(String _phoneNumber) {
        this._phoneNumber = _phoneNumber;
    }
    
    private int _supplierId;
    private String _name;
    private String _address;
    private String _phoneNumber;
}
