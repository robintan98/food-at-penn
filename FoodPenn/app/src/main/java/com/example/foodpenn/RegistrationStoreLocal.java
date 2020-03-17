package com.example.foodpenn;

import java.util.HashMap;

public class RegistrationStoreLocal implements RegistrationStore{

    HashMap<String, String> data;
    static RegistrationStore instance = new RegistrationStoreLocal();


    private RegistrationStoreLocal () {
        data  = new HashMap<String, String>();
    }

    public static RegistrationStore getInstance() {
        return instance;
    }

    @Override
    public void addUser(String name, String password) {
        data.put(name, password);
    }

    @Override
    public boolean verifyLogin(String name, String password) {
        if (name == null) {
            return false;
        }
        String passwordActual = data.get(name);
        if (password == null || passwordActual == null || !password.equals(passwordActual)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean accountExists(String name) {
        if (name != null) {
            return data.containsKey(name);
        }
        throw new NullPointerException("Null username");
    }
}
