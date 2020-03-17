package com.example.foodpenn;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {
    RegistrationStore users; //Will be implemented with singleton once we have the server running
    EditText userName;
    EditText password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        users = RegistrationStoreLocal.getInstance();
        userName = findViewById(R.id.newUsername);
        password = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.newPasswordConfirm);
    }

    public void checkCreateAccount(View view) {
        String newPassword = password.getText().toString();
        String newPasswordConfirm = confirmPassword.getText().toString();
        String newEmail = userName.getText().toString();
        if (newEmail.length() < 10 || !newEmail.substring(newEmail.length()-9).equals("upenn.edu")){
            printMessage("Please use a valid Penn Email");
        } else if (users.accountExists(newEmail)) {
            printMessage("Email already in use");
        } else if (!newPassword.equals(newPasswordConfirm)) {
            printMessage("Passwords must match");
        } else {

            users.addUser(newEmail, newPassword);
            printMessage("Success!");
            finishActivity(3);
            finish();
        }

    }

    private void printMessage(String message) {
        Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show();
    }
}
