package org.sportApp.userInterface.sportsman.ui.plans;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.dto.PlanDto;
import org.sportApp.dto.TrainingEventDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.sportsman.ui.exercise.ExerciseWindow;
import org.sportApp.userInterface.adapters.TrainingEventsAdapter;

import java.util.ArrayList;
import java.util.List;

public class OnePlanWindow extends AppCompatActivity {
    private boolean isWindowOpened = false;
    private List<TrainingEventDto> trainings = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_plan);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("planDto")) {
            PlanDto planDto = (PlanDto) intent.getSerializableExtra("planDto");
            assert planDto != null;
            trainings = planDto.getTrainings();
            Log.d("myTag", "in one plan window exercises' size is " + trainings.size());
        }

        if (!isWindowOpened) {
            //getAllPlans(UserManager.getInstance().getUserId());
            isWindowOpened = true;
        }

        RecyclerView currentTrainingRecyclerView = findViewById(R.id.availableTrainingsRecyclerView);
        TrainingEventsAdapter currentAdapter = new TrainingEventsAdapter(trainings, new TrainingEventsAdapter.OnItemClickListener<TrainingEventDto>() {
            @Override
            public void onItemClick(int position) {
                showTrainings(position);
            }
        });
        currentTrainingRecyclerView.setAdapter(currentAdapter);
        currentTrainingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

//    private final ActivityResultLauncher<Intent> oneTrainingLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//        if (result.getResultCode() == RESULT_OK) {
//            assert result.getData() != null;
//            ExerciseDto exerciseDto = (ExerciseDto) result.getData().getSerializableExtra("exerciseDto");
//            assert exerciseDto != null;
//            Log.d("exerciseDescription", exerciseDto.getDescription());
//        }
//    });


    private void showTrainings(int position) {
        if (position != RecyclerView.NO_POSITION) {
            TrainingEventDto event = trainings.get(position);
            Log.d("training", String.valueOf(event.getEventId()));
            Intent intent = new Intent(this, ExerciseWindow.class);
            intent.putExtra("trainingDto", event);
            startActivity(intent);
        }
    }


}
