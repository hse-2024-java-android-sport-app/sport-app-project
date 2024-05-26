package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.sportApp.training.TrainingEventDto;
import org.sportApp.userInterface.R;

import java.util.List;

public class TrainingEventsAdapter extends RecyclerView.Adapter<TrainingEventsAdapter.TrainingEventsViewHolder> {
    private final List<TrainingEventDto> trainingsList;
    private final OnItemClickListener listener;

    public TrainingEventsAdapter(List<TrainingEventDto> trainingList, OnItemClickListener listener) {
        this.trainingsList = trainingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrainingEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_training_events, parent, false);
        return new TrainingEventsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingEventsViewHolder holder, int position) {
        TrainingEventDto training = trainingsList.get(position);
        holder.bind(training, listener);
    }

    @Override
    public int getItemCount() {
        return trainingsList.size();
    }

    public interface OnItemClickListener {
        void onItemLongClick(int position);

        void onItemClick(int position);
    }

    public static class TrainingEventsViewHolder extends RecyclerView.ViewHolder {
        private final TextView trainingNameTextView;
        private final TextView numberOfExercisesTextView;
        private final TextView totalDurationTextView;


        public TrainingEventsViewHolder(View itemView) {
            super(itemView);
            trainingNameTextView = itemView.findViewById(R.id.trainingNameTextView);
            numberOfExercisesTextView = itemView.findViewById(R.id.numberOfExercises);
            totalDurationTextView = itemView.findViewById(R.id.totalDuration);
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull TrainingEventDto training, OnItemClickListener listener) {

//            trainingNameTextView.setText(training.getName());
//            numberOfExercisesTextView.setText("Total number of exercises: " + training.getExercises().size() + ".");
//            if (hours > 0) {
//                totalDurationTextView.setText("Total duration of training: " + hours + " h " + minutes + " min " + seconds + " sec ");
//            }
//            else {
//                totalDurationTextView.setText("Total duration of training: " + minutes + " min " + seconds + " sec ");
//            }

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
}
