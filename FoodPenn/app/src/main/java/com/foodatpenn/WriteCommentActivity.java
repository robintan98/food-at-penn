package com.foodatpenn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodpenn.R;

public class WriteCommentActivity extends AppCompatActivity {

    String comments;
    String id;
    TextView commentsScreen;
    EditText addComment;
    TextView idNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_comment);

        id = this.getIntent().getStringExtra("ID").toString();
        comments = "";

        commentsScreen = (TextView) findViewById(R.id.comments);
        commentsScreen.setText(comments);

        addComment = (EditText) findViewById(R.id.addComment);

        idNumber = (TextView) findViewById(R.id.idNum);

        idNumber.setText("on Post with ID " + id);

    }

    public void submitButton(View view) {
        comments += "\n" + addComment.getText().toString();

        commentsScreen.setText(comments);

        //AllPostsActivity obj = new AllPostsActivity();
        //obj.updateComments(comments, Integer.parseInt(id.toString()));
    }


}
