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

public class CompletedPlans extends FragmentWithAddButton<PlanDto> {
    private final List<PlanDto> completedPlans = new ArrayList<>();
    private PlanAdapter completedAdapter;
    private List<PlanDto> allPlans = new ArrayList<>();

    private UserDto currentUser;

    @Override
    protected Class<?> getAddWindowClass() {
        return CreatePlan.class;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_common;
    }

    @Override
    protected int getRecyclerView() {
        return R.id.commonRecyclerView;
    }

    @Override
    protected int getAddButtonId() {
        return R.id.addCommonButton;
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
        } else {
            currentUser = UserManager.getInstance();
        }
        Log.d("ApplicationTag", "CompletedPlans: current User's id is " + currentUser.getId());
        getAllPlans(currentUser.getId());

        PlanDto planDto = new PlanDto();
        planDto.setSportsmanId(currentUser.getId());
        super.startAddButton(view);
        completedPlans.addAll(allPlans.stream().filter(PlanDto::isCompleted).collect(Collectors.toList()));
        Log.d("ApplicationTag", "CompletedPlansWindow: completed plans are " + completedPlans);
        completedAdapter = new PlanAdapter(completedPlans, R.layout.item_current_plan, new PlanAdapter.OnItemClickListener<PlanDto>() {
            @Override
            public void onItemClick(int position) {
                showItem(position);
            }
        });
        super.setUpAdapter(view, R.id.commonRecyclerView, completedAdapter);
        Log.d("ApplicationTag", "CompletedPlans: type is " + UserManager.getInstance().getType());
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
            completedPlans.clear();
            completedPlans.addAll(allPlans.stream().filter(PlanDto::isCompleted).collect(Collectors.toList()));
            if (completedAdapter != null) {
                completedAdapter.notifyDataSetChanged();
            }
        } else {
            Log.d("ApplicationTag", "CompletedPlans: allPlans is null or empty.");
        }
    }

    private void showItem(int position) {
        if (UserManager.getInstance().getType() == UserDto.Kind.sportsman) {
            super.showItem(position, completedPlans, "planDto");
            Log.d("ApplicationTag", "CompletedPlans: current Plans " + completedPlans);
        } else {
            super.showItem(position, completedPlans, "planDto");
        }
    }

    private void getAllPlans(Long userId) {
        BackendService.getAllPlans(userId).thenAccept(resultDto -> {
            allPlans = resultDto;
            Log.d("ApplicationTag", "CompletedPlans: resultDto is " + resultDto);
        }).exceptionally(e -> {
            Log.e("ApplicationTag", "CompletedPlans " + e.getMessage(), e);
            return null;
        }).join();
    }
}
