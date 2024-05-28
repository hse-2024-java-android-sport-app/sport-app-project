package org.sportApp.userInterface.sportsman.ui.trainingEvents;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.training.TrainingEventDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.sportsman.ui.trainingEvents.CreatingTypeSelectionWindow;
import org.sportApp.userInterface.sportsman.ui.trainingEvents.TrainingEventsAdapter;
import org.sportApp.userInterface.sportsman.ui.trainings.OneTrainingWindow;

import java.util.ArrayList;
import java.util.List;

public class TrainingEventsWindow extends Fragment {
    private boolean isWindowOpened = false;
    private List<TrainingEventDto> trainings = new ArrayList<>();

    //Button createEvent, chooseEvent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_training_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!isWindowOpened) {
//            trainings = TestData.getTrainings();
            isWindowOpened = true;
        }

        RecyclerView currentTrainingRecyclerView = view.findViewById(R.id.trainingEventRecyclerView);
        TrainingEventsAdapter currentAdapter = new TrainingEventsAdapter(trainings, new TrainingEventsAdapter.OnItemClickListener() {
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

        ImageButton add = view.findViewById(R.id.addTrainingEventButton);
        add.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CreatingTypeSelectionWindow.class);
            startActivity(intent);
        });
//        createEvent = view.findViewById(R.id.createEvent);
//        chooseEvent = view.findViewById(R.id.choseEvent);
//        createEvent.setOnClickListener(v -> {
//            Intent intent = new Intent(requireContext(), AddTrainingWindow.class);
//            startActivity(intent);
//        });
//        chooseEvent.setOnClickListener(v -> {
//            Intent intent = new Intent(requireContext(), AddTrainingWindow.class);
//            startActivity(intent);
//        });
    }

    private void showTraining(int position) {
        if (position != RecyclerView.NO_POSITION) {
            TrainingEventDto training = trainings.get(position);
            //Log.d("training", training.getName());
            Intent intent = new Intent(requireContext(), OneTrainingWindow.class);
            intent.putExtra("trainingEventDto", training);
            startActivity(intent);
        }
    }
}
