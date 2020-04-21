package com.foodatpenn.data;

import android.content.Context;
import android.util.Log;

import com.foodatpenn.MyApplication;
import com.foodatpenn.Retrofit.IMyService;
import com.foodatpenn.Retrofit.RetrofitClient;
import com.foodatpenn.Retrofit.RetrofitClientPosts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class PostStoreMongo implements PostStore {
    private static final String FILE_NAME = "data1.txt";
    HashMap<String, Posts> data;
    static PostStore instance = new PostStoreMongo();
    Context currentContext;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    boolean status, containsStatus;
    Posts currentUser, defaultUser;



    private PostStoreMongo () {

        defaultUser = new Posts("waiting on server", "", "waiting on server", "waiting on server", "waiting on server", "waiting on server", "waiting on server");
        currentUser = defaultUser;

        data  = new HashMap<String, Posts>();
        currentContext = MyApplication.getAppContext();

        status = false;
        containsStatus = true;


        //Init service
        Retrofit retrofitClientPosts = RetrofitClientPosts.getInstance();
        iMyService = retrofitClientPosts.create(IMyService.class);

    }

    public static PostStore getInstance() {
        return instance;
    }



    @Override
    public void addPost(String date, String food, String description, String id, String location, String email, String comments) {
        Posts newPost = new Posts(date, food, description, id, location, email, comments);
        data.put(id, newPost);

        String text = newPost.toString();
        FileOutputStream fos = null;



        compositeDisposable.add(iMyService.createPost(date, food, description, id, location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                    }
                }));


    }

    private void setStatus(boolean bool, int statusVar) {
        if (statusVar == 1) {
            status = bool;
        } else {
            containsStatus = bool;
        }
    }



    private void getLoggedInUser(String email) {
        compositeDisposable.add(iMyService.getUser(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        setCurrentUser(response);
                    }
                }));
    }

    private void setUserMap() {
        compositeDisposable.add(iMyService.allUsers("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONArray jsonArr = new JSONArray(response);
                        for (int i = 0; i < jsonArr.length(); i++) {
                            String date = jsonArr.getJSONObject(i).getString("date");
                            String food = jsonArr.getJSONObject(i).getString("food");
                            String description = jsonArr.getJSONObject(i).getString("description");
                            String id = jsonArr.getJSONObject(i).getString("id");
                            String location = jsonArr.getJSONObject(i).getString("location");
                            String email = jsonArr.getJSONObject(i).getString("email");
                            String comments = jsonArr.getJSONObject(i).getString("comments");
                            Posts currPost = new Posts(date, food, description, id, location, email, comments);
                            putUser(id, currPost);
                        }
                    }
                }));
    }

    private void putUser(String id, Posts post) {
        data.put(id, post);
    }

    private void setCurrentUser(String o) {
        try {
            JSONObject jObj = new JSONObject(o);
            Iterator<String> iter = jObj.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                Log.v(key, jObj.get(key).toString());
            }
            String date = jObj.getString("email");
            String food = jObj.getString("name");
            String description = jObj.getString("year");
            String id = jObj.getString("phone");
            String location = jObj.getString("location");
            String email = jObj.getString("email");
            String comments = jObj.getString("comments");
            Posts post = new Posts(date, food, description, id, location, email, comments);
            currentUser = post;
        } catch (JSONException e) {
            e.printStackTrace();
            currentUser = defaultUser;
        }
    }

    @Override
    public boolean accountExists(String name) {
        if (name != null) {
            compositeDisposable.add(iMyService.contains(name)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String response) throws Exception {
                            if (response.contains("false")) {
                                setStatus(false, 2);
                            }
                        }
                    }));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!containsStatus) {
                status = true;
                currentUser = defaultUser;
                return false;
            }
            return true;
        }

        throw new NullPointerException("Null username");
    }

    @Override
    public String getFood(String id) {
        if (id.equals(currentUser.getId())) {
            return currentUser.getFood();
        }
        return data.get(id).getFood();
    }



    @Override
    public String getDescription(String id) {
        if (id.equals(currentUser.getId())) {
            return currentUser.getDescription();
        }
        return data.get(id).getDescription();
    }

    @Override
    public String getLocation(String id) {
        if (id.equals(currentUser.getId())) {
            return currentUser.getLocation();
        }
        return data.get(id).getLocation();
    }

    @Override
    public String getEmail(String id) {
        if (id.equals(currentUser.getId())) {
            return currentUser.getEmail();
        }
        return data.get(id).getEmail();
    }

    @Override
    public String getComments(String id) {
        if (id.equals(currentUser.getId())) {
            return currentUser.getComments();
        }
        return data.get(id).getComments();
    }

    @Override
    public void modifyPost(String id, String food, String description, String location) {
        currentUser.setFood(food);
        currentUser.setDescription(description);
        currentUser.setLocation(location);

        compositeDisposable.add(iMyService.modifyPost(id, food, description, location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                    }
                }));
    }

    @Override
    public void deletePost(String id) {

        compositeDisposable.add(iMyService.deletePost(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                    }
                }));
    }

    private String currentUsersString() {
        String returnVal = "";
        for (String userEmail: data.keySet()) {
            returnVal += data.get(userEmail).toString() + "\n";
        }
        return returnVal;
    }

    @Override
    public Map<String, Posts> getUsers() {
        Map<String, Posts> returnVal = new HashMap<String, Posts>();
        for (String s: data.keySet()) {
            returnVal.put(s, data.get(s));
        }
        return returnVal;
    }


}
