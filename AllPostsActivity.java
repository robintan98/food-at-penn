package com.foodatpenn;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodpenn.R;
import com.foodatpenn.data.RegisterStoreDataLocal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AllPostsActivity extends AppCompatActivity {

    private List<CreatePostsActivity.Post> posts;
    private TextView textView;
    private String allPosts;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_posts_activity);
        allPosts = this.getIntent().getStringExtra("RESULT");
        textView = findViewById(R.id.textPosts);
        textView.setText(allPosts);

        }
    }

