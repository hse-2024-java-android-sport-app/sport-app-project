package org.sportApp.userInterface.sportsman.ui.trainings;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.training.ExerciseDto;
import org.sportApp.userInterface.R;

import java.util.List;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ExercisesViewHolder> {
    private final List<ExerciseDto> trainingList;
    private final OnItemClickListener listener;

    public ExercisesAdapter(List<ExerciseDto> trainingList, OnItemClickListener listener) {
        this.trainingList = trainingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExercisesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercises, parent, false);
         return new ExercisesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisesViewHolder holder, int position) {
        ExerciseDto exercise = trainingList.get(position);
        holder.bind(exercise, listener);
    }

    @Override
    public int getItemCount() {
        return trainingList.size();
    }

    public interface OnItemClickListener {
        void onItemLongClick(int position);

        void onItemClick(int position);
    }

    public static class ExercisesViewHolder extends RecyclerView.ViewHolder {
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
        public void bind(@NonNull ExerciseDto exercise, OnItemClickListener listener) {
            exerciseNameTextView.setText(exercise.getName());
            exerciseDescriptionTextView.setText("In this exercise: " + exercise.getDescription() + ".");
            exerciseRepetitionsTextView.setText("Number of repetitions: " + exercise.getRepetitions() + " times.");

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
