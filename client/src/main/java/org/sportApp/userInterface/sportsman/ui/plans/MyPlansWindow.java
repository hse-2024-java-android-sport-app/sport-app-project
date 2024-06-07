package org.sportApp.userInterface.sportsman.ui.plans;

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

import org.sportApp.registration.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.training.PlanDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.PlanAdapter;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AllPlansWindow extends Fragment {
    private List<PlanDto> currentPlans = new ArrayList<>();
    private List<PlanDto> completedPlans = new ArrayList<>();
    private List<PlanDto> allPlans = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plans, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserDto currentUser = UserManager.getInstance();
        if (currentUser.getType().equals(UserDto.Kind.sportsman)) {
            getAllPlans(UserManager.getInstance().getId());
        }
        else {
            //getAllPlans(UserManager.getInstance().getLastId());
        }
        RecyclerView currentPlanRecyclerView = view.findViewById(R.id.currentPlanRecyclerView);
        currentPlans = allPlans.stream()
                .filter(plan -> !plan.isCompleted())
                .collect(Collectors.toList());

        completedPlans = allPlans.stream()
                .filter(PlanDto::isCompleted)
                .collect(Collectors.toList());
        PlanAdapter currentAdapter = new PlanAdapter(currentPlans, R.layout.item_current_plan, new PlanAdapter.OnItemClickListener<PlanDto>() {
            @Override
            public void onItemClick(int position) {
                showPlan(position, 0);
            }
        });

        ImageButton add = view.findViewById(R.id.addTrainingButton);
        PlanDto planDto = new PlanDto();
        planDto.setSportsmanId(UserManager.getInstance().getId());
        add.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EditPlanWindow.class);
            startActivity(intent);
        });
        currentPlanRecyclerView.setAdapter(currentAdapter);
        currentPlanRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        RecyclerView completedPlansRecyclerView = view.findViewById(R.id.completedPlansRecyclerView);
        PlanAdapter completedAdapter = new PlanAdapter(completedPlans, R.layout.item_completed_plan, new PlanAdapter.OnItemClickListener<PlanDto>() {

            @Override
            public void onItemClick(int position) {
                showPlan(position, 1);
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

    private void showPlan(int position, int type) {
        if (position != RecyclerView.NO_POSITION) {
            PlanDto plan;
            if (type == 0) {
                plan = currentPlans.get(position);
                Log.d("currentPlans", String.valueOf(currentPlans.size()));
            }
            else {
                plan = completedPlans.get(position);
                Log.d("completedPlans", String.valueOf(completedPlans.size()));
            }
            Intent intent = new Intent(requireContext(), OnePlanWindow.class);
            intent.putExtra("planDto", plan);
            startActivity(intent);
        }
    }

    private void getAllPlans(Long userId) {
        BackendService.getAllPlans(userId).thenAccept(resultDto -> {
                    allPlans = resultDto;
                    Log.d("UserType", "resultDto: " + resultDto);
                })
                .exceptionally(e -> {
                    //Toast.makeText(TrainingsWindow.this, "Authorization failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                }).join();
    }
}
