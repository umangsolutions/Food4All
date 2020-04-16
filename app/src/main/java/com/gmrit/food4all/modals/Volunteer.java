package com.gmrit.food4all.modals;

import java.io.Serializable;

public class Volunteer implements Serializable {

    String Name;
    String Email;
    String Phone;
    int count=0;
    String category;

    public Volunteer() {
    }

    public Volunteer(String name, String email, String phone, int count, String category) {
        Name = name;
        Email = email;
        Phone = phone;
        this.count = count;
        this.category = category;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toString() {
        return  this.Phone;
    }
}
