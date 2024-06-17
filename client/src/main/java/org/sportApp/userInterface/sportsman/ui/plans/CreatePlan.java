package org.sportApp.userInterface.sportsman.ui.plans;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.sportApp.requests.BackendService;
import org.sportApp.dto.PlanDto;
import org.sportApp.dto.TrainingEventDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.sportsman.ui.events.TypeSelection;
import org.sportApp.utils.UserManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CreatePlan extends AppCompatActivity {
    private final PlanDto planDto = new PlanDto();
    private final List<TrainingEventDto> events = new ArrayList<>();
    private ActivityResultLauncher<Intent> addEventLauncher;

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
        addEventLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    TrainingEventDto eventDto = (TrainingEventDto) result.getData().getSerializableExtra("eventDto");
                    events.add(eventDto);
                }
            }
        });


        Button addEvent = findViewById(R.id.buttonAddTraining);
        addEvent.setOnClickListener(v -> {
            Intent intent = new Intent(CreatePlan.this, TypeSelection.class);
            addEventLauncher.launch(intent);
        });
        saveChangesButton.setOnClickListener(v -> {
            planDto.setSportsmanId(UserManager.getInstance().getId());
            planDto.setName(nameEditText.getText().toString());
            saveChanges(planDto);
        });
    }

    private void saveChanges(@NonNull PlanDto planDto) {
        planDto.setTrainings(events);
        createPlan(planDto);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("planDto", planDto);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void createPlan(@NonNull PlanDto planDto) {
        Log.d("myTag", "TrainingDto " + (planDto.getTrainings().get(0).getTrainingDto() != null));
        BackendService.createPlan(planDto).thenAccept(resultDto -> {
                    planDto.setPlanId(resultDto);
                    Log.d("myTag", "planId: " + resultDto);
                })
                .exceptionally(e -> null).join();
    }
}