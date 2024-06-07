package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.sportApp.dto.PlanDto;
import org.sportApp.dto.TrainingEventDto;
import org.sportApp.userInterface.R;

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
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_current_plan, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_completed_plan, parent, false);
        }
        return new PlanViewHolder(itemView);
    }

    public static class PlanViewHolder extends BaseViewHolder<PlanDto> {
        private final TextView planNameTextView;
        private final TextView numberOfEventsTextView;
        private final TextView firstTrainingEvent;
        private final TextView lastTrainingEvent;

        public PlanViewHolder(View itemView) {
            super(itemView);
            planNameTextView = itemView.findViewById(R.id.planNameTextView);
            numberOfEventsTextView = itemView.findViewById(R.id.numberOfEvents);
            firstTrainingEvent = itemView.findViewById(R.id.firstDate);
            lastTrainingEvent = itemView.findViewById(R.id.lastDate);
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull PlanDto plan, OnItemClickListener<PlanDto> listener) {
            super.bind(plan, listener);
            planNameTextView.setText(plan.getName());
            numberOfEventsTextView.setText(String.valueOf(plan.getTrainings().size()));

            Optional<TrainingEventDto> earliestEvent = plan.getTrainings().stream()
                    .min(Comparator.comparing(TrainingEventDto::getDate));
            if (earliestEvent.isPresent()) {
                TrainingEventDto earliest = earliestEvent.get();
                firstTrainingEvent.setText("The first training event at: " + earliest.getDate());
            } else {
                firstTrainingEvent.setText("The first training event at: " + "10.05.2002");
            }

            Optional<TrainingEventDto> latestEvent = plan.getTrainings().stream()
                    .max(Comparator.comparing(TrainingEventDto::getDate));
            if (latestEvent.isPresent()) {
                TrainingEventDto latest = latestEvent.get();
                lastTrainingEvent.setText("The first training event at: " + latest.getDate());
            } else {
                lastTrainingEvent.setText("The first training event at: " + "10.05.2002");
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        PlanDto plan = items.get(position);
        return plan.isCompleted() ? 1 : 0;
    }
}
