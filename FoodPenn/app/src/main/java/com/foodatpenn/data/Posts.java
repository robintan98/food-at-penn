package com.foodatpenn.data;

import java.util.ArrayList;

public class Posts {
    private String id, food, description, location, date, email, comments;

    public Posts (String date, String food, String description, String id, String location, String email, String com) {
        this.date = date;
        this.food = food;
        this.description = description;
        this.id = id;
        this.location = location;
        this.email = email;
        this.comments = com;
    }


    public String getDate() {
        return date;
    }

    public String getFood() {
        return food;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location1) {
        this.location = location1;
    }

    public void setDescription(String description1) {
        this.description = description1;
    }

    public void setFood(String food1) {
        this.food = food1;
    }

    public void setDate(String date1) {
        this.date = date1;
    }

    public void setEmail(String email1) {this.email = email1;}

    public void setComments(String comments1) {this.comments = comments1;}

    public String getComments() {return comments;}

    public String getEmail() {return email;}


    @Override
    public String toString() {
        return date + ";" + food + ";" + description + ";" + id + ";" + location + ";" + email + ";" + comments;
    }

    public static Posts fromString(String s) {
        String[] data = s.split(";");
        if (data.length != 7) {
            return null;
        } else {
            return new Posts(data[0], data[1], data[2], data[4], data[3], data[5], data[6]);
        }
    }
}
