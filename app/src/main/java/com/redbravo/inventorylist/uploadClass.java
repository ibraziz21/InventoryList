package com.redbravo.inventorylist;
//constructors, getters and setters for uploading to firebase
public class uploadClass {
    private String productname;
    private int sku;
    private int quantity;
    private String imageuri;
    private int price;

    public uploadClass() {
    }

    public uploadClass(String productname, int sku, int quantity, String imageuri, int price) {
        this.productname = productname;
        this.sku = sku;
        this.quantity = quantity;
        this.imageuri = imageuri;
        this.price = price;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public int getSku() {
        return sku;
    }

    public void setSku(int sku) {
        this.sku = sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
