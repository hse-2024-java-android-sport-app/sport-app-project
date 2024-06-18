package org.sportApp.userInterface.sportsman.ui.exercise;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.sportApp.dto.ExerciseDto;
import org.sportApp.userInterface.R;

public class ExerciseWindow extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
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
                descriptionText.setText("Description: " + exerciseDto.getDescription());
                repetitionsText.setText("Number of repetitions: " + exerciseDto.getRepetitions());
                durationText.setText("Duration: " + exerciseDto.getDuration());
                setsText.setText("Number of sets: " + exerciseDto.getSets());
                if (!exerciseDto.getVideoUrl().isEmpty()) {
                    videoUrlText.setText(exerciseDto.getVideoUrl());
                    videoUrlText.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    videoUrlText.setVisibility(View.GONE);
                }
            }
        }
    }
}