package com.alex.transaction;

import java.util.Objects;

public class User {
  private String user;
  private String password;  
  private int balance;


  public User() {
  }

  public User(String user, String password, int balance) {
    this.user = user;
    this.password = password;
    this.balance = balance;
  }

  public String getUser() {
    return this.user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getBalance() {
    return this.balance;
  }

  public void setBalance(int balance) {
    this.balance = balance;
  }

  public User user(String user) {
    setUser(user);
    return this;
  }

  public User password(String password) {
    setPassword(password);
    return this;
  }

  public User balance(int balance) {
    setBalance(balance);
    return this;
  }

  @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(user, user.user) && Objects.equals(password, user.password) && balance == user.balance;
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, password, balance);
  }

  @Override
  public String toString() {
    return "{" +
      " user='" + getUser() + "'" +
      ", password='" + getPassword() + "'" +
      ", balance='" + getBalance() + "'" +
      "}";
  }

}
