package org.sportapp.registration;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationWindow extends AppCompatActivity {
    Button bCancel, bRegister;
    EditText firstName, lastName, userName, password;
    boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bRegister = findViewById(R.id.registrationButton);
        bCancel = findViewById(R.id.cancelButton);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);

        bRegister.setOnClickListener(v -> {
            isAllFieldsChecked = CheckAllFields();
            if (isAllFieldsChecked) {
                UserRegistrationDto userDto = new UserRegistrationDto();
                registerUser(userDto);
            }
        });

        bCancel.setOnClickListener(v -> {
            RegistrationWindow.this.finish();
            System.exit(0);
        });
    }

    private void registerUser(UserRegistrationDto userDto) {
        String fName = firstName.getText().toString();
        String sName = lastName.getText().toString();
        String uName = userName.getText().toString();
        String pass = password.getText().toString();

        userDto.setFirstName(fName);
        userDto.setSecondName(sName);
        userDto.setLogin(uName);
        userDto.setPassword(pass);

        Intent intent = new Intent(RegistrationWindow.this, TypeSelectionWindow.class);
        intent.putExtra("userDto", userDto);
        startActivity(intent);
    }

    private boolean CheckAllFields() {
        if (firstName.length() == 0) {
            firstName.setError("This field is required");
            return false;
        }

        if (lastName.length() == 0) {
            lastName.setError("This field is required");
            return false;
        }

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
        return true;
    }
}
