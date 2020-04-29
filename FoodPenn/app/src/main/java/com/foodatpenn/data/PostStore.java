package com.foodatpenn.data;

import java.util.Map;

public interface PostStore {
    public void addPost(String date, String food, String description, String id, String location, String email, String comments);
    public boolean accountExists(String name);
    public String getFood(String id);
    public String getDescription(String id);
    public String getLocation(String id);
    public String getEmail(String id);
    public String getComments(String id);
    public void updateComments(String comments, String id);
    public void clearPost(String email);
    public void getSize(String id);
    public int getMax();
    public void modifyPost(String date, String food, String description, String location, String email);
    public void deletePost(String id);
    public Map<String, Posts> getUsers();

    public void setUserMap();
}
