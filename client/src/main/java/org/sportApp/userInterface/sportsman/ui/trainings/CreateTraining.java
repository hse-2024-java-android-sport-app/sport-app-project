package org.sportApp.userInterface.sportsman.ui.trainings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.sportApp.dto.ExerciseDto;
import org.sportApp.dto.TrainingDto;
import org.sportApp.dto.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.ExercisesAdapter;
import org.sportApp.userInterface.sportsman.ui.exercise.AddExerciseWindow;
import org.sportApp.userInterface.sportsman.ui.exercise.ExerciseWindow;
import org.sportApp.userInterface.sportsman.ui.overview.BaseCreateActivity;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CreateTraining extends BaseCreateActivity<ExerciseDto, TrainingDto> {

    private final List<ExerciseDto> exercises = new ArrayList<>();
    private ExercisesAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_add_training;
    }

    @Override
    protected List<ExerciseDto> getItems() {
        return exercises;
    }

    @Override
    protected int getRecyclerView() {
        return R.id.addTrainingsRecyclerView;
    }

    @Override
    protected Class<?> getShowWindowClass() {
        return ExerciseWindow.class;
    }

    @Override
    protected String getName() {
        return "trainingDto";
    }

    @Override
    protected void openAddWindow() {
        Intent intent = new Intent(this, AddExerciseWindow.class);
        addExerciseLauncher.launch(intent);
    }

    @Override
    protected void save() {
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setExercises(exercises);
        UserDto currentUser;
        if (UserManager.getInstance().getType().equals(UserDto.Kind.coach)) {
            currentUser = UserManager.getLastUser();
        }
        else {
            currentUser = UserManager.getInstance();
        }
        trainingDto.setUserId(currentUser.getId());
        createTraining(trainingDto);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("trainingDto", trainingDto);
        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, "Your training saved!", Toast.LENGTH_SHORT).show();
        Log.d("myTag", "user's id in training " + trainingDto.getUserId());
        finish();
    }

    @Override
    protected int getAddButton() {
        return R.id.addTrainingButton;
    }

    @Override
    protected int getSaveButton() {
        return R.id.saveTrainingButton;
    }

    @Override
    protected int getNameEditText() {
        return R.id.trainingNameEditText;
    }

    @Override
    protected BaseAdapter<ExerciseDto, ? extends BaseAdapter.BaseViewHolder<ExerciseDto>> getAdapter() {
        return adapter;
    }

    @Override
    protected BaseAdapter<ExerciseDto, ? extends BaseAdapter.BaseViewHolder<ExerciseDto>> createAdapter() {
        adapter = new ExercisesAdapter(exercises, new BaseAdapter.OnItemClickListener<ExerciseDto>() {
            @Override
            public void onItemClick(int position) {
                showItem(position, "exerciseDto");
            }
        });
        return adapter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private final ActivityResultLauncher<Intent> addExerciseLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            assert result.getData() != null;
            ExerciseDto exerciseDto = (ExerciseDto) result.getData().getSerializableExtra("exerciseDto");
            assert exerciseDto != null;
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
                Log.d("myTag", String.valueOf(exercises.size()));
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void createTraining(TrainingDto trainingDto) {
        Log.d("myTag", "password: " + org.sportApp.utils.UserManager.getInstance().getPassword());
        BackendService.createTraining(trainingDto)
                .thenAccept(resultDto -> {
                    trainingDto.setTrainId(resultDto);
                    Log.d("myTag", "training's id: " + resultDto);
                })
                .exceptionally(e -> {
                    Log.e("myTag", "Failed to create training or exercises.", e);
                    return null;
                }).join();
    }

}
