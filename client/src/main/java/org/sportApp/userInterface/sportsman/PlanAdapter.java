package org.sportApp.userInterface.sportsman;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.training.TrainingEventDto;
import org.sportApp.userInterface.R;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private final List<TrainingEventDto> trainingEvents;

    public PlanAdapter(List<TrainingEventDto> trainingEvents) {
        this.trainingEvents = trainingEvents;
    }

    @NonNull
    @Override
    public PlanAdapter.PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_plan, parent, false);
        return new PlanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanAdapter.PlanViewHolder holder, int position) {
        TrainingEventDto event = trainingEvents.get(position);
        holder.eventTextView.setText(String.valueOf("Plan " + event.getId()));
    }

    @Override
    public int getItemCount() {
        return trainingEvents.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView eventTextView;

        PlanViewHolder(View itemView) {
            super(itemView);
            eventTextView = itemView.findViewById(R.id.eventTextView);
        }
    }
}
