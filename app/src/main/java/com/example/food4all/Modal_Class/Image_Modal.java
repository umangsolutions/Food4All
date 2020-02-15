package com.example.food4all.Modal_Class;

public class Image_Modal {
    private String name;
    private String date;
    private String noofplaces;
    private String area;
    private String url;

    public Image_Modal() {
    }

    public Image_Modal(String name, String date, String noofplaces, String area, String url) {
        this.name = name;
        this.date = date;
        this.noofplaces = noofplaces;
        this.area = area;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNoofplaces() {
        return noofplaces;
    }

    public void setNoofplaces(String noofplaces) {
        this.noofplaces = noofplaces;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
