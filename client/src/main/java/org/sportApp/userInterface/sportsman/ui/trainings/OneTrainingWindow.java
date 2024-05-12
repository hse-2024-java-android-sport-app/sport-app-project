package org.sportApp.userInterface.sportsman.ui.trainings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.testData.TestData;
import org.sportApp.training.ExerciseDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.sportsman.ui.exercise.ExerciseWindow;

import java.util.ArrayList;
import java.util.List;

public class OneTrainingWindow extends AppCompatActivity {
    private boolean isWindowOpened = false;
    private List<ExerciseDto> exercises = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_training);

        if (!isWindowOpened) {
            exercises = TestData.getExercises();
            isWindowOpened = true;
        }

        RecyclerView currentTrainingRecyclerView = findViewById(R.id.availableTrainingsRecyclerView);
        ExercisesAdapter currentAdapter = new ExercisesAdapter(exercises, new ExercisesAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(int position) {
            }

            @Override
            public void onItemClick(int position) {
                showExercise(position);
            }
        });
        currentTrainingRecyclerView.setAdapter(currentAdapter);
        currentTrainingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showExercise(int position) {
        if (position != RecyclerView.NO_POSITION) {
            ExerciseDto exercise = exercises.get(position);
            Log.d("exercise", exercise.getDescription());
            Intent intent = new Intent(this, ExerciseWindow.class);
            intent.putExtra("exerciseDto", exercise);
            startActivity(intent);
        }
    }
}
