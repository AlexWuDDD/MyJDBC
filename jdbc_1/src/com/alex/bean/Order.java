package com.alex.bean;

import java.sql.Date;
import java.util.Objects;

public class Order {
    private int orderId;
    private String orderName;
    private Date orderDate;
    

    public Order() {
    }

    public Order(int orderId, String orderName, Date orderDate) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return this.orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return this.orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Date getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Order orderId(int orderId) {
        setOrderId(orderId);
        return this;
    }

    public Order orderName(String orderName) {
        setOrderName(orderName);
        return this;
    }

    public Order orderDate(Date orderDate) {
        setOrderDate(orderDate);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Order)) {
            return false;
        }
        Order order = (Order) o;
        return orderId == order.orderId && Objects.equals(orderName, order.orderName) && Objects.equals(orderDate, order.orderDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderName, orderDate);
    }

    @Override
    public String toString() {
        return "{" +
            " orderId:" + getOrderId() +
            ", orderName:'" + getOrderName() + "'" +
            ", orderDate:'" + getOrderDate() + "'" +
            "}";
    }
    
}
