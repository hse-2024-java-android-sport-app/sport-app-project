package org.sportApp.userInterface.sportsman;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.requests.BackendService;
import org.sportApp.training.PlanDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.sportsman.ui.plans.EditPlanWindow;
import org.sportApp.userInterface.sportsman.ui.plans.PlanAdapter;
import org.sportApp.userInterface.trainings.AddTrainingWindow;

import java.util.ArrayList;
import java.util.List;

public class SportsmanWindow extends AppCompatActivity {

    private PlanAdapter planAdapter;
    private List<PlanDto> planDtoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportsman_window);

        Button addPlanButton = findViewById(R.id.addPlanButton);
        addPlanButton.setOnClickListener(view -> addPlan());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        planDtoList = new ArrayList<>();
        planAdapter = new PlanAdapter(planDtoList, new PlanAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(int position) {
                Toast.makeText(SportsmanWindow.this, "Long click on item at position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(int position) {
                editPlan(position);
            }
        });
        recyclerView.setAdapter(planAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addPlan() {

        PlanDto newPlan = new PlanDto();

        planDtoList.add(newPlan);
        planAdapter.notifyDataSetChanged();

        BackendService.createPlan(newPlan);
    }

    private void editPlan(int position) {
        if (position != RecyclerView.NO_POSITION) {
            PlanDto planDto = planDtoList.get(position);
            Intent intent = new Intent(this, EditPlanWindow.class);
            intent.putExtra("planDto", planDto);
            startActivity(intent);
        }
    }

    public void openTrainingWindow(View view) {
        Intent intent = new Intent(this, AddTrainingWindow.class);
        startActivity(intent);
    }
}
