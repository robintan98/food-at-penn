package com.foodatpenn;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.foodpenn.R;
import com.foodatpenn.data.RegistrationStore;
import com.foodatpenn.data.RegistrationStoreLocal;

public class LandingPageActivity extends AppCompatActivity{
    TextView centerMessage;
    RegistrationStore rs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        rs = RegistrationStoreLocal.getInstance();

        centerMessage = findViewById(R.id.centerMessage);
        String userEmail = this.getIntent().getStringExtra("Email");

        centerMessage.setText("Success!!!  Welcome " + rs.getName(userEmail) + ", Member of Class of " + rs.getClassYear(userEmail) + " with phone " + rs.getPhone(userEmail));
    }

    public void moveToModify(View view) {
        Intent i = new Intent(this, ModifyPageActivity.class);

    }
}
