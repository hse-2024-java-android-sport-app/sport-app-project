package org.sportApp.userInterface.sportsman.ui.plans;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import org.sportApp.dto.PlanDto;
import org.sportApp.dto.TrainingEventDto;
import org.sportApp.dto.UserDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.EventsAdapter;
import org.sportApp.userInterface.sportsman.ui.events.OneEvent;
import org.sportApp.userInterface.sportsman.ui.overview.BaseActivity;
import org.sportApp.utils.UserManager;

import java.util.List;

public class OnePlan extends BaseActivity<TrainingEventDto, PlanDto> {
    @Override
    protected int getLayout() {
        return R.layout.activity_one_plan;
    }

    @Override
    protected List<TrainingEventDto> getItems() {
        return entity.getTrainings();
    }

    @Override
    protected int getRecyclerView() {
        return R.id.availableTrainingsRecyclerView;
    }

    @Override
    protected Class<?> getShowWindowClass() {
        return OneEvent.class;
    }

    @Override
    protected String getName() {
        return "planDto";
    }

    @Override
    protected BaseAdapter<TrainingEventDto, ? extends BaseAdapter.BaseViewHolder<TrainingEventDto>> createAdapter() {
        return new EventsAdapter(items, new EventsAdapter.OnItemClickListener<TrainingEventDto>() {
            @Override
            public void onItemClick(int position) {
                showTrainings(position);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ApplicationTag", "OnePlanWindow: entity is" + entity);
    }

    private void showTrainings(int position) {
        super.showItem(position, "eventDto");
        Log.d("ApplicationTag", "OnePlanWindow: show item");
    }
}
