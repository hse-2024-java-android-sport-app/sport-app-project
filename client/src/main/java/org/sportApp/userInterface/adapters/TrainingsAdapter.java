package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.sportApp.dto.TrainingDto;
import org.sportApp.userInterface.R;

import java.util.List;

public class TrainingsAdapter extends BaseAdapter<TrainingDto, BaseAdapter.BaseViewHolder<TrainingDto>> {
    public TrainingsAdapter(List<TrainingDto> items, OnItemClickListener<TrainingDto> listener) {
        super(items, R.layout.item_trainings, listener, TrainingsViewHolder::new);
    }

    public static class TrainingsViewHolder extends BaseViewHolder<TrainingDto> {
        private final TextView numberOfExercisesTextView;
        private final TextView totalDurationTextView;


        public TrainingsViewHolder(View itemView) {
            super(itemView);
            numberOfExercisesTextView = itemView.findViewById(R.id.numberOfExercises);
            totalDurationTextView = itemView.findViewById(R.id.totalDuration);
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull TrainingDto training, OnItemClickListener<TrainingDto> listener) {
            super.bind(training, listener);
            training.calculateDuration();

            int hours = training.getHours();

            int minutes = training.getMinutes();

            int seconds = training.getSeconds();

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