package com.example.foodpenn;

public interface RegistrationStore {
    public void addUser(String name, String password);
    public boolean verifyLogin(String name, String password);
    public boolean accountExists(String name);
}
