package com.example.food4all;

public class Restaurants {
    private String Name;
    private String Email;
    private Long Phone;
    private String Address;
    private Float Quantity;

    public Restaurants() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public Long getPhone() {
        return Phone;
    }

    public void setPhone(Long phone) {
        this.Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public Float getQuantity() {
        return Quantity;
    }

    public void setQuantity(Float quantity) {
        this.Quantity = quantity;
    }

    public String toString() {
        return this.Name + "-" + Address + "-" + Phone;
    }
}
