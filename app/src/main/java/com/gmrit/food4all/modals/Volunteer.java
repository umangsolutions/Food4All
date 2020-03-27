package com.gmrit.food4all.modals;

public class Volunteer {
    private String Name;
    private String Email;
    private String Phone;
    private int count=0;

    public Volunteer() {
    }

    public Volunteer(String name, String email, String phone, int count) {
        Name = name;
        Email = email;
        Phone = phone;
        this.count = count;
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


    public String toString() {
        return  this.Phone;
    }
}
