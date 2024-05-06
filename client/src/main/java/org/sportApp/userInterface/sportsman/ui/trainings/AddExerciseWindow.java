package org.sportApp.userInterface.trainings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.sportApp.training.ExerciseDto;
import org.sportApp.userInterface.R;

public class AddExerciseWindow extends Fragment {

    private EditText descriptionEditText;
    private EditText repetitionsEditText;
    private EditText durationEditText;
    private EditText setsEditText;
    private EditText videoUrlEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_add_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        repetitionsEditText = view.findViewById(R.id.repetitionsEditText);
        durationEditText = view.findViewById(R.id.durationEditText);
        setsEditText = view.findViewById(R.id.setsEditText);
        videoUrlEditText = view.findViewById(R.id.videoUrlEditText);

        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveExercise());
    }

    private void saveExercise() {
        String description = descriptionEditText.getText().toString();
        String repetitionsText = repetitionsEditText.getText().toString();
        String durationText = durationEditText.getText().toString();
        String setsText = setsEditText.getText().toString();
        String videoUrl = videoUrlEditText.getText().toString();

        if (description.isEmpty() || repetitionsText.isEmpty() || durationText.isEmpty() || setsText.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int repetitions = Integer.parseInt(repetitionsText);
        int duration = Integer.parseInt(durationText);
        int sets = Integer.parseInt(setsText);

        ExerciseDto exerciseDto = new ExerciseDto();
        exerciseDto.setDescription(description);
        exerciseDto.setRepetitions(repetitions);
        exerciseDto.setDuration(duration);
        exerciseDto.setSets(sets);
        exerciseDto.setVideoUrl(videoUrl);

        Intent intent = new Intent();
        intent.putExtra("exerciseDto", exerciseDto);
        requireActivity().setResult(org.sportApp.userInterface.sportsman.ui.trainings.TrainingFragment.RESULT_OK, intent);
        requireActivity().finish();
    }
}
