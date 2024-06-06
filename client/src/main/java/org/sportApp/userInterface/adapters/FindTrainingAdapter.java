package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.training.ExerciseDto;
import org.sportApp.training.TrainingDto;
import org.sportApp.userInterface.R;

import java.util.List;

public class FindTrainingAdapter extends RecyclerView.Adapter<FindTrainingAdapter.FindTrainingViewHolder> {
    private final List<TrainingDto> trainingsList;
    private final OnItemClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public FindTrainingAdapter(List<TrainingDto> trainingList, OnItemClickListener listener) {
        this.trainingsList = trainingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FindTrainingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_training, parent, false);
        return new FindTrainingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FindTrainingViewHolder holder, int position) {
        TrainingDto training = trainingsList.get(position);
        holder.bind(training, listener, position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return trainingsList.size();
    }

    public interface OnItemClickListener {
        void onItemLongClick(int position);

        void onItemClick(int position);
    }

    public static class FindTrainingViewHolder extends RecyclerView.ViewHolder {
        private final TextView trainingNameTextView;
        private final TextView numberOfExercisesTextView;
        private final TextView totalDurationTextView;

        private final RadioButton trainingRadioButton;

        public FindTrainingViewHolder(View itemView) {
            super(itemView);
            trainingNameTextView = itemView.findViewById(R.id.trainingNameTextView);
            numberOfExercisesTextView = itemView.findViewById(R.id.numberOfExercises);
            totalDurationTextView = itemView.findViewById(R.id.totalDuration);
            trainingRadioButton = itemView.findViewById(R.id.trainingRadioButton);
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull TrainingDto training, OnItemClickListener listener, boolean choosed) {
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

            trainingRadioButton.setChecked(choosed);
            trainingRadioButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
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
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged(); // Обновляем список, чтобы увидеть изменения
    }
}