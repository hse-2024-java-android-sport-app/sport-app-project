package org.sportApp.userInterface.sportsman.ui.plans;

import android.annotation.SuppressLint;
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
    private final List<PlanDto> currentPlans = new ArrayList<>();
    private final List<PlanDto> completedPlans = new ArrayList<>();
    private PlanAdapter currentAdapter;
    private PlanAdapter completedAdapter;
    private List<PlanDto> allPlans = new ArrayList<>();

    private UserDto currentUser;

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

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("myTag", "My Plans " + UserManager.getInstance().getType());
        if (UserManager.getInstance().getType() == (UserDto.Kind.coach)) {
            currentUser = UserManager.getLastUser();
        }
        else {
            currentUser = UserManager.getInstance();
        }
        Log.d("ApplicationTag", "MyPlansWindow: current User's id is " + currentUser.getId());
        getAllPlans(currentUser.getId());

        PlanDto planDto = new PlanDto();
        planDto.setSportsmanId(currentUser.getId());
        super.startAddButton(view);
        Log.d("ApplicationTag", "MyPlansWindow: first plan is completed: " + allPlans.get(0).isCompleted());
        currentPlans.clear();
        completedPlans.clear();
        currentPlans.addAll(allPlans.stream().filter(plan -> !plan.isCompleted()).collect(Collectors.toList()));
        completedPlans.addAll(allPlans.stream().filter(PlanDto::isCompleted).collect(Collectors.toList()));
        currentAdapter = new PlanAdapter(currentPlans, R.layout.item_current_plan, new PlanAdapter.OnItemClickListener<PlanDto>() {
            @Override
            public void onItemClick(int position) {
                showItem(position, 0);
            }
        });
        super.setUpAdapter(view, R.id.currentPlanRecyclerView, currentAdapter);
        Log.d("ApplicationTag", "MyPlans: type is " + UserManager.getInstance().getType());
        if (UserManager.getInstance().getType() == UserDto.Kind.sportsman) {
            completedAdapter = new PlanAdapter(completedPlans, R.layout.item_completed_plan, new PlanAdapter.OnItemClickListener<PlanDto>() {

                @Override
                public void onItemClick(int position) {
                    showItem(position, 1);
                }
            });
            super.setUpAdapter(view, R.id.completedPlansRecyclerView, completedAdapter);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateData() {
        getAllPlans(currentUser.getId());
        if (allPlans != null && !allPlans.isEmpty()) {
            currentPlans.clear();
            completedPlans.clear();
            currentPlans.addAll(allPlans.stream().filter(plan -> !plan.isCompleted()).collect(Collectors.toList()));
            completedPlans.addAll(allPlans.stream().filter(PlanDto::isCompleted).collect(Collectors.toList()));
            if (currentAdapter != null) {
                currentAdapter.notifyDataSetChanged();
            }
            if (completedAdapter != null) {
                completedAdapter.notifyDataSetChanged();
            }
        } else {
            Log.d("ApplicationTag", "MyPlansWindow: allPlans is null or empty.");
        }
    }

    private void showItem(int position, int type) {
        if (UserManager.getInstance().getType() == UserDto.Kind.sportsman) {
            if (type == 0) {
                super.showItem(position, currentPlans, "planDto");
                Log.d("ApplicationTag", "MyPlansWindow: current Plans " + currentPlans);
            } else {
                super.showItem(position, completedPlans, "planDto");
                Log.d("ApplicationTag", "MyPlansWindow: completed Plans " + completedPlans);
            }
        } else {
            super.showItem(position, currentPlans, "planDto");
        }
    }

    private void getAllPlans(Long userId) {
        BackendService.getAllPlans(userId).thenAccept(resultDto -> {
                    allPlans = resultDto;
            Log.d("ApplicationTag", "MyPlansWindow: resultDto is " + resultDto);
        }).exceptionally(e -> {
            Log.e("ApplicationTag", "MyPlansWindow " + e.getMessage(), e);
            return null;
        }).join();
    }
}
