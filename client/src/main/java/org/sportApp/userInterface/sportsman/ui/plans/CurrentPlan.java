package org.sportApp.userInterface.sportsman.ui.plans;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.sportApp.dto.PlanDto;
import org.sportApp.dto.TrainingEventDto;
import org.sportApp.dto.UserDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.EventsAdapter;
import org.sportApp.userInterface.sportsman.ui.events.OneEvent;
import org.sportApp.userInterface.sportsman.ui.events.TypeSelection;
import org.sportApp.userInterface.sportsman.ui.overview.FragmentWithAddButton;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CurrentPlan extends FragmentWithAddButton<TrainingEventDto> {
    private List<TrainingEventDto> events = new ArrayList<>();

    private List<PlanDto> allPlans = new ArrayList<>();

    @Override
    protected Class<?> getAddWindowClass() {
        return TypeSelection.class;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_current_plan;
    }

    @Override
    protected int getRecyclerView() {
        return R.id.currentPlanRecyclerView;
    }

    @Override
    protected int getAddButtonId() {
        return R.id.addCurrentPlanButton;
    }

    @Override
    protected Class<?> getShowWindowClass() {
        return OneEvent.class;
    }

    @Override
    protected BaseAdapter<TrainingEventDto, ? extends BaseAdapter.BaseViewHolder<TrainingEventDto>> createAdapter() {
        return new EventsAdapter(events, new EventsAdapter.OnItemClickListener<TrainingEventDto>() {
            @Override
            public void onItemClick(int position) {
                showEvent(position);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        PlanDto currentPlan = getCurrentPlan();
        TextView planTitle = view.findViewById(R.id.currentPlan);
        if (currentPlan != null) {
            planTitle.setText(currentPlan.getName());
            events = currentPlan.getTrainings();
        } else {
            planTitle.setText("Your current plan");
        }
        super.onViewCreated(view, savedInstanceState);
        if (UserManager.getInstance().getType() == UserDto.Kind.sportsman) {
            ImageButton addButton = view.findViewById(R.id.addCurrentPlanButton);
            addButton.setVisibility(View.GONE);
        }
        else {
            super.startAddButton(view);
        }
    }

    private void showEvent(int position) {
        super.showItem(position, events, "eventDto");
    }


    @Nullable
    private PlanDto getCurrentPlan() {
        BackendService.getAllPlans(UserManager.getInstance().getId()).thenAccept(resultDto -> {
            allPlans = resultDto;
            Log.d("ApplicationTag", "CurrentPlanWindow: resultDto is " + resultDto);
        }).exceptionally(e -> {
            Log.e("ApplicationTag", "CurrentPlanWindow " + e.getMessage(), e);
            return null;
        }).join();
        ArrayList<PlanDto> currentPlans = allPlans.stream().filter(plan -> !plan.isCompleted()).collect(Collectors.toCollection(ArrayList::new));
        return !currentPlans.isEmpty() ? currentPlans.get(0) : null;
    }
}