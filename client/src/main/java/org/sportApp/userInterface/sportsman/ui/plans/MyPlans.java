package org.sportApp.userInterface.sportsman.ui.plans;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.sportApp.dto.PlanDto;
import org.sportApp.dto.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.PlanAdapter;
import org.sportApp.userInterface.sportsman.ui.overview.FragmentWithAddButton;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyPlans extends FragmentWithAddButton<PlanDto> {
    private List<PlanDto> currentPlans = new ArrayList<>();
    private List<PlanDto> completedPlans = new ArrayList<>();
    private List<PlanDto> allPlans = new ArrayList<>();

    @Override
    protected Class<?> getAddWindowClass() {
        return CreatePlan.class;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_plans;
    }

    @Override
    protected int getRecyclerView() {
        return R.id.currentPlanRecyclerView;
    }

    @Override
    protected int getAddButtonId() {
        return R.id.addTrainingButton;
    }

    @Override
    protected Class<?> getShowWindowClass() {
        return OnePlan.class;
    }

    @Override
    protected BaseAdapter<PlanDto, ? extends BaseAdapter.BaseViewHolder<PlanDto>> createAdapter() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserDto currentUser;
        if (UserManager.getInstance().getType().equals(UserDto.Kind.coach)) {
            currentUser = UserManager.getLastUser();
        }
        else {
            currentUser = UserManager.getInstance();
        }
        Log.d("myTag", "Plans: " + currentUser.getId());
        if (currentUser.getType().equals(UserDto.Kind.sportsman)) {
            getAllPlans(currentUser.getId());
        }
        else {
            getAllPlans(currentUser.getId());
        }
        PlanDto planDto = new PlanDto();
        planDto.setSportsmanId(currentUser.getId());
        super.startAddButton(view);
        currentPlans = allPlans.stream().filter(plan -> !plan.isCompleted()).collect(Collectors.toList());
        completedPlans = allPlans.stream().filter(PlanDto::isCompleted).collect(Collectors.toList());
        PlanAdapter currentAdapter = new PlanAdapter(currentPlans, R.layout.item_current_plan, new PlanAdapter.OnItemClickListener<PlanDto>() {
            @Override
            public void onItemClick(int position) {
                showItem(position, 0);
            }
        });
        super.setUpAdapter(view, R.id.currentPlanRecyclerView, currentAdapter);
        PlanAdapter completedAdapter = new PlanAdapter(completedPlans, R.layout.item_completed_plan, new PlanAdapter.OnItemClickListener<PlanDto>() {

            @Override
            public void onItemClick(int position) {
                showItem(position, 1);
            }
        });
        super.setUpAdapter(view, R.id.completedPlansRecyclerView, completedAdapter);
    }

    private void showItem(int position, int type) {
        if (type == 0) {
            super.showItem(position, currentPlans, "planDto");
            Log.d("currentPlans", String.valueOf(currentPlans.size()));
        } else {
            super.showItem(position, completedPlans, "planDto");
            Log.d("completedPlans", String.valueOf(completedPlans.size()));
        }
    }

    private void getAllPlans(Long userId) {
        Log.d("myTag", "UserId " + userId);
        BackendService.getAllPlans(userId).thenAccept(resultDto -> {
                    allPlans = resultDto;
                    Log.d("UserType", "resultDto: " + resultDto);
        }).exceptionally(e -> null).join();
    }
}
