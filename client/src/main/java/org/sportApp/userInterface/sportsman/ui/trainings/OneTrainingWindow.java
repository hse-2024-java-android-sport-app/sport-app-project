package org.sportApp.userInterface.sportsman.ui.trainings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.testData.OneTraining;
import org.sportApp.training.ExerciseDto;
import org.sportApp.training.PlanDto;
import org.sportApp.userInterface.R;

import java.util.ArrayList;
import java.util.List;

public class OneTrainingWindow extends Fragment {
    private boolean isWindowOpened = false;
    private List<ExerciseDto> exercises = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_one_training, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!isWindowOpened) {
            exercises = OneTraining.getTraining();
            isWindowOpened = true;
        }

        RecyclerView currentTrainingRecyclerView = view.findViewById(R.id.availableTrainingsRecyclerView);
        TrainingAdapter currentAdapter = new TrainingAdapter(exercises, new TrainingAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(int position) {
            }

            @Override
            public void onItemClick(int position) {
            }
        });
        currentTrainingRecyclerView.setAdapter(currentAdapter);
        currentTrainingRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private final ActivityResultLauncher<Intent> addPlanLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
            assert result.getData() != null;
            PlanDto planDto = result.getData().getParcelableExtra("planDto");
        }
    });
}
