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

    private List<TrainingEventDto> trainingEvents;
    private PlanAdapter planAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportsman_window);

        Button addTrainingButton = findViewById(R.id.addTrainingButton);
        addTrainingButton.setOnClickListener(view -> {
            Intent intent = new Intent(SportsmanWindow.this, AddTrainingWindow.class);
            startActivity(intent);
        });

        Button addPlanButton = findViewById(R.id.addPlanButton);
        addPlanButton.setOnClickListener(view -> addPlan(new TrainingEventDto()));

        trainingEvents = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        planAdapter = new PlanAdapter(trainingEvents);
        recyclerView.setAdapter(planAdapter);
    }

    public void addPlan(TrainingEventDto event) {
        trainingEvents.add(event);
        int pos = trainingEvents.size();
        event.setId(pos);
        planAdapter.notifyItemInserted(pos);
    }
}
