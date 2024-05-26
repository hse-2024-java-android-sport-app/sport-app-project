package org.sportApp.userInterface.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import org.sportApp.registration.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.coach.MainActivity;


public class TypeSelectionWindow extends AppCompatActivity {

    Button bSportsman, bCoach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        Intent intent = getIntent();
        UserDto userDto = (UserDto) intent.getSerializableExtra("userDto");
        bSportsman = findViewById(R.id.sportsmanButton);
        bCoach = findViewById(R.id.coachButton);

        bSportsman.setOnClickListener(v -> {
            Toast.makeText(TypeSelectionWindow.this, "You are sportsman!", Toast.LENGTH_SHORT).show();
            assert userDto != null;
            userDto.setType(UserDto.Kind.sportsman);
            registerUser(userDto);
            Intent sportsmanIntent = new Intent(TypeSelectionWindow.this, org.sportApp.userInterface.sportsman.MainActivity.class);
            sportsmanIntent.putExtra("userDto", userDto);
            startActivity(sportsmanIntent);
        });


        bCoach.setOnClickListener(v -> {
            Toast.makeText(TypeSelectionWindow.this, "You are coach!", Toast.LENGTH_SHORT).show();
            assert userDto != null;
            userDto.setType(UserDto.Kind.coach);
            registerUser(userDto);
            Intent coachIntent = new Intent(TypeSelectionWindow.this, MainActivity.class);
            coachIntent.putExtra("userDto", userDto);
            startActivity(coachIntent);
        });
    }

    private void registerUser(UserDto userDto) {
        BackendService.registerUser(userDto)
                .thenAccept(resultDto -> {
                    userDto.setId(resultDto);

                    Log.d("id", resultDto.toString());
                    Toast.makeText(TypeSelectionWindow.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                })
                .exceptionally(e -> {
                    Toast.makeText(TypeSelectionWindow.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                });
    }
}
