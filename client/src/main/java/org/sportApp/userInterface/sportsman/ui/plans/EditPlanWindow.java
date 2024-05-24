package org.sportApp.userInterface.sportsman.ui.plans;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import org.sportApp.userInterface.R;

import java.time.LocalDate;
import java.util.Calendar;

public class EditPlanWindow extends AppCompatActivity {
    private LocalDate selectedDate;
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
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        selectedDate = LocalDate.of(year1, monthOfYear + 1, dayOfMonth);
                    }
                    Toast.makeText(EditPlanWindow.this, "Date saved", Toast.LENGTH_SHORT).show();
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void showExerciseSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Exercise Option")
                .setItems(R.array.exercise_options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Toast.makeText(EditPlanWindow.this, "New Exercise", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(EditPlanWindow.this, "Existing Exercise", Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
        builder.create().show();
    }

    private void deletePlan() {
        finish();
    }

    private void saveChanges() {
        boolean isCompleted = completedCheckBox.isChecked();
        Toast.makeText(this, "Changes saved!", Toast.LENGTH_SHORT).show();
        Intent resultIntent = new Intent();
//        resultIntent.putExtra("planDto", planDto);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}