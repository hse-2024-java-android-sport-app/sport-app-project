package org.sportApp.userInterface.sportsman;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.sportApp.userInterface.R;

public class EditPlanWindow extends AppCompatActivity {

    private CheckBox completedCheckBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plan);

        Button selectDateButton = findViewById(R.id.datePickerButton);
        selectDateButton.setOnClickListener(v -> showDatePickerDialog());

        Button addExerciseButton = findViewById(R.id.addExerciseButton);
        addExerciseButton.setOnClickListener(v -> showExerciseSelectionDialog());

        completedCheckBox = findViewById(R.id.completedCheckBox);

        Button deletePlanButton = findViewById(R.id.deletePlanButton);
        deletePlanButton.setOnClickListener(v -> deletePlan());

        Button saveChangesButton = findViewById(R.id.saveChangesButton);
        saveChangesButton.setOnClickListener(v -> saveChanges());
    }

    private void showDatePickerDialog() {
    }

    private void showExerciseSelectionDialog() {
    }

    private void deletePlan() {
    }

    private void saveChanges() {
        boolean isCompleted = completedCheckBox.isChecked();
        Toast.makeText(this, "Changes saved!", Toast.LENGTH_SHORT).show();
    }
}
