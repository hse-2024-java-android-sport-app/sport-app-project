package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.dto.ExerciseDto;
import org.sportApp.dto.TrainingDto;
import org.sportApp.userInterface.R;

import java.util.List;

public class FindTrainingAdapter extends BaseAdapter<TrainingDto, BaseAdapter.BaseViewHolder<TrainingDto>> {
    private static int selectedPosition = RecyclerView.NO_POSITION;

    public FindTrainingAdapter(List<TrainingDto> items, OnItemClickListener<TrainingDto> listener) {
        super(items, R.layout.item_find_training, listener, FindTrainingViewHolder::new);
    }

    public static class FindTrainingViewHolder extends BaseViewHolder<TrainingDto> {
        //private final TextView trainingNameTextView;
        private final TextView numberOfExercisesTextView;
        private final TextView totalDurationTextView;

        private final RadioButton trainingRadioButton;

        public FindTrainingViewHolder(View itemView) {
            super(itemView);
            //trainingNameTextView = itemView.findViewById(R.id.trainingNameTextView);
            numberOfExercisesTextView = itemView.findViewById(R.id.numberOfExercises);
            totalDurationTextView = itemView.findViewById(R.id.totalDuration);
            trainingRadioButton = itemView.findViewById(R.id.trainingRadioButton);
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull TrainingDto training, OnItemClickListener<TrainingDto> listener) {
            super.bind(training, listener);
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

            trainingRadioButton.setChecked(getAdapterPosition() == selectedPosition);
            trainingRadioButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSelectedPosition(int position) {
        int previousSelectedPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(previousSelectedPosition);
        notifyItemChanged(selectedPosition);
    }
}