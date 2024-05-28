package org.sportApp.userInterface.sportsman.ui.plans;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.sportApp.requests.BackendService;
import org.sportApp.training.ExerciseDto;
import org.sportApp.training.PlanDto;
import org.sportApp.training.TrainingEventDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.sportsman.ui.exercise.AddExerciseWindow;
import org.sportApp.userInterface.sportsman.ui.trainingEvents.AddTrainingEventWindow;
import org.sportApp.userInterface.sportsman.ui.trainingEvents.CreatingTypeSelectionWindow;
import org.sportApp.userInterface.sportsman.ui.trainings.AddTrainingWindow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditPlanWindow extends AppCompatActivity {
    private final PlanDto planDto = new PlanDto();
    private final List<TrainingEventDto> trainings = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plan);
        Button saveChangesButton = findViewById(R.id.buttonSaveChanges);

        EditText nameEditText = findViewById(R.id.editTextName);
        nameEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String planName = textView.getText().toString().trim();
                if (!planName.isEmpty()) {
                    textView.setEnabled(false);
                    saveChangesButton.requestFocus();
                    return true;
                }
            }
            return false;
        });
        planDto.setName(nameEditText.getText().toString());
        Log.d("myTag", "planDto name " + planDto.getName());

        Button addEvent = findViewById(R.id.buttonAddTraining);
        addEvent.setOnClickListener(v -> openAddEventWindow());
        saveChangesButton.setOnClickListener(v -> saveChanges(planDto));
    }

    private void openAddEventWindow() {
        Intent intent = new Intent(this, CreatingTypeSelectionWindow.class);
        addEventLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> addEventLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            assert result.getData() != null;
            TrainingEventDto trainingEventDto = (TrainingEventDto) result.getData().getSerializableExtra("trainingEventDto");
            assert trainingEventDto != null;
            Log.d("myTag", "training Event Dto " + trainingEventDto.getTrainingDto().toString());
        }
    });

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            TrainingEventDto exerciseDto = (TrainingEventDto) data.getSerializableExtra("trainingEventDto");
            if (exerciseDto != null) {
                trainings.add(exerciseDto);
                //adapter.notifyDataSetChanged();
            }
        }
    }

    private void deletePlan() {
        finish();
    }

    private void saveChanges(PlanDto planDto) {
        createPlan(planDto);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("planDto", planDto);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void createPlan(PlanDto planDto) {
        BackendService.createPlan(planDto).thenAccept(resultDto -> {
            planDto.setPlanId(resultDto);
            Log.d("myTag", "plan's id: " + resultDto);
        }).exceptionally(e -> {
            Log.e("myTag", "Failed to create plan.", e);
            return null;
        }).join();
    }
}