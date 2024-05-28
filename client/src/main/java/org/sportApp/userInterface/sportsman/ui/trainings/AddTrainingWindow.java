package org.sportApp.userInterface.sportsman.ui.trainings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.requests.BackendService;
import org.sportApp.training.ExerciseDto;
import org.sportApp.training.TrainingDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.sportsman.ui.exercise.AddExerciseWindow;
import org.sportApp.userInterface.sportsman.ui.exercise.ExerciseWindow;
import org.sportApp.userInterface.adapters.ExercisesAdapter;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
//
public class AddTrainingWindow extends AppCompatActivity {

    private final List<ExerciseDto> exercises = new ArrayList<>();
    private ExercisesAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_training);

        RecyclerView currentTrainingRecyclerView = findViewById(R.id.addTrainingsRecyclerView);
        adapter = new ExercisesAdapter(exercises, new BaseAdapter.OnItemClickListener<ExerciseDto>() {
            @Override
            public void onItemLongClick(int position, ExerciseDto item) {
                showExercise(position);
            }

            @Override
            public void onItemClick(int position, ExerciseDto item) {
                showExercise(position);
            }
        });
        currentTrainingRecyclerView.setAdapter(adapter);
        currentTrainingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton addButton = findViewById(R.id.addTrainingButton);
        addButton.setOnClickListener(v -> openAddExerciseWindow());

        Button saveTrainingButton = findViewById(R.id.saveTrainingButton);
        saveTrainingButton.setOnClickListener(v -> saveTrainingEvent());

        EditText trainingNameEditText = findViewById(R.id.trainingNameEditText);

        trainingNameEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String trainingName = textView.getText().toString().trim();
                if (!trainingName.isEmpty()) {
                    textView.setEnabled(false);
                    saveTrainingButton.requestFocus();
                    return true;
                }
            }
            return false;
        });
    }

    private void openAddExerciseWindow() {
        Intent intent = new Intent(this, AddExerciseWindow.class);
        addExerciseLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> addExerciseLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            assert result.getData() != null;
            ExerciseDto exerciseDto = (ExerciseDto) result.getData().getSerializableExtra("exerciseDto");
            assert exerciseDto != null;
            Log.d("exerciseDescription", exerciseDto.getDescription());
        }
    });

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            ExerciseDto exerciseDto = (ExerciseDto) data.getSerializableExtra("exerciseDto");
            if (exerciseDto != null) {
                exercises.add(exerciseDto);
                adapter.notifyDataSetChanged();
            }
        }
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

    private void saveTrainingEvent() {
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setExercises(exercises);
        trainingDto.setUserId(UserManager.getInstance().getUserId());
        createTraining(trainingDto);
        Toast.makeText(this, "Your training saved!", Toast.LENGTH_SHORT).show();
        Log.d("myTag", "user's id in training " + trainingDto.getUserId());
    }

    private void createTraining(TrainingDto trainingDto) {
        BackendService.createTraining(trainingDto)
                .thenCompose(resultDto -> {
                    trainingDto.setTrainId(resultDto);
                    Log.d("myTag", "training's id: " + resultDto);

                    List<CompletableFuture<Long>> futures = new ArrayList<>();
                    for (ExerciseDto exercise : trainingDto.getExercises()) {
                        CompletableFuture<Long> future = addExerciseByTrain(exercise, resultDto);
                        futures.add(future);
                    }
                    return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
                })
                .thenAccept(voidResult -> {
                    Log.d("myTag", "All exercises added successfully.");
                })
                .exceptionally(e -> {
                    Log.e("myTag", "Failed to create training or exercises.", e);
                    return null;
                }).join();
    }

    private CompletableFuture<Long> addExerciseByTrain(ExerciseDto exerciseDto, Long trainId) {
        return BackendService.addExerciseByTrain(exerciseDto, trainId)
                .thenApply(resultDto -> {
                    Log.d("myTag", "exercise's id: " + resultDto);
                    exerciseDto.setId(resultDto);
                    return resultDto;
                }).exceptionally(e -> {
                    //Toast.makeText(AuthorizationWindow.this, "Authorization failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

}
