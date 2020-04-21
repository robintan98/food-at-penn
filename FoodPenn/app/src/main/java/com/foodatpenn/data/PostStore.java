package com.foodatpenn.data;

import java.util.ArrayList;
import java.util.Map;

public interface PostStore {
    public void addPost(String date, String food, String description, String id, String location, String email, String comments);
    public boolean accountExists(String name);
    public String getFood(String id);
    public String getDescription(String id);
    public String getLocation(String id);
    public String getComments(String id);
    public String getEmail(String id);
    public void modifyPost(String date, String food, String description, String location);
    public void deletePost(String id);
    public Map<String, Posts> getUsers();
}
