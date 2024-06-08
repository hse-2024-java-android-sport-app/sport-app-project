package org.sportApp.userInterface.sportsman.ui.plans;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.dto.PlanDto;
import org.sportApp.dto.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.PlanAdapter;
import org.sportApp.userInterface.sportsman.ui.overview.BaseListFragment;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyPlansWindow extends BaseListFragment<PlanDto> {
    private List<PlanDto> currentPlans = new ArrayList<>();
    private List<PlanDto> completedPlans = new ArrayList<>();
    private List<PlanDto> allPlans = new ArrayList<>();

    @Override
    protected Class<?> getAddWindowClass() {
        return EditPlanWindow.class;
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
        return OnePlanWindow.class;
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
        UserDto currentUser = UserManager.getInstance();
        if (currentUser.getType().equals(UserDto.Kind.sportsman)) {
            getAllPlans(UserManager.getInstance().getId());
        }
        else {
            //getAllPlans(UserManager.getInstance().getLastId());
        }
        PlanDto planDto = new PlanDto();
        planDto.setSportsmanId(UserManager.getInstance().getId());
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
        }
        else {
            super.showItem(position, completedPlans, "planDto");
            Log.d("completedPlans", String.valueOf(completedPlans.size()));
        }
//        if (position != RecyclerView.NO_POSITION) {
//            PlanDto plan;
//            if (type == 0) {
//                plan = currentPlans.get(position);
//                Log.d("currentPlans", String.valueOf(currentPlans.size()));
//            }
//            else {
//                plan = completedPlans.get(position);
//                Log.d("completedPlans", String.valueOf(completedPlans.size()));
//            }
//            Intent intent = new Intent(requireContext(), OnePlanWindow.class);
//            intent.putExtra("planDto", plan);
//            startActivity(intent);
//        }
    }

    private void getAllPlans(Long userId) {
        BackendService.getAllPlans(userId).thenAccept(resultDto -> {
                    allPlans = resultDto;
                    Log.d("UserType", "resultDto: " + resultDto);
                }).exceptionally(e -> null).join();
    }
}
