
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

    private String id;
    private String name;
    private String picture;
    private int price;
    private int warranty_time;
    private int supplier_id;

    public Equipment_Details(String id, String name, String picture, int price, int warranty_time, int supplier_id) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.price = price;
        this.warranty_time = warranty_time;
        this.supplier_id = supplier_id;

    }

    public Equipment_Details() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public int getPrice() {
        return price;
    }

    public int getWarranty_time() {
        return warranty_time;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setWarranty_time(int warranty_time) {
        this.warranty_time = warranty_time;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }
}
