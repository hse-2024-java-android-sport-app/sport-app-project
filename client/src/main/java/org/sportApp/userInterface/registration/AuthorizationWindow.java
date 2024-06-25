package org.sportApp.userInterface.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.sportApp.dto.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;
import org.sportApp.utils.UserManager;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

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
            UserDto userDto = new UserDto();
            userDto.setLogin(uName);
            userDto.setPassword(pass);
            if (isAllFieldsChecked) {
                userDto.setType(UserDto.Kind.sportsman);
                signInUser(userDto)
                        .thenAccept(resultDto -> getUser(userDto.getId(), userDto))
                        .exceptionally(e -> {
                            Log.e("ApplicationTag", "Authorization: " + Objects.requireNonNull(e.getMessage()));
                            return null;
                        }).join();
                Log.d("ApplicationTag", "User type: " + userDto.getType());
                if (userDto.getType().equals(UserDto.Kind.sportsman)) {
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
        }
        return true;
    }

    private CompletableFuture<Void> signInUser(UserDto userDto) {
        return BackendService.signInUser(userDto)
                .thenAccept(resultDto -> {
                    userDto.setId(resultDto);
                    UserManager.getInstance().setId(resultDto);
                    Log.d("ApplicationTag", "Authorization: resultDto is " + resultDto);
                })
                .exceptionally(e -> {
                    Log.e("ApplicationTag", "Authorization: " + Objects.requireNonNull(e.getMessage()));
                    return null;
                });
    }

    private void getUser(Long id, UserDto userDto){
        BackendService.getUser(id).thenAccept(resultDto -> {
                    userDto.setInfo(resultDto);
                    UserManager.getInstance().setInfo(resultDto);
                    Log.d("ApplicationTag", "Authorization " + UserManager.getInstance().getType());
                    Log.d("ApplicationTag", "Authorization: resultDto is " + resultDto);
                })
                .exceptionally(e -> {
                    Log.e("ApplicationTag", "Authorization: " + Objects.requireNonNull(e.getMessage()));
                    return null;
                }).join();
    }
}