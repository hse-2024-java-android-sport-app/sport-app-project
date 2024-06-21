package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.sportApp.dto.PlanDto;
import org.sportApp.dto.TrainingEventDto;
import org.sportApp.dto.UserDto;
import org.sportApp.model.User;
import org.sportApp.userInterface.R;
import org.sportApp.utils.UserManager;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PlanAdapter extends BaseAdapter<PlanDto, BaseAdapter.BaseViewHolder<PlanDto>> {

    public PlanAdapter(List<PlanDto> items, int planLayout, OnItemClickListener<PlanDto> listener) {
        super(items, planLayout, listener, PlanViewHolder::new);
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (UserManager.getInstance().getType() == UserDto.Kind.sportsman) {
            if (viewType == 0) {
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_current_plan, parent, false);
            } else {
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_completed_plan, parent, false);
            }
        }
        else{
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_current_plan, parent, false);
        }
        return new PlanViewHolder(itemView);
    }

    public static class PlanViewHolder extends BaseViewHolder<PlanDto> {
        private final TextView planNameTextView;
        private final TextView numberOfEventsTextView;
        private final TextView firstTrainingEvent;
        private final TextView lastTrainingEvent;

        private final TextView creationTime;

        public PlanViewHolder(View itemView) {
            super(itemView);
            planNameTextView = itemView.findViewById(R.id.planNameTextView);
            numberOfEventsTextView = itemView.findViewById(R.id.numberOfEvents);
            firstTrainingEvent = itemView.findViewById(R.id.firstDate);
            lastTrainingEvent = itemView.findViewById(R.id.lastDate);
            creationTime = itemView.findViewById(R.id.creationTime);
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull PlanDto plan, OnItemClickListener<PlanDto> listener) {
            super.bind(plan, listener);
            planNameTextView.setText(plan.getName());
            numberOfEventsTextView.setText("Total number of trainings: " + plan.getTrainings().size());

            Optional<TrainingEventDto> earliestEvent = plan.getTrainings().stream()
                    .min(Comparator.comparing(TrainingEventDto::getDate));
            if (earliestEvent.isPresent()) {
                TrainingEventDto earliest = earliestEvent.get();
                firstTrainingEvent.setText("The first training event at: " + earliest.getDate());
            }

            Optional<TrainingEventDto> latestEvent = plan.getTrainings().stream()
                    .max(Comparator.comparing(TrainingEventDto::getDate));
            if (latestEvent.isPresent()) {
                TrainingEventDto latest = latestEvent.get();
                lastTrainingEvent.setText("The last training event at: " + latest.getDate());
            }
            DateTimeFormatter formatter;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && plan.getCreationTime() != null) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                creationTime.setText(plan.getCreationTime().format(formatter));
            } else {
                creationTime.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        PlanDto plan = items.get(position);
        return plan.isCompleted() ? 1 : 0;
    }
}

