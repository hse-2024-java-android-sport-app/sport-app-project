package org.sportApp.userInterface.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.sportApp.dto.TrainingDto;
import org.sportApp.dto.TrainingEventDto;
import org.sportApp.userInterface.R;

import java.util.List;

public class EventsAdapter extends BaseAdapter<TrainingEventDto, BaseAdapter.BaseViewHolder<TrainingEventDto>> {

    public EventsAdapter(List<TrainingEventDto> items, OnItemClickListener<TrainingEventDto> listener) {
        super(items, R.layout.item_training_events, listener, TrainingEventsViewHolder::new);
    }
    public static class TrainingEventsViewHolder extends BaseViewHolder<TrainingEventDto> {
        private final TextView numberOfExercisesTextView;
        private final TextView totalDurationTextView;

        private final TextView eventDate;


        public TrainingEventsViewHolder(View itemView) {
            super(itemView);
            numberOfExercisesTextView = itemView.findViewById(R.id.numberOfExercises);
            totalDurationTextView = itemView.findViewById(R.id.totalDuration);
            eventDate = itemView.findViewById(R.id.trainingDate);
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull TrainingEventDto trainingEvent, OnItemClickListener<TrainingEventDto> listener) {
            TrainingDto trainingDto = trainingEvent.getTrainingDto();
            trainingDto.calculateDuration();
            int hours = trainingDto.getHours();

            int minutes = trainingDto.getMinutes();

            int seconds = trainingDto.getSeconds();

            numberOfExercisesTextView.setText("Total number of exercises: " + trainingDto.getExercises().size() + ".");
            if (hours > 0) {
                totalDurationTextView.setText("Total duration of training: " + hours + " h " + minutes + " min " + seconds + " sec ");
            }
            else {
                totalDurationTextView.setText("Total duration of training: " + minutes + " min " + seconds + " sec ");
            }
            eventDate.setText("Date of training: " + trainingEvent.getDate());
        }
    }
}
