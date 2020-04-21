package com.foodatpenn;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodpenn.R;
import com.foodatpenn.data.PostStore;
import com.foodatpenn.data.PostStoreMongo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyPostsActivity extends AppCompatActivity {

    PostStore postList;
    private ArrayList<Post> posts;
    private TextView textView;
    private String allPosts;
    private Button modifyButton;
    private Button remove;
    EditText id;
    EditText food;
    EditText description;
    EditText locationFood;
    EditText removeId;
    String userEmail;
    TextView name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_posts_activity);

        postList = PostStoreMongo.getInstance();
        posts = (ArrayList<Post>) getIntent().getSerializableExtra("RESULT");

        id = (EditText) findViewById(R.id.id1);
        food = (EditText) findViewById(R.id.food1);
        description = (EditText) findViewById(R.id.description1);
        locationFood = (EditText) findViewById(R.id.locationFood1);
        modifyButton = (Button) findViewById(R.id.modifyButton);

        removeId = (EditText) findViewById(R.id.remove1);
        remove = (Button) findViewById(R.id.removeButton);
        showEntries();

        name = (TextView) findViewById(R.id.usernamePosts);

        userEmail = this.getIntent().getStringExtra("Email");

        name.setText(userEmail);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePost();
                finish();
                startActivity(getIntent());
            }
        });

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyButtonClicked();
                finish();
                startActivity(getIntent());
            }
        });
    }

    public void showEntries(){
        //        String allPosts = "";
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss ");

        for (int i = 0; i < posts.size(); i++) {
            Date current = posts.get(i).getDate();
            String date = df.format(current);

            allPosts += date + "\n" +
                    "Food is " + posts.get(i).getFood() + "\n"
                    + "Description is " + posts.get(i).getDescription() + "\n"
                    + "Location is " + posts.get(i).getLocation() + "\n" + "ID is " + posts.get(i).getId() + "\n" + "\n";
        }

        textView = findViewById(R.id.textPosts);
        textView.setText(allPosts);
    }

    private void modifyButtonClicked() {
        Date current = Calendar.getInstance().getTime();
        String index = id.getText().toString();
        int finalValue = Integer.parseInt(index);
        postList.modifyPost(index,food.getText().toString(), description.getText().toString(),
                locationFood.getText().toString());
        for (int i = 0; i < posts.size(); i++){
            Post curr = posts.get(i);
            if (curr.getId() == finalValue){
                Post p = new Post(food.getText().toString(), description.getText().toString(),
                        locationFood.getText().toString(), current, finalValue, posts.get(i).getEmail(), posts.get(i).getComments());
                posts.set(i, p);
            }
        }

        id.getText().clear();
        food.getText().clear();
        description.getText().clear();
        locationFood.getText().clear();
    }

    public void removePost() {
        String index = removeId.getText().toString();
        postList.deletePost(index);
        int finalValue = Integer.parseInt(index);
        for (int i = 0; i < posts.size(); i++){
            Post curr = posts.get(i);
            if (curr.getId() == finalValue){
                posts.remove(i);
            }
        }
    }
}
