package org.sportApp.userInterface.sportsman;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.sportApp.userInterface.R;
import org.sportApp.training.TrainingEventDto;

import java.util.ArrayList;
import java.util.List;

public class SportsmanWindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportsman_window);

        Button addTrainingButton = findViewById(R.id.addTrainingButton);
        addTrainingButton.setOnClickListener(view -> {
            Intent intent = new Intent(SportsmanWindow.this, AddTrainingWindow.class);
            startActivity(intent);
        });

        List<TrainingEventDto> trainingEvents = fakeTrainingEvents();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PlanAdapter planViewer = new PlanAdapter(trainingEvents);
        recyclerView.setAdapter(planViewer);
    }

    private List<TrainingEventDto> fakeTrainingEvents() {
        List<TrainingEventDto> trainingEvents = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            TrainingEventDto event = new TrainingEventDto();
            event.setId(i);
            trainingEvents.add(event);
        }
        return trainingEvents;
    }
}
