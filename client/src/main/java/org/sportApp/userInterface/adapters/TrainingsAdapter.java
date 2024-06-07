package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.sportApp.dto.ExerciseDto;
import org.sportApp.dto.TrainingDto;
import org.sportApp.userInterface.R;

import java.util.List;

public class TrainingsAdapter extends BaseAdapter<TrainingDto, BaseAdapter.BaseViewHolder<TrainingDto>> {
    public TrainingsAdapter(List<TrainingDto> items, OnItemClickListener<TrainingDto> listener) {
        super(items, R.layout.item_trainings, listener, TrainingsViewHolder::new);
    }

    public static class TrainingsViewHolder extends BaseViewHolder<TrainingDto> {
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
        }
    }
}