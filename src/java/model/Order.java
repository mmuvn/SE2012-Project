/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author admin
 */
public class Order {
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_CANCELLED = 1;
    public static final int STATUS_APPROVED = 2;

    int orderID;
    Date orderDate;
    double total;
    String userID;
    int status;

    public Order() {
    }

    public Order(Date orderDate, double total, String userID) {
        this.orderDate = orderDate;
        this.total = total;
        this.userID = userID;
        this.status = STATUS_PENDING;
    }

    public Order(int orderID, Date orderDate, double total, String userID) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.total = total;
        this.userID = userID;
        this.status = STATUS_PENDING;
    }

    public Order(Date orderDate, double total, String userID, int status) {
        this.orderDate = orderDate;
        this.total = total;
        this.userID = userID;
        this.status = status;
    }

    public Order(int orderID, Date orderDate, double total, String userID, int status) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.total = total;
        this.userID = userID;
        this.status = status;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusLabel() {
        switch (status) {
            case STATUS_APPROVED:
                return "Approved";
            case STATUS_CANCELLED:
                return "Cancelled";
            default:
                return "Pending";
        }
    }

    @Override
    public String toString() {
        return "" + orderID + "-" + orderDate + "-" + total + "-" + userID + "-" + status;
    }
      
}
