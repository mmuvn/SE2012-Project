/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author admin
 */
public class Product {
    int productID;
    String productName;
    String image;
    double price;
    int quantity;
    String categoryID;
    String description;
    int status;
    int totalSold;

    public Product() {
    }

    public Product(String productName, String image, double price, int quantity, String categoryID, String description, int status) {
        this.productName = productName;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.categoryID = categoryID;
        this.description = description;
        this.status = status;
        this.totalSold = 0;
    }

    public Product(int productID, String productName, String image, double price, int quantity, String categoryID, String description, int status) {
        this.productID = productID;
        this.productName = productName;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.categoryID = categoryID;
        this.description = description;
        this.status = status;
        this.totalSold = 0;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(int totalSold) {
        this.totalSold = totalSold;
    }

    @Override
    public String toString() {
        return "" + productID + "-" + productName + "-" + image + "-" + price + "-" + quantity + "-" + categoryID + "-" + description + "-" + status + "-" + totalSold;
    }
}
