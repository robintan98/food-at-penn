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
import com.foodatpenn.data.PostStore;
import com.foodatpenn.data.PostStoreMongo;

public class CreatePostsActivity extends AppCompatActivity {
    PostStore postList;
    EditText food;
    EditText description;
    EditText locationFood;
   // MainActivity email = new MainActivity();
    private Button submitButton;
    String userEmail;
    String comments;

    private ArrayList<Post> posts;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_posts_activity);
        postList = PostStoreMongo.getInstance();

        //String emailId = email.getEmail();

        food = (EditText) findViewById(R.id.food);
        description = (EditText) findViewById(R.id.description);
        locationFood = (EditText) findViewById(R.id.locationFood);
        submitButton = (Button) findViewById(R.id.submitButton);
        userEmail = this.getIntent().getStringExtra("Email");
        comments = "";


        if (getIntent().getSerializableExtra("RESULT") == null){
            posts = new ArrayList<Post>();
        }
        else {
            posts = (ArrayList<Post>) getIntent().getSerializableExtra("RESULT");
            counter = posts.size();
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitButtonClicked();
            }
        });


    }

    private void submitButtonClicked() {
        //no comments when a post is first submitted
        Date current = Calendar.getInstance().getTime();
        Post p = new Post(food.getText().toString(), description.getText().toString(), locationFood.getText().toString(), current, counter, userEmail, comments);
        postList.addPost(current.toString(), food.getText().toString(), description.getText().toString(), Integer.toString(counter), locationFood.getText().toString(), userEmail, "");

        counter++;
        food.getText().clear();
        description.getText().clear();
        locationFood.getText().clear();

        posts.add(p);
    }

    public void moveToSeeAllPosts(View view) {
        Intent i = new Intent(this, AllPostsActivity.class);
        i.putExtra("RESULT", posts);

        startActivityForResult(i, 2);
    }

    public void moveToMyPosts(View view) {
        Intent i = new Intent(this, MyPostsActivity.class);
        i.putExtra("Email", userEmail);
        i.putExtra("RESULT", posts);
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


}
