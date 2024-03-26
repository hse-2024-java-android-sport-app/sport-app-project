package org.sportapp.registration;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

public class TypeSelectionWindow extends AppCompatActivity {

    ImageButton bSportsman, bCoach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        UserRegistrationDto userDto = (UserRegistrationDto) intent.getSerializableExtra("userDto");
        bSportsman = findViewById(R.id.sportsmanButton);
        bCoach = findViewById(R.id.coachButton);

        bSportsman.setOnClickListener(v -> {
            Toast.makeText(TypeSelectionWindow.this, "You are sportsman!", Toast.LENGTH_SHORT).show();
            assert userDto != null;
            userDto.setType(UserRegistrationDto.Kind.sportsman);
            registerUser(userDto);
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