package org.sportApp.userInterface.sportsman.ui.trainings;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.sportApp.requests.BackendService;
import org.sportApp.training.ExerciseDto;
import org.sportApp.training.TrainingDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.sportsman.ui.exercise.AddExerciseWindow;
import org.sportApp.utils.SessionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTrainingWindow extends Fragment {

    private LocalDate selectedDate;
    private final ArrayList<ExerciseDto> exercises = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_add_training, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button selectDateButton = view.findViewById(R.id.datePickerButton);
        selectDateButton.setOnClickListener(v -> showDatePickerDialog());

        Button addExerciseButton = view.findViewById(R.id.addExerciseButton);
        addExerciseButton.setOnClickListener(v -> showExerciseSelectionDialog());

        Button saveTrainingButton = view.findViewById(R.id.saveTrainingButton);
        saveTrainingButton.setOnClickListener(v -> saveTrainingEvent());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, monthOfYear, dayOfMonth) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                selectedDate = LocalDate.of(year1, monthOfYear + 1, dayOfMonth);
            }
            Toast.makeText(requireContext(), "Date saved", Toast.LENGTH_SHORT).show();
        }, year, month, day);
        datePickerDialog.show();
    }

    private final ActivityResultLauncher<Intent> addExerciseLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
            assert result.getData() != null;
            ExerciseDto exerciseDto = (ExerciseDto) result.getData().getSerializableExtra("exerciseDto");
            assert exerciseDto != null;
            Log.d("exerciseDescription", exerciseDto.getDescription());
        }
    });

    private void showExerciseSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Exercise Option").setItems(R.array.exercise_options, (dialog, which) -> {
            switch (which) {
                case 0:
                    Intent intent = new Intent(requireContext(), AddExerciseWindow.class);
                    addExerciseLauncher.launch(intent);
                    break;
                case 1:
                    Toast.makeText(requireContext(), "Existing Exercise", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        builder.create().show();
    }

    private void saveTrainingEvent() {
        if (selectedDate == null) {
            Toast.makeText(requireContext(), "You didn't select the date", Toast.LENGTH_SHORT).show();
            return;
        }

        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setExercises(exercises);
        SessionManager sessionManager = new SessionManager(requireContext());
        long userId = sessionManager.getUserId();
        trainingDto.setSportsmanId(userId);
        BackendService.addTraining(trainingDto);
        Toast.makeText(requireContext(), "Your training saved!", Toast.LENGTH_SHORT).show();
    }
}
