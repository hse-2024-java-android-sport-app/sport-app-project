package org.sportApp.userInterface.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.sportApp.registration.UserDto;
import org.sportApp.userInterface.R;

import java.util.Calendar;

public class BirthDateWindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthdate);

        DatePicker datePicker = findViewById(R.id.datePicker);
        Button continueButton = findViewById(R.id.continueButton);

        continueButton.setOnClickListener(v -> {
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();

            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(year, month, day);

            Calendar currentCalendar = Calendar.getInstance();

            if (selectedCalendar.getTimeInMillis() <= currentCalendar.getTimeInMillis()) {
                Intent intent = getIntent();
                UserDto userDto = (UserDto) intent.getSerializableExtra("userDto");
                assert userDto != null;
                //userDto.setDateOfBirth(selectedCalendar.getTime());
                Intent nextIntent = new Intent(BirthDateWindow.this, TypeSelectionWindow.class);
                nextIntent.putExtra("userDto", userDto);
                startActivity(nextIntent);
            } else {
                Toast.makeText(BirthDateWindow.this, "Please select a valid date", Toast.LENGTH_SHORT).show();
            }
        });

    }
}