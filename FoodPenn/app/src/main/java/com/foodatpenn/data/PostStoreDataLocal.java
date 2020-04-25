package com.foodatpenn.data;

import android.content.Context;
import android.widget.Toast;

import com.foodatpenn.MyApplication;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class PostStoreDataLocal implements PostStore{
    private static final String FILE_NAME = "data1.txt";
    HashMap<String, Posts> data;
    static PostStore instance = new PostStoreDataLocal();
    Context currentContext;


    private PostStoreDataLocal () {
        data  = new HashMap<String, Posts>();
        currentContext = MyApplication.getAppContext();
//        File root = currentContext.getExternalFilesDir(null);
//        File file = new File(root, "data.txt");
        FileInputStream fis = null;
        try {
            fis = currentContext.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String text;

            while ((text = br.readLine()) != null) {
                Posts newPost = Posts.fromString(text);
                if (newPost != null) {
                    data.put(newPost.getId(), newPost);
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

        try {
            fos = currentContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            String writeText = this.currentPostsString() + text;
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
        return null;
    }

    @Override
    public String getComments(String id) {
        return null;
    }

    @Override
    public void getSize(String id) {

    }

    @Override
    public void updateComments(String comments, String id) {

        Posts currentUser = data.get(id);
        currentUser.setComments(comments);

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

        FileOutputStream fos = null;

        try {
            fos = currentContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            String writeText = this.currentPostsString();
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

    @Override
    public void deletePost(String id) {
    }

    private String currentPostsString() {
        String returnVal = "";
        for (String id: data.keySet()) {
            returnVal += data.get(id).toString() + "\n";
        }
        return returnVal;
    }

    @Override
    public Map<String, Posts> getUsers() {
        return null;
    }

    @Override
    public void setUserMap() {

    }

}
