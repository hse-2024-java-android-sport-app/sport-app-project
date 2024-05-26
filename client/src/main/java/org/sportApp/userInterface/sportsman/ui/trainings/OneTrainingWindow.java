package org.sportApp.userInterface.sportsman.ui.trainings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.training.ExerciseDto;
import org.sportApp.training.TrainingDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.sportsman.ui.exercise.ExerciseWindow;
import org.sportApp.userInterface.adapters.ExercisesAdapter;

import java.util.ArrayList;
import java.util.List;

public class OneTrainingWindow extends AppCompatActivity {
    private boolean isWindowOpened = false;
    private List<ExerciseDto> exercises = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_training);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("trainingDto")) {
            TrainingDto trainingDto = (TrainingDto) intent.getSerializableExtra("trainingDto");
            assert trainingDto != null;
            exercises = trainingDto.getExercises();
            Log.d("myTag", "in one training window exercises' size is " + exercises.size());
        }

        if (!isWindowOpened) {
            //exercises = TestData.getExercises();
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

    private final ActivityResultLauncher<Intent> oneExerciseLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            assert result.getData() != null;
            ExerciseDto exerciseDto = (ExerciseDto) result.getData().getSerializableExtra("exerciseDto");
            assert exerciseDto != null;
            Log.d("exerciseDescription", exerciseDto.getDescription());
        }
    });


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
