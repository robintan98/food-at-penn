package com.foodatpenn;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.foodpenn.R;
import com.foodatpenn.data.RegistrationStore;
import com.foodatpenn.data.RegisterStoreDataLocal;

public class LandingPageActivity extends AppCompatActivity {
    TextView centerMessage;
    RegistrationStore rs;
    String userEmail;
    Intent starterIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        rs = RegisterStoreDataLocal.getInstance();
        starterIntent = getIntent();
        centerMessage = findViewById(R.id.centerMessage);
        userEmail = this.getIntent().getStringExtra("Email");

        centerMessage.setText("Success!!!  Welcome " + rs.getName(userEmail) + ", Member of Class of " + rs.getClassYear(userEmail) + " with phone " + rs.getPhone(userEmail));
    }

    public void moveToModify(View view) {
        Intent i = new Intent(this, ModifyPageActivity.class);
        i.putExtra("Email", userEmail);
        startActivityForResult(i, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        recreate();
    }
}
