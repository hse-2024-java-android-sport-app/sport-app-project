package org.sportApp.userInterface.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.sportApp.model.User;
import org.sportApp.registration.UserRegistrationDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
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
            String uName = userName.getText().toString();
            String pass = password.getText().toString();
            UserRegistrationDto userDto = new UserRegistrationDto();
            userDto.setLogin(uName);
            userDto.setPassword(pass);
            if (isAllFieldsChecked) {
                userDto.setType(UserRegistrationDto.Kind.sportsman);
                signInUser(userDto)
                        .thenAccept(resultDto -> getType(userDto.getId(), userDto))
                        .exceptionally(e -> {
                            Toast.makeText(AuthorizationWindow.this, "Authorization failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            return null;
                        });
                Log.d("Main thread ", userDto.getType().toString());
                if (userDto.getType().equals(UserRegistrationDto.Kind.sportsman)) {
                    Intent sportsmanIntent = new Intent(AuthorizationWindow.this, org.sportApp.userInterface.sportsman.MainActivity.class);
                    sportsmanIntent.putExtra("userDto", userDto);
                    startActivity(sportsmanIntent);
                }
                else {
                    Intent coachIntent = new Intent(AuthorizationWindow.this, org.sportApp.userInterface.coach.MainActivity.class);
                    coachIntent.putExtra("userDto", userDto);
                    startActivity(coachIntent);
                }
            }
        });
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

    private CompletableFuture<Void> signInUser(UserRegistrationDto userDto) {
        return BackendService.signInUser(userDto)
                .thenAccept(resultDto -> {
                    userDto.setId(resultDto);
                    Log.d("Authorization", "resultDto: " + resultDto);
                })
                .exceptionally(e -> {
                    //Toast.makeText(AuthorizationWindow.this, "Authorization failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

    private void getType(Long id, UserRegistrationDto userDto){
        BackendService.getType(id).thenAccept(resultDto -> {
                    userDto.setType(resultDto);
                    Log.d("UserType", "resultDto: " + resultDto);
                })
                .exceptionally(e -> {
                    Toast.makeText(AuthorizationWindow.this, "Authorization failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                }).join();
    }
}