package com.proyecto.cashcarp.clases;

import com.google.firebase.Timestamp;

public class Usuario {
    private String email;
    private String password;
    private String nickname;
    private double budget;
    private Timestamp registrationDate;


    public Usuario() {

    }

    public Usuario(String email, String password, String nickname, double budget) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.budget = budget;
        setRegistrationDate();
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate() {
        this.registrationDate = Timestamp.now();
    }
}
