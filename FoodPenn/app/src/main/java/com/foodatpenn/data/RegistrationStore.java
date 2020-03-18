package com.foodatpenn.data;

public interface RegistrationStore {
    public void addUser(String email, String password, String name, int year, String phone);
    public boolean verifyLogin(String name, String password);
    public boolean accountExists(String name);
    public String getName(String email);
    public int getClassYear(String email);
    public String getPhone(String email);
}