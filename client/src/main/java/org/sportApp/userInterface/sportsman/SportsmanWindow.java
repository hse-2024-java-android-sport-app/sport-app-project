package org.sportApp.userInterface.sportsman;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.training.PlanDto;
import org.sportApp.userInterface.R;

import java.util.ArrayList;
import java.util.List;

public class SportsmanWindow extends AppCompatActivity implements PlanAdapter.OnItemClickListener {

    private int curPosition = -1;
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
        planAdapter = new PlanAdapter(planDtoList, this);
        recyclerView.setAdapter(planAdapter);
    }

    @Override
    public void onItemLongClick(int position) {
        Toast.makeText(this, "Long click on item at position: " + position, Toast.LENGTH_SHORT).show();
        curPosition = position;
    }

    @SuppressLint("NonConstantResourceId")
    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.plan_item_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.edit_plan) {
                editPlan(curPosition);
                return true;
            } else if (item.getItemId() == R.id.delete_plan) {
                deletePlan(curPosition);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addPlan() {
        PlanDto newPlan = new PlanDto();
        planDtoList.add(newPlan);
        planAdapter.notifyDataSetChanged();
    }

    private void editPlan(int position) {
        PlanDto planDto = planDtoList.get(position);
        Intent intent = new Intent(this, EditPlanWindow.class);
        intent.putExtra("planDto", planDto);
        startActivity(intent);
    }

    private void deletePlan(int position) {
        planDtoList.remove(position);
        planAdapter.notifyItemRemoved(position);
    }
}
