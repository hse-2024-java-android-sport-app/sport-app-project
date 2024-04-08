package org.sportApp.userInterface.sportsman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import org.sportApp.userInterface.R;
import org.sportApp.training.TrainingEventDto;

import java.util.ArrayList;
import java.util.List;

public class SportsmanWindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportsman_window);

        List<TrainingEventDto> trainingEvents = new ArrayList<>();

        PlanAdapter planAdapter = new PlanAdapter(trainingEvents);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(planAdapter);
    }
}
