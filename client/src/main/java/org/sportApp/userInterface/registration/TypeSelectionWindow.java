package org.sportApp.userInterface.registration;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import org.sportApp.registration.UserRegistrationDto;
import org.sportApp.registration.UserRegistrationService;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.sportsman.SportsmanWindow;

public class TypeSelectionWindow extends AppCompatActivity {

    Button bSportsman, bCoach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        Intent intent = getIntent();
        UserRegistrationDto userDto = (UserRegistrationDto) intent.getSerializableExtra("userDto");
        bSportsman = findViewById(R.id.sportsmanButton);
        bCoach = findViewById(R.id.coachButton);

        bSportsman.setOnClickListener(v -> {
            Toast.makeText(TypeSelectionWindow.this, "You are sportsman!", Toast.LENGTH_SHORT).show();
            assert userDto != null;
            userDto.setType(UserRegistrationDto.Kind.sportsman);
            Intent sportsmanIntent = new Intent(TypeSelectionWindow.this, SportsmanWindow.class);
            sportsmanIntent.putExtra("userDto", userDto);
            startActivity(sportsmanIntent);
        });


        bCoach.setOnClickListener(v -> {
            Toast.makeText(TypeSelectionWindow.this, "You are coach!", Toast.LENGTH_SHORT).show();
            assert userDto != null;
            userDto.setType(UserRegistrationDto.Kind.coach);
            registerUser(userDto);
        });
    }

    private void registerUser(UserRegistrationDto userDto) {
        UserRegistrationService registrationService = new UserRegistrationService();
        registrationService.registerUser(userDto)
                .thenAccept(resultDto -> Toast.makeText(TypeSelectionWindow.this, "User registered successfully!", Toast.LENGTH_SHORT).show())
                .exceptionally(e -> {
                    Toast.makeText(TypeSelectionWindow.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                });
    }
}