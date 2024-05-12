package org.sportApp.userInterface.sportsman.ui.trainings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.testData.TestData;
import org.sportApp.training.TrainingDto;
import org.sportApp.userInterface.R;

import java.util.ArrayList;
import java.util.List;

public class TrainingsWindow extends Fragment {
    private boolean isWindowOpened = false;
    private List<TrainingDto> trainings = new ArrayList<>();

    ImageButton add;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!isWindowOpened) {
            trainings = TestData.getTrainings();
            isWindowOpened = true;
        }

        assert getArguments() != null;
        RecyclerView currentTrainingRecyclerView = view.findViewById(R.id.trainingRecyclerView);
        TrainingsAdapter currentAdapter = new TrainingsAdapter(trainings, new TrainingsAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(int position) {
            }

            @Override
            public void onItemClick(int position) {
                showTraining(position);
            }
        });
        currentTrainingRecyclerView.setAdapter(currentAdapter);
        currentTrainingRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        add = view.findViewById(R.id.addTrainingButton);
        add.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddTrainingWindow.class);
            startActivity(intent);
        });
    }

    private void showTraining(int position) {
        if (position != RecyclerView.NO_POSITION) {
            TrainingDto training = trainings.get(position);
            Log.d("training", training.getName());
            Intent intent = new Intent(requireContext(), OneTrainingWindow.class);
            intent.putExtra("trainingDto", training);
            startActivity(intent);
        }
    }
}