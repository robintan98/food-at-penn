package com.foodatpenn;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodpenn.R;
import com.foodatpenn.data.RegistrationStore;
import com.foodatpenn.data.RegistrationStoreMongo;

import java.util.Arrays;
import java.util.Map;

public class ViewAllActivity extends AppCompatActivity {
    TextView displayName, year, rating;
    RegistrationStore users;
    String userEmail;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_users);
        users = RegistrationStoreMongo.getInstance();
        displayName = findViewById(R.id.viewAllName);
        rating = findViewById(R.id.viewAllRating);
        year = findViewById(R.id.viewAllClassYear);
        spinner = findViewById(R.id.spinner);

        Map<String, String> usersForSpinner = users.getUsers();
        String[] arr = new String[usersForSpinner.keySet().size()];
        int counter = 0;
        for (String s: usersForSpinner.keySet()) {
            arr[counter] = s;
            counter += 1;
        }

        Arrays.sort(arr);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        userEmail = this.getIntent().getStringExtra("Email");

    }

    public void findUser(View view) {
        String userEmail = spinner.getSelectedItem().toString();
        displayName.setText(users.getName(userEmail));
        year.setText("" + users.getClassYear(userEmail));
        rating.setText(Double.toString(users.getRating(userEmail)));
    }



}
