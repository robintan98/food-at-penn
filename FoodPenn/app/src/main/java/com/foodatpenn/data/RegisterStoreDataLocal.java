package com.foodatpenn.data;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.foodatpenn.MyApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.util.HashMap;

public class RegisterStoreDataLocal implements RegistrationStore {

    HashMap<String, User> data;
    static RegistrationStore instance = new RegisterStoreDataLocal();


    private RegisterStoreDataLocal () {
        data  = new HashMap<String, User>();
        Context currentContext = MyApplication.getAppContext();
        File root = currentContext.getExternalFilesDir(null);
        File file = new File(root, "data.txt");


        try {
            //Create the file
            file.createNewFile();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String current = br.readLine();
            while (current != null) {
                User newUser = User.fromString(current);
                if (newUser != null) {
                    data.put(newUser.getEmail(), newUser);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static RegistrationStore getInstance() {
        return instance;
    }

    @Override
    public void addUser(String email, String password, String name, int year, String phone) {
        User newUser = new User(email, password, name, year, phone);
        data.put(email, newUser);
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("data.txt", true)));
            pw.println(newUser.toString());
            Log.v("Status", "Data Stored");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean verifyLogin(String name, String password) {
        if (name == null) {
            return false;
        }
        User user = data.get(name);
        if (user == null) {
            return false;
        }
        String passwordActual = user.getPassword();
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

    @Override
    public String getName(String email) {
        return data.get(email).getName();
    }

    @Override
    public int getClassYear(String email) {
        return data.get(email).getClassYear();
    }

    @Override
    public String getPhone(String email) {
        return data.get(email).getPhoneNumber();
    }

    @Override
    public void modifyUser(String email, String name, int year, String phone) {
        User currentUser = data.get(email);
        currentUser.setName(name);
        currentUser.setClassYear(year);
        currentUser.setPhoneNumber(phone);

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("data.txt", true)));
            pw.println(currentUser.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
