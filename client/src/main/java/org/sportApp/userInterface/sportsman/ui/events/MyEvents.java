package org.sportApp.userInterface.sportsman.ui.events;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.sportApp.dto.TrainingEventDto;
import org.sportApp.requests.BackendService;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.adapters.BaseAdapter;
import org.sportApp.userInterface.adapters.EventsAdapter;
import org.sportApp.userInterface.sportsman.ui.overview.BaseFragment;
import org.sportApp.userInterface.sportsman.ui.overview.FragmentWithAddButton;

import java.util.ArrayList;
import java.util.List;

public class MyEvents extends FragmentWithAddButton<TrainingEventDto> {
    private final List<TrainingEventDto> events = new ArrayList<>();

    @Override
    protected Class<?> getAddWindowClass() {
        return TypeSelection.class;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_training_events;
    }

    @Override
    protected int getRecyclerView() {
        return R.id.trainingEventRecyclerView;
    }

    @Override
    protected int getAddButtonId() {
        return R.id.addTrainingEventButton;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //getAllTrainingEvents...
        super.onViewCreated(view, savedInstanceState);
        super.startAddButton(view);
    }

    private void showEvent(int position) {
        super.showItem(position, events, "eventDto");
    }

    private void getAllEvents(Long userId) {
        BackendService.getAllTrainings(userId).thenAccept(resultDto -> {
                    //events = resultDto;
                    Log.d("UserType", "resultDto: " + resultDto);
                })
                .exceptionally(e -> null).join();
    }
}
