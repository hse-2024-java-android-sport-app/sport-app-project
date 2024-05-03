package org.sportApp.userInterface.registration;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import org.sportApp.userInterface.R;
import org.sportApp.userInterface.sportsman.SportsmanWindow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorizationWindow extends AppCompatActivity {
    Button bSignIn;
    EditText userName, password;
    boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        bSignIn = findViewById(R.id.signInButton);

        userName = findViewById(R.id.userNameAuth);
        password = findViewById(R.id.passwordAuth);

        bSignIn.setOnClickListener(v -> {
            isAllFieldsChecked = CheckAllFields();
            if (isAllFieldsChecked) {
                findUser(String.valueOf(userName));
            }
        });
    }

    private void findUser(String userName) {
        Intent intent = new Intent(AuthorizationWindow.this, SportsmanWindow.class);
        startActivity(intent);
    }

    private boolean CheckAllFields() {

        if (userName.length() == 0) {
            userName.setError("This field is required");
            return false;
        }

        if (password.length() == 0) {
            password.setError("Password is required");
            return false;
        } else if (password.length() < 8) {
            password.setError("Password must be minimum 8 characters");
            return false;
        }
        else if (!isValid(password)) {
            password.setError("Password must contain only Latin letters and digits, and should not contain \\");
            return false;
        }
        return true;
    }

    public static boolean isValid(EditText password) {
        String regex = "[a-zA-Z0-9]+";
        String passwordString = password.getText().toString();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passwordString);
        return matcher.matches() && !passwordString.contains("\\");
    }
}