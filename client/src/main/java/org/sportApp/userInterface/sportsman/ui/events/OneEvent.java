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
import java.util.concurrent.ExecutionException;

public class OneEvent extends BaseActivity<ExerciseDto, TrainingEventDto> {
    @Override
    protected int getLayout() {
        return R.layout.activity_one_event;
    }

    @Override
    protected List<ExerciseDto> getItems() {
        Log.d("ApplicationTag", "OneEvent window: trainingDto is " + entity.getTraining());
        if (entity != null && entity.getTraining() != null) {
            Log.d("ApplicationTag", "OneEventWindow: exercises " + entity.getTraining().getExercises());
            return entity.getTraining().getExercises();
        } else {
            return null;
        }
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
        Log.d("ApplicationTag", "OneEvent: entity is " + entity.toString());
        Log.d("ApplicationTag", "OneEvent: entity is Completed" + entity.isCompleted());
        CheckBox isCompleted = findViewById(R.id.isCompleted);
        Boolean result = getEventIsCompleted();
        if (result != null) {
            isCompleted.setChecked(result);
        } else {
            Log.d("ApplicationTag", "OneEvent: isCompleted is null");
        }
//        isCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
        //entity.setCompleted(isChecked);
//        });

        Button saveChangesButton = findViewById(R.id.buttonSaveChanges);
        saveChangesButton.setOnClickListener(v -> saveChanges(isCompleted.isChecked()));
    }

    private void saveChanges(Boolean isChecked) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("eventDto", entity);
        markEventCompleted(isChecked);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
    private void showExercise(int position) {
        super.showItem(position, "exerciseDto");
    }

    private void markEventCompleted(Boolean isChecked) {
        BackendService.markEventCompleted(entity.getEventId(), isChecked).thenAccept(resultDto -> {
        }).exceptionally(e -> null).join();
    }

    private Boolean getEventIsCompleted() {
        try {
            return BackendService.getEventIsCompleted(entity.getEventId()).get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("ApplicationTag", "OneEventClass " + e.getMessage(), e);
        }
        return null;
    }
}
