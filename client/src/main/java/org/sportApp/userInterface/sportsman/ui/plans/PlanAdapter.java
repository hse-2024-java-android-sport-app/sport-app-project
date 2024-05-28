package org.sportApp.userInterface.sportsman.ui.plans;

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
    private final List<PlanDto> planList;
    private final OnItemClickListener listener;

    public PlanAdapter(List<PlanDto> planList, OnItemClickListener listener) {
        this.planList = planList;
        this.listener = listener;
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

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        PlanDto plan = planList.get(position);
        holder.bind(plan, listener);
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public interface OnItemClickListener {
        void onItemLongClick(int position);

        void onItemClick(int position);
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        private final TextView planNameTextView;

        public PlanViewHolder(View itemView) {
            super(itemView);
            planNameTextView = itemView.findViewById(R.id.planNameTextView);
        }

        public void bind(@NonNull PlanDto plan, OnItemClickListener listener) {
            planNameTextView.setText(plan.getName());

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onItemLongClick(getAdapterPosition());
                }
                return true;
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        PlanDto plan = planList.get(position);
        if (plan.isCompleted()) {
            return 1;
        } else {
            return 0;
        }
    }
}
