package org.sportApp.userInterface.sportsman;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.userInterface.R;
import org.sportApp.training.TrainingEventDto;
import org.sportApp.training.TrainingDto;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private final List<TrainingEventDto> trainingEvents;

    public PlanAdapter(List<TrainingEventDto> trainingEvents) {
        this.trainingEvents = trainingEvents;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_plan, parent, false);
        return new PlanViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        TrainingEventDto event = trainingEvents.get(position);
        TrainingDto trainingDto = event.getTrainingDto();
        if (trainingDto != null && !trainingDto.getExercises().isEmpty()) {
            String firstExerciseDescription = trainingDto.getExercises().get(0).getDescription();
            holder.planNameTextView.setText(firstExerciseDescription);
        } else {
            holder.planNameTextView.setText("No exercises");
        }
    }

    @Override
    public int getItemCount() {
        return trainingEvents.size();
    }

    static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView planNameTextView;

        PlanViewHolder(View itemView) {
            super(itemView);
            planNameTextView = itemView.findViewById(R.id.planNameTextView);
        }
    }
}
