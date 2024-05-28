package org.sportApp.userInterface.sportsman.ui.trainingEvents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;

import org.sportApp.training.TrainingEventDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.sportsman.ui.trainings.AddTrainingWindow;
import org.sportApp.userInterface.sportsman.ui.trainings.FindTraining;

import java.util.Calendar;
import java.util.Date;

public class CreatingTypeSelectionWindow extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TrainingEventDto trainingEventDto = new TrainingEventDto();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Button createEvent = findViewById(R.id.createEvent);
        Button chooseEvent = findViewById(R.id.choseEvent);

        DatePicker datePicker = findViewById(R.id.datePicker);

        trainingEventDto.setDate(getSelectedDate(datePicker));

        createEvent.setOnClickListener(v -> {
            Intent intent = new Intent(CreatingTypeSelectionWindow.this, AddTrainingWindow.class);
            startActivity(intent);
        });
        chooseEvent.setOnClickListener(v -> {
            Intent intent = new Intent(CreatingTypeSelectionWindow.this, FindTraining.class);
            startActivity(intent);
        });
    }
    @NonNull
    private Date getSelectedDate(@NonNull DatePicker datePicker) {
        int year = datePicker.getYear();
        int month = datePicker.getMonth() + 1;
        int day = datePicker.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }
}
