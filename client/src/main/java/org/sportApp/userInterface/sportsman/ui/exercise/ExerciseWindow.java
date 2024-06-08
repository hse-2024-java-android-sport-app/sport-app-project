package org.sportApp.userInterface.sportsman.ui.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.sportApp.dto.ExerciseDto;
import org.sportApp.userInterface.R;

public class ExerciseWindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_exercise);
        TextView descriptionText = findViewById(R.id.descriptionText);
        TextView repetitionsText = findViewById(R.id.repetitionsText);
        TextView durationText = findViewById(R.id.durationText);
        TextView setsText = findViewById(R.id.setsText);
        TextView videoUrlText = findViewById(R.id.videoUrlText);

        Intent intent = getIntent();
        assert intent.hasExtra("exerciseDto");
        if (intent.hasExtra("exerciseDto")) {
            ExerciseDto exerciseDto = (ExerciseDto) intent.getSerializableExtra("exerciseDto");
            if (exerciseDto != null) {
                descriptionText.setText(exerciseDto.getDescription());
                repetitionsText.setText(String.valueOf(exerciseDto.getRepetitions()));
                durationText.setText(String.valueOf(exerciseDto.getDuration()));
                setsText.setText(String.valueOf(exerciseDto.getSets()));
                videoUrlText.setText(exerciseDto.getVideoUrl());
            }
        }
    }
}