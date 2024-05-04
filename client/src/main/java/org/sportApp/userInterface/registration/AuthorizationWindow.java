package org.sportApp.userInterface.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.sportApp.registration.UserRegistrationDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.sportsman.SportsmanWindow;
import org.sportApp.utils.SessionManager;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
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
                UserRegistrationDto userDto = new UserRegistrationDto();
                if (findUser(userDto)) {
                    Intent intent = new Intent(AuthorizationWindow.this, SportsmanWindow.class);
                    intent.putExtra("userDto", userDto);
                    startActivity(intent);
                } else {
                    userName.setText("");
                    password.setText("");
                    Toast.makeText(AuthorizationWindow.this, "User doesn't exist.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean findUser(@NonNull UserRegistrationDto userDto) {
        String uName = userName.getText().toString();
        String pass = password.getText().toString();
        userDto.setLogin(uName);
        userDto.setPassword(pass);
        CompletableFuture<Boolean> future = BackendService.isLoginExist(uName);
        try {
            boolean exist = future.get();
            if (exist) {
                BackendService.signInUser(userDto).thenAccept(resultDto -> {
                            SessionManager sessionManager = new SessionManager(getApplicationContext());
                            sessionManager.saveUserId(resultDto);
                            Toast.makeText(AuthorizationWindow.this, "User authorized successfully!", Toast.LENGTH_SHORT).show();
                        })
                        .exceptionally(e -> {
                            Toast.makeText(AuthorizationWindow.this, "Authorization failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            return null;
                        });

                return true;
            }
            else {
                return false;
            }

        } catch (Exception e) {
            Log.d("myTag", Objects.requireNonNull(e.getMessage()));
            Toast.makeText(AuthorizationWindow.this, "Failed to check user existence: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
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
        } else if (!isValid(password)) {
            password.setError("Password must contain only Latin letters and digits, and should not contain \\");
            return false;
        }
        return true;
    }

    public static boolean isValid(@NonNull EditText password) {
        String regex = "[a-zA-Z0-9]+";
        String passwordString = password.getText().toString();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passwordString);
        return matcher.matches() && !passwordString.contains("\\");
    }
}