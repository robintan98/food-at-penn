package com.example.foodpenn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText userEmail;
    private EditText password;
    private Button login;
    private Button createAcct;
    private RegistrationStore registered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userEmail = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        createAcct = (Button) findViewById(R.id.createAcct);
        registered = RegistrationStoreLocal.getInstance();
    }

    public void loginAttempt(View view) {
        String userName = userEmail.getText().toString();
        String passString = password.getText().toString();

        if (validate(userName, passString)) {
            Intent i = new Intent(this, LandingPageActivity.class);
            startActivityForResult(i, 2);
        } else {
            Toast.makeText(
                    this,
                    "Username or password incorrect",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private boolean validate (String userName, String passString) {
        return registered.verifyLogin(userName, passString);
    }

    public void jumpToRegistration(View view) {
        Intent i = new Intent(this, RegistrationActivity.class);
        startActivityForResult(i, 2);
    }


}
