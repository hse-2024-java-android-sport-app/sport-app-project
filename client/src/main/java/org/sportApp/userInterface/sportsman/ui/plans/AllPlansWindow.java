package org.sportApp.userInterface.sportsman.ui.plans;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.sportApp.requests.BackendService;
import org.sportApp.training.TrainingDto;
import org.sportApp.userInterface.R;
import org.sportApp.training.PlanDto;
import org.sportApp.userInterface.sportsman.ui.trainings.OneTrainingWindow;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.List;

public class AllPlansWindow extends Fragment {

    private boolean isWindowOpened = false;
    private final List<PlanDto> plans = new ArrayList<>();
    private final List<PlanDto> currentPlans = new ArrayList<>();
    private final List<PlanDto> completedPlans = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plans, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!isWindowOpened) {
//            getAllPlans(UserManager.getInstance().getUserId());
//            PlanDto planDto1 = new PlanDto();
//            planDto1.setName("Test Plan");
//            planDto1.setCompleted(false);
//            currentPlanDtos.add(planDto1);
//
//            for (int i = 0; i < 20; i++) {
//                PlanDto plan = new PlanDto();
//                plan.setName("Completed plan " + i);
//                plan.setCompleted(true);
//                completedPlanDtos.add(plan);
//            }

            isWindowOpened = true;
        }

        RecyclerView currentPlanRecyclerView = view.findViewById(R.id.currentPlanRecyclerView);
        PlanAdapter currentAdapter = new PlanAdapter(currentPlans, new PlanAdapter.OnItemClickListener() {
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
        PlanAdapter completedAdapter = new PlanAdapter(completedPlans, new PlanAdapter.OnItemClickListener() {
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

    private final ActivityResultLauncher<Intent> addPlanLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
            assert result.getData() != null;
            PlanDto planDto = result.getData().getParcelableExtra("planDto");
        }
    });

    private void showPlan(int position) {
        if (position != RecyclerView.NO_POSITION) {
//            PlanDto completedPlan = completedPlanDtos.get(position);
//            Log.d("completedPlans", String.valueOf(completedPlanDtos.size()));
//            Intent intent = new Intent(requireContext(), OnePlanWindow.class);
//            intent.putExtra("planDto", completedPlan);
//            startActivity(intent);
        }
    }

//    private void getAllTrainings(Long userId) {
//        BackendService.getAllPlans(userId).thenAccept(resultDto -> {
//                    plans = resultDto;
//                    Log.d("UserType", "resultDto: " + resultDto);
//                })
//                .exceptionally(e -> {
//                    //Toast.makeText(TrainingsWindow.this, "Authorization failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    return null;
//                }).join();
//    }
}
