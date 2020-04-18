package com.foodatpenn.data;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.foodatpenn.MainActivity;
import com.foodatpenn.MyApplication;
import com.foodatpenn.Retrofit.IMyService;
import com.foodatpenn.Retrofit.RetrofitClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class RegistrationStoreMongo implements RegistrationStore {
    private static final String FILE_NAME = "data.txt";
    HashMap<String, User> data;
    static RegistrationStore instance = new RegistrationStoreMongo();
    Context currentContext;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    boolean status;
    User currentUser, defaultUser;



    private RegistrationStoreMongo () {

        defaultUser = new User("waiting on server", "", "waiting on server", 1111, "waiting on server");
        currentUser = defaultUser;

        data  = new HashMap<String, User>();
        currentContext = MyApplication.getAppContext();

        status = false;
//        File root = currentContext.getExternalFilesDir(null);
//        File file = new File(root, "data.txt");
        FileInputStream fis = null;
        try {
            fis = currentContext.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String text;

            while ((text = br.readLine()) != null) {
                User newUser = User.fromString(text);
                if (newUser != null) {
                    data.put(newUser.getEmail(), newUser);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //Init service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

    }

    public static RegistrationStore getInstance() {
        return instance;
    }



    @Override
    public void addUser(String email, String password, String name, int year, String phone) {
        User newUser = new User(email, password, name, year, phone);
        data.put(email, newUser);

        String text = newUser.toString();
        FileOutputStream fos = null;



        compositeDisposable.add(iMyService.registerUser(email, name, password, Integer.toString(year), phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                    }
                }));


        try {
            fos = currentContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            String writeText = this.currentUsersString() + text;
            fos.write(writeText.getBytes());

            Toast.makeText(currentContext, "Saved to " + currentContext.getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private void setStatus(boolean bool) {
        status = bool;
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

        if (password == null) {
            return false;
        }

        final String serverResponse;
        final boolean returnVal;


        compositeDisposable.add(iMyService.loginUser(name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        if (response.contains("successful")) {
                            setStatus(true);
                        }
                    }
                }));

        if (status == true) {
            status = false;
            return true;
        }
        return false;
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

        FileOutputStream fos = null;

        try {
            fos = currentContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            String writeText = this.currentUsersString();
            fos.write(writeText.getBytes());

            Toast.makeText(currentContext, "Saved to " + currentContext.getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String currentUsersString() {
        String returnVal = "";
        for (String userEmail: data.keySet()) {
            returnVal += data.get(userEmail).toString() + "\n";
        }
        return returnVal;
    }

    class AndroidSucksException extends IllegalStateException {
        public AndroidSucksException (String s) {
            super(s);
        }
    }
}
