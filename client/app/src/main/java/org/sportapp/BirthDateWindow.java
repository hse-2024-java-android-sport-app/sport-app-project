package org.sportapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

public class BirthDateWindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthdate);

        DatePicker datePicker = findViewById(R.id.datePicker);
        Button continueButton = findViewById(R.id.continueButton);

        continueButton.setOnClickListener(v -> {
            Intent intent = getIntent();
            UserRegistrationDto userDto = (UserRegistrationDto) intent.getSerializableExtra("userDto");
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date selectedDate = calendar.getTime();
            assert userDto != null;
            userDto.setDate(selectedDate);
            Intent nextIntent = new Intent(BirthDateWindow.this, TypeSelectionWindow.class);
            nextIntent.putExtra("userDto", userDto);
            startActivity(nextIntent);
        });
    }
}