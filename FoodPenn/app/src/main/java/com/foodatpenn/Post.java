package com.foodatpenn;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {
    private String food1;
    private String description1;
    private String location1;
    private Date date1;
    private int id;
    private String email;
    private String comments;
    public Post(String f, String d, String l, Date da, int iden, String em, String comm) {
        food1 = f;
        description1 = d;
        location1 = l;
        date1 = da;
        id = iden;
        email = em;
        comments = comm;
    }

    public String getFood() {
        return food1;
    }

    public String getDescription() {
        return description1;
    }

    public String getLocation() {
        return location1;
    }

    public Date getDate() {
        return date1;
    }

    public int getId() {return id; }

    public String getEmail() {return email; }

    public String getComments() {return comments; }

    public void setComments(String newComm) {comments = newComm;}
}