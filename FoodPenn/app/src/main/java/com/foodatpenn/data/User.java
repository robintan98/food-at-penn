package com.foodatpenn.data;

public class User {
    private String email, name, phoneNumber, password;
    private int classYear;

    public User (String email, String password, String name, int year, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.classYear = year;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public int getClassYear() {
        return classYear;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setClassYear(int classYear) {
        this.classYear = classYear;
    }
}
