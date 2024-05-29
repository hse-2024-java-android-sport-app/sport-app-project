package org.sportApp.userInterface.sportsman.ui.plans;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.requests.BackendService;
import org.sportApp.training.PlanDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.PlanAdapter;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AllPlansWindow extends Fragment {
    private List<PlanDto> plans = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plans, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getAllPlans(UserManager.getInstance().getUserId());
        RecyclerView currentPlanRecyclerView = view.findViewById(R.id.currentPlanRecyclerView);
//        List<PlanDto> currentPlans = plans.stream()
//                .filter(plan -> !plan.isCompleted())
//                .collect(Collectors.toList());
        List<PlanDto> currentPlans = new ArrayList<>();
        PlanDto testPlan = new PlanDto();
        testPlan.setName("Test plan");
        currentPlans.add(new PlanDto());

        List<PlanDto> completedPlans = plans.stream()
                .filter(PlanDto::isCompleted)
                .collect(Collectors.toList());
        PlanAdapter currentAdapter = new PlanAdapter(currentPlans, new PlanAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(int position) {
            }

            @Override
            public void onItemClick(int position) {
            }
        });

        ImageButton add = view.findViewById(R.id.addTrainingButton);
        PlanDto planDto = new PlanDto();
        planDto.setSportsmanId(UserManager.getInstance().getUserId());
        add.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EditPlanWindow.class);
            startActivity(intent);
        });
        currentPlanRecyclerView.setAdapter(currentAdapter);
        currentPlanRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        RecyclerView completedPlansRecyclerView = view.findViewById(R.id.completedPlansRecyclerView);
        PlanAdapter completedAdapter = new PlanAdapter(plans, new PlanAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(int position) {
            }

            @Override
            public void onItemClick(int position) {
                showPlan(position);
            }
        });
        completedPlansRecyclerView.setAdapter(completedAdapter);
        completedPlansRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

//    private final ActivityResultLauncher<Intent> addPlanLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//        if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
//            assert result.getData() != null;
//            PlanDto planDto = result.getData().getParcelableExtra("planDto");
//        }
//    });

    private void showPlan(int position) {
        if (position != RecyclerView.NO_POSITION) {
            PlanDto completedPlan = plans.get(position);
            Log.d("completedPlans", String.valueOf(plans.size()));
            Intent intent = new Intent(requireContext(), OnePlanWindow.class);
            intent.putExtra("planDto", completedPlan);
            startActivity(intent);
        }
    }

    private void getAllPlans(Long userId) {
        BackendService.getAllPlans(userId).thenAccept(resultDto -> {
                    plans = resultDto;
                    Log.d("UserType", "resultDto: " + resultDto);
                })
                .exceptionally(e -> {
                    //Toast.makeText(TrainingsWindow.this, "Authorization failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                }).join();
    }
}
