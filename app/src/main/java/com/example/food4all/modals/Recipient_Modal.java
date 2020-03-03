package com.example.food4all.modals;

public class Recipient_Modal {
    private String org_name;
    private String type;
    private String usname;
    private String password;
    private String phone;
    private String address;

    public Recipient_Modal() {
    }

    public Recipient_Modal(String org_name, String type, String usname, String password, String phone, String address) {
        this.org_name = org_name;
        this.type = type;
        this.usname = usname;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsname() {
        return usname;
    }

    public void setUsname(String usname) {
        this.usname = usname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
