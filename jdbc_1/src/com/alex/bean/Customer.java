package com.alex.bean;

import java.sql.Date;
import java.util.Objects;

/**
 * ORM编程思想：（obeject releation mapping）
 * 一个数据表对应一个java类
 * 表中的一条记录对应java类中的一个对象
 * 表中的一个字段对应java类的一个属性
 */
public class Customer {

    private int id;
    private String name;
    private String email;
    private Date birth;


    public Customer() {
    }

    public Customer(Integer id, String name, String email, Date birth) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirth() {
        return this.birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Customer id(int id) {
        setId(id);
        return this;
    }

    public Customer name(String name) {
        setName(name);
        return this;
    }

    public Customer email(String email) {
        setEmail(email);
        return this;
    }

    public Customer birth(Date birth) {
        setBirth(birth);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Customer)) {
            return false;
        }
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(name, customer.name) && Objects.equals(email, customer.email) && Objects.equals(birth, customer.birth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, birth);
    }

    @Override
    public String toString() {
        return "{" +
            " id:" + getId() +
            ", name:'" + getName() + "'" +
            ", email:'" + getEmail() + "'" +
            ", birth:'" + getBirth() + "'" +
            "}";
    }

}
