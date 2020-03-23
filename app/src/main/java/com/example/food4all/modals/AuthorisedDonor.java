package com.example.food4all.modals;

public class AuthorisedDonor {
    private String no_of_pople;
    private String time_cooked;

    public AuthorisedDonor() {
    }

    public AuthorisedDonor(String no_of_pople, String time_cooked) {
        this.no_of_pople = no_of_pople;
        this.time_cooked = time_cooked;
    }

    public String getNo_of_pople() {
        return no_of_pople;
    }

    public void setNo_of_pople(String no_of_pople) {
        this.no_of_pople = no_of_pople;
    }

    public String getTime_cooked() {
        return time_cooked;
    }

    public void setTime_cooked(String time_cooked) {
        this.time_cooked = time_cooked;
    }
}
