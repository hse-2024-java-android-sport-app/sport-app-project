package org.sportApp.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.Toast;
import org.sportApp.*;

public class Registration extends AppCompatActivity {
    private EditText userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UserRegistrationService userRegistrationService = new UserRegistrationService();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = findViewById(R.id.idUserName);
        password = findViewById(R.id.idPassword);
        Button register = findViewById(R.id.idRegister);
        register.setOnClickListener(v -> {
            String userNameCur = userName.getText().toString();
            String passwordCur = password.getText().toString();
            if (TextUtils.isEmpty(userNameCur) || TextUtils.isEmpty(passwordCur)) {
                Toast.makeText(Registration.this, "Please enter user name and password", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Registration.this, "Registration successful", Toast.LENGTH_SHORT).show();
            }
        });
    }
}