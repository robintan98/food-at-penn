package com.foodatpenn.data;

import java.util.HashMap;
import java.util.Map;

public class PostStoreLocal implements PostStore {

    HashMap<String, Posts> data;
    static PostStore instance = new PostStoreLocal();


    private PostStoreLocal () {
        data  = new HashMap<String, Posts>();
    }

    public static PostStore getInstance() {
        return instance;
    }

    @Override
    public void addPost(String date, String food, String description, String id, String location, String email) {
        data.put(id, new Posts(date, food, description, id, location, email));
    }


    @Override
    public boolean accountExists(String name) {
        if (name != null) {
            return data.containsKey(name);
        }
        throw new NullPointerException("Null username");

    }


    @Override
    public String getFood(String id) {
        return data.get(id).getFood();
    }

    @Override
    public String getDescription(String id) {
        return data.get(id).getDescription();
    }

    @Override
    public String getLocation(String id) {
        return data.get(id).getLocation();
    }
    @Override
    public String getEmail(String id) {
        return data.get(id).getEmail();
    }

    @Override
    public void getSize(String id) {

    }

    @Override
    public int getMax() {
        return 0;
    }

    @Override
    public void modifyPost(String food, String description, String id, String location, String email) {
        Posts currentUser = data.get(id);
        currentUser.setFood(food);
        currentUser.setDescription(description);
        currentUser.setLocation(location);
    }

    @Override
    public void deletePost(String id) {

    }

    @Override
    public Map<String, Posts> getUsers() {
        return null;
    }

    @Override
    public void setUserMap() {

    }
}
