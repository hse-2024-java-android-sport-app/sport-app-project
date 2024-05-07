package org.sportApp.userInterface.sportsman.ui.plans;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.userInterface.R;
import org.sportApp.training.PlanDto;

import java.util.ArrayList;
import java.util.List;

public class PlanWindow extends Fragment {

    private final List<PlanDto> currentPlanDtos = new ArrayList<>();
    private final List<PlanDto> completedPlanDtos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plans, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PlanDto planDto1 = new PlanDto();
        planDto1.setName("Test Plan");
        planDto1.setCompleted(false);
        currentPlanDtos.add(planDto1);

        for (int i = 0; i < 20; i++) {
            PlanDto plan = new PlanDto();
            plan.setName("Completed plan " + i);
            plan.setCompleted(true);
            completedPlanDtos.add(plan);
        }

        RecyclerView currentPlanRecyclerView = view.findViewById(R.id.currentPlanRecyclerView);
        PlanAdapter currentAdapter = new PlanAdapter(currentPlanDtos, new PlanAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(int position) {
            }

            @Override
            public void onItemClick(int position) {
            }
        });
        currentPlanRecyclerView.setAdapter(currentAdapter);
        currentPlanRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        RecyclerView completedPlansRecyclerView = view.findViewById(R.id.completedPlansRecyclerView);
        PlanAdapter completedAdapter = new PlanAdapter(completedPlanDtos, new PlanAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(int position) {
            }

            @Override
            public void onItemClick(int position) {
            }
        });
        completedPlansRecyclerView.setAdapter(completedAdapter);
        completedPlansRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}