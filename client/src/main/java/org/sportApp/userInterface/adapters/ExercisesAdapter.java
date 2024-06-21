package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.sportApp.dto.ExerciseDto;
import org.sportApp.userInterface.R;

import java.util.List;

public class ExercisesAdapter extends BaseAdapter<ExerciseDto, BaseAdapter.BaseViewHolder<ExerciseDto>> {

    public ExercisesAdapter(List<ExerciseDto> items, OnItemClickListener<ExerciseDto> listener) {
        super(items, R.layout.item_exercises, listener, ExercisesViewHolder::new);
    }

    public static class ExercisesViewHolder extends BaseViewHolder<ExerciseDto> {
        private final TextView exerciseNameTextView;
        private final TextView exerciseDescriptionTextView;

        private final TextView exerciseRepetitionsTextView;

        public ExercisesViewHolder(View itemView) {
            super(itemView);
            exerciseNameTextView = itemView.findViewById(R.id.exerciseNameTextView);
            exerciseDescriptionTextView = itemView.findViewById(R.id.exerciseDescriptionTextView);
            exerciseRepetitionsTextView = itemView.findViewById(R.id.exerciseRepetitionsTextView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(@NonNull ExerciseDto exercise, OnItemClickListener<ExerciseDto> listener) {
            super.bind(exercise, listener);
            exerciseNameTextView.setText(exercise.getName());
            exerciseDescriptionTextView.setText("In this training: " + exercise.getDescription() + ".");
            exerciseRepetitionsTextView.setText("Number of repetitions: " + exercise.getRepetitions() + " times.");
        }
    }
}
