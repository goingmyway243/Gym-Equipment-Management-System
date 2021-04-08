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
     private int supplierId;
    private String name;
    private String address;
    private String phoneNumber;

    public Supplier(int supplierId, String name, String address, String phoneNumber) {
        this.supplierId = supplierId;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Supplier() {
    }

    public int getSupplierId() {
        return supplierId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
