package org.sportApp.userInterface.sportsman;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.training.PlanDto;
import org.sportApp.userInterface.R;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private List<PlanDto> trainingEvents;
    private final OnItemClickListener listener;

    public PlanAdapter(List<PlanDto> trainingEvents, OnItemClickListener listener) {
        this.trainingEvents = trainingEvents;
        this.listener = listener;
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
        PlanDto event = trainingEvents.get(position);
        holder.eventTextView.setText("Plan " + event.getId());

        holder.itemView.setOnLongClickListener(view -> {
            if (listener != null) {
                listener.onItemLongClick(position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return trainingEvents.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTrainingEvents(List<PlanDto> trainingEvents) {
        this.trainingEvents = trainingEvents;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemLongClick(int position);
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView eventTextView;

        PlanViewHolder(View itemView) {
            super(itemView);
            eventTextView = itemView.findViewById(R.id.eventTextView);
        }
    }
}
