
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author skqist225
 */
public class Equipment_Details {

    public Equipment_Details(String _id, String _name, String _picture, int _price, int _warranty_time, int _supplier_id) {
        this._id = _id;
        this._name = _name;
        this._picture = _picture;
        this._price = _price;
        this._warranty_time = _warranty_time;
        this._supplier_id = _supplier_id;

    }

    public Equipment_Details() {
    }

    public String getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public String getPicture() {
        return _picture;
    }

    public int getPrice() {
        return _price;
    }

    public int getWarranty_time() {
        return _warranty_time;
    }

    public int getSupplier_id() {
        return _supplier_id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public void setPicture(String _picture) {
        this._picture = _picture;
    }

    public void setPrice(int _price) {
        this._price = _price;
    }

    public void setWarranty_time(int _warranty_time) {
        this._warranty_time = _warranty_time;
    }

    public void setSupplier_id(int _supplier_id) {
        this._supplier_id = _supplier_id;
    }
    
    private String _id;
    private String _name;
    private String _picture;
    private int _price;
    private int _warranty_time;
    private int _supplier_id;
}
