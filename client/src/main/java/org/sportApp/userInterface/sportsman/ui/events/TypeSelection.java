package org.sportApp.userInterface.sportsman.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.sportApp.dto.TrainingDto;
import org.sportApp.dto.TrainingEventDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.sportsman.ui.trainings.CreateTraining;
import org.sportApp.userInterface.sportsman.ui.trainings.FindTraining;

import java.util.Calendar;
import java.util.Date;

public class TypeSelection extends AppCompatActivity {
    private TrainingEventDto eventDto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        eventDto = new TrainingEventDto();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Button createEvent = findViewById(R.id.createEvent);
        Button chooseEvent = findViewById(R.id.choseEvent);

        DatePicker datePicker = findViewById(R.id.datePicker);

        eventDto.setDate(getSelectedDate(datePicker));

        createEvent.setOnClickListener(v -> {
            Log.d("ApplicationTag", "TypeSelectionWindow: create Event");
            openAddTrainingWindow(0);
        });
        chooseEvent.setOnClickListener(v -> {
            Log.d("ApplicationTag", "TypeSelectionWindow: choose Event");
            openAddTrainingWindow(1);
        });

        Button saveChanges = findViewById(R.id.saveChanges);
        saveChanges.setOnClickListener(v -> {
            if (eventDto.getTraining() != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("eventDto", eventDto);
                //Log.d("ApplicationTag", "TypeSelectionWindow: eventDto Date " + eventDto.getDate().toString());
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Log.d("ApplicationTag", "TypeSelectionWindow: trainingDto is null");
                Toast.makeText(this, "Please select a training", Toast.LENGTH_SHORT).show();
            }
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

    private void openAddTrainingWindow(int type) {
        Intent intent = (type == 0) ? new Intent(this, CreateTraining.class) : new Intent(this, FindTraining.class);
        addEventLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> addEventLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Log.d("ApplicationTag", "TypeSelectionWindow: Added training event");
            assert result.getData() != null;
            TrainingDto trainingDto = (TrainingDto) result.getData().getSerializableExtra("trainingDto");
            assert trainingDto != null;
            Log.d("ApplicationTag", "TypeSelectionWindow: TrainingDto: " + trainingDto);
            eventDto.setTraining(trainingDto);
        }
    });
}
