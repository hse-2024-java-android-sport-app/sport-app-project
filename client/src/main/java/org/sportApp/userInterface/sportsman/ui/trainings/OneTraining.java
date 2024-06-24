package org.sportApp.userInterface.sportsman.ui.trainings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.sportApp.dto.ExerciseDto;
import org.sportApp.dto.PlanDto;
import org.sportApp.dto.TrainingDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.ExercisesAdapter;
import org.sportApp.userInterface.sportsman.ui.exercise.ExerciseWindow;
import org.sportApp.userInterface.sportsman.ui.overview.BaseActivity;
import org.sportApp.utils.UserManager;

import java.util.List;

public class OneTraining extends BaseActivity<ExerciseDto, TrainingDto> {
    private CheckBox isCompleted;
    @Override
    protected int getLayout() {
        return R.layout.activity_one_training;
    }

    @Override
    protected List<ExerciseDto> getItems() {
        return entity.getExercises();
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
        return "trainingDto";
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
    }

    private void showExercise(int position) {
        super.showItem(position, "exerciseDto");
    }
}
