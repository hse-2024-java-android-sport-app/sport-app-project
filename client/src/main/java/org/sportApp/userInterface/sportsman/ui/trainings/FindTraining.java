package org.sportApp.userInterface.sportsman.ui.trainings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.dto.TrainingDto;
import org.sportApp.dto.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.FindTrainingAdapter;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.List;

public class FindTraining extends AppCompatActivity {
    private List<TrainingDto> trainings = new ArrayList<>();
    FindTrainingAdapter currentAdapter;
    private int finalPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_training);
        RecyclerView recyclerView = findViewById(R.id.trainingRecyclerView);
        UserDto currentUser;
        if (UserManager.getInstance().getType().equals(UserDto.Kind.coach)) {
            currentUser = UserManager.getLastUser();
        }
        else {
            currentUser = UserManager.getInstance();
        }
        getAllTrainings(currentUser.getId());
        currentAdapter = new FindTrainingAdapter(trainings, new BaseAdapter.OnItemClickListener<TrainingDto>() {
            @Override
            public void onItemClick(int position) {
                currentAdapter.setSelectedPosition(position);
                finalPosition = position;
            }
        });
        recyclerView.setAdapter(currentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button saveButton = findViewById(R.id.findTrainingSaveButton);
        saveButton.setOnClickListener(v -> {
            saveTraining();
            finish();
        });

    }


    private void getAllTrainings(Long userId) {
        BackendService.getAllTrainings(userId).thenAccept(resultDto -> {
                    trainings = resultDto;
                    Log.d("ApplicationTag", "FindTrainingWindow: trainings " + resultDto);
                })
                .exceptionally(e -> {
                    Log.e("ApplicationTag", "FindTrainingWindow" + e.getMessage(), e);
                    return null;
                }).join();
    }



    private void saveTraining() {
        if (finalPosition >= 0 && finalPosition < trainings.size()) {
            TrainingDto trainingDto = trainings.get(finalPosition);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("trainingDto", trainingDto);
            setResult(RESULT_OK, resultIntent);
            Toast.makeText(this, "Your training saved!", Toast.LENGTH_SHORT).show();
            Log.d("ApplicationTag", "FindTrainingWindow: user's id in training " + trainingDto.getUserId());
        } else {
            Toast.makeText(this, "No training selected", Toast.LENGTH_SHORT).show();
        }
    }
}

