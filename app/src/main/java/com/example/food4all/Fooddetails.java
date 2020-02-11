package com.example.food4all;

import android.app.Application;

import com.google.firebase.database.Exclude;

public class Fooddetails  {
    private String name;
    private String phone;
    private String address;
    private String place;
    private String date;
    private String status;

    public String getDate() {
        return date;
    }

    public Fooddetails() {
    }

    public Fooddetails(String name, String phone, String address, String place, String date, String status) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.place = place;
        this.date = date;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setDate(String date) { this.date = date; }

    public void setStatus(String status) { this.status = status; }

    public String getStatus() { return status; }


}
