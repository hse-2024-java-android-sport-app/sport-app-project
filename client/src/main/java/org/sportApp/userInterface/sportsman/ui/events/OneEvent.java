package org.sportApp.userInterface.sportsman.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.Nullable;

import org.sportApp.dto.ExerciseDto;
import org.sportApp.dto.TrainingEventDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.ExercisesAdapter;
import org.sportApp.userInterface.sportsman.ui.exercise.ExerciseWindow;
import org.sportApp.userInterface.sportsman.ui.overview.BaseActivity;

import java.util.List;

public class OneEvent extends BaseActivity<ExerciseDto, TrainingEventDto> {
    @Override
    protected int getLayout() {
        return R.layout.activity_one_event;
    }

    @Override
    protected List<ExerciseDto> getItems() {
        return entity.getTrainingDto().getExercises();
    }

    @Override
    protected int getRecyclerView() {
        return R.id.trainingsRecyclerView;
    }

    @Override
    protected Class<?> getShowWindowClass() {
        return ExerciseWindow.class;
    }

    @Override
    protected String getName() {
        return "eventDto";
    }

    @Override
    protected BaseAdapter<ExerciseDto, ? extends BaseAdapter.BaseViewHolder<ExerciseDto>> createAdapter() {
        return new ExercisesAdapter(items, new BaseAdapter.OnItemClickListener<ExerciseDto>() {

            @Override
            public void onItemClick(int position) {
                showExercise(position);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("trainingDto", entity.toString());

        CheckBox isCompleted = findViewById(R.id.isCompleted);
        isCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            entity.setCompleted(isChecked);
            markEventCompleted();
        });

        Button saveChangesButton = findViewById(R.id.buttonSaveChanges);
        saveChangesButton.setOnClickListener(v -> saveChanges());
    }

    private void saveChanges() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("eventDto", entity);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
    private void showExercise(int position) {
        super.showItem(position, "exerciseDto");
    }

    private void markEventCompleted() {
        BackendService.markEventCompleted(entity.getEventId()).thenAccept(resultDto -> {
        }).exceptionally(e -> null);
    }
}
