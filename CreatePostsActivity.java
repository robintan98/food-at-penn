package com.foodatpenn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodpenn.R;

public class CreatePostsActivity extends AppCompatActivity {

    EditText food;
    EditText description;
    EditText locationFood;
    private Button submitButton;
    private List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_posts_activity);

        food = (EditText) findViewById(R.id.food);
        description = (EditText) findViewById(R.id.description);
        locationFood = (EditText) findViewById(R.id.locationFood);
        submitButton = (Button) findViewById(R.id.submitButton);

        posts = new ArrayList<Post>();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitButtonClicked();
            }
        });


    }

    private void submitButtonClicked() {
        Date current = Calendar.getInstance().getTime();
        Post p = new Post(food.getText().toString(), description.getText().toString(),
                locationFood.getText().toString(), current);
        food.getText().clear();
        description.getText().clear();
        locationFood.getText().clear();

        posts.add(p);
    }

    public void moveToSeeAllPosts(View view) {
        String allPosts = "";
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzzz");

        for (int i = 0; i < posts.size(); i++) {
            Date current = posts.get(i).getDate();
            String date = df.format(current);

            allPosts += date + "\n" +
                    "Food is " + posts.get(i).getFood() + "\n"
                    + "Description is " + posts.get(i).getDescription() + "\n"
                    + "Location is " + posts.get(i).getLocation() + "\n" + "\n";
        }

        Intent i = new Intent(this, AllPostsActivity.class);
        i.putExtra("RESULT", allPosts);
        startActivityForResult(i, 2);
    }

    public void sortRecentPosts(View view) {

        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post1, Post post2) {
                if (post1.getDate().getTime() < post2.getDate().getTime()) {
                    return 1;
                }
                else if (post1.getDate().getTime() == post2.getDate().getTime()) {
                    return 0;
                }
                else {
                    return -1;
                }
            }
        });

    }

   public class Post {
       private String food1;
       private String description1;
       private String location1;
       private Date date1;
       public Post(String f, String d, String l, Date da) {
           food1 = f;
           description1 = d;
           location1 = l;
           date1 = da;
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
   }


}
