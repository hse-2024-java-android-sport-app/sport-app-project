package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.training.ExerciseDto;
import org.sportApp.training.TrainingDto;
import org.sportApp.userInterface.R;

import java.util.List;

public class TrainingsAdapter extends RecyclerView.Adapter<TrainingsAdapter.TrainingsViewHolder> {
    private final List<TrainingDto> trainingsList;
    private final OnItemClickListener listener;

    public TrainingsAdapter(List<TrainingDto> trainingList, OnItemClickListener listener) {
        this.trainingsList = trainingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrainingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trainings, parent, false);
        return new TrainingsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingsViewHolder holder, int position) {
        TrainingDto training = trainingsList.get(position);
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

    public static class TrainingsViewHolder extends RecyclerView.ViewHolder {
        private final TextView trainingNameTextView;
        private final TextView numberOfExercisesTextView;
        private final TextView totalDurationTextView;


        public TrainingsViewHolder(View itemView) {
            super(itemView);
            trainingNameTextView = itemView.findViewById(R.id.trainingNameTextView);
            numberOfExercisesTextView = itemView.findViewById(R.id.numberOfExercises);
            totalDurationTextView = itemView.findViewById(R.id.totalDuration);
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull TrainingDto training, OnItemClickListener listener) {
            int totalDuration = 0;
            for (ExerciseDto exercise : training.getExercises()) {
                totalDuration += exercise.getDuration() * exercise.getRepetitions();
            }

            int hours = totalDuration / 3600;
            int minutes = (totalDuration % 3600) / 60;
            int seconds = totalDuration % 60;

            numberOfExercisesTextView.setText("Total number of exercises: " + training.getExercises().size() + ".");
            if (hours > 0) {
                totalDurationTextView.setText("Total duration of training: " + hours + " h " + minutes + " min " + seconds + " sec ");
            }
            else {
                totalDurationTextView.setText("Total duration of training: " + minutes + " min " + seconds + " sec ");
            }

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