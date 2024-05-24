package org.sportApp.userInterface.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.sportApp.registration.UserRegistrationDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationWindow extends AppCompatActivity {
    Button bCancel, bRegister;
    EditText firstName, lastName, userName, password;
    boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

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

        Intent intent = new Intent(RegistrationWindow.this, BirthDateWindow.class);
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

        CompletableFuture<Boolean> future = BackendService.isLoginExist(userName.getText().toString());
        boolean exist;
        try {
            exist = future.get();
            if (exist) {
                Toast.makeText(RegistrationWindow.this, "This username is already taken.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ExecutionException | InterruptedException e) {
            Log.d("myTag", Objects.requireNonNull(e.getMessage()));
            Toast.makeText(RegistrationWindow.this, "Failed to check user existence: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void onAlreadyHaveAccountClicked(View view) {
        Intent intent = new Intent(this, AuthorizationWindow.class);
        startActivity(intent);
    }
}